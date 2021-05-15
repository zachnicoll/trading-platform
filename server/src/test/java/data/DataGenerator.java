package data;

import java.sql.SQLException;

/**
 * Interface that should be implemented by all test Data Generators.
 * Ensures that the generators are destroying the test data that they created.
 */
public interface DataGenerator {

    /**
     * All entries in tables that were created with particular IDs should be deleted from the database here.
     */
    void destroyTestData() throws SQLException;
}
