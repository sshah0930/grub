package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.R;

import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.RingsAroundMeAdapter;
import Adapters.UserRingsAdapter;
import Constants.FragmentTags;
import Models.Ring;
import Models.RingAroundMe;
import Utilities.APIUtilities;
import Utilities.DataHolder;
import Utilities.ResponseHandlerUtilities;
import VolleyCallbackInterfaces.APISuccessCallback;
import VolleyCallbackInterfaces.RingsAroundMeItemClickListener;
import VolleyCallbackInterfaces.UserRingsItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRingsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    DataHolder dataHolder;
    APIUtilities apiUtilities;

    BaseActivity activity;
    private FloatingActionButton createRingFloatingActionButton;
    private RecyclerView userRingsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UserRingsAdapter recylerViewAdapter;
    private ContentLoadingProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;


    private ArrayList<Ring> userRingsList;

    public MyRingsFragment() {
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

        View view = inflater.inflate(R.layout.fragment_my_rings, container, false);

        activity = (BaseActivity) getActivity();
        apiUtilities = new APIUtilities(activity);
        activity.myToolbar.setTitle("My Rings");
        activity.coreScreenLayout();

        dataHolder = DataHolder.getInstance();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshUserRingsLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.progressBar);
        createRingFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.createRingFloatingActionButton);
        createRingFloatingActionButton.setOnClickListener(this);

        userRingsRecyclerView = (RecyclerView)view.findViewById(R.id.userRingsRecyclerView);
        userRingsRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        userRingsRecyclerView.setLayoutManager(layoutManager);
        userRingsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        userRingsList = dataHolder.getUserRings();

        if(userRingsList != null){
            updateRecyclerView();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            getUserRings();
        }

        return view;
    }

    private void getUserRings(){
        apiUtilities.getUserRingsAPI(new APISuccessCallback() {
            @Override
            public void onAPICallSuccess(JSONObject responseObject) {
                userRingsList = ResponseHandlerUtilities.userRings(responseObject);
                progressBar.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                if(userRingsList.size() > 0){
                    dataHolder.setUserRings(userRingsList);
                    updateRecyclerView();
                }else{
                    activity.fragmentManager.beginTransaction().replace(R.id.content_frame, new RingsAroundMeParentFragment(),FragmentTags.RINGS_NEAR_ME_PARENT_FRAGMENT_TAG).commit();                }
            }
        });
    }

    private void updateRecyclerView(){
        recylerViewAdapter = new UserRingsAdapter(userRingsList, new UserRingsItemClickListener() {
            @Override
            public void onItemClick(Ring ring) {
                Fragment ringDetailFragment = new RingDetailParentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("layoutStyle", "both");
                bundle.putParcelable("selectedRing",ring);
                ringDetailFragment.setArguments(bundle);
                activity.fragmentManager.beginTransaction().replace(R.id.content_frame,ringDetailFragment).addToBackStack("backToUserRings").commit();
            }
        });
        userRingsRecyclerView.setAdapter(recylerViewAdapter);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getUserRings();
        updateRecyclerView();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == createRingFloatingActionButton.getId()){
            activity.fragmentManager.beginTransaction().replace(R.id.content_frame, new CreateRingFragment(),FragmentTags.CREATE_RING_FRAGMENT_TAG).addToBackStack("backToMyRings").commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_myrings_items, menu);

        MenuItem item = menu.findItem(R.id.ringsNearMeNavigationItem);
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.white)), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ringsNearMeNavigationItem) {

            activity.fragmentManager.beginTransaction().replace(R.id.content_frame, new RingsAroundMeParentFragment(),FragmentTags.RINGS_NEAR_ME_PARENT_FRAGMENT_TAG).commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
