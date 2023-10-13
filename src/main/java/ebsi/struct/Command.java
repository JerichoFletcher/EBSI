package ebsi.struct;

import ebsi.core.Env;
import ebsi.struct.controllers.MessageController;

public abstract class Command extends MessageController {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String[] getUsages();

    protected static String getCommandAliases(Command command) {
        return String.format("*Alias:* `%s`", String.join("`, `", command.getTags()));
    }

    protected static String getCommandUsages(Command command) {
        StringBuilder str = new StringBuilder();
        str.append("*Usages:*");

        String primaryTag = command.getPrimaryTag();
        for (String usage : command.getUsages()) {
            str.append(String.format("\n`%s%s", Env.PREFIX, primaryTag));
            if (!usage.isBlank()) str.append(String.format(" %s", usage));
            str.append("`");
        }

        return str.toString();
    }
}
