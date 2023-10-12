package ebsi.struct.handlers;

import net.dv8tion.jda.api.events.Event;

import java.util.Set;

public interface Handler<T extends Event> {
    Set<String> getNames();
    <T1 extends T> void handle(T1 event, String[] eventArgs);
}
