package app.vcampus.utils;

import app.vcampus.controller.StoreController;
import app.vcampus.controller.StoreTransactionController;
import app.vcampus.controller.StudentInfoController;
import app.vcampus.controller.UserController;
import app.vcampus.controller.LibraryController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ControllerManager {
    private final UserController userController = new UserController();
    private final StoreController storeController = new StoreController();
    private final RouteMapping routeMapping = new RouteMapping();
    private final StudentInfoController studentInfoController = new StudentInfoController();
    private final LibraryController libraryController = new LibraryController();
    private final Gson gson = new Gson();

    public ControllerManager() {
        // Register routes
        routeMapping.registerRoute("login", userController::login);

        routeMapping.registerRoute("lib/search", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/addtolist", libraryController::borrowBook);
        routeMapping.registerRoute("lib/returnbook", libraryController::returnBook);
        routeMapping.registerRoute("lib/renewbook", libraryController::delayReturnBook);
        routeMapping.registerRoute("lib/fetchImageUrl", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/search", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/check", libraryController::viewBorrowRecord);
        routeMapping.registerRouteWithParams("lib/file_upload", libraryController::addBook);

        routeMapping.registerRoute("shop/addtolist", storeController::addItem);
        routeMapping.registerRoute("searchItems", storeController::searchItems);
        routeMapping.registerRoute("purchase", storeController::handlePurchase);
        routeMapping.registerRoute("getAllItems", storeController::getAllItems);
        routeMapping.registerRoute("getTransactions", storeController::getAllTransaction);

        routeMapping.registerRoute("searchItems", storeController::searchItems);
        routeMapping.registerRoute("purchase", storeController::handlePurchase);
        routeMapping.registerRoute("shop/search", storeController::searchItems);
        routeMapping.registerRoute("shop/buy", storeController::handlePurchase);
        routeMapping.registerRoute("getAllItems", storeController::getAllItems);
        routeMapping.registerRoute("getTransactions", storeController::getAllTransaction);
        routeMapping.registerRoute("enterStore", storeController::enterStore);
        routeMapping.registerRoute("getTransactionsByCardNumber", storeController::getTransactionsByCardNumber);

        routeMapping.registerRoute("arc/add", studentInfoController::addStudentStatus);
        routeMapping.registerRoute("arc/view", studentInfoController::getStudentInfo);
        routeMapping.registerRoute("arc/delete", studentInfoController::deleteStudentStatus);
        routeMapping.registerRoute("arc/search", studentInfoController::searchStudent);
        routeMapping.registerRoute("arc/modify", studentInfoController::updateStudentStatus);
        routeMapping.registerRoute("arc/modifyPassword", userController::modifyPassword);
    }

    public String handleRequest(String jsonData) {
        // Parse JSON request
        Request request = gson.fromJson(jsonData, Request.class);

        // Call the appropriate controller method based on the request type
        return routeMapping.handleRequest(request.getType(), jsonData);
    }

    public String handleRequestWithParams(String jsonData, String additionalParam) {
        // Parse JSON request
        Request request = gson.fromJson(jsonData, Request.class);

        // Call the appropriate controller method based on the request type
        return routeMapping.handleRequestWithParams(request.getType(), jsonData, additionalParam);
    }
}