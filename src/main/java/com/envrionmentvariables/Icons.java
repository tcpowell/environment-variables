package com.envrionmentvariables;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import net.runelite.client.util.ImageUtil;

public final class Icons
{
	static final ImageIcon SHOW_ICON;
	static final ImageIcon SHOW_ICON_HOVER;
	static final ImageIcon HIDE_ICON;
	static final ImageIcon HIDE_ICON_HOVER;
	static final ImageIcon COPY_ICON;
	static final ImageIcon COPY_ICON_HOVER;

	static
	{
		final BufferedImage showImg = ImageUtil.loadImageResource(EnvironmentVariablesPlugin.class, "/visible_icon.png");
		SHOW_ICON = new ImageIcon(showImg);
		SHOW_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset(showImg, 0.53f));

		final BufferedImage hideImg = ImageUtil.loadImageResource(EnvironmentVariablesPlugin.class, "/invisible_icon.png");
		HIDE_ICON = new ImageIcon(hideImg);
		HIDE_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset(hideImg, 0.53f));

		final BufferedImage copyImg = ImageUtil.loadImageResource(EnvironmentVariablesPlugin.class, "/copy_icon.png");
		COPY_ICON = new ImageIcon(copyImg);
		COPY_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset(copyImg, 0.53f));
	}
}
