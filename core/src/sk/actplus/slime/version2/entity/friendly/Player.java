package sk.actplus.slime.version2.entity.friendly;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import java.util.Random;

import sk.actplus.slime.constants.Category;
import sk.actplus.slime.entity.player.Neighbors;
import sk.actplus.slime.other.BodyArray;
import sk.actplus.slime.version2.Game;
import sk.actplus.slime.version2.GameScreen;
import sk.actplus.slime.version2.entity.PolygonRenderer;
import sk.actplus.slime.version2.input.PlayerInputProcessor;

/**
 * Created by Ja on 17.2.2018.
 */

public class Player extends sk.actplus.slime.version2.entity.Entity{
    public static final BodyDef.BodyType BODY_TYPE = BodyDef.BodyType.DynamicBody;
    public static short HITBOX_category = Category.JELLY_HITBOX;
    public static short PARTICLES_category = Category.JELLY;

    public static final int NUM_SEGMENTS = 15;
    public static final boolean HITBOX_ROTATION = false;
    public static final boolean PARTICLES_ROTATION = false;

    public static final float HITBOX_SIDE = 3f;
    public static float PARTICLES_RADIUS = HITBOX_SIDE / NUM_SEGMENTS / 2f;
    public static final float ORBITAL_SIDE = 3f;

    public static final float HITBOX_DENSITY = 0.0001f;
    public static final float PARTICLES_DENSITY = 0.01f;

    public static final float HITBOX_RESTITUTION = 0f;
    public static final float PARTICLES_RESTITUTION = 0f;


    public static final float HITBOX_FRICTION = 1f;
    public static final float PARTICLES_FRICTION = 1f;

    public static final float HITBOX_FREQUENCY_HZ = 3f;
    public static final float PARTICLES_FREQUENCY_HZ = 1.5f;

    public static final float HITBOX_DAMPING = 0.2f;
    public static final float PARTICLES_DAMPING = 0.2f;

    public static final boolean FIXED_ROTATION = false;


