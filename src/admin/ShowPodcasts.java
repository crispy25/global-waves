package admin;

import audioplayer.audiomodels.audiocollections.Podcast;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import host.Host;
import usermanager.UserManager;

import java.util.ArrayList;

public final class ShowPodcasts implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        Host host = (Host) UserManager.searchUser(commandInput.getUsername());
        assert host != null;
        record PodcastInfo(String name, ArrayList<String> episodes) {
        }

        ArrayList<PodcastInfo> result = new ArrayList<>();

        for (Podcast podcast : host.getPodcasts()) {
            ArrayList<String> episodes = new ArrayList<>();
            podcast.getAudioFiles().forEach(audioFile -> episodes.add(audioFile.getName()));

            result.add(new PodcastInfo(podcast.getName(), episodes));
        }

        output.put(Constants.NORMAL_USER, commandInput.getUsername());
        output.putPOJO(Constants.OUTPUT_RESULT, result);
    }
}
