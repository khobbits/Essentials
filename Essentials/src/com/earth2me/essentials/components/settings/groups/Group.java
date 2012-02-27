package com.earth2me.essentials.components.settings.groups;

import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Group implements IStorageObject
{
	@Comment("Message format of chat messages")
	private String messageFormat;
	@Comment("Prefix for name")
	private String prefix;
	@Comment("Suffix for name")
	private String suffix;
	@Comment("Amount of homes a player can have")
	private Integer homes;
	@Comment("Cooldown between teleports")
	private Integer teleportCooldown;
	@Comment("Delay before teleport")
	private Integer teleportDelay;
	@Comment("Cooldown between heals")
	private Integer healCooldown;
}