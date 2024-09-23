package com.pedestriamc.strings.tabcompleters;

import com.pedestriamc.strings.Strings;
import com.pedestriamc.strings.chat.channels.ChannelManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChannelTabCompleter implements TabCompleter {

    private final ChannelManager channelManager;

    public ChannelTabCompleter(@NotNull Strings strings) {
        this.channelManager = strings.getChannelManager();
    }

    private final List<String> empty = new ArrayList<>();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length <= 1) {
            List<String> list = new ArrayList<>(channelManager.getNonProtectedChannelNames());
            list.add("join");
            list.add("leave");
            return list;
        }

        if (args.length > 3) {
            return empty;
        }

        if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave")) {
            return channelManager.getNonProtectedChannelNames();
        }

        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }
}
