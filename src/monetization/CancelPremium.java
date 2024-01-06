package monetization;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class CancelPremium implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                    formatted(commandInput.getUsername()));
        } else if (!user.isPremium()) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NOT_PREMIUM.
                    formatted(commandInput.getUsername()));
        } else {
            user.getAudioPlayer().updateCurrentlyPlayingAudio(commandInput.getTimestamp());
            user.payArtists();
            user.setPremium(false);
            user.setCredits(0);

            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_CANCEL_PREMIUM.
                    formatted(commandInput.getUsername()));
        }
    }
}
