/**
 * @author abdelouahabella
 * Date: Apr 10, 2023
 * Time: 11:08:36 AM
 */
package com.agri40.notification.web.rest.listener;

import com.agri40.notification.domain.Message;
import com.agri40.notification.domain.Notification;
import com.agri40.notification.domain.enumeration.MessageType;
import com.agri40.notification.repository.MessageRepository;
import com.agri40.notification.repository.NotificationRepository;
import java.time.Instant;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SensoringListner {

    private final Logger log = LoggerFactory.getLogger(SensoringListner.class);

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @RabbitListener(queues = { "icow.message" })
    public void receive(@Payload Map<String, Object> fileBody) {
        // check construct a message table from fileBody
        Message message = new Message();
        message.setCreatedDateTime(Instant.now());
        message.setRoom(fileBody.get("room").toString());
        message.setContent(fileBody.get("content").toString());
        message.setUsername(fileBody.get("username").toString());
        message.setMessageType(MessageType.CLIENT);
        messageRepository.save(message);

        System.out.println("Message " + fileBody);
    }

    //    icow.deviceid
    @RabbitListener(queues = { "icow.save-notification" })
    public String receiver(Map<String, Object> data) {
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
