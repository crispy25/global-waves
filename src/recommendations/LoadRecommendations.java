package recommendations;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class LoadRecommendations implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                                                        formatted(commandInput.getUsername()));
        } else if (!user.isOnline()) {
            output.put(Constants.OUTPUT_MESSAGE, commandInput.getUsername() + Constants.IS_OFFLINE);
        } else if (user.getLastRecommendation() == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_RECOMMENDATIONS_AVAILABLE);
        } else {
            user.getAudioPlayer().updateCurrentlyPlayingAudio(commandInput.getTimestamp());
            user.getAudioPlayer().resetAtTimestamp(commandInput.getTimestamp());

            user.setUpcomingAd(false);
            user.setPlayingAd(false);

            user.getAudioPlayer().setCurrentlyPlaying(
                                            user.getLastRecommendation().getAudioEntityCopy());
            user.getAudioPlayer().getCurrentlyPlaying().setUser(user);
            user.getAudioPlayer().getCurrentlyPlaying().startAudio(commandInput.getTimestamp());

            user.getSearchBar().setSearchResult(null);
            user.getSearchBar().setSelected(null);

            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_LOAD);
        }
    }
}
