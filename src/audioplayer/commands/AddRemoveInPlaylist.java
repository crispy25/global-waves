package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Playlist;
import audioplayer.audiomodels.audiofiles.Song;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;
import usermanager.User;
import usermanager.UserManager;

public final class AddRemoveInPlaylist extends AudioPlayerCommand {

    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        AudioEntity currentlyPlaying = audioPlayer.getCurrentlyPlaying();
        Playlist playlist;
        if (currentlyPlaying == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_LOADED_ADD_REMOVE);
        } else if (!currentlyPlaying.getCurrentAudio().isSong()) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_SONG);
        } else {
            try {
                playlist =  audioPlayer.getPlaylists().get(commandInput.getPlaylistId() - 1);
            } catch (IndexOutOfBoundsException e) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.PLAYLIST_NOT_EXISTS);
                return;
            }
            if (playlist.addRemoveSong((Song) currentlyPlaying.getCurrentAudio())
                    == Playlist.ResultCodes.ADDED.ordinal()) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_ADD);
            } else {
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_REMOVE);
            }
            audioPlayer.resetSelectedAudioEntity();

            User user = UserManager.searchUser(commandInput.getUsername());
            assert user != null;
            user.getSearchBar().setSearchResult(null);
            user.getSearchBar().setSelected(null);
        }
    }
}
