package softwarestudio.douglas.nthu_event.client;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import softwarestudio.douglas.nthu_event.client.adapter.EventAdapter;
import softwarestudio.douglas.nthu_event.client.fragment.EventTagFragment;
import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;

public class FindActivity extends FragmentActivity implements ActionBar.TabListener {
    private static final String TAG = FindActivity.class.getSimpleName();
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    private static String[] tabsName = {"最新", "最近", "最熱門","分類"};

    private RestManager mRestMgr;

    /*三種不同排序方式的list*/
    public static ArrayList<Event> eventList1 = new ArrayList<Event>();
    public static ArrayList<Event> eventList2 = new ArrayList<Event>();
    public static ArrayList<Event> eventList3 = new ArrayList<Event>();
    //private HashMap<Long> userInEvent;
    //private static EventAdapter eventAdapter1;

    private static ProgressDialog progressDialog;
    /*宣告成static >> 為了讓fragment能存取*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        mRestMgr = RestManager.getInstance(getApplication());
        /* get 一次所有活動，在不同fragment再分別排序 */


        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),tabsName.length);

        progressDialog = new ProgressDialog(FindActivity.this);
        progressDialog.setMessage(getString(R.string.info_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        getEvents("latest");
        getEvents("nearest");
        getEvents("hottest");//一次抓完所有Event 不同fragment分別再排序
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        private int count;
        public AppSectionsPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.count = count;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 3:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new EventTagFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new EventSectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(EventSectionFragment.ARG_SECTION_NUMBER, i+1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return this.count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }

    private void createTab(){
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for(int i=0; i<mAppSectionsPagerAdapter.getCount(); i++){
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(tabsName[i])
                            .setTabListener(this));
        }
        Toast.makeText(FindActivity.this, "成功建立tab",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed(){
        finish();
    }
    private void getEvents(final String sortType){

        Map<String, String> params = new HashMap<String, String>();
        /*params.put("tag1", null);
        params.put("tag2", null);*/

        params.put("SortType",sortType);//要求server根據參數不同有不同排序方式
            mRestMgr.listResource(Event.class, params, new RestManager.ListResourceListener<Event>() {
                @Override
                public void onResponse(int code, Map<String, String> headers,
                                       List<Event> resources) {
                    if(sortType.equals("latest")){
                        eventList1.clear();
                        eventList1.addAll(resources);
                    }
                    else if(sortType.equals("nearest")){
                        eventList2.clear();
                        eventList2.addAll(resources);
                    }
                    else if(sortType.equals("hottest")){
                        eventList3.clear();
                        eventList3.addAll(resources);
                    }

                    if(sortType.equals("hottest")){//所有get結束
                        createTab();
                        progressDialog.dismiss();
                        Toast.makeText(FindActivity.this, "list所有活動",
                                Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onRedirect(int code, Map<String, String> headers, String url) {
                    onError(null, null, code, headers);
                }

                @Override
                public void onError(String message, Throwable cause, int code,
                                    Map<String, String> headers) {
                    Log.d(this.getClass().getSimpleName(), "" + code + ": " + message);
                    Toast.makeText(FindActivity.this, "無法讀取活動",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }, null);
    }

    public static class EventSectionFragment extends ListFragment {

       // private ListView mListView;
        private EventAdapter mEventAdapter;
        private int secNum;
        private Button updateBtn;
        public static final String ARG_SECTION_NUMBER = "section_number";

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
            secNum = args.getInt(ARG_SECTION_NUMBER);
            switch (secNum){
                case 1:
                    mEventAdapter = new EventAdapter(getActivity(), eventList1);
                    break;
                case 2:
                    mEventAdapter = new EventAdapter(getActivity(), eventList2);
                    break;
                case 3:
                    mEventAdapter = new EventAdapter(getActivity(), eventList3);
                    break;
                default:
                    mEventAdapter = new EventAdapter(getActivity(), eventList3);
            }
            setListAdapter(mEventAdapter);
            mEventAdapter.notifyDataSetChanged();
            /*getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mEventAdapter.notifyDataSetChanged();
                    //progressDialog.dismiss();
                }
            });*/
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

            //mListView = (ListView) rootView.findViewById(R.id.list_events);
            //updateBtn = (Button) rootView.findViewById(R.id.btn_update);
            /*updateBtn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View view){
                   //mListView.setAdapter(mEventAdapter);

                }
            });*/

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
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // TODO Auto-generated method stub
            super.onListItemClick(l, v, position, id);
            //Toast.makeText(getActivity(), "你按下"+arr[position], Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), ShowActivity.class);
            Event event = (Event) mEventAdapter.getItem(position);
            Log.d(TAG, "event clicked:" + event.getId().toString());

                    /*Event class有implement Serializable 所以可以用intent傳*/
            Bundle bundle = new Bundle();
            bundle.putString("EventId", event.getId().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

}