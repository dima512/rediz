package ru.kaiko.rediz;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProtocolTest {

    @Test
    public void testFromCharScenario1() {
        assertTrue(Protocol.fromChar('*') == Protocol.ARRAY);
    }

    @Test
    public void testFromCharScenario2() {
        assertTrue(Protocol.fromChar('+') == Protocol.SIMPLE);
    }

    @Test
    public void testFromCharScenario3() {
        Character character = null;
        assertTrue(Protocol.fromChar(character) == Protocol.UNKNOWN);
    }

    @Test
    public void testFromCharScenario4() {
        assertTrue(Protocol.fromChar('!') == Protocol.UNKNOWN);
    }
}