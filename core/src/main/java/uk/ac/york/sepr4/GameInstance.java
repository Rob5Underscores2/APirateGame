package uk.ac.york.sepr4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import lombok.Data;
import uk.ac.york.sepr4.hud.MessageHUD;
import uk.ac.york.sepr4.hud.PauseHUD;
import uk.ac.york.sepr4.hud.StatsHUD;
import uk.ac.york.sepr4.object.PirateMap;
import uk.ac.york.sepr4.object.building.BuildingManager;
import uk.ac.york.sepr4.object.building.ShopUI;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.item.ItemManager;
import uk.ac.york.sepr4.object.quest.QuestManager;
import uk.ac.york.sepr4.screen.PirateScreen;
import uk.ac.york.sepr4.screen.SailScreen;
import uk.ac.york.sepr4.screen.TransitionScreen;

@Data
public class GameInstance {

    public static GameInstance INSTANCE;
    private APirateGame game;
    private SailScreen sailScreen;

    private PirateMap pirateMap;
    private TiledMapRenderer tiledMapRenderer;

    private ItemManager itemManager;
    private EntityManager entityManager;
    private QuestManager questManager;
    private BuildingManager buildingManager;

    private StatsHUD statsHUD;
    private MessageHUD messageHUD;
    private PauseHUD pauseHUD;

    private ShopUI shopUI;

    private boolean paused = false;

    public GameInstance(APirateGame game) {
        this.game = game;
        INSTANCE = this;

        // Locate and set up tile map.
        pirateMap = new PirateMap(new TmxMapLoader().load("map/PirateMap.tmx"));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(pirateMap.getTiledMap(), 1 / 2f);

        // Initialize game managers
        this.itemManager = new ItemManager();
        this.entityManager = new EntityManager(this);
        this.questManager = new QuestManager(entityManager);
        this.buildingManager = new BuildingManager(this);

        // Create HUDs (display for xp, gold, etc..)
        this.statsHUD = new StatsHUD(this);
        this.messageHUD = new MessageHUD(this);
        this.pauseHUD = new PauseHUD(this);

        sailScreen = new SailScreen(this);

        start();
    }

    public void start() {
        Gdx.app.debug("GameInstance", "Starting Instance");
        switchScreen(sailScreen);
    }

    /***
     * Util method - for quick access.
     * @param screen
     */
    public void switchScreen(Screen screen) {
        game.setScreen(screen);
    }

    /***
     * Fade switch screen. Fade from current screen to target screen.
     * @param fadeIn target screen
     * @param dispose whether to dispose current screen
     */
    public void fadeSwitchScreen(PirateScreen fadeIn, boolean dispose) {
        if(game.getScreen() instanceof TransitionScreen) {
            //dont transition if already transitioning (avoids unnecessary checks in implementing classes)
            return;
        }
        switchScreen(new TransitionScreen(this, (PirateScreen) game.getScreen(), fadeIn, dispose));
    }

    /***
     * Default fade switch screen - do not dispose.
     * @param fadeIn target screen
     */
    public void fadeSwitchScreen(PirateScreen fadeIn) {
        fadeSwitchScreen(fadeIn, false);
    }

}