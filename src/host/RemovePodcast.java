package host;

import audioplayer.Library;
import audioplayer.audiomodels.audiocollections.Podcast;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class RemovePodcast implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, String.format(Constants.USER_NON_EXISTENT_MESSAGE,
                    commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.HOST_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_HOST.
                                                    formatted(commandInput.getUsername()));
        } else if (!((Host) user).hasPodcast(commandInput.getName())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_PODCAST_WITH_NAME.
                    formatted(commandInput.getUsername()));
        } else if (!((Host) user).canDeletePodcast(commandInput.getName(),
                                                    commandInput.getTimestamp())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.DELETE_PODCAST_ERROR.
                                                    formatted(commandInput.getUsername()));
        } else {
            Podcast podcast = ((Host) user).getPodcastWithName(commandInput.getName());

            ((Host) user).getPodcasts().remove(podcast);
            Library.getPodcasts().remove(podcast);

            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_DELETE_PODCAST.
                                                    formatted(commandInput.getUsername()));
        }
    }
}
