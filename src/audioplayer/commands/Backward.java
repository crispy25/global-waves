package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Podcast;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

public final class Backward extends AudioPlayerCommand {
    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        audioPlayer.updateCurrentlyPlayingAudio(commandInput.getTimestamp());
        AudioEntity currentlyPlaying = audioPlayer.getCurrentlyPlaying();

        if (currentlyPlaying == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_LOADED_BACKWARD);
        } else if (!currentlyPlaying.isPodcast()) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_PODCAST);
        } else {
            ((Podcast) currentlyPlaying).forwardBackward(commandInput.getTimestamp(),
                                                        -Constants.SKIP_SECONDS);
            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_BACKWARD);
        }
    }
}
