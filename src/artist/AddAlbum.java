package artist;

import audioplayer.Library;
import audioplayer.audiomodels.audiocollections.Album;
import audioplayer.audiomodels.audiofiles.AudioFile;
import audioplayer.audiomodels.audiofiles.Song;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import notifications.Notification;
import usermanager.User;
import usermanager.UserManager;

public final class AddAlbum implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());
        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                                                    formatted(commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.ARTIST_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_AN_ARTIST.
                                                    formatted(commandInput.getUsername()));
        } else if (((Artist) user).hasAlbum(commandInput.getName())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.SAME_ALBUM_NAME.
                                                    formatted(commandInput.getUsername()));
        } else if (!Album.canAddAlbum(commandInput.getSongs())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.SAME_SONG_NAME_IN_ALBUM.
                                                    formatted(commandInput.getUsername()));
        } else {
            Album album = new Album(commandInput);
            ((Artist) user).getAlbums().add(album);
            Library.getAlbums().add(album);

            for (AudioFile audioFile : album.getAudioFiles()) {
                Library.getSongs().add((Song) audioFile);
                Library.getSongsLikes().put(((Song) audioFile).toString(), 0);
            }

            // notify subscribers
            user.getSubscriberManager().notifySubscribers(
                    new Notification(Constants.NEW_ALBUM, Constants.NEW_ALBUM
                            + Constants.FROM_CONTENT_CREATOR.formatted(user.getName())));

            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_ADD_ALBUM.
                                                    formatted(commandInput.getUsername()));
        }
    }
}
