package softwarestudio.douglas.nthu_event.client.model;

import java.io.Serializable;
import java.util.List;

import softwarestudio.douglas.nthu_event.client.service.rest.model.Resource;

public class Event extends Resource<Long> implements Serializable {

    public static String getCollectionName() { return "events"; }

    private String title;
    private String description;
    private String location;
    private String tag1;
    private String tag2;
    private String posterName;
    private long time;
    private long timeStamp;
    private int joinNum;
    private List<User> joinUser;

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
    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }
    public long getTime() {
        return time;
    }

    public void setId(Long id) {
        this.id=id;
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
    public String getPosterName() {
        return posterName;
    }
    public List<User> getJoinUser() {
        return joinUser;
    }

    public void setJoinUser(List<User> joinUser) {
        this.joinUser = joinUser;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

}
