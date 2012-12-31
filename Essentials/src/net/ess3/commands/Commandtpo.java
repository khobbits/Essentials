package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandtpo extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		//Just basically the old tp command
		final IUser player = ess.getUserMap().matchUser(args[0], false);

		// Verify permission
		if (user.getPlayer().canSee(player.getPlayer()) || Permissions.TELEPORT_HIDDEN.isAuthorized(user))
		{
			user.getTeleport().now(player.getPlayer(), false, TeleportCause.COMMAND);
			user.sendMessage(_("teleporting"));
		}
		else
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
