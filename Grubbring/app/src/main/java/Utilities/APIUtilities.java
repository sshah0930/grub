package Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Constants.DeveloperConfig;
import Constants.SharedPreferencesKeys;
import Constants.StatusCodes;
import VolleyCallbackInterfaces.APISuccessCallback;
import VolleyCallbackInterfaces.LoginSuccessCallback;

/**
 * Created by shivangshah on 5/16/16.
 */
public class APIUtilities {

    Context context;
    String sessionCookie;

    public APIUtilities(Context context){
        this.context = context;

        sessionCookie = SharedPreferencesUtilities.getSharedPrefSessionCookie(context);
    }

    public void loginAPI(final String username, final String password, final LoginSuccessCallback callback){

        String loginURL = DeveloperConfig.API_HOST_URL + "/login";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest loginRequest = new StringRequest(Request.Method.POST,loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject responseObject = (new JsonParser()).parse(response).getAsJsonObject();

                String status = String.valueOf(responseObject.get("status")).replace("\"","");
                //String cookie = String.valueOf(responseObject.get("cookieSession"));

                if(status.equals(StatusCodes.LOGIN_SUCCESS)){

                    SharedPreferencesUtilities.updateSharedPrefSessionCookie(context, String.valueOf(responseObject.get("cookieSession")));
                    callback.onAPICallSuccess(true);

                }else{

                    callback.onAPICallSuccess(false);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onAPICallSuccess(false);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                JSONObject finalResponseObj = new JSONObject();

                String sessionId = networkResponse.headers.get("Set-Cookie");
                sessionId = sessionId.substring(0,sessionId.indexOf(";"));

                String bodyResponse = new String(networkResponse.data);

                String finalResponse = "";
                com.android.volley.Response<String> result = null;

                try{
                    JsonObject jsonObject = (new JsonParser()).parse(bodyResponse).getAsJsonObject();
                    jsonObject.addProperty("cookieSession", sessionId);

                    finalResponseObj.put("cookieSession", sessionId);
                    finalResponseObj.put("body", bodyResponse);

                    finalResponse = jsonObject.toString();

                    result = com.android.volley.Response.success(finalResponse,
                            HttpHeaderParser.parseCacheHeaders(networkResponse));

                }catch (Exception e){

                }

                return result;
            }
        };
        queue.add(loginRequest);
    }

    public void getLoginAttemptsAPI(String email, final APISuccessCallback callback){
        String url = DeveloperConfig.API_HOST_URL + "/profile/loginAttempts/"+email;

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        callback.onAPICallSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        callback.onAPICallSuccess(null);
                    }
                }

        );

        queue.add(profileRequest);
    }

    public void getUserRingsAPI(final APISuccessCallback callback){
        String url = DeveloperConfig.API_HOST_URL + "/ring/subscribedRings";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest userRingsRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        callback.onAPICallSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        SharedPreferencesUtilities.deleteSharedPreferenceSessionCookie(context);
                        callback.onAPICallSuccess(null);
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cookie", sessionCookie);
                return params;
            }
        };

        queue.add(userRingsRequest);

    }

    public void getRingsNearMe(double latitude,double longitude, final APISuccessCallback callback){
        String url = DeveloperConfig.API_HOST_URL + "/ring?latitude="+latitude+"&longitude="+longitude;
        //url = "https://grubbring-api-sshah0930-1.c9.io/api/ring?latitude=40.9261129&longitude=-74.1046374";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest ringsNearMeRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        callback.onAPICallSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        SharedPreferencesUtilities.deleteSharedPreferenceSessionCookie(context);
                        callback.onAPICallSuccess(null);
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cookie", sessionCookie);
                return params;
            }
        };

        queue.add(ringsNearMeRequest);

    }

//    public void beginRegistrationAPI(final VolleyCallbackAPICall callback){
//
//        String url = "https://grubbring-api-sshah0930-1.c9.io/api/registration/beginRegistration";
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // response
//                        callback.onAPICallSuccess(response);
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//
//                    }
//                }
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("Cookie", sessionCookie);
//                return params;
//            }
//
////            @Override
////            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
////                if (response != null) {
////                    mStatusCode = response.statusCode;
////                }
////                return super.parseNetworkResponse(response);
////            }
//        };
//
//        queue.add(profileRequest);
//    }

    public void requestToJoinRingAPI(int ringId, final APISuccessCallback callback){
        String url = DeveloperConfig.API_HOST_URL + "/ring/join/"+ringId;

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest joinRingRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        callback.onAPICallSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        callback.onAPICallSuccess(null);
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Cookie", sessionCookie);
                return params;
            }
        };

        queue.add(joinRingRequest);
    }

    public void profileAPI(final APISuccessCallback callback){

        String url = DeveloperConfig.API_HOST_URL + "/profile";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        callback.onAPICallSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        SharedPreferencesUtilities.deleteSharedPreferenceSessionCookie(context);
                        callback.onAPICallSuccess(null);

                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Cookie", sessionCookie);
                return params;
            }
        };

        queue.add(profileRequest);
    }

    public void logoutAPI(final LoginSuccessCallback callback){

        String url = DeveloperConfig.API_HOST_URL + "/logout";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest logoutRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        SharedPreferencesUtilities.deleteSharedPreferenceSessionCookie(context);
                        callback.onAPICallSuccess(true);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        SharedPreferencesUtilities.deleteSharedPreferenceSessionCookie(context);
                        callback.onAPICallSuccess(false);

                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Cookie", sessionCookie);
                return params;
            }
        };

        queue.add(logoutRequest);
    }

}
