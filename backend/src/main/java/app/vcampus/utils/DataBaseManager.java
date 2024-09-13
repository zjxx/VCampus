package app.vcampus.utils;

/**
 * DataBaseManager is a singleton class responsible for managing the instance of the DataBase.
 * It ensures that only one instance of the DataBase is created and provides a global point of access to it.
 */
public class DataBaseManager {
    private volatile static DataBase instance;

    /**
     * Private constructor to prevent instantiation.
     */
    private DataBaseManager() {
        // private constructor to prevent instantiation
    }

    /**
     * Returns the singleton instance of the DataBase.
     * If the instance is null, it initializes the DataBase and returns it.
     *
     * @return the singleton instance of the DataBase
     */
    public static synchronized DataBase getInstance() {
        if (instance == null) {
            synchronized (DataBase.class){
                instance = new DataBase();
                instance.init();
            }
        }
        return instance;
    }
}