package me.ryanmood.scenariotesting;

import lombok.Getter;
import me.ryanmood.scenariotesting.scenarios.NoAxeScenario;
import me.ryanmood.scenariotesting.scenarios.NoPickaxeScenario;
import net.fateuhc.plugin.api.FateUHCAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ScenarioTesting extends JavaPlugin {

    private FateUHCAPI fateUHCAPI;

    private NoAxeScenario noAxeScenario;
    private NoPickaxeScenario noPickaxeScenario;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("FateUHC") != null) {
            this.fateUHCAPI = FateUHCAPI.INSTANCE;
            this.noAxeScenario = new NoAxeScenario(this);
            this.noPickaxeScenario = new NoPickaxeScenario(this);
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Loaded ScenarioTesting and the scenarios.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
