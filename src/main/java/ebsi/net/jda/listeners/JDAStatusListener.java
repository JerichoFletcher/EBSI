package ebsi.net.jda.listeners;

import ebsi.core.Env;
import ebsi.util.Log;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class JDAStatusListener extends ListenerAdapter {
    @Override
    public void onStatusChange(StatusChangeEvent event) {
        Log.get(this).info("Status is {}", event.getNewStatus());
    }

    @Override
    public void onGenericSession(GenericSessionEvent event) {
        Log.get(this).info("Session is {}", event.getState());
    }

    @Override
    public void onReady(ReadyEvent event) {
        LocalDateTime onlineStartTime = LocalDateTime.now();
        Log.get(this).info("Setting online start time as {}", onlineStartTime);
        Env.onlineStartTime = onlineStartTime;
    }
}
