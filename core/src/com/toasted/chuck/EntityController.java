package com.toasted.chuck;

import java.util.ArrayList;

public interface EntityController {
	public void acceptEvent(int keycode, boolean newState, Entity owner, ArrayList<Entity> entities);
}
