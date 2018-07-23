package com.gmail.val59000mc.playuhc.mc1_13.commands;

import com.gmail.val59000mc.playuhc.mc1_13.exceptions.UhcPlayerDoesntExistException;
import com.gmail.val59000mc.playuhc.mc1_13.game.GameManager;
import com.gmail.val59000mc.playuhc.mc1_13.game.GameState;
import com.gmail.val59000mc.playuhc.mc1_13.languages.Lang;
import com.gmail.val59000mc.playuhc.mc1_13.players.PlayerState;
import com.gmail.val59000mc.playuhc.mc1_13.players.PlayersManager;
import com.gmail.val59000mc.playuhc.mc1_13.players.UhcPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommandExecutor implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if(sender instanceof Player){
			Player player = (Player) sender;
			GameManager gm = GameManager.getGameManager();
			PlayersManager pm = gm.getPlayersManager();
			UhcPlayer uhcPlayer;
			try {
				uhcPlayer = pm.getUhcPlayer(player);
				if(uhcPlayer != null 
						&& uhcPlayer.getState().equals(PlayerState.DEAD)
						&& gm.getGameState().equals(GameState.PLAYING)
						&& gm.getConfiguration().getSpectatingTeleport()){
					if(args.length == 1){
						Player target = Bukkit.getPlayer(args[0]);
						if(target == null){
							uhcPlayer.sendMessage(ChatColor.RED+ Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
						}else{
							UhcPlayer uhcTarget = pm.getUhcPlayer(target);
							if(uhcTarget.getState().equals(PlayerState.PLAYING)){
								uhcPlayer.sendMessage(ChatColor.GREEN+ Lang.COMMAND_SPECTATING_TELEPORT.replace("%player%", uhcTarget.getName()));
								player.teleport(target);
							}else{
								uhcPlayer.sendMessage(ChatColor.RED+ Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
							}
						}
							
					}else{
						uhcPlayer.sendMessage(ChatColor.RED+ Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
					}
				}else{
					uhcPlayer.sendMessage(ChatColor.RED+ Lang.COMMAND_SPECTATING_TELEPORT_ERROR);
				}
			} catch (UhcPlayerDoesntExistException e) {
			}

			return true;
		}
		

		return false;
	}

}
