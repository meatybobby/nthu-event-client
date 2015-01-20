package softwarestudio.douglas.nthu_event.client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import softwarestudio.douglas.nthu_event.client.R;
import softwarestudio.douglas.nthu_event.client.ShowActivity;
import softwarestudio.douglas.nthu_event.client.adapter.EventAdapter;
import softwarestudio.douglas.nthu_event.client.model.Event;

/**
 * Created by Douglas on 2015/1/20.
 */
public class EventTagFragment extends Fragment {

    private ListView mListView;
    private EventAdapter mEventAdapter;

    private int secNum;
    private Button updateBtn;
    public static final String ARG_SECTION_NUMBER = "section_number";
    private Spinner spinner1, spinner2;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //mEventAdapter.notifyDataSetChanged();4
        Bundle args = getArguments();
            /*之後用拿到的參數來判斷要如何排序Events*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_taglist, container, false);

        mListView = (ListView) rootView.findViewById(R.id.list_tags);
        updateBtn = (Button) rootView.findViewById(R.id.btn_update);
        spinner1 = (Spinner) rootView.findViewById(R.id.eventCat1);
        spinner2 = (Spinner) rootView.findViewById(R.id.eventCat2);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.events_category1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
       // spinner1.setOnItemSelectedListener(spnListener1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.events_category2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
       // spinner2.setOnItemSelectedListener(spnListener2);

        updateBtn.setOnClickListener(new Button.OnClickListener(){
             @Override
             public void onClick(View view){
                 //mListView.setAdapter(mEventAdapter);
             //    getTagEvent();

             }
        });

        // mListView.setAdapter(mEventAdapter);
        //mEventAdapter.notifyDataSetChanged();

            /*getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mEventAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            });*/
            /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO 前往單一活動頁面

                    Intent intent = new Intent(getActivity(), ShowActivity.class);
                    Event event = (Event) mEventAdapter.getItem(position);
                    Log.d(TAG, "event clicked:" + event.getId().toString());

                    /*Event class有implement Serializable 所以可以用intent傳
                    Bundle bundle = new Bundle();
                    bundle.putString("EventId", event.getId().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });*/
        return rootView;
    }


   // @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
       // super.onListItemClick(l, v, position, id);
        //Toast.makeText(getActivity(), "你按下"+arr[position], Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ShowActivity.class);
        Event event = (Event) mEventAdapter.getItem(position);
       // Log.d(TAG, "event clicked:" + event.getId().toString());

                    /*Event class有implement Serializable 所以可以用intent傳*/
        Bundle bundle = new Bundle();
        bundle.putString("EventId", event.getId().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }

}