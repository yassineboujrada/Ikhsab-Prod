/**
 * @author abdelouahabella
 * Date: Apr 10, 2023
 * Time: 11:08:36 AM
 */
package com.agri40.core.web.listener;

import com.agri40.core.domain.User;
import com.agri40.core.repository.UserRepository;
import com.agri40.core.socket.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telesign.MessagingClient;
import com.telesign.PhoneIdClient;
import com.telesign.RestClient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    // icow.live-stream
    @RabbitListener(queues = { "icow.live-stream" })
    public Map<String, Object> receiveLiveStream(@Payload Map<String, Object> json) {
        json.put("room", "notification" + json.get("cowId"));
        log.info("Receive live stream {}", json);
        if (json.get("type").equals("PEDOMETRE")) {
            Map<String, Object> data = new HashMap<>();
            data.put("room", "notification" + json.get("cowId"));
            // add random value to the json between 1 and 5 called group steps less than 5
            data.put("groupSteps", ((Map<String, Object>)json.get("params")).get("groupSteps") );
            if (json.get("params") != null) {
                Map<String, Object> params = (Map<String, Object>) json.get("params");
                data.put("stepNumber", params.get("stepNumber"));
                data.put("cow_high", Integer.parseInt(params.get("stepNumber").toString()) > ((Integer)data.get("groupSteps")) );
                if (json.get("stepNumber1") != null && json.get("stepNumber2") != null) {
                    log.info(
                        "last stepNbr {} , stepNbr1 {} , stepNbr2 {}",
                        params.get("stepNumber"),
                        json.get("stepNumber1"),
                        json.get("stepNumber2")
                    );
                    if (
                        Integer.parseInt(json.get("stepNumber1").toString()) > ((Integer)data.get("groupSteps")) &&
                        Integer.parseInt(json.get("stepNumber2").toString())  > ((Integer)data.get("groupSteps")) &&
                        Integer.parseInt(params.get("stepNumber").toString())  > ((Integer)data.get("groupSteps"))
                    ) {
                        data.put("cow_hot", true);
                        if((Boolean)json.get("smsService"))
                        SendSMSMessage("la vache avec l'étiquette "+ json.get("cowName") +" pourrait être en chaleur, merci de vérifier",json.get("userPhone").toString());
                        // json.get("cowId");
                        // rabbitTemplate.convertAndSend("icow.cowChaleur", json.get("cowId"));
                        Map<String, Object> notification = new HashMap<>();
                        notification.put("content", "La vache " + json.get("cowId") + " est probablement en chaleur, merci de verifier");
                        // TODO: get real id
                        notification.put("room", "notification"+json.get("userId"));
                        notification.put("createdDateTime", json.get("createdAt"));
                        notification.put("type", "chaleur");
                        notification.put("cowId", json.get("cowId"));
                        notification.put("receiver", json.get("userId"));
                        String notificationId = (String) rabbitTemplate.convertSendAndReceive("icow.save-notification", notification);
                        notification.put("notificationId", notificationId);
                        Event event = new Event(notification);
                        eventPublisher.publishEvent(event);
                        System.out.println("Notification sent");
                    } else {
                        data.put("cow_hot", false);
                    }
                }
                data.put("createdDateTime", json.get("createdAt"));
                Event event = new Event(data);
                eventPublisher.publishEvent(event);
                System.out.println("Live stream sent");
                return null;
            }
            Event event = new Event(json);
            eventPublisher.publishEvent(event);
        }
        return null;
    }

    // listener for send notification to user
    @RabbitListener(queues = { "icow.notification" })
    public String receiveNotification(@Payload Map<String, Object> data) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("content",data.get("content"));
        notification.put("room", "notification" + data.get("receiver"));
        notification.put("createdDateTime", data.get("createdDateTime"));
        notification.put("type", data.get("type"));
        notification.put("cowId", data.get("cowId"));
        notification.put("receiver", data.get("receiver"));
        Event event = new Event(notification);
        eventPublisher.publishEvent(event);
        return null;
    }

    private void SendSMSMessage(String messsage, String Number) {
        String customerId = System.getenv().getOrDefault("CUSTOMER_ID", "7E828603-064D-4FF4-BA0D-E947AD24F8AC");
        String apiKey = System
            .getenv()
            .getOrDefault("API_KEY", "wIIALINC0b/M7NhSmzui66mafLXLodZNaOlDqxALfzLU+uizuy65JlM0xeICTWgJZN23TGrdg+NjEKDqAndMsQ==");

        String phoneNumber = System.getenv().getOrDefault("PHONE_NUMBER", Number);
        String messageType = "ARN";

        try {
            MessagingClient messagingClient = new MessagingClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = messagingClient.message(phoneNumber, messsage, messageType, null);

            if (telesignResponse.ok) {
                System.out.println(
                    "Yay, the message was sent successfully! Reference ID: " + telesignResponse.json.get("reference_id").getAsString()
                );
            } else {
                System.out.println("Oh no, the message failed to send! " + telesignResponse.body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
