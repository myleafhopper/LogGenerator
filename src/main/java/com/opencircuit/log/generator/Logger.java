package com.opencircuit.log.generator;

public interface Logger {

    void setLogDirectory(String logDirectory);
    void setLogHeader(String applicationName);
    void setLogThread(int threadNumber);
    void startTrace();
    void subTrace();
    void reoccuringSubTrace();
    void getExceptionMessageTrace(Exception e);
    void getExceptionStackTrace(Exception e);
    void setCustomExceptionMessage(String message);
    void endTrace();

}
