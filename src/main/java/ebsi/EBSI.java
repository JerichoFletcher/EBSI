package ebsi;

import ebsi.core.Handlers;
import ebsi.net.jda.JDAService;

public class EBSI {
    public static void main(String[] args) throws InterruptedException {
        JDAService.start();
        Handlers.init();
    }
}
