package monetization;

import artist.Artist;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class EndProgram implements ICommand {

    public record ArtistInfo(double songRevenue, String mostProfitableSong, double merchRevenue) {
    }
    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {

        for (User user : UserManager.getUsersByType(Constants.NORMAL_USER)) {
            user.getAudioPlayer().updateCurrentlyPlayingAudio(commandInput.getTimestamp());
            if (user.isPremium()) {
                user.payArtists();
            }
        }

        ArrayList<Artist> artists = new ArrayList<>();
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        for (User user : UserManager.getUsersByType(Constants.ARTIST_USER)) {
            Artist artist = (Artist) user;

            if (!artist.getSongsListens().isEmpty() || artist.getMerchRevenue() != 0) {
                artists.add(artist);
            }
        }

        LinkedHashMap<String, ArtistInfo> sortedArtists = getArtistsSorted(artists);

        for (String artist : sortedArtists.keySet()) {
            LinkedHashMap<String, Object> artistResult = new LinkedHashMap<>();
            artistResult.put(Constants.SONG_REVENUE, sortedArtists.get(artist).songRevenue);
            artistResult.put(Constants.MERCH_REVENUE, sortedArtists.get(artist).merchRevenue);
            artistResult.put(Constants.RANKING, getRanking(artist, sortedArtists));

            String mostProfitableSong = sortedArtists.get(artist).mostProfitableSong;
            if (mostProfitableSong == null) {
                mostProfitableSong = Constants.N_A;
            }

            artistResult.put(Constants.MOST_PROFITABLE_SONG, mostProfitableSong);
            result.put(artist, artistResult);
        }


        output.putPOJO(Constants.OUTPUT_RESULT, result);
    }

    private LinkedHashMap<String, ArtistInfo> getArtistsSorted(final ArrayList<Artist> artists) {
        LinkedHashMap<String, ArtistInfo> sortedArtists = new LinkedHashMap<>();
        artists.sort(Comparator.comparing(User::getName));

        for (User user : artists) {
            ArtistInfo artistInfo = calculateSongsRevenue((Artist) user);
            sortedArtists.put(user.getName(), artistInfo);
        }

        Comparator<ArtistInfo> comparator = (o1, o2) -> (int) (o2.songRevenue + o2.merchRevenue
                                                                - o1.songRevenue - o1.merchRevenue);
        sortedArtists = sortedArtists.entrySet().stream().
                sorted(Map.Entry.comparingByValue(comparator)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((integer, integer2) -> integer), LinkedHashMap::new));

        return sortedArtists;
    }

    private ArtistInfo calculateSongsRevenue(final Artist artist) {
        double revenue = 0, maxSongRevenue = 0;
        String mostProfitableSong = null;

        // sort the songs lexicographically
        Comparator<String> comparatorLex = String::compareTo;
        LinkedHashMap<String, Double> revenues = artist.getSongsRevenue().entrySet().stream().
                sorted(Map.Entry.comparingByKey(comparatorLex)).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        ((d, d2) -> d), LinkedHashMap::new));

        for (Map.Entry<String, Double> entry : revenues.entrySet()) {
            revenue += entry.getValue();

            if (entry.getValue() > maxSongRevenue) {
                maxSongRevenue = entry.getValue();
                mostProfitableSong = entry.getKey();
            }
        }

        revenue = (double) Math.round(revenue * Constants.ONE_HUNDRED) / Constants.ONE_HUNDRED;
        return new ArtistInfo(revenue, mostProfitableSong, artist.getMerchRevenue());
    }

    private int getRanking(final String artist, final LinkedHashMap<String, ArtistInfo> artists) {
        int ranking = 1;

        for (Map.Entry<String, ArtistInfo> entry : artists.entrySet()) {
            if (entry.getKey().equals(artist)) {
                return ranking;
            }
            ranking++;
        }

        return 0;
    }

}
