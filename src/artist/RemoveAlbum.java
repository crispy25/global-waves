package artist;

import audioplayer.Library;
import audioplayer.audiomodels.audiocollections.Album;
import audioplayer.audiomodels.audiofiles.AudioFile;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class RemoveAlbum implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, String.format(Constants.USER_NON_EXISTENT_MESSAGE,
                    commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.ARTIST_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_AN_ARTIST.
                                                    formatted(commandInput.getUsername()));
        } else if (!((Artist) user).hasAlbum(commandInput.getName())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_ALBUM_WITH_NAME.
                                                    formatted(commandInput.getUsername()));
        } else if (!((Artist) user).canDeleteAlbum(commandInput.getName(),
                                                        commandInput.getTimestamp())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.DELETE_ALBUM_ERROR.
                                                    formatted(commandInput.getUsername()));
        } else {
            Album album = ((Artist) user).getAlbumWithName(commandInput.getName());

            for (AudioFile audioFile : album.getAudioFiles()) {
                Library.removeSong(audioFile.toString());
            }

            ((Artist) user).getAlbums().remove(album);
            Library.getAlbums().remove(album);

            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_DELETE_ALBUM.
                                                    formatted(commandInput.getUsername()));
        }
    }
}
