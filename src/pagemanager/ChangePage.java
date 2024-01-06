package pagemanager;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class ChangePage implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (!user.isOnline()) {
            output.put(Constants.OUTPUT_MESSAGE, commandInput.getUsername() + Constants.IS_OFFLINE);
            return;
        }
        user.getAudioPlayer().updateCurrentlyPlayingAudio(commandInput.getTimestamp());

        if (user.getPages().containsKey(commandInput.getNextPage())) {
            user.setCurrentPage(user.getPages().get(commandInput.getNextPage()));

            user.getPageHistory().add(user.getCurrentPage());

            output.put(Constants.OUTPUT_MESSAGE,
                    Constants.SUCCESSFUL_PERSONAL_PAGE_ACCESS.formatted(commandInput.getUsername(),
                            commandInput.getNextPage()));
        } else if (user.getAudioPlayer().getCurrentlyPlaying() != null) {
            User contentCreator = UserManager.searchUser(user.getAudioPlayer().
                                        getCurrentlyPlaying().getAudioCreator());

            if (contentCreator.getType().equals(Constants.ARTIST_USER)) {
                user.setCurrentPage(contentCreator.getPages().get(Constants.ARTIST_PAGE));
            } else {
                user.setCurrentPage(contentCreator.getPages().get(Constants.HOST_PAGE));
            }

            user.getPageHistory().add(user.getCurrentPage());

            output.put(Constants.OUTPUT_MESSAGE,
                    Constants.SUCCESSFUL_PERSONAL_PAGE_ACCESS.formatted(commandInput.getUsername(),
                            commandInput.getNextPage()));
        } else {
            output.put(Constants.OUTPUT_MESSAGE,
                    Constants.NON_EXISTENT_PAGE.formatted(commandInput.getUsername()));
        }
    }
}
