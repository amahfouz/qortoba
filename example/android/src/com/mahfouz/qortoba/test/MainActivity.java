package com.mahfouz.qortoba.test;

import com.mahfouz.qortoba.AndroidQortobaWebView;
import com.mahfouz.qortoba.QortobaAngularServiceProxy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public final class MainActivity extends Activity {

	private static final String URL = "file:///android_asset/index.html";
    private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView1);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
            	if (! URL.equals(url))
            		return;

            	Log.w("QORTOBA", "Page load done!");

                AndroidQortobaWebView wrappedWebView
                    = new AndroidQortobaWebView(webView);

                // create Angular service proxy

                AlertServiceJsApi serviceProxy
                    = QortobaAngularServiceProxy.create
                    (AlertServiceJsApi.class, "AlertService", wrappedWebView);

                // invoke using the proxy

                serviceProxy.show("Hello!");
            }
        });

        WebView.setWebContentsDebuggingEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        // add JS interfaces

        Log.w("QORTOBA", "Loading file into web view.");

    	webView.addJavascriptInterface(new Test(), "test");
    	webView.loadData("", "text/html", null);
        webView.loadUrl(URL);
	}


	private final class Test {

		@JavascriptInterface
		public void greet(String name) {
			Log.w("QORDOBA", "Greetings" + name);
		}

	}

	/**
	 * API of the alert service used for generating dynamic proxxy.
	 */
	public interface AlertServiceApi {

	    void show(String message);
	}
}
