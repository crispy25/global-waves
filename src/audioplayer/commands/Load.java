package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;
import constants.RepeatStates;
import usermanager.User;
import usermanager.UserManager;

public final class Load extends AudioPlayerCommand {
    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        AudioEntity selectedAudioEntity = audioPlayer.getSelectedAudioEntity();
        if (selectedAudioEntity == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_SELECTED_LOAD);
        } else if (selectedAudioEntity.isEmpty()) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.EMPTY_COLLECTION);
        } else {
            User user = UserManager.searchUser(commandInput.getUsername());
            assert user != null;

            if (selectedAudioEntity.isPodcast()
                    && selectedAudioEntity.getCurrentAudio() != null) {
                selectedAudioEntity.resumeAudio(commandInput.getTimestamp());
            } else {
                selectedAudioEntity.setUser(user);
                selectedAudioEntity.startAudio(commandInput.getTimestamp());
            }
            audioPlayer.setCurrentlyPlaying(selectedAudioEntity);

            audioPlayer.setShuffle(false);
            audioPlayer.setRepeatState(RepeatStates.NO_REPEAT);
            audioPlayer.getCurrentlyPlaying().setRepeatState(RepeatStates.NO_REPEAT);

            audioPlayer.resetSelectedAudioEntity();
            audioPlayer.resetSearchResult();

            user.getSearchBar().setSearchResult(null);
            user.getSearchBar().setSelected(null);

            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_LOAD);
        }
    }
}
