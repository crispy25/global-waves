package constants;

import audioplayer.audiomodels.AudioEntity;

import java.util.ArrayList;
import java.util.List;

public final class RepeatStatesMessage {
    public static final String NO_REPEAT = "No Repeat";
    public static final String REPEAT_ONCE = "Repeat Once";
    public static final String REPEAT_INFINITE = "Repeat Infinite";
    public static final String REPEAT_ALL = "Repeat All";
    public static final String REPEAT_CURRENT_SONG = "Repeat Current Song";

    private static final ArrayList<String> STATES = new ArrayList<>(List.of(NO_REPEAT, REPEAT_ONCE,
                                                REPEAT_INFINITE, REPEAT_ALL, REPEAT_CURRENT_SONG));

    private RepeatStatesMessage() {

    }

    /**
     * Calculate repeat state message to be printed based on the audio entity being played
     * @return repeat state message based on the audio entity being played
     */
    public static String get(final int repeatState, final AudioEntity audioEntity) {
        if (repeatState == 0 || audioEntity == null) {
            return STATES.get(0);
        }
        if (audioEntity.isPodcast() || audioEntity.isSong()) {
            return STATES.get(repeatState);
        } else {
            return STATES.get(repeatState + 2);
        }
    }
}
