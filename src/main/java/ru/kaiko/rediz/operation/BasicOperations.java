package ru.kaiko.rediz.operation;

/**
 * https://redis.io/commands#string
 */

public interface BasicOperations {

    boolean set(String key, String value);

    /**
     * @param expire in seconds
     */
    boolean set(String key, String value, int expire);

    /**
     * @param expire in seconds
     */
    boolean expire(String key, int expire);

    String get(String key);

    boolean delete(String key);

    Integer incr(String key);

    Integer incrBy(String key, int increment);

    Integer decr(String key);

    Integer decrBy(String key, int increment);

    Boolean exists(String key);
}
