package com.mtautumn.edgequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class BlockItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String blockImageDirectory = "blocks";
	private static final String itemImageDirectory = "items";
	private ArrayList<BufferedImage> blockImg = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> itemImg = new ArrayList<BufferedImage>();
	private boolean isItem;
	private boolean isBlock;
	private String name;
	private Short id;

	//---Battle---
	public int attackDamage = 1; //hit points attributed to target
	public byte wearPosition = -1; //-1 (nowhere), 0 (head), 1 (torso), 2 (legs), 3 (feet)
	public double defence = 0.0; //Percentage of damage removed when worn

	public boolean isLightSource = false;
	public boolean isHot = false;
	public boolean melts = false;
	public String meltsInto = "";
	public double hardness = 1.0; //seconds take to destroy with hands
	public boolean isLiquid = false;
	public boolean isPassable = false;
	public boolean canHavePrints = false;

	public BlockItem(int id, boolean isBlock, boolean isItem, String name, int[] blockAnimation, int[] itemAnimation) {
		this.isItem = isItem;
		this.isBlock = isBlock;
		this.id = (short)id;
		this.name = name;
		if (isBlock) {
			for (Short i = 0; i < blockAnimation.length; i++) {
				blockImg.add(getTexture(name + blockAnimation[i], blockImageDirectory));
			}
		}
		if (isItem) {
			for (Short i = 0; i < itemAnimation.length; i++) {
				itemImg.add(getTexture(name + itemAnimation[i], itemImageDirectory));
			}
		}
	}

	public BufferedImage getItemImg(int time) {
		if (isItem) return itemImg.get(time % itemImg.size());
		if (isBlock) return blockImg.get(time % blockImg.size());
		return new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
	}

	public BufferedImage getBlockImg(int time) {
		if (isBlock) return blockImg.get(time % blockImg.size());
		if (isItem) return itemImg.get(time % itemImg.size());
		return new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
	}

	public boolean getIsBlock() { return isBlock; }

	public boolean getIsItem() { return isItem; }

	public String getName() { return name; }

	public Short getID() { return id; }

	public boolean isName(String testName) { return testName.equals(name); }

	public boolean isID(Short testID) { return testID == id; }

	private BufferedImage getTexture(String name, String directory) {
		try {
			return (BufferedImage) ImageIO.read(new File("textures/" + directory + "/" + name + ".png"));
		} catch (Exception e) {
			System.out.println("Could not load texture: " + "textures/" + directory + "/" + name + ".png");
			return new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		}
	}
}
