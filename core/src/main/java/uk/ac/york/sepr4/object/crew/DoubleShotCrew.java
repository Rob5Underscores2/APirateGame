package uk.ac.york.sepr4.object.crew;

public class DoubleShotCrew extends CrewMember {


    public DoubleShotCrew() {
        super(1, "Double Shot", "1", 5.0,
                100, 3, 5, 15f);
    }

    @Override
    public boolean fire() {
        if(getCurrentCooldown() == 0) {
            //can fire

            setCurrentCooldown(getCooldown());
            return true;
        }
        //cooling down
        return false;
    }

    @Override
    protected Double getDamage() {
        return null;
    }
}
