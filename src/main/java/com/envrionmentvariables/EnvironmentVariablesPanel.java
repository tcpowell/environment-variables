package com.envrionmentvariables;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.FlatTextField;

public class EnvironmentVariablesPanel extends PluginPanel
{
	private final JPanel titlePanel = new JPanel(new BorderLayout());
	private final JLabel title = new JLabel();

	private final JLabel toggleVisibility = new JLabel(Icons.SHOW_ICON);
	private final JLabel copyString = new JLabel(Icons.COPY_ICON);

	public EnvironmentVariablesPanel(EnvironmentVariablesConfig config, Map<String, String> targetVariables, String concatString)
	{
		super();

		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel presetActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));

		toggleVisibility.setToolTipText("Toggle Variable Visibility");
		toggleVisibility.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				toggleVisibility.setIcon(Icons.SHOW_ICON);
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				toggleVisibility.setIcon(Icons.SHOW_ICON_HOVER);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				toggleVisibility.setIcon(Icons.SHOW_ICON);
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
			addComponent(entry.getKey(), entry.getValue(), config.maskvariables());
		}

	}

	private JTextField addComponent(String label, String value, boolean hidden)
	{
		if (hidden)
		{
			String masked = "";
			for (int i = 0; i < value.length(); i++)
			{
				masked = masked + "*";
			}
			value = masked;
		}

		final JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		final JLabel uiLabel = new JLabel(label);
		final FlatTextField uiInput = new FlatTextField();

		uiInput.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		uiInput.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
		uiInput.setBorder(new EmptyBorder(5, 7, 5, 7));
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
}