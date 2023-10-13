package ebsi.core;

import ebsi.commands.HelpCommand;
import ebsi.commands.PingCommand;
import ebsi.commands.ThrowDiceCommand;
import ebsi.controllers.RandomInterceptController;
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

    /**
     * Gets all registered handlers.
     * @return A list of all registered handlers, including commands.
     */
    public static List<Handler<? extends Event>> getHandlers() {
        return handlers;
    }

    /**
     * Initializes event handlers.
     */
    public static void init() {
        if (!handlers.isEmpty()) throw new IllegalStateException("Handlers already initialized");

        // Controllers
        register(new RandomInterceptController());

        // Commands
        register(new HelpCommand());
        register(new PingCommand());
        register(new ThrowDiceCommand());
    }

    /**
     * Gets all registered commands.
     * @return A list of all registered commands.
     */
    public static List<Command> getCommands() {
        return handlers.stream()
                .filter(handler -> handler instanceof Command)
                .collect(ArrayList::new, (list, handler) -> list.add((Command)handler), ArrayList::addAll);
    }

    /**
     * Registers a handler to the bot.
     * @param handler A handler to register.
     * @return {@code true} if the handler is added to the registry; {@code false} if the handler is already registered.
     */
    public static boolean register(Handler<? extends Event> handler) {
        if (handlers.contains(handler)) return false;
        handlers.add(handler);
        Log.get(Handlers.class).info("Registered handler '{}'", handler.getClass().getSimpleName());
        return true;
    }

    public static void accept(Event event) {
        if (event instanceof MessageReceivedEvent msgEvent) {
            acceptMessage(msgEvent);
            return;
        }
    }

    private static void acceptMessage(MessageReceivedEvent event) {
        String[] args = chop(event.getMessage());
        String name = args == null ? null : args[0].toLowerCase();

        for (Handler<? extends Event> handler : handlers) {
            if (handler instanceof MessageController controller && (controller.getTags() == null || controller.getTags().contains(name))) {
                Log.get(Handler.class).info("Sending event '{}' to handler '{}'", event.getClass().getSimpleName(), controller.getClass().getSimpleName());
                controller.handle(event, args == null ? null : Arrays.copyOfRange(args, 1, args.length));
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
