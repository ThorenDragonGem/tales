package engine.graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet
{
	private BufferedImage sheet;

	public SpriteSheet(BufferedImage sheet)
	{
		this.sheet = sheet;
	}

	public BufferedImage crop(int x, int y, int width, int height)
	{
		return sheet.getSubimage(x, y, width, height);
	}

	public BufferedImage[] cropArray(int startX, int startY, int width, int height)
	{
		BufferedImage[] res = new BufferedImage[(sheet.getWidth() / width) * (sheet.getHeight() / height)];
		int i = 0;
		for(int x = startX; x < sheet.getWidth(); x += width)
		{
			for(int y = startY; y < sheet.getHeight(); y += height)
			{
				res[i++] = crop(x, y, width, height);
			}
		}
		return res;
	}
}
