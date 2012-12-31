package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandtpaall extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			if (isUser(sender))
			{
				teleportAAllPlayers(sender, ess.getUserMap().getUser((Player)sender));
				return;
			}
			throw new NotEnoughArgumentsException();
		}

		final IUser player = ess.getUserMap().matchUserExcludingHidden(args[0], getPlayerOrNull(sender));
		teleportAAllPlayers(sender, player);
	}

	private void teleportAAllPlayers(final CommandSender sender, final IUser user)
	{
		sender.sendMessage(_("teleportAAll"));
		for (Player onlinePlayer : server.getOnlinePlayers())
		{

			final IUser player = ess.getUserMap().getUser(onlinePlayer);
			if (user == player)
			{
				continue;
			}
			if (!player.getData().isTeleportEnabled())
			{
				continue;
			}

			ISettings settings = ess.getSettings();
			if (user.getPlayer().getWorld() != player.getPlayer().getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions() && !Permissions.WORLD.isAuthorized(
					user, user.getPlayer().getWorld().getName()))
			{
				continue;
			}
			try
			{
				player.requestTeleport(user, true);
				player.sendMessage(_("teleportHereRequest", user.getPlayer().getDisplayName()));
				player.sendMessage(_("typeTpaccept"));
				int tpaAcceptCancellation = 0;
				tpaAcceptCancellation = settings.getData().getCommands().getTeleport().getRequestTimeout();
				if (tpaAcceptCancellation != 0)
				{
					player.sendMessage(_("teleportRequestTimeoutInfo", tpaAcceptCancellation));
				}
			}
			catch (Exception ex)
			{
				ess.getCommandHandler().showCommandError(sender, commandName, ex);
			}
		}
	}
}
