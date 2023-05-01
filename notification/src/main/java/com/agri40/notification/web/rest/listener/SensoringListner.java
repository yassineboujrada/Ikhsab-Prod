/**
 * @author abdelouahabella
 * Date: Apr 10, 2023
 * Time: 11:08:36 AM
 */
package com.agri40.notification.web.rest.listener;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.agri40.notification.domain.Notification;
import com.agri40.notification.repository.NotificationRepository;
import com.telesign.MessagingClient;
import com.telesign.RestClient;

@Component
public class SensoringListner {

    private final Logger log = LoggerFactory.getLogger(SensoringListner.class);


    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = { "icow.hotSuspect" })
    public void receive(@Payload Map<String,Object> data) {
        // check if the last notification is not seen if so UPDATE TIME
        Notification notification = notificationRepository.findFirstByCowIdAndSeenOrderByDateDesc(data.get("cowId").toString(), false);
        if (notification != null) {
            notification.setDate(Instant.now());
            notificationRepository.save(notification);
            log.info("Not Seen Notification updated");
        }
        else {

            // create and save notification
            Notification notification1 = new Notification();
            notification1.setContent("Your cow is a hot suspect");
            notification1.setCowId(data.get("cowId").toString());
            notification1.setSender("System");
            notification1.setReceiver(data.get("userId").toString());
            notification1.setDate(Instant.now());
            notification1.seen(false);
            Notification result = notificationRepository.save(notification1);

            // CONVERT NOTIFICATION TO MAP
            Map<String, Object> notificationmap = new HashMap<>();
            notificationmap.put("content", result.getContent());
            notificationmap.put("room", "notification" + result.getReceiver());
            notificationmap.put("createdDateTime", result.getDate().toString());
            notificationmap.put("type", "Warning");
            notificationmap.put("cowId", result.getCowId()); 
            notificationmap.put("receiver", result.getReceiver());
            notificationmap.put("sender", result.getSender());

            // send notification 
            rabbitTemplate.convertAndSend("icow.notification", notificationmap);
            log.info("New Notification sent");



            // send sms
            if ((Boolean)data.get("smsService")){
                SendSMSMessage("e-khsab (Pôle Digital) la vache Tag N° " + data.get("cowName").toString()
                        + " pourrait-être en chaleur. Merci de vérifier 🙂", "0627049227");

                SendSMSMessage("e-khsab (Pôle Digital) la vache Tag N° " + data.get("cowName").toString()
                        + " pourrait-être en chaleur. Merci de vérifier 🙂", "0677894124");

                SendSMSMessage("e-khsab (Pôle Digital) la vache Tag N° " + data.get("cowName").toString()
                        + " pourrait-être en chaleur. Merci de vérifier 🙂", "0672820760");

                        SendSMSMessage("e-khsab (Pôle Digital) la vache Tag N° " + data.get("cowName").toString()
                        + " pourrait-être en chaleur. Merci de vérifier 🙂", "0661484142");
                        
                SendSMSMessage("e-khsab (Pôle Digital) la vache Tag N° " + data.get("cowName").toString()
                        + " pourrait-être en chaleur. Merci de vérifier 🙂", data.get("userPhone").toString());
            log.info("////////////     SMS sent     ////////////");
            }
            else
                log.info("SMS Service is disabled");

        }
        
        // check if the time betwen last saved notification and now is more than 1 minutes and notification
    }

    private void SendSMSMessage(String messsage, String Number) {
        String customerId = System.getenv().getOrDefault("CUSTOMER_ID", "7E828603-064D-4FF4-BA0D-E947AD24F8AC");
        String apiKey = System
                .getenv()
                .getOrDefault("API_KEY",
                        "wIIALINC0b/M7NhSmzui66mafLXLodZNaOlDqxALfzLU+uizuy65JlM0xeICTWgJZN23TGrdg+NjEKDqAndMsQ==");

        String phoneNumber = System.getenv().getOrDefault("PHONE_NUMBER", Number);
        String messageType = "ARN";

        try {
            MessagingClient messagingClient = new MessagingClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = messagingClient.message(phoneNumber, messsage, messageType,
                    null);

            if (telesignResponse.ok) {
                System.out.println(
                        "Yay, the message was sent successfully! Reference ID: "
                                + telesignResponse.json.get("reference_id").getAsString());
            } else {
                System.out.println("Oh no, the message failed to send! " + telesignResponse.body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = { "icow.save-notification" })
    public String receiver(Map<String, Object> data) {
        log.info("Received data from icow.save-notification queue : "+data);
        // save notification in database
        Notification notification = new Notification();
        notification.setContent(data.get("content").toString());
        notification.setCowId(data.get("cowId").toString());
        // if sender is null then it's a system notification
        notification.setSender(data.get("sender") != null ? data.get("sender").toString() : "System");
        // if receiver is null then it's a system notification
        if (data.get("receiver") == null) {
            notification.setReceiver("System");
        } else {
            notification.setReceiver(data.get("receiver").toString());
        }
        notification.setDate(Instant.now());
        notification.seen(false);
        Notification result = notificationRepository.save(notification);
        log.info("Notification saved in database");
        return result.getId();
    }
}
