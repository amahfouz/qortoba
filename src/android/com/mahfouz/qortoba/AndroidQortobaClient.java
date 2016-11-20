package com.mahfouz.qortoba;

import org.json.JSONArray;

import com.mahfouz.qortoba.internal.QortobaInvocation;

import android.webkit.WebView;

/**
 * Android-specific Qortoba client.
 *
 * Invokes arbitrary angular service method on an Android WebView.
 */
public final class AndroidQortobaClient {

	private static final String LOG_TAG = "QORTOBA";

	private final WebView webView;
	private final AndroidWebView wrapper;

	public AndroidQortobaClient(WebView webView) {
		if (webView == null)
			throw new IllegalArgumentException();

		this.webView = webView;
		this.wrapper = new AndroidWebView();
	}

	public void invoke(String serviceName, String methodName, JSONArray params) {
		QortobaInvocation inv = new QortobaInvocation
			(serviceName, methodName, params != null ? params.toString() : null);
		inv.invoke(wrapper);
	}


	/**
	 * Wraps an Android WebView for passing to a QortobaInvocation.
	 */
	private final class AndroidWebView implements QortobaInvocation.WebView {

		@Override
		public void runJavaScript(String jsString) {

			// add a void(0); to force the JS to execute

			String augmentedJs = "javascript:" + jsString + "void(0);";
			webView.loadUrl(augmentedJs);
		}
	}
}
