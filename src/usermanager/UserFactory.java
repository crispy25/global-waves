package usermanager;

import artist.Artist;
import commandinput.CommandInput;
import constants.Constants;
import fileio.input.UserInput;
import host.Host;

public final class UserFactory {

    private UserFactory() {
    }

    /**
     * Creates a new user based on the type given in the command input
     * @param commandInput the command input
     * @return the new user
     */
    public static User createUser(final CommandInput commandInput) {
        switch (commandInput.getType()) {
            case Constants.ARTIST_USER:
                return new Artist(commandInput.getUsername(),
                        commandInput.getAge(),
                        commandInput.getCity());
            case Constants.HOST_USER:
                return new Host(commandInput.getUsername(),
                        commandInput.getAge(),
                        commandInput.getCity());
            default:
                UserInput userInput = new UserInput();

                userInput.setUsername(commandInput.getUsername());
                userInput.setAge(commandInput.getAge());
                userInput.setCity(commandInput.getCity());

                return new User(userInput);
        }
    }
}
