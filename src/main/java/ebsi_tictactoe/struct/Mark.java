package ebsi_tictactoe.struct;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public enum Mark {
    EMPTY(0b00, null), X(0b10, "U+274C"), O(0b11, "U+2B55");

    private final byte mask;
    private final String emojiUnicode;

    Mark(int mask, String emojiUnicode) {
        this.mask = (byte) mask;
        this.emojiUnicode = emojiUnicode;
    }

    public boolean isEmpty() {
        return (mask & 0b10) == 0;
    }

    public Mark adversary() {
        return switch(this) {
            case O -> X;
            case X -> O;
            case EMPTY -> EMPTY;
        };
    }

    public boolean isO() {
        return (mask & 0b01) == 1;
    }

    public Emoji asEmoji() {
        return emojiUnicode == null ? null : Emoji.fromFormatted(emojiUnicode);
    }

    public static Mark from(int isNotEmpty, int isO) {
        return from(((isNotEmpty > 0 ? 1 : 0) << 1) | (isO > 0 ? 1 : 0));
    }

    public static Mark from(int mask) {
        return (mask & 0b10) == 0 ? EMPTY : ((mask & 0b01) == 0 ? X : O);
    }
}
