package commandinput;

import admin.AddUser;
import admin.DeleteUser;
import admin.GetAllUsers;
import admin.GetOnlineUsers;
import advertising.AdBreak;
import artist.AddAlbum;
import artist.RemoveAlbum;
import admin.ShowAlbums;
import admin.ShowPodcasts;
import artist.AddMerch;
import artist.AddEvent;
import artist.RemoveEvent;
import audioplayer.commands.Load;
import audioplayer.commands.PlayPause;
import audioplayer.commands.Status;
import audioplayer.commands.CreatePlaylist;
import audioplayer.commands.AddRemoveInPlaylist;
import audioplayer.commands.Like;
import audioplayer.commands.ShowPreferredSongs;
import audioplayer.commands.ShowPlaylists;
import audioplayer.commands.Repeat;
import audioplayer.commands.Shuffle;
import audioplayer.commands.Forward;
import audioplayer.commands.Backward;
import audioplayer.commands.Next;
import audioplayer.commands.Prev;
import audioplayer.commands.Follow;
import audioplayer.commands.SwitchVisibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import constants.CommandTypes;
import constants.Constants;
import contracts.ICommand;

import com.fasterxml.jackson.databind.node.ObjectNode;
import globalstatistics.GetTop5Songs;
import globalstatistics.GetTop5Playlists;
import globalstatistics.GetTop5Artists;
import globalstatistics.GetTop5Albums;
import host.AddAnnouncement;
import host.AddPodcast;
import host.RemoveAnnouncement;
import host.RemovePodcast;
import lombok.Getter;
import lombok.Setter;
import monetization.BuyMerch;
import monetization.BuyPremium;
import monetization.CancelPremium;
import monetization.SeeMerch;
import monetization.EndProgram;
import notifications.GetNotifications;
import notifications.Subscribe;
import pagemanager.ChangePage;
import pagemanager.PrintCurrentPage;
import pagemanager.navigation.NextPage;
import pagemanager.navigation.PreviousPage;
import recommendations.LoadRecommendations;
import recommendations.UpdateRecommendations;
import searchbar.Search;
import searchbar.Select;
import usermanager.SwitchConnectionStatus;
import wrapped.Wrapped;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
public final class CommandHandler {

    private static LinkedHashMap<String, ICommand> commands = new LinkedHashMap<>();

