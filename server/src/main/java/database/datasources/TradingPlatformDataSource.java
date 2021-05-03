package database.datasources;

import java.util.List;


/**
 * Interface for the different data sources located in the trading platform database.
 * This interface should be implemented by AssetDataSource, AssetTypeDataSource,
 * OrganisationalUnitDataSource, TradeDataSource, and UserDataSource.
 * This interface should act as a base template for the shared functionality of each datasource.
 * Each datasource should extend on this interface and implement datasource specific methods.
 */
interface TradingPlatformDataSource<T> {

    /** Finds instance of object in database by id
     * and returns a java object representation. 
     * 
     * @param id - UUID generated object unique identifier
     * @return specific object based on implementation and datasource - User or Trade etc.
     */
     T getById(String id);


    /** Returns a list of all instances of an object type located in the database. 
     * 
     * @return list of objects specific to the datasource, and implementation - Assets or Users etc.
     */
     List<T> getAll();


    /** Creates a new object type in the database, eg. new User; new AssetType; etc.
     * 
     * @param newObject - new object to be created. eg. User or OrganisationalUnit etc.
     * @return boolean indicating if the creation into the database was a success.
     */
     boolean createNew(T newObject);


    /** Updates object attributes in the database one at a time by id -
     * meant to be overloaded.
     *
     * @param id - object's id that is being updated.
     * @param attribute - attribute that is being updated
     * @param value - value of the attribute that is being updated
     * @return boolean indicating whether the update into the database was a success.
     */
     boolean updateByAttribute(String id, String attribute, T value);


    /** Checks to see if an object by their id exists within the database.
     *
     * @param id - id of the object
     * @return boolean indicating whether the object exists.
     */
     boolean checkExistById(String id);

    /** Deletes instance in the database based on id
     *
     * @param id - id of the object
     */
    void deleteById(String id);

}
