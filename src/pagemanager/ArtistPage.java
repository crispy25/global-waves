package pagemanager;

import lombok.Getter;
import lombok.Setter;
import artist.Artist;
import usermanager.User;

@Setter
@Getter
public final class ArtistPage extends Page {

    private final String contentTemplate = "Albums:\n\t[%s]\n\nMerch:\n\t[%s]\n\nEvents:\n\t[%s]";
    private Artist artist;

    @Override
    public String acceptVisit(final User user) {
        return contentTemplate.formatted(artist.getAlbumsAsString(),
                                            artist.getMerchAsString(),
                                                artist.getEventsAsString());
    }
}
