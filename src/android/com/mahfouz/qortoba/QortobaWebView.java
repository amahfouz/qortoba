package com.mahfouz.qortoba;

/**
 * Abstracts a platform-specific WebView for the purpose of running JS.
 */
public interface QortobaWebView {

    /**
     * Runs the JavaScript string in the context of the associated
     * platform Web view.
     */
    void runJavaScript(String jsString);
}
