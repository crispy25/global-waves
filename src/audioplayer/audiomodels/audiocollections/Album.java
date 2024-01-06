package audioplayer.audiomodels.audiocollections;

import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiofiles.AudioFile;
import audioplayer.audiomodels.audiofiles.Song;
import commandinput.CommandInput;
import commandinput.Filter;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public final class Album extends Playlist {

    private int releaseYear;
    private String description;

    @Override
    public AudioEntity getAudioEntityCopy() {
        Album album = new Album();
        album.setName(name);
        album.setOwner(owner);
        album.setDescription(description);
        album.setReleaseYear(releaseYear);

        for (AudioFile audioFile : audioFiles) {
            album.getAudioFiles().add((AudioFile) audioFile.getAudioEntityCopy());
        }
        album.setUser(user);

        return album;
    }

    @Override
    public boolean filter(final Filter filters) {

        if (filters.getName() != null
                && !name.toLowerCase().startsWith(filters.getName().toLowerCase())) {
            return false;
        }
        if (filters.getOwner() != null
                && !owner.toLowerCase().startsWith(filters.getOwner().toLowerCase())) {
            return false;
        }
        if (filters.getDescription() != null
                && !description.toLowerCase().startsWith(filters.getDescription().toLowerCase())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public Album(final CommandInput commandInput) {
        name = commandInput.getName();
        owner = commandInput.getUsername();
        releaseYear = commandInput.getReleaseYear();
        description = commandInput.getDescription();

        for (SongInput songInput : commandInput.getSongs()) {
            Song newSong = new Song(songInput);
            newSong.setAudioCreator(owner);
            audioFiles.add(newSong);
        }
    }

    public Album() {
    }

    /**
     * Checks if given list of songs does not contain duplicates
     * @param songs list of songs
     * @return true if album can be created from the list of songs, false otherwise
     */
    public static boolean canAddAlbum(final ArrayList<SongInput> songs) {
        HashMap<String, Integer> songOccurrences = new HashMap<>();
        for (SongInput song : songs) {
            if (songOccurrences.put(song.getName(), 1) != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isAlbum() {
        return true;
    }
}
