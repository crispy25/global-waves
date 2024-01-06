package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.audiocollections.Playlist;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

public final class SwitchVisibility extends AudioPlayerCommand {

    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        if (commandInput.getPlaylistId() - 1 >= audioPlayer.getPlaylists().size()) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.INVALID_PLAYLIST_ID);
        } else {
            Playlist playlist = audioPlayer.getPlaylists().get(commandInput.getPlaylistId() - 1);
            playlist.switchVisibility();
            String visibility = playlist.isPublic() ? Constants.PUBLIC : Constants.PRIVATE;
            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_SWITCH_VISIBILITY
                                                    + visibility + Constants.DOT);
        }
    }
}
