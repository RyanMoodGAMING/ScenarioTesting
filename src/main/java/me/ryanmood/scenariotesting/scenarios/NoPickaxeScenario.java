package me.ryanmood.scenariotesting.scenarios;

import com.cryptomorin.xseries.XMaterial;
import lombok.AccessLevel;
import lombok.Getter;
import me.ryanmood.scenariotesting.ScenarioTesting;
import net.fateuhc.plugin.api.data.ScenarioData;
import net.fateuhc.plugin.api.events.ScenarioToggleEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.HashSet;

@Getter
public class NoPickaxeScenario implements Listener {

    @Getter(AccessLevel.PRIVATE)
    private ScenarioTesting plugin;

    private HashSet<XMaterial> pickaxes = new HashSet<>(Arrays.asList(XMaterial.NETHERITE_PICKAXE, XMaterial.DIAMOND_PICKAXE, XMaterial.GOLDEN_PICKAXE,
            XMaterial.IRON_PICKAXE, XMaterial.STONE_PICKAXE, XMaterial.WOODEN_PICKAXE));
    private ScenarioData scenario;

    public NoPickaxeScenario(ScenarioTesting plugin) {
        this.plugin = plugin;

        this.scenario = new ScenarioData(true,
                Material.DIAMOND_PICKAXE,
                "NO_PICKAXE",
                "No Pickaxe",
                "Stop the player from crafting or using",
                "a pickaxe.");

        this.getPlugin().getFateUHCAPI().getScenariosManager().registerScenario(this.scenario);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onToggle(ScenarioToggleEvent event) {
        // If you wish to do anything when the scenario is toggled.
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!this.getScenario().isActive()) return;

        XMaterial result = XMaterial.matchXMaterial(event.getInventory().getResult());
        if (this.getPickaxes().contains(result)) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(ChatColor.RED + "You can't craft axes due to a scenario.");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!this.getScenario().isActive()) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        XMaterial currentItem = XMaterial.matchXMaterial(event.getCurrentItem().getType());
        if (this.getPickaxes().contains(currentItem)) {
            event.setCancelled(true);
            event.setCurrentItem(XMaterial.AIR.parseItem());
            event.getWhoClicked().sendMessage(ChatColor.RED + "The axe has been removed as they are currently disabled");
            return;
        }
    }
}
