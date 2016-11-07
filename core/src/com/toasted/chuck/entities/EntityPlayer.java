package com.toasted.chuck.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.toasted.chuck.Audio;
import com.toasted.chuck.EntityController;
import com.toasted.chuck.Graphics;
import com.toasted.chuck.Level;
import com.toasted.chuck.Light;
import com.toasted.chuck.LightEmitter;

public class EntityPlayer extends Entity implements LightEmitter{
	protected Light illuminator;
	protected Texture playerArt = new Texture("player.png");
	protected TextureRegion[] sprites = new TextureRegion[8];
	private float moveSpeed = 100;
	private float throwSpeed = 100;
	private Entity holding;
	private Vector2 lastVelocity;
	private int lastDirectionalUsed;
	private ArrayList<Integer> directionals = new ArrayList<Integer>();//Don't think I'll use this
	private boolean[] arrowKeys = new boolean[4]; //UDLR
	
	private float stepTimerFull = .25f;
	private float stepTimer = stepTimerFull;
	private static Sound pickup = Audio.getSound("pickup.wav");
	private static Sound throwing = Audio.getSound("throw.wav");
	
	public EntityPlayer(){
		weight = 1;
		isChuckable = false;
		for(int i = 0;i < 4;i++){
			sprites[i] = new TextureRegion(playerArt, i * 16, 0, 16, 16);
			sprites[i + 4] = new TextureRegion(playerArt, i * 16, 16, 16,16);
		}
		lastVelocity = new Vector2();
		collision = new Rectangle(0, 0, 16, 16);
		illuminator = new Light(position.x + collision.width / 2, position.y + collision.height / 2, Light.VAL_PLAYER);
		controller = new EntityController(){

			public void acceptEvent(int keycode, boolean newState, Level lvl) {
				
				
				if(newState){
					switch(keycode){
					case Keys.SPACE:
						if(holding == null)
							for(Entity e: lvl.getEntities()){
								if(e.collision.overlaps(collision) && e.isChuckable){
									holding = e;
									holding.shouldDrawSelf = false;
									pickup.play();
									if(holding instanceof LightEmitter){
										LightEmitter le = (LightEmitter)holding;
									}
									
									
									
									break;
								}
							}
						else {
							holding.velocity.add(velocity).mulAdd(getDirectionalVector(), throwSpeed);
							holding.flyLength = .5f;
							holding.shouldDrawSelf = true;
							throwing.play();
							holding = null;
						}
						break;
					case Keys.Z: //Drop the beat
						if(holding != null){
							
							
							Rectangle boxCollision = new Rectangle(holding.collision);
							
							
							boxCollision.x += getDirectionalVector().x * 16;
							boxCollision.y += getDirectionalVector().y * 16;
							boolean collides = false;
							for(Rectangle r: lvl.getCollisions()){
								if(r.overlaps(boxCollision)){
									//can't place it here
									collides = true;
									break;
								}
							}
							if(!collides){
								holding.shouldDrawSelf = true;
								holding.position.mulAdd(getDirectionalVector(), 16);
								holding = null;
							
							}
						}
					}
					if(keycode == Keys.UP || keycode == Keys.DOWN || keycode == Keys.LEFT || keycode == Keys.RIGHT){
						lastDirectionalUsed = keycode;
						directionals.add(keycode - Keys.UP);
						arrowKeys[keycode - Keys.UP] = newState;
					}
					if(Math.abs(velocity.x) > 0)
						lastVelocity.x = velocity.x;
					if(Math.abs(velocity.y) > 0)
						lastVelocity.y = velocity.y;
					
				} else {
					if(keycode == Keys.UP || keycode == Keys.DOWN || keycode == Keys.LEFT || keycode == Keys.RIGHT){
						directionals.remove(new Integer(keycode - Keys.UP));
						arrowKeys[keycode - Keys.UP] = newState;
					}
					
					switch(keycode){
					case Keys.LEFT:
					case Keys.RIGHT:
					case Keys.UP:
					case Keys.DOWN:
						int t = getNewerPressEvent(getNewerPressEvent(0, 1), getNewerPressEvent(2, 3));
						if (t < 0){
							lastDirectionalUsed = keycode;
						} else 
							lastDirectionalUsed = Keys.UP + t;
						
						
					}
					
					
				}
				
			}
			
		};
	}
	private int getNewerPressEvent(int i, int j){
		for(int k = directionals.size() - 1;k >= 0;k--){
			if(directionals.get(k).equals(i)) return i;
			if(directionals.get(k).equals(j)) return j;
		}
		
		return -1;
	}
	private void doControls(){
		for(int i = 0;i < 4;i++){
			switch(i){
			case 0: //UP
				if(getNewerPressEvent(0, 1) == 0)
					velocity.y = moveSpeed;
				break;
			case 1: //DOWN
				if(!arrowKeys[0] && !arrowKeys[1]){
					//no up/down directions are pressed
					velocity.y = 0;
					break;
				}
				if(getNewerPressEvent(0, 1) == 1)
					velocity.y = -moveSpeed;
				break; 
			case 2: //LEFT
				if(getNewerPressEvent(2, 3) == 2)
					velocity.x = -moveSpeed;
				break;
			case 3: //RIGHT
				if(!arrowKeys[2] && !arrowKeys[3]){
					velocity.x = 0;
				}
				if(getNewerPressEvent(2,3) == 3)
					velocity.x = moveSpeed;
				break;
			}
			if(Math.abs(velocity.x) > 0 && Math.abs(velocity.y) > 0){
				velocity.x = Math.signum(velocity.x) * ((float)Math.sqrt(2) / 2f) * moveSpeed;
				velocity.y = Math.signum(velocity.y) * ((float)Math.sqrt(2) / 2f) * moveSpeed;
			}
		}
	}
	public void update(float delta, Level lvl){
		
		//test array based controls
			
		doControls();
		
		float vectorMag = (float)Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2));
		if(vectorMag != 0){
			stepTimer -= delta;
			if(stepTimer <= 0){
				stepTimer = stepTimerFull;
				step.play(.5f, 1, 0);
			}
		}
		doCollisions(delta, lvl.getCollisions());
		
		
		if(holding != null){
			holding.position.x = position.x;
			holding.position.y = position.y;
		}
		
		illuminator.getPosition().x = position.x + collision.width / 2;
		illuminator.getPosition().y = position.y + collision.height / 2;
		
		
	}

	@Override
	public void draw(Graphics g) {
		g.getBatch().draw(pickSprite(), position.x, position.y);
		if(holding != null){
			holding.draw(g, 0, 12);
		}
	}
	private TextureRegion pickSprite(){
		switch(lastDirectionalUsed){
		case Keys.UP:
			return sprites[2 + (holding != null ? 4 : 0)];
		case Keys.LEFT:
			return sprites[1 + (holding != null ? 4 : 0)];
		case Keys.DOWN:
			return sprites[0 + (holding != null ? 4 : 0)];
		case Keys.RIGHT:
			return sprites[3 + (holding != null ? 4 : 0)];
		}
		return sprites[0];
		
	}
	private Vector2 getDirectionalVector(){
		switch(lastDirectionalUsed){
		case Keys.UP:
			return new Vector2(0, 1);
		case Keys.LEFT:
			return new Vector2(-1, 0);
		case Keys.DOWN:
			return new Vector2(0, -1);
		case Keys.RIGHT:
			return new Vector2(1, 0);
		}
		return new Vector2(0, -1);
	}
	public Light getLight() {
		return illuminator;
	}
}
