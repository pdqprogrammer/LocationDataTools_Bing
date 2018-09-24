import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testRun {
    private static List<String[]> testLocationList = new ArrayList<>();
    private static List<String[]> testClosestList = new ArrayList<>();

    public static void main(String[] args){
        //initialize lists to use for various checks
        InitList();

        System.exit(0);
    }

    private static void InitList(){
        String[][] locArray = {
                {"address","city","state","zip","country"},
                {"address","city","state","zip","country"},
                {"address","city","state","zip","country"},
                {"address","city","state","zip","country"},
                {"address","city","state","zip","country"}
        };

        testLocationList = Arrays.asList(locArray);

        String[][] closeArray = {
                {"address","city","state","zip","country","latitude","longitude"},
                {"address","city","state","zip","country","latitude","longitude"},
                {"address","city","state","zip","country","latitude","longitude"},
                {"address","city","state","zip","country","latitude","longitude"},
                {"address","city","state","zip","country","latitude","longitude"}
        };

        testClosestList = Arrays.asList(closeArray);
    }
}

//important functions to develop
//create list of closest locations -- set max distance, location max count, list of locations
//query for all information on an address based on result -- location information
//build a location map site -- list of locations, pin list, center point coords, set zoom (auto or manual)
//append map into an already built website -- base html, id to insert into
//map screen shot capabilities -- map site, class or tool to take screens of site
