package com.toasted.chuck;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Graphics {
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	OrthographicCamera cam;
	
	final FileHandle VERTEX = Gdx.files.internal("Vertex.vert");
    final FileHandle FRAGMENT = Gdx.files.internal("Fragment.frag");
    final int orthoX = 16 * 28;
    final int orthoY = 9  * 28;
    final float orthoScale = orthoX / (float)getWidth();
    public float lightValueMultiplier = 1f; // for testing fade out
    private static int MAX_LIGHTS = 50;
    private static Texture textureMap;
    ShaderProgram shader = new ShaderProgram(VERTEX, FRAGMENT);
	public Graphics(){
		cam = new OrthographicCamera();
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		cam.setToOrtho(false, orthoX, orthoY);
		
	}
	public int getWidth(){
		return Gdx.graphics.getWidth();
	}
	public int getHeight(){
		return Gdx.graphics.getHeight();
	}
	
	public void startShapes(ShapeType shapeType){
		shapeRenderer.begin(shapeType);
	}
	public void endShapes(){
		shapeRenderer.end();
	}
	public void startSprite(){
		spriteBatch.begin();
	}
	public void endSprite(){
		spriteBatch.end();
	}
	public SpriteBatch getBatch(){
		return spriteBatch;
	}
	public ShapeRenderer getShapes(){
		return shapeRenderer;
	}
	public void prepare(){
		shapeRenderer.setProjectionMatrix(cam.combined);
		spriteBatch.setProjectionMatrix(cam.combined);
		
		spriteBatch.setShader(shader);
		
		if(!shader.isCompiled()){
			System.err.println(shader.getLog());
		}
		
		shader.begin();
		shader.setUniformf("u_screenResolution", new Vector2(getWidth(), getHeight()));
		
	}
	public void passLightsToShader(ArrayList<Light> lights){
		if(lights.size() > MAX_LIGHTS){ // if there are too many to do
			
		} else {
			
			int loc = shader.getUniformLocation("u_lightCoord[0]");
			int locIn = shader.getUniformLocation("u_lightIntensity[0]");
			int locCol = shader.getUniformLocation("u_lightColor[0]");
			int i = 0;
			for(Light l: lights){
				if(l == null || !l.isEmitting()){
					continue;
				}
				Vector3 v3 = cam.project(new Vector3(l.getX(), l.getY(), 0));
				Vector2 v = new Vector2(v3.x, v3.y);
				shader.setUniformf(loc + i, v);
				shader.setUniformf(locIn + i, l.getIntensity() * lightValueMultiplier);
				
				shader.setUniformf(locCol + i, l.getLightColor());
				i++;
				
			}
			shader.setUniformi("u_actualLights", i);
			shader.setUniformf("u_ambientLight", Light.VAL_AMBIENT);
		}
	}
	public static TextureRegion getSubTexture(int i){
		if(textureMap == null) textureMap = new Texture("env.png");
		return new TextureRegion(textureMap, (i % 8) * 16, (i / 8) * 16, 16, 16);
	}
}
