package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

public final class Next extends AudioPlayerCommand {

    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        audioPlayer.updateCurrentlyPlayingAudio(commandInput.getTimestamp());
        AudioEntity currentlyPlaying = audioPlayer.getCurrentlyPlaying();

        if (currentlyPlaying == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_LOADED_NEXT);
        } else {
            // if currently playing is an ad, don't skip
            if (currentlyPlaying.getUser().isPlayingAd()) {
                return;
            }

            currentlyPlaying = currentlyPlaying.next(commandInput.getTimestamp());
            if (currentlyPlaying == null) {
                audioPlayer.setShuffle(false);
                output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_LOADED_NEXT);
            } else {
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_NEXT
                            + currentlyPlaying.getCurrentAudio().getName() + Constants.DOT);
            }
        }
    }
}
