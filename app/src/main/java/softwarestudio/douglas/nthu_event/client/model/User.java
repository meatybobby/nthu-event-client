package softwarestudio.douglas.nthu_event.client.model;

import softwarestudio.douglas.nthu_event.client.service.rest.model.Resource;

public class User extends Resource<Long> {

    private String fbAccessToken;
    private String name;
    private String fbId;

    public User(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public static String getCollectionName() {
        return "users";
    }
}