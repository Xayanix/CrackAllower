package pl.xayanix.crackallower.listeners;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import pl.xayanix.crackallower.CrackAllower;
import pl.xayanix.crackallower.util.ChatUtil;

import java.lang.reflect.Field;
import java.util.Optional;

public class PlayerLoginListener implements Listener {

    private CrackAllower crackAllower;

    private static Class initialHandler;
    private static Field fieldUniqueId;
    private static Field fieldOfflineId;

    static {
        try {
            initialHandler = Class.forName("net.md_5.bungee.connection.InitialHandler");
            fieldUniqueId = initialHandler.getDeclaredField("uniqueId");
            fieldOfflineId = initialHandler.getDeclaredField("offlineId");

            fieldUniqueId.setAccessible(true);
            fieldOfflineId.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }



    public PlayerLoginListener(CrackAllower crackAllower){
        this.crackAllower = crackAllower;
        crackAllower.getProxy().getPluginManager().registerListener(crackAllower, this);
    }

    @EventHandler
    public void onLogin(final PreLoginEvent loginEvent){
        boolean isOnline = BungeeCord.getInstance().getPlayer(loginEvent.getConnection().getName()) != null;
        if(isOnline){
            loginEvent.setCancelled(true);
            loginEvent.setCancelReason(ChatUtil.fixColors(crackAllower.getConfiguration().getString("messages.isOnline")));
            return;
        }

        loginEvent.getConnection().setOnlineMode(this.crackAllower.getPremiumCheckBypassPlayers().stream().noneMatch(player -> player.equalsIgnoreCase(loginEvent.getConnection().getName())));
        if(!loginEvent.getConnection().isOnlineMode()){
            Optional<String> anyMatch = this.crackAllower.getPremiumCheckBypassPlayers().stream()
                    .filter(p -> p.equalsIgnoreCase(loginEvent.getConnection().getName()) && !p.equals(loginEvent.getConnection().getName()))
                    .findAny();
            if(anyMatch.isPresent()){
                String realName = anyMatch.get();
                loginEvent.setCancelled(true);
                loginEvent.setCancelReason(ChatUtil.fixColors(crackAllower.getConfiguration().getString("messages.invaildName").replace("{nick1}", loginEvent.getConnection().getName()).replace("{nick2}", realName)));
                return;
            }
        }
    }

    @EventHandler(priority = -128)
    public void onLogin(final LoginEvent loginEvent) throws IllegalAccessException {

        fieldUniqueId.set(loginEvent.getConnection(), fieldOfflineId.get(loginEvent.getConnection()));

        /*if(loginEvent.getConnection().isOnlineMode() && this.crackAllower.getPremiumCheckBypassPlayers().stream().anyMatch(player -> player.equalsIgnoreCase(loginEvent.getConnection().getName()))){
            loginEvent.setCancelled(true);
            loginEvent.setCancelReason(ChatUtil.fixColors(crackAllower.getConfiguration().getString("messages.nicknameTaken")));
        }*/
    }

}
