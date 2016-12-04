package com.mahfouz.qortoba;

import org.json.JSONArray;

/**
 * Serialization utilities for Qortoba Java-to-JavaScript communication.
 */
public final class QortobaSerializer {

    /**
     * Serializes the parameters array into a JSON array
     * and then converts the result into a string which
     * is ready to be used in a JavaScript string.
     *
     * Returns the string result or null if args is null.
     */
    public static String serializeParamsArray(Object[] args) {
        final String paramsArrStr;

        if (args != null) {
            JSONArray jsonAr = new JSONArray();

            for (Object arg : args) {
                jsonAr.put(arg.toString());
            }

            paramsArrStr = jsonAr.toString();
        }
        else
            paramsArrStr = null;

        // replace null with empty array

        String serializedParams = (paramsArrStr != null)
            ? paramsArrStr
            : "[]";

        // escape double quotes

        String escapedParams
            = serializedParams.replace("\"", "\\\"");

        return escapedParams;
    }
}
