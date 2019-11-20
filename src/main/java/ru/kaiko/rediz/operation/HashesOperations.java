package ru.kaiko.rediz.operation;

import java.util.List;

/**
 * https://redis.io/commands#hash
 */

public interface HashesOperations {

    boolean hSet(String key, String field, String value);

    boolean hExists(String key, String field);

    String hGet(String key, String field);

    List<String> hKeys(String key);

    List<String> hVals(String key);

}
