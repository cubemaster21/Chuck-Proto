package com.toasted.chuck.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.toasted.chuck.Graphics;

public class EntityBox extends Entity{
	float decel = 25;
	Texture crate = new Texture("crate.png");
	Texture shadow = new Texture("shadow.png");
	
	public EntityBox(float x, float y){
		super();
		position.x = x;
		position.y = y;
		collision = new Rectangle(x, y, 16, 16);
	}
	public void update(float delta, ArrayList<Entity> entities, ArrayList<Rectangle> collisions) {
		flyLength -= delta;
		if(flyLength <= 0)
			velocity.mulAdd(velocity, -delta * decel);
		doCollisions(delta, collisions);
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
