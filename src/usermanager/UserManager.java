package usermanager;

import fileio.input.LibraryInput;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

public final class UserManager {
    @Getter
    private static ArrayList<User> users;

    @Getter
    private static LibraryInput library;

    private UserManager() {
    }

    /**
     * Initialize the user manager
     * @param libraryInput the library input
     */
    public static void initialize(final LibraryInput libraryInput) {

        library = libraryInput;
        users = new ArrayList<>();

        for (UserInput userInput : libraryInput.getUsers()) {
            users.add(new User(userInput));
        }
    }

    /**
     * Search the all users of the given type
     * @param type the type of the user
     * @return the list of users
     */
    public static ArrayList<User> getUsersByType(final String type) {
        ArrayList<User> usersByType = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals(type)) {
                usersByType.add(user);
            }
        }
        return usersByType;
    }

    /**
     * Search for the user with the given username
     * @param username the username of the searched user
     * @return the user
     */
    public static User searchUser(final String username) {
        for (User user: users) {
            if (user.getUserInput().getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


    /**
     * Search for the user with the given username
     * @param username the username of the searched user
     * @return the user
     */
    public static UserInput getUserInputFromLibrary(final String username) {
        for (UserInput userInput: library.getUsers()) {
            if (userInput.getUsername().equals(username)) {
                return userInput;
            }
        }
        return null;
    }

    /**
     * Search for the user with the given username and if no user is found, a new user is created
     * @param username the username of the searched user
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user: users) {
            if (user.getUserInput().getUsername().equals(username)) {
                return user;
            }
        }

        User newUser = new User(getUserInputFromLibrary(username));
        users.add(newUser);
        return newUser;
    }

    /**
     * Checks if the user manager has the user with the given username
     * @param username the username of the user
     * @return true if user is found, false otherwise
     */
    public static boolean hasUser(final String username) {
        return users.stream().anyMatch(user -> user.getName().equals(username));
    }
}
