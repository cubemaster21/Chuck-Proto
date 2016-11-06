package com.toasted.chuck;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Light {
	public final static float VAL_TORCH = .4f;
	public final static float VAL_PLAYER = .9f;
	public final static float VAL_AMBIENT = .25f;
	private static Vector2 tmp = new Vector2();
	private Vector2 position;
	private float intensity = 1;
	private boolean isEmitting = true;
	private Vector3 lightColor = new Vector3(1,1,1);
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
	public void setIntensity(float value){
		intensity = value;
	}
	public boolean isEmitting(){
		return isEmitting;
	}
	public void setEmitting(boolean state){
		isEmitting = state;
	}
	public void setLightColor(float r, float g, float b){
		lightColor.set(r, g, b);
	}
	public Vector3 getLightColor(){
		return lightColor;
	}
	public String toString(){
		String txt = "X: " + getX();
		txt += "\nY: " + getY();
		txt += "\nIntensity: " + getIntensity();
		txt += "\nIsEmitting: " + isEmitting();
		txt += "\nLightColor: " + getLightColor();
		return txt;
	}
}
