/**
 * @author abdelouahabella
 * Date: Apr 12, 2023
 * Time: 9:17:50 AM
 */
package com.agri40.core.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {

    // TODO: review the following line

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebSocketConfig.class);

    @Bean
    public CommandLineRunner websocketServerRunner(SocketIOServer server) {
        log.info("-------Starting WebSocket server------");
        return args -> {
            server.start();
        };
    }

    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.port}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {
        log.info(" host: " + host + " port: " + port);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setContext("/socket.io");
        return new SocketIOServer(config);
    }
}
