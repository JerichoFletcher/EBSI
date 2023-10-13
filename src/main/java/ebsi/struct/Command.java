package ebsi.struct;

import ebsi.core.Env;
import ebsi.struct.controllers.MessageController;

public abstract class Command extends MessageController {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String[] getUsages();

    public String getCommandAliases() {
        return String.format("*Alias:* `%s`", String.join("`, `", getTags()));
    }

    public String getCommandUsages() {
        StringBuilder str = new StringBuilder();
        str.append("*Usages:*");

        String primaryTag = getPrimaryTag();
        for (String usage : getUsages()) {
            str.append(String.format("\n`%s%s", Env.PREFIX, primaryTag));
            if (!usage.isBlank()) str.append(String.format(" %s", usage));
            str.append("`");
        }

        return str.toString();
    }
}
