package com.envrionmentvariables;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EnvironmentVariablesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EnvironmentVariablesPlugin.class);
		RuneLite.main(args);
	}
}