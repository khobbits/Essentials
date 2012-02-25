package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.EssentialsConf;
import com.earth2me.essentials.api.IReloadable;
import java.io.File;
import java.util.*;


public class UserManagerComponent implements IReloadable
{
	private final transient EssentialsConf users;
	private final transient List<String> spyusers = new ArrayList<String>();
	private final static String ADDRESS = "address";
	private final static String SPY = "spy";

	public UserManagerComponent(final File folder)
	{
		users = new EssentialsConf(new File(folder, "users.yml"));
		reload();
	}

	public final boolean isSpy(final String username)
	{
		return users.getBoolean(username.toLowerCase(Locale.ENGLISH) + "." + SPY, false);
	}

	public void setSpy(final String username, final boolean spy)
	{
		setUser(username.toLowerCase(Locale.ENGLISH), getAddress(username), spy);
	}

	public final String getAddress(final String username)
	{
		return users.getString(username.toLowerCase(Locale.ENGLISH) + "." + ADDRESS, null);
	}

	public final String getUserByAddress(final String search)
	{
		final Set<String> usernames = users.getKeys(false);
		for (String username : usernames)
		{
			final String address = users.getString(username + "." + ADDRESS, null);
			if (address != null && search.equalsIgnoreCase(address))
			{
				return username;
			}
		}
		return null;
	}

	public void setAddress(final String username, final String address)
	{
		setUser(username.toLowerCase(Locale.ENGLISH), address, isSpy(username));
	}

	public List<String> getSpyUsers()
	{
		return spyusers;
	}

	private void setUser(final String username, final String address, final boolean spy)
	{
		final Map<String, Object> userdata = new HashMap<String, Object>();
		userdata.put(ADDRESS, address);
		userdata.put(SPY, spy);
		users.setProperty(username, userdata);
		users.save();
		reload();
	}

	@Override
	public final void reload()
	{
		users.load();
		spyusers.clear();
		final Set<String> keys = users.getKeys(false);
		for (String key : keys)
		{
			if (isSpy(key))
			{
				final String address = getAddress(key);
				if (address != null)
				{
					spyusers.add(address);
				}
			}
		}
	}
}