package irc.cpe.cozy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;

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
        // Make service bound if necessary
        if (toggle.isChecked()) ServiceManager.getService(getApplicationContext());
    }

}
