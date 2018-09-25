import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationManagement {
    //get location data to get broad information
    public static String[] GetLocationDetails(String query){
        //check if query string is empty and return an empty string
        if (query.isEmpty())
            return new String[]{};

        //get bing url with query data
        String bingURL = LocationServices.QueryData(query);

        //call api here to json object
        JSONObject bingApiJson = JSONGetter.GetJsonString(bingURL);

        //check if json not empty
        if(bingApiJson.isEmpty())
            return new String[]{};

        try {
            //check for location information. push to string array. return string array
            JSONArray resources = bingApiJson.getJSONArray("resourceSets").getJSONObject(0).getJSONArray("resources");
            String[] locationInfo = GetBestMatchLoc(resources);

            return locationInfo;
        } catch(Exception e){
            return new String[]{};
        }
    }

    //get location details used specific information
    public static String[] GetLocationDetails(String address, String city, String state, String zip, String country){
        //check if all data is empty
        if(country.isEmpty() && address.isEmpty() && city.isEmpty() && state.isEmpty() && zip.isEmpty())
            return new String[]{};

        //get bing url using specific information
        String bingURL = LocationServices.SpecificQueryData(address, city, state, zip, country);

        //call api here to json object
        JSONObject bingApiJson = JSONGetter.GetJsonString(bingURL);

        //check if json not empty
        if(bingApiJson.isEmpty())
            return GetLocationDetails(address + " " + city  + " " + state  + " " +  zip);

        try {
            //check for location information. push to string array. return string array
            JSONArray resources = bingApiJson.getJSONArray("resourceSets").getJSONObject(0).getJSONArray("resources");
            String[] locationInfo = GetBestMatchLoc(resources);

            return locationInfo;
        } catch(Exception e){
            return new String[]{};
        }
    }

    //method to get the distance for coords list
    public static double GetDistanceBetween(List<double[]> coords){
        //check if there are more than two coords. if not then return 0.0
        if(coords.size() < 2)
            return 0.0;

        //get url for distance api call with coords
        String bingURL = LocationServices.DistanceMilesData(coords);

        //call api here to json object
        JSONObject bingApiJson = JSONGetter.GetJsonString(bingURL);

        //check if json not empty
        if(bingApiJson.isEmpty())
            return 0.0;

        //read json and get distance converted to float
        JSONObject resourceJson = bingApiJson.getJSONArray("resourceSets").getJSONObject(0).getJSONArray("resources").getJSONObject(0);
        float distanceInMiles = Float.parseFloat(resourceJson.get("travelDistance").toString());

        return distanceInMiles;
    }

    //method to determine center coords based on the total amount of coords provided
    public static double[] GetCenterCoords(List<double[]> coords){
        double[] centerCoords = new double[2];

        //sum all coords together -- need to add check for duplicate coordinates
        for(int i=0; i<coords.size(); i++){
            centerCoords[0] += coords.get(i)[0];
            centerCoords[1] += coords.get(i)[1];
        }

        //divide total coords by amount of coords input
        centerCoords[0] = centerCoords[0]/coords.size();
        centerCoords[1] = centerCoords[1]/coords.size();

        return centerCoords;//return result
    }

    public static List<List<String[]>> GetClosestLocationList(List<String[]> locationList, int[] coordColumns){
        //BUILD NEXT

        return new ArrayList<>();
    }

    //method to determine the best location match based on if match is good or ranking order
    private static String[] GetBestMatchLoc(JSONArray resourceJson){
        String[] locationInfo = new String[7];//hold location details

        JSONObject address;
        JSONObject point;

        //check to see how many results are returned and populate location information from api
        if(resourceJson.length() == 1){
            address = resourceJson.getJSONObject(0).getJSONObject("address");
            point = resourceJson.getJSONObject(0).getJSONObject("point");

            locationInfo[0] = address.get("addressLine").toString();
            locationInfo[1] = address.get("locality").toString();
            locationInfo[2] = address.get("adminDistrict").toString();
            locationInfo[3] = address.get("postalCode").toString();
            locationInfo[4] = address.get("countryRegion").toString();

            locationInfo[5] = point.getJSONArray("coordinates").get(0).toString();
            locationInfo[6] = point.getJSONArray("coordinates").get(1).toString();
        } else if (resourceJson.length() > 1){
            //loop through resource json array to find a good match or use first provided address
            for(int i=0; i<resourceJson.length(); i++){
                JSONArray matchCodeJsonArray = resourceJson.getJSONObject(i).getJSONArray("matchCodes");

                address = resourceJson.getJSONObject(i).getJSONObject("address");
                point = resourceJson.getJSONObject(i).getJSONObject("point");

                if (matchCodeJsonArray.toString().toLowerCase().contains("good")){
                    locationInfo[0] = address.get("addressLine").toString();
                    locationInfo[1] = address.get("locality").toString();
                    locationInfo[2] = address.get("adminDistrict").toString();
                    locationInfo[3] = address.get("postalCode").toString();
                    locationInfo[4] = address.get("countryRegion").toString();

                    locationInfo[5] = point.getJSONArray("coordinates").get(0).toString();
                    locationInfo[6] = point.getJSONArray("coordinates").get(1).toString();

                    break;
                } else if (i == 0){
                    locationInfo[0] = address.get("addressLine").toString();
                    locationInfo[1] = address.get("locality").toString();
                    locationInfo[2] = address.get("adminDistrict").toString();
                    locationInfo[3] = address.get("postalCode").toString();
                    locationInfo[4] = address.get("countryRegion").toString();

                    locationInfo[5] = point.getJSONArray("coordinates").get(0).toString();
                    locationInfo[6] = point.getJSONArray("coordinates").get(1).toString();
                }
            }
        } else {
            //failed to get any location information
            System.out.println("Failed to get location information from Bing API");
            return new String[]{};//return blank String array
        }

        return locationInfo;//send back populated String array
    }
}
