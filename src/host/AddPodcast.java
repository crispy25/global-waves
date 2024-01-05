package host;

import audioplayer.Library;
import audioplayer.audiomodels.audiocollections.Podcast;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import fileio.input.PodcastInput;
import notifications.Notification;
import usermanager.User;
import usermanager.UserManager;

public final class AddPodcast implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, String.format(Constants.USER_NON_EXISTENT_MESSAGE,
                    commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.HOST_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_HOST.
                                                    formatted(commandInput.getUsername()));
        } else if (((Host) user).hasPodcast(commandInput.getName())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.SAME_PODCAST_NAME.
                                                    formatted(commandInput.getUsername()));
        } else if (!((Host) user).canAddPodcast(commandInput.getEpisodes())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.SAME_EPISODE_ERROR.
                                                    formatted(commandInput.getUsername()));
        } else {
            PodcastInput podcastInput = new PodcastInput();

            podcastInput.setName(commandInput.getName());
            podcastInput.setOwner(commandInput.getUsername());
            podcastInput.setEpisodes(commandInput.getEpisodes());

            Podcast podcast = new Podcast(podcastInput);
            ((Host) user).getPodcasts().add(podcast);
            Library.getPodcasts().add(podcast);

            // notify subscribers
            user.getSubscriberManager().notifySubscribers(
                    new Notification(Constants.NEW_PODCAST, Constants.NEW_PODCAST
                            + Constants.FROM_CONTENT_CREATOR.formatted(user.getName())));


            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_ADD_PODCAST.
                                                    formatted(commandInput.getUsername()));
        }
    }
}
