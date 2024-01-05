package constants;

public final class Constants {

    // Output message constants

    public static final String OUTPUT_MESSAGE = "message";
    public static final String OUTPUT_STATS = "stats";

    public static final String OUTPUT_RESULT = "result";
    public static final String OUTPUT_RESULTS = "results";
    public static final String OUTPUT_COMMAND = "command";
    public static final String OUTPUT_TIMESTAMP = "timestamp";
    public static final String OUTPUT_USER = "user";

    // Error messages
    public static final String NO_SEARCH_BEFORE_SELECT =
            "Please conduct a search before making a selection.";
    public static final String NO_SOURCE_SELECTED_LOAD =
            "Please select a source before attempting to load.";
    public static final String NO_SOURCE_LOADED_PLAY_PAUSE =
            "Please load a source before attempting to pause or resume playback.";
    public static final String NO_SOURCE_LOADED_ADD_REMOVE =
            "Please load a source before adding to or removing from the playlist.";
    public static final String NO_SOURCE_LOADED_LIKE =
            "Please load a source before liking or unliking.";
    public static final String NO_SOURCE_LOADED_REPEAT =
            "Please load a source before setting the repeat status.";
    public static final String NO_SOURCE_LOADED_SHUFFLE =
            "Please load a source before using the shuffle function.";
    public static final String NO_SOURCE_LOADED_FORWARD =
            "Please load a source before attempting to forward.";
    public static final String NO_SOURCE_LOADED_BACKWARD =
            "Please select a source before rewinding.";
    public static final String NO_SOURCE_LOADED_NEXT =
            "Please load a source before skipping to the next track.";
    public static final String NO_SOURCE_LOADED_PREV =
            "Please load a source before returning to the previous track.";
    public static final String NO_SOURCE_SELECTED_FOLLOW =
            "Please select a source before following or unfollowing.";
    public static final String NOT_A_SONG = "The loaded source is not a song.";
    public static final String LOADED_SOURCE_NOT_A_PLAYLIST =
            "The loaded source is not a playlist or an album.";
    public static final String SELECTED_SOURCE_NOT_A_PLAYLIST =
            "The selected source is not a playlist.";
    public static final String NOT_A_PODCAST = "The loaded source is not a podcast.";
    public static final String EMPTY_COLLECTION = "You can't load an empty audio collection!";
    public static final String PLAYLIST_WITH_SAME_NAME =
            "A playlist with the same name already exists.";
    public static final String PLAYLIST_NOT_EXISTS = "The specified playlist does not exist.";
    public static final String OWN_PLAYLIST_FOLLOW_ERROR =
            "You cannot follow or unfollow your own playlist.";
    public static final String INVALID_PLAYLIST_ID = "The specified playlist ID is too high.";
    public static final String INVALID_ID = "The selected ID is too high.";

    public static final String USERNAME_TAKEN_ERROR_MESSAGE = "The username %s is already taken.";
    public static final String USER_ADDED_MESSAGE = "The username %s has been added successfully.";
    public static final String USER_DELETED_MESSAGE = "%s was successfully deleted.";
    public static final String USER_CAN_NOT_BE_DELETED = "%s can't be deleted.";

    public static final String USER_NON_EXISTENT_MESSAGE = "The username %s doesn't exist.";
    public static final String NOT_A_NORMAL_USER = "%s is not a normal user.";
    public static final String NOT_AN_ARTIST = "%s is not an artist.";
    public static final String NOT_A_HOST = "%s is not a host.";


