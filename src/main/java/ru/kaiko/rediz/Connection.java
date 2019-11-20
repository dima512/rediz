package ru.kaiko.rediz;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection {

    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public Connection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Connection(Socket socket) {
        this.socket = socket;
        this.port = socket.getPort();
        this.host = socket.getInetAddress().getHostName();
        connect();
    }

    public void connect() {
        if (isConnected()) return;
        try {
            if (socket == null) socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            socket = null;
            in = null;
            out = null;
            throw new RuntimeException(e.getCause() + ", " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            out.close();
            in.close();
            socket.close();
            out = null;
            in = null;
            socket = null;
        } catch (IOException e) {
            throw new RuntimeException(e.getCause() + ", " + e.getMessage());
        }
    }

    public void reconnect() {
        disconnect();
        connect();
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void write(String data) {
        if (!isConnected()) connect();
        try {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            reconnect();
            e.printStackTrace(); // todo : or need log?
        }
    }

    public Object read() {
        String firstLine = firstLine();
        Protocol protocol = Protocol.fromChar(firstLine.charAt(0));
        return switchAndParse(protocol, firstLine);
    }

    public String firstLine() {
        try {
            String res = in.readLine();
            return res == null || res.isBlank() ? "-ERR" : res;
        } catch (IOException e) {
            reconnect();
            e.printStackTrace(); // todo : or need log?
            return "-ERR";
        }
    }

    //    https://redis.io/topics/protocol
    // line can't be null or empty
    public Object switchAndParse(Protocol protocol, String line) {
        switch (protocol) {
            case SIMPLE:
                return parseLine(line);
            case INTEGER:
                return parseInteger(line);
            case BULK:
                return parseBulk(line);
            case ARRAY:
                return parseArray(line);
            case ERROR:
                return null; // TODO: think how to response when error, Either will be great
            default:
                reconnect();
                return null;
        }
    }

    private String parseLine(String line) {
        return line.substring(1);
    }

    private Integer parseInteger(String line) {
        try {
            return Integer.parseInt(line.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // TODO: hm, what will happen when it gets really big string? from docs: it can be maximum 512mb
    private String parseBulk(String enterLine) {
        try {
            Integer length = Integer.parseInt(enterLine.substring(1).trim());
            if (length == -1) return null;
            String line = in.readLine();
            if (line != null || line.length() == length) {
                return line;
            } else {
                throw new RuntimeException("different sizes, expected : " + length + ", got : " + line.length());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> parseArray(String enterLine) {
        try {
            Integer length = Integer.parseInt(enterLine.substring(1));
            List<String> result = new ArrayList<>();
            if (length == 0) return result;
            String line;
            for (int i = length; i > 0; i--) {
                Integer expectedSize = Integer.parseInt(in.readLine().substring(1).trim());
                line = in.readLine();
                if (line != null || line.length() == expectedSize) result.add(line);
                else throw new RuntimeException("different sizes, expected : " + length + ", got : " + line.length());
            }
            return result;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String ping() {
        write("PING\r\n");
        return (String) read();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
