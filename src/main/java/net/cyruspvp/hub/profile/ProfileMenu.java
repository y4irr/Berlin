package net.cyruspvp.hub.profile;

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Hub
 * @author Yair © 2024
 * Date: 13 - nov
 */

import net.cyruspvp.hub.Berlin;
import net.cyruspvp.hub.procedure.ChatProcedurePrompt;
import net.cyruspvp.hub.user.User;
import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.TextureUtil;
import net.cyruspvp.hub.utilities.item.ItemBuilder;
import net.cyruspvp.hub.utilities.menu.Button;
import net.cyruspvp.hub.utilities.menu.Menu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ProfileMenu extends Menu {

    private final Berlin plugin;
    private final Player target;

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_GRAY + (target == player ? "Your Profile" : target.getName() + "'s Profile");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        User user = Berlin.get().getUserManager().getUser(target.getUniqueId());

        for (int i = 0; i <= 8; i++) {
            buttons.put(i, decorativeItem());
        }

        for (int i = 36; i <= 44; i++) {
            buttons.put(i, decorativeItem());
        }
        buttons.put(19, socialButton("TWITTER", user, TextureUtil.TWITTER_HEAD, "&bTwitter",  user.getSocialStuff().getTwitterLink()));
        buttons.put(21, socialButton("YOUTUBE", user, TextureUtil.YOUTUBE_HEAD, "&cYouTube", user.getSocialStuff().getYoutubeLink()));
        buttons.put(23, socialButton("DISCORD", user, TextureUtil.DISCORD_HEAD, "&9Discord", user.getSocialStuff().getDiscordLink()));
        buttons.put(25, socialButton("TWITCH", user, TextureUtil.TWITCH_HEAD, "&dTwitch", user.getSocialStuff().getTwitchLink()));

        return buttons;
    }

    private Button socialButton(String name, User user, String texture, String displayName, String link) {
        return new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = ChatUtil.translate(Arrays.asList(
                        "&7",
                        "&7Current Link: &f" + link,
                        "&7"
                ));
                return new ItemBuilder("SKULL")
                        .setData(3)
                        .setName(ChatUtil.translate(displayName))
                        .setSkullOwner(texture)
                        .setLore(lore)
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                if (player != target) return;

                ConversationFactory factory = new ConversationFactory(plugin)
                        .withFirstPrompt(new ChatProcedurePrompt(name, user, player))
                        .withLocalEcho(false)
                        .withTimeout(60);

                player.beginConversation(factory.buildConversation(player));
                player.closeInventory();
            }
        };
    }

    public Button decorativeItem() {
        return new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder("STAINED_GLASS_PANE")
                        .setData(7).build();
            }
        };
    }
}
