package com.mahfouz.qortoba;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.json.JSONArray;

import android.webkit.WebView;

/**
 * Proxy for a JavaScript object.
 *
 * The object is specified via a Java interface provided
 * to the proxy at creation time.
 */
public final class QortobaJsProxy implements InvocationHandler {

    /** Qortoba client for JavaScript invocation */
    private final AndroidQortobaClient jsClient;

    /** Name of the angular service to invoke */
    private final String jsServiceName;

    public QortobaJsProxy(String serviceName, AndroidQortobaClient client) {
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
            new QortobaJsProxy(serviceName, client));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {

        // invoke the corresponding JavaScript method

        JSONArray jsonAr = new JSONArray();

        for (Object arg : args) {
            jsonAr.put(arg.toString());
        }

        jsClient.invoke(jsServiceName, method.getName(), jsonAr);

        return null;
    }
}
