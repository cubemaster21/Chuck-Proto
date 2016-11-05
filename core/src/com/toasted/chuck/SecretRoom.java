package com.toasted.chuck;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.toasted.chuck.entities.Entity;

public class SecretRoom {
	public ArrayList<Rectangle> entityHiders = new ArrayList<Rectangle>();
	public ArrayList<Entity> toSpawnOnReveal = new ArrayList<Entity>();
	String roomName;
}
