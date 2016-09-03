package com.meloduet.android.mytypecho;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LayoutInflater inflater;
    private int views[] = new int[]{R.layout.post_view, R.layout.list_view};
    private int selected = 0;
    private ViewFlipper viewFlipper;
    private Activity mainView = this;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initConfig();
        inflater = LayoutInflater.from(this);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        Button saveButton = (Button) findViewById(R.id.setting_save);
        Log.d("onClick", saveButton.toString());
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick", view.toString());
                SettingData.url = ((EditText) mainView.findViewById(R.id.setting_url)).getText().toString();
                SettingData.apikey = ((EditText) mainView.findViewById(R.id.setting_apikey)).getText().toString();
                SettingData.admkey = ((EditText) mainView.findViewById(R.id.setting_admkey)).getText().toString();
                SettingData.username = ((EditText) mainView.findViewById(R.id.setting_username)).getText().toString();
                SettingData.password = ((EditText) mainView.findViewById(R.id.setting_password)).getText().toString();

                SharedPreferences.Editor editor = getSharedPreferences("data",
                        MODE_PRIVATE).edit();
                editor.putString("url", SettingData.url);
                editor.putString("apikey", SettingData.apikey);
                editor.putString("admkey", SettingData.admkey);
                editor.putString("username", SettingData.username);
                editor.putString("password", SettingData.password);
                editor.commit();
                Toast.makeText(mainView, "保存成功!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button publishButton = (Button) findViewById(R.id.publish);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            PostArticle(((EditText)mainView.findViewById(R.id.title_text)
            ).getText().toString(),((EditText)mainView.findViewById(R.id.content_text)).getText().toString())
            ;
                Toast.makeText(mainView, "已提交请求!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void initConfig() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SettingData.url = pref.getString("url", "");
        SettingData.apikey = pref.getString("apikey", "");
        SettingData.admkey = pref.getString("admkey", "");
        SettingData.username = pref.getString("username", "");
        SettingData.password = pref.getString("password", "");
        ((EditText) findViewById(R.id.setting_url)).setText(SettingData.url);
        ((EditText) findViewById(R.id.setting_apikey)).setText(SettingData.apikey);
        ((EditText) findViewById(R.id.setting_admkey)).setText(SettingData.admkey);
        ((EditText) findViewById(R.id.setting_username)).setText(SettingData.username);
        ((EditText) findViewById(R.id.setting_password)).setText(SettingData.password);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            selected = 2;
            viewFlipper.setDisplayedChild(selected);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.slide_publish) {
            selected = 0;
            viewFlipper.setDisplayedChild(selected);
        } else if (id == R.id.slide_list) {
            selected = 1;
            viewFlipper.setDisplayedChild(selected);
        }/* else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }*/ else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    String text = (String) msg.obj;
                    if (text.length()>200)
                    {
                        Toast.makeText(MainActivity.this, "发布成功!", Toast.LENGTH_SHORT).show();
                        ((EditText)mainView.findViewById(R.id.title_text)
                        ).setText("");
                        ((EditText)mainView.findViewById(R.id.content_text)).setText("");
                    }else{
                        Toast.makeText(MainActivity.this, "发布失败, 可能是参数错误\n" + text, Toast.LENGTH_LONG).show();
                    }

                    break;
                case 0:
                    Toast.makeText(MainActivity.this, "发起请求失败, 可能是网络错误或Url填错!", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    };
    String path = "";
    public void PostArticle(String title,String text) {
        path = SettingData.url.trim()+"?";

            path+="action=post";
            path+="&apikey=" + java.net.URLEncoder.encode(SettingData.apikey);
            path+="&admkey=" +  java.net.URLEncoder.encode(SettingData.admkey);
            path+="&user=" + java.net.URLEncoder.encode(SettingData.username);
            path+="&password=" + java.net.URLEncoder.encode(SettingData.password);
        path+="&title=" + java.net.URLEncoder.encode(title);
        path+="&text=" + java.net.URLEncoder.encode(text+"\n------\n"+"——来自Meloduet MyTypecho Android客户端（基于QuickApi插件）");
        Log.d("Post",path);
        /*
        * localhost/te/index.php/quickapi?apikey=EIuP3xOwzUe4yjRG&admkey=PIg7LAxxaTiIq6DP&action=post&user=admin&password=admin&title=test&text=text*/
        //访问网络，把html源文件下载下来
        new Thread(){
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");//声明请求方式 默认get
                    //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.3.3; zh-cn; sdk Build/GRI34) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/6.0.0.57_r870003.501 NetType/internet");
                    int code = conn.getResponseCode();
                    if(code ==200){
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readStream(is);

                        Message msg = Message.obtain();//减少消息创建的数量
                        msg.obj = result;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Message msg = Message.obtain();//减少消息创建的数量
                    msg.what = 0;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            };
        }.start();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.meloduet.android.mytypecho/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.meloduet.android.mytypecho/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
