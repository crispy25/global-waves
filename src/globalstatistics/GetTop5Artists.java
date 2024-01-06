package globalstatistics;

import audioplayer.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class GetTop5Artists implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        LinkedHashMap<String, Integer> artists = new LinkedHashMap<>();

        for (User user : UserManager.getUsersByType(Constants.ARTIST_USER)) {
            artists.put(user.getName(), 0);
        }

        for (Map.Entry<String, Integer> entry : Library.getSongsLikes().entrySet()) {
            String artistName = entry.getKey().split(Constants.SEPARATOR)[1];
            if (artists.containsKey(artistName)) {
                artists.put(artistName, artists.get(artistName) + entry.getValue());
            }
        }

        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        artists = artists.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));

        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : artists.entrySet()) {
            result.add(entry.getKey());

            if (result.size() == Constants.MAX_RESULTS) {
                break;
            }
        }

        output.putPOJO(Constants.OUTPUT_RESULT, result);
        output.remove(Constants.OUTPUT_USER);
    }
}
