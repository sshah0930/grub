package com.teamgrub.grubbring.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.teamgrub.grubbring.R;

import Constants.FragmentTags;
import Fragments.ActivityFeedParentFragment;
import Fragments.MoreFragment;
import Fragments.MyRingsFragment;
import Fragments.NotificationsFragment;
import Fragments.RequestsFragment;
import Utilities.APIUtilities;
import VolleyCallbackInterfaces.LoginSuccessCallback;

public class BaseActivity extends AppCompatActivity{

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final int PERMISSION_ACCESS_FINE_LOCATION = 2;

    public FrameLayout frameLayout;

    public AHBottomNavigation mainBottomNavigation;

    public Toolbar myToolbar;

    Fragment newFragment;
    public FragmentManager fragmentManager;

    APIUtilities apiUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        myToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        mainBottomNavigation = (AHBottomNavigation)findViewById(R.id.mainBottomNavigation);

        myToolbar.setBackgroundColor(getResources().getColor(R.color.colorLoginBackGround));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryText));
        setSupportActionBar(myToolbar);

        fragmentManager = getFragmentManager();
        apiUtilities = new APIUtilities(this);

        addBottomNavigationBarItems();
        styleBottomNavigationBar();
        setupTabClickListener();
        initializeFirstFragment();

    }

    private void setupTabClickListener(){
        mainBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if(position == 0){
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new MyRingsFragment(), FragmentTags.MY_RINGS_FRAGMENT_TAG).commit();
                }
                else if(position == 1){
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new ActivityFeedParentFragment()).commit();
                }
                else if(position == 2){
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new NotificationsFragment()).commit();
                }
                else if(position == 3){
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new MoreFragment()).commit();
                }
            }
        });
    }

    private void addBottomNavigationBarItems(){
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Rings", android.R.drawable.ic_menu_rotate, getResources().getColor(R.color.colorLoginBackGround));
        mainBottomNavigation.addItem(item1);

        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Activities", android.R.drawable.ic_menu_gallery, getResources().getColor(R.color.colorLoginBackGround));
        mainBottomNavigation.addItem(item2);

        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Notifications", android.R.drawable.ic_notification_clear_all, getResources().getColor(R.color.colorLoginBackGround));
        mainBottomNavigation.addItem(item3);

        AHBottomNavigationItem item4 = new AHBottomNavigationItem("More", android.R.drawable.ic_menu_more, getResources().getColor(R.color.colorLoginBackGround));
        mainBottomNavigation.addItem(item4);
    }

    private void styleBottomNavigationBar(){
        mainBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorLoginBackGround));
        mainBottomNavigation.setAccentColor(R.color.colorPrimaryText);
        mainBottomNavigation.setInactiveColor(Color.LTGRAY);
//        //  Enables Reveal effect
        mainBottomNavigation.setColored(true);
        mainBottomNavigation.setCurrentItem(0);

    }


    private void initializeFirstFragment(){
        newFragment = new MyRingsFragment();
        fragmentManager.beginTransaction().add(R.id.content_frame, newFragment, FragmentTags.RINGS_FRAGMENT_TAG).commit();
    }

    public void coreScreenLayout(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mainBottomNavigation.setVisibility(View.VISIBLE);
    }

    public void childScreenLayout(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainBottomNavigation.setVisibility(View.GONE);
    }

    private void logout(){
        apiUtilities.logoutAPI(new LoginSuccessCallback() {
            @Override
            public void onAPICallSuccess(boolean didLogin) {
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getFragmentManager().findFragmentByTag(FragmentTags.RINGS_NEAR_ME_PARENT_FRAGMENT_TAG);
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        fragment.onActivityResult(requestCode, resultCode, data);
                        //startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        fragment.onActivityResult(requestCode, resultCode, data);
                        break;
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Fragment fragment = getFragmentManager().findFragmentByTag(FragmentTags.RINGS_NEAR_ME_PARENT_FRAGMENT_TAG);
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                } else {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }

                break;
        }
    }

}
