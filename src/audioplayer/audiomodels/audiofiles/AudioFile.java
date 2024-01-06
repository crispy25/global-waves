package audioplayer.audiomodels.audiofiles;

import constants.Constants;
import lombok.Getter;
import lombok.Setter;
import constants.RepeatStates;
import audioplayer.audiomodels.AudioEntity;
import contracts.IFilterable;

@Getter
@Setter
public abstract class AudioFile extends AudioEntity implements IFilterable {
    protected int lastUpdateTime;
    protected int remainedTime = 0;
    protected boolean playing = false;
    protected boolean playedTwice = false;

    /**
     * Update number of listens for the user listening this audio entity
     */
    public abstract void updateUserListens();

    /**
     * Update number of listens for the artist of this audio entity
     */
    public abstract void updateAudioCreatorListens();

    @Override
    public final void setRepeatState(final int repeatState) {
        super.setRepeatState(repeatState);
        playedTwice = false;
    }

    @Override
    public final void pauseAudio(final int timestamp) {
        updateRemainedTime(timestamp);
        playing = false;
    }

    @Override
    public final void resumeAudio(final int timestamp) {
        playing = true;
        lastUpdateTime = timestamp;

        updateUserListens();
    }


    @Override
    public final void updateRemainedTime(final int timestamp) {
        if (playing) {
            remainedTime -= Math.abs(timestamp - lastUpdateTime);
            lastUpdateTime = timestamp;

            if (remainedTime <= 0) {
                playing = false;

                if (user.isUpcomingAd()) {
                    user.setUpcomingAd(false);
                    playing = true;

                    remainedTime += Constants.AD_DURATION;
                    user.setPlayingAd(true);
                    user.payArtists();
                }

                if (user.isPlayingAd()) {
                    if (remainedTime <= 0) {
                        user.setPlayingAd(false);
                        playing = false;
                    }
                }

                if (repeatState == RepeatStates.REPEAT_ONCE && !playedTwice) {
                    remainedTime = getDuration() + remainedTime;
                    playing = true;
                    playedTwice = true;
                    repeatState = RepeatStates.NO_REPEAT;

                    updateUserListens();

                    if (remainedTime <= 0) {
                        playing = false;
                    }

                } else if (repeatState == RepeatStates.REPEAT_INFINITE) {
                    int timesPlayed = -remainedTime / getDuration();
                    remainedTime = getDuration() - (-remainedTime % getDuration());
                    while (timesPlayed > 0) {
                        updateUserListens();
                        timesPlayed--;
                    }
                    playing = true;
                }
            }
        }
    }

    @Override
    public final AudioEntity next(final int timestamp) {
        remainedTime = 0;
        updateRemainedTime(timestamp);
        if (playing) {
            return this;
        }
        return null;
    }

    @Override
    public final AudioEntity prev(final int timestamp) {
        if (remainedTime < getDuration()) {
            startAudio(timestamp);
        } else if (playedTwice) {
            playedTwice = false;
            playing = true;
        }
        startAudio(timestamp);

        return this;
    }

    @Override
    public final boolean isPaused() {
        return !playing;
    }

    @Override
    public final AudioEntity getCurrentAudio() {
        return this;
    }

    /**
     * @return duration of the audio file playing
     */
    public abstract int getDuration();
}
