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
public class Reinforce {
    private int Level;
    private double Power;
    private double Probability;
    
    public Reinforce(ConfigurationSection config){
        this.Level = Integer.parseInt(config.getName());
        this.Power = config.getDouble("Power");
        this.Probability = config.getDouble("Probability");
    }

    public int getLevel() {
        return Level;
    }

    public double getPower() {
        return Power;
    }

    public double getProbability() {
        return Probability;
    }
    
    
}
