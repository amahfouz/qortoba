package com.mahfouz.qortoba;

import java.lang.reflect.Proxy;

/**
 * Factory for Java objects that proxy JavaScript objects.
 */
public final class QortobaObjectFactory  {

    /** Qortoba client for JavaScript invocation */
    private final QortobaProxy proxy;
    private final ObjectPeerCallback objPeerCallback;

    public QortobaObjectFactory(QortobaWebView webView) {
        if (webView == null)
            throw new IllegalArgumentException();

        this.proxy = new QortobaProxy(webView);
        this.objPeerCallback = new ObjectPeerCallback();
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
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> api,
                        String className,
                        Object[] constructorArgs) {

        // instantiate object on the JavaScript side
        QortobaJsObjId newObjId = QortobaJsObjId.create();

        proxy.instantiate(newObjId, className, constructorArgs);

        return (T) Proxy.newProxyInstance
            (api.getClassLoader(),
            new Class[] { api },
            new QortobaObjectPeer(newObjId, objPeerCallback));

    }

    //
    // Nested
    //

    /**
     * Implements the peer object callback allowing objects
     * to invoke methods on JavaScript peers and destroy them.
     */
    private final class ObjectPeerCallback
        implements QortobaObjectPeer.Callback {

        @Override
        public void release(QortobaJsObjId objectId) {
            proxy.destroy(objectId);
        }

        @Override
        public void invoke(QortobaJsObjId objId,
                           String methodName,
                           Object[] args) {

            proxy.invoke(objId, methodName, args);
        }
    }
}
