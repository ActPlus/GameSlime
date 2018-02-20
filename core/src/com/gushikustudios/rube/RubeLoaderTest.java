package com.gushikustudios.rube;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gushikustudios.rube.loader.RubeSceneAsyncLoader;
import com.gushikustudios.rube.loader.RubeSceneLoader;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;

/**
 * Use the left-click to pan. Scroll-wheel zooms.
 * 
 * @author cvayer, tescott
 * 
 */
public class RubeLoaderTest implements ApplicationListener, InputProcessor, ContactListener
{
   private OrthographicCamera mCam;
   private OrthographicCamera mTextCam;
   private RubeScene mScene;
   private Box2DDebugRenderer mDebugRender;

   private Array<SimpleSpatial> mSpatials; // used for rendering rube images
   private Array<PolySpatial> mPolySpatials;
   private Map<String, Texture> mTextureMap;
   private Map<Texture, TextureRegion> mTextureRegionMap;

   private static final Vector2 mTmp = new Vector2(); // shared by all objects
   private static final Vector2 mTmp3 = new Vector2(); // shared during polygon creation
   private SpriteBatch mBatch;
   private PolygonSpriteBatch mPolyBatch;
   private AssetManager mAssetManager;

   // used for pan and scanning with the mouse.
   private Vector3 mCamPos;
   private Vector3 mCurrentPos;

   private World mWorld;

   private float mAccumulator; // time accumulator to fix the physics step.

   private int mVelocityIter = 8;
   private int mPositionIter = 3;
   private float mSecondsPerStep = 1 / 60f;
   
   private float mFlashLoadingText;
   private boolean mHideLoadingText;
   
   private static final float MAX_DELTA_TIME = 0.25f;
   
   private BitmapFont mBitmapFont;
   
   private static final String [][] RUBE_SCENE_FILE_LIST =
   {
      {
         "data/palm.json"
      },
      {
         "data/base.json",
         "data/images1.json",
         "data/bodies1.json",
         "data/images2.json",
         "data/bodies2.json",
         "data/images3.json"
      }
   };
   
   private static final float FLASH_RATE = 0.25f;
   
   private enum GAME_STATE
   {
      STARTING,
      LOADING,
      RUNNING
   }
   
   private GAME_STATE mState;
   @SuppressWarnings("unused")
   private GAME_STATE mPrevState;
   private GAME_STATE mNextState;
   
   private boolean mUseAssetManager;
   private int mRubeFileList;
   private int mRubeFileIndex;

   public RubeLoaderTest()
   {
      this(false);
   }
   
   public RubeLoaderTest(boolean useAssetManager)
   {
      mUseAssetManager = useAssetManager;
   }
   
   public RubeLoaderTest(boolean useAssetManager, int rubeFileList)
   {
      this(useAssetManager);
      mRubeFileList = rubeFileList;
   }
   
   @Override
   public void create()
   {
      float w = Gdx.graphics.getWidth();
      float h = Gdx.graphics.getHeight();
      
      mBitmapFont = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);

      Gdx.input.setInputProcessor(this);

      mCamPos = new Vector3();
      mCurrentPos = new Vector3();

      mCam = new OrthographicCamera(100, 100 * h / w);
      mCam.position.set(50, 50, 0);
      mCam.zoom = 1.8f;
      mCam.update();
      
      mTextCam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
      mTextCam.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
      mTextCam.zoom = 1;
      mTextCam.update();

      mDebugRender = new Box2DDebugRenderer();

      mBatch = new SpriteBatch();
      mPolyBatch = new PolygonSpriteBatch();
      
      mTextureMap = new HashMap<String, Texture>();
      mTextureRegionMap = new HashMap<Texture, TextureRegion>();
      
