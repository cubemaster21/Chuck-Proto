package com.toasted.chuck;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class EntityPlayer extends Entity{
	Texture playerArt = new Texture("player.png");
	TextureRegion[] sprites = new TextureRegion[8];
	float moveSpeed = 100;
	float throwSpeed = 100;
	Entity holding;
	Vector2 lastVelocity;
	int lastDirectionalUsed;
	int lastXDirectional, lastYDirectional;
	private ArrayList<Integer> directionals = new ArrayList<Integer>();//Don't think I'll use this
	private boolean[] arrowKeys = new boolean[4]; //UDLR
	public EntityPlayer(){
		isChuckable = false;
		for(int i = 0;i < 4;i++){
			sprites[i] = new TextureRegion(playerArt, i * 16, 0, 16, 16);
			sprites[i + 4] = new TextureRegion(playerArt, i * 16, 16, 16,16);
		}
		lastVelocity = new Vector2();
		collision = new Rectangle(0, 0, 16, 16);
		controller = new EntityController(){

			public void acceptEvent(int keycode, boolean newState, Entity owner, ArrayList<Entity> entities) {
				
				
				if(newState){
					switch(keycode){
					case Keys.SPACE:
						if(holding == null)
							for(Entity e: entities){
								if(e.collision.overlaps(collision) && e.isChuckable){
									holding = e;
									holding.shouldDrawSelf = false;
									break;
								}
							}
						else {
							holding.velocity.add(velocity).mulAdd(getDirectionalVector(), throwSpeed);
							holding.flyLength = .5f;
							holding.shouldDrawSelf = true;
							
							holding = null;
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
	private void doControls(ArrayList<Entity> entities){
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
	public void update(float delta, ArrayList<Entity> entities, ArrayList<Rectangle> collisions){
		
		//test array based controls
			
		doControls(entities);
		
		
		
		doCollisions(delta, collisions);
		
		
		if(holding != null){
			holding.position.x = position.x;
			holding.position.y = position.y;
		}
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
}
