/**
 * @author abdelouahabella
 * Date: Apr 12, 2023
 * Time: 12:46:51 PM
 */
package com.agri40.core.socket;

import java.util.Map;

public class Event {

    private Map<String, Object> data;

    public Event(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
