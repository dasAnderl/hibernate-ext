package com.anderl.hibernate.ext.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by ga2unte on 22/07/13.
 */
public class LogTimer {

    private static Logger log = LoggerFactory.getLogger(LogTimer.class);

    private StackTraceElement stackTraceElement;
    private long start;
    private int stackIndex = 2;

    public LogTimer enter() {
        stackIndex = 3;
        return enter(null);
    }

    public LogTimer enter(String logMsg) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        stackTraceElement = stackTraceElements[stackIndex];
        log.info("Entered " + stackTraceElement + " " + (logMsg != null ? logMsg : ""));
        start = System.currentTimeMillis();
        return this;
    }

    public LogTimer enter(String logMsg, Object... logMsgParams) {
        stackIndex = 3;
        if (logMsg == null) {
            return enter(null);
        }
        logMsg = logMsg.replace("{}", "%s");
        return enter(String.format(logMsg, logMsgParams));
    }


    public void exit() {
        exit(null);
    }

    public void exit(String logMsg) {
        long duration = System.currentTimeMillis() - start;
        logMsg = StringUtils.isEmpty(logMsg) ? "" : " - " + logMsg;
        String msg = String.format("%s.%s was running for %s ms %s",
                stackTraceElement.getClassName().substring(stackTraceElement.getClassName().lastIndexOf(".")+1),
                stackTraceElement.getMethodName(),
                duration,
                logMsg);
        if (duration > 1500) {
            log.warn(msg);
        } else {
            log.info(msg);
        }
    }

    public void exit(String logMsg, Object... logMsgParams) {
        if (logMsg == null) {
            exit(null);
        }
        logMsg = logMsg.replace("{}", "%s");
        exit(String.format(logMsg, logMsgParams));
    }


}

