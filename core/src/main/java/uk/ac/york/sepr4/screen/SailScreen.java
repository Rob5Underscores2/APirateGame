package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.io.SailInputProcessor;
import uk.ac.york.sepr4.hud.HealthBar;
import uk.ac.york.sepr4.object.building.BuildingManager;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.entity.LivingEntity;
import uk.ac.york.sepr4.object.entity.NPCBoat;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.object.item.ItemManager;
import uk.ac.york.sepr4.object.item.Reward;
import uk.ac.york.sepr4.object.projectile.Projectile;
import uk.ac.york.sepr4.object.quest.QuestManager;
import uk.ac.york.sepr4.utils.AIUtil;

/**
 * SailScreen is main game class. Holds data related to current player including the
 * {@link BuildingManager}, {@link ItemManager},
 * {@link QuestManager} and {@link EntityManager}
 * <p>
 * Responds to keyboard and mouse input by the player. InputMultiplexer used to combine input processing in both
 * this class (mouse clicks) and {@link Player} class (key press).
 */
public class SailScreen extends PirateScreen {

    private GameInstance gameInstance;

    private static SailScreen sailScreen;

    private ShapeRenderer shapeRenderer;

    private SailInputProcessor sailInputProcessor;

    public static SailScreen getInstance() {
        return sailScreen;
    }

    /**
     * SailScreen Constructor
     * Adds the player as an actor to the stage.
     *
     * @param gameInstance
     */
    public SailScreen(GameInstance gameInstance) {
        super(gameInstance, new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        this.gameInstance = gameInstance;
        sailScreen = this;

        // Debug options (extra logging, collision shape renderer (viewing tile object map))
        if(gameInstance.getGame().DEBUG) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            shapeRenderer = new ShapeRenderer();
        }


        //Set input processor and focus
        getInputMultiplexer().addProcessor(gameInstance.getEntityManager().getOrCreatePlayer());
        sailInputProcessor = new SailInputProcessor(gameInstance);
        getInputMultiplexer().addProcessor(sailInputProcessor);

        setEnableStatsHUD(true);
        setEnableMessageHUD(true);

        //Create and spawn player
        startGame();
    }

    private void startGame() {
        getStage().addActor(gameInstance.getEntityManager().getOrCreatePlayer());
    }

    /**
     * Method responsible for rendering the SailScreen on each frame. This clears the screen, updates the map and
     * visible entities, then calls the stage act. This causes actors (entities) on the stage to move (act).
     *
     * @param delta Time between last and current frame.
     */
    @Override
    public void renderInner(float delta) {
        //if player dead, go to main menu
        Player player = gameInstance.getEntityManager().getOrCreatePlayer();
        BuildingManager buildingManager = gameInstance.getBuildingManager();
        EntityManager entityManager = gameInstance.getEntityManager();
        if (player.isDead()) {
            Gdx.app.debug("SailScreen", "Player Died!");
            gameInstance.fadeSwitchScreen(new EndScreen(gameInstance, false));
            return;
        }

        if(!player.isDying()) {
            //spawns/despawns entities, handles animations and projectiles
            entityManager.handleStageEntities(getStage(), delta);
        } else {
            //when the player is dying - only process animations
            entityManager.getAnimationManager().handleEffects(getStage(),delta);
        }
        if (gameInstance.getPirateMap().isObjectsEnabled()) {
            gameInstance.getBuildingManager().spawnCollegeEnemies(delta);
            buildingManager.checkBossSpawn();
            buildingManager.departmentPrompt();
            buildingManager.minigamePrompt();
        }

        handleHealthBars();

        checkCollisions();

        // Update camera and focus on player.
        getOrthographicCamera().position.set(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f, 0);
        getOrthographicCamera().update();
        getBatch().setProjectionMatrix(getOrthographicCamera().combined);
        gameInstance.getTiledMapRenderer().setView(getOrthographicCamera());
        gameInstance.getTiledMapRenderer().render();

        //debug
        if (gameInstance.getGame().DEBUG) {
            shapeRenderer.setProjectionMatrix(getBatch().getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);

            for (Polygon polygonMapObject : gameInstance.getPirateMap().getCollisionObjects()) {
                shapeRenderer.polygon(polygonMapObject.getTransformedVertices());
            }
            shapeRenderer.end();
        }


        //Added for assessment 3: enabled changes to SailHUD for entering shops
//        if(inDepartment) {
//            gameInstance.getShopUI().getStage().act();
//            gameInstance.getShopUI().getStage().draw();
//        }
    }

