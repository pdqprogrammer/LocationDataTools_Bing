import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationManagement {
    public static String[] GetLocationDetails(String query){
        if (query.isEmpty())
            return new String[]{};

        String bingURL = LocationServices.QueryData(query);

        //call api here to json object
        JSONObject bingApiJson = JSONGetter.GetJsonString(bingURL);

        //check if json not empty
        if(bingApiJson.isEmpty())
            return new String[]{};

        //check for location information. push to string array. return string array
        String[] locationInfo = new String[7];

        //fill in string array with api data

        return locationInfo;
    }

    public static String[] GetLocationDetails(String address, String city, String state, String zip, String country){
        if(country.isEmpty() && address.isEmpty() && city.isEmpty() && state.isEmpty() && zip.isEmpty())
            return new String[]{};

        String bingURL = LocationServices.SpecificQueryData(address, city, state, zip, country);

        //call api here to json object
        JSONObject bingApiJson = JSONGetter.GetJsonString(bingURL);

        //check if json not empty
        if(bingApiJson.isEmpty())
            return GetLocationDetails(address + " " + city  + "," + state  + " " +  zip);

        //check for location information. push to string array. return string array
        String[] locationInfo = new String[7];

        //fill in string array with api data

        return locationInfo;
    }

    public static float GetDistanceBetween(List<float[]> coords){
        if(coords.size() < 2)
            return -1.0f;

        String bingURL = LocationServices.DistanceMilesData(coords);

        //call api here to json object
        JSONObject bingApiJson = JSONGetter.GetJsonString(bingURL);

        //check if json not empty
        if(bingApiJson.isEmpty())
            return 0.0f;

        //if not empty read json for distance and return as float
        float distanceInMiles = 0.0f;

        //set distance based on api data converted to a float

        return distanceInMiles;
    }

    public static float[] GetCenterCoords(List<float[]> coords){
        float[] centerCoords = new float[2];

        for(int i=0; i<coords.size(); i++){
            centerCoords[0] += coords.get(i)[0];
            centerCoords[1] += coords.get(i)[1];
        }

        centerCoords[0] = centerCoords[0]/coords.size();
        centerCoords[1] = centerCoords[1]/coords.size();

        return centerCoords;
    }

    public static List<List<String[]>> GetClosestLocationList(List<String[]> locationList, int[] coordColumns){
        //

        return new ArrayList<>();
    }
}
