package ebsi.struct;

import ebsi.struct.controllers.MessageController;

public abstract class Command extends MessageController {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String[] getUsages();
}
