package commandinput;

import audioplayer.AudioPlayer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.CommandTypes;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public abstract class AudioPlayerCommand implements ICommand {

    /**
     * Execute the command if the user is online
     * @param commandInput command given
     * @param output output of the command
     */
    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.getUser(commandInput.getUsername());

        if (user.isOnline()) {
            user.getAudioPlayer().updateCurrentlyPlayingAudio(commandInput.getTimestamp());
            user.updateAudioPlayer();

            execute(user.getAudioPlayer(), commandInput, output);

            // reset user selected entity
            user.getSearchBar().setSelected(null);

        } else {
            if (commandInput.getCommand().equals(CommandTypes.STATUS)) {
                execute(user.getAudioPlayer(), commandInput, output);
            } else {
                output.put(Constants.OUTPUT_MESSAGE,
                        commandInput.getUsername() + Constants.IS_OFFLINE);
            }
        }
    }

    protected abstract void execute(AudioPlayer audioPlayer,
                                    CommandInput commandInput,
                                    ObjectNode output);
}
