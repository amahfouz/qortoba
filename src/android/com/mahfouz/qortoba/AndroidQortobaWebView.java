package com.mahfouz.qortoba;

import android.webkit.WebView;

/**
 * Wraps an Android WebView.
 */
public final class AndroidQortobaWebView implements QortobaWebView {

    private final WebView webView;

    public AndroidQortobaWebView(WebView webView) {
        if (webView == null)
            throw new IllegalArgumentException();

        this.webView = webView;
    }

    @Override
    public void runJavaScript(String jsString) {

        // add a void(0); to force the JS to execute

        String augmentedJs = "javascript:" + jsString + "void(0);";
        webView.loadUrl(augmentedJs);
    }
}