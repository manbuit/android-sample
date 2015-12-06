package com.manbuit.android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.OrderBy;
import com.manbuit.android.fragment.utils.DownloadFileAsync;
import com.manbuit.android.fragment.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity
        //extends Activity
        extends AppCompatActivity
        implements View.OnClickListener/*, AppCompatCallback*/ {

    final static String BG_COLOR = "#26C6DA";
    final static String BG_COLOR_FOCUS = "#80DEEA";

    AccountFragment accountFragment;
    FavoriteFragment favoriteFragment;
    StdDBFragment stdDBFragment;

    TextView tvAccount;
    TextView tvFavorite;
    TextView tvStdDB;
    Toolbar toolbar;

    SearchView searchView;

    StdApp global;

    RequestQueue queue;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        global = (StdApp) getApplication();

        queue = Volley.newRequestQueue(MainActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("JYJY");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_main);
        //setSupportActionBar(toolbar);

        /*searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
        //searchView.setIconified(false); //处于显示SearchView的状态
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return true;
            }
        });*/

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                //Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                switch (item.getItemId()){
                    case R.id.action_update:
                        //Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        UpdateAPK.update(MainActivity.this,global,queue, true);
                        break;
                    case R.id.action_logout:
                        //Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    default: break;
                }
                return true;
            }
        });

        fragmentManager = getFragmentManager();

        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvFavorite = (TextView) findViewById(R.id.tvFavorite);
        tvStdDB = (TextView) findViewById(R.id.tvStdDB);

        tvAccount.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);
        tvStdDB.setOnClickListener(this);

        setTabSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStdDB:
                //Toast.makeText(MainActivity.this, "tvStdDB", Toast.LENGTH_SHORT).show();
                setTabSelection(0);
                toolbar.setTitle("标准总库");
                break;
            case R.id.tvAccount:
                //Toast.makeText(MainActivity.this, "tvAccount", Toast.LENGTH_SHORT).show();
                setTabSelection(1);
                toolbar.setTitle("部门台账");
                break;
            case R.id.tvFavorite:
                //Toast.makeText(MainActivity.this, "tvFavorite", Toast.LENGTH_SHORT).show();
                setTabSelection(2);
                toolbar.setTitle("我的收藏");
                break;
            default:
                Toast.makeText(MainActivity.this, "nothing", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                //tvStdDB.setBackgroundColor(Color.WHITE);
                tvStdDB.setBackgroundColor(Color.parseColor(BG_COLOR_FOCUS));
                if (stdDBFragment == null) {
                    stdDBFragment = new StdDBFragment();
                    transaction.add(R.id.content, stdDBFragment);
                } else {
                    transaction.show(stdDBFragment);
                }
                break;
            case 1:
                //tvAccount.setBackgroundColor(Color.WHITE);
                tvAccount.setBackgroundColor(Color.parseColor(BG_COLOR_FOCUS));
                if (accountFragment == null) {
                    accountFragment = new AccountFragment();
                    transaction.add(R.id.content, accountFragment);
                } else {
                    transaction.show(accountFragment);
                }
                break;
            case 2:
                //tvFavorite.setBackgroundColor(Color.WHITE);
                tvFavorite.setBackgroundColor(Color.parseColor(BG_COLOR_FOCUS));
                if (favoriteFragment == null) {
                    favoriteFragment = new FavoriteFragment();
                    transaction.add(R.id.content, favoriteFragment);
                } else {
                    transaction.show(favoriteFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
        //toolbar.setTitle("部门台账"); //toolbar为空
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tvAccount.setBackgroundColor(Color.parseColor(BG_COLOR));
        tvFavorite.setBackgroundColor(Color.parseColor(BG_COLOR));
        tvStdDB.setBackgroundColor(Color.parseColor(BG_COLOR));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (accountFragment != null) {
            transaction.hide(accountFragment);
        }
        if (favoriteFragment != null) {
            transaction.hide(favoriteFragment);
        }
        if (stdDBFragment != null) {
            transaction.hide(stdDBFragment);
        }
    }

    // TODO 实现菜单
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/


/*    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }*/
}
