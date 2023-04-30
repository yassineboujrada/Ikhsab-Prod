/**
 * @author abdelouahabella
 * Date: Apr 10, 2023
 * Time: 11:42:22 AM
 */
package com.agri40.core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConfig {

    @Bean
    public Queue queue() {
        return new Queue("icow.userprofile", true);
    }
}
