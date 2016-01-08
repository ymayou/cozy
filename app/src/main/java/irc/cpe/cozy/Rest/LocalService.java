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
    private int NOTIFICATION = R.string.local_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            System.out.println("[DEBUG] Service binder called");
            return LocalService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        System.out.println("[DEBUG] Service created");
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mNM.cancel(NOTIFICATION);
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text = getText(R.string.local_service_started);
        PendingIntent contentIntent;
        contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LocalServiceActivities.Controller.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_logo)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.local_service_label))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .build();

        mNM.notify(NOTIFICATION, notification);
    }

    /**
     * Get an instance of a CozyClient
     *
     * @param context application context
     * @return CozyClient instance
     */
    private CozyClient getClient(Context context) {
        CozyManager cozyManager = CozyManager.getInstance(context);
        if (cozyManager.isConnectedToInternet()) {
            return cozyManager.cozyClient;
        } else {
            return null;
        }
    }

    /**
     * Save a document in CozyCloud
     *
     * @param object  object to save
     * @param context application context
     * @param id      optional ID needed for update
     * @return the ID of the document
     */
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

    /**
     * Delete a document from CozyCloud
     *
     * @param context application context
     * @param id      optional ID needed for update
     * @return the ID of the document
     */
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