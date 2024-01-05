package contracts;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;

public interface ICommand {
    /**
     * Execute the command and modifies the output
     * @param commandInput command given
     * @param output output of the command
     */
    void execute(CommandInput commandInput, ObjectNode output);
}
