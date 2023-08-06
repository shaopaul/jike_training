package com.jike.sp.cat;

import org.dom4j.DocumentException;

public class SPCat {
    public static void run(String[] args) throws DocumentException, InterruptedException {
        SPCatServer spCatServer = new SPCatServer("com.jike.webapp");
        spCatServer.start();
    }
}