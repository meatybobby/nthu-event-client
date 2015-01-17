package softwarestudio.douglas.nthu_event.client.model;


import softwarestudio.douglas.nthu_event.client.service.rest.model.Resource;

public class Session extends Resource {

    public static Session currentSession;
    private String fbAccessToken;

    public Session(String fbAccessToken){
        this.fbAccessToken = fbAccessToken;
    }

    public static String getCollectionName() { return "sessions"; }
}
