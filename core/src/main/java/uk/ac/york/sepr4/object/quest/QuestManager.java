package uk.ac.york.sepr4.object.quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;
import uk.ac.york.sepr4.object.entity.EntityManager;

@Data
public class QuestManager {

    private Array<Quest> questList;
    private EntityManager entityManager;

    public QuestManager(EntityManager entityManager) {
        this.entityManager = entityManager;

        Json json = new Json();
        this.questList= json.fromJson(Array.class, Quest.class, Gdx.files.internal("quests.json"));

    }

    /**
     * Checks through the current questList to find the most recently started and not completed quest.
     * @return Quest in progress
     */
    public Quest getCurrentQuest(){
        for (Quest quest : questList){
            if (quest.isStarted() && !(quest.isCompleted())) {
                return quest;
            }
        }
        return null;
    }

    /**
    * Returns the current quest if there is one, otherwise states no quest active.
    * @return String representation of quest status.
    **/
    public String getQuestStatus() {
        if (this.getCurrentQuest() == null) {
            return "No quests active";
        }
        else{
            return this.getCurrentQuest().getName();
        }
    }

}
