package audioplayer;

import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Playlist;
import audioplayer.audiomodels.audiocollections.Podcast;
import audioplayer.audiomodels.audiofiles.AudioFile;
import audioplayer.audiomodels.audiofiles.Song;
import commandinput.CommandInput;
import constants.Constants;
import lombok.Getter;
import lombok.Setter;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public final class AudioPlayer {
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Podcast> podcasts = new ArrayList<>();

    private AudioEntity selectedAudioEntity;
    private ArrayList<AudioEntity> searchResult;

    private int repeatState;
    private boolean shuffle = false;

    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<Playlist> followedPlaylists = new ArrayList<>();
    private Playlist likedSongs = new Playlist();

    private AudioEntity currentlyPlaying;

    /**
     * Update currently playing audio entity
     * @param timestamp current time
     */
    public void updateCurrentlyPlayingAudio(final int timestamp) {
        if (currentlyPlaying != null) {
            currentlyPlaying.updateRemainedTime(timestamp);

            if (currentlyPlaying.getCurrentAudio() == null
                    || currentlyPlaying.getRemainedTime() <= 0) {
                currentlyPlaying = null;
                shuffle = false;
                repeatState = 0;
            } else {
                repeatState = currentlyPlaying.getRepeatState();
            }
        }
    }

    /**
     * Set the repeat state of the audio player
     * @param repeatState the new repeat state
     */
    public void setRepeatState(final int repeatState) {
        this.repeatState = repeatState;
        currentlyPlaying.setRepeatState(repeatState);
    }

    /**
     * Sets selected audio entity to the audio entity in the search result at index given in command
     */
    public void setSelectedAudioEntity(final CommandInput command) {
        User user = UserManager.searchUser(command.getUsername());
        assert user != null;

        selectedAudioEntity = (AudioEntity) user.getSearchBar().getSearchResult().
                                get(Integer.parseInt(command.getItemNumber()) - 1);

        // Use the copy of the audio
        if (!selectedAudioEntity.isPodcast()) {
            selectedAudioEntity = selectedAudioEntity.getAudioEntityCopy();
        } else {
            Podcast podcast = hasWatchedPodcast(selectedAudioEntity.getName());
            if (podcast != null) {
                selectedAudioEntity = podcast;
            } else {
                selectedAudioEntity = selectedAudioEntity.getAudioEntityCopy();
            }
        }
    }

    /**
     * Sets selected audio entity to null
     */
    public void resetSelectedAudioEntity() {
        selectedAudioEntity = null;
    }

    /**
     * Sets currently playing audio file to null
     */
    public void resetCurrentlyPlaying() {
        currentlyPlaying = null;
    }

    /**
     * Sets search result to null
     */
    public void resetSearchResult() {
        searchResult = null;
    }

    /**
     * Reset the audio player and if the user was watching a podcast, pause it
     * @param timestamp current time
     */
    public void resetAtTimestamp(final int timestamp) {
        if (currentlyPlaying != null) {
            // pause current episode if user is watching a podcast
            if (currentlyPlaying.isPodcast()) {
                currentlyPlaying.pauseAudio(timestamp);
                podcasts.add((Podcast) currentlyPlaying);
            }

            currentlyPlaying.updateRemainedTime(timestamp);
        }

        resetCurrentlyPlaying();
        resetSelectedAudioEntity();
    }

    /**
     * Checks if the playlist is in the list of the followed playlists
     * @param playlist the playlist to be checked for
     * @return true if user is following the playlist, false otherwise
     */
    public boolean isFollowingPlaylist(final Playlist playlist) {
        return followedPlaylists.stream().anyMatch(p -> p.equals(playlist));
    }


    /**
     * Checks if user has watched the podcast with the given name
     * @param podcastName the podcast name
     * @return the podcast if user has watched the podcast before, null otherwise
     */
    public Podcast hasWatchedPodcast(final String podcastName) {
        for (Podcast p : podcasts) {
            if (p.getName().equals(podcastName)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Removes the playlist from the followed playlists
     * @param playlist the playlist to unfollow
     */
    public void removePlaylistFromFollowedPlaylists(final Playlist playlist) {
        followedPlaylists.removeIf(p -> p.equals(playlist));
    }

    /**
     * Get the song names of the top 5 songs liked
     * @return all the songs names joined
     */
    public String getTop5SongsLiked() {
        LinkedHashMap<String, Integer> songsNames = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> sortedSongsNames;

        for (AudioFile audioFile : likedSongs.getAudioFiles()) {
            songsNames.put(((Song) audioFile).toString(),
                    Library.getSongsLikes().get(((Song) audioFile).toString()));
        }

        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        sortedSongsNames = songsNames.entrySet().stream().
                sorted(Map.Entry.comparingByValue(comparator)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));

        return getTop5SongsLikedNames(sortedSongsNames);
    }

    private String getTop5SongsLikedNames(final Map<String, Integer> sortedSongsNames) {
        int songCount = 0;

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : sortedSongsNames.entrySet()) {
            String[] keys = entry.getKey().split(Constants.SEPARATOR);
            stringBuilder.append(keys[0]);
            stringBuilder.append(", ");

            songCount++;
            if (songCount == Constants.MAX_RESULTS) {
                break;
            }
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Get the playlist names of the top 5 playlists from the list of followed playlists
     * @return all the playlists names joined
     */
    public String getTop5PlaylistsFollowed() {
        Library.updatePlaylistsLikes();

        LinkedHashMap<Playlist, Integer> playlistsNames = new LinkedHashMap<>();
        LinkedHashMap<Playlist, Integer> sortedPlaylistsNames;

        for (Playlist playlist : followedPlaylists) {
            playlistsNames.put(playlist, Library.getPlaylistLikes().
                                                    get(Library.getPlaylist(playlist)));
        }

        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        sortedPlaylistsNames = playlistsNames.entrySet().stream().
                sorted(Map.Entry.comparingByValue(comparator)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));


        return getTop5PlaylistsFollowedNames(sortedPlaylistsNames);
    }

    private String getTop5PlaylistsFollowedNames(final Map<Playlist, Integer> sortedPlaylists) {
        int playlistCount = 0;

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Playlist, Integer> entry : sortedPlaylists.entrySet()) {
            stringBuilder.append(entry.getKey().getName());
            stringBuilder.append(", ");

            playlistCount++;
            if (playlistCount == Constants.MAX_RESULTS) {
                break;
            }
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Get the playlist names from the list of followed playlists
     * @return all the playlist names joined
     */
    public String getFollowedPlaylistNames() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Playlist playlist : followedPlaylists) {
            stringBuilder.append(playlist.getName());
            stringBuilder.append(" - ");
            stringBuilder.append(playlist.getOwner());
            stringBuilder.append(", ");
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Get the song names from the playlist given
     * @param playlist the playlist
     * @return all the song names joined
     */
    public String getPlaylistSongNames(final Playlist playlist) {
        StringBuilder stringBuilder = new StringBuilder();

        for (AudioFile audioFile : playlist.getAudioFiles()) {
            stringBuilder.append(audioFile.getName());
            stringBuilder.append(" - ");
            stringBuilder.append(((Song) audioFile).getArtist());
            stringBuilder.append(", ");
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }
}
