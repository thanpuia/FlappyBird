package com.lalthanpuia.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.css.Rect;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,bottomTube,topTube;
	//ShapeRenderer shapeRenderer, shapeRendererTopTube, shapeRendererBottomTube ;


	Texture gameover;
	float birdY = 0;
	float velocity = 0;
	Texture[] birds;
	int flapState = 0;

	int score = 0;
	int scoringTube = 0;
	Circle birdCircle;
	Rectangle[] topTubeRectangle; ;
	Rectangle[] bottomTubeRectangle;
	int gameState = 0;
	float gravity = 2;

	float gap =400;
	float maxTubeOffset ;

	Random randomGenerator;

    float tubeVelocity = 2;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;
    BitmapFont font;

    @Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");

		birdCircle = new Circle();
		topTubeRectangle = new Rectangle[numberOfTubes];
		bottomTubeRectangle = new Rectangle[numberOfTubes];

		//shapeRenderer = new ShapeRenderer();

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");

		gameover = new Texture("gameover.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 -gap / 2 -100;
		randomGenerator = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

		startGame();

	}

	public void startGame() {
		birdY = Gdx.graphics.getHeight() / 2 -birds[0].getHeight() / 2;

		for (int i =0 ; i < numberOfTubes; i++) {

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200 );

			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();
		}
    }

	@Override
	public void render () {

		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1 ) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {

				score++;

				Gdx.app.log("Score",String.valueOf(score));

				if (scoringTube < numberOfTubes - 1) {

					scoringTube++;

				} else {

					scoringTube = 0;

				}
			}
			if (Gdx.input.justTouched()) {

				velocity = -20;

			}

			for (int i =0 ; i < numberOfTubes; i++) {

				if(tubeX[i] < - topTube.getWidth()) {

					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200 );

				} else {

					tubeX[i] = tubeX[i] - tubeVelocity;


				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);

				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				 topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				 bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}
			if (birdY > 0 ) {

				velocity = velocity + gravity;
				birdY -= velocity;

			} else {

				gameState = 2;

			}
		} else if (gameState == 0){

			if (Gdx.input.justTouched()) {

				gameState = 1;
			}
		} else if ( gameState == 2) {

			batch.draw(gameover, Gdx.graphics.getWidth() /2 - gameover.getWidth() /2, Gdx.graphics.getHeight() /2 - gameover.getHeight() /2 );

			if (Gdx.input.justTouched()) {

				gameState = 1;
				startGame();
				scoringTube = 0;
				score = 0;
				velocity = 0;
			}
		}

		if (flapState == 0)
			flapState = 1;
		else
			flapState = 0;

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getHeight(), birdY);

		font.draw(batch, String.valueOf(score), 100, 200);

		birdCircle.set (Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);


		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for (int i =0 ; i < numberOfTubes; i++) {

			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(),bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRectangle[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangle[i])) {

				gameState = 2;
			}

		}

		//	shapeRenderer.end();
		batch.end();

	}
}
