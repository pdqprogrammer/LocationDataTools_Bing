import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationMapSite {
    //generate general map based on input coords -- no pin
    public static void GenerateLocationMap(String fileDirectory, double[] centerCoords, int zoom) {
        List<double[]> coords = new ArrayList();
        String[] pins = new String[0];
        BuildMapPage(fileDirectory, coords, centerCoords, pins, zoom);
    }

    //generate map with single pin based on input coords
    public static void GeneratePinLocationMap(String fileDirectory, double[] centerCoords, String[] pins, int zoom) {
        List<double[]> coords = new ArrayList();
        coords.add(centerCoords);
        BuildMapPage(fileDirectory, coords, centerCoords, pins, zoom);
    }

    //generate map with multiple pins based on variety of locations coords provided
    public static void GenerateMultiPinLocationMap(String fileDirectory, List<double[]> coords, double[] centerCoords, String[] pins, int zoom) {
        BuildMapPage(fileDirectory, coords, centerCoords, pins, zoom);
    }

    //build page with correct data
    private static void BuildMapPage(String fileDirectory, List<double[]> coords, double[] centerCoords, String[] pins, int zoom){
        //get base html file as a string
        String htmlString = GetBaseHTMLFile();

        //check the size of coords and pin count to determine if to show pins
        if (coords.size() >= 1 && pins.length >= 1) {
            htmlString = htmlString.replace("[PINS_VISIBLE_HERE]", "true");
        } else {
            htmlString = htmlString.replace("[PINS_VISIBLE_HERE]", "false");
        }

        //check pin count if pin should be used
        if (pins.length > 0) {
            htmlString = htmlString.replace("[FIRST_PIN_HERE]", pins[0]);
        } else {
            htmlString = htmlString.replace("[FIRST_PIN_HERE]", "");
        }

        //set the center point and zoom level of map
        htmlString = htmlString.replace("[CENTER_LAT_HERE]", centerCoords[0]+"");
        htmlString = htmlString.replace("[CENTER_LONG_HERE]", centerCoords[1]+"");
        htmlString = htmlString.replace("[ZOOM_LEVEL_HERE]", zoom+"");

        //base html for add in additional pins
        String pinhtml = "";
        int pinIndex = 0;

        //loop through each coord on list and add pin to map for coord
        for(int i = 0; i < coords.size(); ++i) {
            //add in pin and coord data to html
            pinhtml += "\npushpinOptions = {visible: true, icon:'" + pins[pinIndex] + "', width: 30, height: 50, textOffset: offset};\n";
            pinhtml += "pushpin = new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(" + coords.get(i)[0] + "," + coords.get(i)[1] + "), pushpinOptions);\n";
            pinhtml += "map.entities.push(pushpin);\n";

            //check if more than one pin and continue through pin array
            if (pins.length > 1) {
                pinIndex++;

                //if past current amount of pins loop back to first pin
                if (pinIndex >= pins.length) {
                    pinIndex = 0;
                }
            }
        }

        //swap in pin html here
        htmlString = htmlString.replace("[BUILD_OUT_PINS_HERE]", pinhtml);

        //write html string to file
        WriteStringToFile(htmlString, fileDirectory);
    }

    private static String GetBaseHTMLFile() {
        //load in base html file
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File htmlFile = new File(classLoader.getResource("bingMapHtml.html").getFile());

        //try to pull in file data otherwise return empty string
        try {
            BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void WriteStringToFile(String fileData, String fileLocation) {
        //get file name from provided save location and make directory if one doesnt exist
        String fileName = fileLocation.split("/")[fileLocation.split("/").length - 1];
        String directory = fileLocation.replace(fileName, "");
        (new File(directory)).mkdirs();

        //try to write file and close when complete. if fails then send message saying saving failed.
        try {
            File file = new File(fileLocation);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileData);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Failed to write string to file");
            e.printStackTrace();
        }

    }
}
