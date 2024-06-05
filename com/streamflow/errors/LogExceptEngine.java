package com.streamflow.errors;

import com.streamflow.state.GlobalMetaVariables;

import java.io.*;

public class LogExceptEngine implements Thread.UncaughtExceptionHandler {

    public static boolean DEBUG = false;
    private static final int TAB_SIZE = 8; // Tab size
    private static final int FIXED_CLASS_NAME_LENGTH = 24; // Fixed length for class names

    @Override
    public void uncaughtException(Thread t, Throwable e)  {

        try {
            BufferedWriter writer = new BufferedWriter(new BufferedWriter(new FileWriter(GlobalMetaVariables.LOG_DATA_STORAGE_PATH())));
            //write date and time;
            writer.write("Date: " + java.time.LocalDate.now() + " Time: " + java.time.LocalTime.now());
            writer.newLine();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            writer.write(sw.toString());
            writer.flush();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        logWithNative((Exception) e, LogExceptEngine.class, "Uncaught exception in thread: " + t.getName(), Level.ERROR);
        log(LogExceptEngine.class, "Please check the log file for more details.", Level.INFO);
    }

    public enum Level {
        WARN, ERROR, INFO
    }

    public static void log(Class<?> reportingObject, String message, Level level) {
        String formattedClassName = formatClassName(reportingObject.getSimpleName());
        String tabs = calculateTabs(formattedClassName, message);
        switch (level) {
            case INFO:
                System.out.printf("[INFO][%s]:%s%s%n", formattedClassName, tabs, message);
                break;
            case WARN:
                System.err.printf("[WARN][%s]:%s%s%n", formattedClassName, tabs, message);
                break;
            case ERROR:
                System.err.printf("[ERROR][%s]:%s%s%n", formattedClassName, tabs, message);
                if (DEBUG) {
                    new Exception().printStackTrace();
                }
                System.exit(1);
                break;
        }
    }

    public static void logWithNative(Exception e, Class<?> reportingObject, String message, Level level) {
        String formattedClassName = formatClassName(reportingObject.getSimpleName());
        String tabs = calculateTabs(formattedClassName, message);
        String errorMessage = String.format("[ERROR][%s]:%s%s\n%sNative Error: %s", formattedClassName, tabs, message, tabs, e.getMessage());
        switch (level) {
            case INFO:
                System.out.printf("[INFO][%s]:%s%s\n%sNative Error: %s%n", formattedClassName, tabs, message, tabs, e.getMessage());
                break;
            case WARN:
                System.err.printf("[WARN][%s]:%s%s\n%sNative Error: %s%n", formattedClassName, tabs, message, tabs, e.getMessage());
                break;
            case ERROR:
                System.err.printf("%s%n", errorMessage);
                if (DEBUG) {
                    e.printStackTrace();
                }
                System.exit(1);
                break;
        }
    }

    private static String formatClassName(String className) {
        if (className.length() > FIXED_CLASS_NAME_LENGTH) {
            return className.substring(0, FIXED_CLASS_NAME_LENGTH);
        } else {
            return String.format("%-" + FIXED_CLASS_NAME_LENGTH + "s", className);
        }
    }

    private static String calculateTabs(String formattedClassName, String message) {
        int tabCount = 2;
        return "\t".repeat(tabCount);
    }

}