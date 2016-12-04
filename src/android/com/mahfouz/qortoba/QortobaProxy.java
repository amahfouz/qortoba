package com.mahfouz.qortoba;

/**
 * Proxy for JS facilities provided by Qortoba.
 *
 * Allows creation and destruction of JavaScript objects
 * and invoking methods on them.
 *
 * This class is used internally and is package protected.
 */
final class QortobaProxy {

    private final QortobaWebView webView;

    QortobaProxy(QortobaWebView webView) {
        this.webView = webView;
    }

    /**
     * Proxy to instantiate an object from the specified class, specified
     * by its JavaScript constructor function name, passing to the
     * constructor function the specified args.
     */
    public void instantiate(QortobaJsObjId objId,
                            String className,
                            Object[] args) {
        String jsString
            = "qortoba.instantiate("
                + objId.toString() + ", "
                + className + ", "
                + QortobaSerializer.serializeParamsArray(args) + ");";
        webView.runJavaScript(jsString);
    }

    /**
     * Proxy for "destroy" to release reference to the specified object.
     */
    public void destroy(QortobaJsObjId objId) {
        String jsString = "qortoba.destroy(" + objId + ")";
        webView.runJavaScript(jsString);
    }

    /**
     * Proxy for invoke to invoke the specified method with the specified
     * args on the object with the given ID.
     *
     * Has no effect if the object does not exist.
     */
    public void invoke(QortobaJsObjId objId,
                       String methodName,
                       Object[] args) {

        String jsString
            = "qortoba.invoke("
             + objId + ", "
             + methodName + ", "
             + QortobaSerializer.serializeParamsArray(args) + ");";

        webView.runJavaScript(jsString);
    }
}
