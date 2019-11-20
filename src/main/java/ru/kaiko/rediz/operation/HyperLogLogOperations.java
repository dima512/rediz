package ru.kaiko.rediz.operation;

/**
 * https://redis.io/commands#hyperloglog
 */

public interface HyperLogLogOperations {

    boolean pfAdd(String key, String element);

    Integer pfCount(String key);

    boolean pfMerge(String destKey, String... sourceKeys);

}
