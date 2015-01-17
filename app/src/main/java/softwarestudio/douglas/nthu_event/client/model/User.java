package softwarestudio.douglas.nthu_event.client.model;

import softwarestudio.douglas.nthu_event.client.service.rest.model.Resource;

public class User extends Resource {

    private String fbAccessToken;

    public User(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public static String getCollectionName() {
        return "users";
    }
}