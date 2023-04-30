/**
 * @author abdelouahabella
 * Date: Apr 12, 2023
 * Time: 9:29:23 AM
 */
package com.agri40.core.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SocketModule {

    // TODO: review the following line

    private final Logger log = LoggerFactory.getLogger(SocketModule.class);
    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Event.class, DataListener());
        // notification_reseved
        // server.addEventListener("notification_reseved", Event.class, DataListener());
        // server.addEventListener("live_data", Event.class, DataListener());
    }

    private DataListener<Event> DataListener() {
        log.info("********DataListener*******");
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendData(senderClient, data.getData());
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            // String room = client.getHandshakeData().getSingleUrlParam("room");
            // String username = client.getHandshakeData().getSingleUrlParam("room");
            var params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            String username = params.get("username").stream().collect(Collectors.joining());
            client.joinRoom(room);
            log.info("Socket ID[{}] - room[{}] Connected to Notification module through", client.getSessionId().toString(), room);
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            log.info("Socket ID[{}] - room[{}] -  discnnected to Notification module through", client.getSessionId().toString(), room);
        };
    }
}
