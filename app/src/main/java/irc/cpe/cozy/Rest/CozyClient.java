package irc.cpe.cozy.Rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Response;

public class CozyClient {

    private Context c;

    public CozyClient(Context context) {
        c = context;
    }

    /**
     * Add the device to CozyCloud API and save device password in settings
     *
     * @return true if the device is authorized to use the API
     */
    public boolean addDevice() {
        // TODO : replace password (using app settings)
        String password = "teddygustiaux";
        String deviceId = Settings.Secure.getString(c.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        boolean result = false;
        RestClient client = new RestClient();
        try {
            // TODO : replace URL (using app settings)
            String response = client.post("https://gustiaux.cozycloud.cc/device",
                    "{\"login\":\"" + deviceId + "\", \"permissions\": {\"File\": {\"description\": \"Synchronize files\"}} }",
                    "owner",
                    password).body().string();

            JSONObject jsonObject = new JSONObject(response);
            Iterator<?> keys = jsonObject.keys();

            SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("android_device_id", deviceId);
            editor.apply();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (!(jsonObject.get(key) instanceof JSONObject)) {
                    System.out.println("[DEBUG] Cozy API response");
                    System.out.println("[DEBUG] Key: " + key);
                    System.out.println("[DEBUG] Value: " + jsonObject.getString(key));

                    // If the device has already been added
                    if (key.equals("error") && jsonObject.getString(key).equals("This name is already used")) {
                        result = true;
                        break;
                    } else if (key.equals("password")) {
                        // Device added
                        result = true;
                        editor.putString("cozy_device_password", jsonObject.getString(key));
                        editor.apply();
                        break;
                    } else {
                        result = false;
                    }
                }
            }
            System.out.println("[DEBUG] Cozy device ID: " + settings.getString("android_device_id", null));
            System.out.println("[DEBUG] Cozy device passord: " + settings.getString("cozy_device_password", null));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Create a new document
     * @param document document body
     * @return the document ID
     */
    public String createDocument(String document) {
        String result = null;
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_device_password", null);
        String username = settings.getString("android_device_id", null);
        try {
            // TODO : replace URL (using app settings)
            Response response = client.post("https://gustiaux.cozycloud.cc/ds-api/data/",
                    document,
                    username,
                    password);
            System.out.println("[DEBUG] Cozy API response");
            if (response.code() == 201) {
                System.out.println("[DEBUG] Document created");
                String body = response.body().string();
                JSONObject jsonObject = new JSONObject(body);
                result = jsonObject.getString("_id");
            } else {
                System.out.println("[DEBUG] " + response.body().string());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getDocument(String id) {
        String result = null;
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_device_password", null);
        String username = settings.getString("android_device_id", null);
        try {
            // TODO : replace URL (using app settings)
            Response response = client.get("https://gustiaux.cozycloud.cc/ds-api/data/" + id + "/",
                    username,
                    password);
            System.out.println("[DEBUG] Cozy API response");
            if (response.code() == 200) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean deleteDocument(String id) {
        boolean result = false;
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_device_password", null);
        String username = settings.getString("android_device_id", null);
        try {
            // TODO : replace URL (using app settings)
            Response response = client.delete("https://gustiaux.cozycloud.cc/ds-api/data/" + id + "/",
                    username,
                    password);
            System.out.println("[DEBUG] Cozy API response");
            if (response.code() == 204) {
                result = true;
            } else {
                System.out.println("[DEBUG] " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean updateDocument(String documentId, String document) {
        boolean result = false;
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_device_password", null);
        String username = settings.getString("android_device_id", null);
        try {
            // TODO : replace URL (using app settings)
            // TODO : use other API function in case of no ID (error during creation)
            Response response = client.put("https://gustiaux.cozycloud.cc/ds-api/data/" + documentId + "/",
                    document,
                    username,
                    password);
            System.out.println("[DEBUG] Cozy API response");
            if (response.code() == 200) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void removeDevice() {
        String password = "teddygustiaux";
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String device = settings.getString("android_device_id", null);
        try {
            // TODO : replace URL (using app settings)
            client.delete("https://gustiaux.cozycloud.cc/device/" + device,
                    "owner",
                    password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
