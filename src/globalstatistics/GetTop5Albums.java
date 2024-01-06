package globalstatistics;

import audioplayer.Library;
import audioplayer.audiomodels.audiocollections.Album;
import audioplayer.audiomodels.audiofiles.AudioFile;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Map;

import java.util.stream.Collectors;

public final class GetTop5Albums implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {

        LinkedHashMap<String, Integer> albums = new LinkedHashMap<>();

        for (Album album : Library.getAlbums()) {
            albums.put(album.getName() + Constants.SEPARATOR + album.getOwner(), 0);
            for (AudioFile song : album.getAudioFiles()) {
                albums.put(album.getName() + Constants.SEPARATOR + album.getOwner(),
                            albums.get(album.getName() + Constants.SEPARATOR + album.getOwner())
                            + Library.getSongsLikes().get(song.toString()));
            }
        }

        TreeMap<String, Integer> albumsSortedLexicographic = new TreeMap<>(albums);
        LinkedHashMap<String, Integer> sortedAlbums =
                new LinkedHashMap<>(albumsSortedLexicographic);

        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        sortedAlbums = sortedAlbums.entrySet().stream().
                sorted(Map.Entry.comparingByValue(comparator)).
                    collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));

        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedAlbums.entrySet()) {
            result.add(entry.getKey().split(Constants.SEPARATOR)[0]);

            if (result.size() == Constants.MAX_RESULTS) {
                break;
            }
        }

        output.putPOJO(Constants.OUTPUT_RESULT, result);
        output.remove(Constants.OUTPUT_USER);
    }
}
