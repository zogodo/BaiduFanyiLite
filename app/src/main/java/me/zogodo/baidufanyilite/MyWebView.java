package me.zogodo.baidufanyilite;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;

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
    public SwipeRefreshLayout swipe_refresh_layout = null;
    public int screen_witdh;
    public int screen_height;
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
        Object ob = this.getParent();
        if (ob != null)
        {
            this.swipe_refresh_layout = (SwipeRefreshLayout)ob;
            this.BindFresh(this.swipe_refresh_layout);
            RelativeLayout rel_layout = (RelativeLayout)this.swipe_refresh_layout.getParent();
            this.activity.setContentView(rel_layout);
        }
        else
        {
            this.activity.setContentView(this);
        }
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
        this.screen_witdh = display.getWidth();
        this.screen_height = display.getHeight();

        this.myjs = js;
        this.mycss = css;
        this.loadUrl(url);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void BindFresh(final SwipeRefreshLayout swipeRefresh)
    {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                //重新加载刷新页面
                String url = MyWebView.this.getUrl();
                MyWebView.this.loadUrl(url);
            }
        });
        // swipeRefresh.post(new Runnable() {
        //     @Override
        //     public void run() {
        //         swipeRefresh.setRefreshing(true);
        //         //MyWebView.this.loadUrl(MyWebView.this.getUrl());
        //     }
        // });
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light
        );

        this.setWebChromeClient(new WebChromeClient() // alert() 要用
        {
            public boolean onConsoleMessage(ConsoleMessage cm)
            {
                Log.d("MyApplication", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }

            public void onProgressChanged(WebView view, int newProgress)
            {
                if (newProgress == 100)
                {
                    //隐藏进度条
                    swipeRefresh.setRefreshing(false);
                }
                else if (!swipeRefresh.isRefreshing())
                {
                    swipeRefresh.setRefreshing(true);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (swipe_refresh_layout == null)
        {
            return super.onTouchEvent(event);
        }
        float y = event.getY();
        float x = event.getX();
        if (x > this.screen_witdh/2 - x/6 && x < this.screen_witdh/2 + x/6)
        {
            swipe_refresh_layout.setEnabled(true);
        }
        else
        {
            swipe_refresh_layout.setEnabled(false);
        }
        return super.onTouchEvent(event);
    }

}


