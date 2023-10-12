package ebsi.struct.controllers;

import ebsi.struct.Handler;
import net.dv8tion.jda.api.events.Event;

import java.util.Set;

public abstract class Controller<T extends Event> implements Handler<T> {
    public abstract Set<String> getTags();

    public String getPrimaryTag() {
        return getTags().stream().findFirst().orElseThrow();
    }
}
