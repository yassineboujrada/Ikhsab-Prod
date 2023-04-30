/**
 * @author abdelouahabella
 * Date: Apr 10, 2023
 * Time: 11:08:36 AM
 */
package com.agri40.core.web.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.agri40.core.domain.User;
import com.agri40.core.repository.UserRepository;
import com.agri40.core.socket.Event;

import reactor.core.publisher.Mono;

@Component
public class SensoringListner {

    private final Logger log = LoggerFactory.getLogger(SensoringListner.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserRepository userRepository;


    @RabbitListener(queues = { "icow.live-stream" })
    Boolean sendLiveData(@Payload Map<String, Object> data){
        log.info("Received live stream {}", data);
        try {
            Event event = new Event(data);
            eventPublisher.publishEvent(event);
            log.info("--- Live stream sent ---");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // listener for send notification to user
    @RabbitListener(queues = { "icow.notification" })
    public void receiveNotification(@Payload Map<String, Object> data) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("content",data.get("content"));
        notification.put("room", "notification" + data.get("receiver"));
        notification.put("createdDateTime", data.get("createdDateTime"));
        notification.put("type", data.get("type"));
        notification.put("cowId", data.get("cowId"));
        notification.put("receiver", data.get("receiver"));
        notification.put("sender", data.get("sender"));
        log.info("Sending notification to room {}", notification.get("room"));
        Event event = new Event(notification);
        eventPublisher.publishEvent(event);
    }


    @RabbitListener(queues = { "icow.profile" })
    public Map<String, Object> receiveProfile(@Payload String id) {
        log.info("Receive profile id {}", id);
        // get user account by id and create a map containing firstname, lastname, email handle the mono type
        Map<String, Object> map = new HashMap<>();
        Mono<User> user = userRepository.findById(id);
        User u = user.block();
        map.put("firstname", u.getFirstName());
        map.put("lastname", u.getLastName());
        map.put("email", u.getEmail());
        return map;
    }
}
