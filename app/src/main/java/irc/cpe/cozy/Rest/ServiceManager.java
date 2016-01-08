package irc.cpe.cozy.Rest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import irc.cpe.cozy.R;

public class ServiceManager {

    private static ServiceManager manager = null;
    private static Context context = null;
    private static boolean mIsBound;
    private static LocalService mBoundService;

    private ServiceManager(Context context) {
        ServiceManager.context = context;
    }

    public static LocalService getService(Context context) {
        if (manager == null)
            manager = new ServiceManager(context);
        if (!mIsBound) {
            doBindService();
        }
        return mBoundService;
    }

    private static ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((LocalService.LocalBinder)service).getService();
            Toast.makeText(context, R.string.local_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
            Toast.makeText(context, R.string.local_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    private static void doBindService() {
        Intent i = new Intent(context, LocalService.class);
        context.bindService(i, mConnection, Context.BIND_AUTO_CREATE);
        context.startService(i);
        mIsBound = true;
    }

    private static void doUnbindService() {
        if (mIsBound) {
            context.unbindService(mConnection);
            mIsBound = false;
        }
    }

    public static void destroy() {
        doUnbindService();
    }

}
