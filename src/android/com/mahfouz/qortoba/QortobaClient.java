package com.mahfouz.qortoba;

/**
 * Client for invoking JavaScript methods.
 */
public interface QortobaClient {

    /**
     * Invokes the specified method on the specified service
     * passing it the array of object parameters.
     *
     * The service ID uniquely identifies a service within
     * the invocation context (e.g. a page loaded in Web View).
     */
    void invoke(String serviceId, String methodName, Object[] params);
}
