/**
 * @author abdelouahabella
 * Date: Apr 12, 2023
 * Time: 9:30:10 AM
 */
package com.agri40.core.socket;

import com.agri40.core.rabbitmq.QueueSender;
import com.agri40.core.socket.enumeration.MessageType;
import com.corundumstudio.socketio.SocketIOClient;
import java.time.Instant;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    @Autowired
    private QueueSender queueSender;

    public void sendSocketMessagingDTO(SocketIOClient senderClient, Map<String, Object> message, String room) {
        Map<String, Object> stream = Map.of("content", message.get("content"), "room", room);
        queueSender.send(stream);
        System.out.println("sendSocketMessagingDTO " + message + " " + room);
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message", message);
            }
        }
    }

    public void sendData(SocketIOClient senderClient, Map<String, Object> message) {
        message.put("messageType", MessageType.CLIENT);
        sendSocketMessagingDTO(senderClient, message, message.get("room").toString());
    }

    public void saveInfoMessagingDTO(SocketIOClient senderClient, Map<String, Object> message, String room) {
        message.put("messageType", MessageType.SERVER);
        sendSocketMessagingDTO(senderClient, message, room);
    }
}
