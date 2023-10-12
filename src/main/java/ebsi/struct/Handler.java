package ebsi.struct;

import net.dv8tion.jda.api.events.Event;

public interface Handler<T extends Event> {
    <T1 extends T> void handle(T1 event, String[] eventArgs);
}
