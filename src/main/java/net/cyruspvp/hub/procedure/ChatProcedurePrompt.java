package net.cyruspvp.hub.procedure;

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Hub
 * @author Yair © 2024
 * Date: 13 - nov
 */

import net.cyruspvp.hub.user.User;
import net.cyruspvp.hub.utilities.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatProcedurePrompt extends StringPrompt {
    private final String socialType;
    private final User user;
    private final Player player;

    public ChatProcedurePrompt(String socialType, User user, Player player) {
        this.socialType = socialType;
        this.user = user;
        this.player = player;
    }



    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return ChatUtil.translate("&ePlease type the social link to set to the profile, or type &ccancel &e to cancel the procedure");
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        if (s.equalsIgnoreCase("cancel") || s.equalsIgnoreCase("no")) {
            conversationContext.getForWhom().sendRawMessage(ChatColor.RED + "You have cancelled the procedure!");
            return Prompt.END_OF_CONVERSATION;
        }

        try {
            switch (socialType) {
                case "YOUTUBE":
                    user.getSocialStuff().setYoutubeLink(s);
                    break;
                case "DISCORD":
                    user.getSocialStuff().setDiscordLink(s);
                    break;
                case "TWITCH":
                    user.getSocialStuff().setTwitchLink(s);
                    break;
                case "TWITTER":
                    user.getSocialStuff().setTwitterLink(s);
                    break;
            }
            conversationContext.getForWhom().sendRawMessage(ChatColor.GREEN + "You successfully put the link to your social profile!");

        } catch (Exception e) {
            e.printStackTrace();
            conversationContext.getForWhom().sendRawMessage(ChatColor.RED + "There was an issue updating the link for " + socialType);
        }
        return Prompt.END_OF_CONVERSATION;
    }
}
