package com.earth2me.essentials.components.commands;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentialsModule;
import com.earth2me.essentials.api.IPermission;
import com.earth2me.essentials.components.users.IUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public interface IEssentialsCommand extends IPermission
{
	void run(IUser user, Command cmd, String commandLabel, String[] args)
			throws Exception;

	void run(CommandSender sender, Command cmd, String commandLabel, String[] args)
			throws Exception;

	void init(IContext ess, String commandLabel);

	void setEssentialsModule(IEssentialsModule module);
}