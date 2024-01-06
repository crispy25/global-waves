package admin;

import audioplayer.audiomodels.audiocollections.Album;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import artist.Artist;
import usermanager.UserManager;

import java.util.ArrayList;

public final class ShowAlbums implements ICommand {
    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        Artist artist = (Artist) UserManager.searchUser(commandInput.getUsername());
        assert artist != null;

        record AlbumInfo(String name, ArrayList<String> songs) {
        }

        ArrayList<AlbumInfo> result = new ArrayList<>();
        for (Album album : artist.getAlbums()) {
            ArrayList<String> songs = new ArrayList<>();
            album.getAudioFiles().forEach(audioFile -> songs.add(audioFile.getName()));

            result.add(new AlbumInfo(album.getName(), songs));
        }

        output.put(Constants.NORMAL_USER, commandInput.getUsername());
        output.putPOJO(Constants.OUTPUT_RESULT, result);
    }
}
