package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.object.building.*;
import uk.ac.york.sepr4.object.entity.npc.NPCBoat;
import uk.ac.york.sepr4.object.entity.npc.NPCBuilder;
import uk.ac.york.sepr4.object.entity.npc.NPCEntity;
import uk.ac.york.sepr4.object.entity.npc.NPCMonster;
import uk.ac.york.sepr4.object.projectile.ProjectileManager;
import uk.ac.york.sepr4.utils.ShapeUtil;

import java.util.*;


public class EntityManager {

    private Player player;

    @Getter
    private Array<NPCEntity> npcList = new Array<>();

    private GameInstance gameInstance;

    @Getter
    private AnimationManager animationManager;
    @Getter
    private ProjectileManager projectileManager;

    private Integer MAX_ENTITIES = 8;

    private float KRAKEN_CHANCE = 0.25f;

    //time till next spawn attempt
    private float spawnDelta = 0f;

    public EntityManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        this.projectileManager = new ProjectileManager();
        this.animationManager = new AnimationManager(this);
    }
    
    public Player getOrCreatePlayer() {
        if(player == null) {
            player = new Player(gameInstance.getPirateMap().getSpawnPoint());
            animationManager.createWaterTrail(player);
        }
        return player;
    }

    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<>();
        npcList.forEach(npcBoat -> list.add(npcBoat));
        list.add(getOrCreatePlayer());

        return new ArrayList<>(list);
    }

    public boolean isOccupied(Rectangle rectangle) {
        for(LivingEntity livingEntities : getLivingEntities()) {
            if(rectangle.overlaps(livingEntities.getRectBounds())) {
                return true;
            }
        }
        return false;
    }

    public Optional<Building> getPlayerLocation() {
        for(College building : gameInstance.getBuildingManager().getColleges()) {
            if(building.getBuildingZone().contains(player.getX(), player.getY())) {
                return Optional.of(building);
            }
        }
        for(Department building : gameInstance.getBuildingManager().getDepartments()) {
            if(building.getBuildingZone().contains(player.getX(), player.getY())) {
                return Optional.of(building);
            }
        }
        for(MinigameBuilding building : gameInstance.getBuildingManager().getTaverns()) {
            if(building.getBuildingZone().contains(player.getX(), player.getY())) {
                return Optional.of(building);
            }
        }
        return  Optional.empty();
    }



    public void addNPC(NPCEntity npcEntity){
        if(!npcList.contains(npcEntity, false)) {
            this.npcList.add(npcEntity);
            if(npcEntity instanceof NPCBoat) {
                animationManager.createWaterTrail(npcEntity);
            }
        } else {
            Gdx.app.error("EntityManager", "Tried to add an NPC with ID that already exists!");
        }
    }

    public void handleStageEntities(Stage stage, float delta){
        projectileManager.handleProjectiles(stage);
        handleNPCs(stage);
        animationManager.handleEffects(stage, delta);
    }

    /**
     * Adds and removes NPCs as actors from the stage.
     */
    private void handleNPCs(Stage stage) {
        stage.getActors().removeAll(removeDeadNPCs(), true);

        for (NPCEntity npcEntity : npcList) {
            if (!stage.getActors().contains(npcEntity, true)) {
                Gdx.app.debug("EntityManager", "Adding new NPCBoat to actors list.");
                stage.addActor(npcEntity);
            }
        }
    }

    public void spawnEnemies(float delta) {
        spawnDelta+=delta;
        if(spawnDelta >= 1f) {

            //Spawn college NPCs if player close
            BuildingManager buildingManager = gameInstance.getBuildingManager();
            for (College college : buildingManager.getColleges()) {
                //check how many entities already exist in college zone (dont spawn too many)
                if (college.getBuildingZone().contains(player.getRectBounds())) {
                    if (gameInstance.getEntityManager().getLivingEntitiesInArea(college.getBuildingZone()).size
                            < college.getMaxEntities()) {
                        Optional<NPCBoat> optionalEnemy = gameInstance.getBuildingManager().generateCollegeNPC(college, false);
                        if (optionalEnemy.isPresent()) {
                            //checks if spawn spot is valid
                            Gdx.app.debug("Building Manager", "Spawning a college enemy at " + college.getName());
                            addNPC(optionalEnemy.get());
                        }
                    } else {
                        //Gdx.app.debug("BuildingManager", "Max entities @ "+college.getName());
                    }
                }
            }

            HashMap<Polygon, Integer> spawnZones = gameInstance.getPirateMap().getSpawnZones();
            Player player = gameInstance.getEntityManager().getOrCreatePlayer();
            if(npcList.size < MAX_ENTITIES) {
                spawnZones.forEach(((polygon, difficulty) -> {
                    if (polygon.contains(player.getX(), player.getY())) {
                        //player is spawn zone
                        Optional<Vector2> optionalSpawnPos = ShapeUtil.getRandomPosition(polygon);
                        if (optionalSpawnPos.isPresent()) {
                            double dist = player.distanceFrom(optionalSpawnPos.get());
                            Gdx.app.debug("EM", dist+"");
                            if(dist > 500 && dist < 2000) {
                                Random random = new Random();
                                if(KRAKEN_CHANCE>=random.nextFloat()) {
                                    NPCMonster npcMonster = new NPCMonster(optionalSpawnPos.get(), difficulty);
                                    Gdx.app.debug("Building Manager", "Spawning a moster");
                                    addNPC(npcMonster);
                                } else {
                                    NPCBoat npcBoat = new NPCBuilder().generateRandomEnemy(optionalSpawnPos.get(), Optional.empty(),
                                            difficulty, false);
                                    Gdx.app.debug("Building Manager", "Spawning an enemy");
                                    addNPC(npcBoat);
                                }
                            }
                        }
                    }
                }));
            }

            spawnDelta = 0f;
        }
    }

    public Array<LivingEntity> getLivingEntitiesInArea(Rectangle rectangle) {
        Array<LivingEntity> entities = new Array<>();
        for(NPCEntity npcEntity : npcList) {
            if(npcEntity.getRectBounds().overlaps(rectangle)){
                entities.add(npcEntity);
            }
        }
        if(player.getRectBounds().overlaps(rectangle)) {
            entities.add(player);
        }
        return entities;
    }

    private Array<NPCEntity> removeDeadNPCs() {
        Array<NPCEntity> toRemove = new Array<>();
        for(NPCEntity npcEntity : npcList) {
            if(npcEntity.isDead()){
                toRemove.add(npcEntity);
            }
        }
        npcList.removeAll(toRemove, true);
        return toRemove;
    }









}
