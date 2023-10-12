package ebsi.core;

import java.time.LocalDateTime;

public class Env {
    private static final String ebsiToken = System.getenv("DISCORD_EBSI_TOKEN");

    public static final String PREFIX = "!";
    public static LocalDateTime onlineStartTime;

    public static String getEbsiToken() {
        return ebsiToken;
    }
}
