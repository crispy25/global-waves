package searchbar;

import audioplayer.AudioPlayer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import contracts.ISearchable;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;

public final class Select implements ICommand {

    @Override
    public void execute(final CommandInput commandInput,
                        final ObjectNode output) {

        User user = UserManager.getUser(commandInput.getUsername());

        if (!user.isOnline()) {
            output.put(Constants.OUTPUT_MESSAGE, user.getName() + Constants.IS_OFFLINE);
            return;
        }

        AudioPlayer audioPlayer = user.getAudioPlayer();
        ArrayList<ISearchable> searchResult = user.getSearchBar().getSearchResult();

        if (searchResult == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SEARCH_BEFORE_SELECT);
        } else if (Integer.parseInt(commandInput.getItemNumber()) > searchResult.size()) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.INVALID_ID);
        } else {
            user.getSearchBar().setSelected(searchResult.
                                        get(Integer.parseInt(commandInput.getItemNumber()) - 1));

            // set selected audio entity only if it's an audio source
            if (searchResult.get(0).getType().equals(Constants.AUDIO)) {
                audioPlayer.setSelectedAudioEntity(commandInput);

                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_SELECT
                                    + searchResult.get(Integer.parseInt(commandInput.
                                        getItemNumber()) - 1).getName()
                                            + Constants.DOT);

            } else {
                User selectedUser = ((User) user.getSearchBar().getSelected());
                if (selectedUser.getType().equals(Constants.ARTIST_USER)) {
                    user.setCurrentPage(selectedUser.getPages().get(Constants.ARTIST_PAGE));
                } else {
                    user.setCurrentPage(selectedUser.getPages().get(Constants.HOST_PAGE));
                }

                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_SELECT
                                    + searchResult.get(Integer.parseInt(commandInput.
                                        getItemNumber()) - 1).getName()
                                            + "'s page"
                                                + Constants.DOT);
            }

            user.getSearchBar().setSearchResult(null);
        }
    }
}
