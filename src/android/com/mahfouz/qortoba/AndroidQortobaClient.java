package com.mahfouz.qortoba;

import org.json.JSONArray;

import android.webkit.WebView;

/**
 * Android-specific Qortoba client.
 *
 * Invokes arbitrary angular service method on an Android WebView.
 */
public final class AndroidQortobaClient implements QortobaClient {

	private final WebView webView;
	private final AndroidWebView wrapper;

	public AndroidQortobaClient(WebView webView) {
		if (webView == null)
			throw new IllegalArgumentException();

		this.webView = webView;
		this.wrapper = new AndroidWebView();
	}

	public void invoke(String serviceName,
	                   String methodName,
	                   Object[] objParams) {

        final String serializedParams;

        if (objParams != null) {
            JSONArray jsonAr = new JSONArray();

            for (Object arg : objParams) {
                jsonAr.put(arg.toString());
            }

            serializedParams = jsonAr.toString();
        }
        else
            serializedParams = null;

		QortobaAngularInvocation inv = new QortobaAngularInvocation
			(serviceName, methodName, serializedParams);

		inv.invoke(wrapper);
	}


	/**
	 * Wraps an Android WebView for passing to a QortobaInvocation.
	 */
	private final class AndroidWebView implements QortobaAngularInvocation.WebView {

		@Override
		public void runJavaScript(String jsString) {

			// add a void(0); to force the JS to execute

			String augmentedJs = "javascript:" + jsString + "void(0);";
			webView.loadUrl(augmentedJs);
		}
	}
}
