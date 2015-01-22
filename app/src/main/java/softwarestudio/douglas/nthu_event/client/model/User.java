package softwarestudio.douglas.nthu_event.client.model;

import softwarestudio.douglas.nthu_event.client.service.rest.model.Resource;

public class User extends Resource<Long> {

    private String fbAccessToken;
    private String name;

    public User(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getCollectionName() {
        return "users";
    }
}