package com.example.MovieExam.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {

    private static final String PROP_FILE = "config/config.settings";
    private static DBConnector instance;
    private SQLServerDataSource dataSource;
    private static boolean connectionAvailable = true;
    private static String lastError = "";

    private DBConnector() {
        try {
            File configFile = new File(PROP_FILE);

            if (!configFile.exists()) {
                connectionAvailable = false;
                lastError = "Configuration file not found: " + configFile.getAbsolutePath();
                System.err.println("WARNING: " + lastError);
                System.err.println("Application will run in offline mode - database features disabled.");
                return;
            }

            Properties props = new Properties();
            props.load(new FileInputStream(configFile));

            String server = props.getProperty("Server");
            String database = props.getProperty("Database");
            String user = props.getProperty("User");
            String password = props.getProperty("Password");

            if (server == null || database == null || user == null || password == null) {
                connectionAvailable = false;
                lastError = "Missing required database properties (Server, Database, User, Password)";
                System.err.println("WARNING: " + lastError);
                System.err.println("Application will run in offline mode - database features disabled.");
                return;
            }

            dataSource = new SQLServerDataSource();
            dataSource.setServerName(server);
            dataSource.setDatabaseName(database);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setPortNumber(1433);
            dataSource.setTrustServerCertificate(true);

            // Test the connection
            testConnection();

        } catch (Exception e) {
            connectionAvailable = false;
            lastError = "Database initialization error: " + e.getMessage();
            System.err.println("WARNING: " + lastError);
            System.err.println("Application will run in offline mode - database features disabled.");
        }
    }

    public static DBConnector getInstance() {
        if (instance == null) {
            instance = new DBConnector();
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            getInstance();
        }

        if (!connectionAvailable) {
            throw new SQLException(
                    "Database is not available: " + lastError +
                            "\n\nPlease check:\n" +
                            "1. SQL Server is running\n" +
                            "2. config/config.settings file exists with correct credentials\n" +
                            "3. TCP/IP is enabled in SQL Server Configuration Manager\n" +
                            "4. Firewall allows port 1433"
            );
        }

        if (instance.dataSource == null) {
            throw new SQLException("DataSource is not initialized");
        }

        return instance.dataSource.getConnection();
    }

    public static boolean isConnectionAvailable() {
        if (instance == null) {
            getInstance();
        }
        return connectionAvailable;
    }

    public static String getLastError() {
        return lastError;
    }

    private void testConnection() {
        try (Connection conn = dataSource.getConnection()) {
            connectionAvailable = true;
            lastError = "";
            System.out.println("✓ Database connection successful");
            System.out.println("  Server: " + dataSource.getServerName());
            System.out.println("  Database: " + dataSource.getDatabaseName());
        } catch (Exception e) {
            connectionAvailable = false;
            lastError = e.getMessage();
            System.err.println("✗ Database connection failed: " + lastError);
            System.err.println("Application will continue in offline mode.");
        }
    }

    public static void retryConnection() {
        if (instance != null) {
            instance.testConnection();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Testing Database Connection...");
        System.out.println("================================");

        DBConnector connector = DBConnector.getInstance();

        if (isConnectionAvailable()) {
            try (Connection connection = getConnection()) {
                System.out.println("\n✓ Connection test successful!");
                System.out.println("Connection is open: " + !connection.isClosed());
            }
        } else {
            System.err.println("\n✗ Connection test failed!");
            System.err.println("Error: " + getLastError());
            System.err.println("\nApplication can still run in offline mode.");
        }
    }
}