    // Success messages
    public static final String SUCCESSFUL_SEARCH = "Search returned ";
    public static final String SUCCESSFUL_SELECT = "Successfully selected ";
    public static final String SUCCESSFUL_LOAD = "Playback loaded successfully.";
    public static final String SUCCESSFUL_PAUSED = "Playback paused successfully.";
    public static final String SUCCESSFUL_PLAYED = "Playback resumed successfully.";
    public static final String SUCCESSFUL_ADD = "Successfully added to playlist.";
    public static final String SUCCESSFUL_REMOVE = "Successfully removed from playlist.";
    public static final String SUCCESSFUL_LIKE = "Like registered successfully.";
    public static final String SUCCESSFUL_UNLIKE = "Unlike registered successfully.";
    public static final String PLAYLIST_CREATED = "Playlist created successfully.";
    public static final String REPEAT_MODE_CHANGED = "Repeat mode changed to ";
    public static final String SHUFFLE_ACTIVATED = "Shuffle function activated successfully.";
    public static final String SHUFFLE_DEACTIVATED = "Shuffle function deactivated successfully.";
    public static final String SUCCESSFUL_FORWARD = "Skipped forward successfully.";
    public static final String SUCCESSFUL_BACKWARD = "Rewound successfully.";
    public static final String SUCCESSFUL_NEXT =
            "Skipped to next track successfully. The current track is ";
    public static final String SUCCESSFUL_PREV =
            "Returned to previous track successfully. The current track is ";
    public static final String SUCCESSFUL_FOLLOW = "Playlist followed successfully.";
    public static final String SUCCESSFUL_UNFOLLOW = "Playlist unfollowed successfully.";
    public static final String SUCCESSFUL_SWITCH_VISIBILITY =
            "Visibility status updated successfully to ";
    public static final String DOT = ".";
    public static final String RESULTS = " results";

    // Other
    public static final String PUBLIC = "public";
    public static final String PRIVATE = "private";
    public static final int REPEAT_MAX_STATES = 3;
    public static final int SKIP_SECONDS = 90;
    public static final int FOLLOWER = 1;
    public static final int MAX_RESULTS = 5;

    public static final String NORMAL_USER = "user";

    public static final String ARTIST_USER = "artist";
    public static final String HOST_USER = "host";
    public static final String SUCCESSFUL_STATUS_CHANGE = " has changed status successfully.";
    public static final String IS_OFFLINE = " is offline.";
    public static final String SUCCESSFUL_ADD_ALBUM = "%s has added new album successfully.";
    public static final String SAME_ALBUM_NAME = "%s has another album with the same name.";
    public static final String NO_ALBUM_WITH_NAME = "%s doesn't have an album with the given name.";
    public static final String DELETE_ALBUM_ERROR = "%s can't delete this album.";
    public static final String SUCCESSFUL_DELETE_ALBUM = "%s deleted the album successfully.";
    public static final String SAME_SONG_NAME_IN_ALBUM =
            "%s has the same song at least twice in this album.";
    public static final String SAME_MERCH_NAME = " has merchandise with the same name.";
    public static final String SAME_EVENT_NAME = " has another event with the same name.";
    public static final String NEGATIVE_PRICE = "Price for merchandise can not be negative.";
    public static final String SUCCESSFUL_ADD_MERCH = " has added new merchandise successfully.";
    public static final String SAME_PODCAST_NAME = "%s has another podcast with the same name.";
    public static final String NO_PODCAST_WITH_NAME =
            "%s doesn't have a podcast with the given name.";
    public static final String DELETE_PODCAST_ERROR = "%s can't delete this podcast.";
    public static final String SUCCESSFUL_DELETE_PODCAST = "%s deleted the podcast successfully.";
    public static final String SAME_EPISODE_ERROR = "%s has the same episode in this podcast.";
    public static final String SUCCESSFUL_ADD_PODCAST = "%s has added new podcast successfully.";
    public static final String SAME_ANNOUNCEMENT_NAME =
                                "%s has already added an announcement with this name.";
    public static final String NO_ANNOUNCEMENT_WITH_NAME =
                                "%s has no announcement with the given name.";
    public static final String SUCCESSFUL_ADD_ANNOUNCEMENT =
                                "%s has successfully added new announcement.";
    public static final String SUCCESSFUL_DELETE_ANNOUNCEMENT =
                                "%s has successfully deleted the announcement.";
    public static final String HOME = "Home";
    public static final String LIKED_CONTENT = "LikedContent";
    public static final String ARTIST_PAGE = "Artist Page";
    public static final String HOST_PAGE = "Host Page";
    public static final String SUCCESSFUL_PERSONAL_PAGE_ACCESS = "%s accessed %s successfully.";
    public static final String NON_EXISTENT_PAGE = "%s is trying to access a non-existent page.";
    public static final String INVALID_DATE = "Event for %s does not have a valid date.";
    public static final String SUCCESSFUL_ADD_EVENT = " has added new event successfully.";
    public static final String SUCCESSFUL_DELETED_EVENT = "%s deleted the event successfully.";
    public static final String NO_EVENT_WITH_NAME = "%s doesn't have an event with the given name.";

