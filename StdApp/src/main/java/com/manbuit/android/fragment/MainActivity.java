package com.manbuit.android.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AccountFragment accountFragment;
    FavoriteFragment favoriteFragment;
    StdDBFragment stdDBFragment;
    OtherFragment otherFragment;

    TextView tvAccount;
    TextView tvFavorite;
    TextView tvStdDB;
    TextView tvOther;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvFavorite = (TextView) findViewById(R.id.tvFavorite);
        tvStdDB = (TextView) findViewById(R.id.tvStdDB);
        tvOther = (TextView) findViewById(R.id.tvOther);

        tvAccount.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);
        tvStdDB.setOnClickListener(this);
        tvOther.setOnClickListener(this);

        setTabSelection(0);
    }

    private void initView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAccount:
                //Toast.makeText(MainActivity.this, "tvAccount", Toast.LENGTH_SHORT).show();
                setTabSelection(0);
                break;
            case R.id.tvFavorite:
                //Toast.makeText(MainActivity.this, "tvFavorite", Toast.LENGTH_SHORT).show();
                setTabSelection(1);
                break;
            case R.id.tvStdDB:
                //Toast.makeText(MainActivity.this, "tvStdDB", Toast.LENGTH_SHORT).show();
                setTabSelection(2);
                break;
            case R.id.tvOther:
                //Toast.makeText(MainActivity.this, "tvOther", Toast.LENGTH_SHORT).show();
                setTabSelection(3);
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
                tvAccount.setBackgroundColor(Color.WHITE);
                if (accountFragment == null) {
                    accountFragment = new AccountFragment();
                    transaction.add(R.id.content, accountFragment);
                } else {
                    transaction.show(accountFragment);
                }
                break;
            case 1:
                tvFavorite.setBackgroundColor(Color.WHITE);
                if (favoriteFragment == null) {
                    favoriteFragment = new FavoriteFragment();
                    transaction.add(R.id.content, favoriteFragment);
                } else {
                    transaction.show(favoriteFragment);
                }
                break;
            case 2:
                tvStdDB.setBackgroundColor(Color.WHITE);
                if (stdDBFragment == null) {
                    stdDBFragment = new StdDBFragment();
                    transaction.add(R.id.content, stdDBFragment);
                } else {
                    transaction.show(stdDBFragment);
                }
                break;
            case 3:
                tvOther.setBackgroundColor(Color.WHITE);
                if (otherFragment == null) {
                    otherFragment = new OtherFragment();
                    transaction.add(R.id.content, otherFragment);
                } else {
                    transaction.show(otherFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tvAccount.setBackgroundColor(Color.parseColor("#AACCEE"));
        tvFavorite.setBackgroundColor(Color.parseColor("#AACCEE"));
        tvStdDB.setBackgroundColor(Color.parseColor("#AACCEE"));
        tvOther.setBackgroundColor(Color.parseColor("#AACCEE"));
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
        if (otherFragment != null) {
            transaction.hide(otherFragment);
        }
    }
}
