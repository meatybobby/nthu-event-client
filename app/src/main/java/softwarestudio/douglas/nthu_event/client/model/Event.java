package softwarestudio.douglas.nthu_event.client.model;

import java.io.Serializable;

import softwarestudio.douglas.nthu_event.client.service.rest.model.Resource;

public class Event extends Resource implements Serializable {

    public static String getCollectionName() { return "events"; }

    private String title;
    private String description;
    private String location;
    private String[] tag;
    private long time;



    private long timeStamp;
    private int joinNum;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String[] getTag() {
        return tag;
    }
    public void setTag(String[] tag) {
        this.tag = tag;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getJoinNum() {
        return joinNum;
    }
    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
