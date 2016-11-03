package com.toasted.chuck;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	public Vector2 position = new Vector2();
	public Vector2 velocity = new Vector2();
	public EntityController controller;
	public Rectangle collision;
	float flyLength;
	public boolean isChuckable = true;
	boolean shouldDrawSelf = true;
	public Entity(){
		
	}
	public abstract void update(float delta, ArrayList<Entity> entities, ArrayList<Rectangle> collisions);
	protected void doCollisions(float delta, ArrayList<Rectangle> collisions){
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
			}
		}
		collision.y = position.y;
	}
	public abstract void draw(Graphics g);
	public void draw(Graphics g, float dx, float dy){
		draw(g);
	}
}
