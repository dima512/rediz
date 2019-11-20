package ru.kaiko.rediz.operation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.kaiko.rediz.Connection;

import java.io.IOException;

import static org.junit.Assert.*;

public class HyperLogLogOperationsImplTest {

    private Connection connection;
    private HyperLogLogOperationsImpl hyper;

    @Before
    public void setUpEach() {
        this.connection = Mockito.mock(Connection.class);
        this.hyper = new HyperLogLogOperationsImpl(connection);
    }

    @Test
    public void testPfAddScenario1() throws IOException {
        String request = "PFADD GERMANY BERLIN\r\n";

        Mockito.doNothing().when(connection).write(request);
        Mockito.when(connection.read()).thenReturn(Integer.valueOf(1));

        assertTrue(hyper.pfAdd("GERMANY", "BERLIN"));
        Mockito.verify(connection, Mockito.times(1)).write(request);
        Mockito.verify(connection, Mockito.times(1)).read();
    }

    @Test
    public void testPfAddScenario2TryAddToHyperLogLogWrongType() throws IOException {
        String request = "PFADD GERMANY BERLIN\r\n";

        Mockito.doNothing().when(connection).write(request);
        // connection.firstLine() -> -WRONGTYPE Key is not a valid HyperLogLog string value.
        Mockito.when(connection.read()).thenReturn(null);

        assertFalse(hyper.pfAdd("GERMANY", "BERLIN"));
        Mockito.verify(connection, Mockito.times(1)).write(request);
        Mockito.verify(connection, Mockito.times(1)).read();
    }

    @Test
    public void testPfCountScenario1() {
        String request = "PFCOUNT GERMANY\r\n";

        Mockito.doNothing().when(connection).write(request);
        Mockito.when(connection.read()).thenReturn(Integer.valueOf(3));

        assertTrue(hyper.pfCount("GERMANY") == 3);
        Mockito.verify(connection, Mockito.times(1)).write(request);
        Mockito.verify(connection, Mockito.times(1)).read();
    }


    @Test
    public void testPfCountScenario1TryCountHyperLogLogWrongType() {
        String request = "PFCOUNT GERMANY\r\n";

        Mockito.doNothing().when(connection).write(request);
        // connection.firstLine() -> -WRONGTYPE Key is not a valid HyperLogLog string value.
        Mockito.when(connection.read()).thenReturn(null);

        assertTrue(hyper.pfCount("GERMANY") == null);
        Mockito.verify(connection, Mockito.times(1)).write(request);
        Mockito.verify(connection, Mockito.times(1)).read();
    }

    @Test
    public void testPfMergeScenario1() {
        String request = "PFMERGE CITY TOWN\r\n";

        Mockito.doNothing().when(connection).write(request);
        Mockito.when(connection.read()).thenReturn("OK");

        assertTrue(hyper.pfMerge("CITY", "TOWN"));
        Mockito.verify(connection, Mockito.times(1)).write(request);
        Mockito.verify(connection, Mockito.times(1)).read();
    }

    @Test
    public void testPfMergeScenario2TryMergeHyperLogLogWrongType() {
        String request = "PFMERGE CITY TOWN\r\n";

        Mockito.doNothing().when(connection).write(request);
        // connection.firstLine() -> -WRONGTYPE Key is not a valid HyperLogLog string value.
        Mockito.when(connection.read()).thenReturn(null);

        assertFalse(hyper.pfMerge("CITY", "TOWN"));
        Mockito.verify(connection, Mockito.times(1)).write(request);
        Mockito.verify(connection, Mockito.times(1)).read();
    }
}
