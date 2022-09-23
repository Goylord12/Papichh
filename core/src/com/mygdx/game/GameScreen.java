package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final MyGdxGame game;
    public Texture roflan1;
    public Texture roflan2;
    public Texture roflan3;
    public Texture roflan4;
    public Texture roflan5;
    public Texture roflan6;
    public Texture roflan7;
    public Texture roflan8;
    public Texture roflan9;
    public Texture roflan10;
    public Texture roflanCatcherImg;
    public Texture[] roflanDropArray;
    public Texture backGround;
    public Texture ruletka;
    public Texture theGreatestMan;

    public int type;

    Sound PapichNePonimaet;
    Sound DropDyda;
    Sound DropsoundPapih;
    Sound Papich1;
    Sound catchSound;
    Music FonMusic;

    OrthographicCamera camera;
    SpriteBatch batch;

    Rectangle roflanCatcher;
    Array<Roflandrop> roflanDrops;

    long lastRoflanTime;
    int combo = 0;
    BitmapFont font = new BitmapFont();

    Vector3 touchPos = new Vector3();

    public GameScreen(final MyGdxGame gam) {
        this.game = gam;

        Papich1 = Gdx.audio.newSound(Gdx.files.internal("Papich1.wav"));
        catchSound = Gdx.audio.newSound(Gdx.files.internal("catchSound.wav"));
        FonMusic = Gdx.audio.newMusic(Gdx.files.internal("FonMusic.wav"));

        roflan1 = new Texture(Gdx.files.internal(("roflan№1.png")));
        roflan2 = new Texture(Gdx.files.internal(("roflan№2.png")));
        roflan3 = new Texture(Gdx.files.internal(("roflan№3.png")));
        roflan4 = new Texture(Gdx.files.internal(("roflan№4.png")));
        roflan5 = new Texture(Gdx.files.internal(("roflan№5.png")));
        roflan6 = new Texture(Gdx.files.internal(("roflan№6.png")));
        roflan7 = new Texture(Gdx.files.internal(("roflan№7.png")));
        roflan8 = new Texture(Gdx.files.internal(("roflan№8.png")));
        roflan9 = new Texture(Gdx.files.internal(("roflan№9.png")));
        roflan10 = new Texture(Gdx.files.internal(("roflan№10.png")));
        roflanCatcherImg = new Texture((Gdx.files.internal("roflanCatcherImg.png")));
        roflanDropArray = new Texture[] {roflan1, roflan2,roflan3, roflan4, roflan5,roflan6,roflan7,roflan8,roflan9,roflan10};

        backGround = new Texture((("FonRuletki.png")));
        ruletka = new Texture(("Ruletka.png"));
        theGreatestMan = new Texture(("TheGreatestMan.png"));

        batch = new SpriteBatch();

        roflanCatcher = new Rectangle();
        roflanCatcher.x = (float)Gdx.graphics.getWidth() / 2 - (float) roflanCatcherImg.getWidth() / 2;
        roflanCatcher.y = 200;
        roflanCatcher.width = roflanCatcherImg.getWidth();
        roflanCatcher.height = 10;

        FonMusic.setLooping(true);
        FonMusic.play();

        camera =new OrthographicCamera();
        camera.setToOrtho(false,1920, 1080);

        roflanDrops = new Array<>();
        spawnRoflandrop();
    }
    private void spawnRoflandrop() {
        Circle roflan = new Circle();

        int[] number = new int[] {337, 627, 915, 1203, 1487};
        roflan.x = number[MathUtils.random(0, 4)];
        roflan.y = 850;
        roflan.radius = 60;
        type = MathUtils.random(0,9);
        roflanDrops.add(new Roflandrop(roflan, type));

        lastRoflanTime = TimeUtils.nanoTime();

    }




    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backGround, 0,0);
        game.batch.draw(ruletka, 248,205);
        game.batch.draw(theGreatestMan,30,900);
        game.font.draw(game.batch, String.valueOf(combo), 1636,120 );
        game.batch.draw(roflanCatcherImg, roflanCatcher.x, roflanCatcher.y - 5);

        for(Roflandrop roflandrop: roflanDrops) {
            game.batch.draw(roflanDropArray[roflandrop.type], roflandrop.circle.x-roflandrop.circle.radius, roflandrop.circle.y-roflandrop.circle.radius);
        }
        game.batch.end();



        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            if(roflanCatcher.x >= 337)roflanCatcher.x -= 290;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            if(roflanCatcher.x <=(1487 - roflanCatcherImg.getWidth()/2))roflanCatcher.x += 290;
        }

        if(roflanCatcher.x < 0) roflanCatcher.x = 0;
        if(roflanCatcher.x > Gdx.graphics.getWidth() - roflanCatcherImg.getWidth() ) roflanCatcher.x = Gdx.graphics.getWidth() - roflanCatcherImg.getWidth() ;


        Iterator<Roflandrop> iter = roflanDrops.iterator();
        while(iter.hasNext()) {

            Roflandrop roflandrop = iter.next();
             if(combo < 50){
                 if(TimeUtils.nanoTime() - lastRoflanTime > MathUtils.random(1150000000, 1500000000)) spawnRoflandrop();
                roflandrop.circle.y -= 200 * Gdx.graphics.getDeltaTime();
            }
            if(combo >= 50){
                roflandrop.circle.y -= 205 * Gdx.graphics.getDeltaTime();
                if(TimeUtils.nanoTime() - lastRoflanTime > MathUtils.random(1000000000, 1100000000)) spawnRoflandrop();

            }
            if(combo >= 210){
                roflandrop.circle.y -= 210 * Gdx.graphics.getDeltaTime();
                if(TimeUtils.nanoTime() - lastRoflanTime > MathUtils.random(650000000, 790000000)) spawnRoflandrop();
            }
            if(combo >= 400){
                roflandrop.circle.y -= 215 * Gdx.graphics.getDeltaTime();
                if(TimeUtils.nanoTime() - lastRoflanTime > MathUtils.random(500000000, 600000000)) spawnRoflandrop();
            }
            if(combo > 600){
                roflandrop.circle.y -= 220 * Gdx.graphics.getDeltaTime();
                if(TimeUtils.nanoTime() - lastRoflanTime > MathUtils.random(400000000, 500000000)) spawnRoflandrop();
            }
            if(combo > 800){
                roflandrop.circle.y -= 225 * Gdx.graphics.getDeltaTime();
                if(TimeUtils.nanoTime() - lastRoflanTime > MathUtils.random(300000000, 400000000)) spawnRoflandrop();
            }
            if(combo > 1000){
                roflandrop.circle.y -= 230 * Gdx.graphics.getDeltaTime();
                if(TimeUtils.nanoTime() - lastRoflanTime > MathUtils.random(200000000, 300000000)) spawnRoflandrop();
            }
            if(combo > 1200){
                roflandrop.circle.y -= 240 * Gdx.graphics.getDeltaTime();
                if(TimeUtils.nanoTime() - lastRoflanTime > MathUtils.random(100000000, 200000000)) spawnRoflandrop();
            }
            if(combo == -100){
                PapichNePonimaet = Gdx.audio.newSound(Gdx.files.internal("Papich-100.wav"));
                PapichNePonimaet.play();
            }

            if(roflandrop.circle.y + roflandrop.circle.radius*2 < 370) {iter.remove();combo-=1;}
            if(Intersector.overlaps(roflandrop.circle, roflanCatcher)) {
                if(roflandrop.type == 0) {
                    catchSound.play();
                    iter.remove();
                    combo += 5;
                }
                else if(roflandrop.type == 1){
                    catchSound.play();
                    combo -=5;
                    iter.remove();
                }
                else if(roflandrop.type == 2){
                    catchSound.play();
                    combo+=3;
                    iter.remove();
                }
                else if(roflandrop.type == 3){
                    catchSound.play();
                    combo-=10;
                    iter.remove();
                }
                else if(roflandrop.type == 4){
                    catchSound.play();
                    combo-=3;
                    iter.remove();
                }
                else if(roflandrop.type == 5){
                    catchSound.play();
                    combo+=15;
                    iter.remove();
                }
                else if(roflandrop.type == 6){
                    catchSound.play();
                    combo += MathUtils.random(-10, 10);
                    iter.remove();
                }
                else if(roflandrop.type == 7){
                    catchSound.play();
                    combo-=11;
                    iter.remove();
                }else if(roflandrop.type == 8){
                    DropsoundPapih = Gdx.audio.newSound(Gdx.files.internal("DropSound100.wav"));
                    DropsoundPapih.play();
                    combo+=100;
                    iter.remove();
                }else if(roflandrop.type== 9){
                    DropDyda = Gdx.audio.newSound(Gdx.files.internal("DropDyda.wav"));
                    DropDyda.play();
                    catchSound.play();
                    combo-=100;
                    iter.remove();
                }
            }
        }
    }
    public void show() {
        FonMusic.play();
    }
    class Roflandrop {
        Circle circle;
        int type;

        public Roflandrop(Circle circle, int type) {
            this.circle = circle;
            this.type = type;
        }
    }
    public void resize(int width, int height) {
    }
    public void hide() {
    }
    public void pause() {
    }
    public void resume() {
    }
    public void dispose() {
        batch.dispose();
        roflan1.dispose();
        roflan2.dispose();
        roflan3.dispose();
        roflan4.dispose();
        roflan5.dispose();
        roflan6.dispose();
        roflan7.dispose();
        roflan8.dispose();
        roflan9.dispose();
        roflan10.dispose();
        roflanCatcherImg.dispose();

        Papich1.dispose();
        catchSound.dispose();
        FonMusic.dispose();

        batch.dispose();
        font.dispose();


    }
}
