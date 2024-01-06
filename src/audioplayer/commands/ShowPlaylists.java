package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.audiocollections.Playlist;
import audioplayer.audiomodels.audiofiles.AudioFile;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public final class ShowPlaylists extends AudioPlayerCommand {

    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        @AllArgsConstructor
        @Getter
        @Setter
        class ShowPlaylistResult {
            private int followers;
            private String name;
            private ArrayList<String> songs;
            private String visibility;
        }

        ArrayList<ShowPlaylistResult> result = new ArrayList<>();

        for (Playlist playlist: audioPlayer.getPlaylists()) {
            ArrayList<String> songNames = new ArrayList<>();
            for (AudioFile song: playlist.getAudioFiles()) {
                songNames.add(song.getName());
            }
            result.add(new ShowPlaylistResult(playlist.getFollowers(), playlist.getName(),
                    songNames, playlist.isPublic() ? Constants.PUBLIC : Constants.PRIVATE));
        }
        output.putPOJO(Constants.OUTPUT_RESULT, result);
    }
}
