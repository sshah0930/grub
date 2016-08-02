package Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Constants.StatusCodes;
import Models.Ring;
import Models.RingAroundMe;

/**
 * Created by shivangshah on 5/28/16.
 */
public class ResponseHandlerUtilities {

    public static String getLoginAttempts(JSONObject response){
        String loginFailedMessage = "";

            try{
                String status = response.getString("status");
                if(status.equals(StatusCodes.LOGIN_ATTEMPTS_SUCCESS)){
                    JSONObject dataObj = response.getJSONObject("data");
                    int loginAttempts = dataObj.getInt("loginAttempts");
                    String accountStatus = dataObj.getString("accountStatus");

                    if(loginAttempts > 0 && accountStatus.equals("active")){
                        loginFailedMessage = "Invalid username or password.";
                    }else if(loginAttempts == 0 && accountStatus.equals("Blocked")){
                        loginFailedMessage = "Your account is blocked. Please reactivate your account.";
                    }
                }else if(status.equals(StatusCodes.LOGIN_ATTEMPTS_FAIL)){
                    loginFailedMessage = "This username does not exist.";
                }
            }catch (Exception e){

            }

        return loginFailedMessage;

    }

    public static ArrayList<Ring> userRings(JSONObject response){
        ArrayList<Ring> userRingsList = new ArrayList<>();

        try{
            String status = response.getString("status");
            if(status.equals(StatusCodes.RECIEVED_SUBSCRIBED_RINGS_SUCCESS)){
                JSONArray dataArray = response.getJSONArray("data");
                for(int i=0;i<dataArray.length();i++){
                    Ring userRing = new Ring();
                    JSONObject ringObj = dataArray.getJSONObject(i);

                    userRing.setRingId(ringObj.getInt("ringId"));
                    userRing.setRingName(ringObj.getString("name"));

                    userRingsList.add(userRing);
                }
            }
        }catch (Exception e){

        }

        return userRingsList;
    }

    public static ArrayList<RingAroundMe> getRingsNearMe(JSONObject response){
        ArrayList<RingAroundMe> ringsNearMeList = new ArrayList<>();

        try{
            String status = response.getString("status");
            if(status.equals(StatusCodes.RETURNED_RINGS_NEAR_USER_SUCCESS)){
                JSONArray dataArray = response.getJSONArray("data");
                for(int i=0;i<dataArray.length();i++){
                    RingAroundMe ringAroundMe = new RingAroundMe();
                    JSONObject ringObj = dataArray.getJSONObject(i);

                    ringAroundMe.setAddress(ringObj.getString("addr"));
                    ringAroundMe.setCity(ringObj.getString("city"));
                    ringAroundMe.setState(ringObj.getString("state"));
                    ringAroundMe.setRingName(ringObj.getString("name"));
                    ringAroundMe.setCreatorFirstName(ringObj.getString("firstName"));
                    ringAroundMe.setCreatorLastName(ringObj.getString("lastName"));
                    ringAroundMe.setRingId(ringObj.getInt("ringId"));
                    ringAroundMe.setLatitude(ringObj.getDouble("latitude"));
                    ringAroundMe.setLongitude(ringObj.getDouble("longitude"));

                    ringsNearMeList.add(ringAroundMe);
                }

            }
        }catch (Exception e){

        }

        return ringsNearMeList;
    }

    public static boolean requestToJoinRing(JSONObject response){
        boolean successJoinRequest = false;



        return successJoinRequest;
    }
}
