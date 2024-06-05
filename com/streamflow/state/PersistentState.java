package com.streamflow.state;

import com.streamflow.errors.LogExceptEngine;

import java.sql.*;

public class PersistentState {

    private Connection connection;


    public PersistentState() {
        LogExceptEngine.log(PersistentState.class, "StreamFlow Orchestrator is initializing relational database connection.", LogExceptEngine.Level.INFO);
        try {
        connection = DriverManager.getConnection(GlobalMetaVariables.RELATIONAL_CONNECTION_URL());
        } catch (Exception e) {
            LogExceptEngine.logWithNative(e, PersistentState.class, "Error connecting to the relational database. Please check if the connection URL in the `meta.json` file is correct.", LogExceptEngine.Level.ERROR);
        } LogExceptEngine.log(PersistentState.class, "StreamFlow Orchestrator relational database connection initialized successfully.", LogExceptEngine.Level.INFO);
    }

    public void initializeSystem()  {
        LogExceptEngine.log(PersistentState.class, "StreamFlow Orchestrator is initializing system tables.", LogExceptEngine.Level.INFO);
        try {
            String sql = "CREATE TABLE IF NOT EXISTS system_users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username VARCHAR(255),"
                    + "password_hash INTEGER"
                    + ");";
            connection.createStatement().execute(sql);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM system_users");
            if (rs.next() && rs.getInt("count") == 0) {
                // No users, insert default user
                String sql_str = "INSERT INTO system_users(username, password_hash) VALUES(?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql_str)) {
                    pstmt.setString(1, GlobalMetaVariables.DEFAULT_USERNAME());
                    pstmt.setInt(2, GlobalMetaVariables.DEFAULT_PASSWORD_HASH());
                    pstmt.executeUpdate();



                }
            }
            Statement finalCheck = connection.createStatement();
            ResultSet set = finalCheck.executeQuery("SELECT COUNT(*) FROM system_users WHERE username = 'root' AND password_hash = 0");
            if (set.next()) {
                if (1 == set.getInt(1)) {
                    LogExceptEngine.log(PersistentState.class, "Default user initialized for this engine. Please change root user credentials as soon as possible.", LogExceptEngine.Level.WARN);
                }
            }
        } catch (Exception e) {
            LogExceptEngine.logWithNative(e, PersistentState.class, "Error creating system tables. Please check if the database is not corrupted.", LogExceptEngine.Level.ERROR);
        }

    }

    public boolean authenticateUser(String username, String password) {
        try {
            String sql = "SELECT * FROM system_users WHERE username = ? AND password_hash = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setInt(2, password.hashCode());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LogExceptEngine.logWithNative(e, PersistentState.class, "Error db access. Please check if the database is not corrupted.", LogExceptEngine.Level.ERROR);
            return false;
        }
    }



}
