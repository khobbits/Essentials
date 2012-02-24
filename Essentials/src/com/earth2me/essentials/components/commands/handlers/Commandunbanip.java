package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;


public class Commandunbanip extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		try
		{
			@Cleanup
			final IUser user = getPlayer(args, 0, true);
			user.acquireReadLock();
			getContext().getServer().unbanIP(user.getData().getIpAddress());
		}
		catch (Exception ex)
		{
		}
		getContext().getServer().unbanIP(args[0]);
		sender.sendMessage(_("unbannedIP"));
	}
}