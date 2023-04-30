package com.agri40.core.config;

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
    public Declarables B() {
        return new Declarables(
            new DirectExchange("icow.exchange"),
            new Queue("icow.live-stream"),
            new Binding("icow.live-stream", Binding.DestinationType.QUEUE, "icow.exchange", "Icow-routing-key", null)
        );
    }

    @Bean
    public Declarables C() {
        return new Declarables(
            new DirectExchange("icow.exchange"),
            new Queue("icow.profile"),
            new Binding("icow.profile", Binding.DestinationType.QUEUE, "icow.exchange", "Icow-routing-key", null)
        );
    }
    
    @Bean
    public Declarables F() {
        return new Declarables(
                new DirectExchange("icow.exchange"),
                new Queue("icow.notification"),
                new Binding("icow.notification", Binding.DestinationType.QUEUE, "icow.exchange", "Icow-routing-key",
                        null));
    }

    @Bean
    public Declarables G() {
        return new Declarables(
                new DirectExchange("icow.exchange"),
                new Queue("icow.save-notification"),
                new Binding("icow.save-notification", Binding.DestinationType.QUEUE, "icow.exchange", "Icow-routing-key",
                        null));
    }
}
