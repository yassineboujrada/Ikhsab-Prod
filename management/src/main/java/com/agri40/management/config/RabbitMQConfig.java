/**
 * @author Homeella
 * Date: Apr 29, 2023
 * Time: 11:33:54 PM
 */
package com.agri40.management.config;

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
    public Declarables D() {
        return new Declarables(
            new DirectExchange("icow.exchange"),
            new Queue("icow.deviceid"),
            new Binding("icow.deviceid", Binding.DestinationType.QUEUE, "icow.exchange", "Icow-routing-key", null)
        );
    }

    @Bean
    public Declarables E() {
        return new Declarables(
            new DirectExchange("icow.exchange"),
            new Queue("icow.userprofile"),
            new Binding("icow.userprofile", Binding.DestinationType.QUEUE, "icow.exchange", "Icow-routing-key", null)
        );
    }
//    icow.addInsemineEvent
    @Bean
    public Declarables F() {
        return new Declarables(
            new DirectExchange("icow.exchange"),
            new Queue("icow.addInsemineEvent"),
            new Binding("icow.addInsemineEvent", Binding.DestinationType.QUEUE, "icow.exchange", "Icow-routing-key", null)
        );
    }
}

