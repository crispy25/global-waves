package pagemanager;

import lombok.Getter;
import lombok.Setter;
import usermanager.User;

@Getter
@Setter
public final class HomePage extends Page {

    private final String contentTemplate = """
            Liked songs:
            \t[%s]

            Followed playlists:
            \t[%s]

            Song recommendations:
            \t[%s]

            Playlists recommendations:
            \t[%s]""";

    @Override
    public String acceptVisit(final User user) {
        user.updateAudioPlayer();
        return contentTemplate.formatted(user.getAudioPlayer().getTop5SongsLiked(),
                                            user.getAudioPlayer().getTop5PlaylistsFollowed(),
                                                user.getRecommendedSongsAsString(),
                                                    user.getRecommendedPlaylistAsString());
    }
}
