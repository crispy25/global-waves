package artist;

import audioplayer.Library;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Album;
import audioplayer.audiomodels.audiocollections.Playlist;
import audioplayer.audiomodels.audiofiles.AudioFile;
import audioplayer.audiomodels.audiofiles.Song;
import constants.Constants;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import pagemanager.ArtistPage;
import searchbar.SearchBar;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Getter
@Setter
public final class Artist extends User {

    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Merch> merchandise = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();

    private final LinkedHashMap<String, Integer> albumsListens = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> songsListens = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> fans = new LinkedHashMap<>();

    private final LinkedHashMap<String, Double> songsRevenue = new LinkedHashMap<>();
    private double merchRevenue = 0;

    public Artist(final String username, final int age, final String city) {
        userInput = new UserInput();
        searchBar = new SearchBar();

        userInput.setUsername(username);
        userInput.setAge(age);
        userInput.setCity(city);
        this.type = Constants.ARTIST_USER;

        ArtistPage artistPage = new ArtistPage();
        artistPage.setArtist(this);
        artistPage.setOwner(username);
        pages.put(Constants.ARTIST_PAGE, artistPage);
    }

    /**
     * Get audio statistics for artist
     * @return linked hash map with statistics for artist
     */
    public LinkedHashMap<String, Object> getStatistics() {
        if (songsListens.isEmpty() && albumsListens.isEmpty()) {
            return null;
        }

        LinkedHashMap<String, Object> statistics = new LinkedHashMap<>();

        statistics.put(Constants.TOP_ALBUMS, getTop5(albumsListens));
        statistics.put(Constants.TOP_SONGS, getTop5(songsListens));

        ArrayList<String> sortedFans = new ArrayList<>(getTop5(fans).keySet());

        statistics.put(Constants.TOP_FANS, sortedFans);
        statistics.put(Constants.LISTENERS, fans.size());

        return statistics;
    }

    /**
     * Returns true if artist has album with the given name
     * @param albumName the album name to search for
     * @return true if artist has album with the given name, false otherwise
     */
    public boolean hasAlbum(final String albumName) {
        return albums.stream().anyMatch(album -> album.getName().equals(albumName));
    }

    /**
     * Returns true if artist has event with the given name
     * @param eventName the event name to search for
     * @return true if artist has event with the given name, false otherwise
     */
    public boolean hasEvent(final String eventName) {
        return events.stream().anyMatch(event -> event.getName().equals(eventName));
    }

    /**
     * Returns true if artist has merch with the given name
     * @param merchName the merch name to search for
     * @return true if artist has merch the with given name, false otherwise
     */
    public boolean hasMerch(final String merchName) {
        return merchandise.stream().anyMatch(merch -> merch.getName().equals(merchName));
    }

    /**
     * Returns the merch with the given name
     * @param merchName the merch name to search for
     * @return the merch if artist has merch the with given name, null otherwise
     */
    public Merch getMerch(final String merchName) {
        for (Merch m : merchandise) {
            if (m.getName().equals(merchName)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Remove event with the with given name
     * @param eventName the event name to search for
     */
    public void removeEvent(final String eventName) {
        events.removeIf(event -> event.getName().equals(eventName));
    }

    /**
     * Create a string with all the album names
     * @return the created string
     */
    public String getAlbumsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Album album : albums) {
            stringBuilder.append(album);
            stringBuilder.append(", ");
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Create a string with all the merch names
     * @return the created string
     */
    public String getMerchAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Merch merch : merchandise) {
            stringBuilder.append(merch);
            stringBuilder.append(", ");
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Create a string with all the event names
     * @return the created string
     */
    public String getEventsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Event event : events) {
            stringBuilder.append(event);
            stringBuilder.append(", ");
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Check if user has any relation at the current time with entities of other users
     * @param timestamp current time
     * @return true if user can be deleted, false otherwise
     */
    public boolean canBeDeleted(final int timestamp) {
        for (Album album : albums) {
            for (User user : UserManager.getUsers()) {
                if (user.getType().equals(Constants.NORMAL_USER)) {
                    user.getAudioPlayer().updateCurrentlyPlayingAudio(timestamp);
                    AudioEntity currentlyPlaying = user.getAudioPlayer().getCurrentlyPlaying();

                    if (user.getCurrentPage().getOwner().equals(getName())) {
                        return false;
                    }

                    if (currentlyPlaying == null) {
                        continue;
                    }
                    if (album.hasAudioFileWithName(currentlyPlaying.getCurrentAudio().getName())) {
                        return false;
                    }
                    if (album.equals(currentlyPlaying)) {
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
        albums.forEach(this::removeAlbum);

    }

    private void removeAlbum(final Album album) {
        for (AudioFile audioFile : album.getAudioFiles()) {
            for (Song song : Library.getSongs()) {
                if (song.toString().equals(((Song) audioFile).toString())) {
                    Library.getSongsLikes().remove(song.toString());
                    Library.getSongs().remove(song);
                    break;
                }
            }
        }
        Library.getAlbums().remove(album);
    }

    /**
     * Search for the album with the given name
     * @param albumName the album name
     * @return the album if it was found, null otherwise
     */
    public Album getAlbumWithName(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Checks if the album with the given name can be deleted
     * @param albumName the album name
     * @param timestamp current time
     * @return true if album can be deleted, false otherwise
     */
    public boolean canDeleteAlbum(final String albumName, final int timestamp) {
        Album album = getAlbumWithName(albumName);

        for (User user : UserManager.getUsersByType(Constants.NORMAL_USER)) {
            if (userHasSongFromAlbum(user, album)) {
                return false;
            }

            user.getAudioPlayer().updateCurrentlyPlayingAudio(timestamp);
            AudioEntity currentlyPlaying = user.getAudioPlayer().getCurrentlyPlaying();

            if (currentlyPlaying == null) {
                continue;
            }

//            if (album.hasAudioFileWithName(currentlyPlaying.getCurrentAudio().getName())) {
//                return false;
//            }

            if (currentlyPlaying.getCurrentAudio().isSong()
                    && ((Song) currentlyPlaying.getCurrentAudio()).getAlbum().equals(albumName)) {
                return false;
            }
        }
        return true;
    }

    private boolean userHasSongFromAlbum(final User user, final Album album) {
        for (Playlist playlist : user.getAudioPlayer().getPlaylists()) {
            for (AudioFile audioFile : playlist.getAudioFiles()) {
                if (album.hasAudioFileWithName(audioFile.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
