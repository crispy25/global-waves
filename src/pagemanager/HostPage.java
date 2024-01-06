package pagemanager;

import lombok.Getter;
import lombok.Setter;
import host.Host;
import usermanager.User;
@Setter
@Getter
public final class HostPage extends Page {

    private final String contentTemplate = "Podcasts:\n\t[%s]\n\nAnnouncements:\n\t[%s]";
    private Host host;

    @Override
    public String acceptVisit(final User user) {
        return contentTemplate.formatted(host.getPodcastsAsString(),
                                            host.getAnnouncementsAsString());
    }
}
