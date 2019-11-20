package ru.kaiko.rediz.integration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.kaiko.rediz.Connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConnectionIT {

    private static Connection connection;

    @BeforeClass
    public static void setUp() {
        connection = new Connection("localhost", 6379);
        connection.connect();
    }

    @AfterClass
    public static void clean() {
        connection.disconnect();
        connection = null;
    }

    @Test(expected = RuntimeException.class)
    public void testConnect() {
        Connection conn = new Connection("localhost222", 6379);
        conn.connect();
    }

    @Test
    public void testConnectAndDisconnect() {
        Connection conn = new Connection("localhost", 6379);
        conn.connect();
        assertTrue("must be connected", conn.isConnected());
        conn.disconnect();
        assertFalse("must be not connected", conn.isConnected());
        conn = null;
    }

    @Test
    public void testReconnect() {
        connection.reconnect();
        assertTrue("must be connected", connection.isConnected());
        assertTrue("port must be 6379", connection.getPort() == 6379);
        assertFalse("host must be not localhost222", connection.getHost().equals("localhost222"));
    }

    @Test
    public void testIsConnected() {
        Connection conn = new Connection("localhost", 6379);
        conn.connect();
        assertTrue("must be connected", conn.isConnected());
        conn.disconnect();
        conn = null;
    }

    @Test
    public void testPing() {
        assertTrue("when it sends PING answer must be PONG", connection.ping().equals("PONG"));
    }

}
