/**
 * @author Homeella
 * Date: Apr 29, 2023
 * Time: 11:35:11 PM
 */
package com.agri40.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Declarables S() {
        return new Declarables(
                new DirectExchange("icow.exchange"),
                new Queue("icow.hotSuspect"),
                new Binding("icow.hotSuspect", Binding.DestinationType.QUEUE, "icow.exchange", "Icow-routing-key", null));
    }

}
