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
public class Gem extends ConfigItem{
    private String Key;
    private int Max;
    private Double Success;
    
    public Gem(ConfigurationSection config){
        super(config);
        this.Key = config.getName();
        this.Max = config.getInt("Max");
        this.Success = config.getDouble("Success");
    }

    public String getKey() {
        return Key;
    }

    public int getMax() {
        return Max;
    }

    public Double getSuccess() {
        return Success;
    }
    
    
}
