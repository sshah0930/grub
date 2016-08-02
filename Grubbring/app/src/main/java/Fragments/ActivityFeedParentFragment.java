package Fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.R;

import Constants.FragmentTags;
import Utilities.UserLocationUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFeedParentFragment extends Fragment{

    BaseActivity activity;
    private TabLayout activityFeedTabLayout;

    public ActivityFeedParentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_feed, container, false);

        activity = (BaseActivity)getActivity();
        activity.myToolbar.setTitle("Activity Feed");
        activity.coreScreenLayout();

        activityFeedTabLayout = (TabLayout)view.findViewById(R.id.activityfeedTabBar);

        setupTabLayoutWithoutViewPager();

        return view;
    }

    private void setupTabLayoutWithoutViewPager(){
        activityFeedTabLayout.addTab(activityFeedTabLayout.newTab().setText("Recent"),true);
        activityFeedTabLayout.addTab(activityFeedTabLayout.newTab().setText("Current"));

        activityFeedTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    replaceFragment(new ActivityFeedRecentFragment(), FragmentTags.RINGS_NEAR_ME_MAP_FRAGMENT_TAG);
                }else if(tab.getPosition() == 1){
                    replaceFragment(new ActivityFeedCurrentFragment(), FragmentTags.RINGS_NEAR_ME_LIST_FRAGMENT_TAG);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void replaceFragment(Fragment fragment, String tag){
        activity.fragmentManager.beginTransaction().replace(R.id.activityfeed_content_frame,fragment).commit();
    }

}
