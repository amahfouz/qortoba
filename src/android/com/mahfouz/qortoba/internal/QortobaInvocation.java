package com.mahfouz.qortoba.internal;

/**
 * Platform-independent invocation of Qortoba.
 *
 * Invocation specified using an (angular) service name, a method
 * name (of the service) and params (specified as an array serialized
 * as a String in the form "[param1, param2, param3]").
 */
public final class QortobaInvocation {

	private static final String SERVICE_PLACEHOLDER = "wl-service_name";
	private static final String METHOD_PLACEHOLDER = "wl-method_name";
	private static final String PARAMS_PLACEHOLDER = "wl-params_array";

	private static final String INVOCATION_JS_STR
		= "qortoba.callback(1.0, "
		    + "\"wl-service_name\", "
		    + "\"wl-method_name\", "
		    + "\"wl-params_array\");";

	private final String serviceName;
	private final String methodName;
	private final String paramsArrStr;

	public QortobaInvocation(String serviceName,
							 String methodName,
							 String paramsArrIfAny) {

		if (serviceName == null || methodName == null)
			throw new IllegalArgumentException();

		this.serviceName = serviceName;
		this.methodName = methodName;
		this.paramsArrStr = paramsArrIfAny;
	}

	public void invoke(WebView webView) {

		// replace placholders with actual values

		String withService
		    = INVOCATION_JS_STR.replace(SERVICE_PLACEHOLDER, serviceName);
		String withMethod
		    = withService.replace(METHOD_PLACEHOLDER, methodName);

		String serializedParams = (paramsArrStr != null)
			? paramsArrStr
			: "[]";

		// escape the double quotes
		String escapedParams
		    = serializedParams.replace("\"", "\\\"");
		String withParams
		    = withMethod.replace(PARAMS_PLACEHOLDER, escapedParams);

		// run the JavaScript on the web view

		webView.runJavaScript(withParams);
	}

	//
	// Abstracts a web view
	//

	public interface WebView {
		void runJavaScript(String jsString);
	}
}
