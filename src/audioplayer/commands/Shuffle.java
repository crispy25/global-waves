package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Playlist;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

public final class Shuffle extends AudioPlayerCommand {

    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        audioPlayer.updateCurrentlyPlayingAudio(commandInput.getTimestamp());
        AudioEntity currentlyPlaying = audioPlayer.getCurrentlyPlaying();

        if (currentlyPlaying == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_LOADED_SHUFFLE);
        } else if (currentlyPlaying.isPodcast() || currentlyPlaying.isSong()) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.LOADED_SOURCE_NOT_A_PLAYLIST);
        } else {
            if (audioPlayer.isShuffle()) {
                ((Playlist) currentlyPlaying).setShuffle(false);
                output.put(Constants.OUTPUT_MESSAGE, Constants.SHUFFLE_DEACTIVATED);
            } else {
                ((Playlist) currentlyPlaying).setShuffleSeed(commandInput.getSeed());
                ((Playlist) currentlyPlaying).setShuffle(true);

                output.put(Constants.OUTPUT_MESSAGE, Constants.SHUFFLE_ACTIVATED);
            }
            audioPlayer.setShuffle(!audioPlayer.isShuffle());
        }
    }
}
