import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class JSONGetter {
    public static JSONObject GetJsonString(String urlText){
        try {
            URL url = new URL(urlText);//set url test and grab url

            Scanner scan = new Scanner(url.openStream());//open url stream

            String jsonString = new String();//string to hold json text

            //loop through the stream and push to json string
            while (scan.hasNext())
                jsonString += scan.nextLine();

            scan.close();//close scan connection

            //check if json array and convert to a json object
            if(jsonString.charAt(0) == '[' && jsonString.charAt(jsonString.length() - 1) == ']')
                jsonString = "{jsonObject:" + jsonString + "}";

            return new JSONObject(jsonString);//return json object
        } catch (MalformedURLException e){
            return new JSONObject();//send empty jsonObject
        } catch (IOException e){
            return new JSONObject();//send empty jsonObject
        }
    }
}
