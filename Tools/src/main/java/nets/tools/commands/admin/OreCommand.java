package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.server.commands.CommandTeleport;
import net.saidora.api.builders.InventoryBuilder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.manager.OreManager;
import nets.tools.model.Ore;
import nets.tools.objects.OreController;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class OreCommand {

    public void register(){
        new CommandTree("ore")
                .withAliases("rudy", "ores", "drop", "drops")
                .executesPlayer((player, commandArguments) -> {
                    int size = OreManager.getInstance().getList().size();
                    InventoryBuilder inventoryBuilder = new InventoryBuilder(Bukkit.createInventory(null, Math.max(9, (int) Math.ceil((double) size / 9) * 9), ComponentHelper.asComponent("<!italic><gradient:#DA7A00:#F0AA52>⛏ sᴢᴀɴsᴀ ɴᴀ ʀᴜᴅʏ</gradient>")));
                    OreManager.getInstance().getList().forEach(ore -> inventoryBuilder.addItem(itemStack -> itemStack.setType(ore.what()), itemMeta -> {
                        itemMeta.displayName(ComponentHelper.asComponent(ore.displayName()));
                        itemMeta.lore(ComponentHelper.asComponents(ore.lore()));
                    }));
                    inventoryBuilder.click(event -> event.setCancelled(true));
                    inventoryBuilder.open(player);
                })
                .then(new LiteralArgument("add")
                        .withPermission("ethercraft.command.admin.ore.add")
                        .combineWith(new ItemStackArgument("itemToGenerate"), new DoubleArgument("chance"), new IntegerArgument("yCord"), new TextArgument("displayName"), new TextArgument("lore"))
                        .executes((commandSender, commandArguments) -> {
                            ItemStack itemStack = (ItemStack) commandArguments.get("itemToGenerate");
                            double chance = (double) commandArguments.get("chance");
                            int yCord = (int) commandArguments.get("yCord");
                            String displayName = (String) commandArguments.get("displayName");
                            List<String> lore = Arrays.stream(((String)commandArguments.get("lore")).split("%nl%")).toList();
                            Ore ore = new OreController(itemStack.getType(), chance, yCord, displayName, lore);
                            OreManager.getInstance().getList().add(ore);
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ORE_ADD).send(commandSender);
                        })
                )
                .then(new LiteralArgument("remove")
                        .withPermission("ethercraft.command.admin.ore.remove")
                        .combineWith(createOreArgument("ore"))
                        .executes((commandSender, commandArguments) -> {
                            Ore ore = (Ore) commandArguments.get("ore");
                            OreManager oreManager = OreManager.getInstance();
                            synchronized (oreManager) {
                                oreManager.getList().remove(ore);
                            }
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ORE_REMOVE).send(commandSender);
                        })
                )
                .then(new LiteralArgument("edit")
                        .withPermission("ethercraft.command.admin.ore.edit")
                        .then(createOreArgument("ore")
                                .then(new LiteralArgument("chance")
                                        .combineWith(new DoubleArgument("value"))
                                        .executes((commandSender, commandArguments) -> {
                                            Ore ore = (Ore) commandArguments.get("ore");
                                            double value = (double) commandArguments.get("value");
                                            ore.chance(value);
                                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_ORE_EDIT_CHANCE, Placeholder.parsed("ore", ore.what().name()), Placeholder.parsed("chance", String.valueOf(value))).send(commandSender);
                                        })
                                )
                        )
                )
                .register();
    }

    private Argument<Ore> createOreArgument(String nodeName){
        return new CustomArgument<>(new StringArgument(nodeName), info -> OreManager.getInstance()
                .getList()
                .stream()
                .filter(ore -> ore.what().name().equalsIgnoreCase(info.input()))
                .findFirst()
                .orElseThrow(() -> CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("This type of ore does not exists."))))
                .includeSuggestions((suggestionInfo, suggestionsBuilder) -> {
            OreManager.getInstance().getList().forEach(ore -> suggestionsBuilder.suggest(ore.what().name()));
            return suggestionsBuilder.buildFuture();
        });
    }

}
