package com.opencircuit.log.generator;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ImplementsLogger implements Logger {

    private String logDirectory;
    private String applicationName;
    private int threadNumber;
    private File logFile;
    private StringBuilder dashes;
    private FileWriter fileWriter = null;
    private BufferedWriter bufferedWriter = null;

    public ImplementsLogger()
    {
        logDirectory = null;
        applicationName = null;
        threadNumber = 4;
        logFile = null;
    }

    //******************************************************************************************************************
    //******************************************************************************************************************

    public void setLogDirectory(String logDirectory)
    {
        this.logDirectory = logDirectory;
    }

    public void setLogHeader(String applicationName)
    {
        this.applicationName = applicationName;
    }

    public void setLogThread(int threadNumber) { this.threadNumber = threadNumber; }

    public void startTrace() {

        closeFileAccess();
        initializeLogSettings();
        dashes = new StringBuilder();
        dashes.append("-");
        openFileAccess();
        writeToLogFile("\n" + getExecutionTrace(dashes.toString(), " LOG ") + "\n");
    }

    public void subTrace() {

        dashes.append("-");
        writeToLogFile(getExecutionTrace(dashes.toString(), " LOG ") + "\n");
    }

    public void reoccuringSubTrace() {

        String dashes = "-" + this.dashes.toString();
        writeToLogFile(getExecutionTrace(dashes, " LOG ") + "\n");
    }

    public void getExceptionMessageTrace(Exception e) {

        writeToLogFile(getExecutionTrace(dashes.toString(), "ERROR") + " >>> ");
        writeToLogFile(e.toString() + "\n");
    }

    public void getExceptionStackTrace(Exception e) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);

        writeToLogFile(getExecutionTrace(dashes.toString(), "ERROR") + " >>> ");
        writeToLogFile(stringWriter.toString() + "\n");
    }

    public void setCustomExceptionMessage(String message) {

        writeToLogFile(getExecutionTrace(dashes.toString(), "ERROR") + " >>> ");
        writeToLogFile(message + "\n");
    }

    public void endTrace() {

        writeToLogFile("\n");
        closeFileAccess();
        dashes = null;
    }

    //******************************************************************************************************************
    //******************************************************************************************************************
    //******************************************************************************************************************

    private void initializeLogSettings()
    {
        if (logFile != null) { return; }
        initializeLogDirectory();
        initializeLogFile();
        openFileAccess();
        initializeLogHeader();
        closeFileAccess();
    }

    private void initializeLogDirectory()
    {
        if (logDirectory == null) { logDirectory = System.getProperty("user.dir") + "\\Logs"; }
        new File(logDirectory).mkdirs();
    }

    private void initializeLogFile() {

        try {

            String date = getCurrentDate();
            String time = getCurrentTime().replace(":", "-");
            String filePath = logDirectory + "\\LOG " + date + " (" + time + ").txt";
            logFile = new File(filePath);
            logFile.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeLogHeader()
    {
        if (applicationName == null) { writeToLogFile("\n<--- Execution Trace Log --->\n"); }
        else { writeToLogFile("\n<--- " + applicationName + " Execution Trace Log --->\n"); }
    }

    private String getCurrentDate() {

        String format = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return simpleDateFormat.format(timestamp);
    }

    private String getCurrentTime() {

        String format = "hh:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return simpleDateFormat.format(timestamp);
    }

    private String getClassTrace() {

        String className = Thread
                .currentThread()
                .getStackTrace()[threadNumber]
                .getClassName();

        if (className.contains(".")) {
            className = className.substring(className.lastIndexOf(".") + 1);
        }

        return className;
    }

    private String getMethodName() {

        return Thread.currentThread()
                .getStackTrace()[threadNumber]
                .getMethodName();
    }

    private String getExecutionTrace(String dashes, String logType) {

        String date = getCurrentDate();
        String time = getCurrentTime();
        String logTime = date + " " + time + " [" + logType + "]: ";
        String className = getClassTrace();
        String methodName = getMethodName();

        return logTime + dashes + "> " + className + ": " + methodName;
    }

    private void openFileAccess() {

        try {

            fileWriter = new FileWriter(logFile, true);
            bufferedWriter = new BufferedWriter(fileWriter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToLogFile(String contents) {

        try { bufferedWriter.write(contents); }
        catch (Exception e) { e.printStackTrace(); }
    }

    private void closeFileAccess() {

        closeBufferedWriter();
        closeFileWriter();
    }

    private void closeBufferedWriter() {

        try { if (bufferedWriter != null) { bufferedWriter.close(); } }
        catch (Exception e) { e.printStackTrace(); }
    }

    private void closeFileWriter() {

        try { if (fileWriter != null) { fileWriter.close(); } }
        catch (Exception e) { e.printStackTrace(); }
    }
}