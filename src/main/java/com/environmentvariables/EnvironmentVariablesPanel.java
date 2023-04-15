package com.environmentvariables;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.FlatTextField;

public class EnvironmentVariablesPanel extends PluginPanel
{
	protected final EnvironmentVariablesPlugin plugin;

	private final JPanel titlePanel = new JPanel(new BorderLayout());
	private final JLabel title = new JLabel();

	private final JLabel toggleVisibility = new JLabel(Icons.SHOW_ICON);
	private final JLabel copyString = new JLabel(Icons.COPY_ICON);

	@Getter
	public boolean maskVariables = false;

	public EnvironmentVariablesPanel(EnvironmentVariablesPlugin plug, Map<String, String> targetVariables, String concatString)
	{
		super();

		this.plugin = plug;
		maskVariables = plugin.isMasked();
		toggleVisibility.setIcon(maskVariables ? Icons.HIDE_ICON : Icons.SHOW_ICON);

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel presetActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));

		toggleVisibility.setToolTipText("Toggle Variable Visibility");
		toggleVisibility.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				toggleVisibility.setIcon(maskVariables ? Icons.SHOW_ICON_HOVER : Icons.HIDE_ICON_HOVER);
				maskVariables = !maskVariables;
				plugin.saveConfig();
				toggleMask(targetVariables);
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				toggleVisibility.setIcon(maskVariables ? Icons.HIDE_ICON_HOVER : Icons.SHOW_ICON_HOVER);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				toggleVisibility.setIcon(maskVariables ? Icons.HIDE_ICON : Icons.SHOW_ICON);
			}
		});

		copyString.setToolTipText("Copy Environment String to Clipboard");
		copyString.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				final StringSelection selection = new StringSelection(concatString);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

				JOptionPane.showMessageDialog(copyString,
					"Concatenated variable string copied to clipboard.",
					"Success",
					JOptionPane.PLAIN_MESSAGE);
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				copyString.setIcon(Icons.COPY_ICON_HOVER);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				copyString.setIcon(Icons.COPY_ICON);
			}
		});

		title.setText("Environment Variables");
		title.setForeground(Color.WHITE);
		title.setBorder(new EmptyBorder(0, 0, 0, 40));

		presetActions.add(toggleVisibility);
		presetActions.add(copyString);

		titlePanel.add(title, BorderLayout.WEST);
		titlePanel.add(presetActions, BorderLayout.EAST);

		add(titlePanel);

		add(Box.createRigidArea(new Dimension(5, 40)));

		//Add components
		for (Map.Entry<String, String> entry : targetVariables.entrySet())
		{
			addComponent(entry.getKey(), entry.getValue(), maskVariables);
		}

	}

	private JTextField addComponent(String label, String value, boolean hidden)
	{
		if (hidden)
		{
			value = maskString(value);
		}

		final JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.setName(label + "container");

		final JLabel uiLabel = new JLabel(label);
		final FlatTextField uiInput = new FlatTextField();

		uiInput.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		uiInput.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		uiInput.setBorder(new EmptyBorder(5, 7, 5, 7));
		uiInput.setName(label);
		uiInput.setText(value);

		uiLabel.setFont(FontManager.getRunescapeSmallFont());
		uiLabel.setBorder(new EmptyBorder(0, 0, 4, 0));
		uiLabel.setForeground(Color.WHITE);

		container.add(uiLabel, BorderLayout.NORTH);
		container.add(uiInput, BorderLayout.CENTER);

		add(container);
		add(Box.createRigidArea(new Dimension(5, 20)));

		return uiInput.getTextField();
	}

	private void toggleMask(Map<String, String> targetVariables)
	{
		for (Map.Entry<String, String> entry : targetVariables.entrySet())
		{
			for (Component component : getComponents())
			{
				if (component instanceof JPanel && component.getName() != null && component.getName().equals(entry.getKey() + "container"))
				{
					for (Component child : ((JPanel) component).getComponents())
					{
						if (child instanceof FlatTextField && child.getName() != null && child.getName().equals(entry.getKey()))
						{
							((FlatTextField) child).setText(maskVariables ? maskString(entry.getValue()) : entry.getValue());
						}
					}
				}
			}
		}
	}

	private String maskString(String inputString)
	{
		return IntStream.range(0, inputString.length()).mapToObj(i -> "*").collect(Collectors.joining());
	}

}