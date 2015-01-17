package softwarestudio.douglas.nthu_event.client;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
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

public class FindActivity extends FragmentActivity implements ActionBar.TabListener {


    AppSectionsPagerAdapter mAppSectionsPagerAdapter;


    ViewPager mViewPager;
    private static String[] tabsName = {"最新", "最近", "最熱門", "分類"};


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);


        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),tabsName.length);

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
               // case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                   // return new LaunchpadSectionFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new EventSectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(EventSectionFragment.ARG_SECTION_NUMBER, i);
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



    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class EventSectionFragment extends ListFragment {
        private ArrayList<Event> mEventList = new ArrayList<Event>();
        private ListView mListView;
        private EventAdapter mEventAdapter;
        private RestManager mRestMgr;

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
            Bundle args = getArguments();
            /*之後用拿到的參數來判斷要如何排序Events*/
            String txt = tabsName[args.getInt(ARG_SECTION_NUMBER)];

            mRestMgr = RestManager.getInstance(getActivity().getApplication());

            mListView = (ListView) rootView.findViewById(R.id.list_events);
            mEventAdapter = new EventAdapter(getActivity(), mEventList);
            mListView.setAdapter(mEventAdapter);

            getEvents(args.getInt(ARG_SECTION_NUMBER));

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO 前往單一活動頁面
                }
            });

            return rootView;
        }
        private void getEvents(int sectionNum){
           /*根據sectionNum 決定排序events的方式*/
            /*以下還有bug*/
           /* Map<String, String> params = new HashMap<String, String>();
            mRestMgr.listResource(Event.class, params, new RestManager.ListResourceListener<Event>() {
                @Override
                public void onResponse(int code, Map<String, String> headers,
                                       List<Event> resources) {
                    if (resources != null) {

                        mEventList.clear();
                        for(Event m : resources){
                            mEventList.add(m);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mEventAdapter.notifyDataSetChanged();
                            }
                        });
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
                }
            }, null);*/
        }
    }
}