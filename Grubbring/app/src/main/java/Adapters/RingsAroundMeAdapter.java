package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamgrub.grubbring.R;

import java.util.ArrayList;

import Models.Ring;
import Models.RingAroundMe;
import VolleyCallbackInterfaces.RingsAroundMeItemClickListener;

/**
 * Created by shivangshah on 5/30/16.
 */
public class RingsAroundMeAdapter extends RecyclerView.Adapter<RingsAroundMeAdapter.MyViewHolder> {

    ArrayList<RingAroundMe> ringsAroundMeList;
    RingsAroundMeItemClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ringNameTextView;
        TextView addressTextView;
        TextView numberOfMembersTextView;
        TextView distanceTextView;
        ImageView ringLogoImageView;
        ImageView joinRingImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.ringNameTextView = (TextView) itemView.findViewById(R.id.ringNameTextView);
            this.addressTextView = (TextView) itemView.findViewById(R.id.ringAddressTextView);
            this.numberOfMembersTextView = (TextView) itemView.findViewById(R.id.numberOfMembersTextView);
            this.distanceTextView = (TextView) itemView.findViewById(R.id.distanceTextView);
            this.ringLogoImageView = (ImageView) itemView.findViewById(R.id.ringLogoImageView);
            this.joinRingImageView = (ImageView) itemView.findViewById(R.id.joinRingImageView);
        }
        public void bind(final RingAroundMe ring, final RingsAroundMeItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(ring);
                }
            });
        }
    }

    public RingsAroundMeAdapter(ArrayList<RingAroundMe> ringsAroundMeList, RingsAroundMeItemClickListener listener){
        this.ringsAroundMeList = ringsAroundMeList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rings_near_me_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(ringsAroundMeList.get(position), listener);
        TextView ringNameTextView = holder.ringNameTextView;
        TextView addressTextView = holder.addressTextView;
        TextView numberOfMembersTextView = holder.numberOfMembersTextView;
        TextView distanceTextView = holder.distanceTextView;
        ImageView ringLogoImageView = holder.ringLogoImageView;
        ImageView joinRingImageView = holder.joinRingImageView;

        String ringAddress = ringsAroundMeList.get(position).getAddress();
        String ringCity = ringsAroundMeList.get(position).getCity();
        String ringState = ringsAroundMeList.get(position).getState();

        ringNameTextView.setText(ringsAroundMeList.get(position).getRingName());
        addressTextView.setText(ringAddress + "\n" + ringCity + ", " + ringState);
        numberOfMembersTextView.setText("7 members");
        distanceTextView.setText("0.3 miles");

    }

    @Override
    public int getItemCount() {
        return ringsAroundMeList.size();
    }
}