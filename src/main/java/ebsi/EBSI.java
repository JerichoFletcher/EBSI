package ebsi;

import ebsi.core.Env;
import ebsi.core.Handlers;
import ebsi.net.jda.JDAService;
import ebsi.util.Log;

public class EBSI {
    public static void main(String[] args) throws InterruptedException {
        JDAService.start();
        Handlers.init();
    }
}
