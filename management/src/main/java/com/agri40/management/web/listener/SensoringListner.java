/**
 * @author abdelouahabella
 * Date: Apr 10, 2023
 * Time: 11:08:36 AM
 */
package com.agri40.management.web.listener;

import com.agri40.management.domain.Cow;
import com.agri40.management.domain.Events;
import com.agri40.management.domain.Profile;
import com.agri40.management.domain.enumeration.EventType;
import com.agri40.management.rabbitmq.QueueSender;
import com.agri40.management.repository.CowRepository;
import com.agri40.management.repository.EventsRepository;
import com.agri40.management.repository.ProfileRepository;
import com.agri40.management.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SensoringListner {

    private final Logger log = LoggerFactory.getLogger(SensoringListner.class);

    private static final String ENTITY_NAME = "managementNotification";

    ObjectMapper oMapper = new ObjectMapper();

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private CowRepository cowRepository;

    @RabbitListener(queues = { "icow.deviceid" })
    public Map<String, Object> receive(String deviceid) throws Exception {
        log.debug("Requested to get Cow id by deviceid: {}", deviceid);
        System.out.println("Message " + deviceid);
        //get cow
        Optional<com.agri40.management.domain.Cow> cow = cowRepository.findByDeviceId(deviceid);
        if (!cow.isPresent()) {
            throw new BadRequestAlertException("The Device Is Not Connected To Any Cow", ENTITY_NAME, "id null");
        }
        Cow cow1 = cow.get();
        Map<String, Object> cowMap = new HashMap<>();
        cowMap.put("cowId", cow1.getId());
        log.debug("Requested to get Cow id by deviceid: {}", cow1);
        // get user userPhone
        Optional<com.agri40.management.domain.Profile> profile = profileRepository.findByUserId(cow1.getUserId());

        if (!profile.isPresent()) {
            throw new BadRequestAlertException("The Cow Is Not Connected To Any User", ENTITY_NAME, "id null");
        }
        Profile profile1 = profile.get();
        cowMap.put("userPhone", profile1.getPhoneNumber());
        cowMap.put("userId", profile1.getUserId());
        cowMap.put("smsService", profile1.getSmsService());
        cowMap.put("cowName", cow1.getNom());
        return cowMap;
    }

    @RabbitListener(queues = { "icow.userprofile" })
    public void receive(@Payload Map<String, Object> stream) {
        log.debug("Received message as generic Map: {}", stream);
        System.out.println("Message " + stream.toString());
    }

    @RabbitListener(queues = { "icow.addInsemineEvent" })
    public void receiveChaleur(@Payload String id) {
        log.debug("REST request to add insemin event to cow : {}", id);
        Events event = new Events();
        event.setCowId(id);
        event.date(java.time.Instant.now());
        event.setType(EventType.ENSIMINE);
        eventsRepository.save(event);
    }
}
