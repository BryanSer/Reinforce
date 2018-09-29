/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */

package Br.Reinforce;

import Br.API.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 * @since 2018-9-28
 */
public class ConfigItem {
    protected ItemStack Item;
    
    protected ConfigItem(ConfigurationSection config){
        Item = Utils.AnalyticalItem_2(config.getString("Item"));
    }

    public ItemStack getItem() {
        return Item.clone();
    }
    
    
    public boolean isSimilar(ItemStack other){
        return this.Item.isSimilar(other);
    }
}
