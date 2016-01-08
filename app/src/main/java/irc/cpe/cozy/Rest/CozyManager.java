package irc.cpe.cozy.Rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CozyManager {

    private static CozyManager manager = null;
    private Context context = null;
    public CozyClient cozyClient = null;

    private CozyManager(Context context) {
        this.cozyClient = new CozyClient(context);
        this.context = context;
    }

    /**
     * Get an instance of a CozyManager
     *
     * @param context application context
     * @return instance of a CozyManager
     */
    public static CozyManager getInstance(Context context) {
        if (manager == null)
            manager = new CozyManager(context);
        return manager;
    }

    /**
     * Check if the app is connected to the Internet
     *
     * @return true if connected
     */
    public boolean isConnectedToInternet() {
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
