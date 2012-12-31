package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;


public class Commandtpaccept extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (user.getTeleportRequester() == null)
		{
			throw new Exception(_("noPendingRequest"));
		}

		final IUser target = user.getTeleportRequester();
		if (target == null || !target.isOnline() || (user.isTpRequestHere() && !Permissions.TPAHERE.isAuthorized(
				target)) || (!user.isTpRequestHere() && !Permissions.TPA.isAuthorized(target) && !Permissions.TPAALL.isAuthorized(target)))
		{
			throw new Exception(_("noPendingRequest"));
		}

		if (args.length > 0 && !target.getName().contains(args[0]))
		{
			throw new Exception(_("noPendingRequest"));
		}


		ISettings settings = ess.getSettings();
		int tpaAcceptCancellation = settings.getData().getCommands().getTeleport().getRequestTimeout();

		if (tpaAcceptCancellation != 0 && (System.currentTimeMillis() - user.getTeleportRequestTime()) / 1000 > tpaAcceptCancellation)
		{
			user.requestTeleport(null, false);
			throw new Exception(_("requestTimedOut"));
		}

		final Trade charge = new Trade(commandName, ess);
		if (user.isTpRequestHere())
		{
			charge.isAffordableFor(user);
		}
		else
		{
			charge.isAffordableFor(target);
		}
		user.sendMessage(_("requestAccepted"));
		target.sendMessage(_("requestAcceptedFrom", user.getPlayer().getDisplayName()));

		if (user.isTpRequestHere())
		{
			target.getTeleport().teleportToMe(user, charge, TeleportCause.COMMAND);
		}
		else
		{
			target.getTeleport().teleport(user.getPlayer(), charge, TeleportCause.COMMAND);
		}
		user.requestTeleport(null, false);
		throw new NoChargeException();
	}
}
