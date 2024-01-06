package audioplayer.commands;

import audioplayer.AudioPlayer;
import audioplayer.Library;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Playlist;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.AudioPlayerCommand;
import commandinput.CommandInput;
import constants.Constants;

public final class Follow extends AudioPlayerCommand {
    @Override
    public void execute(final AudioPlayer audioPlayer, final CommandInput commandInput,
                        final ObjectNode output) {
        AudioEntity selectedAudioEntity = audioPlayer.getSelectedAudioEntity();

        if (selectedAudioEntity == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_SOURCE_SELECTED_FOLLOW);
        } else if (selectedAudioEntity.isPodcast() || selectedAudioEntity.isSong()) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.SELECTED_SOURCE_NOT_A_PLAYLIST);
        } else if (hasPlaylist(audioPlayer, (Playlist) selectedAudioEntity)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.OWN_PLAYLIST_FOLLOW_ERROR);
        } else {
            Playlist playlist = ((Playlist) selectedAudioEntity);
            if (audioPlayer.isFollowingPlaylist(playlist)) {
                playlist.updateFollowers(-Constants.FOLLOWER);
                audioPlayer.removePlaylistFromFollowedPlaylists(playlist);

                Library.getPlaylist(playlist).updateFollowers(-1);
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_UNFOLLOW);
            } else {
                playlist.updateFollowers(Constants.FOLLOWER);
                audioPlayer.getFollowedPlaylists().add(playlist);

                Library.getPlaylist(playlist).updateFollowers(+1);
                output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_FOLLOW);
            }
        }
    }

    private boolean hasPlaylist(final AudioPlayer audioPlayer, final Playlist playlist) {
        for (Playlist p: audioPlayer.getPlaylists()) {
            if (p.equals(playlist)) {
                return true;
            }
        }
        return false;
    }
}
