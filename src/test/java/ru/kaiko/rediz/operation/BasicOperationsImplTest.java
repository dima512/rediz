package ru.kaiko.rediz.operation;

import org.junit.Test;
import org.mockito.Mockito;
import ru.kaiko.rediz.Connection;

import static org.junit.Assert.*;

// todo : mockito & junit

public class BasicOperationsImplTest {

    @Test
    public void set() {
        Connection connection = Mockito.mock(Connection.class);
        BasicOperationsImpl basicOperations = new BasicOperationsImpl(connection);
        String request = "SET GERMANY BERLIN\r\n";

        Mockito.doNothing().when(connection).write(request);
        Mockito.when(connection.read()).thenReturn("OK");

        assertTrue(basicOperations.set("GERMANY", "BERLIN"));
        Mockito.verify(connection, Mockito.times(1)).write(request);
        Mockito.verify(connection, Mockito.times(1)).read();
    }
}
