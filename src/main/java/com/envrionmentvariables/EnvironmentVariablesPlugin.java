package com.envrionmentvariables;

import com.google.inject.Provides;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
	name = "Environment Variables"
)
public class EnvironmentVariablesPlugin extends Plugin
{
	private static List<String> JAGEX_VARIABLES = Arrays.asList(new String[]{"JX_CHARACTER_ID", "JX_DISPLAY_NAME", "JX_REFRESH_TOKEN", "JX_SESSION_ID", "JX_ACCESS_TOKEN"});
	private static String JAGEX_PREFIX = "JX_";
	private static String ICON_PATH = "/panel_icon.png";
	protected static final String CONFIG_GROUP = "environmentvariables";

	private static Map<String, String> environmentVariables = System.getenv();

	private NavigationButton navButton;

	private EnvironmentVariablesPanel panel;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ConfigManager configmanager;

	@Inject
	private EnvironmentVariablesConfig config;

	@Override
	protected void startUp() throws Exception
	{

		Map<String, String> targetVariables = new TreeMap<String, String>();

		// Grab known Jagex environment variables
		for (String jagexKey : JAGEX_VARIABLES)
		{
			targetVariables.put(jagexKey, nvl(environmentVariables.get(jagexKey), ""));
		}

		// Identify any new Jagex variables
		for (Map.Entry<String, String> entry : environmentVariables.entrySet())
		{
			if (entry.getKey().startsWith(JAGEX_PREFIX) && !JAGEX_VARIABLES.contains(entry.getKey()))
			{
				log.warn("New Jagex Variable detected: " + entry.getKey());
				targetVariables.put(entry.getKey(), nvl(entry.getValue(), ""));
			}
		}

		String concatString = "";
		for (Map.Entry<String, String> entry : targetVariables.entrySet())
		{
			if (entry.getValue() != null && !entry.getValue().equals(""))
			{
				concatString = concatString + entry.getKey() + "=" + entry.getValue() + ";";
			}
		}

		//Add the panel to the client toolbar
		panel = new EnvironmentVariablesPanel(this, targetVariables, concatString);
		navButton = NavigationButton.builder()
			.tooltip("Environment Variables")
			.icon(ImageUtil.loadImageResource(getClass(), ICON_PATH))
			.priority(6)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);

	}

	public static String nvl(String input, String replacement)
	{
		return input == null ? replacement : input;
	}

	public boolean isMasked()
	{
		return config.maskvariables();
	}

	@Override
	protected void shutDown() throws Exception
	{
		saveConfig();
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
	}

	public void saveConfig()
	{
		configmanager.setConfiguration(CONFIG_GROUP, "maskvariables", panel.isMaskVariables());
	}

	@Provides
	EnvironmentVariablesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EnvironmentVariablesConfig.class);
	}
}
