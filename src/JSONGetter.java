import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class JSONGetter {
    public static JSONObject GetJsonString(String urlText){
        try {
            URL url = new URL(urlText);

            Scanner scan = new Scanner(url.openStream());

            String jsonString = new String();
            while (scan.hasNext())
                jsonString += scan.nextLine();
            scan.close();

            if(jsonString.charAt(0) == '[' && jsonString.charAt(jsonString.length() - 1) == ']')
                jsonString = "{jsonObject:" + jsonString + "}";

            return new JSONObject(jsonString);
        } catch (MalformedURLException e){
            return new JSONObject();//send empty jsonObject
        } catch (IOException e){
            return new JSONObject();//send empty jsonObject
        }
    }
}
