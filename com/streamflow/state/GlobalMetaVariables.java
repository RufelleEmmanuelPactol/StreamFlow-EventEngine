package com.streamflow.state;

import com.google.gson.Gson;
import com.streamflow.errors.LogExceptEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GlobalMetaVariables {

    private static final int DEFAULT_PASSWORD_HASH = "".hashCode();
    private static final String DEFAULT_USERNAME = "root";
    private static int PORT;
    private static int PROCESS_THREAD_COUNT;

    private static String RELATIONAL_CONNECTION_URL;

    public static String RELATIONAL_CONNECTION_URL() {
        return RELATIONAL_CONNECTION_URL;
    }

    public static int DEFAULT_PASSWORD_HASH() {
        return DEFAULT_PASSWORD_HASH;
    }

    public static String DEFAULT_USERNAME() {
        return DEFAULT_USERNAME;
    }


    public static int PORT() {
        return PORT;
    }

    public static int PROCESS_THREAD_COUNT() {
        return PROCESS_THREAD_COUNT;
    }
    public static String LOG_DATA_STORAGE_PATH;

    public static String LOG_DATA_STORAGE_PATH() {
        return LOG_DATA_STORAGE_PATH;
    }

    public static void initializeMetaVariables() {
        LogExceptEngine.log(GlobalMetaVariables.class, "StreamFlow Orchestrator is initializing variables and threads.", LogExceptEngine.Level.INFO);
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/streamflow/meta/meta.json"))){
            Gson gson = new Gson();
            HashMap meta = gson.fromJson(reader, HashMap.class);
            PORT = (int)(double) meta.get("port");
            PROCESS_THREAD_COUNT = (int)(double) meta.get("threadCount");
            RELATIONAL_CONNECTION_URL = (String) meta.get("relationalConnectionURL");
            LOG_DATA_STORAGE_PATH = (String) meta.get("logDataStoragePath");

        } catch (IOException e) {
            LogExceptEngine.logWithNative(e, GlobalMetaVariables.class, "Error reading meta.json. Please check if the formatting is not corrupted or that the file exists.", LogExceptEngine.Level.ERROR);
        }

        LogExceptEngine.log(GlobalMetaVariables.class, "StreamFlow Orchestrator meta variables at `meta.json` initialized successfully.", LogExceptEngine.Level.INFO);
    }
}
