package net.cyruspvp.hub.utilities.menu.buttons;

import net.cyruspvp.hub.utilities.ChatUtil;
import net.cyruspvp.hub.utilities.item.ItemBuilder;
import net.cyruspvp.hub.utilities.menu.Button;
import net.cyruspvp.hub.utilities.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class JumpToPageButton extends Button {

    private final int page;
    private final PaginatedMenu menu;
    private final boolean current;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
        itemBuilder.setName(ChatUtil.translate("&cPage " + this.page));

        if (this.current) {
            itemBuilder.setLore(
                    "",
                    ChatUtil.translate("&aCurrent page")
            );
        }

        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        playNeutral(player);
    }
}
