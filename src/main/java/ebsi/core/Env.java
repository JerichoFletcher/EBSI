package ebsi.core;

import java.time.LocalDateTime;

public class Env {
    private static final String ebsiToken = System.getenv("DISCORD_EBSI_TOKEN");
    private static final String appData = System.getProperty("user.dir") + "\\EBSI";
    private static final String guildData = appData + "\\guild";

    public static final String PREFIX = "!";
    public static LocalDateTime onlineStartTime;

    public static String getEbsiToken() {
        return ebsiToken;
    }
    public static String getAppDataPath() {
        return appData;
    }
    public static String getGuildDataPath() {
        return guildData;
    }
}
