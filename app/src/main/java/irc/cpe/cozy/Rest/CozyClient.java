package irc.cpe.cozy.Rest;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class CozyClient {

    public String addDevice(Context c, String deviceId, String password) {
        String result = null;
        RestClient client = new RestClient();
        try {
            result = client.post("https://gustiaux.cozycloud.cc/device",
                    "{\"login\":\"" + deviceId + "\", \"permissions\": {\"File\": {\"description\": \"Synchronize files\"}} }",
                    "owner",
                    password);

            JSONObject jsonObject = new JSONObject(result);
            Iterator<?> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (!(jsonObject.get(key) instanceof JSONObject)) {
                    System.out.println(key);
                    System.out.println(jsonObject.getString(key));
                    if (key.equals("error") && jsonObject.getString(key).equals("This name is already used")) {
                        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
                        result = settings.getString("cozy_device_password", null);
                    } else if (key.equals("password")) {
                        result = jsonObject.getString(key);
                        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("cozy_device_password", result);
                        editor.apply();
                        break;
                    } else {
                        result = null;
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean addDocument() {
        return false;
    }

    public boolean deleteDocument() {
        return false;
    }

    public boolean updateDocument() {
        return false;
    }
}
