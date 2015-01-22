package softwarestudio.douglas.nthu_event.client.adapter;

/**
 * Created by Douglas on 2015/1/20.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import softwarestudio.douglas.nthu_event.client.R;
import softwarestudio.douglas.nthu_event.client.model.Comment;
import softwarestudio.douglas.nthu_event.client.model.User;

/**
 * Created by Douglas on 2015/1/17.
 */
public class UserListAdapter extends BaseAdapter {

    private List<User> usrList;
    private LayoutInflater mMyInflater;


    public UserListAdapter(Context c, ArrayList<User> list) {
        this.usrList = list;
        mMyInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return usrList.size();
    }

    @Override
    public Object getItem(int position) {
        return usrList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(ArrayList<User> list) {
        this.usrList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mMyInflater.inflate(R.layout.user_item, null);
        User user = usrList.get(position);

        TextView userName = (TextView) convertView.findViewById(R.id.user_name);
        userName.setText(user.getName());

        return convertView;
    }

    private String convertDate(long millis){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd k:mm");
        return formatter.format( new Date(millis) );
    }

}