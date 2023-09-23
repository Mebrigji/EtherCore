package nets.tools.commands.admin;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.saidora.api.helpers.ComponentHelper;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;

public class NpcCommand {

    public void register(){
        new CommandTree("npc")
                .withPermission("ethercraft.command.admin.npc")
                .then(new LiteralArgument("spawn")
                        .combineWith(new TextArgument("name"))
                        .combineWith(new EntityTypeArgument("entityType").replaceSuggestions((suggestionInfo, suggestionsBuilder) -> {
                            Arrays.stream(EntityType.values()).filter(EntityType::isSpawnable).forEach(entityType -> suggestionsBuilder.suggest(entityType.name().toLowerCase()));
                            return suggestionsBuilder.buildFuture();
                        }))
                        .executesPlayer((player, commandArguments) -> {
                            String name = (String) commandArguments.get(0);
                            EntityType entityType = (EntityType) commandArguments.get(1);
                            Location location = player.getLocation();
                            Entity npc = location.getWorld().spawnEntity(location, entityType);
                            if(npc instanceof LivingEntity livingEntity){
                                livingEntity.setAI(false);
                            }
                            npc.setCustomNameVisible(true);
                            npc.customName(ComponentHelper.asComponent(name));
                        }).then(new LocationArgument("location", LocationType.PRECISE_POSITION)
                                .executesPlayer((player, commandArguments) -> {
                                    String name = (String) commandArguments.get(0);
                                    EntityType entityType = (EntityType) commandArguments.get(1);
                                    Location location = (Location) commandArguments.get(2);
                                    Entity npc = location.getWorld().spawnEntity(location, entityType);
                                    if(npc instanceof LivingEntity livingEntity){
                                        livingEntity.setAI(false);
                                    }
                                    npc.setCustomNameVisible(true);
                                    npc.customName(ComponentHelper.asComponent(name));
                                })
                        )
                )
                .register();
    }

}
