package ebsi.core;

import ebsi.commands.HelpCommand;
import ebsi.commands.PingCommand;
import ebsi.struct.Command;
import ebsi.struct.controllers.MessageController;
import ebsi.struct.Handler;
import ebsi.util.Log;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Handlers {
    private static final List<Handler<? extends Event>> handlers = new ArrayList<>();
    static {
        handlers.add(new HelpCommand());
        handlers.add(new PingCommand());
    }

    public static List<Handler<? extends Event>> getHandlers() {
        return handlers;
    }

    public static List<Command> getCommands() {
        return handlers.stream()
                .filter(handler -> handler instanceof Command)
                .collect(ArrayList::new, (list, handler) -> list.add((Command)handler), ArrayList::addAll);
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
            if (handler instanceof MessageController controller && controller.getTags().contains(name)) {
                Log.get(Handler.class).info("Sending event '{}' to handler '{}'", event.getClass().getSimpleName(), controller.getClass().getSimpleName());
                controller.handle(event, Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }

    private static String[] chop(Message message) {
        String content = message.getContentDisplay().trim().strip();
        if (!content.startsWith(Env.PREFIX)) return null;
        content = content.substring(1).trim().strip();

        return content.split("\\s+");
    }
}
