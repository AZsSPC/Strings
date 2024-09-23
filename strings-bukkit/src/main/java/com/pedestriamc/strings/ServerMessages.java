package com.pedestriamc.strings;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServerMessages {

    private final Strings strings;
    private final String joinMessageTemplate;
    private final String leaveMessageTemplate;
    private final ArrayList<String> motd;
    private final boolean usePAPI;

    public ServerMessages(@NotNull Strings strings) {
        this.strings = strings;
        this.joinMessageTemplate = strings.getConfig().getString("join-message");
        this.leaveMessageTemplate = strings.getConfig().getString("leave-message");
        this.usePAPI = strings.usePlaceholderAPI();
        this.motd = new ArrayList<>();
        List<?> list = strings.getConfig().getList("motd");
        if (list != null) { // as I know [strings.getConfig().getList()] can't return [null] so this line pointless
            for (Object obj : list) {
                if (obj instanceof String) {
                    motd.add((String) obj);
                }
            }
        }
    }

    private String replaceStuff(String message, Player player, User user) {
        if (usePAPI) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        message = message.replace("{displayname}", player.getName());
        message = message.replace("{username}", player.getName());
        message = message.replace("{prefix}", user.getPrefix());
        message = message.replace("{suffix}", user.getSuffix());
        // just insert more replacements there
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String replaceStuff(String message, Player player) {
        return replaceStuff(message, player, strings.getUser(player));
    }

    public String joinMessage(Player player) {
        return replaceStuff(joinMessageTemplate, player);
    }

    public String leaveMessage(Player player) {
        return replaceStuff(leaveMessageTemplate, player);
    }

    public void sendMOTD(Player player) {
        ArrayList<String> playerMOTD = new ArrayList<>(motd);
        User user = strings.getUser(player);
        for (String message : playerMOTD) {
            player.sendMessage(replaceStuff(message, player, user));
        }
    }
}
