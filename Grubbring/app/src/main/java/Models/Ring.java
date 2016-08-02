package Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shivangshah on 5/30/16.
 */
public class Ring implements Parcelable {

    public Ring(){}

    public String ringName;
    public int ringId;
    public String creatorFirstName;
    public String creatorLastName;
    public String address;
    public String city;
    public String state;
    public Double latitude;
    public Double longitude;

    public String getRingName() {
        return ringName;
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }

    public int getRingId() {
        return ringId;
    }

    public void setRingId(int ringId) {
        this.ringId = ringId;
    }

    public String getCreatorFirstName() {
        return creatorFirstName;
    }

    public void setCreatorFirstName(String creatorFirstName) {
        this.creatorFirstName = creatorFirstName;
    }

    public String getCreatorLastName() {
        return creatorLastName;
    }

    public void setCreatorLastName(String creatorLastName) {
        this.creatorLastName = creatorLastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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

    protected Ring(Parcel in) {
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

    public static final Parcelable.Creator<Ring> CREATOR = new Parcelable.Creator<Ring>() {
        @Override
        public Ring createFromParcel(Parcel source) {
            return new Ring(source);
        }

        @Override
        public Ring[] newArray(int size) {
            return new Ring[size];
        }
    };
}
