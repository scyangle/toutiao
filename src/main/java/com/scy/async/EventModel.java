package com.scy.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shichengyao on 1/27/17.
 */
public class EventModel {
    private EventType type;
    private int actorId;
    private int entityId;
    private int entityType;
    private int entityOwnerId;
    private Map<String, String> exts = new HashMap<>();

    public EventType getType() {
        return type;
    }

    /**
     * @param type
     * @return EventModel 支持链式操作
     */
    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    /**
     * @param actorId
     * @return EventModel 支持链式操作
     */
    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     * @return EventModel 支持链式操作
     */
    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    /**
     * @param entityType
     * @return EventModel 支持链式操作
     */
    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    /**
     * @param entityOwnerId
     * @return EventModel 支持链式操作
     */
    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public String getExts(String name) {
        return exts.get(name);
    }

    /**
     * @param name
     * @param value
     * @return EventModel 支持链式操作
     */
    public EventModel setExts(String name, String value) {
        exts.put(name, value);
        return this;
    }
}