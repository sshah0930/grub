package Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamgrub.grubbring.R;

import java.util.ArrayList;

import Models.Ring;
import Models.RingAroundMe;
import VolleyCallbackInterfaces.RingsAroundMeItemClickListener;
import VolleyCallbackInterfaces.UserRingsItemClickListener;

/**
 * Created by shivangshah on 7/3/16.
 */

public class UserRingsAdapter extends RecyclerView.Adapter<UserRingsAdapter.UserRingsViewHolder> {

    ArrayList<Ring> userRingsList;
    UserRingsItemClickListener listener;

    public static class UserRingsViewHolder extends RecyclerView.ViewHolder {

        TextView ringNameTextView;

        public UserRingsViewHolder(View itemView) {
            super(itemView);
            this.ringNameTextView = (TextView) itemView.findViewById(R.id.ringNameTextView);
        }

        public void bind(final Ring ring, final UserRingsItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(ring);
                }
            });
        }
    }

    public UserRingsAdapter(ArrayList<Ring> userRingsList, UserRingsItemClickListener listener){
        this.userRingsList = userRingsList;
        this.listener = listener;
    }

    @Override
    public UserRingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rings_near_me_row, parent, false);

        UserRingsViewHolder myViewHolder = new UserRingsAdapter.UserRingsViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(UserRingsAdapter.UserRingsViewHolder holder, int position) {
        holder.bind(userRingsList.get(position), listener);

        TextView ringNameTextView = holder.ringNameTextView;
        ringNameTextView.setText(userRingsList.get(position).getRingName());
    }

    @Override
    public int getItemCount() {
        return userRingsList.size();
    }

}
