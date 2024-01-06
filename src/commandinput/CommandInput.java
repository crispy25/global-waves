package commandinput;

import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class CommandInput {
    private String command;
    private String username;
    private int timestamp;
    private String type;
    private String itemNumber;
    private Filter filters;
    private int seed;
    private int playlistId;
    private String playlistName;

    private int age;
    private String city;
    private ArrayList<SongInput> songs;
    private String description;
    private String name;
    private String date;
    private int price;
    private ArrayList<EpisodeInput> episodes;
    private int releaseYear;
    private String nextPage;

    private String recommendationType;

    public CommandInput() {
    }
}
