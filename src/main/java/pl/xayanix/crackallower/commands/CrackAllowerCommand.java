package pl.xayanix.crackallower.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import pl.xayanix.crackallower.CrackAllower;
import pl.xayanix.crackallower.util.ChatUtil;

import java.util.stream.Collectors;

public class CrackAllowerCommand extends Command {

    private CrackAllower crackAllower;

    public CrackAllowerCommand(CrackAllower crackAllower) {
        super("premium", "crackallower.admin");
        this.crackAllower = crackAllower;
        crackAllower.getProxy().getPluginManager().registerCommand(crackAllower, this);
    }


    @Override
    public void execute(CommandSender sender, String args[]) {
        if(args.length == 0) {
            sender.sendMessage(ChatUtil.fixColors(crackAllower.getConfiguration().getString("messages.invaildCommand")));
            return;
        }

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("list")){
                sender.sendMessage(ChatUtil.fixColors(crackAllower.getConfiguration().getString("messages.listPremium").replace("{PLAYERS}", String.join(", ", crackAllower.getPremiumCheckBypassPlayers()))));
                return;
            }

            sender.sendMessage(ChatUtil.fixColors(crackAllower.getConfiguration().getString("messages.invaildCommand")));
            return;
        }

        if(args[0].equalsIgnoreCase("add")){
            if(!crackAllower.getPremiumCheckBypassPlayers().contains(args[1]))
                crackAllower.getPremiumCheckBypassPlayers().add(args[1]);

            crackAllower.saveConfiguration();
            sender.sendMessage(ChatUtil.fixColors(crackAllower.getConfiguration().getString("messages.addedToList").replace("{PLAYER}", args[1])));

        }

        if(args[0].equalsIgnoreCase("remove")){
            crackAllower.getPremiumCheckBypassPlayers().remove(args[1]);
            crackAllower.saveConfiguration();
            sender.sendMessage(ChatUtil.fixColors(crackAllower.getConfiguration().getString("messages.removedFromList").replace("{PLAYER}", args[1])));

        }

    }

}
