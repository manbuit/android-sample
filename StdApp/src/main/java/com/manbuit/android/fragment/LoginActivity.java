package com.manbuit.android.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Loader;
import android.database.Cursor;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    //private RequestQueue queue;

    private StdApp global;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        global = (StdApp) getApplication();

        //queue = Volley.newRequestQueue(LoginActivity.this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitle("  "+getResources().getString(R.string.app_name));
        mToolbar.setSubtitle("    " + getResources().getString(R.string.app_productor));
        mToolbar.setLogo(R.drawable.ic_jyjy);
        mToolbar.setLogoDescription("sss");

        //环境切换功能未完成，该菜单暂不启用
        //mToolbar.inflateMenu(R.menu.menu_login);

        //setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                //Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                switch (item.getItemId()){
                    case R.id.action_config:
                        final EditText editText = new EditText(LoginActivity.this);
                        editText.setHint("示例：http://192.168.1.70:8080");
                        editText.setEnabled(false);
                        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 3:
                                        editText.setEnabled(true);
                                        break;
                                    default:
                                        editText.setEnabled(false);
                                        break;
                                }
                            }
                        };
                        AlertDialog.OnClickListener onOK = new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(LoginActivity.this, String.valueOf(which), Toast.LENGTH_SHORT).show();
                            }
                        };
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("选择服务端")
                                .setSingleChoiceItems(new String[]{"正式环境", "测试环境", "开发环境", "自定义"}, 0, onClickListener)
                                .setView(editText)
                                .setPositiveButton("确定", onOK)
                                .setNegativeButton("取消", null)
                                .show();
                        break;
                    default: break;
                }
                return true;
            }
        });

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        String i_userName = getIntent().getStringExtra("userName");
        String i_passWord = getIntent().getStringExtra("passWord");

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);;
        UpdateAPK.update(LoginActivity.this, false);

        if(!TextUtils.isEmpty(i_userName)){
            mUsernameView.setText(i_userName);
            mPasswordView.setText(i_passWord);
            this.attemptLogin();
        }
        else{
            restoreUsernameAndPassword();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    private void restoreUsernameAndPassword() {

        String key_username = getResources().getString(R.string.key_username);
        String key_password = getResources().getString(R.string.key_password);

        SharedPreferences sp = global.getMySP();
        String username = sp.getString(key_username, null);
        String password = sp.getString(key_password, null);

        mUsernameView.setText(username);
        mPasswordView.setText(password);
    }

    private void saveUsernameAndPassword() {

        String key_username = getResources().getString(R.string.key_username);
        String key_password = getResources().getString(R.string.key_password);

        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        SharedPreferences sp = global.getMySP();

        SharedPreferences.Editor e = sp.edit();
        e.putString(key_username,username);
        e.putString(key_password, password);
        e.commit();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        saveUsernameAndPassword();

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            //Toast.makeText(LoginActivity.this, "开始登录...", Toast.LENGTH_SHORT).show();

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    //editText.getText().toString(),
                    global.getLoginCheckUrl(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject result = new JSONObject(s);
                                JSONObject data = result.getJSONObject("data");
                                Iterator it = data.keys();
                                while (it.hasNext()) {
                                    String key = (String) it.next();
                                    global.getMyContext().put(key,data.get(key));
                                }
                                //Toast.makeText(LoginActivity.this,global.getMyContext().get("token").toString(),Toast.LENGTH_SHORT).show();
                                showProgress(false);

                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                //Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "用户名或密码不正确",Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            showProgress(false);
                            Toast.makeText(LoginActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    //在这里设置需要post的参数
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("username",
                            (!username.toLowerCase().equals("suadmin") && !username.toLowerCase().equals("admin") && !username.contains("@jsciq.gov.cn"))
                                    ? String.format("%s@jsciq.gov.cn",username)
                                    : username
                    );
                    map.put("password", password);
                    return map;
                }
            };
            global.getRequestQueue().add(stringRequest);

            //Toast.makeText(LoginActivity.this, "等待登录结果...", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
            showExitTips();
            return false;
        }
        return false;
    }

    private void showExitTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("确定退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

