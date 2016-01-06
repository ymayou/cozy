package irc.cpe.cozy.Rest;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    Response post(String url, String json, String user, String password) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("{Content-Type", "application/json}")
                .addHeader("Accept", "*/*");
        if (password != null && password.length() > 0) {
            String credential = Credentials.basic(user, password);
            builder.header("Authorization", credential);
        }
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        return response;
    }

    Response get(String url, String user, String password) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("{Content-Type", "application/json}")
                .addHeader("Accept", "*/*");
        if (password != null && password.length() > 0) {
            String credential = Credentials.basic(user, password);
            builder.header("Authorization", credential);
        }
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        return response;
    }


    Response delete(String url, String user, String password) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("{Content-Type", "application/json}")
                .addHeader("Accept", "*/*")
                .delete();
        if (password != null && password.length() > 0) {
            String credential = Credentials.basic(user, password);
            builder.header("Authorization", credential);
        }
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
