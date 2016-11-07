package com.toasted.chuck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class Audio {
	public static Sound getSound(String filename){
		return Gdx.audio.newSound(Gdx.files.internal(filename));
	}
	public static Music getMusic(String filename){
		return Gdx.audio.newMusic(Gdx.files.internal(filename));
	}
}