    private int score;
    private PlayerInputProcessor inputProccesor;
    public BodyArray bodies;
    public PolygonRenderer polygonRenderer;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    public Player(GameScreen screen, InputMultiplexer mux, OrthographicCamera camera) {
        super(screen);
        this.camera = camera;

        inputProccesor = new PlayerInputProcessor(this);

        body = createJellyBody(0, 4);
        mux.addProcessor(inputProccesor);


        shapeRenderer = new ShapeRenderer();

        Random rand = new Random();
        color = new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1.00f);
        color = new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1.00f);

        //polygonRenderer = new PolygonRenderer(getOutlineArray(), 3, Color.BLUE);
        //polygonRenderer = new PolygonRenderer(getOutlineArray(), ((NUM_SEGMENTS-1) * 4), Color.BLUE);
    }

    public void applyForce(float vx, float vy) {
        bodies.applyForceToCenter(vx,vy,false);
    }

    public void setVelocity(float vx, float vy) {
        bodies.setLinearVelocity(vx,vy);
    }



    private Body createJellyBody(float xi, float yi) {

        Neighbors bodyParticles[][] = new Neighbors[NUM_SEGMENTS][NUM_SEGMENTS];
        bodies = new BodyArray();
        Body body;


        /**
         * Main Body's Definitions, Body Definition and Fixture Definition
         * Creates Main ExpectsInput Body, controlled by Input
         * */

        BodyDef bodyDefMain = defineBody(BODY_TYPE, xi, yi, HITBOX_ROTATION);
        PolygonShape shapeMain = new PolygonShape();
        shapeMain.setAsBox(HITBOX_SIDE / 2f, HITBOX_SIDE / 2f);

        FixtureDef fixtureDefMain = defineFixture(shapeMain, HITBOX_DENSITY, HITBOX_RESTITUTION, HITBOX_FRICTION, HITBOX_category, HITBOX_category);

        /**
         * Orbital's Body Definitions Definitions, Body Definition and Fixture Definition
         */

        BodyDef bodyDefOrbital = defineBody(BODY_TYPE, xi, yi, PARTICLES_ROTATION);

        CircleShape shapeOrbital = new CircleShape();
        shapeOrbital.setRadius(PARTICLES_RADIUS / 2);
        FixtureDef fixtureDefOrbital = defineFixture(shapeOrbital, PARTICLES_DENSITY, PARTICLES_RESTITUTION, PARTICLES_FRICTION, PARTICLES_category, Category.NOTHING);


        body = createBody(world, bodyDefMain, fixtureDefMain);
        body.getFixtureList().get(0).setUserData("player");


        float space = (ORBITAL_SIDE / ((float) NUM_SEGMENTS));

        /**
         * Create NUM_SEG x NUM_SEG body grid
         *
         */
        for (int i = 0; i < NUM_SEGMENTS; i++) {
            for (int j = 0; j < NUM_SEGMENTS; j++) {
                //if ((j==0)||(i==0)||(i==NUM_SEGMENTS-1)||(j==NUM_SEGMENTS-1)) {
                //crete body
                float x, y;
                x = ORBITAL_SIDE / 2f - j * space - space / 2f;
                y = ORBITAL_SIDE / 2f - i * space - space / 2f;

                Vector2 circlePosition = new Vector2(x, y);


                Vector2 v2 = body.getPosition().cpy().add(circlePosition);
                bodyDefOrbital.position.set(v2);

                // Create the body and fixture
                Body particle = createBody(world, bodyDefOrbital, fixtureDefOrbital);
                bodies.add(particle);
                bodyParticles[j][i] = new Neighbors(particle);
                //}

                particle.getFixtureList().get(0).setUserData("player");


            }
        }

        for (int i = 0; i < NUM_SEGMENTS; i++) {
            for (int j = 0; j < NUM_SEGMENTS; j++) {
                //load neighbors
                try {
                    bodyParticles[j][i].neighbors.add(bodyParticles[j - 1][i - 1].body);
                } catch (Exception e) {

                }

                try {
                    bodyParticles[j][i].neighbors.add(bodyParticles[j][i - 1].body);
                } catch (Exception e) {

                }

                try {
                    bodyParticles[j][i].neighbors.add(bodyParticles[j + 1][i - 1].body);
                } catch (Exception e) {

                }

                try {
                    bodyParticles[j][i].neighbors.add(bodyParticles[j + 1][i].body);
                } catch (Exception e) {

                }
            }
        }

        DistanceJointDef HITBOX_jointDef = defineDistanceJointDef(HITBOX_FREQUENCY_HZ, HITBOX_DAMPING, FIXED_ROTATION);
        DistanceJointDef PARTICLES_jointDef = defineDistanceJointDef(PARTICLES_FREQUENCY_HZ, PARTICLES_DAMPING, FIXED_ROTATION);


        DistanceJointDef tempDef = defineDistanceJointDef(0f, 1f, false);
        for (int i = 0; i < NUM_SEGMENTS; i++) {
            for (int j = 0; j < NUM_SEGMENTS; j++) {
                //create joints
                try {
                    for (int k = 0; k < bodyParticles[j][i].neighbors.size; k++) {

                        createDistanceJointAtCenter(world, PARTICLES_jointDef, bodyParticles[j][i].body, bodyParticles[j][i].neighbors.get(k));

                    }
                } catch (Exception e) {
                }

                try {
                    if ((i == NUM_SEGMENTS / 2) && (j == NUM_SEGMENTS / 2)) {


                        createDistanceJointSpec(world, tempDef, bodyParticles[j][i].body, body);

                    } else {
                        createDistanceJointSpec(world, HITBOX_jointDef, bodyParticles[j][i].body, body);
                    }
                } catch (Exception e) {
                }

            }
        }


        return body;
    }


    @Override
    public void handleCollision(short collisionBIT) {

    }

    @Override
    public void destroy() {
        screen.removeInputProcessor(inputProccesor);
    }

    @Override
    public void render(float delta) {
        //TODO : render graphics from OpenGL
    }

    private float[] vects = new float[6];
    private Color color;

    @Override
    public void render(float delta, PolygonSpriteBatch polyBatch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);





        for (int j = 1; j < NUM_SEGMENTS; j++) {
            for (int i = (NUM_SEGMENTS * (j-1)); i < NUM_SEGMENTS * j - 1; i++) {
                //color = new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1.00f);
                shapeRenderer.setColor(color);
                shapeRenderer.triangle( (bodies.getBody(0 + i).getPosition().x - camera.position.x)* GameScreen.PPM + GameScreen.CLIENT_WIDTH / 2, (bodies.getBody(0 + i).getPosition().y - camera.position.y)* GameScreen.PPM + GameScreen.CLIENT_HEIGHT / 2,
                        (bodies.getBody(1 + i).getPosition().x  - camera.position.x ) * GameScreen.PPM + GameScreen.CLIENT_WIDTH / 2, (bodies.getBody(1 + i).getPosition().y - camera.position.y)* GameScreen.PPM + GameScreen.CLIENT_HEIGHT / 2,
                        (bodies.getBody(NUM_SEGMENTS + i).getPosition().x - camera.position.x)* GameScreen.PPM + GameScreen.CLIENT_WIDTH / 2, (bodies.getBody(NUM_SEGMENTS + i).getPosition().y - camera.position.y)* GameScreen.PPM + GameScreen.CLIENT_HEIGHT / 2);

                //shapeRenderer.setColor(color);
                shapeRenderer.triangle( (bodies.getBody(1 + i).getPosition().x - camera.position.x)* GameScreen.PPM + GameScreen.CLIENT_WIDTH/2,(bodies.getBody(1 + i).getPosition().y - camera.position.y) * GameScreen.PPM + GameScreen.CLIENT_HEIGHT/2,
                        (bodies.getBody(NUM_SEGMENTS + i).getPosition().x - camera.position.x)* GameScreen.PPM + GameScreen.CLIENT_WIDTH/2,(bodies.getBody(NUM_SEGMENTS + i).getPosition().y  - camera.position.y)* GameScreen.PPM + GameScreen.CLIENT_HEIGHT/2,
                        (bodies.getBody(NUM_SEGMENTS + i + 1 ).getPosition().x - camera.position.x)* GameScreen.PPM + GameScreen.CLIENT_WIDTH/2,(bodies.getBody(NUM_SEGMENTS + i + 1).getPosition().y - camera.position.y )* GameScreen.PPM + GameScreen.CLIENT_HEIGHT/2 );
            }
        }

        shapeRenderer.end();

        //this.getPolygonRenderer().getPolygonSprite().draw(polyBatch);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        //polygonRenderer.update(getOutlineArray());
    }

    public Vector2[] getOutlineArray(){

        Vector2[] vec = new Vector2[(NUM_SEGMENTS-1) * 4];
        for (int i = 0; i < (NUM_SEGMENTS - 1); i++){
            vec[i] = bodies.get(i).getPosition().cpy();
            vec[NUM_SEGMENTS -1 + i] = bodies.get((i+1) * NUM_SEGMENTS - 1).getPosition().cpy();
            vec[NUM_SEGMENTS + NUM_SEGMENTS - 2 +i] = bodies.get(NUM_SEGMENTS * NUM_SEGMENTS - i -1).getPosition().cpy();
            vec[3 * NUM_SEGMENTS -3 +i] = bodies.get(NUM_SEGMENTS*(NUM_SEGMENTS-1) - i* NUM_SEGMENTS).getPosition().cpy();
        }

        return vec;
    }

    public PolygonRenderer getPolygonRenderer(){return polygonRenderer;}

}

