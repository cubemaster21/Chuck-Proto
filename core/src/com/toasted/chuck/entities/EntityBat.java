package com.toasted.chuck.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.toasted.chuck.Graphics;
import com.toasted.chuck.Level;

public class EntityBat extends Entity{
	private static final float MOVE_SPEED = 40f;
	private TextureRegion[] animation = new TextureRegion[]{Graphics.getSubTexture(20), Graphics.getSubTexture(21)};
	private float timeToPickNextDirection = 0f;
	private float animationSpeed = .05f;
	private float animationTimer = animationSpeed;

	private int animationFrame = 0;
	private Random rand = new Random();
	
	public EntityBat(float x, float y){
		super(x, y);
		isChuckable = false;
		isHostile = true;
	}
	
	public void update(float delta, Level lvl) {
		Rectangle collide = doCollisions(delta, lvl.getCollisions());
		//wandering ai
		timeToPickNextDirection -= delta;
		if(timeToPickNextDirection <= 0 || collide != null){
			float nextDirection = rand.nextFloat();
			timeToPickNextDirection = rand.nextFloat() * 1.5f;
			velocity.x = (float)Math.cos(nextDirection * Math.PI * 2f) * MOVE_SPEED;
			velocity.y = (float)Math.sin(nextDirection * Math.PI * 2f) * MOVE_SPEED;
		}
		animationTimer -= delta;
		if(animationTimer <= 0){
			animationTimer = animationSpeed;
			animationFrame++;
		}
		
	}

	public void draw(Graphics g) {
		g.getBatch().draw(animation[animationFrame % 2], getX(), getY());
	}

}
