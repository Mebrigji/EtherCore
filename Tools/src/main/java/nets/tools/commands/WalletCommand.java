package nets.tools.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.saidora.api.notifications.NotificationBuilder;
import nets.tools.Main;
import nets.tools.db.MongodbConnector;
import nets.tools.manager.UserManager;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Arrays;

public class WalletCommand {

    DecimalFormat decimalFormat = new DecimalFormat("##.##");

    public void register(){
        new CommandTree("wallet")
                .withAliases("portfel", "bal", "balance")
                .executesPlayer((player, commandArguments) -> {
                    UserManager.getInstance().getUser(player).ifPresent(user -> {
                        NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET, Placeholder.parsed("balance", decimalFormat.format(user.getBalance()))).send(player);
                    });
                })
                .then(new LiteralArgument("help")
                        .withPermission("ethercraft.command.admin.economy.wallet")
                        .executes((commandSender, commandArguments) -> {
                            NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_HELP).send(commandSender);
                        })
                )
                .then(new LiteralArgument("transfer")
                        .combineWith(new PlayerArgument("player"), new DoubleArgument("amount"))
                        .executesPlayer((player, commandArguments) -> {
                            Player target = (Player) commandArguments.get("player");
                            double amount = (double) commandArguments.get("amount");
                            String playerName = target.getName();
                            if(!Main.getInstance().getWalletConfiguration().WALLED_ENABLED){
                                player.sendMessage("Sorry but wallet is currently disabled.");
                                return;
                            }

                            if(player.getName().equals(playerName)) {
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_TRANSFER_SAME).send(player);
                                return;
                            }

                            UserManager.getInstance().getUser(player).ifPresent(user -> {
                                if(amount < 1){
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_TRANSFER_MUST_BE_HIGHER_THAT_ONE).send(player);
                                    return;
                                }

                                if(user.getBalance() < amount || Double.isInfinite(amount) || Double.isNaN(amount)){
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_TRANSFER_NOT_ENOUGH_MONEY).send(player);
                                    return;
                                }

                                UserManager.getInstance().find(playerName).ifPresentOrElse(u -> {
                                    user.setBalance(user.getBalance() - amount);
                                    u.setBalance(u.getBlocksMined() + amount);

                                    MongodbConnector mongodbConnector = Main.getInstance().getConnector();
                                    mongodbConnector.addToHistory(playerName, amount, player.getName(), MongodbConnector.HistoryType.TRANSFER);
                                    NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_TRANSFER_SENDER, Placeholder.parsed("amount", String.valueOf(amount)), Placeholder.parsed("player", u.getNickName())).send(player);
                                    u.getPlayer().ifPresent(p -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_TRANSFER_TARGET, Placeholder.parsed("amount", String.valueOf(amount)), Placeholder.parsed("player", user.getNickName())).send(p));
                                }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_ERROR_NOT_EXISTS).send(player));
                            });
                        })
                ).then(new LiteralArgument("set")
                        .withPermission("ethercraft.command.admin.economy.wallet")
                        .combineWith(new PlayerArgument("player"), new DoubleArgument("amount"))
                        .executes((sender, commandArguments) -> {
                            Player target = (Player) commandArguments.get(0);
                            double amount = (double) commandArguments.get(1);
                            String playerName = target.getName();
                            if(!Main.getInstance().getWalletConfiguration().WALLED_ENABLED && sender instanceof Player){
                                sender.sendMessage("Sorry but wallet is currently disabled.");
                                return;
                            }
                            UserManager.getInstance().find(playerName).ifPresentOrElse(user -> {
                                user.setBalance(amount);
                                MongodbConnector mongodbConnector = Main.getInstance().getConnector();
                                mongodbConnector.addToHistory(playerName, amount, sender.getName(), MongodbConnector.HistoryType.SET);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_SET, Placeholder.parsed("player", playerName), Placeholder.parsed("amount", String.valueOf(amount))).send(sender);
                            }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_ERROR_NOT_EXISTS).send(sender));
                        })
                ).then(new LiteralArgument("add")
                        .withPermission("ethercraft.command.admin.economy.wallet")
                        .combineWith(new PlayerArgument("player"), new DoubleArgument("amount"))
                        .executes((sender, commandArguments) -> {
                            Player target = (Player) commandArguments.get(0);
                            double amount = (double) commandArguments.get(1);
                            String playerName = target.getName();
                            if(!Main.getInstance().getWalletConfiguration().WALLED_ENABLED && sender instanceof Player){
                                sender.sendMessage("Sorry but wallet is currently disabled.");
                                return;
                            }
                            UserManager.getInstance().find(playerName).ifPresentOrElse(user -> {
                                user.setBalance(user.getBalance() + amount);
                                MongodbConnector mongodbConnector = Main.getInstance().getConnector();
                                mongodbConnector.addToHistory(playerName, amount, sender.getName(), MongodbConnector.HistoryType.ADD);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_DEPOSIT, Placeholder.parsed("player", playerName), Placeholder.parsed("amount", String.valueOf(amount))).send(sender);
                            }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_ERROR_NOT_EXISTS).send(sender));
                        })
                ).then(new LiteralArgument("remove")
                        .withPermission("ethercraft.command.admin.economy.wallet")
                        .combineWith(new PlayerArgument("player"), new DoubleArgument("amount"))
                        .executes((sender, commandArguments) -> {
                            Player target = (Player) commandArguments.get(0);
                            double amount = (double) commandArguments.get(1);
                            String playerName = target.getName();
                            if(!Main.getInstance().getWalletConfiguration().WALLED_ENABLED && sender instanceof Player){
                                sender.sendMessage("Sorry but wallet is currently disabled.");
                                return;
                            }
                            UserManager.getInstance().find(playerName).ifPresentOrElse(user -> {
                                user.setBalance(user.getBalance() - amount);
                                MongodbConnector mongodbConnector = Main.getInstance().getConnector();
                                mongodbConnector.addToHistory(playerName, amount, sender.getName(), MongodbConnector.HistoryType.REMOVE);
                                NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_WITHDRAW, Placeholder.parsed("player", playerName), Placeholder.parsed("amount", String.valueOf(amount))).send(sender);
                            }, () -> NotificationBuilder.of(NotificationBuilder.NotificationType.CHAT, Main.getInstance().getWalletConfiguration().COMMAND_WALLET_ERROR_NOT_EXISTS).send(sender));
                        })
                )
                .register();
    }

}
