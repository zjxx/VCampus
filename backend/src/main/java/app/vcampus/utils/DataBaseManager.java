package app.vcampus.utils;

public class DataBaseManager {
    private volatile static DataBase instance;

    private DataBaseManager() {
        // private constructor to prevent instantiation
    }

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