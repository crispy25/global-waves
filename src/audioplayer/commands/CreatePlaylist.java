package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.Library;
import audioplayer.audiomodels.audiocollections.Playlist;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

public final class CreatePlaylist extends AudioPlayerCommand {

    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        if (hasPlaylistWithName(audioPlayer, commandInput.getPlaylistName())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.PLAYLIST_WITH_SAME_NAME);
        } else {
            Playlist playlist = new Playlist(commandInput);
            audioPlayer.getPlaylists().add(playlist);

            Library.getPlaylists().add(playlist);
            Library.getPlaylistLikes().put(playlist, 0);

            output.put(Constants.OUTPUT_MESSAGE, Constants.PLAYLIST_CREATED);
        }
    }

    private boolean hasPlaylistWithName(final AudioPlayer audioPlayer, final String playlistName) {
        return audioPlayer.getPlaylists().stream().
                            anyMatch(playlist -> playlist.getName().equals(playlistName));
    }
}
