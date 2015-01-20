package softwarestudio.douglas.nthu_event.client.model;

import java.util.ArrayList;
import java.util.List;

import softwarestudio.douglas.nthu_event.client.service.rest.model.Resource;


public class Comment extends Resource<Long> {
    public static String getCollectionName() { return "comment"; }
    private long eventId;
    private long userId;
    private String userName;
    private long timeStamp;
    private String content;
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getEventId() {
        return eventId;
    }
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

}
