package com.chenshuyi.netty.nio;

import java.io.IOException;

/**
 * @auhotr Ron Chan
 * @date 2018-10-26
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        TimeServerHandler timeServer = new TimeServerHandler(port);
        new Thread(timeServer, "NIO-TimeServerHandler-001").start();
    }
}
