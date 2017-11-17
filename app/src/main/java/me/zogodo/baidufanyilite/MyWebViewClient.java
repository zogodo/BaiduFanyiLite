package me.zogodo.baidufanyilite;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by zogod on 17/2/19.
 */
public class MyWebViewClient extends WebViewClient
{
    MyWebView mywebview;

    public MyWebViewClient(MyWebView mywebview) {
        this.mywebview = mywebview;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        // Log.e("sho__ url", url);
        WebView.HitTestResult hit = view.getHitTestResult();
        if (hit.getExtra() != null)
        {
            view.stopLoading();
            view.setVisibility(view.INVISIBLE);

            LayoutInflater inflater = (LayoutInflater) this.mywebview.activity.getSystemService(LAYOUT_INFLATER_SERVICE);
            RelativeLayout rel_layout = (RelativeLayout) inflater.inflate(R.layout.my_web_layout, null);
            MyWebView new_mywebview = (MyWebView)rel_layout.findViewById(R.id.web_show0);
            new_mywebview.activity = this.mywebview.activity;
            new_mywebview.webview_stack = this.mywebview.webview_stack;
            //new_mywebview.first_load = false;
            new_mywebview.VebViewInit(url, this.mywebview.myjs, this.mywebview.mycss);
            this.mywebview.webview_stack.push(new_mywebview);

            new_mywebview.StartView();
        }
        return false;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        // Log.e("star_ url", url);
        super.onPageStarted(view, url, favicon);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onPageFinished(WebView view, String url)
    {
        // Log.e("ended url", url);
        this.mywebview.loadUrl("javascript:" + this.mywebview.myjs);
        this.mywebview.injectCSS();

        this.mywebview.setVisibility(this.mywebview.VISIBLE);

        super.onPageFinished(view, url);
    }

}