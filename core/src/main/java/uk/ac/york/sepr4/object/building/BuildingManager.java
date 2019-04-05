package uk.ac.york.sepr4.object.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.object.crew.CrewMember;
import uk.ac.york.sepr4.object.entity.npc.NPCBoat;
import uk.ac.york.sepr4.object.entity.npc.NPCBuilder;
import uk.ac.york.sepr4.object.entity.Player;

import java.util.Optional;
import java.util.Random;

@Data
public class BuildingManager {

    private Array<College> colleges = new Array<>();
    private Array<Department> departments = new Array<>();
    private Array<MinigameBuilding> taverns = new Array<>();

    private GameInstance gameInstance;

    //time till next spawn attempt
    private float spawnDelta;

    /***
     * This class handles instances of buildings (Colleges and Departments)
     *
     * It is responsible for loading from file and making sure the map object relating to this building is present.
     * There is a method which arranges spawning of college enemies.
     * @param gameInstance
     */
    public BuildingManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.spawnDelta = 0f;

        if(gameInstance.getPirateMap().isObjectsEnabled()) {
            Json json = new Json();
            loadBuildings(json.fromJson(Array.class, College.class, Gdx.files.internal("data/colleges.json")));
            loadBuildings(json.fromJson(Array.class, Department.class, Gdx.files.internal("data/departments.json")));
            loadBuildings(json.fromJson(Array.class, MinigameBuilding.class, Gdx.files.internal("data/minigame.json")));
            Gdx.app.log("BuildingManager",
                    "Loaded "+colleges.size+" colleges and "+departments.size+" departments!");

        } else {
            Gdx.app.error("Building Manager", "Objects not enabled, not loading buildings!");
        }
    }

    //TODO: Could have a cooldown here
    public void checkBossSpawn() {
        for(College college : colleges) {
            if(!college.isBossSpawned()) {
                //TODO: Add collision check for boss spawn
                Player player = gameInstance.getEntityManager().getOrCreatePlayer();
                if (college.getBuildingZone().contains(player.getRectBounds())) {
                    Gdx.app.debug("BuildingManager", "Player entered college zone: " + college.getName());
                    Optional<NPCBoat> npcBoss = generateCollegeNPC(college, true);
                    if(npcBoss.isPresent()) {
                        college.setBossSpawned(true);
                        gameInstance.getEntityManager().addNPC(npcBoss.get());
                    }

                }
            }
        }
    }

    private Optional<Vector2> getValidRandomSpawn(College college, float size) {
        int attempts = 0;
        while (attempts<10) {
            Vector2 test = college.getRandomSpawnVector();
            Rectangle rectangle = new Rectangle(test.x-(size/2), test.y-(size/2), size, size);
            if(!gameInstance.getPirateMap().isColliding(rectangle)
                    && !gameInstance.getEntityManager().isOccupied(rectangle)) {
                return Optional.of(test);
            }
            attempts++;
        }
        return Optional.empty();
    }

    /**
     * Generate an NPCBoat which has the appropriate position and difficulty for a college
     * @param college College for which the NPCBoat is being generated
     * @param boss Whether the generated npc is a boss
     * @return       An NPCBoat with correct parameters
     */
    private Optional<NPCBoat> generateCollegeNPC(College college, boolean boss) {
        Random random = new Random();
        if(random.nextDouble() <= college.getSpawnChance()){
            Optional<Vector2> pos = getValidRandomSpawn(college, 250f);
            if(pos.isPresent()) {
                NPCBoat boat = new NPCBuilder().generateRandomEnemy( pos.get(), college,
                         boss ? college.getBossDifficulty().floatValue() : college.getEnemyDifficulty().floatValue(), boss);
                return Optional.of(boat);
            }
        }
        return Optional.empty();
    }

    public void spawnCollegeEnemies(float delta) {
        spawnDelta+=delta;
        if(spawnDelta >= 1f) {
            for (College college : this.colleges) {
                //check how many entities already exist in college zone (dont spawn too many)
                if(gameInstance.getEntityManager().getLivingEntitiesInArea(college.getBuildingZone()).size
                        < college.getMaxEntities()) {
                    Optional<NPCBoat> optionalEnemy = generateCollegeNPC(college,false);
                    if (optionalEnemy.isPresent()) {
                        //checks if spawn spot is valid
                        Gdx.app.debug("Building Manager", "Spawning an enemy at " + college.getName());
                        gameInstance.getEntityManager().addNPC(optionalEnemy.get());
                    }
                } else {
                    //Gdx.app.debug("BuildingManager", "Max entities @ "+college.getName());
                }
            }
            spawnDelta = 0f;
        }
    }

    //TODO: Make generic method and remove duplicate code
    private void loadBuildings(Array<Building> loading) {
        for(Building building : loading) {
            if (building.load(gameInstance.getPirateMap())) {
                if(building instanceof College) {
                    //check if crew member id is defined
                    College college = (College) building;
                    Optional<CrewMember> optionalCrewMember =
                            gameInstance.getCrewBank().getCrewFromID(college.getCrewMemberId());
                    if(optionalCrewMember.isPresent()) {
                        college.setCrewMember(optionalCrewMember.get());
                        colleges.add(college);
                    } else {
                        Gdx.app.error("BuildingManager", "Failed to load " + building.getName() + ": Crew Member ID not valid!");
                    }                } else if (building instanceof Department) {
                    //check if crew member id is defined
                    Department department = (Department) building;
                    Optional<CrewMember> optionalCrewMember =
                            gameInstance.getCrewBank().getCrewFromID(department.getCrewMemberId());
                    if(optionalCrewMember.isPresent()) {
                        department.setCrewMember(optionalCrewMember.get());
                        departments.add(department);
                    } else {
                        Gdx.app.error("BuildingManager", "Failed to load " + building.getName() + ": Crew Member ID not valid!");
                    }
                } else if (building instanceof MinigameBuilding) {
                    taverns.add((MinigameBuilding) building);
                }
                Gdx.app.debug("BuildingManager", "Loaded " + building.getName());
            } else {
                Gdx.app.error("BuildingManager", "Failed to load " + building.getName());
            }

        }
    }
}
