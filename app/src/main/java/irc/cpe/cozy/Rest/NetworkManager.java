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

    private String buildOAuthHeader(String httpMeth, String baseUrl, String headerExtra, String extraUrl, String requestStr) {

        Map<String, String> parameters = new HashMap<>();
        if(requestStr!=null && !requestStr.isEmpty() && requestStr.contains("=")) {
            parameters.putAll(parseExtraUrl(requestStr));
        }
        parameters.putAll(parseExtraUrl(extraUrl));
        parameters.put("oauth_consumer_key",consumer_key);
        String nonce = generate_nonce();
        parameters.put("oauth_nonce",nonce);
        parameters.put("oauth_signature_method", "HMAC-SHA1");
        String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis()/1000);
        parameters.put("oauth_timestamp", timestamp);
        if(headerExtra != null && !headerExtra.isEmpty())
            parameters.putAll(parseExtraUrl(headerExtra.replace("\"", "")));
        if(token != null)
            parameters.put("oauth_token", token);
        parameters.put("oauth_version", "1.0");

        String header = "OAuth ";
        if(headerExtra != null && !headerExtra.isEmpty())
            header += headerExtra+",";
        header +="oauth_consumer_key=\""+consumer_key+"\",";
        header +="oauth_nonce=\""+nonce+"\",";
        header +="oauth_signature=\""+generate_signature(parameters, httpMeth, baseUrl)+"\",";
        header +="oauth_signature_method=\""+"HMAC-SHA1"+"\",";
        header +="oauth_timestamp=\""+timestamp+"\",";
        if(token != null)
            header +="oauth_token=\""+token+"\",";
        header +="oauth_version=\""+"1.0"+"\"";


        return header;
    }

    private String generate_nonce()
    {
        String nonce = "";
        Random rand = new Random(Calendar.getInstance().getTimeInMillis());
        nonce+=String.valueOf(Calendar.getInstance().getTimeInMillis());
        nonce+=String.valueOf(rand.nextInt());
        nonce = String.format("%04x", new BigInteger(1, nonce.getBytes()));
        return nonce.substring(0,32);
    }

    private String generate_signature(Map<String,String> parameters, String httpMethod, String baseUrl) {

        String outputString = "";
        String parametersString="";
        List<String> keys = new ArrayList<>(parameters.keySet());
        Collections.sort(keys);
        for(String key : keys)
        {
            parametersString += key;
            parametersString += "=";
            try {
                parametersString += URLEncoder.encode(parameters.get(key), "UTF-8").replace("+","%20");
            }catch(Exception e){
                e.printStackTrace();
            }
            parametersString +="&";
        }
        parametersString = parametersString.substring(0,parametersString.lastIndexOf("&"));

        String signatureBaseString = httpMethod.toUpperCase();
        try {
            signatureBaseString += "&";
            signatureBaseString += URLEncoder.encode(baseUrl, "UTF-8");
            signatureBaseString += "&";
            signatureBaseString += URLEncoder.encode(parametersString,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }


        String signingKey = mySecretKey;
        signingKey += "&";
        if(oauth_token_secret != null)
            signingKey += oauth_token_secret;
        try {
            SecretKeySpec key = new SecretKeySpec(signingKey.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(key);
            byte[] data = mac.doFinal(signatureBaseString.getBytes("UTF-8"));
            outputString = Base64.encodeToString(data,Base64.DEFAULT);
            outputString = outputString.substring(0, outputString.length() - 1);
            outputString = URLEncoder.encode(outputString,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        return outputString;

    }

    private Map<String,String> parseExtraUrl(String extraUrl){
        Map<String, String> extras = new HashMap<>();
        boolean next;
        if(extraUrl == null)
            next = false;
        else
            next =  extraUrl.contains("=");

        while(next)
        {
            String key = extraUrl.substring(0,extraUrl.indexOf("="));
            extraUrl = extraUrl.substring(extraUrl.indexOf("=")+1);
            String val;
            if(extraUrl.contains("&"))
                val = extraUrl.substring(0,extraUrl.indexOf("&")-1);
            else {
                val = extraUrl;
                next = false;
            }
            extras.put(key,val);

        }
        return extras;
    }

    private String makeRequest(String httpMethod, String baseUrl, String urlExtra, String message){
        String header = buildOAuthHeader(httpMethod, baseUrl, null, urlExtra, message);
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

    public void callCozy(WebserviceListener lt){
        if (isConnectedToInternet(context)) {
            String data = makeRequest("POST", "https://gustiaux.cozycloud.cc/device", null, "{\"login\":\"cozy-android-notes-cpe\", \"permissions\": {\"File\": {\"description\": \"Synchronize files\"}} }");
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
