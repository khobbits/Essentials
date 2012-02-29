package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;


public class Commandlightning extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{

		IUserComponent user = null;
		if (sender instanceof Player)
		{
			user = getContext().getUser(((Player)sender));
		}
		if (args.length < 1 & user != null)
		{
			user.getWorld().strikeLightning(user.getTargetBlock(null, 600).getLocation());
			return;
		}

		if (getServer().matchPlayer(args[0]).isEmpty())
		{
			throw new Exception($("playerNotFound"));
		}

		int power = 1;
		if (args.length > 1)
		{
			try
			{
				power = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException ex)
			{
			}
		}

		for (Player matchPlayer : getServer().matchPlayer(args[0]))
		{
			sender.sendMessage($("lightningUse", matchPlayer.getDisplayName()));
			if (power <= 0)
			{
				matchPlayer.getWorld().strikeLightningEffect(matchPlayer.getLocation());
			}
			else
			{
				LightningStrike strike = matchPlayer.getWorld().strikeLightning(matchPlayer.getLocation());
				matchPlayer.damage(power - 1, strike);
			}
			if (!getContext().getUser(matchPlayer).isGodModeEnabled())
			{
				matchPlayer.setHealth(matchPlayer.getHealth() < 5 ? 0 : matchPlayer.getHealth() - 5);
			}
			@Cleanup
			final ISettingsComponent settings = getContext().getSettings();
			settings.acquireReadLock();
			if (settings.getData().getCommands().getLightning().isWarnPlayer())
			{
				matchPlayer.sendMessage($("lightningSmited"));
			}
		}
	}
}
