package ru.kaiko.rediz.integration.operation;

import org.junit.*;
import ru.kaiko.rediz.Rediz;

import static org.junit.Assert.*;

// todo : add negative scenarios

public class HyperLogLogOperationsImplIT {

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
    public void testPfAdd() {
        assertTrue(rediz.getHyperLogLogOperations().pfAdd("ITALY", "ROME"));
        assertTrue(rediz.getHyperLogLogOperations().pfAdd("ITALY", "TURIN"));
        assertTrue(rediz.getHyperLogLogOperations().pfAdd("ITALY", "MILAN"));
        assertFalse(rediz.getHyperLogLogOperations().pfAdd("ITALY", "MILAN"));
        assertFalse(rediz.getHyperLogLogOperations().pfAdd("ITALY", "ROME"));
    }

    @Test
    public void testPfCount() {
        rediz.getHyperLogLogOperations().pfAdd("GERMANY", "BERLIN");
        rediz.getHyperLogLogOperations().pfAdd("GERMANY", "MUNCHEN");
        rediz.getHyperLogLogOperations().pfAdd("GERMANY", "HAMBURG");

        assertTrue("total number of cities in germany must be 3", rediz.getHyperLogLogOperations().pfCount("GERMANY") == 3);
        rediz.getHyperLogLogOperations().pfAdd("GERMANY", "HAMBURG");
        rediz.getHyperLogLogOperations().pfAdd("GERMANY", "MUNCHEN");
        assertTrue("total number of cities in germany must be 3", rediz.getHyperLogLogOperations().pfCount("GERMANY") == 3);
    }

    @Test
    public void testPfMerge() {
        assertTrue("even if they do not exist redis merges them successfully", rediz.getHyperLogLogOperations().pfMerge("CITY", "TOWN"));

        rediz.flushCurrentDB();
        rediz.getHyperLogLogOperations().pfAdd("CITY", "ROME");
        rediz.getHyperLogLogOperations().pfAdd("CITY", "TURIN");
        rediz.getHyperLogLogOperations().pfAdd("CITY", "MILAN");

        rediz.getHyperLogLogOperations().pfAdd("TOWN", "BERLIN");
        rediz.getHyperLogLogOperations().pfAdd("TOWN", "MUNCHEN");
        rediz.getHyperLogLogOperations().pfAdd("TOWN", "HAMBURG");

        assertTrue(rediz.getHyperLogLogOperations().pfMerge("CITY", "TOWN"));
        assertTrue("total number of cities must be 6", rediz.getHyperLogLogOperations().pfCount("CITY") == 6);

        rediz.flushCurrentDB();

        rediz.getHyperLogLogOperations().pfAdd("CITY", "BERLIN");
        rediz.getHyperLogLogOperations().pfAdd("CITY", "TURIN");

        rediz.getHyperLogLogOperations().pfAdd("TOWN", "BERLIN");
        rediz.getHyperLogLogOperations().pfAdd("TOWN", "TURIN");

        assertTrue(rediz.getHyperLogLogOperations().pfMerge("CITY", "TOWN"));
        assertTrue("total number of cities must be 2", rediz.getHyperLogLogOperations().pfCount("CITY") == 2);
    }
}
