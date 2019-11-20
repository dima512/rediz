package ru.kaiko.rediz.integration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.kaiko.rediz.Rediz;
import static org.junit.Assert.*;

public class RedizIT {

    private static Rediz rediz;

    @BeforeClass
    public static void setUp() {
        rediz = new Rediz("localhost", 6379);
        rediz.flushCurrentDB();
    }

    @AfterClass
    public static void clean() {
        rediz.flushCurrentDB();
        rediz.close();
        rediz = null;
    }

    @Test
    public void testSwitchDB() {
        assertTrue("switch must be successful", rediz.switchDB(2));
        assertTrue("current db must be 2", rediz.getCurrentDB() == 2);
    }

    @Test
    public void testFlushDB() {
        rediz.getBasicOperations().set("GERMANY", "BERLIN");
        assertTrue("check berlin exists", rediz.getBasicOperations().get("GERMANY").equals("BERLIN"));
        assertTrue("flush must be successful", rediz.flushCurrentDB());
        assertTrue("after flush it doesn't exist", rediz.getBasicOperations().get("GERMANY") == null);
    }

    @Test
    public void testToString() {
        assertTrue("port must 6379", rediz.toString().contains("6379"));
        assertTrue("host must localhost", rediz.toString().contains("localhost"));
    }
}
