package com.zlu.test;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class DataPool implements DataSource {

     LinkedList<Connection> pool = new LinkedList<Connection>();

    public DataPool() {
        try {
            for (int i = 0; i < 10; i++) {
                pool.addLast(createConnection());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Connection createConnection()  {

         final Connection conn = ConnUtil.getConn();

        Connection proxyconn = (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(),
                conn.getClass().getInterfaces(),
                new InvocationHandler() {

                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String methodName=method.getName();
                        Object result=null;
                        if(methodName.equals("close")){
                            if(pool.size()>=10){
                                if(conn!=null && !conn.isClosed()){
                                    conn.close();
                                }
                            }else{
                                pool.addLast(conn);
                            }
                        }else{
                            result=method.invoke(conn,args);
                        }
                        return result;
                    }

                });
        return proxyconn;
    }

    public Connection getConnection() throws SQLException {
         if(pool.size()>0)
            return pool.removeFirst();
         else{
             for (int i = 0; i < 5; i++) {
                 pool.addLast(createConnection());
             }
             return pool.removeFirst();
         }

    }

    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public Logger getParentLogger() {
        return null;
    }
}
