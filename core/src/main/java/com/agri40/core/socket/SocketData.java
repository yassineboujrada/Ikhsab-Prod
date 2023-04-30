/**
 * @author Homeella
 * Date: Apr 27, 2023
 * Time: 6:41:01 PM
 */
package com.agri40.core.socket;

import com.agri40.core.socket.enumeration.MessageType;
import java.util.Map;

public class SocketData {

    private String createdDateTime;

    private MessageType messageType;

    private Map<String, Object> content;

    private String room;

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
