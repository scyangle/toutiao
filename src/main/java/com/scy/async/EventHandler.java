package com.scy.async;

import java.util.List;

/**
 * Created by Shichengyao on 1/27/17.
 */
public interface EventHandler {
    void doHandle(EventModel eventModel);

    List<EventType> getSupportEventTypes();
}
