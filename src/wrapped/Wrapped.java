package wrapped;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

import java.util.LinkedHashMap;

public final class Wrapped implements ICommand {
    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_USER_DATA.
                    formatted(Constants.NORMAL_USER, commandInput.getUsername()));
        } else {
            if (user.getType().equals(Constants.NORMAL_USER)) {
                user.getAudioPlayer().updateCurrentlyPlayingAudio(commandInput.getTimestamp());
            } else {
                // update fans number of listens
                UserManager.getUsersByType(Constants.NORMAL_USER).forEach(u -> u.getAudioPlayer().
                                        updateCurrentlyPlayingAudio(commandInput.getTimestamp()));
            }
            LinkedHashMap<String, Object> result = user.getStatistics();

            // if user has no data to show
            if (result != null) {
                output.putPOJO(Constants.OUTPUT_RESULT, result);
            } else {
                output.put(Constants.OUTPUT_MESSAGE, Constants.NO_USER_DATA.
                        formatted(user.getType(), commandInput.getUsername()));
            }
        }
    }


}
