package ebsi.net.jda.listeners;

import ebsi.core.Handlers;
import ebsi.util.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JDAMessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor() == event.getJDA().getSelfUser()) return;
        Log.get(this).info("[{} > #{}] {}: {}",
                event.getGuild().getName(),
                event.getChannel().getName(),
                event.getMember().getEffectiveName(),
                event.getMessage().getContentStripped()
        );
        Handlers.accept(event);
    }
}
