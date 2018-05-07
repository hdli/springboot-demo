package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by hdli on 2018-4-19.
 */
public class Test {

    public static void main(String[] args) {

    }

    public static void dbTest(){
        String url = "jdbc:mysql://localhost:3306/seckill";
        String driver = "com.mysql.jdbc.Driver";
        try{
            Class.forName(driver);
        }catch(Exception e){
            System.out.println("无法加载驱动");
        }
        try {
            Connection con = DriverManager.getConnection(url,"test","123456");
            if(!con.isClosed())
                System.out.println("success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
