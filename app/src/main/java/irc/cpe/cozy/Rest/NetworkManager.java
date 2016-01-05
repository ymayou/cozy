package irc.cpe.cozy.Rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {

    private static NetworkManager manager = null;
    private Context context = null;

    private NetworkManager(Context context) {
        this.context = context;
    }

    public static NetworkManager getInstance(Context context) {
        if (manager == null)
            manager = new NetworkManager(context);
        return manager;
    }

    public void callCozy(WebserviceListener lt, String deviceId) {
        if (isConnectedToInternet(context)) {
            CozyClient cozyClient = new CozyClient();
            String password = cozyClient.addDevice(context, deviceId, "teddygustiaux");
            lt.notesChanged(password);
        }
    }

    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            NetworkInfo.State networkState = networkInfo.getState();
            if (networkState.equals(NetworkInfo.State.CONNECTED)) {
                return true;
            }
        }
        return false;
    }
}
