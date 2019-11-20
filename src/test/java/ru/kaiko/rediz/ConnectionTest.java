package ru.kaiko.rediz;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import static org.junit.Assert.*;

public class ConnectionTest {

    private Connection connection;
    private Socket socket;
    private InetAddress inetAddress;

    @After
    public void clean() {
        connection = null;
        socket = null;
        inetAddress = null;
    }

    private void setUpForEachAndLoadDataIntoInputStream(String inputData) throws IOException {
        inetAddress = InetAddress.getByName("localhost");
        socket = Mockito.mock(Socket.class);

        Mockito.when(socket.getInetAddress()).thenReturn(inetAddress);
        Mockito.when(socket.getPort()).thenReturn(Integer.valueOf(6379));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(socket.getOutputStream()).thenReturn(out);

        ByteArrayInputStream in = new ByteArrayInputStream((inputData + "\r\n").getBytes());
        Mockito.when(socket.getInputStream()).thenReturn(in);

        connection = new Connection(socket);
    }

    @Test
    public void connect() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("");
        Mockito.verify(socket, Mockito.times(1)).getInputStream();
        Mockito.verify(socket, Mockito.times(1)).getOutputStream();
    }

    @Test
    public void disconnect() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("");
        Mockito.doNothing().when(socket).close();
        connection.disconnect();
        Mockito.verify(socket, Mockito.times(1)).close();
    }

    @Test
    public void isConnected() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("");
        Mockito.when(socket.isConnected()).thenReturn(Boolean.TRUE);
        assertTrue(connection.isConnected());
        Mockito.verify(socket, Mockito.times(2)).isConnected(); // from connect()
    }

    @Test
    public void write() throws IOException {
        int port = 8888;
        byte [] message = "hello".getBytes();

        ServerSocket serverSocket = new ServerSocket(port);

        new Thread(() -> {
            try {
                Thread.sleep(500);
                Rediz rediz = new Rediz("localhost", port);
                rediz.getConnection().write("hello");
                rediz.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Socket socket = serverSocket.accept();
        assertArrayEquals(message, socket.getInputStream().readAllBytes());
        socket.close();
    }

    @Test
    public void testReadScenario1() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("+OK");
        assertTrue(connection.read().equals("OK"));
    }

    @Test
    public void testReadScenario2() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("*2\r\n$3\r\nfoo\r\n$3\r\nbar");
        String[] expected = {"foo", "bar"};
        assertArrayEquals(expected, ((List<String>) connection.read()).toArray());
    }

    @Test
    public void testFirstLineScenario1() throws IOException {
        setUpForEachAndLoadDataIntoInputStream(":1");
        assertTrue(connection.firstLine().equals(":1"));
    }

    @Test
    public void testFirstLineScenario2() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("");
        assertTrue(connection.firstLine().startsWith("-ERR"));
    }

    @Test
    public void testSwitchAndParseScenario0Unknown() throws IOException {
        String response = "???";
        setUpForEachAndLoadDataIntoInputStream(response);
        assertTrue(connection.switchAndParse(Protocol.fromChar(response.charAt(0)), response) == null);
    }

    @Test
    public void testSwitchAndParseScenario1Simple() throws IOException {
        String response = "+OK";
        setUpForEachAndLoadDataIntoInputStream(response);
        assertTrue(connection.switchAndParse(Protocol.fromChar(response.charAt(0)), response).equals("OK"));
    }

    @Test
    public void testSwitchAndParseScenario2Integer() throws IOException {
        String response = ":3";
        setUpForEachAndLoadDataIntoInputStream(response);
        assertTrue((Integer) connection.switchAndParse(Protocol.fromChar(response.charAt(0)), response) == 3);
    }

    @Test
    public void testSwitchAndParseScenario3Error() throws IOException {
        String response = "-ERR unknown command 'foobar'";
        setUpForEachAndLoadDataIntoInputStream(response);
        assertTrue(connection.switchAndParse(Protocol.fromChar(response.charAt(0)), response) == null);
    }

    @Test
    public void testSwitchAndParseScenario4Bulk() throws IOException {
        String response1 = "$6";
        String response2 = "foobar";
        setUpForEachAndLoadDataIntoInputStream(response2);
        assertTrue(connection.switchAndParse(Protocol.fromChar(response1.charAt(0)), response1).equals("foobar"));
    }

    @Test
    public void testSwitchAndParseScenario5Array() throws IOException {
        String response1 = "*2";
        String response2 = "$3\r\nfoo\r\n$3\r\nbar";
        setUpForEachAndLoadDataIntoInputStream(response2);
        String[] expected = {"foo", "bar"};
        assertArrayEquals(expected, ((List<String>) connection.switchAndParse(Protocol.fromChar(response1.charAt(0)), response1)).toArray());
    }

    @Test
    public void ping() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("+PONG");
        assertTrue(connection.ping().equals("PONG"));
    }

    @Test
    public void getHost() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("");
        assertTrue(connection.getHost().equals("localhost"));
    }

    @Test
    public void getPort() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("");
        assertTrue(connection.getPort() == 6379);
    }

    @Test
    public void getSocket() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("");
        assertTrue(connection.getSocket() == socket);
    }

    @Test
    public void testToString() throws IOException {
        setUpForEachAndLoadDataIntoInputStream("");
        assertTrue(connection.toString().contains("6379"));
    }
}
