package database.datasources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Interface for the different data sources located in the trading platform database.
 * This interface should be implemented by AssetDataSource, AssetTypeDataSource,
 * OrganisationalUnitDataSource, TradeDataSource, and UserDataSource.
 * This interface should act as a base template for the shared functionality of each datasource.
 * Each datasource should extend on this interface and implement datasource specific methods.
 */
public abstract class AbstractDataSource<T> {

    /**
     * Convert a ResultSet object to an Object of type T.
     * @param results The ResultSet to extract information from
     * @return Constructed object of type T
     * @throws SQLException If ResultSet extraction methods fail
     */
    protected abstract T resultSetToObject(ResultSet results) throws SQLException;

    /** Finds instance of object in database by id
     * and returns a java object representation. 
     * 
     * @param id - UUID generated object unique identifier
     * @return specific object based on implementation and datasource - User or Trade etc.
     */
    public abstract T getById(UUID id) throws SQLException;

    /** Returns a list of all instances of an object type located in the database. 
     * 
     * @return list of objects specific to the datasource, and implementation - Assets or Users etc.
     */
     public abstract ArrayList<T> getAll() throws SQLException;


    /** Creates a new object type in the database, eg. new User; new AssetType; etc.
     * 
     * @param newObject - new object to be created. eg. User or OrganisationalUnit etc.
     */
     public abstract void createNew(T newObject) throws SQLException;


    /** Updates object attributes in the database one at a time by id -
     * meant to be overloaded.
     *
     * @param id - object's id that is being updated.
     * @param attribute - attribute that is being updated
     * @param value - value of the attribute that is being updated
     * @return boolean indicating whether the update into the database was a success.
     */
     public abstract void updateByAttribute(UUID id, String attribute, T value) throws SQLException;


    /** Checks to see if an object by their id exists within the database.
     *
     * @param id - id of the object
     * @return boolean indicating whether the object exists.
     */
     public abstract boolean checkExistById(UUID id) throws SQLException;

    /** Deletes instance in the database based on id
     *
     * @param id - id of the object
     */
    public abstract void deleteById(UUID id) throws SQLException;

}
