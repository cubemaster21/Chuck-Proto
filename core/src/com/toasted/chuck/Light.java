package com.toasted.chuck;

import com.badlogic.gdx.math.Vector2;

public class Light {
	public final static float VAL_TORCH = .4f;
	public final static float VAL_PLAYER = .9f;
	private static Vector2 tmp = new Vector2();
	Vector2 position;
	float intensity = 1;
	public Light(float x, float y, float intensity){
		position = new Vector2(x, y);
		this.intensity = intensity;
	}
	public Vector2 getScreenPos(float cx, float cy){
		return tmp.set(position).sub(cx, cy);
	}
	
}
