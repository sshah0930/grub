package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.Activities.LoginActivity;
import com.teamgrub.grubbring.R;

import org.json.JSONObject;

import Constants.FragmentTags;
import Models.Ring;
import Models.RingAroundMe;
import Utilities.APIUtilities;
import Utilities.ResponseHandlerUtilities;
import VolleyCallbackInterfaces.APISuccessCallback;


public class RingDetailParentFragment extends Fragment implements View.OnClickListener{

    APIUtilities apiUtilities;

    BaseActivity activity;
    private TabLayout ringDetailTabLayout;
    private Button joinRingButton, leaveRingButton;
    private String layoutStyle;
    public Ring selectedRing;
    private boolean isJoinRingRequestSuccess;

    private Bundle bundle;

    public RingDetailParentFragment() {

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
        View view = inflater.inflate(R.layout.fragment_ring_detail_base, container, false);

        ringDetailTabLayout = (TabLayout)view.findViewById(R.id.ringDetailTabBar);
        joinRingButton = (Button)view.findViewById(R.id.joinRingButton);
        leaveRingButton = (Button)view.findViewById(R.id.leaveRingButton);

        joinRingButton.setOnClickListener(this);
        leaveRingButton.setOnClickListener(this);

        activity = (BaseActivity) getActivity();
        apiUtilities = new APIUtilities(activity);
        activity.myToolbar.getMenu().clear();
        activity.myToolbar.setTitle("Ring Details");
        activity.childScreenLayout();

        bundle = this.getArguments();
        layoutStyle = bundle.getString("layoutStyle");

        setupTabLayoutWithoutViewPager();

        if(layoutStyle.equals("single")){
            singleDetailsView();
        }else if(layoutStyle.equals("both")){
            bothDetailsView();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == joinRingButton.getId()){
            sendRequestToJoinRing();
        }else if(v.getId() == leaveRingButton.getId()){
            leaveRing();
        }
    }

    private void singleDetailsView(){
        selectedRing = (RingAroundMe)bundle.getParcelable("selectedRing");
        leaveRingButton.setVisibility(View.INVISIBLE);
        ringDetailTabLayout.setVisibility(View.GONE);
        activity.fragmentManager.beginTransaction().replace(R.id.ringdetail_content_frame, new RingDetailInfoFragment()).commit();
    }

    private void bothDetailsView(){
        selectedRing = bundle.getParcelable("selectedRing");
        joinRingButton.setVisibility(View.INVISIBLE);
        activity.fragmentManager.beginTransaction().replace(R.id.ringdetail_content_frame, new RingDetailActivityListFragment()).commit();
    }

    private void sendRequestToJoinRing(){
        apiUtilities.requestToJoinRingAPI(selectedRing.getRingId(), new APISuccessCallback() {
            @Override
            public void onAPICallSuccess(JSONObject responseObject) {
                if(responseObject != null){
                    //server sent response
                    isJoinRingRequestSuccess = ResponseHandlerUtilities.requestToJoinRing(responseObject);
                    if(isJoinRingRequestSuccess){
                        Toast.makeText(getActivity(), "Success Request", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), "Fail Request", Toast.LENGTH_LONG).show();
                    }

                }else{
                    //server down, cookie deleted from sharedprefs(APIUtilities), log user out, navigate to login screen
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void leaveRing(){

    }

    private void setupTabLayoutWithoutViewPager(){
        ringDetailTabLayout.addTab(ringDetailTabLayout.newTab().setText("Info"));
        ringDetailTabLayout.addTab(ringDetailTabLayout.newTab().setText("Activities"), true);

        ringDetailTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    replaceFragment(new RingDetailInfoFragment(), FragmentTags.RINGS_NEAR_ME_MAP_FRAGMENT_TAG);
                }else if(tab.getPosition() == 1){
                    replaceFragment(new RingDetailActivityListFragment(), FragmentTags.RINGS_NEAR_ME_LIST_FRAGMENT_TAG);
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
        activity.fragmentManager.beginTransaction().replace(R.id.ringdetail_content_frame,fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.fragmentManager.popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }
}
