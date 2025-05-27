package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryExecutorTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private Logger log;

    private QueryExecutor queryExecutor;

    @BeforeEach
    void setUp() {
        queryExecutor = new QueryExecutor();
    }

    @Test
    void executeCountQuery_WithValidResult_ShouldReturnCount() {
        // Arrange
        String queryString = "SELECT COUNT(u) FROM User u WHERE u.role = :role";
        String paramName = "role";
        String paramValue = "ADMIN";
        Long expectedResult = 5L;

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.setParameter(paramName, paramValue)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedResult);

        // Act
        long result = queryExecutor.executeCountQuery(entityManager, queryString, paramName, paramValue, log);

        // Assert
        assertEquals(expectedResult.longValue(), result);
        verify(entityManager).createQuery(queryString);
        verify(query).setParameter(paramName, paramValue);
        verify(query).getSingleResult();
        verifyNoInteractions(log);
    }

    @Test
    void executeCountQuery_WithNullResult_ShouldReturnZero() {
        // Arrange
        String queryString = "SELECT COUNT(u) FROM User u WHERE u.role = :role";
        String paramName = "role";
        String paramValue = "NONEXISTENT";

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.setParameter(paramName, paramValue)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        long result = queryExecutor.executeCountQuery(entityManager, queryString, paramName, paramValue, log);

        // Assert
        assertEquals(0L, result);
        verify(entityManager).createQuery(queryString);
        verify(query).setParameter(paramName, paramValue);
        verify(query).getSingleResult();
        verifyNoInteractions(log);
    }

    @Test
    void executeCountQuery_WithException_ShouldReturnZeroAndLogError() {
        // Arrange
        String queryString = "INVALID SQL";
        String paramName = "role";
        String paramValue = "ADMIN";
        RuntimeException exception = new RuntimeException("SQL syntax error");

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.setParameter(paramName, paramValue)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(exception);

        // Act
        long result = queryExecutor.executeCountQuery(entityManager, queryString, paramName, paramValue, log);

        // Assert
        assertEquals(0L, result);
        verify(entityManager).createQuery(queryString);
        verify(query).setParameter(paramName, paramValue);
        verify(query).getSingleResult();
        verify(log).error("Error executing count query: {}", exception.getMessage());
    }

    @Test
    void executeCountQuery_WithExceptionOnCreateQuery_ShouldReturnZeroAndLogError() {
        // Arrange
        String queryString = "INVALID SQL";
        String paramName = "role";
        String paramValue = "ADMIN";
        RuntimeException exception = new RuntimeException("Cannot create query");

        when(entityManager.createQuery(queryString)).thenThrow(exception);

        // Act
        long result = queryExecutor.executeCountQuery(entityManager, queryString, paramName, paramValue, log);

        // Assert
        assertEquals(0L, result);
        verify(entityManager).createQuery(queryString);
        verify(log).error("Error executing count query: {}", exception.getMessage());
        verifyNoInteractions(query);
    }

    @Test
    void executeCountQuery_WithExceptionOnSetParameter_ShouldReturnZeroAndLogError() {
        // Arrange
        String queryString = "SELECT COUNT(u) FROM User u WHERE u.role = :role";
        String paramName = "invalidParam";
        String paramValue = "ADMIN";
        RuntimeException exception = new RuntimeException("Parameter not found");

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.setParameter(paramName, paramValue)).thenThrow(exception);

        // Act
        long result = queryExecutor.executeCountQuery(entityManager, queryString, paramName, paramValue, log);

        // Assert
        assertEquals(0L, result);
        verify(entityManager).createQuery(queryString);
        verify(query).setParameter(paramName, paramValue);
        verify(log).error("Error executing count query: {}", exception.getMessage());
    }

    @Test
    void executeSimpleCountQuery_WithValidResult_ShouldReturnCount() {
        // Arrange
        String queryString = "SELECT COUNT(u) FROM User u";
        Long expectedResult = 10L;

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedResult);

        // Act
        long result = queryExecutor.executeSimpleCountQuery(entityManager, queryString, log);

        // Assert
        assertEquals(expectedResult.longValue(), result);
        verify(entityManager).createQuery(queryString);
        verify(query).getSingleResult();
        verifyNoInteractions(log);
    }

    @Test
    void executeSimpleCountQuery_WithNullResult_ShouldReturnZero() {
        // Arrange
        String queryString = "SELECT COUNT(u) FROM EmptyTable u";

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        long result = queryExecutor.executeSimpleCountQuery(entityManager, queryString, log);

        // Assert
        assertEquals(0L, result);
        verify(entityManager).createQuery(queryString);
        verify(query).getSingleResult();
        verifyNoInteractions(log);
    }

    @Test
    void executeSimpleCountQuery_WithException_ShouldReturnZeroAndLogError() {
        // Arrange
        String queryString = "INVALID SQL";
        RuntimeException exception = new RuntimeException("SQL syntax error");

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(exception);

        // Act
        long result = queryExecutor.executeSimpleCountQuery(entityManager, queryString, log);

        // Assert
        assertEquals(0L, result);
        verify(entityManager).createQuery(queryString);
        verify(query).getSingleResult();
        verify(log).error("Error executing simple count query: {}", exception.getMessage());
    }

    @Test
    void executeSimpleCountQuery_WithExceptionOnCreateQuery_ShouldReturnZeroAndLogError() {
        // Arrange
        String queryString = "INVALID SQL";
        RuntimeException exception = new RuntimeException("Cannot create query");

        when(entityManager.createQuery(queryString)).thenThrow(exception);

        // Act
        long result = queryExecutor.executeSimpleCountQuery(entityManager, queryString, log);

        // Assert
        assertEquals(0L, result);
        verify(entityManager).createQuery(queryString);
        verify(log).error("Error executing simple count query: {}", exception.getMessage());
        verifyNoInteractions(query);
    }

    @Test
    void executeCountQuery_WithDifferentParameterTypes_ShouldWork() {
        // Test with Integer parameter
        String queryString = "SELECT COUNT(u) FROM User u WHERE u.age = :age";
        String paramName = "age";
        Integer paramValue = 25;
        Long expectedResult = 3L;

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.setParameter(paramName, paramValue)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedResult);

        // Act
        long result = queryExecutor.executeCountQuery(entityManager, queryString, paramName, paramValue, log);

        // Assert
        assertEquals(expectedResult.longValue(), result);
        verify(entityManager).createQuery(queryString);
        verify(query).setParameter(paramName, paramValue);
        verify(query).getSingleResult();
    }

    @Test
    void executeCountQuery_WithLongResult_ShouldHandleCorrectly() {
        // Arrange - Test when result is already Long type
        String queryString = "SELECT COUNT(u) FROM User u WHERE u.role = :role";
        String paramName = "role";
        String paramValue = "USER";
        Long expectedResult = 100L;

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.setParameter(paramName, paramValue)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedResult);

        // Act
        long result = queryExecutor.executeCountQuery(entityManager, queryString, paramName, paramValue, log);

        // Assert
        assertEquals(expectedResult.longValue(), result);
    }

    @Test
    void executeSimpleCountQuery_WithLongResult_ShouldHandleCorrectly() {
        // Arrange - Test when result is already Long type
        String queryString = "SELECT COUNT(u) FROM User u";
        Long expectedResult = 1000L;

        when(entityManager.createQuery(queryString)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedResult);

        // Act
        long result = queryExecutor.executeSimpleCountQuery(entityManager, queryString, log);

        // Assert
        assertEquals(expectedResult.longValue(), result);
    }
}