package irc.cpe.cozy.Rest;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.Runnable;

import irc.cpe.cozy.R;

public class WebserviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        final WebserviceListener lt = new WebserviceListener(){
            @Override
            public void notesChanged(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run(){
                        TextView editText = (TextView) findViewById(R.id.textView);
                        editText.setText("OK");
                        System.out.println(data);
                        //JSONArray jsonArray = new JSONArray(data);
                        //System.out.println(jsonArray.length());
                    }
                });
            }
        };

        new Thread(new Runnable(){
            @Override
            public void run() {
                NetworkManager.getInstance(WebserviceActivity.this).callCozy(lt);
            }
        }
        ).start();
    }
}
