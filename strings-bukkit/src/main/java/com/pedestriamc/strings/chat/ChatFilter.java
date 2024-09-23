package com.pedestriamc.strings.chat;

import com.pedestriamc.strings.Strings;
import com.pedestriamc.strings.message.Message;
import com.pedestriamc.strings.message.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChatFilter {
    private final String regex = "(?i)\\b((?:https?|ftp)://|www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)";
    private final Pattern pattern = Pattern.compile(regex);
    private final List<String> urlWhitelist;
    private List<String> bannedWords;

    public ChatFilter(@NotNull Strings strings) {
        this.urlWhitelist = Optional.ofNullable(strings.getConfig().getList("url-whitelist"))
                .orElse(Collections.emptyList())
                .stream()
                .filter(String.class::isInstance)
                .map(obj -> normalizeUrl((String) obj))
                .collect(Collectors.toList());
    }

    private String normalizeUrl(String url) {
        return url.replaceAll("((?:https?|ftp)://|www\\.|/$)", "").toLowerCase();
    }

    public String urlFilter(String msg, Player player) {
        boolean urlReplaced = false;
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            String match = matcher.group();
            if (!urlWhitelist.contains(normalizeUrl(match))) {
                msg = msg.replace(match, "");
                urlReplaced = true;
            }
        }
        if (urlReplaced) {
            Messenger.sendMessage(Message.LINKS_PROHIBITED, player);
        }
        return msg;
    }

    public String profanityFilter(String msg, Player player) {
        return msg;
    }
}
