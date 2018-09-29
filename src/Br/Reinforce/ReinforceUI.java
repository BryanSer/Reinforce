/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.Reinforce;

import Br.API.GUI.Ex.BaseUI;
import Br.API.GUI.Ex.Item;
import Br.API.GUI.Ex.Snapshot;
import Br.API.GUI.Ex.SnapshotFactory;
import Br.API.ItemBuilder;
import Br.API.Utils;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.manager.AttributeManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 * @since 2018-9-29
 */
public class ReinforceUI extends BaseUI {

    public static final String LEVEL_SP = "§r§r§e§l§r";

    private static final int SLOT_ITEM = 10;
    private static final int SLOT_GEM = 12;
    private static final int SLOT_PROTECTED = 13;
    private static final int SLOT_BUTTON = 15;
    private static final int SLOT_RESULT = 16;
    private static Map<String, AnimeTask> Tasks = new HashMap<>();
    private SnapshotFactory Factory = SnapshotFactory.getDefaultSnapshotFactory(this);
    private Item[] Contains = new Item[27];

    {
        super.Rows = 3;
        super.AllowShift = false;
        super.DisplayName = "§6强化";
        super.Name = "R_UI";

        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 11, 15}) {
            final String id = String.format("id_%d", i);
            List<String> lore = new ArrayList<>();
            switch (i) {
                case 1:
                case 19:
                    lore.add("§6§l请在此处放入要强化的物品");
                    break;
                case 3:
                case 21:
                    lore.add("§b请在此处放入强化宝石");
                    break;
                case 4:
                case 22:
                    lore.add("§a此处可放入保护卷轴");
                    lore.add("§a保护你的物品在强化后不会消失");
                    break;
            }
            Contains[i] = Item
                    .getNewInstance((p) -> {
                        Snapshot s = this.getSnapshot(p);
                        Integer type = (Integer) s.getData(id);
                        ItemStack display = new ItemStack(Material.STAINED_GLASS_PANE);
                        display.setDurability(type == null ? 0 : type.shortValue());
                        if (!lore.isEmpty()) {
                            ItemMeta im = display.getItemMeta();
                            im.setLore(lore);
                            display.setItemMeta(im);
                        }
                        return display;
                    })
                    .setUpdateIcon(true);
        }
        Contains[SLOT_ITEM] = Item.getNewInstance((ItemStack) null)
                .setButtonCellback(p -> true)
                .setUpdateIcon(false);
        Contains[SLOT_GEM] = Item.getNewInstance((ItemStack) null)
                .setButtonCellback(p -> true)
                .setUpdateIcon(false);
        Contains[SLOT_PROTECTED] = Item.getNewInstance((ItemStack) null)
                .setButtonCellback(p -> true)
                .setUpdateIcon(false);
        Contains[SLOT_RESULT] = Item.getNewInstance((ItemStack) null)
                .setButtonCellback(p -> true)
                .setUpdateIcon(false);
        Contains[SLOT_BUTTON] = Item
                .getNewInstance((p) -> {
                    Snapshot s = this.getSnapshot(p);
                    double chance = 0;
                    ItemStack item = s.getInventory().getItem(SLOT_ITEM);
                    int lv = 0;
                    if (item != null) {
                        ItemMeta im = item.getItemMeta();
                        if (im.hasDisplayName() && im.getDisplayName().contains(LEVEL_SP)) {
                            String[] str = im.getDisplayName().split(LEVEL_SP);
                            lv = Integer.parseInt(str[1].replaceAll("[^0-9]", ""));
                        }
                        Reinforce r = Data.Reinforces.get(lv + 1);
                        if (r == null) {
                            return ItemBuilder
                                    .getBuilder(Material.BARRIER)
                                    .name("§c无法强化")
                                    .lore("§c已达到最高级")
                                    .build();
                        }
                        chance += r.getProbability();
                    } else {
                        return ItemBuilder
                                .getBuilder(Material.BARRIER)
                                .name("§c无法强化")
                                .lore("§c没有物品")
                                .build();
                    }
                    ItemStack gem = s.getInventory().getItem(SLOT_GEM);
                    boolean hasGem = false;
                    if (gem != null) {
                        if (gem.getAmount() > 1) {
                            return ItemBuilder
                                    .getBuilder(Material.BARRIER)
                                    .name("§c无法强化")
                                    .lore("§c强化宝石只能放入一个")
                                    .build();
                        }
                        Gem g = Data.matchGem(gem);
                        if (g != null) {
                            if (g.getMax() < lv) {
                                return ItemBuilder
                                        .getBuilder(Material.BARRIER)
                                        .name("§c无法强化")
                                        .lore("§c强化宝石等级过低")
                                        .build();
                            }
                            hasGem = true;
                            chance += g.getSuccess();
                        }
                    } else {
                        return ItemBuilder
                                .getBuilder(Material.BARRIER)
                                .name("§c无法强化")
                                .lore("§c没有宝石")
                                .build();
                    }
                    ItemStack prot = s.getInventory().getItem(SLOT_PROTECTED);
                    boolean hasPro = false;
                    if (prot != null) {
                        if (prot.getAmount() > 1) {
                            return ItemBuilder
                                    .getBuilder(Material.BARRIER)
                                    .name("§c无法强化")
                                    .lore("§c保护卷轴只能放入一个")
                                    .build();
                        }
                        Protect pro = Data.matchProtect(prot);
                        if (pro != null) {
                            hasPro = true;
                            if (pro.getMax() < lv) {
                                return ItemBuilder
                                        .getBuilder(Material.BARRIER)
                                        .name("§c无法强化")
                                        .lore("§c保护卷轴等级过低")
                                        .build();
                            }
                        }
                    }
                    if (chance <= 0 || !hasGem) {
                        return ItemBuilder
                                .getBuilder(Material.BARRIER)
                                .name("§c无法强化")
                                .lore("§c强化成功概率为0")
                                .build();
                    }
                    ItemBuilder ib = ItemBuilder.getBuilder(Material.ANVIL);
                    ib.name("§6点击强化");
                    ib.lore(String.format("§a§l强化成功概率为: %.1f%%", chance * 100d));
                    if (hasPro) {
                        ib.lore("§b保护卷轴使用中");
                    }
                    return ib.build();
                })
                .setClick(ClickType.LEFT, (p) -> {
                    Snapshot s = this.getSnapshot(p);
                    double chance = 0;
                    ItemStack item = s.getInventory().getItem(SLOT_ITEM);
                    int lv = 0;
                    Reinforce rein = null;
                    if (item != null) {
                        ItemMeta im = item.getItemMeta();
                        if (im.hasDisplayName() && im.getDisplayName().contains(LEVEL_SP)) {
                            String[] str = im.getDisplayName().split(LEVEL_SP);
                            lv = Integer.parseInt(str[1].replaceAll("[^0-9]", ""));
                        }
                        rein = Data.Reinforces.get(lv + 1);
                        if (rein == null) {
                            p.sendMessage("§c您已强化到最高级");
                            return;
                        }
                        chance += rein.getProbability();
                    } else {
                        return;
                    }
                    ItemStack gem = s.getInventory().getItem(SLOT_GEM);
                    boolean hasGem = false;
                    if (gem != null) {
                        if (gem.getAmount() > 1) {
                            p.sendMessage("§c强化宝石只能放入一个");
                            return;
                        }
                        Gem g = Data.matchGem(gem);
                        if (g != null) {
                            if (g.getMax() < lv) {
                                p.sendMessage("§c强化宝石等级过低 不能用于此强化");
                                return;
                            }
                            hasGem = true;
                            chance += g.getSuccess();
                        }
                    } else {
                        return;
                    }
                    ItemStack prot = s.getInventory().getItem(SLOT_PROTECTED);
                    boolean hasPro = false;
                    if (prot != null) {
                        if (prot.getAmount() > 1) {
                            p.sendMessage("§c保护卷轴只能放入一个");
                            return;
                        }
                        Protect pro = Data.matchProtect(prot);
                        if (pro != null) {
                            hasPro = true;
                            if (pro.getMax() < lv) {
                                p.sendMessage("§c保护卷轴等级过低 不能用于此强化");
                                return;
                            }
                        }
                    }
                    if (chance <= 0 || !hasGem) {
                        p.sendMessage("§c强化成功概率为0 无法强化");
                        return;
                    }
                    if (s.getInventory().getItem(SLOT_RESULT) != null) {
                        p.sendMessage("§c请将强化结果槽中的东西移除");
                        return;
                    }
                    s.getInventory().setItem(SLOT_GEM, null);
                    s.getInventory().setItem(SLOT_ITEM, null);
                    s.getInventory().setItem(SLOT_PROTECTED, null);
                    AnimeTask task = null;
                    if (Math.random() < chance) {
                        task = new AnimeTask(reinforce(item, rein), null, s, p, String.format("§6玩家[%s]成功的将武器强化到了+%d", p.getName(), rein.getLevel()));
                    } else {
                        task = new AnimeTask(prot != null ? item : null, null, s, p, String.format("§c玩家[%s]在强化到+%d时失败了 %s", p.getName(), rein.getLevel(), prot == null ? "" : "§6但还好使用了保护卷轴"));
                    }
                    task.runTaskTimer(Data.Plugin, 2, 2);
                    Tasks.put(p.getName(), task);
                });
    }

    public static class AnimeTask extends BukkitRunnable {

        private static int[][] TAR = {{0, 9, 18}, {1, 19}, {2, 11, 20}, {3, 21}, {4, 22}, {5, 23}, {6, 15, 24}, {7, 25}, {8, 17, 26}};

        private ItemStack result;
        private ItemStack protect;
        private Snapshot snap;
        private int time = 9;
        private Player player;
        private String msg;

        public AnimeTask(ItemStack result, ItemStack protect, Snapshot snap, Player p, String msg) {
            this.result = result;
            this.protect = protect;
            this.snap = snap;
            player = p;
            this.msg = msg;
        }

        public ItemStack getResult() {
            return result;
        }

        public ItemStack getProtect() {
            return protect;
        }

        @Override
        public void run() {
            for (int[] is : TAR) {
                for (int i : is) {
                    snap.setData(String.format("id_%d", i), 0);
                }
            }
            for (int ct = 0; ct < 5; ct++) {
                int t = time - ct % 9;
                int color = 0;
                switch (ct) {
                    case 0:
                        color = 14;
                        break;
                    case 1:
                        color = 1;
                        break;
                    case 2:
                        color = 4;
                        break;
                    case 3:
                        color = 5;
                        break;
                    case 4:
                        color = 3;
                        break;
                }
                for (int i : TAR[t]) {
                    snap.setData(String.format("id_%d", i), color);
                }
            }
            time++;
            if (time >= 40) {
                ItemStack is = snap.getInventory().getItem(SLOT_RESULT);
                if (is != null) {
                    Utils.safeDropItem(player, is);
                }
                snap.getInventory().setItem(SLOT_RESULT, result);
                this.cancel();
                Bukkit.broadcastMessage(this.msg);
                Tasks.remove(player.getName());
            }
        }

        public String getMsg() {
            return msg;
        }

    }

    @Override
    public void onClose(Player p, Snapshot s) {
        Inventory inv = s.getInventory();
        Utils.safeDropItem(p, inv.getItem(SLOT_GEM));
        Utils.safeDropItem(p, inv.getItem(SLOT_ITEM));
        Utils.safeDropItem(p, inv.getItem(SLOT_PROTECTED));
        Utils.safeDropItem(p, inv.getItem(SLOT_RESULT));
        AnimeTask t = Tasks.remove(p.getName());
        if (t != null) {
            t.cancel();
            Utils.safeDropItem(p, t.getResult());
            Bukkit.broadcastMessage(t.getMsg());
        }
    }

    @Override
    public Item getItem(Player p, int slot) {
        return Contains[slot];
    }

    @Override
    public SnapshotFactory getSnapshotFactory() {
        return Factory;
    }

    public static ItemStack reinforce(ItemStack ori, Reinforce to) {
        ItemStack is = ori.clone();
        ItemMeta im = is.getItemMeta();
        String name = "";
        if (im.hasDisplayName()) {
            name = im.getDisplayName().replaceAll(LEVEL_SP + ".*" + LEVEL_SP, "");
        }
        name += LEVEL_SP + " +" + to.getLevel() + LEVEL_SP;
        im.setDisplayName(name);
        List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<>();
        List<String> newLore = new ArrayList<>();
        for (String s : lore) {
            String match = ChatColor.stripColor(s);
            for (AttributeManager.Attribute attr : SkillAPI.getAttributeManager().getAttributes().values()) {
                if (match.contains(attr.getKey())) {
                    String old = match.replaceAll("[^0-9]", "");
                    if (old.isEmpty()) {
                        break;
                    }
                    int amount = Integer.parseInt(old);
                    amount *= (1d + to.getPower());
                    s = s.replaceFirst(old, String.valueOf(amount));
                    break;
                }
            }
            newLore.add(s);
        }
        im.setLore(newLore);
        is.setItemMeta(im);
        return is;
    }

}
