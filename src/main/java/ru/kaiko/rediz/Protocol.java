package ru.kaiko.rediz;

/**
 * https://redis.io/topics/protocol
 */

public enum Protocol {
    SIMPLE,
    ERROR,
    INTEGER,
    BULK,
    ARRAY,
    UNKNOWN;

    public static Protocol fromChar(Character character) {
        if (character == null) return UNKNOWN;
        switch (character) {
            case '+': return SIMPLE;
            case '-': return ERROR;
            case ':': return INTEGER;
            case '$': return BULK;
            case '*': return ARRAY;
            default: return UNKNOWN;
        }
    }
}