    static {
        commands.put(CommandTypes.SEARCH, new Search());
        commands.put(CommandTypes.SELECT, new Select());
        commands.put(CommandTypes.LOAD, new Load());
        commands.put(CommandTypes.PLAY_PAUSE, new PlayPause());
        commands.put(CommandTypes.STATUS, new Status());
        commands.put(CommandTypes.CREATE_PLAYLIST, new CreatePlaylist());
        commands.put(CommandTypes.LIKE, new Like());
        commands.put(CommandTypes.ADD_REMOVE_IN_PLAYLIST, new AddRemoveInPlaylist());
        commands.put(CommandTypes.SHOW_PREFERRED_SONGS, new ShowPreferredSongs());
        commands.put(CommandTypes.SHOW_PLAYLISTS, new ShowPlaylists());
        commands.put(CommandTypes.REPEAT, new Repeat());
        commands.put(CommandTypes.SHUFFLE, new Shuffle());
        commands.put(CommandTypes.FORWARD, new Forward());
        commands.put(CommandTypes.BACKWARD, new Backward());
        commands.put(CommandTypes.NEXT, new Next());
        commands.put(CommandTypes.PREV, new Prev());
        commands.put(CommandTypes.FOLLOW, new Follow());
        commands.put(CommandTypes.SWITCH_VISIBILITY, new SwitchVisibility());
        commands.put(CommandTypes.GET_TOP_5_SONGS, new GetTop5Songs());
        commands.put(CommandTypes.GET_TOP_5_PLAYLISTS, new GetTop5Playlists());
        commands.put(CommandTypes.GET_TOP_5_ALBUMS, new GetTop5Albums());
        commands.put(CommandTypes.GET_TOP_5_ARTIST, new GetTop5Artists());

        commands.put(CommandTypes.ADD_USER, new AddUser());
        commands.put(CommandTypes.DELETE_USER, new DeleteUser());
        commands.put(CommandTypes.SWITCH_CONNECTION_STATUS, new SwitchConnectionStatus());
        commands.put(CommandTypes.GET_ONLINE_USERS, new GetOnlineUsers());
        commands.put(CommandTypes.GET_ALL_USERS, new GetAllUsers());
        commands.put(CommandTypes.ADD_ALBUM, new AddAlbum());
        commands.put(CommandTypes.REMOVE_ALBUM, new RemoveAlbum());
        commands.put(CommandTypes.ADD_MERCH, new AddMerch());
        commands.put(CommandTypes.ADD_EVENT, new AddEvent());
        commands.put(CommandTypes.REMOVE_EVENT, new RemoveEvent());
        commands.put(CommandTypes.SHOW_ALBUMS, new ShowAlbums());
        commands.put(CommandTypes.SHOW_PODCASTS, new ShowPodcasts());
        commands.put(CommandTypes.PRINT_CURRENT_PAGE, new PrintCurrentPage());
        commands.put(CommandTypes.CHANGE_PAGE, new ChangePage());
        commands.put(CommandTypes.ADD_PODCAST, new AddPodcast());
        commands.put(CommandTypes.REMOVE_PODCAST, new RemovePodcast());
        commands.put(CommandTypes.ADD_ANNOUNCEMENT, new AddAnnouncement());
        commands.put(CommandTypes.REMOVE_ANNOUNCEMENT, new RemoveAnnouncement());

        commands.put(CommandTypes.WRAPPED, new Wrapped());
        commands.put(CommandTypes.BUY_MERCH, new BuyMerch());
        commands.put(CommandTypes.SEE_MERCH, new SeeMerch());
        commands.put(CommandTypes.BUY_PREMIUM, new BuyPremium());
        commands.put(CommandTypes.CANCEL_PREMIUM, new CancelPremium());
        commands.put(CommandTypes.AD_BREAK, new AdBreak());
        commands.put(CommandTypes.SUBSCRIBE, new Subscribe());
        commands.put(CommandTypes.GET_NOTIFICATIONS, new GetNotifications());
        commands.put(CommandTypes.NEXT_PAGE, new NextPage());
        commands.put(CommandTypes.PREVIOUS_PAGE, new PreviousPage());
        commands.put(CommandTypes.UPDATE_RECOMMENDATIONS, new UpdateRecommendations());
        commands.put(CommandTypes.LOAD_RECOMMENDATIONS, new LoadRecommendations());
        commands.put(CommandTypes.END_PROGRAM, new EndProgram());
    }

    /**
     * Execute the command given on the audio player passed as parameter. The result is stored
     * in output variable.
     * @param commandInput the command to execute
     * @param output output of the command
     */
    public static void handleCommand(final CommandInput commandInput,
                                     final ObjectNode output) {
        output.put(Constants.OUTPUT_USER, commandInput.getUsername());
        output.put(Constants.OUTPUT_TIMESTAMP, commandInput.getTimestamp());

        if (commands.get(commandInput.getCommand()) != null) {
            commands.get(commandInput.getCommand()).execute(commandInput, output);
        }
    }

    /**
     * For each command, pass it to the user's commandHandler, else
     * execute the command
     * @param commandInputs command inputs
     */
    public static void handleCommands(final List<CommandInput> commandInputs,
                                      final ArrayNode outputs) {

        ObjectMapper objectMapper = new ObjectMapper();

        for (CommandInput command: commandInputs) {

            ObjectNode output = objectMapper.createObjectNode();
            output.put(Constants.OUTPUT_COMMAND, command.getCommand());

            handleCommand(command, output);

            outputs.add(output);
        }

        // execute endProgram after all commands
        ObjectNode output = objectMapper.createObjectNode();
        output.put(Constants.OUTPUT_COMMAND, CommandTypes.END_PROGRAM);
        commands.get(CommandTypes.END_PROGRAM).
                execute(commandInputs.get(commandInputs.size() - 1), output);
        outputs.add(output);
    }

    private CommandHandler() {

    }
}
