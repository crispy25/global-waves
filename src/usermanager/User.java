package usermanager;

import artist.Artist;
import audioplayer.AudioPlayer;
import audioplayer.Library;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Playlist;
import audioplayer.audiomodels.audiofiles.AudioFile;
import audioplayer.audiomodels.audiofiles.Song;
import commandinput.Filter;
import constants.Constants;
import contracts.IFilterable;
import contracts.ISearchable;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import notifications.Notification;
import contracts.Subscriber;
import notifications.SubscriberManager;
import pagemanager.HomePage;
import pagemanager.LikedContentPage;
import pagemanager.Page;
import searchbar.SearchBar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

@Getter
@Setter
public class User implements ISearchable, IFilterable, Subscriber {
    protected UserInput userInput;
    protected AudioPlayer audioPlayer;
    protected SearchBar searchBar;

    protected String type = Constants.NORMAL_USER;
    protected boolean online = true;

    protected Page currentPage;
    protected HashMap<String, Page> pages = new HashMap<>();
    private final ArrayList<Page> pageHistory = new ArrayList<>();

    private final LinkedHashMap<String, Integer> artistsListened = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> genresListened = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> songsListened = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> episodesListened = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> albumsListened = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> podcastsListened = new LinkedHashMap<>();

    private final HashMap<Boolean, HashMap<String, HashMap<String, Integer>>> history
                                                                                = new HashMap<>();
    private final ArrayList<String> boughtMerch = new ArrayList<>();

    private final ArrayList<Notification> notifications = new ArrayList<>();
    private final SubscriberManager subscriberManager = new SubscriberManager();

    private final ArrayList<Song> recommendedSongs = new ArrayList<>();
    private final ArrayList<Playlist> recommendedPlaylists = new ArrayList<>();

    private boolean premium = false;
    private double credits = 0;

    private boolean upcomingAd = false;
    private boolean playingAd = false;

    private AudioEntity lastRecommendation = null;

    public User() {
    }

    public User(final UserInput user) {
        userInput = user;
        audioPlayer = new AudioPlayer();
        searchBar = new SearchBar();

        type = Constants.NORMAL_USER;

        HomePage homePage = new HomePage();
        homePage.setOwner(user.getUsername());
        pages.put(Constants.HOME, homePage);

        LikedContentPage likedContentPage = new LikedContentPage();
        likedContentPage.setOwner(user.getUsername());
        pages.put(Constants.LIKED_CONTENT, likedContentPage);

        currentPage = pages.get(Constants.HOME);

        history.put(Constants.PREMIUM_USER, new HashMap<>());
        history.put(!Constants.PREMIUM_USER, new HashMap<>());
    }

    /**
     * Check if the given filters are found in the object properties
     * @param filters filters to be checked
     * @return true if the object has all the filters, false otherwise
     */
    @Override
    public boolean filter(final Filter filters) {
        if (filters.getName() != null && !getName().startsWith(filters.getName())) {
            return false;
        }
        return true;
    }

    /**
     * Get the username of the user
     * @return the username of the user
     */
    @Override
    public String getName() {
        return userInput.getUsername();
    }

    /**
     * Update the audio player of the user
     */
    public void updateAudioPlayer() {
        // remove liked songs that got deleted from the user liked songs
        ArrayList<AudioFile> songsToBeRemoved = new ArrayList<>();
        for (AudioFile audioFile: audioPlayer.getLikedSongs().getAudioFiles()) {
            if (!Library.getSongsLikes().containsKey(((Song) audioFile).toString())) {
                songsToBeRemoved.add(audioFile);
            }
        }
        for (AudioFile audioFile : songsToBeRemoved) {
            audioPlayer.getLikedSongs().removeSong((Song) audioFile);
        }

        // remove followed playlist if it was deleted
        for (int i = 0; i < audioPlayer.getFollowedPlaylists().size(); i++) {
            if (!Library.hasPlaylist(audioPlayer.getFollowedPlaylists().get(i))) {
                audioPlayer.getFollowedPlaylists().remove(i);
                i--;
            }
        }
    }


