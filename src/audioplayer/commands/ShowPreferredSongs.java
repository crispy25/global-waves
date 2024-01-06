package audioplayer.commands;

import audioplayer.AudioPlayer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

import java.util.ArrayList;

public final class ShowPreferredSongs extends AudioPlayerCommand {
    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        ArrayList<String> songNames = new ArrayList<>();

        audioPlayer.getLikedSongs().getAudioFiles().forEach(song -> songNames.add(song.getName()));

        output.putPOJO(Constants.OUTPUT_RESULT, songNames);
    }
}
