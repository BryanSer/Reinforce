/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */

package Br.Reinforce;

import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 * @since 2018-9-28
 */
public class Protect extends ConfigItem{
    private int Max;
    
    public Protect(ConfigurationSection config){
        super(config);
        this.Max = config.getInt("Max");
    }

    public int getMax() {
        return Max;
    }
    
    
}
