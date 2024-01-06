package audioplayer.audiomodels.audiocollections;

import audioplayer.audiomodels.audiofiles.AudioFile;
import constants.Constants;
import commandinput.Filter;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;
import constants.RepeatStates;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiofiles.Episode;

@Getter
@Setter
public final class Podcast extends AudioCollection {

    @Override
    public AudioEntity getAudioEntityCopy() {
        Podcast podcast = new Podcast();
        podcast.setName(name);
        podcast.setOwner(owner);
        podcast.setAudioCreator(owner);
        podcast.setUser(user);

        for (AudioFile audioFile : audioFiles) {
            AudioFile audioFileCopy = (AudioFile) audioFile.getAudioEntityCopy();
            audioFileCopy.setAudioCreator(owner);
            podcast.getAudioFiles().add(audioFileCopy);
        }

        return podcast;
    }

    @Override
    public boolean filter(final Filter filters) {
        if (filters.getName() != null && !name.startsWith(filters.getName())) {
            return false;
        }
        if (filters.getOwner() != null && !owner.equals(filters.getOwner())) {
            return false;
        }
        return true;
    }

    public Podcast(final PodcastInput podcast) {
        setName(podcast.getName());
        setOwner(podcast.getOwner());
        setAudioCreator(podcast.getOwner());

        for (EpisodeInput episodeInput: podcast.getEpisodes()) {
            Episode episode = new Episode(episodeInput);
            episode.setAudioCreator(owner);
            audioFiles.add(episode);
        }
    }

    public Podcast() {
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void startAudio(final int timestamp) {
        // If user was not watching this podcast before, start from the beginning, else
        // resume from the last episode
        if (currentAudioFile == null) {
            currentAudioFile = audioFiles.get(0);
            currentAudioFile.startAudio(timestamp);
        } else {
            currentAudioFile.resumeAudio(timestamp);
        }
    }

    @Override
    public void nextAudioFileAtTime(final int timePassed, final int timestamp) {
        nextEpisode(timePassed, timestamp);
    }

    /**
     * Update currently playing episode
     * @param timePassed time since last episode ended
     * @param timestamp current time
     */
    public void nextEpisode(final int timePassed, final int timestamp) {
        if (repeatState == RepeatStates.REPEAT_ONCE) {
            currentAudioFile.setRemainedTime(currentAudioFile.getDuration() + timePassed);
            currentAudioFile.setLastUpdateTime(timestamp);
        } else if (repeatState == RepeatStates.REPEAT_INFINITE) {
            currentAudioFile.setRemainedTime(currentAudioFile.getDuration()
                                            - (-timePassed % currentAudioFile.getDuration()));
            currentAudioFile.setLastUpdateTime(timestamp);
        } else {
            int index, time = timePassed;
            do {
                index = audioFiles.indexOf(currentAudioFile) + 1;
                if (index >= audioFiles.size()) {
                    currentAudioFile = null;
                    break;
                }
                currentAudioFile = audioFiles.get(index);
                currentAudioFile.setRemainedTime(currentAudioFile.getDuration() - time);
                currentAudioFile.setLastUpdateTime(timestamp);
                time -= currentAudioFile.getDuration();

                currentAudioFile.updateUserListens();

            } while (time >= 0);

            if (currentAudioFile != null) {
                currentAudioFile.setPlaying(true);
            }

        }
    }

    /**
     * Move forward or backward in the current episode by a given amount
     * @param timestamp current time
     * @param time time to skip or rewind
     */
    public void forwardBackward(final int timestamp, final int time) {

        if (time > 0) {
            if (currentAudioFile.getRemainedTime() < Constants.SKIP_SECONDS) {
                nextEpisode(0, timestamp);
                currentAudioFile.startAudio(timestamp);
            }
        } else {
            if (currentAudioFile.getRemainedTime() < Constants.SKIP_SECONDS) {
                currentAudioFile.startAudio(timestamp);
            }
        }
        currentAudioFile.setPlaying(true);
        currentAudioFile.setRemainedTime(currentAudioFile.getRemainedTime() - time);
        currentAudioFile.updateRemainedTime(timestamp);
    }

    @Override
    public AudioEntity prev(final int timestamp) {
        currentAudioFile.startAudio(timestamp);
        return this;
    }

    @Override
    public boolean isPodcast() {
        return true;
    }
}
