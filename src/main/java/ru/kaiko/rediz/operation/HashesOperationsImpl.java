package ru.kaiko.rediz.operation;

import ru.kaiko.rediz.Connection;

import java.util.List;
import static ru.kaiko.rediz.operation.Command.*;

public class HashesOperationsImpl implements HashesOperations {

    private Connection connection;

    public HashesOperationsImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean hSet(String key, String field, String value) {
        connection.write(simpleCommand4("HSET", key, field, value));
        Integer resp = (Integer) connection.read();
        return resp != null && resp == 1;
    }

    @Override
    public boolean hExists(String key, String field) {
        connection.write(simpleCommand3("HEXISTS", key, field));
        Integer resp = (Integer) connection.read();
        return resp != null && resp == 1;
    }

    @Override
    public String hGet(String key, String field) {
        connection.write(simpleCommand3("HGET", key, field));
        return (String) connection.read();
    }

    @Override
    public List<String> hKeys(String key) {
        connection.write(simpleCommand("HKEYS", key));
        return (List<String>) connection.read();
    }

    @Override
    public List<String> hVals(String key) {
        connection.write(simpleCommand("HVALS", key));
        return (List<String>) connection.read();
    }
}
