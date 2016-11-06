package com.toasted.chuck.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.toasted.chuck.Graphics;
import com.toasted.chuck.Level;

public class EntityBox extends Entity{
	float decel = 25;
	Texture crate = new Texture("crate.png");
	Texture shadow = new Texture("shadow.png");
	
	public EntityBox(float x, float y){
		super(x, y);
		weight = 1;
	}
	public void update(float delta, Level lvl) {
		flyLength -= delta;
		if(flyLength <= 0)
			velocity.mulAdd(velocity, -delta * decel);
		Rectangle r = doCollisions(delta, lvl.getCollisions());
		if(r != null){
			if(lvl.isCollisionBoxDestructable(r) && weight > .75f){
				MapProperties mp = lvl.getCollisionProperties(r);
				String layerOpened = (String) mp.get("destructable");
				lvl.getCollisions().remove(r);
				lvl.revealSecretRoom(layerOpened);
			}
		}
		if(flyLength > 0){
			//can hit enemies while in air
			for(Entity e: lvl.getEntities()){
				if(e.isHostile && e.collision.overlaps(collision)){
					velocity.x = 0;
					velocity.y = 0;
					e.stun(4f);
				}
			}
		}
	}

	public void draw(Graphics g) {
//		g.getShapes().setColor(Color.BLUE);
//		g.getShapes().rect(position.x, position.y + (float)Math.sin((1 - Math.max(flyLength, 0) / .5f) * Math.PI) * 8, 16, 16);
		if(shouldDrawSelf)
			draw(g, 0, 0);
		
	}
	public void draw(Graphics g, float dx, float dy){
		
		if(flyLength >= 0){
			g.getBatch().draw(shadow, position.x, position.y-1);
		}
		float flyPerc = (Math.max(flyLength, 0) / .5f);
		if(flyPerc > 0)
			g.getBatch().draw(crate, position.x +dx, position.y + (float)Math.abs(Math.cos((1 - flyPerc) * (Math.PI * 3f / 4f) - (Math.PI / 4f))) * 15 + dy);
		else g.getBatch().draw(crate, position.x + dx, position.y + dy);
	}

}
