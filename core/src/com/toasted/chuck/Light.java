package com.toasted.chuck;

import com.badlogic.gdx.math.Vector2;

public class Light {
	public final static float VAL_TORCH = .4f;
	public final static float VAL_PLAYER = .9f;
	public final static float VAL_AMBIENT = 0f;
	private static Vector2 tmp = new Vector2();
	private Vector2 position;
	private float intensity = 1;
	public Light(float x, float y, float intensity){
		position = new Vector2(x, y);
		this.intensity = intensity;
	}
	public Vector2 getScreenPos(float cx, float cy){
		return tmp.set(position).sub(cx, cy);
	}
	public float getX(){
		return position.x;
	}
	public float getY(){
		return position.y;
	}
	public Vector2 getPosition(){
		return position;
	}
	public float getIntensity(){
		return intensity;
	}
	
}
