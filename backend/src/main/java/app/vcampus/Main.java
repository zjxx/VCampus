package app.vcampus;

import app.vcampus.domain.User;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.server.NettyServer;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        DataBase dataBase = new DataBase();
        dataBase.init();
        List<User> users = dataBase.getWhere(User.class,"username","test");
        for (User user : users) {
            System.out.println(user.getUsername()+" "+user.getPassword());
        }
        dataBase.close();

        System.out.println("Server started at port 8066");

        NettyServer nettyServer = new NettyServer(8066);
        nettyServer.start();


    }
}
