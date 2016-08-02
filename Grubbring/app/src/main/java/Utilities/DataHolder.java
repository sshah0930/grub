package Utilities;

import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Models.Ring;
import Models.RingAroundMe;

/**
 * Created by shivangshah on 6/15/16.
 */
public class DataHolder {

    ArrayList<RingAroundMe> ringsNearMeList;
    ArrayList<Ring> userRings;
    Location location;

    private static DataHolder instance = new DataHolder();

    public static DataHolder getInstance() {
        return instance;
    }

    private DataHolder() {
    }

    public void setRingsNearMeList(ArrayList<RingAroundMe> ringsNearMe){
        ringsNearMeList = ringsNearMe;
    }

    public ArrayList<RingAroundMe> getRingsNearMeList(){
        return ringsNearMeList;
    }

    public void setUserRings(ArrayList<Ring> userRings){
        this.userRings = userRings;
    }

    public ArrayList<Ring> getUserRings(){
        return userRings;
    }

    public void setUserLocation(Location location){
        this.location = location;
    }

    public Location getUserLocation(){
        return location;
    }
}
