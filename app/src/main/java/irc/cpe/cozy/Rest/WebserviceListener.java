package irc.cpe.cozy.Rest;

import java.util.EventListener;

public abstract class WebserviceListener implements EventListener {

    public abstract void notesChanged(String data);
}
