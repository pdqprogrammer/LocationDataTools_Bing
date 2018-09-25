import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testRun {
    public static void main(String[] args){
        //initialize lists to use for various checks
        List<String[]> testLocationList = InitList();
        List<double[]> testCoordsList = InitCoords();

        //run test on getting different location data and output to terminal
        for(int i=0; i<testLocationList.size(); i++){
            //get location data using specific query
            //get location using general query
            //print to terminal

            String[] currLoc = testLocationList.get(i);

            String[] detailLoc = LocationManagement.GetLocationDetails(currLoc[0], currLoc[1], currLoc[2], currLoc[3], currLoc[4]);
            String[] broadLoc = LocationManagement.GetLocationDetails(currLoc[0] + " " + currLoc[1]  + " " + currLoc[2]  + " " +  currLoc[3]);
        }

        double distanceTest = LocationManagement.GetDistanceBetween(testCoordsList);

        System.exit(0);
    }

    private static List<String[]> InitList(){
        String[][] locArray = {
                {"500 E 14th St","New York","NY","10009",""},
                {"100 14th St","Jersey City","NJ","07310-1202","US"},
                {"2300 W Ben White Blvd","Austin","TX","","United States"},
                {"22832 US Highway 281 N","Austin","","",""},
                {"1800 W Empire Ave","Burbank","CA","91504","United States"}
        };

        return Arrays.asList(locArray);
    }

    private static List<double[]> InitCoords(){
        double[][] coordsArray = {
                {34.188560,-118.371399},
                {34.245144,-118.419395},
                {34.141083,-118.224083}
        };

        return Arrays.asList(coordsArray);
    }
}

//important functions to develop
//create list of closest locations -- set max distance, location max count, list of locations
//query for all information on an address based on result -- location information -- check
//build a location map site -- list of locations, pin list, center point coords, set zoom (auto or manual)
//append map into an already built website -- base html, id to insert into
//map screen shot capabilities -- map site, class or tool to take screens of site
