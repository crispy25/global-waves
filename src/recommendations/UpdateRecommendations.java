package recommendations;

import artist.Artist;
import audioplayer.Library;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Playlist;
import audioplayer.audiomodels.audiofiles.AudioFile;
import audioplayer.audiomodels.audiofiles.Song;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Comparator;
import java.util.stream.Collectors;

public final class UpdateRecommendations implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_USER_DATA.
                    formatted(Constants.NORMAL_USER, commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.NORMAL_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_NORMAL_USER.
                                                        formatted(commandInput.getUsername()));
        } else {
            user.getAudioPlayer().updateCurrentlyPlayingAudio(commandInput.getTimestamp());

            boolean foundRecommendation = updateRecommendationsByType(user,
                                                            commandInput.getRecommendationType());

            if (foundRecommendation) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_UPDATE_RECOMMENDATIONS.
                        formatted(commandInput.getUsername()));
            } else {
                output.put(Constants.OUTPUT_MESSAGE, Constants.NO_RECOMMENDATIONS_FOUND);
            }
        }
    }

    private boolean updateRecommendationsByType(final User user, final String type) {
        AudioEntity currentlyPlaying = user.getAudioPlayer().getCurrentlyPlaying();

        if (currentlyPlaying != null) {
            switch (type) {
                case Constants.RANDOM_SONG -> {
                    return updateSongsRecommendations(user, (Song) currentlyPlaying);
                }
                case Constants.RANDOM_PLAYLIST -> {
                    return updateRandomPlaylistsRecommendations(user);
                }
                case Constants.FANS_PLAYLIST -> {
                    return updateFansPlaylistsRecommendations(user,
                            ((Song) currentlyPlaying).getArtist());
                }
                default -> {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean updateSongsRecommendations(final User user, final Song song) {
        int passedTime = song.getDuration() - song.getRemainedTime();
        if (passedTime >= Constants.MINIMUM_SECONDS) {
            ArrayList<Song> songs = Library.getSongsByGenre(song.getGenre());
            Random random = new Random(passedTime);
            Song randomSong = songs.get(random.nextInt(songs.size()));
            user.getRecommendedSongs().add(randomSong);

            user.setLastRecommendation(randomSong);
            return true;
        }
        return false;
    }

    private boolean updateRandomPlaylistsRecommendations(final User user) {
        boolean foundRecommendation = false;
        Playlist playlist = new Playlist();
        LinkedHashMap<Song, Integer> songsLikes = new LinkedHashMap<>();
        ArrayList<AudioFile> songs = getUserFavouriteSongs(user);

        for (AudioFile audioFile : songs) {
            songsLikes.put((Song) audioFile, Library.getSongsLikes().get(audioFile.toString()));
        }

        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        songsLikes = songsLikes.entrySet().stream().
                    sorted(Map.Entry.comparingByValue(comparator)).
                        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            ((integer, integer2) -> integer), LinkedHashMap::new));

        ArrayList<String> top3Genres = getTop3Genres(songs);
        int genreMaxResults = Constants.MAX_FIRST_GENRE;
        for (String genre : top3Genres) {
            ArrayList<Song> topSongsByGenre = new ArrayList<>();
            for (Map.Entry<Song, Integer> entry : songsLikes.entrySet()) {
                if (entry.getKey().getGenre().equals(genre)) {
                    topSongsByGenre.add(entry.getKey());
                }

                if (topSongsByGenre.size() == genreMaxResults) {
                    break;
                }
            }

            for (Song song : topSongsByGenre) {
                foundRecommendation = true;
                playlist.addRemoveSong(song);
            }
            genreMaxResults -= 2;
        }

        playlist.setName(Constants.USER_RECOMMENDATIONS.formatted(user.getName()));
        playlist.setOwner(user.getName());
        user.getRecommendedPlaylists().add(playlist);

        if (foundRecommendation) {
            user.setLastRecommendation(playlist);
        }

        return foundRecommendation;
    }

    private ArrayList<AudioFile> getUserFavouriteSongs(final User user) {

        ArrayList<AudioFile> songs = new ArrayList<>(user.getAudioPlayer().
                                                            getLikedSongs().getAudioFiles());

        for (Playlist playlist : user.getAudioPlayer().getPlaylists()) {
            songs.addAll(playlist.getAudioFiles());
        }

        for (Playlist playlist : user.getAudioPlayer().getFollowedPlaylists()) {
            songs.addAll(playlist.getAudioFiles());
        }

        return songs;
    }

    private ArrayList<String> getTop3Genres(final ArrayList<AudioFile> songs) {
        LinkedHashMap<String, Integer> songsGenres = new LinkedHashMap<>();

        for (AudioFile audioFile : songs) {
            if (songsGenres.containsKey(((Song) audioFile).getGenre())) {
                songsGenres.put(((Song) audioFile).getGenre(),
                        songsGenres.get(((Song) audioFile).getGenre()) + 1);
            } else {
                songsGenres.put(((Song) audioFile).getGenre(), 1);
            }
        }

        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        songsGenres = songsGenres.entrySet().stream().
                sorted(Map.Entry.comparingByValue(comparator)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));

        ArrayList<String> top3Genres = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : songsGenres.entrySet()) {
            top3Genres.add(entry.getKey());

            if (top3Genres.size() == Constants.MAX_GENRES_RESULTS) {
                break;
            }
        }

        return top3Genres;
    }

    private boolean updateFansPlaylistsRecommendations(final User user, final String artistName) {
        boolean foundRecommendation = false;
        Playlist playlist = new Playlist();
        Artist artist = (Artist) UserManager.searchUser(artistName);
        LinkedHashMap<String, Object> top5Fans = artist.getTop5(artist.getFans());

        for (String username : top5Fans.keySet()) {
            User u = UserManager.searchUser(username);

            for (AudioFile audioFile : getTop5LikedSongsByUser(u)) {
                foundRecommendation = true;
                playlist.addRemoveSong((Song) audioFile);
            }
        }

        playlist.setName(Constants.FANS_RECOMMENDATIONS.formatted(artistName));
        playlist.setOwner(user.getName());
        user.getRecommendedPlaylists().add(playlist);

        if (foundRecommendation) {
            user.setLastRecommendation(playlist);
        }

        return foundRecommendation;
    }

    private ArrayList<AudioFile> getTop5LikedSongsByUser(final User user) {
        LinkedHashMap<AudioFile, Integer> songsLikes = new LinkedHashMap<>();

        for (AudioFile audioFile : user.getAudioPlayer().getLikedSongs().getAudioFiles()) {
            songsLikes.put(audioFile, Library.getSongsLikes().get(audioFile.toString()));
        }

        LinkedHashMap<AudioFile, Integer> top5Songs = getTop5(songsLikes);

        return new ArrayList<>(top5Songs.keySet());
    }


    private <T> LinkedHashMap<T, Integer> getTop5(final LinkedHashMap<T, Integer> map) {
        LinkedHashMap<T, Integer> sortedMap;
        LinkedHashMap<T, Integer> top = new LinkedHashMap<>();

        Comparator<Integer> comparator = (o1, o2) -> o2 - o1;
        sortedMap = map.entrySet().stream().
                sorted(Map.Entry.comparingByValue(comparator)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));

        for (Map.Entry<T, Integer> entry : sortedMap.entrySet()) {
            top.put(entry.getKey(), entry.getValue());

            if (top.size() == Constants.MAX_RESULTS) {
                break;
            }
        }
        return top;
    }
}
