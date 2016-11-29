package com.mahfouz.qortoba;

/**
 * Proxy for a JavaScript object.
 */
public final class QortobaObjectProxy {

    /** Unique ID for the peer JS object in its page context */
    private final QortobaJsObjId objectId;
    /** Callback for the object to destroy itself */
    private final Destructor destructor;

    public QortobaObjectProxy(QortobaJsObjId objectId,
                              Destructor destructor) {
        this.objectId = objectId;
        this.destructor = destructor;
    }

    public QortobaJsObjId getJsObjtId() {
        return objectId;
    }

    @Override
    public void finalize() {
        destructor.release(objectId);
    }

    //
    // Nested
    //

    /**
     * API for an object to destroy its JS peer.
     */
    interface Destructor {

        /**
         * Release any reference to the corresponding JS object
         * and release all resources held.
         */
        void release(QortobaJsObjId objectId);
    }
}
