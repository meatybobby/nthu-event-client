package softwarestudio.douglas.nthu_event.client.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import softwarestudio.douglas.nthu_event.client.R;
import softwarestudio.douglas.nthu_event.client.ShowActivity;
import softwarestudio.douglas.nthu_event.client.adapter.EventAdapter;
import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;

/**
 * Created by Douglas on 2015/1/20.
 */
public class EventTagFragment extends Fragment {

    private ListView mListView;
    private EventAdapter mEventAdapter;
    private static final String ALL_KINDS = "全部";

    private int secNum;
    private Button updateBtn;
    public static final String ARG_SECTION_NUMBER = "section_number";
    private Spinner spinner1, spinner2;
    private String[] tag={ALL_KINDS,ALL_KINDS};
    private RestManager mRestMgr;
    private ArrayList<Event> eventList = new ArrayList<Event>();
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
             /*之後用拿到的參數來判斷要如何排序Events*/
        mRestMgr = RestManager.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_taglist, container, false);

        /*設定版面*/
        mListView = (ListView) rootView.findViewById(R.id.list_tags);
        updateBtn = (Button) rootView.findViewById(R.id.btn_update);
        spinner1 = (Spinner) rootView.findViewById(R.id.eventCat1);
        spinner2 = (Spinner) rootView.findViewById(R.id.eventCat2);

        /*設定選單*/
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.tag_1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(spnListener1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.tag_2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(spnListener2);

        /*設定adapter*/
        mEventAdapter = new EventAdapter(getActivity(), eventList);
        mListView.setAdapter(mEventAdapter);

        /*更新按鈕*/
        updateBtn.setOnClickListener(new Button.OnClickListener(){
             @Override
             public void onClick(View view){
                 //mListView.setAdapter(mEventAdapter);
                 progressDialog = new ProgressDialog(getActivity());
                 progressDialog.setMessage(getString(R.string.info_wait));
                 progressDialog.setCancelable(false);
                 progressDialog.show();
                 /*依照選擇的分類get 活動*/
                 getTagEvent(tag[0], tag[1]);
                 //mEventAdapter.notifyDataSetChanged();
             }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO 前往單一活動頁面

                Intent intent = new Intent(getActivity(), ShowActivity.class);
                Event event = (Event) mEventAdapter.getItem(position);
               // Log.d(TAG, "event clicked:" + event.getId().toString());
                    /*Event class有implement Serializable 所以可以用intent傳*/
                    Bundle bundle = new Bundle();
                    bundle.putString("EventId", event.getId().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        return rootView;
    }
    private void getTagEvent(String tag1, String tag2){
        Map<String, String> params = new HashMap<String, String>();

        if(!tag1.equals(ALL_KINDS)){
            params.put("tag1", tag1);
        }
        if(!tag2.equals(ALL_KINDS)){
            params.put("tag2", tag2);
        }

        mRestMgr.listResource(Event.class, params, new RestManager.ListResourceListener<Event>() {
            @Override
            public void onResponse(int code, Map<String, String> headers,
                                   List<Event> resources) {
                eventList.clear();
                eventList.addAll(resources);
                progressDialog.dismiss();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEventAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code,
                                Map<String, String> headers) {
                Log.d(this.getClass().getSimpleName(), "" + code + ": " + message);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "無法取得活動！ 代碼：" + code,
                        Toast.LENGTH_SHORT).show();
            }
        }, null);
    }
    private AdapterView.OnItemSelectedListener spnListener1 =
            new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    tag[0] = parent.getItemAtPosition(pos).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }

            };
    private AdapterView.OnItemSelectedListener spnListener2 =
            new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    tag[1] = parent.getItemAtPosition(pos).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }

            };
}