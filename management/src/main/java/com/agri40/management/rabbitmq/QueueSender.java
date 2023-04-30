/**
 * @author abdelouahabella
 * Date: Apr 10, 2023
 * Time: 11:45:48 AM
 */
package com.agri40.management.rabbitmq;

import java.util.Map;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void send(Map<String, Object> stream) {
        rabbitTemplate.convertAndSend(this.queue.getName(), stream);
    }
}
