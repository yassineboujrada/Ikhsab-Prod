/**
 * @author abdelouahabella
 * Date: Apr 12, 2023
 * Time: 12:39:41 PM
 */
package com.agri40.core.socket;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import java.util.*;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web.Client;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SocketEvents {

    private final SocketIOServer socketIOServer;

    SocketEvents(SocketIOServer server) {
        this.socketIOServer = server;
    }

    @EventListener
    private void onDataSaved(Event event) {
        // Send a message to the client
        System.out.println("-------------------" + event.getData() + "--------------------------");
        Map<String, Object> msg = event.getData();
        // socketIOServer.getBroadcastOperations().sendEvent("notification_reseved", msg);
        // dont use above line, i want to send the message to specific room
        // send to all clients
        for (SocketIOClient client : socketIOServer.getAllClients()) {
            System.out.println("------ " + client.getAllRooms().size());

            // print rooms
            for (String room : client.getAllRooms()) {
                System.out.println("room: " + room);
            }

            // check if the client is in the room

            if (client.getAllRooms().contains(msg.get("room").toString())) {
                client.sendEvent("notification_received", msg);
            }
            // client.sendEvent("notification_received", msg);
        }
        // System.out.println(x.getClients());
    }
}
// System.out.println("-------------------"+event.getData()+"--------------------------");
// Map<String, Object> msg = event.getData();
// // get all clients in the room
// Collection<SocketIOClient> clients = socketIOServer.getRoomOperations("notificationo").getClients();for(
// SocketIOClient client:clients)
// {
//             System.out.println(client.getAllRooms());
//             client.sendEvent("notification_reseved", msg);
//         }
