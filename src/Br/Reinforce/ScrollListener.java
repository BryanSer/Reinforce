/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Reinforce;

import static Br.Reinforce.ReinforceUI.LEVEL_SP;
import static Br.Reinforce.ReinforceUI.reinforce;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 * @since 2018-9-29
 */
public class ScrollListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent evt) {
        if (evt.getCurrentItem() == null || evt.getCursor() == null) {
            return;
        }
        ItemStack scrollitem = evt.getCursor();
        Scroll scroll = Data.matchScroll(scrollitem);
        if (scroll == null) {
            return;
        }
        if (scrollitem.getAmount() > 1) {
            evt.getWhoClicked().sendMessage(MessageFormat.format(ResourceBundle.getBundle("lang_ZH").getString("Scroll.TooMuch"), new Object[] {}));
            return;
        }
        int lv = 0;
        ItemStack item = evt.getCurrentItem().clone();
        ItemMeta im = item.getItemMeta();
        if (im.hasDisplayName() && im.getDisplayName().contains(LEVEL_SP)) {
            String[] str = im.getDisplayName().split(LEVEL_SP);
            lv = Integer.parseInt(str[1].replaceAll("[^0-9]", "")); //NOI18N
        }
        int to = scroll.getTo();
        if (lv >= to) {
            evt.getWhoClicked().sendMessage(MessageFormat.format(ResourceBundle.getBundle("lang_ZH").getString("Scroll.MaxLevel"), new Object[] {}));
            return;
        }
        for (lv += 1; lv <= to; lv++) {
            try {
                item = reinforce(item, Data.Reinforces.get(lv));
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        evt.setCancelled(true);
        evt.setCurrentItem(item);
        evt.setCursor(null);
        Bukkit.broadcastMessage(String.format(MessageFormat.format(ResourceBundle.getBundle("lang_ZH").getString("Broadcast.Scroll"), new Object[] {}), evt.getWhoClicked().getName(), to));
    }
}
