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
        if (locationList.size() < locationCount)
            locationCount = locationList.size();

        List<String[]> closestLocations = new ArrayList<>();
        String[] firstLocation = new String[location.length + 1];

        System.arraycopy(location, 0, firstLocation, 0, location.length);
        firstLocation[firstLocation.length-1] = "0.0";

        closestLocations.add(firstLocation);

        double[] locCoords = {
                Double.parseDouble(location[5]),
                Double.parseDouble(location[6])
        };

        for (int i=0; i<locationList.size(); i++){
            double[] listCoords = {
                    Double.parseDouble(locationList.get(i)[5]),
                    Double.parseDouble(locationList.get(i)[6])
            };

            List<double[]> coordsList = new ArrayList<>();
            coordsList.add(locCoords);
            coordsList.add(listCoords);

            double distanceBetween = LocationManagement.GetDistanceBetween(coordsList);

            if(distanceBetween > maxDistance)
                continue;

            String[] lastCloseLocation = closestLocations.get(closestLocations.size()-1);
            double lastDistance = Double.parseDouble(lastCloseLocation[lastCloseLocation.length-1]);

            //here is where i have to figure out how to manage
            //check if less than first
            //check if more than last
            //check if between first and last
            ////here is the one to figure out that requires some thinking
        }


        return closestLocations;
    }

    //method to return a list of the closest locations...needs optimizations and revamping of formula to run
    public static List<List<String[]>> GetClosestLocationList(List<String[]> locationList, int[] coordIndex, int locationCount, double maxDistance){
        //BUILD NEXT

        //update location count if location list is smaller
        if (locationList.size() < locationCount)
            locationCount = locationList.size();

        //array to hold
        List<List<String[]>> closeLocationsList = new ArrayList<>();

        //loop through main list
        for(int i=0; i<locationList.size(); i++){
            List<String[]> tempList = new ArrayList<String[]>(locationList);
            tempList.remove(i);

            //grab coords from main list
            double[] currCoords = {
                    Double.parseDouble(locationList.get(i)[coordIndex[0]]),
                    Double.parseDouble(locationList.get(i)[coordIndex[1]])
            };

            //list to hold current closest locations gathered
            List<String[]> currCloseList = new ArrayList<>();

            //loop through list minus curr main location
            for (int j=0; j<tempList.size(); j++){
                //grab coords from temp list
                double[] tempCoords = {
                        Double.parseDouble(tempList.get(j)[coordIndex[0]]),
                        Double.parseDouble(tempList.get(j)[coordIndex[1]])
                };

                //add coords to list to check against
                List<double[]> checkCoords = new ArrayList<>();
                checkCoords.add(currCoords);
                checkCoords.add(tempCoords);

                //get the distance between points using bing api
                double distance = GetDistanceBetween(checkCoords);

                //of distance is greater that maxDistance then continue on
                if (distance > maxDistance)
                    continue;

                //string to hold the location to put into list
                String[] putLocArray = new String[tempList.get(j).length + 1];

                if(currCloseList.size() > 0){
                    //grab last location and distance
                    String[] lastLoc = currCloseList.get(currCloseList.size()-1);
                    double lastDist = Double.parseDouble(lastLoc[lastLoc.length-1]);

                    //check if distance is greater than last distance in curr list
                    if (distance > lastDist){
                        //check is list is smaller in size than location amount set
                        if(currCloseList.size() < locationCount) {
                            //copy location info into put location array and set distance
                            System.arraycopy(tempList.get(j), 0, putLocArray, 0, tempList.get(j).length);
                            putLocArray[putLocArray.length - 1] = distance + "";

                            currCloseList.add(putLocArray);//add location into current closest list
                        } else {
                            continue;//move on to next location because the list is at capacity
                        }
                    } else {
                        //loop through the current close list
                        for (int k=0; k<currCloseList.size(); k++){
                            //grab the curr location in the current close list and distance
                            String[] currLoc = currCloseList.get(k);
                            double currDist = Double.parseDouble(currLoc[currLoc.length-1]);

                            //check if distance is closer
                            if(distance < currDist){
                                //replace the curr close location with current location in temp list
                                System.arraycopy(tempList.get(j), 0, putLocArray, 0, tempList.get(j).length);
                                putLocArray[putLocArray.length - 1] = distance + "";

                                currCloseList.add(k, putLocArray);

                                //check if location has gone over size and remove last element
                                if(currCloseList.size() > locationCount){
                                    currCloseList.remove(currCloseList.size()-1);
                                }

                                break;//get out of loop if conditions are hit earlier
                            }
                        }
                    }
                } else {
                    //add to empty list the current location in the temp list
                    System.arraycopy(tempList.get(j), 0, putLocArray, 0, tempList.get(j).length);
                    putLocArray[putLocArray.length - 1] = distance + "";

                    currCloseList.add(putLocArray);
                }
            }
            //push the current close list into close locations list
            closeLocationsList.add(currCloseList);
        }

        return closeLocationsList;
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
