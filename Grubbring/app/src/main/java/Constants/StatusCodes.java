package Constants;

/**
 * Created by shivangshah on 5/24/16.
 */
public class StatusCodes {

    // --------------- Rings API Status Codes -------------
    public static final String REQUEST_TO_JOIN_RING_SUCCESS = "10001";
    public static final String REQUEST_TO_JOIN_RING_FAIL = "00002";
    public static final String REQUEST_TO_LEAVE_RING_SUCCESS = "10003";
    public static final String CREATE_RING_SUCCESS = "10002";
    public static final String CREATE_RING_FAIL = "00009";
    public static final String DELETE_RING_SUCCESS = "10007";
    public static final String DELETE_RING_FAIL = "00005";
    public static final String USER_NOT_SUBSCRIBED_TO_RINGS = "00007";
    public static final String USER_NOT_LEADER_TO_ANY_RINGS = "00006";

//----------------------------------------------------------

    // --------------- Activities API Status Codes -------------
    public static final String CREATE_ACTIVITY_SUCCESS = "11001";
    public static final String CREATE_ACTIVITY_FAIL = "00003";
    public static final String DELETE_ACTIVITY_SUCCESS = "11003";
    public static final String NO_ACTIVITIES_FOUND = "00013";

    // --------------- User Access API Status Codes -------------
    public static final String LOGIN_SUCCESS = "12001";
    public static final String LOGIN_FAIL = "00010";
    public static final String PASSWORD_RESET_SUCCESS = "12003";
    public static final String ACCESS_CODE_VALIDATION_SUCCESS = "12007";
    public static final String UPDATE_USER_PROFILE_SUCCESS = "13001";
    public static final String UPDATE_USER_PROFILE_FAIL = "00012";
    public static final String LOGIN_ATTEMPTS_SUCCESS = "12005";
    public static final String LOGIN_ATTEMPTS_FAIL = "00013";
    public static final String RECIEVED_SUBSCRIBED_RINGS_SUCCESS = "00013";


    // --------------- Orders API Status Codes -----------------
    public static final String CREATE_ORDER_SUCCESS = "14001";
    public static final String CREATE_ORDER_FAIL = "00014";
    public static final String UPDATE_ORDER_SUCCESS = "14003";
    public static final String DELETE_ORDER_SUCCESS = "14005";


    // --------------- Search API Status Codes -----------------
    public static final String SEARCH_RESULTS_FOUND = "15001";
    public static final String SEARCH_RESULTS_NOT_FOUND = "15002";


    // --------------- Other API Status Codes ----------------
    public static final String RETURNED_RINGS_NEAR_USER_SUCCESS = "16001";
    public static final String RETRIEVE_USER_LOCATION_FAIL = "16002";
    public static final String RETURNED_GRUBBERIES_NEAR_USER_SUCCESS = "16003";
    public static final String EXECUTED_QUERY_SUCCESS = "16004";
    public static final String EXECUTED_QUERY_FAIL = "16005";
    public static final String DATABASE_CONNECTION_FAIL = "16006";

}
