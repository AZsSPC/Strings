package com.pedestriamc.strings.listeners;

import com.pedestriamc.strings.Strings;
import com.pedestriamc.strings.api.event.ChannelChatEvent;
import com.pedestriamc.strings.chat.Mentioner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MentionListener implements Listener {

    private final Mentioner mentioner;

    public MentionListener(Strings strings) {
        mentioner = strings.getMentioner();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEvent(AsyncPlayerChatEvent event) {
        if (!(event instanceof ChannelChatEvent)) {
            return;
        }

        Player p = event.getPlayer();
        if (!p.hasPermission("strings.*") && !p.hasPermission("strings.mention") && !p.hasPermission("strings.mention.all")) {
            return;
        }

        String message = event.getMessage();
        Bukkit.getOnlinePlayers().stream()
                .filter(subj -> message.contains("@" + subj.getName()))
                .forEach(subj -> mentioner.mention(subj, p));

        if (p.hasPermission("strings.mention.all") && message.contains("@everyone")) {
            Bukkit.getOnlinePlayers().forEach(subj -> mentioner.mention(subj, p));
        }
    }
}
