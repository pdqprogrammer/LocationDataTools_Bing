import java.util.List;

public class LocationMapSite {
    //take in values
    //use values to generate html
    //different inputs provide different versions of the page

    //generate general map based on input coords -- no pin
    //directory, need coords,
    public static String GenerateLocationMap(String fileDirectory, double[] coords, int zoom){

        return "";//return file location of map
    }

    //generate map with single pin based on input coords
    //directory, need coords, pin image array
    public static String GeneratePinLocationMap(String fileDirectory, double[] coords, String[] pins, int zoom){

        return "";
    }

    //generate map with multiple pins based on variety of locations coords provided
    //directory, coord list, pin image array, center coords
    public static String GenerateMultiPinLocationMap(String fileDirectory, List<double[]> coords, double[] centerCoords, String[] pins, int zoom){

        return "";
    }

    //build page with correct data
    private static void BuildMapPage(String fileDirectory, List<double[]> coords, double[] centerCoords, String[] pins){
        //pull html resource as string
        //replace pin visible if pins in array
        //swap in first pin if pins in array otherwise default to blank

        //swap in center/focus coords

        //swap in zoom level

        //if multiple locations then loop thru each and add pin to map -- increment on pin array and loop to beginning if more locations then pins
    }
}
