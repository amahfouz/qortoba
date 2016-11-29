package com.mahfouz.qortoba;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Factory for Java objects that proxy JavaScript objects.
 */
public final class QortobaObjectFactory {

    /** Qortoba client for JavaScript invocation */
    private final QortobaClient jsClient;

    public QortobaObjectFactory(QortobaClient client) {
        this.jsClient = client;
    }

    /**
     * Instantiates a JavaScript object from the class
     * with the specified name passing along constructor
     * arguments.
     *
     * On the JavaScript side, it is expected that a
     * constructor function with the specified name
     * and arguments is defined at the global level.
     */
    public <T> T create(Class<T> api,
                        String className,
                        Object[] constructorArgs) {

        callJsToCreateTheObject();

        @SuppressWarnings("unchecked")
        return (T) Proxy.newProxyInstance
            (api.getClassLoader(),
            new Class[] { api },
            new InvocHandler());

    }

    private final class InvocHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

            QortobaObjectProxy objProxy = (QortobaObjectProxy) proxy;

            jsClient.invoke
                (objProxy.getJsObjtId().toString(), method.getName(), args);

            return null;
        }

    }

    private final class Destroyer implements QortobaObjectProxy.Destructor {

        @Override
        public void release(QortobaJsObjId objectId) {
            callJsServiceToCreateObject();
        }
    }
}
