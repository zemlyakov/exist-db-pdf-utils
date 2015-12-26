package com.mypdf.function;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by gzemlyakov.
 * gzemlyakov@gmail.com
 */
public class FunctionHelper {

    private static final Logger LOG = Logger.getLogger(MergeFunction.class.getName());

    public static File initLogger(Logger logger) {
        try {
            File logFile = File.createTempFile("basex-pdf-utils", ".log");
            FileHandler fileHandler = new FileHandler(logFile.getAbsolutePath());
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }

}
