package irc.cpe.cozy.Rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class NetworkManager {

    private static NetworkManager manager = null;
    private Context context = null;

    private String consumer_key="";
    private String mySecretKey="";
    private String oauth_token_secret = "";
    private String token = "";

    private NetworkManager(Context context){
        this.context=context;
    }

    public static NetworkManager getInstance(Context context){
        if(manager==null)
            manager = new NetworkManager(context);
        return manager;
    }

    private String makeRequest(String httpMethod, String baseUrl, String urlExtra, String message){
        String header = null;
        String result = null;
        URL url;
        try {
            String urlString = baseUrl;
            if (urlExtra != null && urlExtra.length() > 0) urlString += "?" + urlExtra;
            url = new URL(urlString);
            HttpsURLConnection c;
            c = (HttpsURLConnection) url.openConnection();
            if (httpMethod.equals("GET")) {
                c.setRequestProperty("Authorization", header);
                c.setRequestMethod("GET");
                c.setDoInput(true);
                c.setRequestProperty("Accept", "*/*");
                InputStream istream = c.getInputStream();
                InputStreamReader isr = new InputStreamReader(istream);
                BufferedReader br = new BufferedReader(isr);
                result = br.readLine();
                istream.close();
            } else if (httpMethod.equals("POST")) {
                header = "Basic " + Base64.encodeToString(
                        ("owner" + ":" + "teddygustiaux").getBytes(),
                        Base64.NO_WRAP);
                c.setRequestProperty("Authorization", header);
                c.setRequestProperty("Content-Type", "application/json");
                c.setRequestProperty("Accept", "*/*");
                c.setRequestMethod("POST");
                c.setDoOutput(true);
                OutputStream outputStream = c.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputStream);
                String data = message;
                dos.writeBytes(data);
                dos.flush();
                outputStream.close();
                InputStream istream = c.getInputStream();
                InputStreamReader isr = new InputStreamReader(istream);
                BufferedReader br = new BufferedReader(isr);
                result = br.readLine();
                istream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void callCozy(WebserviceListener lt, String deviceId) {
        if (isConnectedToInternet(context)) {
            CozyClient cozyClient = new CozyClient();
            //String data = makeRequest("POST", "https://gustiaux.cozycloud.cc/device", null, "{\"login\":\"cozy-android-notes-cpe\", \"permissions\": {\"File\": {\"description\": \"Synchronize files\"}} }");
            String data = cozyClient.addDevice(deviceId, "teddygustiaux");
            System.out.println(data);
            lt.notesChanged(data);
        }
    }

    private boolean isConnectedToInternet(Context context)
    {
        //verify the connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            NetworkInfo.State networkState = networkInfo.getState();
            if (networkState.equals(NetworkInfo.State.CONNECTED))
            {
                return true;
            }
        }
        return false;
    }
}
