package softwarestudio.douglas.nthu_event.client.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import softwarestudio.douglas.nthu_event.client.R;
import softwarestudio.douglas.nthu_event.client.model.Event;

/**
 * Created by Douglas on 2015/1/17.
 */
public class EventAdapter extends BaseAdapter {

    private List<Event> mEventList;
    private LayoutInflater mMyInflater;
    private HashMap<String,Drawable> imageMap;
    private List<String> tagList;
    private List<Drawable> iconList;

    public EventAdapter(Context c, ArrayList<Event> list) {
        this.mEventList = list;
        mMyInflater = LayoutInflater.from(c);
        imageMap = new HashMap<String, Drawable>();

        tagList = Arrays.asList(c.getResources().getStringArray(R.array.events_category2));
        iconList = new ArrayList<Drawable>();
        iconList.add(c.getResources().getDrawable(R.drawable.icon_science));
        iconList.add(c.getResources().getDrawable(R.drawable.icon_art));
        iconList.add(c.getResources().getDrawable(R.drawable.icon_social));
        iconList.add(c.getResources().getDrawable(R.drawable.icon_sports));
        iconList.add(c.getResources().getDrawable(R.drawable.icon_mix));

        for(int i=0; i<iconList.size() && i<tagList.size(); i++){
            imageMap.put(tagList.get(i),iconList.get(i));
        }
    }

    @Override
    public int getCount() {
        return mEventList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(ArrayList<Event> list) {
        this.mEventList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mMyInflater.inflate(R.layout.event_item, null);
        Event event = mEventList.get(position);

        TextView titleTxt = (TextView) convertView.findViewById(R.id.txt_title);
        TextView dateTxt = (TextView) convertView.findViewById(R.id.txt_date);
        TextView joinNumTxt = (TextView) convertView.findViewById(R.id.txt_joinNum);

        /*動態更改list icon*/
        ImageView listIcon = (ImageView) convertView.findViewById(R.id.list_icon);
        listIcon.setBackgroundDrawable(imageMap.get(event.getTag2()));

        titleTxt.setText(event.getTitle());
        dateTxt.setText(convertDate(event.getTime()));
        joinNumTxt.setText("目前參加人數： " + event.getJoinNum());

        return convertView;
    }

    private String convertDate(long millis){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format( new Date(millis) );
    }

}