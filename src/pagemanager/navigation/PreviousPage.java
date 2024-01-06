package pagemanager.navigation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class PreviousPage implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_USER_DATA.
                    formatted(Constants.NORMAL_USER, commandInput.getUsername()));
        } else {
            int prevPageIndex = user.getPageHistory().lastIndexOf(user.getCurrentPage()) - 1;
            if (prevPageIndex < 0) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.NO_BACK_PAGES);
            } else {
                user.setCurrentPage(user.getPageHistory().get(prevPageIndex));

                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_PREVIOUS_PAGE.
                        formatted(commandInput.getUsername()));
            }
        }
    }
}
