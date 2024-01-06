package admin;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;

public final class GetAllUsers implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        ArrayList<String> allUsers = new ArrayList<>();

        for (User user : UserManager.getUsersByType(Constants.NORMAL_USER)) {
            allUsers.add(user.getName());
        }

        for (User user : UserManager.getUsersByType(Constants.ARTIST_USER)) {
            allUsers.add(user.getName());
        }

        for (User user : UserManager.getUsersByType(Constants.HOST_USER)) {
            allUsers.add(user.getName());
        }

        output.putPOJO(Constants.OUTPUT_RESULT, allUsers);
        output.remove(Constants.OUTPUT_USER);
    }
}
