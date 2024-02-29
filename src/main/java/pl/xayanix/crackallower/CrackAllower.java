package pl.xayanix.crackallower;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.xayanix.crackallower.commands.CrackAllowerCommand;
import pl.xayanix.crackallower.listeners.PlayerLoginListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Getter
public class CrackAllower extends Plugin {

    private List<String> premiumCheckBypassPlayers = Lists.newArrayList();
    private Configuration configuration;

    @Override
    public void onEnable(){
        this.saveDefaultConfiguration();
        this.loadConfiguration();

        new PlayerLoginListener(this);
        new CrackAllowerCommand(this);
    }


    public void loadConfiguration(){
        try {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            this.premiumCheckBypassPlayers = this.configuration.getStringList("premiumCheckBypassPlayers");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveConfiguration(){
        try {
            this.configuration.set("premiumCheckBypassPlayers", this.premiumCheckBypassPlayers);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.configuration, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDefaultConfiguration(){
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
