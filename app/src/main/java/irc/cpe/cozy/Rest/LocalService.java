package irc.cpe.cozy.Rest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import irc.cpe.cozy.R;

public class LocalService extends Service {
    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            System.out.println("[DEBUG] Service Binder Called");
            return LocalService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Toast.makeText(getApplicationContext(), "Service created", Toast.LENGTH_SHORT).show();
        System.out.println("[DEBUG] Service created");
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that   receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent;
        contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LocalServiceActivities.Controller.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_account_circle_black)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.local_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    public void test() {
        Toast.makeText(getApplicationContext(), "Test called", Toast.LENGTH_SHORT).show();
    }

    private CozyClient getClient(Context context) {
        CozyManager cozyManager = CozyManager.getInstance(context);
        if (cozyManager.isConnectedToInternet()) {
            CozyClient cozyClient = cozyManager.cozyClient;
            return cozyClient;
        } else {
            return null;
        }
    }

    public String saveDocument(final Object object, final Context context, final String id) {
        final String[] result = {null};
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    String json = ow.writeValueAsString(object);
                    CozyClient cozyClient = getClient(context);
                    if (cozyClient != null) {
                        if (id == null) {
                            result[0] = cozyClient.createDocument(json);
                        } else {
                            if (cozyClient.updateDocument(id, json)) {
                                result[0] = id;
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        );
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[DEBUG] Document ID: " + result[0]);
        return result[0];
    }

    public boolean deleteDocument(final Context context, final String id) {
        final boolean[] result = {false};
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                CozyClient cozyClient = getClient(context);
                System.out.println("[DEBUG] Document ID: " + id);
                if (id != null && cozyClient != null) {
                    result[0] = cozyClient.deleteDocument(id);
                }
            }
        }
        );
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[DEBUG] Document deletion: " + result[0]);
        return result[0];
    }
}