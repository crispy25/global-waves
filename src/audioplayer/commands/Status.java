package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.audiomodels.AudioEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;
import constants.RepeatStatesMessage;
import usermanager.UserManager;

public final class Status extends AudioPlayerCommand {
    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        AudioEntity currentlyPlaying = audioPlayer.getCurrentlyPlaying();
        record Stats(String name, int remainedTime, String repeat,
                     boolean shuffle, boolean paused) {
        }

        Stats stats;

        if (currentlyPlaying == null) {
            stats = new Stats("",
                    0,
                    RepeatStatesMessage.get(audioPlayer.getRepeatState(), null),
                    false,
                    true);
        } else {
            currentlyPlaying.updateRemainedTime(commandInput.getTimestamp());
            audioPlayer.setRepeatState(currentlyPlaying.getRepeatState());
            stats = new Stats(currentlyPlaying.getRemainedTime() <= 0
                    ? "" : currentlyPlaying.getCurrentAudio().getName(),
                    Math.max(currentlyPlaying.getRemainedTime(), 0),
                    RepeatStatesMessage.get(currentlyPlaying.getRepeatState(), currentlyPlaying),
                    currentlyPlaying.getRemainedTime() > 0 ? audioPlayer.isShuffle() : false,
                    !UserManager.getUser(commandInput.getUsername()).isOnline()
                                            ? false : currentlyPlaying.isPaused());

            // update currently playing audio if it's over
            if (currentlyPlaying.getRemainedTime() <= 0) {
                audioPlayer.resetCurrentlyPlaying();
            }
        }

        output.putPOJO(Constants.OUTPUT_STATS, stats);
    }
}
