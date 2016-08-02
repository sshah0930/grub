package Fragments;

import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.RingsAroundMeAdapter;
import Models.Ring;
import Models.RingAroundMe;
import Utilities.APIUtilities;
import Utilities.DataHolder;
import VolleyCallbackInterfaces.RingsAroundMeItemClickListener;

public class RingsAroundMeMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    ArrayList<RingAroundMe> ringsAroundMeList;

    private GoogleMap mMap;
    private Animation slideToShow;
    private Animation slideToHide;

    BaseActivity activity;
    APIUtilities apiUtilities;
    DataHolder dataHolder;

    private RecyclerView ringsAroundMeRecyclerViewHorizontal;
    private RecyclerView.LayoutManager layoutManager;
    private RingsAroundMeAdapter recylerViewAdapter;

    private Location userLocation;
    private LatLng userLatLng;
    private ArrayList<LatLng> ringsNearMeLatLngList;


    public RingsAroundMeMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rings_around_me_map, container, false);

        ringsAroundMeRecyclerViewHorizontal = (RecyclerView)view.findViewById(R.id.ringsNearMeRecyclerViewHorizontal);
        slideToShow = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_right_show);
        slideToHide = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_left_hide);

        activity = (BaseActivity)getActivity();
        apiUtilities = new APIUtilities(getActivity());
        dataHolder = DataHolder.getInstance();
        ringsAroundMeList = dataHolder.getRingsNearMeList();

        userLocation = dataHolder.getUserLocation();

        ringsAroundMeRecyclerViewHorizontal.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ringsAroundMeRecyclerViewHorizontal.setLayoutManager(layoutManager);
        ringsAroundMeRecyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());

        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        // Add a marker in Sydney, Australia, and move the camera.
        ringsNearMeLatLngList = new ArrayList<>();

        for(int i=0;i<ringsAroundMeList.size();i++){
            LatLng item = new LatLng(ringsAroundMeList.get(i).getLatitude(), ringsAroundMeList.get(i).getLongitude());
            ringsNearMeLatLngList.add(item);

            if(item != null){
                MarkerOptions marker = new MarkerOptions();
                mMap.addMarker(marker.position(item).title(ringsAroundMeList.get(i).getRingName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }

        addUserMarkerToMap();


        updateRecyclerView();

        CameraPosition cameraPosition = new CameraPosition.Builder().target(userLatLng).zoom(calculateZoomLevel()).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(marker.getTitle().length() != 0){
            showRingsNearMeHorizontalList();

            for(int i=0;i<ringsAroundMeList.size();i++){
                if (marker.getTitle().equals(ringsAroundMeList.get(i).getRingName()))
                {
                    ringsAroundMeRecyclerViewHorizontal.scrollToPosition(i);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        boolean shouldHideList = true;
        for(int i=0;i<ringsNearMeLatLngList.size();i++){
            if(ringsNearMeLatLngList.get(i) == latLng){
               shouldHideList = false;
                break;
            }
        }
        if(shouldHideList){
            hideRingsNearMeHorizontalList();
        }
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
        ringsAroundMeRecyclerViewHorizontal.setAdapter(recylerViewAdapter);
    }

    private void addUserMarkerToMap(){
        userLatLng = new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
        MarkerOptions userMarker = new MarkerOptions();
        userMarker.position(userLatLng);
        userMarker.title("");
        userMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(userMarker);
    }

    private void showRingsNearMeHorizontalList(){
        if(ringsAroundMeRecyclerViewHorizontal.getVisibility() == View.GONE){
            ringsAroundMeRecyclerViewHorizontal.setVisibility(View.VISIBLE);
            ringsAroundMeRecyclerViewHorizontal.startAnimation(slideToShow);
        }
    }

    private void hideRingsNearMeHorizontalList(){
        if(ringsAroundMeRecyclerViewHorizontal.getVisibility() == View.VISIBLE) {
            ringsAroundMeRecyclerViewHorizontal.setVisibility(View.GONE);
            ringsAroundMeRecyclerViewHorizontal.startAnimation(slideToHide);
        }
    }

    private int calculateZoomLevel() {
        Display display = activity.getWindowManager().getDefaultDisplay();

        double equatorLength = 40075004; // in meters
        double widthInPixels = display.getWidth();
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        while ((metersPerPixel * widthInPixels) > 2000) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }
        return zoomLevel;
    }

}
