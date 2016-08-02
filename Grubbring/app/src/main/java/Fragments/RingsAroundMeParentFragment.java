package Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.Activities.LoginActivity;
import com.teamgrub.grubbring.R;

import org.json.JSONObject;

import java.util.ArrayList;

import Constants.FragmentTags;
import Models.RingAroundMe;
import Utilities.APIUtilities;
import Utilities.DataHolder;
import Utilities.LocationManager;
import Utilities.ResponseHandlerUtilities;
import VolleyCallbackInterfaces.APISuccessCallback;
import VolleyCallbackInterfaces.LocationCallback;

public class RingsAroundMeParentFragment extends Fragment implements LocationCallback {

    private ContentLoadingProgressBar progressBar;
    private TabLayout ringsAroundMeTabLayout;
    BaseActivity activity;

    protected static final int PERMISSION_ACCESS_FINE_LOCATION = 2;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    DataHolder dataHolder;
    APIUtilities apiUtilities;
    LocationManager locationManager;

    ArrayList<RingAroundMe> ringsAroundMeList;
    Location location;

    public RingsAroundMeParentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rings_around_me_parent, container, false);

        ringsAroundMeTabLayout = (TabLayout)view.findViewById(R.id.ringsAroundMeTabBar);
        progressBar = (ContentLoadingProgressBar)view.findViewById(R.id.progressBar);

        dataHolder = DataHolder.getInstance();
        apiUtilities = new APIUtilities(getActivity());
        activity = (BaseActivity)getActivity();
        activity.myToolbar.setTitle("Rings Around Me");
        activity.coreScreenLayout();

        ringsAroundMeList = dataHolder.getRingsNearMeList();

        locationManager = new LocationManager(getActivity(), this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_FINE_LOCATION);
        }else{
            if(!locationManager.isLocationEnabled()){
                dataHolder.setRingsNearMeList(null);
                ringsAroundMeList = null;
                locationManager.settingsRequest();
            }
            else{
                if(ringsAroundMeList != null){
                    replaceFragment(new RingsAroundMeMapFragment(), FragmentTags.RINGS_NEAR_ME_MAP_FRAGMENT_TAG);
                }
            }
        }


        setupTabLayoutWithoutViewPager();

        return view;
    }

    private void setupTabLayoutWithoutViewPager(){
        ringsAroundMeTabLayout.addTab(ringsAroundMeTabLayout.newTab().setText("Map"),true);
        ringsAroundMeTabLayout.addTab(ringsAroundMeTabLayout.newTab().setText("List"));

        ringsAroundMeTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    replaceFragment(new RingsAroundMeMapFragment(), FragmentTags.RINGS_NEAR_ME_MAP_FRAGMENT_TAG);
                }else if(tab.getPosition() == 1){
                    replaceFragment(new RingsAroundMeListFragment(), FragmentTags.RINGS_NEAR_ME_LIST_FRAGMENT_TAG);
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
        activity.fragmentManager.beginTransaction().replace(R.id.ringsAroundMe_content_frame,fragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_ringsnearme_items, menu);
        menu.findItem(R.id.myRingsNavigationItem).setTitle(Html.fromHtml("<font color='#ffffff'>My Rings</font>"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.myRingsNavigationItem) {
            activity.fragmentManager.beginTransaction().replace(R.id.content_frame, new MyRingsFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationUpdatedCallback(Location location) {
        if(ringsAroundMeList == null){
            getRingsNearMeCall(location);
        }
        dataHolder.setUserLocation(location);
    }

    private void getRingsNearMeCall(Location location){
        ringsAroundMeTabLayout.setAlpha(0.3f);
        ringsAroundMeTabLayout.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
        apiUtilities.getRingsNearMe(location.getLatitude(), location.getLongitude(), new APISuccessCallback() {
            @Override
            public void onAPICallSuccess(JSONObject responseObject) {
                if(responseObject != null){
                    //server sent response
                    ringsAroundMeList = ResponseHandlerUtilities.getRingsNearMe(responseObject);
                    dataHolder.setRingsNearMeList(ringsAroundMeList);
                    Log.d("Count","Post api, Current count is "+ringsAroundMeList.size());

                }else{
                    //server down, cookie deleted from sharedprefs(APIUtilities), log user out, navigate to login screen
                    Toast.makeText(getActivity(), "Server is not responding!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                ringsAroundMeTabLayout.setAlpha(1.0f);
                ringsAroundMeTabLayout.setClickable(true);
                progressBar.setVisibility(View.INVISIBLE);
                replaceFragment(new RingsAroundMeMapFragment(), FragmentTags.RINGS_NEAR_ME_MAP_FRAGMENT_TAG);
            }
        });
    }

    @Override
    public void onStop() {
        locationManager.disconnectGoogleApiClient();
        //dataHolder.setRingsNearMeList(null);
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: //location settings dialog, user selected YES to enabling location
                        locationManager.startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED: //location settings dialog, user selected NO to enabling location
                        locationManager.settingsRequest(); //ask user again with Location Settings Dialog
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(!locationManager.isLocationEnabled()){
                        locationManager.settingsRequest();
                    }
                } else {
                    Toast.makeText(getActivity(), "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

}
