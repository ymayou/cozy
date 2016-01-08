package irc.cpe.cozy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import irc.cpe.cozy.Rest.ServiceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Switch toggle = (Switch) findViewById(R.id.syncToggle);
        SharedPreferences settings = this.getApplicationContext().getSharedPreferences("UserInfo", 0);
        toggle.setChecked(settings.getBoolean("cozy_automatic_sync", false));
    }

    public void toggleSync(View view) {
        Switch toggle = (Switch) findViewById(R.id.syncToggle);
        SharedPreferences settings = view.getContext().getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("cozy_automatic_sync", toggle.isChecked());
        editor.apply();
        if (settings.getString("cozy_url", null) == null) {
            Toast.makeText(getApplicationContext(), "Your device has to be linked with Cozy first", Toast.LENGTH_SHORT).show();
            toggle.setChecked(false);
        } else {
            // Make service bound if necessary
            if (toggle.isChecked()) ServiceManager.getService(getApplicationContext());
        }
    }

}
