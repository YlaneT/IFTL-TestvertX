package com.example.starter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.*;

public class ReadProperties {
    private static final Logger  log         = LoggerFactory.getLogger(ReadProperties.class);
    private static       boolean alreadyRead = false;
    private static Map<String, String> parameters = new HashMap<String, String>();

    private static Properties conf = null;

    public static Map<String, String> getParameters() {
        return parameters;
    }
    public static String getParameter(String paramName) {
        String param  = "";
        param = parameters.get(paramName);
        if (null==param){
            System.out.println("parameter : " + paramName + " doesn't exist" );
            param="";
        }
        return param;
    }

    public static void read(String JVM_param) {
        try {
            if (!alreadyRead) {
                conf = new Properties();
                FileInputStream is = new FileInputStream(System.getProperty("conf.param"));
                conf.load(is);
                is.close();
                System.out.println("*********** P R O P E R T I E S ***********");

                // liste des valeurs
                Enumeration<?> e = conf.propertyNames();
                while (e.hasMoreElements()) {
                    String propertyName = (String) e.nextElement();
                    parameters.put(propertyName, conf.getProperty(propertyName).trim());
                    log.info(propertyName + "=" + (String) parameters.get(propertyName));
                }
                System.out.println("*********** P R O P E R T I E S ***********");
            }
            alreadyRead = true;

        } catch (Exception e) {
            System.err.println("Error in ReadProperties : " + e.getMessage());
        }

    }
}
