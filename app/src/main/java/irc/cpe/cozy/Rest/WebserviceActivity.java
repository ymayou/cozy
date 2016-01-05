package irc.cpe.cozy.Rest;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Runnable;
import java.util.Properties;

import irc.cpe.cozy.R;

public class WebserviceActivity extends AppCompatActivity {

    public String getDeviceId() {
        return Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        final WebserviceListener lt = new WebserviceListener() {
            @Override
            public void notesChanged(final String password) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(password);
                    }
                });
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkManager.getInstance(WebserviceActivity.this).callCozy(lt, getDeviceId());
            }
        }
        ).start();
    }
}
