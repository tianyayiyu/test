package com.zlu.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnUtil {

    private static String username="root";
    private static String password="root";
    private static String url="jdbc:mysql://localhost:3306/blog";
    private static Connection conn=null;

   static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn(){
        return conn;
    }

    public static void release(Connection conn){
        try {
            if(conn!=null && !conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        DataPool pool=new DataPool();
        Connection conn1 = pool.getConnection();
        Connection conn2 = pool.getConnection();
        Connection conn3 = pool.getConnection();
        Connection conn4 = pool.getConnection();
        conn1.close();
        System.out.println("现在连接池的大小为 ： "+pool.pool.size());

    }
}
