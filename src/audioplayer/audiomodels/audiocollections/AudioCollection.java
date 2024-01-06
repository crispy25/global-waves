package audioplayer.audiomodels.audiocollections;

import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiofiles.AudioFile;
import lombok.Getter;
import lombok.Setter;
import usermanager.User;

import java.util.ArrayList;

@Getter
@Setter
public abstract class AudioCollection extends AudioEntity {
    protected String owner;
    protected AudioFile currentAudioFile;
    protected ArrayList<AudioFile> audioFiles = new ArrayList<>();

    /**
     * Set the user who is listening this audio entity
     * @param user the user who is listening this audio entity
     */
    @Override
    public final void setUser(final User user) {
        this.user = user;
        audioFiles.forEach(audioFile -> audioFile.setUser(user));
    }

    /**
     * Checks if the audio collection has the same name and owner as the given audio collection
     * @param audioCollection the audio collection to be checked against
     * @return true if it has the same name and owner, false otherwise
     */
    public final boolean equals(final AudioCollection audioCollection) {
        return name.equals(audioCollection.name) && owner.equals(audioCollection.owner);
    }

    /**
     * Move to the next audio file based on how much time passed since last audio file ended
     * @param timePassed time since last audio file ended
     * @param timestamp current time
     */
    public abstract void nextAudioFileAtTime(int timePassed, int timestamp);

    @Override
    public final AudioEntity next(final int timestamp) {
        currentAudioFile.setRemainedTime(0);
        updateRemainedTime(timestamp);

        return currentAudioFile != null ? currentAudioFile : null;
    }

    @Override
    public final void pauseAudio(final int timestamp) {
        updateRemainedTime(timestamp);

        if (currentAudioFile != null) {
            currentAudioFile.pauseAudio(timestamp);
        }
    }

    @Override
    public final void resumeAudio(final int timestamp) {
        if (currentAudioFile != null) {
            currentAudioFile.resumeAudio(timestamp);
        }
    }

    @Override
    public final void updateRemainedTime(final int timestamp) {
        if (currentAudioFile != null) {
            // currentAudioFile.updateUserListens();

            currentAudioFile.updateRemainedTime(timestamp);
            if (currentAudioFile != null && currentAudioFile.getRemainedTime() <= 0) {
                nextAudioFileAtTime(-currentAudioFile.getRemainedTime(), timestamp);
            }
        }
    }

    /**
     * Check if audio collection contains audio file
     * @param audioFile audio file to be checked
     * @return true if it contains the audio file, false otherwise
     */
    public final boolean hasAudioFile(final AudioFile audioFile) {
        for (AudioFile file : audioFiles) {
            if (file.getName().equals(audioFile.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if audio collection has the audio file
     * @param audioFileName the audio file name
     * @return true if the audio collection contains it, false otherwise
     */
    public final boolean hasAudioFileWithName(final String audioFileName) {
        for (AudioFile file : audioFiles) {
            if (file.getName().equals(audioFileName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final AudioEntity getCurrentAudio() {
        return currentAudioFile;
    }

    @Override
    public final int getRemainedTime() {
        return currentAudioFile == null ? 0 : currentAudioFile.getRemainedTime();
    }

    @Override
    public final boolean isPaused() {
        return currentAudioFile == null || currentAudioFile.isPaused();
    }
    @Override
    public final boolean isEmpty() {
        return audioFiles.isEmpty();
    }
}
