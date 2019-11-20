package ru.kaiko.rediz.operation;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTest {

    @Test
    public void simpleCommand() {
        String first = "GET GERMANY\r\n";
        String second = "GET GERMANY";
        assertTrue(Command.simpleCommand("GET", "GERMANY").equals(first));
        assertFalse(Command.simpleCommand("GET", "GERMANY").equals(second));
    }

    @Test(expected = IllegalArgumentException.class)
    public void simpleCommandWithException() {
        String secondPartOfCommand = null;
        Command.simpleCommand("GET", secondPartOfCommand);
    }

    @Test
    public void simpleCommand3() {
        String first = "SET GERMANY BERLIN\r\n";
        String second = "SET GERMANY BERLIN";
        assertTrue(Command.simpleCommand3("SET", "GERMANY", "BERLIN").equals(first));
        assertFalse(Command.simpleCommand3("SET", "GERMANY", "BERLIN").equals(second));
    }

    @Test(expected = IllegalArgumentException.class)
    public void simpleCommand3WithException() {
        String secondPartOfCommand = null;
        Command.simpleCommand3("SET", secondPartOfCommand, "BERLIN");
    }

    @Test
    public void simpleCommand4() {
        String first = "HSET GERMANY CAP BERLIN\r\n";
        String second = "HSET GERMANY CAP BERLIN";
        assertTrue(Command.simpleCommand4("HSET", "GERMANY", "CAP", "BERLIN").equals(first));
        assertFalse(Command.simpleCommand4("HSET", "GERMANY", "CAP", "BERLIN").equals(second));
    }

    @Test(expected = IllegalArgumentException.class)
    public void simpleCommand4WithException() {
        String second = null;
        Command.simpleCommand4("SET", second, "BERLIN", "MILAN");
    }
}
