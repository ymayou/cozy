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
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_account_password", null);
        String url = settings.getString("cozy_url", "").replaceAll("/$", "");
        String deviceId = Settings.Secure.getString(c.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        boolean result = false;
        RestClient client = new RestClient();
        try {
            String response = client.post(url + "/device",
                    "{\"login\":\"" + deviceId + "\", \"permissions\": {\"File\": {\"description\": \"Synchronize files\"}} }",
                    "owner",
                    password).body().string();

            JSONObject jsonObject = new JSONObject(response);
            Iterator<?> keys = jsonObject.keys();

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
            System.out.println("[DEBUG] Cozy device password: " + settings.getString("cozy_device_password", null));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Create a new document in CozyCloud API
     *
     * @param document document body
     * @return the document ID
     */
    public String createDocument(String document) {
        String result = null;
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_device_password", null);
        String username = settings.getString("android_device_id", null);
        String url = settings.getString("cozy_url", "").replaceAll("/$", "");
        try {
            Response response = client.post(url + "/ds-api/data/",
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

    /**
     * Retrieve a document from CozyCloud API
     *
     * @param id document ID
     * @return the document as a JSON string
     */
    public String getDocument(String id) {
        String result = null;
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_device_password", null);
        String username = settings.getString("android_device_id", null);
        String url = settings.getString("cozy_url", "").replaceAll("/$", "");
        try {
            Response response = client.get(url + "/ds-api/data/" + id + "/",
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

    /**
     * Delete a document from CozyCloud API
     *
     * @param id document ID
     * @return true if deleted
     */
    public boolean deleteDocument(String id) {
        boolean result = false;
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_device_password", null);
        String username = settings.getString("android_device_id", null);
        String url = settings.getString("cozy_url", "").replaceAll("/$", "");
        try {
            Response response = client.delete(url + "/ds-api/data/" + id + "/",
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

    /**
     * Update a document from CozyCloud API
     *
     * @param documentId document ID
     * @param document   document body
     * @return true if updated
     */
    public boolean updateDocument(String documentId, String document) {
        boolean result = false;
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String password = settings.getString("cozy_device_password", null);
        String username = settings.getString("android_device_id", null);
        String url = settings.getString("cozy_url", "").replaceAll("/$", "");
        try {
            Response response = client.put(url + "/ds-api/upsert/data/" + documentId + "/",
                    document,
                    username,
                    password);
            System.out.println("[DEBUG] Cozy API response");
            if (response.code() == 200 || response.code() == 201) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // TODO : appeler à la fermeture ?
    /**
     * Remove the device from CozyCloud API
     */
    public void removeDevice() {
        RestClient client = new RestClient();
        SharedPreferences settings = c.getSharedPreferences("UserInfo", 0);
        String device = settings.getString("android_device_id", null);
        String url = settings.getString("cozy_url", "").replaceAll("/$", "");
        String password = settings.getString("cozy_account_password", null);
        try {
            client.delete(url + "/device/" + device,
                    "owner",
                    password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
