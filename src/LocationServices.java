import java.util.List;

public class LocationServices {
    /*
    *MANAGE OF BING APIS BASED ON INPUTS PROVIDED BY METHODS CALLING THESE FUNCTIONS.
    *HELPS IN STREAMLINING THE API CALLS BASED IN NEEDS.
    */

    private static final String BING_KEY = "AoZot6IfTomvuyyuFD-J-9HCjZ-VV3k4da0wSh5guC3Q0irjul7pin0MwDx2rB4L";//Bing Key

    //pull query url for Bing query of address information
    public static String QueryData(String query) {
        //http://dev.virtualearth.net/REST/v1/Locations?query=117%20Husson%20Ave%20Bronx,%20NY --- Query Example
        String webURL = "http://dev.virtualearth.net/REST/v1/Locations?query=" + query + "&key=" + BING_KEY;

        return webURL.replace(" ", "%20");//return url with spaces replaced
    }

    //pull query url for Bing of specific address information provided
    public static String SpecificQueryData(String address, String city, String state, String zip, String country){
        String webURL = "http://dev.virtualearth.net/REST/v1/Locations?";

        //append location fields that contain data
        if(!country.isEmpty())
            webURL += "countryRegion=" + country +"&";
        if(!address.isEmpty())
            webURL += "addressLine=" + address +"&";
        if(!city.isEmpty())
            webURL += "adminDistrict=" + city +"&";
        if(!state.isEmpty())
            webURL += "locality=" + state +"&";
        if(!zip.isEmpty())
            webURL += "postalCode=" + zip +"&";

        webURL += "key=" + BING_KEY;

        return webURL.replace(" ", "%20");//return url with spaces replaced
    }

    //pull query url for distance in miles for Bing
    public static String DistanceMilesData (List<double[]> coords){
        String webURL = "http://dev.virtualearth.net/REST/v1/Routes?";

        try{
            //check each of the coords  and append them to url
            for(int i=0; i<coords.size(); i++){
                int waypoint = 1 + i;

                webURL += "waypoint." + waypoint + "=" + coords.get(i)[0] + "," + coords.get(i)[1] + "&";
            }

            webURL += "distanceUnit=mi&key=" + BING_KEY;

            return webURL.replace(" ", "");//return url with spaces removed
        } catch (Exception e) {
            //inform user that the information was unable to be called
            System.out.println("failed to pull distance api");
            return "";
        }
    }
}
