package audioplayer.audiomodels.audiofiles;

import artist.Artist;
import audioplayer.audiomodels.AudioEntity;
import commandinput.Filter;
import constants.Constants;
import fileio.input.SongInput;
import lombok.Getter;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public final class Song extends AudioFile {

    private final SongInput songInput;

    @Override
    public void updateAudioCreatorListens() {
        Artist artist = (Artist) UserManager.searchUser(getArtist());

        // add artist user if not existing
        if (artist == null) {
            artist = new Artist(songInput.getArtist(), 0, null);
            UserManager.getUsers().add(artist);
        }

        if (artist.getAlbumsListens().containsKey(getAlbum())) {
            artist.getAlbumsListens().put(getAlbum(),
                    artist.getAlbumsListens().get(getAlbum()) + 1);
        } else {
            artist.getAlbumsListens().put(getAlbum(), 1);
        }

        if (artist.getSongsListens().containsKey(getName())) {
            artist.getSongsListens().put(getName(), artist.getSongsListens().get(getName()) + 1);
        } else {
            artist.getSongsListens().put(getName(), 1);
        }

        if (artist.getFans().containsKey(user.getName())) {
            artist.getFans().put(user.getName(), artist.getFans().get(user.getName()) + 1);
        } else {
            artist.getFans().put(user.getName(), 1);
        }
    }

    @Override
    public void updateUserListens() {
        if (user.getSongsListened().containsKey(getName())) {
            user.getSongsListened().put(getName(),
                    user.getSongsListened().get(getName()) + 1);
        } else {
            user.getSongsListened().put(getName(), 1);
        }

        if (user.getGenresListened().containsKey(getGenre())) {
            user.getGenresListened().put(getGenre(),
                    user.getGenresListened().get(getGenre()) + 1);
        } else {
            user.getGenresListened().put(getGenre(), 1);
        }

        if (user.getArtistsListened().containsKey(getArtist())) {
            user.getArtistsListened().put(getArtist(),
                    user.getArtistsListened().get(getArtist()) + 1);
        } else {
            user.getArtistsListened().put(getArtist(), 1);
        }

        if (user.getAlbumsListened().containsKey(getAlbum())) {
            user.getAlbumsListened().put(getAlbum(),
                    user.getAlbumsListened().get(getAlbum()) + 1);
        } else {
            user.getAlbumsListened().put(getAlbum(), 1);
        }

        // update user history
        if (user.getHistory().get(user.isPremium()).containsKey((getArtist()))) {
            if (user.getHistory().get(user.isPremium()).get(getArtist()).containsKey(getName())) {
                user.getHistory().get(user.isPremium()).get(getArtist()).put(getName(),
                        user.getHistory().get(user.isPremium()).
                                get(getArtist()).get(getName()) + 1);
            } else {
                user.getHistory().get(user.isPremium()).get(getArtist()).put(getName(), 1);
            }
        } else {
            user.getHistory().get(user.isPremium()).put(getArtist(), new HashMap<>());
            user.getHistory().get(user.isPremium()).get(getArtist()).put(getName(), 1);
        }

        updateAudioCreatorListens();
    }

    @Override
    public AudioEntity getAudioEntityCopy() {
        Song songCopy = new Song(songInput);
        songCopy.setUser(user);
        songCopy.setAudioCreator(getArtist());
        return songCopy;
    }

    @Override
    public String toString() {
        return getName() + Constants.SEPARATOR + getArtist() + Constants.SEPARATOR + getAlbum();
    }

    @Override
    public void startAudio(final int timestamp) {
        lastUpdateTime = timestamp;
        remainedTime = getDuration();
        playing = true;
        playedTwice = false;

        updateUserListens();
    }

    @Override
    public boolean isSong() {
        return true;
    }

    public Song(final SongInput songInput) {
        this.songInput = songInput;
        remainedTime = songInput.getDuration();
        audioCreator = songInput.getArtist();
    }

    public Song(final Song song) {
        songInput = song.getSongInput();
        user = song.getUser();
        audioCreator = song.getAudioCreator();
    }
    @Override
    public int getRemainedTime() {
        return remainedTime;
    }
    @Override
    public boolean filter(final Filter filters) {
        if (filters.getName() != null && !songInput.getName().toLowerCase().
                                            startsWith(filters.getName().toLowerCase())) {
            return false;
        }
        if (filters.getAlbum() != null && !songInput.getAlbum().
                                           equalsIgnoreCase(filters.getAlbum())) {
            return false;
        }
        if (filters.getTags() != null && !checkTags(filters.getTags())) {
            return false;
        }
        if (filters.getLyrics() != null
                && !songInput.getLyrics().toLowerCase().
                    contains(filters.getLyrics().toLowerCase())) {
            return false;
        }
        if (filters.getGenre() != null && !songInput.getGenre().
                                           equalsIgnoreCase(filters.getGenre())) {
            return false;
        }
        if (filters.getReleaseYear() != null && !checkReleaseYear(filters.getReleaseYear())) {
            return false;
        }
        if (filters.getArtist() != null && !songInput.getArtist().
                                            equalsIgnoreCase(filters.getArtist())) {
            return false;
        }
        return true;
    }
    private boolean checkReleaseYear(final String filterYear) {
        int filterReleaseYear = Integer.parseInt((String) filterYear.subSequence(1,
                                                            filterYear.length()));
        if (filterYear.charAt(0) == '>') {
            return songInput.getReleaseYear() > filterReleaseYear;
        } else {
            return songInput.getReleaseYear() < filterReleaseYear;
        }
    }
    private boolean checkTags(final ArrayList<String> filterTags) {
        for (String tag: filterTags) {
            if (!songInput.getTags().contains(tag)) {
                return false;
            }
        }
        return true;
    }

    public int getDuration() {
        return songInput.getDuration();
    }

    public String getName() {
        return songInput.getName();
    }

    public String getAlbum() {
        return songInput.getAlbum();
    }

    public String getLyrics() {
        return songInput.getLyrics();
    }

    public String getGenre() {
        return songInput.getGenre();
    }

    public String getArtist() {
        return songInput.getArtist();
    }

    public int getReleaseYear() {
        return songInput.getReleaseYear();
    }

    public ArrayList<String> getTags() {
        return songInput.getTags();
    }
}
