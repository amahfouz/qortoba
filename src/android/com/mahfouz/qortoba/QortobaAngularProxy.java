package com.mahfouz.qortoba;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.webkit.WebView;

/**
 * Proxy for an Angular service.
 *
 * The service is specified via a Java interface provided
 * to the proxy at creation time.
 */
public final class QortobaAngularProxy implements InvocationHandler {

    /** Qortoba client for Angular invocation */
    private final AndroidQortobaClient jsClient;

    /** Name of the Angular service to invoke */
    private final String jsServiceName;

    public QortobaAngularProxy(String serviceName, AndroidQortobaClient client) {
        this.jsServiceName = serviceName;
        this.jsClient = client;
    }

     @SuppressWarnings("unchecked")
    public  static <T>  T create
        (Class<T> jsInterface, String serviceName, WebView webView) {

        AndroidQortobaClient client = new AndroidQortobaClient(webView);

        return (T) Proxy.newProxyInstance
            (jsInterface.getClassLoader(),
            new Class[] { jsInterface },
            new QortobaAngularProxy(serviceName, client));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {

        // invoke the corresponding Angular service method

        jsClient.invoke(jsServiceName, method.getName(), args);

        return null;
    }
}
