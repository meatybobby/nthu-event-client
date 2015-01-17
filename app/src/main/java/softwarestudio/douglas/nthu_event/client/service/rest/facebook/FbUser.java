package softwarestudio.douglas.nthu_event.client.service.rest.facebook;

import java.util.Locale;

public class FbUser {

    private static final String TAG = FbUser.class.getSimpleName();

    private String id;
    private String name;
    private String email;
    private long birthday;
    private String gender;
    private String locale;

    public FbUser(String id, String name, String email, long birthday, String gender,
                  String locale) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.locale = locale;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public Locale getLocale() {
        String[] ls = locale.split("_");
        return ls.length > 1 ? new Locale(ls[0], ls[1]) : new Locale(ls[0]);
    }
}
