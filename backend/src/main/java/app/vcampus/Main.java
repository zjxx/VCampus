package app.vcampus;

import app.vcampus.domain.User;
import app.vcampus.utils.DataBase;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        dataBase.init();
        List<User> users = dataBase.getWhereName("test");
        for (User user : users) {
            System.out.println(user.getUsername()+" "+user.getPassword());
        }
        dataBase.close();
    }
}
