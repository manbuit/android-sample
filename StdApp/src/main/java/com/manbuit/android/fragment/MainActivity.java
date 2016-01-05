package com.manbuit.android.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manbuit.android.fragment.dataRequest.LoadData;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    Fragment[] fragments = new Fragment[3];
    Map<String, Bundle> queryConds = new HashMap<>();

    TextView tvAccount;
    TextView tvFavorite;
    TextView tvStdDB;
    Toolbar toolbar;

    //SearchView searchView;

    StdApp global;

    //RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        global = (StdApp) getApplication();

        //queue = Volley.newRequestQueue(MainActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("JYJY");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_main);
        //setSupportActionBar(toolbar);


        MenuItem queryMenu = toolbar.getMenu().findItem(R.id.action_query);
        queryMenu.setIcon(
                new IconicsDrawable(MainActivity.this)
                        .icon(GoogleMaterial.Icon.gmd_search)
                        .color(Color.BLACK)
                        .sizeDp(20)
        );


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
                        UpdateAPK.update(MainActivity.this, true);
                        break;
                    case R.id.action_logout:
                        //Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        }
                        break;
                    case R.id.action_query:
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("action_search");
                        builder.setMessage("action_search:");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create().show();*/
                        {

                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, StdQueryDialogActivity.class);

                            // TODO 将当前fregment的查询参数传递到查询对话框中
                            /*intent.putExtra("status","作废")
                                    .putExtra("category","ASTM标准")
                                    .putExtra("codename", "123");*/
                            intent.putExtras(queryConds.get(getCurrentFragment().getClass().getName()));

                            //startActivity(intent);
                            startActivityForResult(intent,0);
                        }
                        break;
                    default: break;
                }
                return true;
            }
        });

        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvFavorite = (TextView) findViewById(R.id.tvFavorite);
        tvStdDB = (TextView) findViewById(R.id.tvStdDB);

        tvAccount.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);
        tvStdDB.setOnClickListener(this);

        setTabSelection(0);
    }

    private Fragment getCurrentFragment() {
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible()){
                return fragment;
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                //Toast.makeText(MainActivity.this,status, Toast.LENGTH_SHORT).show();
                queryConds.put(getCurrentFragment().getClass().getName(),data.getExtras());
                ((LoadData)getCurrentFragment()).loadData(queryConds.get(getCurrentFragment().getClass().getName()));
                break;
            default:
                break;
        }
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideAllFragments(transaction);

        Bundle queryCond = new Bundle();
        queryCond.putString("status","");
        queryCond.putString("category","");
        queryCond.putString("codename", "");
        switch (index) {
            case 0:
                //tvStdDB.setBackgroundColor(Color.WHITE);
                tvStdDB.setBackgroundColor(Color.parseColor(BG_COLOR_FOCUS));
                if (stdDBFragment == null) {
                    stdDBFragment = new StdDBFragment();
                    fragments[0] = stdDBFragment;
                    queryConds.put(StdDBFragment.class.getName(),queryCond);
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
                    fragments[1] = accountFragment;
                    queryConds.put(AccountFragment.class.getName(),queryCond);
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
                    fragments[2] = favoriteFragment;
                    queryConds.put(FavoriteFragment.class.getName(),queryCond);
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
    private void hideAllFragments(FragmentTransaction transaction) {
        for(Fragment fragment : fragments){
            if(fragment!=null) {
                transaction.hide(fragment);
            }
        }
        /*if (accountFragment != null) {
            transaction.hide(accountFragment);
        }
        if (favoriteFragment != null) {
            transaction.hide(favoriteFragment);
        }
        if (stdDBFragment != null) {
            transaction.hide(stdDBFragment);
        }*/
    }
}