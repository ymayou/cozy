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

    /**
     * Make a POST request
     *
     * @param url      URL to call
     * @param json     JSON body of the request
     * @param user     username for authentication
     * @param password password for authentication
     * @return Response to the request
     * @throws IOException
     */
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
        return client.newCall(request).execute();
    }

    /**
     * Make a GET request
     *
     * @param url      URL to call
     * @param user     username for authentication
     * @param password password for authentication
     * @return Response to the request
     * @throws IOException
     */
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
        return client.newCall(request).execute();
    }

    /**
     * Make a DELETE request
     *
     * @param url      URL to call
     * @param user     username for authentication
     * @param password password for authentication
     * @return Response to the request
     * @throws IOException
     */
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
        return client.newCall(request).execute();
    }

    /**
     * Make a PUT request
     *
     * @param url      URL to call
     * @param json     JSON body of the request
     * @param user     username for authentication
     * @param password password for authentication
     * @return Response to the request
     * @throws IOException
     */
    Response put(String url, String json, String user, String password) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("{Content-Type", "application/json}")
                .addHeader("Accept", "*/*")
                .put(body);
        if (password != null && password.length() > 0) {
            String credential = Credentials.basic(user, password);
            builder.header("Authorization", credential);
        }
        Request request = builder.build();
        return client.newCall(request).execute();
    }
}
