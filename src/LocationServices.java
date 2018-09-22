public class LocationServices {
    private static final String BING_KEY = "AoZot6IfTomvuyyuFD-J-9HCjZ-VV3k4da0wSh5guC3Q0irjul7pin0MwDx2rB4L";

    public LocationServices(){

    }

    public String QueryData(String query) {
        //http://dev.virtualearth.net/REST/v1/Locations?query=117%20Husson%20Ave%20Bronx,%20NY&key=Ak3auHZ7m5BgMIhvG6OrkgRl-dre56Jel4DG_C09nodP_sRRfyMQO2BpWVt_74_K
        String webURL = "http://dev.virtualearth.net/REST/v1/Locations?query=" + query + "&key=" + BING_KEY;

        return webURL.replace(" ", "%20");
    }

    public String SpecificQueryData(String address, String city, String state, String zip, String country){
        String webURL = "http://dev.virtualearth.net/REST/v1/Locations?";

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

        return webURL.replace(" ", "%20");
    }

    public String DistanceMilesData (String[] coords1, String[] coords2){
        String webURL = "http://dev.virtualearth.net/REST/v1/Routes?";

        try{
            webURL += "waypoint.1=" + coords1[0] + "," + coords1[1] + "&";
            webURL += "waypoint.2=" + coords1[0] + "," + coords1[1] + "&";

            webURL += "distanceUnit=mi&key=" + BING_KEY;

            return webURL.replace(" ", "%20");
        } catch (Exception e) {
            return "";
        }
    }
}
