package ebsi.struct.controllers;

import ebsi.struct.Handler;
import net.dv8tion.jda.api.events.Event;

import java.util.Set;

public abstract class Controller<T extends Event> implements Handler<T> {
    /**
     * Gets the tags that this handler is associated with.
     * @return A set of all tags that this handler should respond to.
     */
    public abstract Set<String> getTags();

    /**
     * Gets the primary tag associated with this handler.
     * @return The primary tag of this handler. The primary tag must equal one of the tags contained in {@code getTags()}.
     */
    public abstract String getPrimaryTag();
}