    /**
     * Handles HealthBar elements for damaged actors.
     */
    private void handleHealthBars() {
        Player player = gameInstance.getEntityManager().getOrCreatePlayer();
        if (player.getHealth() < player.getMaxHealth()) {
            if (!getStage().getActors().contains(player.getHealthBar(), true)) {
                getStage().addActor(player.getHealthBar());
            }
        }

        for (uk.ac.york.sepr4.object.entity.NPCBoat NPCBoat : gameInstance.getEntityManager().getNpcList()) {
            if (NPCBoat.getHealth() < NPCBoat.getMaxHealth()) {
                if (!getStage().getActors().contains(NPCBoat.getHealthBar(), true)) {
                    getStage().addActor(NPCBoat.getHealthBar());
                }
            }
        }
        Array<Actor> toRemove = new Array<>();
        for (Actor actors : getStage().getActors()) {
            if (actors instanceof HealthBar) {
                HealthBar healthBar = (HealthBar) actors;
                LivingEntity livingEntity = healthBar.getLivingEntity();
                if (livingEntity.getHealth() == livingEntity.getMaxHealth() || livingEntity.isDead() || livingEntity.isDying()) {
                    toRemove.add(actors);
                }
            }
        }
        getStage().getActors().removeAll(toRemove, true);

    }

    /**
     * Checks whether actors have overlapped. In the instance where projectile and entity overlap, deal damage.
     */
    private void checkCollisions() {
        checkProjectileCollisions();
        checkLivingEntityCollisions();
    }

    public void checkLivingEntityCollisions() {
        EntityManager entityManager = gameInstance.getEntityManager();
        //player/map collision check
        //TODO: Improve to make player a polygon
        for(LivingEntity lE : entityManager.getLivingEntities()) {
            //Between entity and map
            if(gameInstance.getPirateMap().isColliding(lE.getRectBounds())) {
                if (lE.getCollidedWithIsland() == 0) {
                    lE.collide(false, 0f);
                }
            }
            if(lE.getCollidedWithIsland() >= 1) {
                lE.setCollidedWithIsland(lE.getCollidedWithIsland() - 1);
            }

            //between living entities themselves
            //TODO: still a bit buggy
            for(LivingEntity lE2 : entityManager.getLivingEntities()) {
                if(!lE.equals(lE2)) {
                    if(lE.getRectBounds().overlaps(lE2.getRectBounds())) {
                        if(lE.getColliedWithBoat() == 0) {
                            lE.collide(true, AIUtil.normalizeAngle((float)(lE.getAngleTowardsEntity(lE2) - Math.PI)));
                        }
                        //Gdx.app.log("gs", ""+lE.getColliedWithBoat());
                    }
                }
                if(lE.getColliedWithBoat() >= 1) {
                    lE.setColliedWithBoat(lE.getColliedWithBoat() - 1);
                }
            }
        }
    }

    private void checkProjectileCollisions() {
        EntityManager entityManager = gameInstance.getEntityManager();
        Player player = entityManager.getOrCreatePlayer();

        for (LivingEntity livingEntity : entityManager.getLivingEntities()) {
            for (Projectile projectile : entityManager.getProjectileManager().getProjectileList()) {
                if (projectile.getShooter() != livingEntity && projectile.getRectBounds().overlaps(livingEntity.getRectBounds())) {
                    //if bullet overlaps player and shooter not player
                    if (!(livingEntity.isDying() || livingEntity.isDead())) {
                        if (!livingEntity.damage(projectile.getDamage())) {
                            //is dead
                            if(livingEntity instanceof NPCBoat) {
                                Gdx.app.debug("SailScreen", "NPCBoat died.");
                                NPCBoat npcBoat = (NPCBoat) livingEntity;
                                Reward reward = gameInstance.getItemManager().generateReward();
                                reward.setGold(reward.getGold() + (int)npcBoat.getDifficulty());
                                reward.setXp(reward.getXp() + (int)npcBoat.getDifficulty());
                                player.issueReward(reward);
                                //if dead NPC is a boss then player can capture its respective college
                                if(npcBoat.isBoss() && npcBoat.getAllied().isPresent()) {
                                    player.capture(npcBoat.getAllied().get());
                                }
                            } else {
                                Gdx.app.debug("SailScreen", "Player died.");
                            }
                        }
                        Gdx.app.debug("SailScreen", "LivingEntity damaged by projectile.");
                        //kill projectile
                        projectile.setActive(false);
                    }
                }
            }
        }
    }


    //Added for assessment 3: Methods to enter and exit departments
    /**
     * Switch the interface to interact with a department
     * @param name the name of the department
     */
//    public void enterDepartment(String name) {
//        try {
//            //this.shopUI = new ShopUI(this, name);
//        } catch (NameNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        //Gdx.input.setInputProcessor(shopUI.getStage());
//        inDepartment = true;
//        paused = true;
//    }

    /**
     * Exit department
     * Should only be called when in a department
     */
    public void exitDepartment() {
        //shopUI.dispose();
        //inDepartment = false;
        //paused = false;
    }
}
