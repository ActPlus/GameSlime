package sk.actplus.slime.other;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import sk.actplus.slime.entity.enemies.Enemy;
import sk.actplus.slime.entity.mapobject.MovingBlock;
import sk.actplus.slime.entity.mapobject.Block;
import sk.actplus.slime.entity.player.Jelly;

import static sk.actplus.slime.constants.Files.SPRITE_BLOCK_COOKIE;
import static sk.actplus.slime.constants.Values.BLOCK_COOKIE_USER_DATA;
import static sk.actplus.slime.constants.Values.HEIGHTGEN_HIGH;
import static sk.actplus.slime.constants.Values.HEIGHTGEN_LOW;
import static sk.actplus.slime.constants.Values.HEIGHTGEN_MED;
import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.PPM;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;

/**
 * Created by Ja on 7.4.2017.
 */

public class MapGenerator {

    Random rand;


    int currentX;
    int currentY = 0;
    int lastY;

    MovableCamera camera;
    BodyArray blocks;
    EnemyArray enemies;
    LightArray lights;
    World world;
    RayHandler rayHandler;
    Jelly player;


    /**
     * Constructor of Map Generator, gets ready for generating
     *
     * @param world                - World to add New Blocks or Lights
     * @param camera               - Camera.pos is needed to delete hidden, unused bodies
     * @param startGeneratingAtPos - Starting position of Generator
     * @param blocks               - Array of Blocks, saves them for easier access
     * @param lights               - Array of Lights, saves them for easier access
     * @param rayHandler           - Renders, updates all lights
     */

