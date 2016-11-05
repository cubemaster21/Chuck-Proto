package com.toasted.chuck;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.toasted.chuck.entities.Entity;

public interface EntityController {
	public void acceptEvent(int keycode, boolean newState, Level world);
}
