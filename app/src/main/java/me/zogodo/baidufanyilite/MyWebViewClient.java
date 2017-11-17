package me.zogodo.baidufanyilite;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
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
        WebView.HitTestResult hit = view.getHitTestResult();
        if (hit.getExtra() != null)
        {
            view.stopLoading();

            MyWebView new_mywebview = new MyWebView(this.mywebview.activity, this.mywebview.webview_stack);
            new_mywebview.VebViewInit(url, this.mywebview.myjs, this.mywebview.mycss);
            new_mywebview.webview_stack.push(new_mywebview);

            new_mywebview.StartView();
        }
        return false;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        super.onPageStarted(view, url, favicon);
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