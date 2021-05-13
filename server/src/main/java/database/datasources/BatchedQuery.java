package database.datasources;

import database.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchedQuery {
    private final Statement batchedStatement;

    public BatchedQuery() throws SQLException {
        Connection dbConnection = DBConnection.getInstance();
        batchedStatement = dbConnection.createStatement();
    }

    public void addToBatch(String query) throws SQLException {
        batchedStatement.addBatch(query);
    }

    public void executeBatch() throws SQLException {
        batchedStatement.executeBatch();
        batchedStatement.clearBatch();
    }

    public void clearBatch() throws SQLException {
        batchedStatement.clearBatch();
    }
}
