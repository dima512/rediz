package ru.kaiko.rediz;

import ru.kaiko.rediz.operation.*;

public class Rediz {

    private final Connection connection;
    private final BasicOperations basicOperations;
    private final HyperLogLogOperations hyperLogLogOperations;
    private final HashesOperations hashesOperations;
    private int currentDB;

    public Rediz() {
        this("localhost", 6379);
    }

    public Rediz(Connection connection) {
        this(connection.getHost(), connection.getPort());
    }

    public Rediz(String host, int port) {
        this.connection = new Connection(host, port);
        this.basicOperations = new BasicOperationsImpl(connection);
        this.hyperLogLogOperations = new HyperLogLogOperationsImpl(connection);
        this.hashesOperations = new HashesOperationsImpl(connection);
        this.currentDB = 0;
        this.connection.connect();
    }

    public Connection getConnection() {
        return connection;
    }

    public BasicOperations getBasicOperations() {
        return basicOperations;
    }

    public HyperLogLogOperations getHyperLogLogOperations() {
        return hyperLogLogOperations;
    }

    public HashesOperations getHashesOperations() {
        return hashesOperations;
    }

    public boolean switchDB(int db) {
        connection.write("SELECT " + db + "\r\n");
        String resp = (String) connection.read();
        if (resp != null && resp.equals("OK")) {
            currentDB = db;
            return true;
        } else {
            return false;
        }
    }

    public boolean flushCurrentDB() {
        connection.write("FLUSHDB\r\n");
        String resp = (String) connection.read();
        if (resp != null && resp.equals("OK")) {
            return true;
        } else {
            return false;
        }
    }

    public int getCurrentDB() {
        return currentDB;
    }

    public void close() {
        connection.disconnect();
    }

    @Override
    public String toString() {
        return "Rediz{" +
                "connection=" + connection +
                '}';
    }
}
