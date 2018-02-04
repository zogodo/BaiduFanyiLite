package me.zogodo.baidufanyilite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{

    //region 成员变量
    MyWebView webview_baidu;
    String url_baidu_fy = "https://fanyi.baidu.com/#auto/zh/";
    String js_baidu;
    String css_baidu;
    long exitTime = 0;
    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        //region 初始化 js css
        try
        {
            js_baidu = ActivityReadFile.readTextFileFromRaw(this, R.raw.js_baidu);
            css_baidu = ActivityReadFile.readTextFileFromRaw(this, R.raw.css_baidu);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //endregion

        webview_baidu = new MyWebView(this);
        webview_baidu.VebViewInit(url_baidu_fy, js_baidu, css_baidu);
        this.setContentView(webview_baidu);
    }

    @Override
    public void onBackPressed()
    {
        /*
        if (webview_baidu.canGoBack())
        {
            webview_baidu.goBack();
            return;
        }
        */
        // 判断是否可后退，是则后退，否则退出程序
        if (((System.currentTimeMillis() - exitTime) > 3000))
        {
            Toast.makeText(getApplicationContext(), "再按一次返回 退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else
        {
            finish();
            System.exit(0);
        }
    }

}
