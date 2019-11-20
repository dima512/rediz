package ru.kaiko.rediz.operation;

import ru.kaiko.rediz.Connection;
import static ru.kaiko.rediz.operation.Command.*;

public class HyperLogLogOperationsImpl implements HyperLogLogOperations {

    private Connection connection;

    public HyperLogLogOperationsImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean pfAdd(String key, String element) {
        connection.write(simpleCommand3("PFADD", key, element));
        Integer resp = (Integer) connection.read();
        return resp != null && resp == 1;
    }

    @Override
    public Integer pfCount(String key) {
        connection.write(simpleCommand("PFCOUNT", key));
        return (Integer) connection.read();
    }

    @Override
    public boolean pfMerge(String destKey, String... sourceKeys) {
        if (sourceKeys == null || sourceKeys.length < 1) return false;
        connection.write(simpleCommand3("PFMERGE", destKey, String.join(" ", sourceKeys)));
        String resp = (String) connection.read();
        return resp != null && resp.equals("OK");
    }
}
