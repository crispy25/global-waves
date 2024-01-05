package host;

import audioplayer.Library;
import audioplayer.audiomodels.AudioEntity;
import audioplayer.audiomodels.audiocollections.Podcast;
import audioplayer.audiomodels.audiofiles.AudioFile;
import audioplayer.audiomodels.audiofiles.Episode;
import constants.Constants;
import fileio.input.EpisodeInput;
import fileio.input.UserInput;
import lombok.Getter;
import pagemanager.HostPage;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
public final class Host extends User {

    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<Announcement> announcements = new ArrayList<>();

    private final LinkedHashMap<String, Integer> episodesListens = new LinkedHashMap<>();
    private final LinkedHashMap<String, Integer> fans = new LinkedHashMap<>();

    public Host(final String username, final int age, final String city) {
        userInput = new UserInput();

        userInput.setUsername(username);
        userInput.setAge(age);
        userInput.setCity(city);
        this.type = Constants.HOST_USER;

        HostPage hostPage = new HostPage();
        hostPage.setHost(this);
        hostPage.setOwner(username);
        pages.put(Constants.HOST_PAGE, hostPage);
    }

    @Override
    public LinkedHashMap<String, Object> getStatistics() {
        LinkedHashMap<String, Object> statistics = new LinkedHashMap<>();

        if (episodesListens.isEmpty()) {
            return null;
        }

        statistics.put(Constants.TOP_EPISODES, getTop5(episodesListens));
        statistics.put(Constants.LISTENERS, fans.size());

        return statistics;
    }

    /**
     * Returns true if host has a podcast with the given name
     * @param podcastName the podcast name to search for
     * @return true if host has a podcast the with given name, false otherwise
     */
    public boolean hasPodcast(final String podcastName) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(podcastName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if host has an announcement with the given name
     * @param announcementName the announcement name to search for
     * @return true if host has an announcement the with given name, false otherwise
     */
    public boolean hasAnnouncement(final String announcementName) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(announcementName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if episodes does not contain duplicates
     * @param episodes the list of episodes
     * @return true if podcast can be created from the list of episodes, false otherwise
     */
    public boolean canAddPodcast(final ArrayList<EpisodeInput> episodes) {
        HashMap<String, Integer> episodeOccurrence = new HashMap<>();
        for (EpisodeInput episodeInput : episodes) {
            if (episodeOccurrence.put(episodeInput.getName(), 1) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove the announcement with the given name
     * @param announcementName the announcement name
     */
    public void removeAnnouncement(final String announcementName) {
        announcements.removeIf(announcement -> announcement.getName().equals(announcementName));
    }

    /**
     * Check if user has any relation at the current time with entities of other users
     * @param timestamp current time
     * @return true if user can be deleted, false otherwise
     */
    public boolean canBeDeleted(final int timestamp) {
        for (Podcast podcast : podcasts) {
            for (User user : UserManager.getUsersByType(Constants.NORMAL_USER)) {
                user.getAudioPlayer().updateCurrentlyPlayingAudio(timestamp);
                AudioEntity currentlyPlaying = user.getAudioPlayer().getCurrentlyPlaying();

                if (user.getCurrentPage().getOwner().equals(getName())) {
                    return false;
                }

                if (currentlyPlaying == null) {
                    continue;
                }

                if (podcast.getName().equals(currentlyPlaying.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Remove all user audio entities from library
     */
    public void removeUserData() {
        for (Podcast podcast : podcasts) {
            Library.getPodcasts().remove(podcast);
        }
    }

    /**
     * Search for the podcast with the given name
     * @param podcastName the podcast name
     * @return the podcast if it was found, null otherwise
     */
    public Podcast getPodcastWithName(final String podcastName) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(podcastName)) {
                return podcast;
            }
        }
        return null;
    }

    /**
     * Checks if the podcast with the given name can be deleted
     * @param podcastName the podcast name
     * @param timestamp current time
     * @return true if podcast can be deleted, false otherwise
     */
    public boolean canDeletePodcast(final String podcastName, final int timestamp) {
        Podcast podcast = getPodcastWithName(podcastName);

        for (User user : UserManager.getUsersByType(Constants.NORMAL_USER)) {
            user.getAudioPlayer().updateCurrentlyPlayingAudio(timestamp);
            AudioEntity currentlyPlaying = user.getAudioPlayer().getCurrentlyPlaying();

            if (currentlyPlaying == null) {
                continue;
            }

            if (podcast.getName().equals(currentlyPlaying.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create a string with all the podcasts names and their episodes
     * @return the created string
     */
    public String getPodcastsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Podcast podcast : podcasts) {
            stringBuilder.append(podcast);
            stringBuilder.append(":\n\t[");
            for (AudioFile audioFile : podcast.getAudioFiles()) {
                stringBuilder.append(audioFile.getName()).
                                append(" - ").
                                append(((Episode) audioFile).getEpisodeInput().
                                                                getDescription()).append(", ");
            }

            if (!podcast.getAudioFiles().isEmpty()) {
                stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
            }

            stringBuilder.append("]\n");
            stringBuilder.append(", ");
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Create a string with all the announcements names
     * @return the created string
     */
    public String getAnnouncementsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Announcement announcement : announcements) {
            stringBuilder.append(announcement);
            stringBuilder.append(", ");
        }

        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.lastIndexOf(", "), stringBuilder.length());
        }

        return stringBuilder.toString();
    }
}
