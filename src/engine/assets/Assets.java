package engine.assets;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import engine.graphics.SpriteSheet;
import engine.inputs.FontLoader;
import engine.utils.ImageLoader;

public class Assets
{
	public static Font font18, font28;

	private static final int width = 32, height = 32;
	public static BufferedImage healthHud, healtHud2, player, grass, stick, mouse, dirtTile, leaf, rock, rockTile, pine, inventory;
	public static BufferedImage[] playerDown, playerUp, playerLeft, playerRight, menuStart, fire;

	public static void init()
	{
		font18 = FontLoader.loadFont("res/fonts/font.ttf", 18);
		font28 = FontLoader.loadFont("res/fonts/font.ttf", 28);

		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/sheet.png"));
		player = sheet.crop(3 * width, 0, width, height);
		grass = sheet.crop(0, 0, width, height);
		mouse = sheet.crop(2 * width, 0, width, height);
		leaf = sheet.crop(width, 0, width, height);

		rock = ImageLoader.loadImage("/textures/rock.png");
		rockTile = ImageLoader.loadImage("/textures/rockTile.png");
		dirtTile = ImageLoader.loadImage("/textures/dirtTile.png");

		SpriteSheet playerSheet = new SpriteSheet(ImageLoader.loadImage("/textures/playerSheet.png"));
		playerDown = new BufferedImage[3];
		playerDown[0] = playerSheet.crop(0, 0, width, height);
		playerDown[1] = playerSheet.crop(width, 0, width, height);
		playerDown[2] = playerSheet.crop(2 * width, 0, width, height);
		playerLeft = new BufferedImage[3];
		playerLeft[0] = playerSheet.crop(0, height, width, height);
		playerLeft[1] = playerSheet.crop(width, height, width, height);
		playerLeft[2] = playerSheet.crop(2 * width, height, width, height);
		playerRight = new BufferedImage[3];
		playerRight[0] = playerSheet.crop(0, 2 * height, width, height);
		playerRight[1] = playerSheet.crop(width, 2 * height, width, height);
		playerRight[2] = playerSheet.crop(2 * width, 2 * height, width, height);
		playerUp = new BufferedImage[3];
		playerUp[0] = playerSheet.crop(0, 3 * height, width, height);
		playerUp[1] = playerSheet.crop(width, 3 * height, width, height);
		playerUp[2] = playerSheet.crop(2 * width, 3 * height, width, height);
		menuStart = new BufferedImage[2];

		menuStart[0] = leaf;
		menuStart[1] = mouse;

		pine = ImageLoader.loadImage("/textures/pineTree.png");
		stick = ImageLoader.loadImage("/textures/stick.png");
		inventory = ImageLoader.loadImage("/textures/inventoryScreen.png");

		SpriteSheet health = new SpriteSheet(ImageLoader.loadImage("/textures/hudSheet.png"));
		healthHud = health.crop(200, 650, 880, 70);

		SpriteSheet health2 = new SpriteSheet(ImageLoader.loadImage("/textures/hudSheet2.png"));
		healtHud2 = health2.crop(0, 0, 754, 24);

		BufferedImage f = ImageLoader.loadImage("/textures/fires.jpg");
		f = removeColor(f, Color.black.getRGB(), new Color(24, 24, 24).getRGB());
		SpriteSheet fireSheet = new SpriteSheet(f);
		fire = new BufferedImage[4 * 4];
		fire = fireSheet.cropArray(0, 0, 64, 64);
	}

	public static BufferedImage removeColor(BufferedImage image, int rgbMin, int rgbMax)
	{
		BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < res.getWidth(); x++)
		{
			for(int y = 0; y < res.getWidth(); y++)
			{
				if(image.getRGB(x, y) >= rgbMin && image.getRGB(x, y) <= rgbMax)
					continue;
				// res.setRGB(x, y, );
				// System.out.println(true + " " + x + " " + y);
				res.setRGB(x, y, image.getRGB(x, y));
			}

		}
		return res;
	}
}
