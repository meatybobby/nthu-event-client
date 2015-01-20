package softwarestudio.douglas.nthu_event.client.adapter;

/**
 * Created by Douglas on 2015/1/20.
 */
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
import softwarestudio.douglas.nthu_event.client.model.Comment;
import softwarestudio.douglas.nthu_event.client.model.Event;

/**
 * Created by Douglas on 2015/1/17.
 */
public class CommentAdapter extends BaseAdapter {

    private List<Comment> commentList;
    private LayoutInflater mMyInflater;


    public CommentAdapter(Context c, ArrayList<Comment> list) {
        this.commentList = list;
        mMyInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(ArrayList<Comment> list) {
        this.commentList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mMyInflater.inflate(R.layout.comment_item, null);
        Comment comment = commentList.get(position);

        TextView userTxt = (TextView) convertView.findViewById(R.id.cmt_user);
        TextView contentTxt = (TextView) convertView.findViewById(R.id.cmt_content);
        TextView dateTxt = (TextView) convertView.findViewById(R.id.cmt_date);

        userTxt.setText("");
        contentTxt.setText("");
        dateTxt.setText("");

        return convertView;
    }

    private String convertDate(long millis){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd k:mm");
        return formatter.format( new Date(millis) );
    }

}