package ru.kaiko.rediz.integration.operation;

import org.junit.*;

import static org.junit.Assert.*;

import ru.kaiko.rediz.Rediz;

// todo : add negative scenarios

public class HashesOperationsImplIT {

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
    public void testHSet() {
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "CAP", "BERLIN"));
        assertFalse(rediz.getHashesOperations().hSet("GERMANY", "CAP", "BERLIN"));
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "POPULATION", "80"));
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "LANGUAGE", "DEUTSCH"));
    }


    @Test
    public void testHExists() {
        assertFalse(rediz.getHashesOperations().hExists("GERMANY", "CAP"));
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "CAP", "BERLIN"));
        assertTrue(rediz.getHashesOperations().hExists("GERMANY", "CAP"));
    }


    @Test
    public void testHGet() {
        String value = "BERLIN";

        assertTrue(rediz.getHashesOperations().hGet("GERMANY", "CAP") == null);
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "CAP", "BERLIN"));
        assertTrue(rediz.getHashesOperations().hGet("GERMANY", "CAP").equals(value));
    }


    @Test
    public void testHKeys() {
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "CAP", "BERLIN"));
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "POPULATION", "80"));
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "LANGUAGE", "DEUTSCH"));

        String[] expected = {"CAP", "POPULATION", "LANGUAGE"};

        assertArrayEquals(expected, rediz.getHashesOperations().hKeys("GERMANY").toArray());
        assertTrue("when it doesn't exist then return empty list", 0 == rediz.getHashesOperations().hKeys("ITALY").size());
    }


    @Test
    public void testHVals() {
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "CAP", "BERLIN"));
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "POPULATION", "80"));
        assertTrue(rediz.getHashesOperations().hSet("GERMANY", "LANGUAGE", "DEUTSCH"));

        String[] expected = {"BERLIN", "80", "DEUTSCH"};

        assertArrayEquals(expected, rediz.getHashesOperations().hVals("GERMANY").toArray());
        assertTrue("when it doesn't exist then return empty list", 0 == rediz.getHashesOperations().hVals("ITALY").size());
    }
}
