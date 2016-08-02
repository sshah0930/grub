package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamgrub.grubbring.Activities.BaseActivity;
import com.teamgrub.grubbring.R;

import Constants.FragmentTags;
import Models.Ring;

public class RingDetailActivityListFragment extends Fragment implements View.OnClickListener{

    BaseActivity activity;
    RingDetailParentFragment detailParentFragment;

    private Ring selectedRing;

    private FloatingActionButton createActivityFloatingActionButton;

    public RingDetailActivityListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ring_detail_activity_list, container, false);

        activity = (BaseActivity) getActivity();

        createActivityFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.createActivityFloatingActionButton);
        createActivityFloatingActionButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == createActivityFloatingActionButton.getId()){
            activity.fragmentManager.beginTransaction().replace(R.id.content_frame, new CreateActivityFragment(), FragmentTags.CREATE_RING_FRAGMENT_TAG).addToBackStack("backToRingDetails").commit();
        }
    }

}
