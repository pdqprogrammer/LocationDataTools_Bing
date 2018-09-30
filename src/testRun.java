import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testRun {
    public static void main(String[] args){
        //initialize lists to use for various checks
        List<String[]> testLocationList = InitList();
        List<double[]> testCoordsList = InitCoords();

        List<String[]> testFullDetailList = new ArrayList<>();

        String fileLoc = System.getProperty("user.home") + "/Desktop/Maps/testing.html";

        //run test on getting different location data and output to terminal
        for(int i=0; i<testLocationList.size(); i++){
            String[] currLoc = testLocationList.get(i);
            String[] detailLoc = LocationManagement.GetLocationDetails(currLoc[0], currLoc[1], currLoc[2], currLoc[3], currLoc[4]);

            testFullDetailList.add(detailLoc);
        }

        double distanceTest = LocationManagement.GetDistanceBetween(testCoordsList);//distance test
        double[] centerCoordsList = LocationManagement.GetCenterCoords(testCoordsList);//center coord test

        List<String[]> closeList = LocationManagement.GetClosestLocationList(testFullDetailList.get(2), testFullDetailList, 3, 5000.0);//closest list test

        //test to build a multipin map site
        LocationMapSite.GenerateMultiPinLocationMap(fileLoc, testCoordsList, centerCoordsList, new String[]{"I:/GameWork/GameCode/CurrentProjects/LocationTools/LocationDataTools_Bing/resources/map-pin-png-6.png"}, 10);

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

//functions to develop in future
//append map into an already built website -- base html, id to insert into
//map screen shot capabilities -- map site, class or tool to take screens of site
