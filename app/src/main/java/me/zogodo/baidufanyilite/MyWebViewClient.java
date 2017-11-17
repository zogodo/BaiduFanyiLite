package me.zogodo.baidufanyilite;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by zogod on 17/2/19.
 */
public class MyWebViewClient extends WebViewClient
{
    MyWebView mywebview;

    public MyWebViewClient(MyWebView mywebview)
    {
        this.mywebview = mywebview;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onPageFinished(WebView view, String url)
    {
        this.mywebview.loadUrl("javascript:" + this.mywebview.myjs);
        this.mywebview.injectCSS();

        this.mywebview.setVisibility(this.mywebview.VISIBLE);

        super.onPageFinished(view, url);
    }

}