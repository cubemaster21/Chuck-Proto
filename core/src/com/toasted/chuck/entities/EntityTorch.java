package com.toasted.chuck.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.toasted.chuck.Graphics;
import com.toasted.chuck.Level;
import com.toasted.chuck.Light;
import com.toasted.chuck.LightEmitter;

public class EntityTorch extends EntityBox implements LightEmitter{
	private Light emitter;
	private TextureRegion torch = new TextureRegion(new Texture("env.png"),0, 16, 16, 16);
	private Random rand = new Random();
	private static TextureRegion[] torchFrames = new TextureRegion[]{
			Graphics.getSubTexture(8),
			Graphics.getSubTexture(25),
			Graphics.getSubTexture(26),
			Graphics.getSubTexture(27),
			Graphics.getSubTexture(28),
	};
	private int currentFrame = 0;
	private float flickerSpeed = .1f;
	private float frameTimer = flickerSpeed;
	private static final float FLICKER = .07f;
	
	public EntityTorch(float x, float y) {
		super(x, y);
		emitter = new Light(x + collision.width / 2, y + collision.height / 2, .3f);
		weight = 0;
	}

	@Override
	public Light getLight() {
		return emitter;
	}

	@Override
	public void update(float delta, Level lvl) {
		super.update(delta, lvl);
		
		//try flickering light
		emitter.setIntensity((rand.nextFloat() * FLICKER + (1f-FLICKER)) * .3f);
		frameTimer -= delta;
		if(frameTimer <= 0){
			frameTimer = flickerSpeed;
			int newFrame;
			do {
				newFrame = rand.nextInt(5);
			} while(newFrame == currentFrame);
			currentFrame = newFrame;
		}
		
		//follow the torch
		emitter.getPosition().set(position).add(collision.width / 2, 3 * collision.height / 4);
	}


	public void draw(Graphics g, float dx, float dy){
		if(flyLength >= 0){
			g.getBatch().draw(shadow, position.x, position.y-1);
		}
		float flyPerc = (Math.max(flyLength, 0) / .5f);
		if(flyPerc > 0)
			g.getBatch().draw(getFrame(), position.x +dx, position.y + (float)Math.abs(Math.cos((1 - flyPerc) * (Math.PI * 3f / 4f) - (Math.PI / 4f))) * 15 + dy);
		else g.getBatch().draw(getFrame(), position.x + dx, position.y + dy);
	}
	private TextureRegion getFrame(){
		return torchFrames[currentFrame];
	}

}
