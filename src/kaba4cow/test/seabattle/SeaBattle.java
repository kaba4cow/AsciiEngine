package kaba4cow.test.seabattle;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.gui.GUIButton;
import kaba4cow.ascii.drawing.gui.GUICheckbox;
import kaba4cow.ascii.drawing.gui.GUIFrame;
import kaba4cow.ascii.drawing.gui.GUISeparator;
import kaba4cow.ascii.drawing.gui.GUIText;
import kaba4cow.ascii.input.Keyboard;

public class SeaBattle implements MainProgram {

	private static final float MOVE_DELAY = 0.4f;

	private static final int COLOR = 0x125FFF;

	private static final int STATE_MENU = 0;
	private static final int STATE_START = 1;
	private static final int STATE_GAME = 2;
	private static final int STATE_PAUSE = 3;

	private static final int GAME_RUN = 0;
	private static final int GAME_WIN = 1;
	private static final int GAME_LOSE = 2;

	private int state;

	private GUIFrame menuFrame;

	private GUIFrame startFrame;
	private GUICheckbox manualPlotting;
	private GUICheckbox playHuman;
	private GUICheckbox smartAi;

	private GUIFrame pauseFrame;

	private GUIFrame gameFrame;
	private GUIFrame gameoverFrame;
	private GUIText gameoverText;

	private Field playerField;
	private Field enemyField;
	private boolean playerMove;
	private float moveTime;
	private int gameState;

	public SeaBattle() {

	}

	@Override
	public void init() {
		state = STATE_MENU;

		menuFrame = new GUIFrame(COLOR, false, false);
		new GUIText(menuFrame, -1, "MENU");
		new GUISeparator(menuFrame, -1, true);
		new GUIButton(menuFrame, -1, "Start new game", f -> switchState(STATE_START));
		new GUIButton(menuFrame, -1, "Quick start", f -> {
			manualPlotting.setSelected(false);
			playHuman.setSelected(true);
			smartAi.setSelected(true);
			newGame();
		});
		new GUIButton(menuFrame, -1, "Quit", f -> Engine.requestClose());

		startFrame = new GUIFrame(COLOR, false, false);
		new GUIText(startFrame, -1, "START NEW GAME");
		new GUISeparator(startFrame, -1, true);
		manualPlotting = new GUICheckbox(startFrame, -1, "Manual Plotting", false);
		playHuman = new GUICheckbox(startFrame, -1, "Human vs AI", true);
		smartAi = new GUICheckbox(startFrame, -1, "Smart AI", false);
		new GUIButton(startFrame, -1, "Start", f -> newGame());
		new GUIButton(startFrame, -1, "Go back", f -> switchState(STATE_MENU));

		pauseFrame = new GUIFrame(COLOR, false, false);
		new GUIText(pauseFrame, -1, "PAUSE");
		new GUISeparator(pauseFrame, -1, true);
		new GUIButton(pauseFrame, -1, "Resume", f -> switchState(STATE_GAME));
		new GUIButton(pauseFrame, -1, "Start new game", f -> switchState(STATE_START));
		new GUIButton(pauseFrame, -1, "Quit", f -> switchState(STATE_MENU));

		gameFrame = new GUIFrame(COLOR, false, false);
		new GUIText(gameFrame, -1, Field.CHAR_SHIP + " : ship");
		new GUIText(gameFrame, -1, Field.CHAR_MISS + " : miss");
		new GUIText(gameFrame, -1, Field.CHAR_HIT + " : hit");
		new GUIText(gameFrame, -1, Field.CHAR_DESTROYED + " : destroyed");
		new GUISeparator(gameFrame, -1, true);
		new GUIText(gameFrame, -1, "Left mouse button - place");
		new GUIText(gameFrame, -1, "Mouse wheel - rotate");

		gameoverFrame = new GUIFrame(COLOR, false, false);
		gameoverText = new GUIText(gameoverFrame, -1, "");
		new GUISeparator(gameoverFrame, -1, true);
		new GUIButton(gameoverFrame, -1, "Back to menu", f -> switchState(STATE_MENU));

		playerField = new Field();
		enemyField = new Field();
	}

	private void newGame() {
		switchState(STATE_GAME);
		gameState = GAME_RUN;
		moveTime = 0f;
		playerField.reset();
		enemyField.reset();
	}

	@Override
	public void update(float dt) {
		switch (state) {
		case STATE_MENU:
			if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
				Engine.requestClose();
			menuFrame.update();
			break;
		case STATE_START:
			startFrame.update();
			break;
		case STATE_GAME:
			if (gameState != GAME_RUN) {
				gameoverFrame.update();
				return;
			}
			if (playerField.isReady() && enemyField.isReady()) {
				moveTime += dt;
				Display.setCursorWaiting(!playerMove);
				if (moveTime >= MOVE_DELAY) {
					boolean play;
					Field field1 = playerMove ? playerField : enemyField;
					Field field2 = playerMove ? enemyField : playerField;
					int result = field1.play(field2, playerMove && playHuman.isSelected(), smartAi.isSelected());
					if (result == Field.HIT) {
						moveTime = 0f;
						play = true;
					} else if (result == Field.MISS) {
						moveTime = 0f;
						play = false;
					} else
						play = true;
					if (!play)
						playerMove = !playerMove;
				}
				if (playerField.isDestroyed())
					gameState = GAME_LOSE;
				else if (enemyField.isDestroyed())
					gameState = GAME_WIN;
			} else {
				playerField.plot(true, manualPlotting.isSelected());
				enemyField.plot(false, false);
			}
			if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
				switchState(STATE_PAUSE);
			break;
		case STATE_PAUSE:
			pauseFrame.update();
			break;
		}
	}

	@Override
	public void render() {
		int w = Display.getWidth();
		int h = Display.getHeight();

		if (state != STATE_GAME)
			Display.setCursorWaiting(false);

		switch (state) {
		case STATE_MENU:
			menuFrame.render(0, 0, w, h, false);
			break;
		case STATE_START:
			startFrame.render(0, 0, w, h, false);
			break;
		case STATE_GAME:
			if (gameState != GAME_RUN) {
				gameoverText.setText(gameState == GAME_WIN ? "You win!" : "You lose!");
				gameoverFrame.render(0, 0, w, h, false);
			} else {
				playerField.render(true);
				enemyField.render(false);
				BoxDrawer.drawBox(Field.SIZE, 0, w / 2 - Field.SIZE - 1, Field.SIZE, false, COLOR);
				BoxDrawer.drawBox(w / 2 + Field.SIZE, 0, w / 2 - Field.SIZE - 1, Field.SIZE, false, COLOR);
				gameFrame.render(0, Field.SIZE, w, h - Field.SIZE, false);
			}
			break;
		case STATE_PAUSE:
			pauseFrame.render(0, 0, w, h, false);
			break;
		}
	}

	private void switchState(int newState) {
		state = newState;
	}

	public static void main(String[] args) {
		Engine.init("Sea Battle", 30);
		if (args != null && args.length == 1 && args[0].equals("-fs"))
			Display.createFullscreen(2, true);
		else
			Display.createWindowed(30, 20, 2, true);
		Engine.start(new SeaBattle());
	}

}
