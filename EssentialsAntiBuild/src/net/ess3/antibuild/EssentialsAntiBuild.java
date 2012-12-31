package net.ess3.antibuild;

import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsAntiBuild extends JavaPlugin implements IAntiBuild
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private transient EssentialsConnect ess = null;
	private transient AntiBuildHolder settings = null;

	@Override
	public void onEnable()
	{
		final PluginManager pm = this.getServer().getPluginManager();
		final Plugin essPlugin = pm.getPlugin("Essentials-3");
		if (essPlugin == null || !essPlugin.isEnabled())
		{
			return;
		}
		ess = new EssentialsConnect(essPlugin, this);

		final EssentialsAntiBuildListener blockListener = new EssentialsAntiBuildListener(this);
		pm.registerEvents(blockListener, this);
	}

	@Override
	public EssentialsConnect getEssentialsConnect()
	{
		return ess;
	}

	@Override
	public AntiBuildHolder getSettings()
	{
		return settings;
	}

	@Override
	public void setSettings(final AntiBuildHolder settings)
	{
		this.settings = settings;
	}
}
