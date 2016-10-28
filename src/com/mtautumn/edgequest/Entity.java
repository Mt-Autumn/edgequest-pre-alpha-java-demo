package com.mtautumn.edgequest;

import java.util.ArrayList;

import com.mtautumn.edgequest.PathFinder.IntCoord;
import com.mtautumn.edgequest.data.DataManager;

public class Entity {
	private int entityID;
	private String entityTexture;
	private byte entityType; //0 = player, 1 = villager, 2 = pet, 3 = passive creature, 4 = hostile creature
	private String nameTag = "";
	private double posX, posY;
	private double moveSpeed = 1.0;
	private int destinationX, destinationY;
	private byte rotation;
	private PathFinder aStar;
	private ArrayList<IntCoord> path;
	private DataManager dm;
	private long lastUpdate;
	
	public Entity(int iD, String texture, byte type, DataManager dm) {
		this.entityID = iD;
		this.entityTexture = texture;
		this.entityType = type;
		this.dm = dm;
	}
	public Entity(int iD, String texture, byte type, double posX, double posY, byte rotation, DataManager dm) {
		this.entityID = iD;
		this.entityTexture = texture;
		this.entityType = type;
		this.posX = posX;
		this.posY = posY;
		this.rotation = rotation;
		this.dm = dm;
	}
	
	
	public double getX() {
		return posX;
	}
	public double getY() {
		return posY;
	}
	public byte getRot() {
		return rotation;
	}
	public int getID() {
		return entityID;
	}
	public String getTextureName() {
		return entityTexture;
	}
	public byte getType() {
		return entityType;
	}
	public String getNameTag() {
		return nameTag;
	}
	public boolean hasNameTag() {
		return !nameTag.equals("");
	}
	
	public void setX(double x) {
		posX = x;
	}
	public void setY(double y) {
		posY = y;
	}
	public void setRot(byte rot) {
		rotation = rot;
	}
	public void changeNameTag(String tag) {
		nameTag = tag;
	}
	public void setDestination(int x, int y) {
		destinationX = x;
		destinationY = y;
		aStar = new PathFinder(dm);
		path = aStar.findPath((int) posX, (int) posY, destinationX, destinationY, dm);
	}
	public void reCalculatePath() {
		if (aStar != null) {
			path = aStar.findPath((int) posX, (int) posY, destinationX, destinationY, dm);
		}
	}
	public void update() {
		if (lastUpdate == 0L) lastUpdate = System.currentTimeMillis();
		if (path != null) {
			if (path.size() > 0) {
				if (approachPoint(path.get(path.size() - 1), System.currentTimeMillis() - lastUpdate)) { //returns true if arrived at point
					path.remove(path.size() - 1);
				}
			}
		}
		lastUpdate = System.currentTimeMillis();
	}
	private boolean approachPoint(IntCoord point, long timeStep) {
		double xSpeed = Math.signum(point.x - posX) * Double.valueOf(timeStep) / 1000.0 * moveSpeed;
		double ySpeed = Math.signum(point.y - posY) * Double.valueOf(timeStep) / 1000.0 * moveSpeed;
		if (xSpeed != 0 && ySpeed != 0) {
			xSpeed *= 0.7071067812;
			ySpeed *= 0.7071067812;
		}
		if ((posX + xSpeed > point.x && posX < point.x) || (posX + xSpeed < point.x && posX > point.x)) {
			xSpeed = point.x - posX;
		}
		if ((posY + ySpeed > point.y && posY < point.y) || (posY + ySpeed < point.y && posY > point.y)) {
			ySpeed = point.y - posY;
		}
		
		if (checkMoveProposal(xSpeed, true)) {
			posX += xSpeed;
		}
		if (checkMoveProposal(ySpeed, false)) {
			posY += ySpeed;
		}
		updateRotation(xSpeed, ySpeed);
		return (Math.abs(posX - point.x) < 0.01 && Math.abs(posY - point.y) < 0.01);
	}
	private boolean checkMoveProposal(double speed, boolean isX) {
		int entityX;
		int entityY;
		if (isX) {
			entityX = (int) Math.floor(speed + posX);
			entityY = (int) Math.floor(posY);
		} else {
			entityY = (int) Math.floor(speed + posY);
			entityX = (int) Math.floor(posX);
		}
		if (dm.world.isStructBlock(entityX, entityY)) {
			return (dm.system.blockIDMap.get(dm.world.getStructBlock(entityX, entityY)).isPassable);
		}
		return true;
	}
	private void updateRotation(double xSpeed, double ySpeed) {
		if(ySpeed < 0 && xSpeed == 0) {
			rotation = 0;
		} else if (ySpeed < 0 && xSpeed < 0) {
			rotation = 7;
		} else if (ySpeed < 0 && xSpeed > 0) {
			rotation = 1;
		} else if (ySpeed == 0 && xSpeed < 0) {
			rotation = 6;
		} else if (ySpeed == 0 && xSpeed > 0) {
			rotation = 2;
		} else if (ySpeed > 0 && xSpeed < 0) {
			rotation = 5;
		} else if (ySpeed > 0 && xSpeed == 0) {
			rotation = 4;
		} else if (ySpeed > 0 && xSpeed > 0) {
			rotation = 3;
		}
	}
}
