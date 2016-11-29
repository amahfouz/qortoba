package com.mahfouz.qortoba;

import java.util.Random;

/**
 * Unique ID for a JS object within the page context.
 */
public final class QortobaJsObjId {

    private final long id;

    private static final Random rand = new Random();

    public static final QortobaJsObjId create() {
        long id = rand.nextLong();
        return new QortobaJsObjId(id);
    }

    private QortobaJsObjId(long id) {
        this.id = id;
    }

    public String toString() {
        return String.valueOf(id);
    }
}
