package ru.kaiko.rediz;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class RedizTest {

    @Test
    public void testConstructor() {
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.getPort()).thenReturn(6379);
        Mockito.when(connection.getHost()).thenReturn("localhost");
        Rediz rediz = new Rediz(connection);

        Mockito.verify(connection, Mockito.times(1)).getPort();
        Mockito.verify(connection, Mockito.times(1)).getHost();
    }
}
