package com.mahfouz.qortoba.test;

import com.mahfouz.qortoba.AndroidQortobaClient;
import com.mahfouz.qortoba.QortobaAngularProxy;

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

                AndroidQortobaClient client
                    = new AndroidQortobaClient(webView);

                // invoke using the client

                client.invoke("AlertService", "show", new Object[] {"Hello!"});

                // invoke via dynamic proxy

                AlertServiceJsApi alertApi = QortobaAngularProxy.create
                    (AlertServiceJsApi.class, "AlertService", webView);

                alertApi.show("Go proxy!");
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

//		@JavascriptInterface
//		public void show(String message) {
//			Log.w("QORDOBA", message);
//		}

		@JavascriptInterface
		public void greet(String name) {
			Log.w("QORDOBA", "Greetings" + name);
		}

	}
}
