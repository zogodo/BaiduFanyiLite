package me.zogodo.baidufanyilite;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by zogod on 17/2/19.
 */
public class MyWebView extends WebView
{
    //region 共有变量
    public String myjs = "";
    public String mycss = "";
    //endregion

    public MyWebView(Context cnt)
    {
        super(cnt);
        this.setVisibility(this.INVISIBLE);
    }

    public void injectCSS()
    {
        this.loadUrl("javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var style = document.createElement('style');" +
                "style.type = 'text/css';" +
                "style.innerHTML = '" + this.mycss + "';" +
                "parent.appendChild(style)" +
                "})()");
    }

    public void VebViewInit(String url, String js, String css)
    {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setSupportMultipleWindows(false);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.setWebViewClient(new MyWebViewClient(this));

        //region 这两个是要在 Chrome inspect 调试时用的
        this.setWebChromeClient(new WebChromeClient() // alert() 要用
        {
            public boolean onConsoleMessage(ConsoleMessage cm)
            {
                Log.d("MyApplication", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }
        });
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.KITKAT)
        {
            this.setWebContentsDebuggingEnabled(true);
        }
        //endregion

        this.myjs = js;
        this.mycss = css;
        this.loadUrl(url);
    }
}


