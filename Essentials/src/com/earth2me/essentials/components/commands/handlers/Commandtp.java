package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Console;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NoChargeException;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtp extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		switch (args.length)
		{
		case 0:
			throw new NotEnoughArgumentsException();

		case 1:
			@Cleanup
			final IUserComponent player = getPlayer(args, 0);
			player.acquireReadLock();
			if (!player.getData().isTeleportEnabled())
			{
				throw new Exception($("teleportDisabled", player.getDisplayName()));
			}
			user.sendMessage($("teleporting"));
			final Trade charge = new Trade(getCommandName(), getContext());
			charge.isAffordableFor(user);
			user.getTeleporter().teleport(player, charge, TeleportCause.COMMAND);
			throw new NoChargeException();

		default:
			if (!Permissions.TPOHERE.isAuthorized(user))
			{
				//TODO: Translate this
				throw new Exception("You need access to /tpohere to teleport other players.");
			}
			user.sendMessage($("teleporting"));
			final IUserComponent target = getPlayer(args, 0);
			final IUserComponent toPlayer = getPlayer(args, 1);
			target.getTeleporter().now(toPlayer, false, TeleportCause.COMMAND);
			target.sendMessage($("teleportAtoB", user.getDisplayName(), toPlayer.getDisplayName()));
			break;
		}
	}

	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		sender.sendMessage($("teleporting"));
		final IUserComponent target = getPlayer(args, 0);
		final IUserComponent toPlayer = getPlayer(args, 1);
		target.getTeleporter().now(toPlayer, false, TeleportCause.COMMAND);
		target.sendMessage($("teleportAtoB", Console.NAME, toPlayer.getDisplayName()));
	}
}
