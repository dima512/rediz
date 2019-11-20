package ru.kaiko.rediz.integration.operation;

import org.junit.*;
import ru.kaiko.rediz.Rediz;

import static org.junit.Assert.*;

public class BasicOperationsImplIT {

    private static Rediz rediz;

    @BeforeClass
    public static void setUp() {
        rediz = new Rediz("localhost", 6379);
    }

    @AfterClass
    public static void clean() {
        rediz.close();
    }

    @Before
    public void setUpEach() {
        rediz.flushCurrentDB();
    }

    @After
    public void cleanEach() {
        rediz.flushCurrentDB();
    }

    @Test
    public void set() {
        assertTrue(rediz.getBasicOperations().set("HERO:1", "BATMAN"));
    }

    @Test
    public void setAndExpire() throws InterruptedException {
        String key = "HERO:1";
        String value = "BATMAN";
        assertTrue(rediz.getBasicOperations().set(key, value, 2));
        assertTrue(rediz.getBasicOperations().get(key).equals(value));
        Thread.sleep(2250);
        assertTrue("it disappeared", rediz.getBasicOperations().get(key) == null);
    }

    @Test
    public void expire() throws InterruptedException {
        String key = "HERO:1";
        String value = "BATMAN";
        assertTrue(rediz.getBasicOperations().set(key, value));
        assertTrue(rediz.getBasicOperations().expire(key, 2));
        assertTrue(rediz.getBasicOperations().get(key).equals(value));
        Thread.sleep(2250);
        assertTrue("it disappeared", rediz.getBasicOperations().get(key) == null);
    }

    @Test
    public void get() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void incr() {
    }

    @Test
    public void incrBy() {
    }

    @Test
    public void decr() {
    }

    @Test
    public void decrBy() {
    }

    @Test
    public void exists() {
    }
}
