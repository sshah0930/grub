package Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shivangshah on 6/18/16.
 */
public class RingAroundMe extends Ring implements Parcelable {

    private Double distanceFromUser;

    public Double getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(Double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.distanceFromUser);
        dest.writeString(this.ringName);
        dest.writeInt(this.ringId);
        dest.writeString(this.creatorFirstName);
        dest.writeString(this.creatorLastName);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
    }

    public RingAroundMe() {
    }

    protected RingAroundMe(Parcel in) {
        this.distanceFromUser = (Double) in.readValue(Double.class.getClassLoader());
        this.ringName = in.readString();
        this.ringId = in.readInt();
        this.creatorFirstName = in.readString();
        this.creatorLastName = in.readString();
        this.address = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<RingAroundMe> CREATOR = new Parcelable.Creator<RingAroundMe>() {
        @Override
        public RingAroundMe createFromParcel(Parcel source) {
            return new RingAroundMe(source);
        }

        @Override
        public RingAroundMe[] newArray(int size) {
            return new RingAroundMe[size];
        }
    };
}
