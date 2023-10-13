package ebsi.controllers;

import ebsi.net.jda.JDAService;
import ebsi.struct.controllers.MessageController;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class RandomInterceptController extends MessageController {
    private static final String[] replies = new String[]{
            "turu",
            "salmanis",
            "besthal",
            "upno",
            "fore",
            "dots",
            "rakit pc",
            "nubes",
            "scrabble",
            "baba",
            "SI",
            "AI",
            "mppl",
            "jarkom",
            "mbd",
            "imk",
            "wbd",
            "lu diem",
            "lu diem anying",
            "gausa bacot",
            "gausa bacot anying",
            "jangan brisik",
            "jangan brisik anying"
    };
    private static final int MAX_LENGTH = 256;
    private static final float PROBABILITY = 0.05f;

    @Override
    public <T1 extends GenericMessageEvent> void handle(T1 event, String[] eventArgs) {
        if (event instanceof MessageReceivedEvent msgEvent) {
            String srcContent = msgEvent.getMessage().getContentRaw().trim().strip();
            if (srcContent.length() > MAX_LENGTH) return;
            if (Math.random() >= PROBABILITY) return;

            JDAService.replyMessage(this,
                    String.format(
                            "daripada '%s' mending %s",
                            srcContent, replies[(int)(Math.random() * replies.length)]
                    ),
                    msgEvent.getMessage()
            );
        }
    }

    @Override
    public Set<String> getTags() {
        return null;
    }

    @Override
    public String getPrimaryTag() {
        return null;
    }
}
