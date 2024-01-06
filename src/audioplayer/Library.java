package audioplayer;

import artist.Artist;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Album;
import audioplayer.audiomodels.audiocollections.Playlist;
import audioplayer.audiomodels.audiocollections.Podcast;
import audioplayer.audiomodels.audiofiles.Song;
import constants.Constants;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public final class Library {
    @Getter
    private static ArrayList<Song> songs = new ArrayList<>();
    @Getter
    private static ArrayList<Podcast> podcasts = new ArrayList<>();
    @Getter
    private static ArrayList<Playlist> playlists = new ArrayList<>();
    @Getter
    private static ArrayList<Album> albums = new ArrayList<>();
    @Getter
    private static LinkedHashMap<String, Integer> songsLikes = new LinkedHashMap<>();
    @Getter
    private static LinkedHashMap<Playlist, Integer> playlistLikes = new LinkedHashMap<>();

    private Library() {
    }

    /**
     * Initialize the library
     * @param libraryInput the library input
     */
    public static void initialize(final LibraryInput libraryInput) {
        songs.clear();
        podcasts.clear();
        playlists.clear();
        albums.clear();

        songsLikes.clear();
        playlistLikes.clear();

        for (SongInput song: libraryInput.getSongs()) {
            Song newSong = new Song(song);
            songs.add(newSong);
            songsLikes.put(newSong.toString(), 0);
        }

        for (PodcastInput podcast : libraryInput.getPodcasts()) {
            podcasts.add(new Podcast(podcast));
        }
    }

    /**
     * Update the total amount of likes for each playlist
     */
    public static void updatePlaylistsLikes() {
        playlistLikes.replaceAll((k, v) -> 0);

        for (Map.Entry<String, Integer> entry : songsLikes.entrySet()) {
            for (Map.Entry<Playlist, Integer> playlistEntry : playlistLikes.entrySet()) {
                if (playlistEntry.getKey().hasAudioFileWithName(entry.getKey())) {
                    playlistLikes.put(playlistEntry.getKey(),
                            playlistEntry.getValue() + entry.getValue());
                }
            }
        }
    }

    /**
     * @param type type of the audio entity
     * @return audio entities of the type given
     */
    public static ArrayList<AudioEntity> getAudioEntitiesByType(final String type) {
        switch (type) {
            case "song":
               return new ArrayList<>(songs);
            case "podcast":
                return new ArrayList<>(podcasts);
            case "playlist":
                return new ArrayList<>(playlists);
            default:
                // return albums sorted
                ArrayList<AudioEntity> albumsSorted = new ArrayList<>();
                for (User user : UserManager.getUsersByType(Constants.ARTIST_USER)) {
                    albumsSorted.addAll(((Artist) user).getAlbums());
                }
                return albumsSorted;
        }
    }

    /**
     * Removes song with given name from library
     * @param songName the name of the song to be removed
     */
    public static void removeSong(final String songName) {
        for (Song song : songs) {
            if (song.toString().equals(songName)) {
                songs.remove(song);
                songsLikes.remove(song.toString());
                break;
            }
        }
    }

    /**
     * Checks if the library has the given playlist
     * @param playlist the playlist
     * @return true if the library has the playlist, false otherwise
     */
    public static boolean hasPlaylist(final Playlist playlist) {
        return playlists.stream().anyMatch(p -> p.equals(playlist));
    }

    /**
     * Search for playlist in the library
     * @param playlist the playlist to be searched for
     * @return the playlist if the library has the playlist, null otherwise
     */
    public static Playlist getPlaylist(final Playlist playlist) {
        for (Playlist p : playlists) {
            if (p.equals(playlist)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get songs filtered by genre
     * @param genre the genre to search for
     * @return list of songs with the given genre
     */
    public static ArrayList<Song> getSongsByGenre(final String genre) {
        ArrayList<Song> songsByGenre = new ArrayList<>();
        for (Song song : songs) {
            if (song.getGenre().equals(genre)) {
                songsByGenre.add(song);
            }
        }
        return songsByGenre;
    }
}
