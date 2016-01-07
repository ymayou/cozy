package irc.cpe.cozy.Rest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class LocalServiceActivities {
    /**
     * <p>Example of explicitly starting and stopping the local service.
     * This demonstrates the implementation of a service that runs in the same
     * process as the rest of the application, which is explicitly started and stopped
     * as desired.</p>
     * <p/>
     * <p>Note that this is implemented as an inner class only keep the sample
     * all together; typically this code would appear in some separate class.
     */
    public static class Controller extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            System.out.println("[DEBUG] Controller called");
            Toast.makeText(getApplicationContext(), "Controller called", Toast.LENGTH_SHORT).show();
        }
    }
}