      mState = mNextState = GAME_STATE.STARTING;
   }

   @Override
   public void dispose()
   {
      if (mBatch != null)
      {
         mBatch.dispose();
      }
      
      if (mPolyBatch != null)
      {
         mPolyBatch.dispose();
      }
      
      if (mDebugRender != null)
      {
         mDebugRender.dispose();
      }
      
      if (mWorld != null)
      {
         mWorld.dispose();
      }
      
      if (mAssetManager != null)
      {
         mAssetManager.dispose();
      }
   }

   @Override
   public void render()
   {
      float delta = Gdx.graphics.getDeltaTime();
      
      // cap maximum delta time...
      if (delta > MAX_DELTA_TIME)
      {
         delta = MAX_DELTA_TIME;
      }
      
      update(delta);
      present(delta);
      
      // state transitions here...
      mPrevState = mState;
      mState = mNextState;
      
   }
   
   /**
    * This method is used for game logic updates.
    * 
    * @param delta
    */
   private void update(float delta)
   {
      // game logic here...
      
      mFlashLoadingText += delta;
      if (mFlashLoadingText > FLASH_RATE)
      {
         mFlashLoadingText = 0;
         mHideLoadingText = !mHideLoadingText;
      }
      switch (mState)
      {
         case STARTING:
            initiateSceneLoad();
            break;
            
         case LOADING:
            processSceneLoad();
            break;
            
         case RUNNING:
            updatePhysics(delta);
            break;
      }
   }
   
   /**
    * The present() method is for drawing / rendering...
    * 
    * @param delta
    */
   private void present(float delta)
   {
      // game rendering logic here...
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      
      switch (mState)
      {
         case STARTING:
         case LOADING:
            if (!mHideLoadingText)
            {
               mBatch.setProjectionMatrix(mTextCam.combined);
               mBatch.begin();
               mBitmapFont.draw(mBatch,"Loading...",10,40);
               mBatch.end();
            }
            break;
            
         case RUNNING:
            renderWorld(delta);
            break;
      }
   }
   
   /**
    * Kicks off asset manager if selected... 
    * 
    */
   private void initiateSceneLoad()
   {
      if (mUseAssetManager)
      {
         // kick off asset manager operations...
         mAssetManager = new AssetManager();
         mAssetManager.setLoader(RubeScene.class, new RubeSceneAsyncLoader(new InternalFileHandleResolver()));
         // kick things off..
         mAssetManager.load(RUBE_SCENE_FILE_LIST[mRubeFileList][mRubeFileIndex], RubeScene.class);
      }
      mNextState = GAME_STATE.LOADING;
   }
   
   /**
    * Either performs a blocking load or a poll on the asset manager load...
    */
   private void processSceneLoad()
   {
      if (mAssetManager == null)
      {
         // perform a blocking load...
         RubeSceneLoader loader = new RubeSceneLoader();
         for (int i = 0; i < RUBE_SCENE_FILE_LIST[mRubeFileList].length; i++)
         {
            // each iteration adds to the scene that is ultimately returned...
            mScene = loader.addScene(Gdx.files.internal(RUBE_SCENE_FILE_LIST[mRubeFileList][mRubeFileIndex++]));
         }
         processScene();
         mNextState = GAME_STATE.RUNNING;
      }
      else if (mAssetManager.update())
      {
         // each iteration adds to the scene that is ultimately returned...
         mScene = mAssetManager.get(RUBE_SCENE_FILE_LIST[mRubeFileList][mRubeFileIndex++], RubeScene.class);
         if (mRubeFileIndex < RUBE_SCENE_FILE_LIST[mRubeFileList].length)
         {
            mAssetManager.load(RUBE_SCENE_FILE_LIST[mRubeFileList][mRubeFileIndex], RubeScene.class);
         }
         else
         {
            processScene();
            mNextState = GAME_STATE.RUNNING;
         }
      }
   }
   
   /**
    * Builds up world based on info from the scene...
    */
   private void processScene()
   {
      createSpatialsFromRubeImages(mScene);
      createPolySpatialsFromRubeFixtures(mScene);

      mWorld = mScene.getWorld();
      // configure simulation settings
      mVelocityIter = mScene.velocityIterations;
      mPositionIter = mScene.positionIterations;
      if (mScene.stepsPerSecond != 0)
      {
         mSecondsPerStep = 1f / mScene.stepsPerSecond;
      }
      mWorld.setContactListener(this);
      
      //
      // example of custom property handling
      //
      Array<Body> bodies = mScene.getBodies();
      if ((bodies != null) && (bodies.size > 0))
      {
         for (int i = 0; i < bodies.size; i++)
         {
            Body body = bodies.get(i);
            String gameInfo = (String)mScene.getCustom(body, "GameInfo", null);

         }{
      }
      }

      // Example of accessing data based on name
      System.out.println("body0 count: " + mScene.getNamed(Body.class, "body0").size);
      System.out.println("fixture0 count: " + mScene.getNamed(Fixture.class, "fixture0").size);
      mScene.printStats();
      
      testSceneSettings();
      
      mScene.clear(); // no longer need any scene references
   }
   
   /**
    * Use an accumulator to ensure a fixed delta for physics simulation...
    * 
    * @param delta
    */
   private void updatePhysics(float delta)
   {
      mAccumulator += delta;

      while (mAccumulator >= mSecondsPerStep)
      {
         mWorld.step(mSecondsPerStep, mVelocityIter, mPositionIter);
         mAccumulator -= mSecondsPerStep;
      }
   }
   
   /**
    * Perform all world rendering...
    * 
    * @param delta
    */
   private void renderWorld(float delta)
   {
      if ((mSpatials != null) && (mSpatials.size > 0))
      {
         mBatch.setProjectionMatrix(mCam.combined);
         mBatch.begin();
         for (int i = 0; i < mSpatials.size; i++)
         {
            mSpatials.get(i).render(mBatch, 0);
         }
         mBatch.end();
      }
      
      if ((mPolySpatials != null) && (mPolySpatials.size > 0))
      {
         mPolyBatch.setProjectionMatrix(mCam.combined);
         mPolyBatch.begin();
         for (int i = 0; i < mPolySpatials.size; i++)
         {
            mPolySpatials.get(i).render(mPolyBatch, 0);
         }
         mPolyBatch.end();
      }
      
      mBatch.setProjectionMatrix(mTextCam.combined);
      mBatch.begin();
      mBitmapFont.draw(mBatch,"fps: " + Gdx.graphics.getFramesPerSecond(),10,20);
      mBatch.end();

      mDebugRender.render(mWorld, mCam.combined);
   }
   
   /**
    * Validation on custom settings in the RUBE file.
    * 
    */
   private void testSceneSettings()
   {
      // 
      // validate the custom settings attached to world object..
      //
      boolean testBool = (Boolean)mScene.getCustom(mWorld, "testCustomBool", false);
      int testInt = (Integer)mScene.getCustom(mWorld, "testCustomInt", 0);
      float testFloat = (Float)mScene.getCustom(mWorld, "testCustomFloat", 0);
      Color color = (Color)mScene.getCustom(mWorld, "testCustomColor", null);
      Vector2 vec = (Vector2)mScene.getCustom(mWorld, "testCustomVec2", null);
      String string = (String)mScene.getCustom(mWorld, "testCustomString", null);
      int bodies1Custom = (Integer)mScene.getCustom(mWorld, "bodies1Custom", 0);
      int bodies2Custom = (Integer)mScene.getCustom(mWorld, "bodies2Custom", 0);
      int bodiesCommonCustom = (Integer)mScene.getCustom(mWorld,"bodiesCommonCustom",0);
      
      // validate multiple file reading...
      if (mRubeFileList == 1)
      {
         if (bodies1Custom != 1)
         {
            throw new GdxRuntimeException("bodies1Custom not read correctly! Expected: " + 1 + " Actual: " + bodies1Custom);
         }
         if (bodies2Custom != 2)
         {
            throw new GdxRuntimeException("bodies2Custom not read correctly! Expected: " + 2 + " Actual: " + bodies2Custom);            
         }
         // this is common between two files, but the last value will hold...
         if (bodiesCommonCustom != 4321)
         {
            throw new GdxRuntimeException("bodiesCommonCustom not read correctly! Expected: " + 4321 + " Actual: " + bodiesCommonCustom);
         }
         System.out.println("Multiple file testing: PASSED!");
      }
      
      if (testBool == false)
      {
         throw new GdxRuntimeException("testCustomBool not read correctly! Expected: " + true + " Actual: " + testBool);
      }
      if (testInt != 8675309)
      {
         throw new GdxRuntimeException("testCustomInt not read correctly! Expected: " + 8675309 + " Actual: "  + testInt);
      }
      if (testFloat != 1.25f)
      {
         throw new GdxRuntimeException("testCustomFloat not read correctly! Expected: " + 1.25f + " Actual: " + testFloat);
      }
      if (color == null) 
      {
         throw new GdxRuntimeException("testCustomColor is reporting null!");
      }
      if ((color.r != 17f/255) || (color.g != 29f/255) || (color.b != 43f/255) || (color.a != 61f/255))
      {
         throw new GdxRuntimeException("testCustomColor not read correctly!  Expected: " + new Color(17f/255,29f/255,43f/255,61f/255) + " Actual: " + color);
      }
      if (vec == null)
      {
         throw new GdxRuntimeException("testCustomVec2 is reporting null!");
      }
      if ((vec.x != 314159) || (vec.y != 21718))
      {
         throw new GdxRuntimeException("testCustomVec2 is not read correctly!  Expected: " + new Vector2(314159,21718) + " Actual: " + vec);
      }
      if (string == null)
      {
         throw new GdxRuntimeException("testCustomString is reporting null!");
      }
      if (!string.equalsIgnoreCase("excelsior!"))
      {
         throw new GdxRuntimeException("testCustomString is not read correctly!  Expected: Excelsior! Actual: " + string);
      }
      
      System.out.println("*** TESTING PASSED ***");
   }

   /**
    * Creates an array of SimpleSpatial objects from RubeImages.
    * 
    */
   private void createSpatialsFromRubeImages(RubeScene scene)
   {

      Array<RubeImage> images = scene.getImages();
      if ((images != null) && (images.size > 0))
      {
         mSpatials = new Array<SimpleSpatial>();
         for (int i = 0; i < images.size; i++)
         {
            RubeImage image = images.get(i);
            mTmp.set(image.width, image.height);
            String textureFileName = "data/" + image.file;
            Texture texture = mTextureMap.get(textureFileName);
            if (texture == null)
            {
               texture = new Texture(textureFileName);
               mTextureMap.put(textureFileName, texture);
            }
            SimpleSpatial spatial = new SimpleSpatial(texture, image.flip, image.body, image.color, mTmp, image.center,
                  image.angleInRads * MathUtils.radiansToDegrees);
            mSpatials.add(spatial);
         }
      }
   }

   /**
    * Creates an array of PolySpatials based on fixture information from the scene. Note that
    * fixtures create aligned textures.
    * 
    * @param scene
    */
   private void createPolySpatialsFromRubeFixtures(RubeScene scene)
   {
      Array<Body> bodies = scene.getBodies();
      
      EarClippingTriangulator ect = new EarClippingTriangulator();

      if ((bodies != null) && (bodies.size > 0))
      {
         mPolySpatials = new Array<PolySpatial>();
         Vector2 bodyPos = new Vector2();
         // for each body in the scene...
         for (int i = 0; i < bodies.size; i++)
         {
            Body body = bodies.get(i);
            bodyPos.set(body.getPosition());
            
            float bodyAngle = body.getAngle()*MathUtils.radiansToDegrees;

            Array<Fixture> fixtures = body.getFixtureList();

            if ((fixtures != null) && (fixtures.size > 0))
            {
               // for each fixture on the body...
               for (int j = 0; j < fixtures.size; j++)
               {
                  Fixture fixture = fixtures.get(j);

                  String textureName = (String)scene.getCustom(fixture, "TextureMask", null);
                  if (textureName != null)
                  {
                     String textureFileName = "data/" + textureName;
                     Texture texture = mTextureMap.get(textureFileName);
                     TextureRegion textureRegion = null;
                     if (texture == null)
                     {
                        texture = new Texture(textureFileName);
                        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                        mTextureMap.put(textureFileName, texture);
                        textureRegion = new TextureRegion(texture);
                        mTextureRegionMap.put(texture, textureRegion);
                     }
                     else
                     {
                        textureRegion = mTextureRegionMap.get(texture);
                     }

                     // only handle polygons at this point -- no chain, edge, or circle fixtures.
                     if (fixture.getType() == Shape.Type.Polygon)
                     {
                        PolygonShape shape = (PolygonShape) fixture.getShape();
                        int vertexCount = shape.getVertexCount();
                        float[] vertices = new float[vertexCount * 2];

                        // static bodies are texture aligned and do not get drawn based off of the related body.
                        if (body.getType() == BodyType.StaticBody)
                        {
                           for (int k = 0; k < vertexCount; k++)
                           {

                              shape.getVertex(k, mTmp);
                              mTmp.rotate(bodyAngle);
                              mTmp.add(bodyPos); // convert local coordinates to world coordinates to that textures are
                                                 // aligned
                              vertices[k * 2] = mTmp.x * PolySpatial.PIXELS_PER_METER;
                              vertices[k * 2 + 1] = mTmp.y * PolySpatial.PIXELS_PER_METER;
                           }
                           
                           short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                           PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                           PolySpatial spatial = new PolySpatial(region, Color.WHITE);
                           mPolySpatials.add(spatial);
                        }
                        else
                        {
                           // all other fixtures are aligned based on their associated body.
                           for (int k = 0; k < vertexCount; k++)
                           {
                              shape.getVertex(k, mTmp);
                              vertices[k * 2] = mTmp.x * PolySpatial.PIXELS_PER_METER;
                              vertices[k * 2 + 1] = mTmp.y * PolySpatial.PIXELS_PER_METER;
                           }
                           short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                           PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                           PolySpatial spatial = new PolySpatial(region, body, Color.WHITE);
                           mPolySpatials.add(spatial);
                        }
                     }
                     else if (fixture.getType() == Shape.Type.Circle)
                     {
                        CircleShape shape = (CircleShape)fixture.getShape();
                        float radius = shape.getRadius();
                        int vertexCount = (int)(12f * radius);
                        float [] vertices = new float[vertexCount*2];
                        if (body.getType() == BodyType.StaticBody)
                        {
                           mTmp3.set(shape.getPosition());
                           for (int k = 0; k < vertexCount; k++)
                           {
                              // set the initial position
                              mTmp.set(radius,0);
                              // rotate it by 1/vertexCount * k
                              mTmp.rotate(360f*k/vertexCount);
                              // add it to the position.
                              mTmp.add(mTmp3);
                              mTmp.rotate(bodyAngle);
                              mTmp.add(bodyPos); // convert local coordinates to world coordinates so that textures are aligned
                              vertices[k*2] = mTmp.x*PolySpatial.PIXELS_PER_METER;
                              vertices[k*2+1] = mTmp.y*PolySpatial.PIXELS_PER_METER;
                           }
                           short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                           PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                           PolySpatial spatial = new PolySpatial(region, Color.WHITE);
                           mPolySpatials.add(spatial);
                        }
                        else
                        {
                           mTmp3.set(shape.getPosition());
                           for (int k = 0; k < vertexCount; k++)
                           {
                              // set the initial position
                              mTmp.set(radius,0);
                              // rotate it by 1/vertexCount * k
                              mTmp.rotate(360f*k/vertexCount);
                              // add it to the position.
                              mTmp.add(mTmp3);
                              vertices[k*2] = mTmp.x*PolySpatial.PIXELS_PER_METER;
                              vertices[k*2+1] = mTmp.y*PolySpatial.PIXELS_PER_METER;
                           }
                           short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                           PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                           PolySpatial spatial = new PolySpatial(region, body, Color.WHITE);
                           mPolySpatials.add(spatial);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void resize(int width, int height)
   {
   }

   @Override
   public void pause()
   {
   }

   @Override
   public void resume()
   {
   }

   @Override
   public boolean keyDown(int keycode)
   {
      return false;
   }

   @Override
   public boolean keyUp(int keycode)
   {
      return false;
   }

   @Override
   public boolean keyTyped(char character)
   {
      return false;
   }

   @Override
   public boolean touchDown(int screenX, int screenY, int pointer, int button)
   {
      mCamPos.set(screenX, screenY, 0);
      mCam.unproject(mCamPos);
      return true;
   }

   @Override
   public boolean touchUp(int screenX, int screenY, int pointer, int button)
   {
      return false;
   }

   @Override
   public boolean touchDragged(int screenX, int screenY, int pointer)
   {
      mCurrentPos.set(screenX, screenY, 0);
      mCam.unproject(mCurrentPos);
      mCam.position.sub(mCurrentPos.sub(mCamPos));
      mCam.update();
      return true;
   }

   @Override
   public boolean mouseMoved(int screenX, int screenY)
   {
      return false;
   }

   @Override
   public boolean scrolled(int amount)
   {
      mCam.zoom += (amount * 0.1f);
      if (mCam.zoom < 0.1f)
      {
         mCam.zoom = 0.1f;
      }
      mCam.update();
      return true;
   }

   @Override
   public void beginContact(Contact contact)
   {
   }

   @Override
   public void endContact(Contact contact)
   {
   }

   @Override
   public void preSolve(Contact contact, Manifold oldManifold)
   {
   }

   @Override
   public void postSolve(Contact contact, ContactImpulse impulse)
   {
   }
}
