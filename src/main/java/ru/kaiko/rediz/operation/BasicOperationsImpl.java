package ru.kaiko.rediz.operation;

import ru.kaiko.rediz.Connection;

import static ru.kaiko.rediz.operation.Command.*;

public class BasicOperationsImpl implements BasicOperations {

    private Connection connection;

    public BasicOperationsImpl(Connection connection) {
        this.connection = connection;
    }

    public boolean set(String key, String value) {
        connection.write(simpleCommand3("SET", key, value));
        String resp = (String) connection.read();
        return resp != null && resp.equals("OK");
    }

    public boolean set(String key, String value, int expire) {
        set(key, value);
        return expire(key, expire);
    }

    public boolean expire(String key, int expire) {
        connection.write(simpleCommand3("EXPIRE", key, String.valueOf(expire)));
        Integer respExpire = (Integer) connection.read();
        return respExpire != null && respExpire == 1;
    }

    public String get(String key) {
        connection.write(simpleCommand("GET", key));
        return (String) connection.read();
    }

    public boolean delete(String key) {
        connection.write(simpleCommand("DEL", key));
        Integer resp = (Integer) connection.read();
        return resp != null && resp == 1;
    }

    public Integer incr(String key) {
        connection.write(simpleCommand("INCR", key));
        return (Integer) connection.read();
    }

    public Integer incrBy(String key, int increment) {
        connection.write(simpleCommand3("INCRBY", key, String.valueOf(increment)));
        return (Integer) connection.read();
    }

    public Integer decr(String key) {
        connection.write(simpleCommand("DECR", key));
        return (Integer) connection.read();
    }

    public Integer decrBy(String key, int decrement) {
        connection.write(simpleCommand3("DECRBY", key, String.valueOf(decrement)));
        return (Integer) connection.read();
    }

    public Boolean exists(String key) {
        connection.write(simpleCommand("EXISTS", key));
        if (connection.read() == null) return null;
        else return ((Integer) connection.read()) == 1;
    }
}