    /**
     * Check if user has any relation at the current time with entities of other users
     * @param timestamp current time
     * @return true if user can be deleted, false otherwise
     */
    public boolean canBeDeleted(final int timestamp) {
        for (Playlist playlist : audioPlayer.getPlaylists()) {
            for (User user : UserManager.getUsersByType(Constants.NORMAL_USER)) {
                if (user == this) {
                    continue;
                }

                user.getAudioPlayer().updateCurrentlyPlayingAudio(timestamp);
                AudioEntity currentlyPlaying = user.getAudioPlayer().getCurrentlyPlaying();

                if (currentlyPlaying == null) {
                    continue;
                }

                if (!currentlyPlaying.isPodcast() && !currentlyPlaying.isSong()) {
                    if (playlist.equals((Playlist) currentlyPlaying)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Remove all user audio entities from library
     */
    public void removeUserData() {
        // remove user created playlists
        for (Playlist playlist : audioPlayer.getPlaylists()) {
            Library.getPlaylists().remove(playlist);
        }

        // remove user likes
        for (AudioFile audioFile : audioPlayer.getLikedSongs().getAudioFiles()) {
            Song song = (Song) audioFile;
            Library.getSongsLikes().put(song.toString(),
                    Library.getSongsLikes().get(song.toString()) - 1);
        }

        // remove user playlist from other users if they followed it
        for (User user : UserManager.getUsersByType(Constants.NORMAL_USER)) {
            removeFollowedPlaylists(audioPlayer.getPlaylists(), user);
        }

        // unfollow playlists
        for (Playlist playlist : audioPlayer.getFollowedPlaylists()) {
            Library.getPlaylist(playlist).updateFollowers(-1);
        }

        // remove playlists created from library
        for (int i = 0; i < audioPlayer.getPlaylists().size(); i++) {
            Library.getPlaylists().remove(audioPlayer.getPlaylists().get(i));
        }
    }

    /**
     * For every playlist of deleted user, removed it from other users followed playlists
     * @param playlists playlists of user
     * @param user deleted user
     */
    private void removeFollowedPlaylists(final ArrayList<Playlist> playlists,
                                         final User user) {
        for (Playlist playlist : playlists) {
            for (int i = 0; i < user.getAudioPlayer().getFollowedPlaylists().size(); i++) {
                if (user.getAudioPlayer().getFollowedPlaylists().get(i) == playlist) {
                    user.getAudioPlayer().getFollowedPlaylists().remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * Get audio statistics for user
     * @return linked hash map with name of the audio entity as key and number of listens as value
     */
    public LinkedHashMap<String, Object> getStatistics() {
        if (artistsListened.isEmpty() && genresListened.isEmpty() && songsListened.isEmpty()
                && albumsListened.isEmpty() && episodesListened.isEmpty()) {
            return null;
        }

        LinkedHashMap<String, Object> statistics = new LinkedHashMap<>();

        statistics.put(Constants.TOP_ARTISTS, getTop5(artistsListened));
        statistics.put(Constants.TOP_GENRES, getTop5(genresListened));
        statistics.put(Constants.TOP_SONGS, getTop5(songsListened));
        statistics.put(Constants.TOP_ALBUMS, getTop5(albumsListened));
        statistics.put(Constants.TOP_EPISODES, getTop5(episodesListened));

        return statistics;
    }

    /**
     * Get top 5 objects by the value of the key
     * @param map the map with objects' names and their values
     * @return linked hash map with top 5
     */
    public LinkedHashMap<String, Object> getTop5(final LinkedHashMap<String, Integer> map) {
        LinkedHashMap<String, Integer> sortedMap;
        LinkedHashMap<String, Object> top = new LinkedHashMap<>();

        Comparator<String> comparatorLex = String::compareTo;
        sortedMap = map.entrySet().stream().
                sorted(Map.Entry.comparingByKey(comparatorLex)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));

        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        sortedMap = sortedMap.entrySet().stream().
                sorted(Map.Entry.comparingByValue(comparator)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));

        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            top.put(entry.getKey(), entry.getValue());

            if (top.size() == Constants.MAX_RESULTS) {
                break;
            }
        }

        return top;
    }

    /**
     * Pay the artists listened
     */
    public void payArtists() {
        HashMap<String, HashMap<String, Integer>> userHistory = history.get(premium);

        int totalSongs = 0;
        for (Map.Entry<String, HashMap<String, Integer>> entry : userHistory.entrySet()) {
            for (Map.Entry<String, Integer> songsEntry : entry.getValue().entrySet()) {
                totalSongs += songsEntry.getValue();
            }
        }

        for (Map.Entry<String, HashMap<String, Integer>> entry : userHistory.entrySet()) {
            Artist artist = (Artist) UserManager.searchUser(entry.getKey());

            for (Map.Entry<String, Integer> songsEntry : entry.getValue().entrySet()) {
                double revenue = (credits / totalSongs) * songsEntry.getValue();

                if (artist.getSongsRevenue().containsKey(songsEntry.getKey())) {
                    artist.getSongsRevenue().put(songsEntry.getKey(),
                            artist.getSongsRevenue().get(songsEntry.getKey()) + revenue);
                } else {
                    artist.getSongsRevenue().put(songsEntry.getKey(), revenue);
                }
            }
        }
        userHistory.clear();
    }

    /**
     * Get recommended songs as string
     * @return string of recommended songs' names
     */
    public final String getRecommendedSongsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Song song : recommendedSongs) {
            stringBuilder.append(song.getName());
            stringBuilder.append(", ");
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Get recommended playlist as string
     * @return string of playlist name
     */
    public final String getRecommendedPlaylistAsString() {
        return recommendedPlaylists.isEmpty() ? "" : recommendedPlaylists.get(0).getName();
    }

    /**
     * Add the notification to the list of notifications
     * @param notification the notification received
     */
    @Override
    public void update(final Notification notification) {
        notifications.add(notification);
    }
}
