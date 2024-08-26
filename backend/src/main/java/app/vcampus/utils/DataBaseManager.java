package app.vcampus.utils;

public class DataBaseManager {
    private static DataBase instance;

    private DataBaseManager() {
        // private constructor to prevent instantiation
    }

    public static synchronized DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
            instance.init();
        }
        return instance;
    }
}