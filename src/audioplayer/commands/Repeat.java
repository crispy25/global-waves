package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;
import constants.RepeatStatesMessage;

public final class Repeat extends AudioPlayerCommand {
    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        audioPlayer.updateCurrentlyPlayingAudio(commandInput.getTimestamp());
        AudioEntity currentlyPlaying = audioPlayer.getCurrentlyPlaying();

        if (currentlyPlaying == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_LOADED_REPEAT);
            return;
        }

        int repeatState = (audioPlayer.getRepeatState() + 1) % Constants.REPEAT_MAX_STATES;
        audioPlayer.setRepeatState(repeatState);
        currentlyPlaying.setRepeatStateAtTimestamp(repeatState, commandInput.getTimestamp());
        output.put(Constants.OUTPUT_MESSAGE, Constants.REPEAT_MODE_CHANGED
                    + RepeatStatesMessage.get(repeatState, currentlyPlaying).toLowerCase()
                    + Constants.DOT);
    }
}
