package softwarestudio.douglas.nthu_event.client.model;

import softwarestudio.douglas.nthu_event.client.service.rest.model.Resource;

public class Event extends Resource {

    public String title;
    public String description;
    public String location;
    public String[] tag;
    public long time;

    public static String getCollectionName() { return "events"; }

}
