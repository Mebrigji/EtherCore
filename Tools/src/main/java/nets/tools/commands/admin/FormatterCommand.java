package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import nets.tools.manager.GroupManager;
import nets.tools.model.Group;

public class FormatterCommand {

    public void register(){
        new CommandTree("formatter")
                .withPermission("ethercraft.command.admin.formatter")
                .then(new LiteralArgument("default")
                        .then(new LiteralArgument("set")
                                .combineWith(new GreedyStringArgument("format"))
                                .executes((commandSender, commandArguments) -> {

                                })
                        )
                        .then(new LiteralArgument("info")
                                .executes((commandSender, commandArguments) -> {

                                })
                        )
                ).then(createGroupArgument("group")
                        .then(new LiteralArgument("set")
                                .executes((commandSender, commandArguments) -> {

                                })
                        )
                        .then(new LiteralArgument("info")
                                .executes((commandSender, commandArguments) -> {

                                })
                        )
                )
                .register();
    }

    private Argument<Group> createGroupArgument(String nodeName){
        return new CustomArgument<>(new StringArgument(nodeName), customArgumentInfo -> GroupManager.getInstance().getGroupMap().getOrDefault(customArgumentInfo.input(), Group.DEFAULT)).includeSuggestions((suggestionInfo, suggestionsBuilder) -> suggestionsBuilder.buildFuture());
    }
}
