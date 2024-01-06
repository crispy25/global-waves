package monetization;

import artist.Artist;
import artist.Merch;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class BuyMerch implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                                                    formatted(commandInput.getUsername()));
        } else {
            User artist = UserManager.searchUser(user.getCurrentPage().getOwner());
            // if user is non-existent or is not an artist
            if (artist == null || !artist.getType().equals(Constants.ARTIST_USER)) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.MERCH_INVALID_PAGE);
                return;
            }

            Merch merch = ((Artist) artist).getMerch(commandInput.getName());
            // check if merch with given name exist
            if (merch == null) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.INVALID_MERCH_NAME.
                                                        formatted(commandInput.getName()));
            } else {
                ((Artist) artist).setMerchRevenue(((Artist) artist).getMerchRevenue()
                                                                            + merch.getPrice());
                user.getBoughtMerch().add(merch.getName());

                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_BUY_MERCH.
                        formatted(commandInput.getUsername()));
            }
        }
    }
}
