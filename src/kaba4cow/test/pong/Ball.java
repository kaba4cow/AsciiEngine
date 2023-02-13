package kaba4cow.test.pong;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.toolbox.maths.vectors.Vector2f;
import kaba4cow.ascii.toolbox.maths.vectors.Vectors;
import kaba4cow.ascii.toolbox.rng.RNG;

public class Ball {

	private Vector2f pos;
	private Vector2f vel;

	public Ball() {
		this.pos = new Vector2f();
		this.vel = new Vector2f();
		reset();
	}

	public void update(float dt, Plate[] plates) {
		Vectors.add(pos, vel, 16f * dt, pos);

		if (pos.x <= 0) {
			pos.x = 0;
			vel.x *= -1f;
		} else if (pos.x >= Display.getWidth() - 1) {
			pos.x = Display.getWidth() - 1;
			vel.x *= -1f;
		}
		if (pos.y <= -1) {
			plates[1].score();
			reset();
		} else if (pos.y >= Display.getHeight()) {
			plates[0].score();
			reset();
		}

		for (int i = 0; i < plates.length; i++)
			if (plates[i].isColliding(pos)) {
				float sd = plates[i].getSignedDistance(pos.x);
				if (sd < 0f)
					vel.x = -RNG.randomFloat(0.25f, 0.5f + sd);
				else
					vel.x = RNG.randomFloat(0.25f, 0.5f + sd);
				vel.y *= -1f;
				vel.normalize();
				break;
			}
	}

	public void render() {
		Drawer.drawChar((int) pos.x, (int) pos.y, Glyphs.WHITE_CIRCLE, 0x000FFF);
	}

	public void reset() {
		pos.x = Display.getWidth() / 2;
		pos.y = Display.getHeight() / 2;

		vel.x = RNG.randomFloat(-1f, 1f);
		vel.y = (RNG.randomBoolean() ? 1f : -1f) * RNG.randomFloat(0.25f, 0.75f);
		vel.normalize();
	}

	public boolean isDirectionUp() {
		return vel.y < 0f;
	}

	public float getX() {
		return pos.x;
	}

}
