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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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
import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;

public class MyPageActivity extends FragmentActivity implements ActionBar.TabListener {
    private static final String TAG = MyPageActivity.class.getSimpleName();
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    private static final String[] tabsName = {"參加的活動","舉辦的活動"/*,"收藏"*/};
    public static ArrayList<Event> joinEventList = new ArrayList<Event>();
    public static ArrayList<Event> hostEventList = new ArrayList<Event>();
    private ProgressDialog progressDialog;
    private RestManager restMgr;
    private static final String JOIN_EVENT = "join";
    private static final String HOST_EVENT = "host";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        restMgr =  RestManager.getInstance(getApplication());

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),tabsName.length);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.info_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        getEvents(JOIN_EVENT);
        getEvents(HOST_EVENT);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the action bar items pressed
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_find:
                intent = new Intent(this, FindActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_user:
                intent = new Intent(this, MyPageActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    }
    private void getEvents(final String type){
        Map<String, String> params = new HashMap<String, String>();

        StringBuilder url = new StringBuilder(getString(R.string.rest_server_url));
        url.append("users/");
        if(type.equals(JOIN_EVENT)){
            url.append("join-event");
        }
        else{
            url.append("host-event");
        }

        restMgr.listUniversal(Event.class, url.toString(),params, new RestManager.ListResourceListener<Event>() {
            @Override
            public void onResponse(int code, Map<String, String> headers,
                                   List<Event> resources) {
                if(type.equals(JOIN_EVENT)){
                    joinEventList.clear();
                    joinEventList.addAll(resources);
                }
                else{
                    hostEventList.clear();
                    hostEventList.addAll(resources);
                    progressDialog.dismiss();
                    createTab();
                }
              //  Toast.makeText(MyPageActivity.this, "成功讀取活動 " + type,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code,
                                Map<String, String> headers) {
                Log.d(this.getClass().getSimpleName(), "" + code + ": " + message);
                Toast.makeText(MyPageActivity.this, "無法list"+type ,Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        private int count;
        public AppSectionsPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.count = count;
        }
        @Override
        public Fragment getItem(int i) {
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new EventSectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(EventSectionFragment.ARG_SECTION_NUMBER, i);
                    fragment.setArguments(args);
                    return fragment;
        }
        @Override
        public int getCount() {
            return this.count;
        }

    }

    public static class EventSectionFragment extends ListFragment {

        private EventAdapter mEventAdapter;
        private int secNum;
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
                case 0:
                    mEventAdapter = new EventAdapter(getActivity(), joinEventList);
                    break;
                case 1:
                    mEventAdapter = new EventAdapter(getActivity(), hostEventList);
                    break;
                default:
                    mEventAdapter = new EventAdapter(getActivity(), joinEventList);
            }
            setListAdapter(mEventAdapter);
            mEventAdapter.notifyDataSetChanged();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
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
            bundle.putString("eventId", event.getId().toString());
            intent.putExtras(bundle);
            startActivity(intent);
           // getActivity().finish();
        }

    }

}
