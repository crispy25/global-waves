package searchbar;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import contracts.ISearchable;
import usermanager.User;
import usermanager.UserManager;
import audioplayer.Library;

import java.util.ArrayList;

public final class Search implements ICommand {

    @Override
    public void execute(final CommandInput commandInput,
                        final ObjectNode output) {
        User user = UserManager.getUser(commandInput.getUsername());

        if (!user.isOnline()) {
            output.putPOJO(Constants.OUTPUT_RESULTS, new ArrayList<>());
            output.put(Constants.OUTPUT_MESSAGE, user.getName() + Constants.IS_OFFLINE);
            return;
        }

        ArrayList<String> resultsName = new ArrayList<>();
        ArrayList<ISearchable> result = new ArrayList<>();
        AudioPlayer audioPlayer = user.getAudioPlayer();

       if (commandInput.getType().equals(Constants.ARTIST_USER)
               || commandInput.getType().equals(Constants.HOST_USER)) {
           searchContentCreator(commandInput, result, resultsName);
       } else {
           searchAudio(commandInput, result, resultsName);
       }

        audioPlayer.resetAtTimestamp(commandInput.getTimestamp());

        user.setUpcomingAd(false);
        user.setPlayingAd(false);

        user.getSearchBar().setSearchResult(result);
        user.getSearchBar().setSelected(null);

        output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_SEARCH + resultsName.size()
                                                + Constants.RESULTS);
        output.putPOJO(Constants.OUTPUT_RESULTS, resultsName);
    }

    private void searchContentCreator(final CommandInput commandInput,
                                      final ArrayList<ISearchable> result,
                                      final ArrayList<String> resultsName) {
        for (User u : UserManager.getUsersByType(commandInput.getType()))  {
            if (u.filter(commandInput.getFilters())) {
                result.add(u);
            }
            if (result.size() == Constants.MAX_RESULTS) {
                break;
            }
        }

        for (ISearchable u : result) {
            resultsName.add(((User) u).getUserInput().getUsername());
        }
    }

    private void searchAudio(final CommandInput commandInput,
                        final ArrayList<ISearchable> result,
                        final ArrayList<String> resultsName) {
        commandInput.getFilters().setUsername(commandInput.getUsername());
        for (AudioEntity audioEntity : Library.getAudioEntitiesByType(commandInput.getType())) {
            if (audioEntity.filter(commandInput.getFilters())) {
                result.add(audioEntity);
            }
            if (result.size() == Constants.MAX_RESULTS) {
                break;
            }
        }

        for (ISearchable audioFile : result) {
            resultsName.add(audioFile.getName());
        }
    }
}
