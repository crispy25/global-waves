package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

public final class Prev extends AudioPlayerCommand {
    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        audioPlayer.updateCurrentlyPlayingAudio(commandInput.getTimestamp());
        AudioEntity currentlyPlaying = audioPlayer.getCurrentlyPlaying();

        if (currentlyPlaying == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_LOADED_PREV);
        } else {
            currentlyPlaying = currentlyPlaying.prev(commandInput.getTimestamp());
            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_PREV
                        + currentlyPlaying.getCurrentAudio().getName() + Constants.DOT);
        }
    }
}
