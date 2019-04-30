package uk.ac.york.sepr4.object.item;

import com.badlogic.gdx.Gdx;
import lombok.Data;

import java.util.Random;

@Data
public class RewardManager {

    private static Integer baseXP = 10, baseGold = 5;

    public static Reward generateReward(Integer difficulty) {

        Reward reward = new Reward(baseXP, baseGold);
        reward.setGold(reward.getGold() * difficulty);
        reward.setXp(reward.getXp() * difficulty);
        Gdx.app.debug("RM", "Generated Gold: "+reward.getGold()+", XP: "+reward.getXp()
        + " for difficulty: "+difficulty);
        return reward;
    }

}
