package com.toasted.chuck;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
		if(lights.size() > 50){ // if there are too many to do
			
		} else {
			shader.setUniformi("u_actualLights", lights.size());
			shader.setUniformf("u_ambientLight", 0f);
			int loc = shader.getUniformLocation("u_lightCoord[" + 0 + "]");
			int locIn = shader.getUniformLocation("u_lightIntensity[0]");
			for(int i = 0;i < lights.size();i++){
				
				Vector3 v3 = cam.project(new Vector3(lights.get(i).position.x, lights.get(i).position.y, 0));
				Vector2 v = new Vector2(v3.x, v3.y);
				shader.setUniformf(loc + i, v);
				shader.setUniformf(locIn + i, lights.get(i).intensity * lightValueMultiplier);
			}
		}
	}
}
