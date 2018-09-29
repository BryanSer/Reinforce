/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Reinforce;

import Br.API.GUI.Ex.UIManager;
import Br.API.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 * @since 2018-9-28
 */
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Data.loadConfig(this);
        UIManager.RegisterUI(new ReinforceUI());
        Bukkit.getPluginManager().registerEvents(new ScrollListener(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 0) {
            UIManager.OpenUI((Player)sender, "R_UI"); //NOI18N
            return true;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) { //NOI18N
            return false;
        }
        if (args[0].equalsIgnoreCase("give") && sender.isOp()) { //NOI18N
            if (args.length < 4) {
                return false;
            }
            Player p = Bukkit.getPlayerExact(args[1]);
            if (p == null || !p.isOnline()) {
                sender.sendMessage(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("lang_ZH").getString("Command.NotOnline"), new Object[] {}));
                return true;
            }
            ConfigItem item = null;
            switch (args[2].toLowerCase()) {
                case "g":
                    item = Data.Gems.get(args[3]);
                    break;
                case "p":
                    item = Data.Protects.get(args[3]);
                    break;
                case "s":
                    item = Data.Scrolls.get(args[3]);
                    break;
            }
            if(item == null){
                sender.sendMessage(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("lang_ZH").getString("Command.ErrorKey"), new Object[] {}));
                return true;
            }
            ItemStack is = item.getItem();
            try {
                is.setAmount(Integer.parseInt(args[4]));
            } catch (Exception e) {
            }
            Utils.safeGiveItem(p, is);
            p.sendMessage(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("lang_ZH").getString("Command.Get"), new Object[] {}));
            sender.sendMessage(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("lang_ZH").getString("Command.Success"), new Object[] {}));
            return true;
        }
        return super.onCommand(sender, command, label, args);
    }

}
