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
public class Scroll extends ConfigItem{
    private String Key;
    private int To;
    private double Success;
    
    public Scroll(ConfigurationSection config){
        super(config);
        this.Key = config.getName();
        this.To = config.getInt("To");
        this.Success = config.getDouble("Success");
    }

    public String getKey() {
        return Key;
    }

    public ItemStack getItem() {
        return Item.clone();
    }

    public int getTo() {
        return To;
    }

    public double getSuccess() {
        return Success;
    }
    
    
}
