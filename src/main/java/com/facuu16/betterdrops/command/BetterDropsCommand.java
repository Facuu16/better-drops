package com.facuu16.betterdrops.command;

import com.facuu16.betterdrops.BetterDrops;
import com.facuu16.betterdrops.manager.DropManager;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NbtApiException;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BetterDropsCommand implements CommandExecutor {
    private final BetterDrops plugin;

    public BetterDropsCommand(BetterDrops plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;

        switch (args[0]) {
            case "reload":
                plugin.reloadConfig();
                DropManager.getInstance(plugin).reload();
                sender.sendMessage(BetterDrops.colorize("&aConfig reloaded successfully"));
                break;

            case "nbt":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("This subcommand can only be run by a player.");
                    return false;
                }

                Player player = (Player) sender;

                try {
                    ItemStack item = player.getInventory().getItemInHand();
                    NBTCompound itemNBT = NBTItem.convertItemtoNBT(item);

                    TextComponent message = new TextComponent(BetterDrops.colorize("&7[CLICK TO COPY] &c&lNBT &7-> " + itemNBT));
                    BaseComponent[] componentBuilder = new ComponentBuilder(BetterDrops.colorize("&aCLICK TO COPY"))
                            .create();

                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentBuilder));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, itemNBT.toString()));
                    player.spigot().sendMessage(message);
                } catch (NbtApiException | NullPointerException exception) {
                    player.sendMessage(BetterDrops.colorize("&cYou must hold an item in hand!"));
                }
                break;
        }

        return true;
    }
}
