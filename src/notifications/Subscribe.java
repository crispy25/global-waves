package notifications;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class Subscribe implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());
        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                                                        formatted(commandInput.getUsername()));
        } else {
            User contentCreator = UserManager.searchUser(user.getCurrentPage().getOwner());
            if (contentCreator.getType().equals(Constants.NORMAL_USER)) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUBSCRIBE_INVALID_PAGE);
            } else {
                if (contentCreator.getSubscriberManager().hasSubscriber(user)) {
                    contentCreator.getSubscriberManager().removeSubscriber(user);
                    output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_UNSUBSCRIBE.
                                                            formatted(commandInput.getUsername(),
                                                                    Constants.UNSUBSCRIBED,
                                                                        contentCreator.getName()));
                } else {
                    contentCreator.getSubscriberManager().addSubscriber(user);
                    output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_SUBSCRIBE.
                                                            formatted(commandInput.getUsername(),
                                                                    Constants.SUBSCRIBED,
                                                                        contentCreator.getName()));
                }
            }
        }
    }
}
