package Tools;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.JSONParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.*;
import com.google.gson.Gson;


public class JsonDataReader {
	public static List<Object[]> readUserData(String filePath) throws IOException {
        
        List<Object[]> userDataList = new ArrayList<>();
        

        JsonParser parser = new JsonParser();
        // Lee el archivo JSON
        //JsonObject jsonObject = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
        //JsonObject jsonObject = parser.parse(new FileReader(filePath)).getAsJsonObject());
        JsonObject jsonObject = parser.parse(new FileReader(filePath)).getAsJsonObject();
       
        JsonArray jsonArray = jsonObject.getAsJsonArray("dataElement22");

        // Itera sobre los objetos JSON y agrega cada usuario a la lista
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject userObject = jsonArray.get(i).getAsJsonObject();
            String DE22 = userObject.get("DE22").getAsString();
            //String password = userObject.get("password").getAsString();
            userDataList.add(new Object[]{DE22});
        }

        return userDataList;
    }
public static List<Object[]> readUserData2(String filePath) throws IOException {
        
        List<Object[]> userDataList = new ArrayList<>();
        

        JsonParser parser = new JsonParser();
        // Lee el archivo JSON
        //JsonObject jsonObject = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
        //JsonObject jsonObject = parser.parse(new FileReader(filePath)).getAsJsonObject());
        JsonObject jsonObject = parser.parse(new FileReader(filePath)).getAsJsonObject();
       
        JsonArray jsonArray = jsonObject.getAsJsonArray("ambiente");

        // Itera sobre los objetos JSON y agrega cada usuario a la lista
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject userObject = jsonArray.get(i).getAsJsonObject();
            String DE22 = userObject.get("DE22").getAsString();
            //String password = userObject.get("password").getAsString();
            userDataList.add(new Object[]{DE22});
        }

        return userDataList;
    }

}
