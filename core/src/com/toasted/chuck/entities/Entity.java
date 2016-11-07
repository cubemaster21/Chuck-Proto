package com.toasted.chuck.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.toasted.chuck.Audio;
import com.toasted.chuck.EntityController;
import com.toasted.chuck.Graphics;
import com.toasted.chuck.Level;

public abstract class Entity {
	protected Vector2 position = new Vector2();
	protected Vector2 velocity = new Vector2();
	protected EntityController controller;
	protected Rectangle collision;
	protected float flyLength;
	protected boolean isChuckable = true;
	protected boolean shouldDrawSelf = true;
	protected boolean isHostile = false;
	protected float stunTimer = 0;
	protected float weight = 0; //used for destruction capabilities and pressure plate triggering
	protected int health  = 3;
	
	protected static Sound hurt = Audio.getSound("hurt.wav");
	protected static Sound drop = Audio.getSound("drop.wav");
	protected static Sound step = Audio.getSound("step.wav");
	public Entity(){
		
	}
	public Entity(float x, float y){
		position = new Vector2(x, y);
		collision = new Rectangle(x, y, 16, 16);
	}
	public abstract void update(float delta, Level lvl);
	protected Rectangle doCollisions(float delta, ArrayList<Rectangle> collisions){
		if(stunTimer > 0) {
			stunTimer -= delta;
			return null;
		}
		Rectangle collidedWith = null;
		position.x += velocity.x * delta;
		
		collision.x = position.x;
		
		for(Rectangle r: collisions){
			if(r.overlaps(collision)){
				if(velocity.x > 0){
					float overlap = collision.x + collision.width - r.x;
					position.x -= overlap;
					velocity.x = 0;
				} else if(velocity.x < 0){
					float overlap = r.x + r.width - collision.x;
					position.x += overlap;
					velocity.x = 0;
				}
				collidedWith = r;
			}
		}
		collision.x = position.x;
		
		
		position.y += velocity.y * delta;
		
		collision.y = position.y;
		
		for(Rectangle r: collisions){
			if(r.overlaps(collision)){
				if(velocity.y > 0){
					float overlap = collision.y + collision.height - r.y;
					position.y -= overlap;
					velocity.y = 0;
				} else if(velocity.y < 0){
					float overlap = r.y + r.height - collision.y;
					position.y += overlap;
					velocity.y = 0;
				}
				collidedWith = r;
			}
		}
		collision.y = position.y;
		return collidedWith;
	}
	public abstract void draw(Graphics g);
	public Vector2 getCenterPoint(){
		return new Vector2(getCenterX(), getCenterY());
	}
	public float getCenterX(){
		return position.x + collision.width / 2;
	}
	public float getCenterY(){
		return position.y + collision.height / 2;
	}
	public void draw(Graphics g, float dx, float dy){
		draw(g);
	}
	public float getX(){
		return position.x;
	}
	public float getY(){
		return position.y;
	}
	public EntityController getController(){
		return controller;
	}
	public float getZSortValue(){
		return getY();
	}
	public void stun(float duration){
		if(stunTimer > 0) return;
		hurt.play();
		this.stunTimer = duration;
	}
}
