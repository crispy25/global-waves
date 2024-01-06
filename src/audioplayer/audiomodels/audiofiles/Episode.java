package audioplayer.audiomodels.audiofiles;

import audioplayer.audiomodels.AudioEntity;
import commandinput.Filter;
import fileio.input.EpisodeInput;
import host.Host;
import lombok.Getter;
import usermanager.UserManager;

@Getter
public final class Episode extends AudioFile {

    private final EpisodeInput episodeInput;

    @Override
    public void updateAudioCreatorListens() {
        Host host = (Host) UserManager.searchUser(audioCreator);

        // add host user if not existing in library
        if (host == null) {
            host = new Host(audioCreator, 0, null);
            UserManager.getUsers().add(host);
        }

        if (host.getEpisodesListens().containsKey(getName())) {
            host.getEpisodesListens().put(getName(), host.getEpisodesListens().get(getName()) + 1);
        } else {
            host.getEpisodesListens().put(getName(), 1);
        }

        if (host.getFans().containsKey(user.getName())) {
            host.getFans().put(user.getName(), host.getFans().get(user.getName()) + 1);
        } else {
            host.getFans().put(user.getName(), 1);
        }
    }

    @Override
    public void updateUserListens() {
        if (user.getEpisodesListened().containsKey(getName())) {
            user.getEpisodesListened().put(getName(),
                    user.getEpisodesListened().get(getName()) + 1);
        } else {
            user.getEpisodesListened().put(getName(), 1);
        }

        updateAudioCreatorListens();
    }

    public Episode(final Episode episode) {
        episodeInput = episode.episodeInput;
        user = episode.getUser();
        audioCreator = episode.audioCreator;
    }

    public Episode(final EpisodeInput episodeInput) {
        this.episodeInput = episodeInput;
    }

    @Override
    public AudioEntity getAudioEntityCopy() {
        Episode episodeCopy = new Episode(episodeInput);
        episodeCopy.setAudioCreator(audioCreator);
        episodeCopy.setUser(user);
        return episodeCopy;
    }

    @Override
    public void startAudio(final int timestamp) {
        lastUpdateTime = timestamp;
        remainedTime = episodeInput.getDuration();
        playing = true;

        updateUserListens();
    }
    @Override
    public boolean filter(final Filter filters) {
        return false;
    }

    @Override
    public String getName() {
        return episodeInput.getName();
    }

    public int getDuration() {
        return episodeInput.getDuration();
    }

}
