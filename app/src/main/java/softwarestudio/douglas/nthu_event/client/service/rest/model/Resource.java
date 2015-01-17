package softwarestudio.douglas.nthu_event.client.service.rest.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public abstract class Resource {
    public static final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    /**
     * @return the collection name.
     */
    public static String getCollectionName() {
        return null;
    }

    protected String id;

    protected Resource() {
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    @Override
    public int hashCode() {
        return gson.toJson(this).hashCode();
    }

}