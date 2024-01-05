package usermanager;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;

public final class SwitchConnectionStatus implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {

        if (!UserManager.hasUser(commandInput.getUsername())) {
            output.put(Constants.OUTPUT_MESSAGE, String.format(Constants.USER_NON_EXISTENT_MESSAGE,
                                                                commandInput.getUsername()));
        } else {
            User user = UserManager.getUser(commandInput.getUsername());
            if (!user.getType().equals(Constants.NORMAL_USER)) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_NORMAL_USER.
                                                            formatted(commandInput.getUsername()));
            } else {
                if (user.getAudioPlayer().getCurrentlyPlaying() != null) {
                    if (user.isOnline()) {
                        user.getAudioPlayer().getCurrentlyPlaying().
                                pauseAudio(commandInput.getTimestamp());
                    } else {
                        user.getAudioPlayer().getCurrentlyPlaying().
                                resumeAudio(commandInput.getTimestamp());
                    }
                }
                user.setOnline(!user.isOnline());

                output.put(Constants.OUTPUT_MESSAGE, commandInput.getUsername()
                                                        + Constants.SUCCESSFUL_STATUS_CHANGE);
            }

        }
    }
}