    // Date
    public static final int MONTHS = 12;
    public static final int REGULAR_MONTH_DAYS = 31;
    public static final int FEBRUARY_MONTH_DAYS = 28;
    public static final int FEBRUARY_MONTH = 2;
    public static final int CURRENT_YEAR = 2023;
    public static final int MINIMUM_YEAR = 1900;
    public static final String AUDIO = "audio";
    public static final String DATE_FORMAT = "..-..-....";
    public static final String HYPHEN = "-";
    public static final String SEPARATOR = ":";

    public static final String TOP_ARTISTS = "topArtists";
    public static final String TOP_GENRES = "topGenres";
    public static final String TOP_SONGS = "topSongs";
    public static final String TOP_ALBUMS = "topAlbums";
    public static final String TOP_FANS = "topFans";
    public static final String TOP_EPISODES = "topEpisodes";
    public static final String LISTENERS = "listeners";
    public static final String NO_USER_DATA = "No data to show for %s %s.";
    public static final String SONG_REVENUE = "songRevenue";
    public static final String MERCH_REVENUE = "merchRevenue";
    public static final String RANKING = "ranking";
    public static final String MOST_PROFITABLE_SONG = "mostProfitableSong";
    public static final String N_A = "N/A";
    public static final String NOTIFICATIONS = "notifications";
    public static final String SUBSCRIBED = "subscribed";
    public static final String UNSUBSCRIBED = "unsubscribed";
    public static final String MERCH_INVALID_PAGE = "Cannot buy merch from this page.";
    public static final String SUBSCRIBE_INVALID_PAGE =
            "To subscribe you need to be on the page of an artist or host.";
    public static final String SUCCESSFUL_SUBSCRIBE = "%s %s to %s successfully.";
    public static final String SUCCESSFUL_UNSUBSCRIBE = "%s %s from %s successfully.";
    public static final String NEW_ALBUM = "New Album";
    public static final String NEW_MERCH = "New Merchandise";
    public static final String NEW_EVENT = "New Event";
    public static final String NEW_PODCAST = "New Podcast";
    public static final String NEW_ANNOUNCEMENT = "New Announcement";
    public static final String FROM_CONTENT_CREATOR = " from %s.";
    public static final String INVALID_MERCH_NAME = "The merch %s doesn't exist.";
    public static final String SUCCESSFUL_BUY_MERCH = "%s has added new merch successfully.";
    public static final String USER_ALREADY_PREMIUM = "%s is already a premium user.";
    public static final String USER_NOT_PREMIUM = "%s is not a premium user.";
    public static final String SUCCESSFUL_BUY_PREMIUM = "%s bought the subscription successfully.";
    public static final String SUCCESSFUL_CANCEL_PREMIUM =
            "%s cancelled the subscription successfully.";
    public static final String SUCCESSFUL_AD_BREAK = "Ad inserted successfully.";
    public static final String USER_NOT_PLAYING_MUSIC = "%s is not playing any music.";
    public static final String NO_FORWARD_PAGES = "There are no pages left to go forward.";
    public static final String NO_BACK_PAGES = "There are no pages left to go back.";
    public static final String SUCCESSFUL_NEXT_PAGE =
            "The user %s has navigated successfully to the next page.";
    public static final String SUCCESSFUL_PREVIOUS_PAGE =
            "The user %s has navigated successfully to the previous page.";
    public static final String SUCCESSFUL_UPDATE_RECOMMENDATIONS =
            "The recommendations for user %s have been updated successfully.";
    public static final String NO_RECOMMENDATIONS_FOUND =
            "No new recommendations were found";
    public static final String NO_RECOMMENDATIONS_AVAILABLE =
            "No recommendations available.";
    public static final String RANDOM_SONG = "random_song";
    public static final String RANDOM_PLAYLIST = "random_playlist";
    public static final String FANS_PLAYLIST = "fans_playlist";
    public static final String USER_RECOMMENDATIONS = "%s's recommendations";
    public static final String FANS_RECOMMENDATIONS = "%s Fan Club recommendations";
    public static final boolean PREMIUM_USER = true;
    public static final double PREMIUM_CREDITS = 1000000;
    public static final int MINIMUM_SECONDS = 30;
    public static final int MAX_GENRES_RESULTS = 3;
    public static final int MAX_FIRST_GENRE = 5;
    public static final int AD_DURATION = 10;
    public static final int ONE_HUNDRED = 100;

    private Constants() {
    }
}
