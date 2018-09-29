/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Reinforce;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 * @since 2018-9-28
 */
public class Data {

    public static Main Plugin;
    public static Map<String, Gem> Gems = new HashMap<>();
    public static Map<String, Protect> Protects = new HashMap<>();
    public static Map<String, Scroll> Scrolls = new HashMap<>();
    public static Map<Integer, Reinforce> Reinforces = new HashMap<>();

    public static Scroll matchScroll(ItemStack is) {
        for (Scroll s : Scrolls.values()) {
            if (s.isSimilar(is)) {
                return s;
            }
        }
        return null;
    }

    public static Gem matchGem(ItemStack is) {
        for (Gem g : Gems.values()) {
            if (g.isSimilar(is)) {
                return g;
            }
        }
        return null;
    }

    public static Protect matchProtect(ItemStack is) {
        for (Protect p : Protects.values()) {
            if (p.isSimilar(is)) {
                return p;
            }
        }
        return null;
    }

    public static void loadConfig(Main plugin) {
        Plugin = plugin;
        if (!plugin.getDataFolder().exists()) {
            plugin.saveDefaultConfig();
        }
        File f = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

        ConfigurationSection lv = config.getConfigurationSection("Reinfore.Level");
        for (String key : lv.getKeys(false)) {
            Reinforce ri = new Reinforce(lv.getConfigurationSection(key));
            Reinforces.put(ri.getLevel(), ri);
        }

        ConfigurationSection scr = config.getConfigurationSection("Scroll");
        for (String key : scr.getKeys(false)) {
            Scrolls.put(key, new Scroll(scr.getConfigurationSection(key)));
        }

        ConfigurationSection gem = config.getConfigurationSection("Gem");
        for (String key : gem.getKeys(false)) {
            Gems.put(key, new Gem(gem.getConfigurationSection(key)));
        }

        ConfigurationSection pro = config.getConfigurationSection("Protect");
        for (String key : pro.getKeys(false)) {
            Protects.put(key, new Protect(pro.getConfigurationSection(key)));
        }
    }
}
