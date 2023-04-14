package com.envrionmentvariables;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("environmentvariables")
public interface EnvironmentVariablesConfig extends Config
{
	@ConfigItem(
		keyName = "maskvariables",
		name = "Mask Variables",
		description = "Replaces variables with asterisks",
		hidden = true
	)
	default boolean maskvariables()
	{
		return true;
	}

}
