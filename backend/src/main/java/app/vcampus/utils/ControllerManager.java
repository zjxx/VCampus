package app.vcampus.utils;

import app.vcampus.controller.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ControllerManager {
    private final UserController userController = new UserController();
    private final StoreController storeController = new StoreController();
    private final ShoppingCartController ShoppingCartController = new ShoppingCartController();
    private final RouteMapping routeMapping = new RouteMapping();
    private final StudentInfoController studentInfoController = new StudentInfoController();
    private final LibraryController libraryController = new LibraryController();
    private final CourseController courseController = new CourseController();
    private final ScoreController scoreController = new ScoreController();
    private final Gson gson = new Gson();

    public ControllerManager() {
        // 注册路由
        routeMapping.registerRoute("login", userController::login);
        routeMapping.registerRoute("addEmail", userController::addEmail);
        routeMapping.registerRoute("sendCode",userController::sendVerificationCode);
        routeMapping.registerRoute("updatePassword", userController::updatePassword);
        routeMapping.registerRoute("updateEmail", userController::updateEmail);

        routeMapping.registerRoute("lib/search", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/addtolist", libraryController::borrowBook);
        routeMapping.registerRoute("lib/returnbook", libraryController::returnBook);
        routeMapping.registerRoute("lib/renewbook", libraryController::delayReturnBook);
        routeMapping.registerRoute("lib/fetchImageUrl", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/search", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/check", libraryController::viewBorrowRecord);
        routeMapping.registerRouteWithParams("lib/add/file_upload", libraryController::addBook);
        routeMapping.registerRouteWithParams("lib/modify/file_upload",libraryController::updateBook);
        routeMapping.registerRoute("lib/viewUserBorrowRecord",libraryController::viewUserBorrowRecord);//查看用户借阅记录


        routeMapping.registerRouteWithParams("shop/addtolist/file_upload", storeController::addItem);//添加商品
        routeMapping.registerRoute("shop/search", storeController::searchItems);//搜索商品
        routeMapping.registerRoute("shop/buy", storeController::handlePurchase);//购买商品
        routeMapping.registerRoute("shop/getAllItems", storeController::getAllItems);//获取所有商品
        routeMapping.registerRoute("shop/getAllTransactions", storeController::getAllTransaction);//获取所有交易
        routeMapping.registerRoute("shop/enterStore",storeController::enterStore);//进入商店展示商品,返回随机商品列表
        routeMapping.registerRoute("shop/getTransactionsByCardNumber", storeController::getTransactionsByCardNumber);//根据卡号获取交易记录
        routeMapping.registerRoute(("shop/deleteItem"), storeController::removeItem);//删除商品
        routeMapping.registerRouteWithParams(("shop/modifyItem/file_upload"), storeController::updateItem);//修改商品信息
        routeMapping.registerRoute(("shop/addItemToCart"), ShoppingCartController::addItemToCart);//添加商品到购物车
        routeMapping.registerRoute(("shop/removeItemFromCart"), ShoppingCartController::removeItemFromCart);//从购物车移除商品
        routeMapping.registerRoute(("shop/viewCart"), ShoppingCartController::viewCart);//查看购物车内容
        routeMapping.registerRoute(("shop/QRbuy"), storeController::QRbuy);



        routeMapping.registerRoute("arc/add", studentInfoController::addStudentStatus);//
        routeMapping.registerRoute("arc/view", studentInfoController::getStudentInfo);
        routeMapping.registerRoute("arc/delete", studentInfoController::deleteStudentStatus);//
        routeMapping.registerRoute("arc/search", studentInfoController::searchStudent);//
        routeMapping.registerRoute("arc/modify", studentInfoController::updateStudentStatus);//
        routeMapping.registerRoute(("arc/modifyPassword"), userController::modifyPassword);//修改密码);

        routeMapping.registerRoute("course/listAll", courseController::showEnrollList);//向登录学生展示方案课程
        routeMapping.registerRoute("course/select", courseController::selectCourse);//学生选择课程
        routeMapping.registerRoute("course/unselect", courseController::unselectCourse);//学生退课
        routeMapping.registerRoute("course/search",courseController::searchCourse);//学生搜索课程
        routeMapping.registerRoute("course/listStudent",courseController::ShowCourseStudent);//老师导出学生选课信息
        routeMapping.registerRoute("course/add",courseController::addCourse);//教务添加课程
        routeMapping.registerRoute("course/delete",courseController::deleteCourse);//教务删除课程
        routeMapping.registerRoute("course/showAll",courseController::showAdminList);//教务查看课程
        routeMapping.registerRoute("course/table",courseController::showStudentCourseTable);//查看课表
        routeMapping.registerRoute("course/modify",courseController::modifyCourse);//教务修改课程信息
        routeMapping.registerRoute("course/getCoursesByTeacherId",courseController::getCoursesByTeacherId);//查看课程信息
        routeMapping.registerRoute("course/getCourseRecord",courseController::getRecordCourses);//老师查看录课信息
        routeMapping.registerRoute("course/getCourseRecordByStudent",courseController::getRecordCoursesByStudent);//学生查看录课信息
        routeMapping.registerRouteWithParams("course/file_upload/video",courseController::videoUpload);//教务添加课程
        routeMapping.registerRoute("course/deleteVideo",courseController::deleteVideo);//教务删除课程


        routeMapping.registerRoute("score/give",scoreController::giveScore);//老师打分
        routeMapping.registerRoute("score/view",scoreController::viewAllScore);//学生查看成绩
        routeMapping.registerRoute("score/list",scoreController::listAllScore);//教务查看成绩
        routeMapping.registerRoute("score/check",scoreController::checkScore);//教务审核成绩
        routeMapping.registerRoute("score/search",scoreController::ViewMyCourseScore);//教师查看课程成绩
        routeMapping.registerRoute("score/modify",scoreController::modifyScore);//教师修改成绩

    }
    public String handleRequest(String jsonData,String ipAddress) {
        // Parse JSON request
        Request request = gson.fromJson(jsonData, Request.class);

        // Call the appropriate controller method based on the request type
        return routeMapping.handleRequest(request.getType(), jsonData, ipAddress);
    }

    public String handleRequestWithParams(String jsonData, String additionalParam,String ipAddress) {
        // Parse JSON request
        Request request = gson.fromJson(jsonData, Request.class);

        // Call the appropriate controller method based on the request type
        return routeMapping.handleRequestWithParams(request.getType(), jsonData, additionalParam, ipAddress);
    }
}