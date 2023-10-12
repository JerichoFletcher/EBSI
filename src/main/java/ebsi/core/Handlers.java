package ebsi.core;

import ebsi.controllers.PingController;
import ebsi.struct.handlers.Handler;
import ebsi.struct.handlers.MessageHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Handlers {
    private static final Logger logger = LoggerFactory.getLogger(Handlers.class);

    private static final String PREFIX = "!";
    private static final List<Handler<? extends Event>> handlers = new ArrayList<>();
    static {
        handlers.add(new PingController());
    }

    public static void accept(Event event) {
        if (event instanceof MessageReceivedEvent msgEvent) {
            acceptMessage(msgEvent);
            return;
        }
    }

    private static void acceptMessage(MessageReceivedEvent event) {
        String[] args = chop(event.getMessage());
        if (args == null) {
            return;
        }

        String name = args[0].toLowerCase();
        for (Handler<? extends Event> handler : handlers) {
            if (handler instanceof MessageHandler msgHandler && msgHandler.getNames().contains(name)) {
                logger.info("Sending event '{}' to handler '{}'", event.getClass().getSimpleName(), msgHandler.getClass().getSimpleName());
                msgHandler.handle(event, Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }

    private static String[] chop(Message message) {
        String content = message.getContentDisplay().trim().strip();
        if (!content.startsWith(PREFIX)) return null;
        content = content.substring(1).trim().strip();

        return content.split("\\s+");
    }
}
