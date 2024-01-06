package advertising;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class AdBreak implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());
        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                                                        formatted(commandInput.getUsername()));
        } else {
            // update normal user audio player
            user.getAudioPlayer().updateCurrentlyPlayingAudio(commandInput.getTimestamp());
            if (user.getAudioPlayer().getCurrentlyPlaying() == null) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NOT_PLAYING_MUSIC.
                                                        formatted(commandInput.getUsername()));
            } else {
                user.setUpcomingAd(true);
                user.setCredits(commandInput.getPrice());

                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_AD_BREAK);
            }
        }
    }
}
