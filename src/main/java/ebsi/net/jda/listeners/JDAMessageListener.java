package ebsi.net.jda.listeners;

import ebsi.core.Handlers;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDAMessageListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(JDAMessageListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor() == event.getJDA().getSelfUser()) return;
        logger.info("[{} > #{}] {}: {}",
                event.getGuild().getName(),
                event.getChannel().getName(),
                event.getMember().getEffectiveName(),
                event.getMessage().getContentDisplay()
        );
        Handlers.accept(event);
    }
}
