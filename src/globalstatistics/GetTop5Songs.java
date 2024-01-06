package globalstatistics;

import audioplayer.Library;
import audioplayer.audiomodels.audiofiles.AudioFile;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public final class GetTop5Songs implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        LinkedHashMap<String, Integer> songsLikes = new LinkedHashMap<>();
        Library.getSongs().forEach(song -> songsLikes.put(song.getName(), 0));

        for (User user: UserManager.getUsersByType(Constants.NORMAL_USER)) {
            for (AudioFile song : user.getAudioPlayer().getLikedSongs().getAudioFiles()) {
                songsLikes.put(song.getName(), songsLikes.get(song.getName()) + 1);
            }
        }

        // get the unique number of likes and sort them in reverse order
        ArrayList<Integer> numLikes = new ArrayList<>(new LinkedHashSet<>(songsLikes.values()));
        numLikes.sort(Comparator.reverseOrder());

        ArrayList<String> result = new ArrayList<>();
        int index = 0;

        // get the first 5 songs with the biggest number of likes
        while (index < numLikes.size()) {
            for (String songName: songsLikes.keySet()) {
                if (songsLikes.get(songName).equals(numLikes.get(index))) {
                    result.add(songName);
                    if (result.size() >= Constants.MAX_RESULTS) {
                        break;
                    }
                }
            }
            if (result.size() == Constants.MAX_RESULTS) {
                break;
            }
            index += 1;
        }

        output.put(Constants.OUTPUT_TIMESTAMP, commandInput.getTimestamp());
        output.putPOJO(Constants.OUTPUT_RESULT, result);
        output.remove(Constants.OUTPUT_USER);
    }
}
