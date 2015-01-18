package softwarestudio.douglas.nthu_event.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import softwarestudio.douglas.nthu_event.client.R;
import softwarestudio.douglas.nthu_event.client.model.Event;

/**
 * Created by Douglas on 2015/1/17.
 */
public class EventAdapter extends BaseAdapter {

    private List<Event> mEventList;
    private LayoutInflater mMyInflater;

    public EventAdapter(Context c, ArrayList<Event> list) {
        this.mEventList = list;
        mMyInflater = LayoutInflater.from(c);
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
        TextView contentTxt = (TextView) convertView.findViewById(R.id.txt_content);

        titleTxt.setText(event.getTitle());
        contentTxt.setText(event.getDescription());

        return convertView;
    }

}