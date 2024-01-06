package audioplayer.audiomodels;

import constants.Constants;
import contracts.IPlayable;
import contracts.ISearchable;
import lombok.Getter;
import lombok.Setter;
import contracts.IFilterable;
import usermanager.User;

@Getter
@Setter
public abstract class AudioEntity implements IFilterable, IPlayable, ISearchable {

    protected String name;
    protected int repeatState;

    protected User user;
    protected String audioCreator;

    @Override
    public final void setRepeatStateAtTimestamp(final int newRepeatState, final int timestamp) {
        updateRemainedTime(timestamp);
        repeatState = newRepeatState;
    }

    /**
     * @return true if audio entity has at least one audio file, false otherwise
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * @return true if audio entity is a song, false otherwise
     */
    public boolean isSong() {
        return false;
    }

    /**
     * @return true if audio entity is a podcast, false otherwise
     */
    public boolean isPodcast() {
        return false;
    }

    /**
     * @return true if audio entity is an album, false otherwise
     */
    public boolean isAlbum() {
        return false;
    }

    /**
     * @return current audio file playing
     */
    public abstract AudioEntity getCurrentAudio();

    /**
     * Skip to the next audio file
     * @param timestamp current time
     * @return next audio file
     */
    public abstract AudioEntity next(int timestamp);

    /**
     * Rewind to the last audio file
     * @param timestamp current time
     * @return previous audio file
     */
    public abstract AudioEntity prev(int timestamp);

    /**
     * Creates a copy of the audio entity
     * @return the copy of audio entity
     */
    public abstract AudioEntity getAudioEntityCopy();

    /**
     * @return audio type
     */
    @Override
    public String getType() {
        return Constants.AUDIO;
    }
}
