package com.toasted.chuck.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.toasted.chuck.Graphics;
import com.toasted.chuck.Level;
import com.toasted.chuck.Light;
import com.toasted.chuck.LightEmitter;

public class EntityTorch extends EntityBox implements LightEmitter{
	private Light emitter;
	private TextureRegion torch = new TextureRegion(new Texture("env.png"),0, 23, 16, 16);
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
		emitter.getPosition().set(position).add(collision.width / 2, 3 * collision.height / 4);
	}


	public void draw(Graphics g, float dx, float dy){
		if(flyLength >= 0){
			g.getBatch().draw(shadow, position.x, position.y-1);
		}
		float flyPerc = (Math.max(flyLength, 0) / .5f);
		if(flyPerc > 0)
			g.getBatch().draw(torch, position.x +dx, position.y + (float)Math.abs(Math.cos((1 - flyPerc) * (Math.PI * 3f / 4f) - (Math.PI / 4f))) * 15 + dy);
		else g.getBatch().draw(torch, position.x + dx, position.y + dy);
	}

}
