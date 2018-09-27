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

    //method to return list of closest locations
    public static List<String[]> GetClosestLocationList(String[] location, List<String[]> locationList, int locationCount, double maxDistance){
        //method to get list with distances
        List<String[]> locationCoordDistList = GetDistanceList(location, locationList);

        //call method to sort the list here

        //check list size and if smaller than location count set its size to count
        if (locationList.size() < locationCount)
            locationCount = locationList.size();

        List<String[]> closestLocationsList = new ArrayList<>();//list to hold closest locations list

        //loop through locations with distance list
        for(int i=0; i<locationCoordDistList.size(); i++){
            String[] currLocation = locationCoordDistList.get(i);//get current location
            double distance = Double.parseDouble(currLocation[currLocation.length-1]);//get distance value

            //if distance is > maxDistance then continue for now...if sorted list then break
            if (distance > maxDistance)
                continue;//switch to break after developing sorted list

            closestLocationsList.add(currLocation);//add to closest location list

            //if closest location list populated with location count amount then break out of loop
            if(closestLocationsList.size() >= locationCount)
                break;
        }

        return closestLocationsList;
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

    //method to get list with distances
    private static List<String[]> GetDistanceList(String[] location, List<String[]> locationList){
        List<String[]> locationDistList = new ArrayList<>();

        double[] locationCoords = {
                Double.parseDouble(location[5]),
                Double.parseDouble(location[6])
        };

        for (int i=0; i<locationList.size(); i++){
            String[] currLoc = new String[locationList.get(i).length + 1];
            System.arraycopy(locationList.get(i), 0, currLoc, 0, locationList.get(i).length);

            double[] currCoords = {
                    Double.parseDouble(currLoc[5]),
                    Double.parseDouble(currLoc[6])
            };

            List<double[]> coordsList = new ArrayList<>();
            coordsList.add(locationCoords);
            coordsList.add(currCoords);

            currLoc[currLoc.length - 1] = GetDistanceBetween(coordsList) + "";

            locationDistList.add(currLoc);
        }

        return locationDistList;
    }

    //method to sort location list
    private static List<String[]> GetSortedDistList(List<String[]> locationDistList){
        if(locationDistList.isEmpty())
            return locationDistList;

        List locDistA = locationDistList.subList(0, locationDistList.size()/2);
        List locDistB = locationDistList.subList(locationDistList.size() - locDistA.size(), locationDistList.size()-1);

        locDistA = GetSortedDistList(locDistA);
        locDistB = GetSortedDistList(locDistB);

        locationDistList = MergeLists(locDistA, locDistB);

        return locationDistList;
    }

    private static List<String[]> MergeLists(List<String[]> locDistListA, List<String[]> locDistListB){
        int listAIndex = 0;
        int listBIndex = 0;

        List<String[]> mergedList = new ArrayList<>();

        while(listAIndex < locDistListA.size() && listBIndex < locDistListB.size()){
            double distanceA = Double.parseDouble(locDistListA.get(listAIndex)[7]);
            double distanceB = Double.parseDouble(locDistListB.get(listBIndex)[7]);

            if(distanceA < distanceB){
                mergedList.add(locDistListA.get(listAIndex));
                listAIndex++;
            } else {
                mergedList.add(locDistListB.get(listBIndex));
                listBIndex++;
            }
        }

        if(listAIndex < locDistListA.size())
            mergedList.addAll(locDistListA.subList(listAIndex, locDistListA.size()-1));

        if(listBIndex < locDistListB.size())
            mergedList.addAll(locDistListB.subList(listBIndex, locDistListB.size()-1));

        return mergedList;
    }
}
