package nets.tools.commands.admin;


import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.helpers.ComponentHelper;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand {

    public void register(){
        new CommandAPICommand("give")
                .withPermission("ethercraft.command.admin.give")
                .withArguments(new PlayerArgument("player"), new ItemStackArgument("item"))
                .withOptionalArguments(new IntegerArgument("amount"))
                .executes((commandSender, commandArguments) -> {
                    Player player = (Player) commandArguments.get("player");
                    ItemStack itemStack = (ItemStack) commandArguments.get("item");
                    commandArguments.getOptional("amount").map(o -> (Integer)o).ifPresent(integer -> itemStack.setAmount(integer));
                    player.getInventory().addItem(itemStack).forEach((integer, stack) -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
                    if(player.equals(commandSender)){
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GIVE_SELF, Placeholder.component("item", ComponentHelper.asComponent("[ITEM]").hoverEvent(itemStack.asHoverEvent()))).send(commandSender);
                    } else {
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GIVE, Placeholder.parsed("player", player.getName()), Placeholder.component("item", ComponentHelper.asComponent("[ITEM]").hoverEvent(itemStack.asHoverEvent()))).send(commandSender);
                    }
                })
                .override();
    }

    //private final Pattern pattern = Pattern.compile("(name=\"([^\"]+)\")|(lore=\"([^\"]+)\")|(amount=([0-9]+))|(durability=([0-9]+))|(enchantments=((\\{\"id\":\"([^\"]+)\"\\,\"level\":([0-9]+)\\})(,\\{\"id\":\"([^\"]+)\"\\,\"level\":([0-9]+)\\})*))");

    //@Command("give")
    //@Permission("ethercraft.command.admin.give")
    //public void handleGiveCommand(@Sender CommandSender commandSender, @Name("player") Player player, @Name("material") Material material, @Optional @Name("extraAttributes") @Text String extra){
    //    ItemHelper itemHelper = ItemHelper.edit(new ItemStack(material));
    //    Map<String, Object> values;
//
    //    if(extra == null) values = new HashMap<>();
    //    else values = getRegexValues(extra);
//
    //    int amount = (int) values.getOrDefault("amount", 1);
    //    int durability = (int) values.getOrDefault("durability", 0);
    //    String displayName = (String) values.get("name");
    //    String lore = (String) values.get("lore");
    //    Map<Enchantment, Integer> enchantmentIntegerMap = (Map<Enchantment, Integer>) values.getOrDefault("enchantments", new HashMap<>());
//
    //    itemHelper.editBukkitItemStack(stack -> {
    //        stack.setAmount(amount);
//
    //        Damageable damageable = (Damageable) stack.getItemMeta();
    //        damageable.setDamage(durability);
//
    //        if(displayName != null && !displayName.isEmpty()) damageable.displayName(ComponentHelper.asComponent(displayName));
    //        if(lore != null && !lore.isEmpty()) damageable.lore(ComponentHelper.asComponents(lore.split("%nl%")));
    //        enchantmentIntegerMap.forEach((enchantment, integer) -> damageable.addEnchant(enchantment, integer, true));
//
    //        stack.setItemMeta(damageable);
    //    });
//
    //    ItemStack itemStack = itemHelper.getItemStack();
//
    //    player.getInventory().addItem(itemStack).forEach((integer, stack) -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
    //    if(player.equals(commandSender)){
    //        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GIVE_SELF, Placeholder.component("item", ComponentHelper.asComponent("[ITEM]").hoverEvent(itemStack.asHoverEvent()))).send(commandSender);
    //    } else {
    //        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getMessages().COMMAND_GIVE, Placeholder.parsed("player", player.getName()), Placeholder.component("item", ComponentHelper.asComponent("[ITEM]").hoverEvent(itemStack.asHoverEvent()))).send(commandSender);
    //    }
    //}
//
    //private Map<String, Object> getRegexValues(String text){
    //    Matcher matcher = pattern.matcher(text);
//
    //    Map<String, Object> values = new HashMap<>();
//
    //    while (matcher.find()){
    //        String full = matcher.group(0);
    //        if(full.startsWith("enchantments")) {
    //            Map<Enchantment, Integer> enchantmentIntegerMap = new HashMap<>();
    //            for (int i = 11; i < matcher.groupCount(); i++) {
    //                Enchantment enchantment = CommandFactory.Binder_Enchantment.enchantmentSet.get(matcher.group(i));
    //                if (enchantment == null) continue;
    //                try {
    //                    enchantmentIntegerMap.put(enchantment, Integer.valueOf(matcher.group(i + 1)));
    //                } catch (NumberFormatException ignored) {
    //                }
    //            }
    //            values.put("enchantments", enchantmentIntegerMap);
    //        } else if(full.startsWith("name")) values.put("name", matcher.group(2));
    //        else if(full.startsWith("lore")) values.put("lore", matcher.group(4));
    //        else if(full.startsWith("amount")) values.put("amount", Integer.parseInt(matcher.group(6)));
    //        else if(full.startsWith("durability")) values.put("durability", Integer.parseInt(matcher.group(8)));
//
    //    }
    //    return values;
    //}
}
