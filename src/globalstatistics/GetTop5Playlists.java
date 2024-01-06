package globalstatistics;

import audioplayer.Library;
import audioplayer.audiomodels.audiocollections.Playlist;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;

import java.util.ArrayList;

public final class GetTop5Playlists implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        ArrayList<Playlist> sortedPlaylists = new ArrayList<>(Library.getPlaylists());

        sortedPlaylists.sort((o1, o2) -> o2.getFollowers() - o1.getFollowers());

        ArrayList<String> result = new ArrayList<>();
        for (Playlist playlist: sortedPlaylists) {
            result.add(playlist.getName());

            if (result.size() == Constants.MAX_RESULTS) {
                break;
            }
        }

        output.put(Constants.OUTPUT_TIMESTAMP, commandInput.getTimestamp());
        output.putPOJO(Constants.OUTPUT_RESULT, result);
        output.remove(Constants.OUTPUT_USER);
    }
}
