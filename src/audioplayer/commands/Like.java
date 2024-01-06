package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.Library;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Playlist;
import audioplayer.audiomodels.audiofiles.Song;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

public final class Like extends AudioPlayerCommand {

    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        audioPlayer.updateCurrentlyPlayingAudio(commandInput.getTimestamp());
        AudioEntity currentlyPlaying = audioPlayer.getCurrentlyPlaying();

        if (currentlyPlaying != null && currentlyPlaying.getCurrentAudio() != null
                && currentlyPlaying.getCurrentAudio().isSong()) {
            if (audioPlayer.getLikedSongs().
                    addRemoveSong((Song) currentlyPlaying.getCurrentAudio())
                        == Playlist.ResultCodes.ADDED.ordinal()) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_LIKE);

                Library.getSongsLikes().put(currentlyPlaying.getCurrentAudio().toString(),
                        Library.getSongsLikes().
                                get(currentlyPlaying.getCurrentAudio().toString()) + 1);

            } else {
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_UNLIKE);

                Library.getSongsLikes().put(currentlyPlaying.getCurrentAudio().toString(),
                        Library.getSongsLikes().
                                get(currentlyPlaying.getCurrentAudio().toString()) - 1);
            }
        } else if (currentlyPlaying == null || currentlyPlaying.getCurrentAudio() == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_LOADED_LIKE);
        } else {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_SONG);
        }
    }
}
