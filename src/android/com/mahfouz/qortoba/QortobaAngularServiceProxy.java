package com.mahfouz.qortoba;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Proxy for an Angular service.
 *
 * The service is specified via a Java interface provided
 * to the proxy at creation time.
 */
public final class QortobaAngularServiceProxy implements InvocationHandler {

    private final QortobaWebView webView;

    /** Name of the Angular service to invoke */
    private final String angularServiceName;

    private QortobaAngularServiceProxy(String serviceName,
                                       QortobaWebView webView) {
        if (serviceName == null || webView == null)
            throw new IllegalArgumentException();

        this.angularServiceName = serviceName;
        this.webView = webView;
    }

     @SuppressWarnings("unchecked")
    public static <T> T create
        (Class<T> jsInterface, String serviceName, QortobaWebView webView) {

        return (T) Proxy.newProxyInstance
            (jsInterface.getClassLoader(),
            new Class[] { jsInterface },
            new QortobaAngularServiceProxy(serviceName, webView));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {

        // invoke the corresponding Angular service method

        String jsString
            = "qortoba.angular("
            + angularServiceName + ","
            + method.getName() + ","
            + QortobaSerializer.serializeParamsArray(args) + ");";

        webView.runJavaScript(jsString);

        return null;
    }
}
