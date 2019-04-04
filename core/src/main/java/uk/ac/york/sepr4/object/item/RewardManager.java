package uk.ac.york.sepr4.object.item;

import lombok.Data;
import java.util.Random;

@Data
public class RewardManager {

    private static Integer baseXP = 10, baseGold = 100;

    public static Reward generateReward(Integer difficulty) {
        Reward reward = generateReward();
        reward.setGold(reward.getGold() + difficulty);
        reward.setXp(reward.getXp() + difficulty);
        return reward;
    }

    public static Reward generateReward() {
        Random random = new Random();
        Double scale = random.nextDouble();

        return new Reward(baseXP + (scale.intValue()*100), baseGold + (scale.intValue()*10));
    }

}
