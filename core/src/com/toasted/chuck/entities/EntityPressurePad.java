package com.toasted.chuck.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.toasted.chuck.Graphics;
import com.toasted.chuck.Level;
import com.toasted.chuck.Light;
import com.toasted.chuck.LightEmitter;

public class EntityPressurePad extends Entity implements LightEmitter{
	
	private final float TRIGGER_WEIGHT = .75f;
	
	
	private TextureRegion art = Graphics.getSubTexture(16);
	private TextureRegion pressed = Graphics.getSubTexture(24);
	private Light indicator;
	private boolean depressed = false;
	public EntityPressurePad(float x, float y){
		super(x, y);
		indicator = new Light(x + collision.width / 2, y + collision.height / 2, 2f);
		
		indicator.setEmitting(false);
		isChuckable = false;
		
	}
	@Override
	public void update(float delta, Level lvl) {
		boolean foundTrigger = false;
		for(Entity e: lvl.getEntities()){
			if(e.weight > TRIGGER_WEIGHT && e.flyLength <= 0){
				if(e.collision.overlaps(collision)){
					foundTrigger = true;
					break;
				}
			}
		}
		if(foundTrigger) {
//			indicator.setEmitting(true);
			depressed = true;
		} else {
			depressed = false;
//			indicator.setEmitting(false);
		}
	}

	@Override
	public void draw(Graphics g) {
		g.getBatch().draw(depressed ? pressed : art, position.x, position.y);
		
	}
	public float getZSortValue(){
		return super.getZSortValue() + 1000000;
	}
	@Override
	public Light getLight() {
		return indicator;
	}

}
