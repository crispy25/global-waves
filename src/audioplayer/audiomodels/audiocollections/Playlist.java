package audioplayer.audiomodels.audiocollections;

import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiofiles.AudioFile;
import audioplayer.audiomodels.audiofiles.Song;
import commandinput.CommandInput;
import commandinput.Filter;
import constants.RepeatStates;
import lombok.Getter;
import lombok.Setter;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
@Setter
public class Playlist extends AudioCollection {

    protected boolean isPublic = true;
    protected int followers;
    protected boolean shuffle = false;
    protected int shuffleSeed;

    protected ArrayList<Integer> indicesVector = new ArrayList<>();

    /**
     * Creates a copy of the audio entity
     * @return the copy of audio entity
     */
    @Override
    public AudioEntity getAudioEntityCopy() {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setOwner(owner);
        for (AudioFile audioFile : audioFiles) {
            playlist.getAudioFiles().add((AudioFile) audioFile.getAudioEntityCopy());
        }

        playlist.setUser(user);

        return playlist;
    }

    public enum ResultCodes {
        ADDED,
        REMOVED
    }

    /**
     * Check if thr given filters are found in the object properties
     * @param filters filters to be checked
     * @return true if the object has all the filters, false otherwise
     */
    @Override
    public boolean filter(final Filter filters) {

        if (filters.getName() != null && !name.toLowerCase().startsWith(
                                                    filters.getName().toLowerCase())) {
            return false;
        }
        if (filters.getOwner() != null && !owner.equals(filters.getOwner())) {
            return false;
        }

        return owner.equals(filters.getUsername()) || isPublic;
    }

    /**
     * Adds the song in the playlist if it is not present, otherwise removes it
     * @param song song to be added/removed
     * @return 0 if song was added, 1 otherwise
     */
    public int addRemoveSong(final Song song) {
        if (hasAudioFile(song)) {
            removeSong(song);
            return ResultCodes.REMOVED.ordinal();
        } else {
            Song newSong = new Song(song);
            audioFiles.add(newSong);
            return ResultCodes.ADDED.ordinal();
        }
    }

    /**
     * Remove the song from the playlist
     * @param song the song to be removed
     */
    public final void removeSong(final Song song) {
        for (AudioFile file : audioFiles) {
            if (((Song) file).toString().equals(song.toString())) {
                audioFiles.remove(file);
                return;
            }
        }
    }

    public Playlist(final CommandInput commandInput) {
        name = commandInput.getPlaylistName();
        owner = commandInput.getUsername();
        user = UserManager.searchUser(commandInput.getUsername());
    }

    public Playlist() {
    }

    /**
     * Update the shuffled indices vector based on the number of songs in the playlist
     */
    private void updateShuffledIndicesVector() {
        indicesVector.clear();
        for (int index = 0; index < audioFiles.size(); index++) {
            indicesVector.add(index);
        }
        Collections.shuffle(indicesVector, new Random(shuffleSeed));
    }

    @Override
    public final void startAudio(final int timestamp) {
        if (!shuffle) {
            currentAudioFile = audioFiles.get(0);
        } else {
            updateShuffledIndicesVector();
            currentAudioFile = audioFiles.get(indicesVector.get(0));
        }
        currentAudioFile.startAudio(timestamp);
    }

    @Override
    public final void nextAudioFileAtTime(final int timePassed, final int timestamp) {
        nextSongInPlaylist(timePassed, timestamp);
    }

    private int getNextSongIndex() {
        updateShuffledIndicesVector();
        int index = audioFiles.indexOf(currentAudioFile);
        int indexInShuffledVector = indicesVector.indexOf(index);
        if (shuffle) {
            if (indexInShuffledVector + 1 >= indicesVector.size()) {
                if (repeatState == RepeatStates.REPEAT_ALL) {
                    return indicesVector.get(0);
                }
                return indicesVector.size();
            }
            return indicesVector.get(indexInShuffledVector + 1);
        }
        return index + 1;
    }

    private int getPrevSongIndex() {
        updateShuffledIndicesVector();
        int index = audioFiles.indexOf(currentAudioFile);
        int indexInShuffledVector = indicesVector.indexOf(index);
        if (shuffle) {
            indexInShuffledVector -= 1;
            return indexInShuffledVector < 0
                    ? indicesVector.get(0) : indicesVector.get(indexInShuffledVector);
        }
        index--;
        if (index < 0) {
            index = 0;
        }
        return index;
    }

    /**
     * Moves to the current song after certain amount of time passed
     * @param timePassed time since last song ended
     */
    private void calculateSongCycle(final int timePassed) {
        int index;
        int time = timePassed;

        while (time >= 0) {
            index = getNextSongIndex();
            index = index % audioFiles.size();
            time -= audioFiles.get(index).getDuration();
            currentAudioFile = audioFiles.get(index);
            currentAudioFile.setRemainedTime(-time);

            currentAudioFile.updateUserListens();
        }
    }

    /**
     * Updates currently playing song based on the repeat mode and shuffle
     * @param timePassed time since last played song ended
     * @param timestamp current time
     */
    public final void nextSongInPlaylist(final int timePassed, final int timestamp) {
        if (repeatState == RepeatStates.REPEAT_CURRENT_SONG) {
            currentAudioFile.setRemainedTime(currentAudioFile.getDuration()
                                            - (timePassed % currentAudioFile.getDuration()));
            currentAudioFile.setLastUpdateTime(timestamp);
        } else if (repeatState == RepeatStates.REPEAT_ALL) {
            calculateSongCycle(timePassed);
            currentAudioFile.setLastUpdateTime(timestamp);
        } else {
            int index, time = timePassed;
            do {
                index = getNextSongIndex();
                if (index >= audioFiles.size()) {
                    currentAudioFile = null;
                    break;
                }

                currentAudioFile = audioFiles.get(index);
                currentAudioFile.setLastUpdateTime(timestamp);
                currentAudioFile.setRemainedTime(currentAudioFile.getDuration() - time);
                time -= currentAudioFile.getDuration();

                currentAudioFile.updateUserListens();

            } while (time >= 0);
        }
        if (currentAudioFile != null) {
            currentAudioFile.setPlaying(true);
        }
    }

    @Override
    public final AudioEntity prev(final int timestamp) {
        if (!(currentAudioFile.getRemainedTime() < currentAudioFile.getDuration())) {
            int index = getPrevSongIndex();

            currentAudioFile = audioFiles.get(index);
        }
        currentAudioFile.startAudio(timestamp);

        return this;
    }

    /**
     * Switch the visibility of the playlist between public and private
     */
    public final void switchVisibility() {
        isPublic = !isPublic;
    }

    /**
     * Update the number of followers of the playlist by adding value
     * @param value number of followers to be added
     */
    public final void updateFollowers(final int value) {
        followers += value;
    }

}
