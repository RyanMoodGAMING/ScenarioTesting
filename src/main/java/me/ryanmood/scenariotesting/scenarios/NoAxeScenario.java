package me.ryanmood.scenariotesting.scenarios;

import com.cryptomorin.xseries.XMaterial;
import lombok.AccessLevel;
import lombok.Getter;
import me.ryanmood.scenariotesting.ScenarioTesting;
import net.fateuhc.plugin.api.events.ScenarioToggleEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.HashSet;

@Getter
public class NoAxeScenario implements Listener {

    @Getter(AccessLevel.PRIVATE)
    private ScenarioTesting plugin;

    private HashSet<XMaterial> axes = new HashSet<>(Arrays.asList(XMaterial.NETHERITE_AXE, XMaterial.DIAMOND_AXE, XMaterial.GOLDEN_AXE,
            XMaterial.IRON_AXE, XMaterial.STONE_AXE, XMaterial.WOODEN_AXE));
    private boolean active;

    public NoAxeScenario(ScenarioTesting plugin) {
        this.plugin = plugin;

        this.getPlugin().getFateUHCAPI().getScenariosManager().registerScenario(true,
                XMaterial.DIAMOND_AXE.parseMaterial(),
                "NO_AXE",
                "No Axe",
                "Stop the player from crafting or using",
                "an axe.");

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onToggle(ScenarioToggleEvent event) {
        // If you wish to do anything when the scenario is toggled.
        if (!event.getScenario().getAlias().equals("NO_AXE")) return;
        if (event.isToEnable()) this.active = true;
        if (!event.isToEnable()) this.active = false;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!this.isActive()) return;

        XMaterial result = XMaterial.matchXMaterial(event.getInventory().getResult());
        if (this.getAxes().contains(result)) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(ChatColor.RED + "You can't craft axes due to a scenario.");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!this.isActive()) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        XMaterial currentItem = XMaterial.matchXMaterial(event.getCurrentItem().getType());
        if (this.getAxes().contains(currentItem)) {
            event.setCancelled(true);
            event.setCurrentItem(XMaterial.AIR.parseItem());
            event.getWhoClicked().sendMessage(ChatColor.RED + "The axe has been removed as they are currently disabled");
            return;
        }
    }

}
