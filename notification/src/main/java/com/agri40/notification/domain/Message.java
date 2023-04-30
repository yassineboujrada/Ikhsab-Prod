package com.agri40.notification.domain;

import com.agri40.notification.domain.enumeration.MessageType;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Message.
 */
@Document(collection = "message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("created_date_time")
    private Instant createdDateTime;

    @Field("message_type")
    private MessageType messageType;

    @Field("content")
    private String content;

    @Field("room")
    private String room;

    @Field("username")
    private String username;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Message id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedDateTime() {
        return this.createdDateTime;
    }

    public Message createdDateTime(Instant createdDateTime) {
        this.setCreatedDateTime(createdDateTime);
        return this;
    }

    public void setCreatedDateTime(Instant createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public MessageType getMessageType() {
        return this.messageType;
    }

    public Message messageType(MessageType messageType) {
        this.setMessageType(messageType);
        return this;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return this.content;
    }

    public Message content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoom() {
        return this.room;
    }

    public Message room(String room) {
        this.setRoom(room);
        return this;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUsername() {
        return this.username;
    }

    public Message username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", createdDateTime='" + getCreatedDateTime() + "'" +
            ", messageType='" + getMessageType() + "'" +
            ", content='" + getContent() + "'" +
            ", room='" + getRoom() + "'" +
            ", username='" + getUsername() + "'" +
            "}";
    }
}
