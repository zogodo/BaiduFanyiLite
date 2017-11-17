package me.zogodo.baidufanyilite;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.Stack;

/**
 * Created by zogod on 17/2/19.
 */
public class MyWebView extends WebView
{
    //region 共有变量
    public Activity activity;
    public Stack<MyWebView> webview_stack;
    public boolean first_view = false;
    public String myjs = "";
    public String mycss = "";
    //endregion

    //region 构造器
    public MyWebView(Activity activity, Stack<MyWebView> webview_stack)
    {
        super(activity);
        this.activity = activity;
        this.webview_stack = webview_stack;
        this.webview_stack.push(this);

        this.setVisibility(this.INVISIBLE);
    }
    public MyWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.activity = (Activity)context;
    }
    //endregion

    //region goBack
    public boolean canGoBack()
    {
        return this.webview_stack.size() > 1;
    }
    public void goBack()
    {
        this.webview_stack.pop();
        MyWebView old_mywebview = this.webview_stack.peek();

        old_mywebview.StartView();
    }
    //endregion

    public void StartView()
    {
        this.activity.setContentView(this);
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

        WindowManager wm = (WindowManager)this.activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        this.myjs = js;
        this.mycss = css;
        this.loadUrl(url);
    }
}


