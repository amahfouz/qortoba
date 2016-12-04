package com.mahfouz.qortoba;

/**
 * Platform-independent invocation of Qortoba.
 *
 * Invocation specified using an (angular) service name, a method
 * name (of the service) and params (specified as an array serialized
 * as a String in the form "[param1, param2, param3]").
 *
 * Package protected. Internal use only.
 */
@Deprecated
final class QortobaAngularInvocation {

	private static final String SERVICE_PLACEHOLDER = "wl-service_name";
	private static final String METHOD_PLACEHOLDER = "wl-method_name";
	private static final String PARAMS_PLACEHOLDER = "wl-params_array";

	private static final String INVOCATION_JS_STR
		= "qortoba.angular("
		    + "\"wl-service_name\", "
		    + "\"wl-method_name\", "
		    + "\"wl-params_array\");";

	private final String serviceName;
	private final String methodName;
	private final String paramsArrStr;

	public QortobaAngularInvocation(String serviceName,
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

    /**
     * Abstracts a WebView for the purpose of running JS.
     */
    public interface WebView {
        void runJavaScript(String jsString);
    }
}
