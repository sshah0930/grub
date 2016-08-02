package Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.Activities.LoginActivity;
import com.teamgrub.grubbring.R;

import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.RingsAroundMeAdapter;
import Models.RingAroundMe;
import Utilities.APIUtilities;
import Utilities.DataHolder;
import Utilities.ResponseHandlerUtilities;
import VolleyCallbackInterfaces.APISuccessCallback;
import VolleyCallbackInterfaces.RingsAroundMeItemClickListener;


public class RingsAroundMeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    DataHolder dataHolder;
    APIUtilities apiUtilities;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView ringsNearMeRecyclerView;
    private RingsAroundMeAdapter recylerViewAdapter;

    BaseActivity activity;

    ArrayList<RingAroundMe> ringsAroundMeList;

    public RingsAroundMeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rings_around_me, container, false);

        apiUtilities = new APIUtilities(getActivity());
        activity = (BaseActivity)getActivity();

        dataHolder = DataHolder.getInstance();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        ringsNearMeRecyclerView = (RecyclerView)view.findViewById(R.id.ringsNearMeRecyclerView);
        ringsNearMeRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        ringsNearMeRecyclerView.setLayoutManager(layoutManager);
        ringsNearMeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(this);

        ringsAroundMeList = dataHolder.getRingsNearMeList();
        if(ringsAroundMeList != null){
            updateRecyclerView();
        }

        return view;
    }

    private void updateRecyclerView(){
        recylerViewAdapter = new RingsAroundMeAdapter(ringsAroundMeList, new RingsAroundMeItemClickListener() {
            @Override
            public void onItemClick(RingAroundMe ring) {
                Fragment ringDetailFragment = new RingDetailParentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("layoutStyle", "single");
                bundle.putParcelable("selectedRing",ring);
                ringDetailFragment.setArguments(bundle);
                activity.fragmentManager.beginTransaction().replace(R.id.content_frame,ringDetailFragment).addToBackStack("backToRingsAroundMe").commit();
            }
        });
        ringsNearMeRecyclerView.setAdapter(recylerViewAdapter);
    }

    private void getRingsNearMeCall(Location location){
        swipeRefreshLayout.setRefreshing(true);
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

            }
        });
    }


    @Override
    public void onRefresh() {
        getRingsNearMeCall(dataHolder.getUserLocation());
        updateRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
    }
}
