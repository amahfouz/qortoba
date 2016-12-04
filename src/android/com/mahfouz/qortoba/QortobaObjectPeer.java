package com.mahfouz.qortoba;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Proxy for a JavaScript object.
 *
 * Invokes methods on companion JavaScript object and triggers
 * the destruction of the companion object upon destruction of
 * itself.
 */
final class QortobaObjectPeer implements InvocationHandler {

    /** Unique ID for the peer JS object in its page context */
    private final QortobaJsObjId objectId;
    /** Callback for the object to destroy itself and invoke methods */
    private final Callback callback;

    public QortobaObjectPeer(QortobaJsObjId objectId,
                             Callback callback) {
        this.objectId = objectId;
        this.callback = callback;
    }

    public QortobaJsObjId getJsObjtId() {
        return objectId;
    }

    @Override
    public void finalize() {
        callback.release(objectId);
    }

    //
    // Nested
    //

    /**
     * API for an object to destroy its JS peer.
     */
    interface Callback {

        /**
         * Release any reference to the corresponding JS object
         * and release all resources held.
         */
        void release(QortobaJsObjId objectId);

        /**
         * Invokes the specified method on the companion JavaScript object.
         */
        void invoke(QortobaJsObjId objId, String methodName, Object[] args);
    }

    //
    // Dynamic proxy implementation
    //

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        callback.invoke(objectId, method.getName(), args);

        return null;
    }
}
