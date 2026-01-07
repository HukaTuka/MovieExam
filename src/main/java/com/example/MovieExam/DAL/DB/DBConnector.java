package com.example.MovieExam.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.sql.Connection;

public class DBConnector {

    private static final String PROP_FILE = "config/config.settings";

    private static DBConnector instance;
    private SQLServerDataSource dataSource;

    private DBConnector() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(new File(PROP_FILE)));

            dataSource = new SQLServerDataSource();
            dataSource.setServerName(props.getProperty("Server"));
            dataSource.setDatabaseName(props.getProperty("Database"));
            dataSource.setUser(props.getProperty("User"));
            dataSource.setPassword(props.getProperty("Password"));
            dataSource.setPortNumber(1433);
            dataSource.setTrustServerCertificate(true);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connector", e);
        }
    }

    public static DBConnector getInstance() {
        if (instance == null) {
            instance = new DBConnector();
        }
        return instance;
    }

    public static Connection getConnection() throws SQLServerException {
        return getInstance().dataSource.getConnection();
    }

    public static void main(String[] args) throws Exception {
        Connection connection = DBConnector.getConnection();
        System.out.println(!connection.isClosed());
        connection.close();
    }
}
