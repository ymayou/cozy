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

    public void callCozy(WebserviceListener lt) {
        if (isConnectedToInternet(context)) {
            CozyClient cozyClient = new CozyClient(context);
            boolean success = cozyClient.addDevice();
            if (success) {
                System.out.println("[DEBUG] Device added successfully");

                String data = null;
                String documentId = cozyClient.createDocument("{\"text\": \"This is my document!\"}");

                String document = cozyClient.getDocument(documentId);
                lt.notesChanged(document);

                boolean update = cozyClient.updateDocument(documentId, "{\"text\": \"This is my updated document!\"}");
                if (update) {
                    System.out.println("[DEBUG] Document updated successfully");
                } else {
                    System.out.println("[DEBUG] Document NOT updated successfully");
                }

                document = cozyClient.getDocument(documentId);
                lt.notesChanged(document);

                boolean deletion = cozyClient.deleteDocument(documentId);
                if (deletion) {
                    System.out.println("[DEBUG] Document deleted successfully");
                } else {
                    System.out.println("[DEBUG] Document NOT deleted successfully");
                }

                cozyClient.removeDevice();
                System.out.println("[DEBUG] Device removed successfully");
            }
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
