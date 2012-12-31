package net.ess3.signs;

import java.io.File;
import java.util.*;
import org.bukkit.plugin.Plugin;
import net.ess3.api.IEssentials;
import net.ess3.storage.AsyncStorageObjectHolder;


public class SignsConfigHolder extends AsyncStorageObjectHolder<SignConfig>
{
	private final transient Plugin plugin;
	private Set<EssentialsSign> enabledSigns = new HashSet<EssentialsSign>();
	private boolean signsEnabled = false;

	public SignsConfigHolder(final IEssentials ess, final Plugin plugin)
	{
		super(ess, SignConfig.class, new File(plugin.getDataFolder(), "signs.yml"));
		this.plugin = plugin;
		onReload();
		final Map<String, Boolean> signs = getData().getSigns();
		for (Map.Entry<String, Boolean> entry : signs.entrySet())
		{
			if (entry.getKey().trim().toUpperCase(Locale.ENGLISH).equals("COLOR") || entry.getKey().trim().toUpperCase(Locale.ENGLISH).equals("COLOUR"))
			{
				signsEnabled = true;
				continue;
			}
			final Signs sign = Signs.valueOf(entry.getKey().toUpperCase(Locale.ENGLISH));
			if (sign != null && entry.getValue())
			{
				enabledSigns.add(sign.getSign());
				signsEnabled = true;
			}
		}

		final Map<String, Boolean> signs2 = new HashMap<String, Boolean>();
		for (Signs sign : Signs.values())
		{
			signs2.put(sign.toString(), enabledSigns.contains(sign.getSign()));
		}
		getData().setSigns(signs2);
		queueSave();
	}

	public Set<EssentialsSign> getEnabledSigns()
	{
		return enabledSigns;
	}

	public boolean areSignsDisabled()
	{
		return !signsEnabled;
	}
}
