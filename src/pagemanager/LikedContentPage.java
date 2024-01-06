package pagemanager;

import usermanager.User;

public final class LikedContentPage extends Page {

    private final String contentTemplate =
            "Liked songs:\n\t[%s]\n\nFollowed playlists:\n\t[%s]";
    @Override
    public String acceptVisit(final User user) {
        user.updateAudioPlayer();
        return contentTemplate.formatted(user.getAudioPlayer().getPlaylistSongNames(
                                            user.getAudioPlayer().getLikedSongs()),
                                                user.getAudioPlayer().getFollowedPlaylistNames());
    }


}