    public MapGenerator(World world, Jelly player, MovableCamera camera, Vector2 startGeneratingAtPos, BodyArray blocks, LightArray lights, EnemyArray enemies, RayHandler rayHandler) {
        currentX = (int) startGeneratingAtPos.x;
        currentY = (int) startGeneratingAtPos.y;
        this.player = player;
        this.camera = camera;
        this.blocks = blocks;
        this.lights = lights;
        this.enemies = enemies;
        this.world = world;
        this.rayHandler = rayHandler;

        rand = new Random();

        lights.add(new PointLight(rayHandler, 50, new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.95f), 150, currentX + 15, currentY + 15 + rand.nextInt(10)));
        generateInitialBodies(new Vector2(camera.position.x, camera.position.y));
    }


    /**
     * Generate Bodies While(OnScreen) Random height
     */
    private void generateInitialBodies(Vector2 cameraPos) {
        currentX = (int) (cameraPos.x - WIDTH_CLIENT / PPM / 2f);
        while ((currentX < cameraPos.x + WIDTH_CLIENT / PPM / 2f)) {
            currentY = currentY + rand.nextInt(3) - 1;
            Block.newRandomBlock(world, blocks, currentX, currentY);
            currentX++;
        }
    }

    public void update() {
        deleteJunkBodies(world);
        generateIfNeeded();
    }

    public void generateIfNeeded() {
        /**
         * Generate new Block if needed
         */
        if (blocks.get(blocks.size - 1).getPosition().x < (camera.position.x + WIDTH_CLIENT / PPM / 2f)) {
            generate();
        }
    }

    public void generate() {
        lastY = currentY;


        int randomNumber = rand.nextInt(100);

        if (LogicalOperations.isBetween(randomNumber, 0, 49)) {

            /**
             * 0-50, MEANS 50% PLAIN
             */
            currentY = currentY + rand.nextInt(3) - 1;
            Block.newRandomBlock(world, blocks, currentX, currentY);

        } else {
            int heightGenFactor = HEIGHTGEN_HIGH;
            if (LogicalOperations.isBetween(randomNumber, 50, 84)) {
                /**
                 * Between and equals 50 and 85 is LOW, results in 35%
                 */
                heightGenFactor = HEIGHTGEN_LOW;
            } else if (LogicalOperations.isBetween(randomNumber, 80, 94)) {
                /**
                 * If random number is between or equals 80 and 95, MEANS 15% chance to go MEDIUM
                 */
                heightGenFactor = HEIGHTGEN_MED;
            }

            /**
             * If more then 95, MEANS there is 5% chance to go HIGH
             */

            currentY = currentY + rand.nextInt(heightGenFactor * 2 + 1) - heightGenFactor;

            if (lastY - currentY < 0) {
                for (int j = 0; j > lastY - currentY; j--) {
                    Block.newRandomBlock(world, blocks, currentX, currentY + j);
                }
            } else if (lastY - currentY > 0) {
                for (int j = 0; j < lastY - currentY; j++) {
                    Block.newRandomBlock(world, blocks, currentX, currentY + j);
                }
            } else {
                Block.newRandomBlock(world, blocks, currentX, currentY);
            }

        }

        if (rand.nextInt(10) == 0) {
            new Block(world, blocks, currentX, currentY + rand.nextInt(10) + 3, SPRITE_BLOCK_COOKIE, BLOCK_COOKIE_USER_DATA);
        }

        if (rand.nextInt(30) == 0) {
            float rayLength = rand.nextInt(30) + 0;
            lights.add(new PointLight(rayHandler, 150, new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.95f), rayLength, currentX + rayLength, currentY +5 + rand.nextInt(10)));
        }

        if (rand.nextInt(40) == 0) {
            new MovingBlock(world, currentX, player.body.getPosition().y, new Vector2(-14, 0), "lefter");
        }

        if (rand.nextInt(30) == 0) {
            enemies.add(new Enemy(world, currentX, (int) player.body.getPosition().y + 10, player));
        }

        currentX++;
    }

    public void deleteJunkBodies(World world) {
        /**
         * Delete bodies if out of camera
         */
        //int num_of_deleted_bodies = 0;
        //int num_of_deleted_lights = 0;
        if ((blocks.size != 0)) {
            System.out.println();
            while ((blocks.first().getPosition().x < (camera.position.x - WIDTH_CLIENT / PPM / 2f))) {
                //num_of_deleted_bodies++;
                world.destroyBody(blocks.first());
                blocks.removeIndex(0);
            }
        }

        /**
         * Delete Junk Lights if FAR out of camera
         */
        if (lights.size != 0) {
            //if ((lights.first().getPosition().x < (camera.position.x - WIDTH_CLIENT / PPM - lights.first().getDistance()))) {
            if ((lights.first().getPosition().x < (camera.position.x - WIDTH_CLIENT / PPM / 2f))) {
                //num_of_deleted_lights++;
                lights.first().remove();
                lights.removeIndex(0);
            }
        }

        if (enemies.size != 0) {
            for (int i = 0; i < enemies.size; i++) {
                if ((enemies.get(i).body.getPosition().x < (camera.position.x - WIDTH_CLIENT / PPM / 2f))) {
                    //num_of_deleted_lights++;
                    world.destroyBody(enemies.get(i).body);
                    enemies.removeIndex(i);
                }
            }
        }

        BodyArray bodies = new BodyArray();
        world.getBodies(bodies);

        if (bodies.size != 0) {
            for (int i = 0; i < bodies.size; i++) {
                if (bodies.getBody(i).getFixtureList().get(0).getUserData() != null) {
                    if (bodies.getBody(i).getFixtureList().get(0).getUserData() == "shoot") {
                        if ((bodies.get(i).getPosition().x < (camera.position.x - WIDTH_CLIENT / PPM / 2f))
                                || (bodies.get(i).getPosition().x > (camera.position.x + WIDTH_CLIENT / PPM / 2f))
                                || (bodies.get(i).getPosition().y < (camera.position.y - HEIGHT_CLIENT / PPM / 2f))
                                || (bodies.get(i).getPosition().y > (camera.position.y + HEIGHT_CLIENT / PPM / 2f))
                                ) {

                            world.destroyBody(bodies.get(i));
                        }
                    } else {
                        if ((bodies.getBody(i).getPosition().x < (camera.position.x - WIDTH_CLIENT / PPM / 2f))) {
                            world.destroyBody(bodies.getBody(i));
                        }
                    }
                }
            }
        }

    }


    /**
     * Debug Deletion, every deleteJunkBodies();
     */
        /*if ((num_of_deleted_bodies != 0)||(num_of_deleted_lights != 0)) {
            System.out.println("Deleted " + num_of_deleted_bodies + " JUNK BODIES and " + num_of_deleted_lights + " JUNK LIGHTS");
            num_of_deleted_bodies = 0;
            num_of_deleted_lights = 0;
        }*/

}
