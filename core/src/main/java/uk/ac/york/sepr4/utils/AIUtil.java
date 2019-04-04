package uk.ac.york.sepr4.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import uk.ac.york.sepr4.object.entity.Entity;
import uk.ac.york.sepr4.object.entity.LivingEntity;
import uk.ac.york.sepr4.object.entity.NPCBoat;

import java.util.Optional;
import java.util.Random;

public class AIUtil {


    private static float idealDistFromTarget = 250f; //For the distance you want NPC to be away from target (Goldy Lox Zone)
    private static float gradientForNormalDist = 50f; //This is the standard deviation of the normal distabution


    public static void actNPCBoat(NPCBoat npcBoat, float deltaTime) {
        //Clears arrays for later use
        Array<Float> forces = new Array<>();
        Array<Float> angles = new Array<>();

        Random r = new Random();

        if (!npcBoat.isDying()) {
            //TARGET CHECK***************
            // timer to check for new target (expensive if done every tick)
            if (npcBoat.getTargetCheck() < 4f) {
                npcBoat.setTargetCheck(npcBoat.getTargetCheck()+deltaTime);
            }
            //Gets a target
            Optional<LivingEntity> optionalTarget = npcBoat.getTarget();
            if (optionalTarget.isPresent()) {
                LivingEntity target = optionalTarget.get();
                npcBoat.setLastTarget(optionalTarget);
                //***************************


                //FORCES WANTED TO BE COMPUTED***************
                //Explained more on the resultant force function take a look
                float f = AIUtil.normalDistFromMean((float) npcBoat.distanceFrom(target), gradientForNormalDist, idealDistFromTarget); //---Normal Distrubtion 0 to 1 in max force this allows for us to have diffrent forces depending on distances to the player

                //Forces due to the target**
                if ((float) npcBoat.distanceFrom(target) < idealDistFromTarget) {
                    forces.add(1 - f);
                    angles.add(AIUtil.normalizeAngle(npcBoat.getAngleTowardsEntity(target)));
                } else {
                    forces.add(1 - f);
                    angles.add(AIUtil.normalizeAngle(npcBoat.getAngleTowardsEntity(target) - (float) Math.PI));
                }
                forces.add(f);
                angles.add(AIUtil.normalizeAngle(target.getAngle() - (float) Math.PI));
                //**

                //Forces due to the other living entitys**
                for (LivingEntity livingentity : npcBoat.getLivingEntitiesInRangeMinusTarget(target)) {
                    float n = AIUtil.normalDistFromMean((float) npcBoat.distanceFrom(livingentity), 50, 200); //---Normal Distrubtion again but to all livining entitys stops them wanting to collide
                    if ((float) npcBoat.distanceFrom(livingentity) < 200) {
                        forces.add((1 - n) / 2);
                        angles.add(AIUtil.normalizeAngle(npcBoat.getAngleTowardsEntity(livingentity) - (float) Math.PI));
                    } else {
                        forces.add((1 - n) / 2);
                        angles.add(AIUtil.normalizeAngle(npcBoat.getAngleTowardsEntity(livingentity)));
                    }
                }
                //**

                //Other forces can be applied in this way where the forces can be any value. In the cases above the max values they can get is 1 this should give you rough estimates of the power of the forces
                //Really good to add in functions that take into account certain things for strategic poistioning/cool interactions like ramming and whirlpools and things like that *HINT* *HINT*

                //********************************************


                //RESULTANT ANGLE*****************
                //Gets the resultant force of all the forces and angles of those forces given by forces and angles arrays
                //returns and array which is basically a pair being (resultant force, resultant forces angle)
                float ang = AIUtil.resultantForce(angles, forces).get(1);
                //********************************


                //NO DUMB MOVE CHECK**************
                //This section can be made to check whether certain moves maybe a bad move, e.g. moving into projectiles firing line, better strategic poistioning *HINT* *HINT*
                float wantedAngle = ang; //change
                //********************************


                //SPEED STUFF*****************
                //Look at NPC Behaviour 2 for more details/visuallisation
                //If not dodging
                if (npcBoat.getDodging() == 0) {
                    //gets the normal of the angle towards the boat explained in NPC Functions 3
                    float NormalFactor = Math.min(AIUtil.normalDistFromMean(AIUtil.angleDiffrenceBetweenTwoAngles(AIUtil.normalizeAngle(target.getAngle()), npcBoat.getAngleTowardsEntity(target)), (float) Math.PI / 8, (float) Math.PI / 2) * 100, 1f);
                    float A;
                    if (target.getSpeed() > npcBoat.getMaxSpeed()) {
                        A = npcBoat.getMaxSpeed();
                    } else {
                        A = target.getSpeed();
                    }
                    float idealSpeed = (1 - f) * npcBoat.getMaxSpeed() + ((f + NormalFactor) / 2) * A;
                    if (idealSpeed > npcBoat.getSpeed()) {
                        npcBoat.setAccelerating(true);
                        npcBoat.setBraking(false);
                    } else {
                        if (npcBoat.getSpeed() / 5 > idealSpeed) {
                            npcBoat.setBraking(true);
                            npcBoat.setAccelerating(false);
                        } else {
                            npcBoat.setAccelerating(false);
                            npcBoat.setBraking(false);
                        }
                    }
                } else {
                    //If dodging act this out
                    npcBoat.setAccelerating(false);
                    npcBoat.setBraking(true);
                    npcBoat.setDodging(npcBoat.getDodging()-1);
                }
                //****************************


                //DODGE STARTER*****************

                //Actual dodge movements are implemented in Speed stuff but for more advanced dodge can be adjusted in both Speed stuff and Turn action *HINT* *HINT*

                //Gets all projectiles that will hit NPC and if above 0
                if (npcBoat.getProjectilesToDodge(npcBoat.getProjectilesInRange()).size > 0) {

                    //Set on a dodge if probability has chosen
                    float prob = 1f * npcBoat.getProjectilesToDodge(npcBoat.getProjectilesInRange()).size;
                    float random = r.nextFloat() * 100f;
                    if (random < prob) {
                        npcBoat.setDodging(100);
                        Gdx.app.debug("NPCBoat", "Dodging");
                    }
                } else {
                    //Stops the NPC being still for longer than needed
                    if (npcBoat.getDodging() > 0) {
                        npcBoat.setDodging(10);
                    }
                }
                //******************************


                //TURN ACTION*******************
                //Stops movement of under PI/16 from actually taking affect
                if (AIUtil.angleDiffrenceBetweenTwoAngles(npcBoat.getAngle(), wantedAngle) < Math.PI / 16) {
                    npcBoat.setTurning(false);
                } else {
                    npcBoat.setTurning(true);
                }

                //Checks for changes in turning so angular speed will be correct
                npcBoat.turnPreCalcs(AIUtil.rightForAngleDiffrenceBetweenTwoAngles(npcBoat.getAngle(), wantedAngle));

                //Sets previous turn to this turns right boolean e.g. if turning right then = true else false meaning left turn
                npcBoat.setPreviousTurn(AIUtil.rightForAngleDiffrenceBetweenTwoAngles(npcBoat.getAngle(), wantedAngle));

                //Sets the angle depending on parameters same as in livingentity
                npcBoat.setAngle(npcBoat.getAngle() + (npcBoat.getAngularSpeed() * deltaTime) * (npcBoat.getSpeed() / npcBoat.getMaxSpeed()) % (float) (2 * Math.PI));
                //******************************


                //FIRING************************
                //Calculates perfectShot into fireangle then adds some randomness to the shot with the parameter of accuracy which is inveresed

                if (target.getSpeed() < target.getMaxSpeed() / 5) {
                    float fireangle = npcBoat.getAngleTowardsEntity(target);
                    //Calls fire at angle
                    npcBoat.fire((float) (fireangle + (-(1 / npcBoat.getAccuracy()) * (Math.PI / 32) + r.nextFloat() * (2 * (1 / npcBoat.getAccuracy()) * (Math.PI / 32)))), npcBoat.getDamage());
                } else {
                    //Stops the AI shooting at distances that are longer than 3 seconds due to infinte inteception points, if going parrell
                    if (AIUtil.timeForPerfectAngleToCollide(npcBoat, target, AIUtil.thetaForAngleDiffrence(AIUtil.normalizeAngle(target.getAngle()), npcBoat.getAngleTowardsEntity(target)), 100) < 3) {
                        float fireangle = AIUtil.perfectAngleToCollide(npcBoat, target, 100);
                        //calls fire at angle
                        npcBoat.fire((float) (fireangle + (-(1 / npcBoat.getAccuracy()) * (Math.PI / 32) + r.nextFloat() * (2 * (1 / npcBoat.getAccuracy()) * (Math.PI / 32)))), npcBoat.getDamage());
                    }
                }
                //******************************
            } else {
                //PATROL**********************
                npcBoat.setAccelerating(false);
                npcBoat.setDodging(0);
                //TODO: Pursue for a bit if had a previous target, then stop moving
                //****************************
            }
        }
    }

    /**
     * @param x1 The original x position
     * @param angle The angle you want to move the x by
     * @param distance The distance you want to move the x by on that angle
     * @return The x position once x1 has been moved along by that angle for a certain distance
     */
    public static float getXwithAngleandDistance(float x1, float angle, float distance) {
        return (float)(x1 + distance*Math.sin(angle));
    }

    //Same as before but Y
    public static float getYwithAngleandDistance(float y1, float angle, float distance) {
        return (float)(y1 - distance*Math.cos(angle));
    }

    /**
     * Takes any angle and converts it back down to the range 0 to 2PI
     * This is due to how LibGDX uses angles where you can have an actor with 10pi if rotated 5 times
     *
     * @param angle
     * @return angle restricted to 0 to 2PI
     */
    public static float normalizeAngle(float angle) {
        //Have to do it in this bad way due to angle mod 2PI not working
        return (float) (angle%(2*Math.PI));
    }

    /**
     * Refer to NPC Functions 4
     * @param thetaP
     * @param thetaTP
     * @return theta (Green angle)
     */
    private static float thetaForAngleDiffrence(double thetaP, double thetaTP){
        double theta;
        if (thetaP <= thetaTP && thetaTP <= Math.PI) {
            theta = thetaP + (Math.PI - thetaTP);
        } else if ((2 * Math.PI - thetaP) <= (2 * Math.PI - thetaTP) && (2 * Math.PI - thetaTP) <= Math.PI) {
            theta = (2 * Math.PI - thetaP) + (Math.PI - (2 * Math.PI - thetaTP));
        } else if (thetaTP <= Math.PI && thetaP > thetaTP && (2 * Math.PI - thetaP) >= (Math.PI - thetaTP)) {
            theta = (2 * Math.PI - thetaP) - (Math.PI - thetaTP);
        } else if ((2 * Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && thetaP >= (Math.PI - (2 * Math.PI - thetaTP))) {
            theta = thetaP - (Math.PI - (2 * Math.PI - thetaTP));
        } else if (thetaTP <= Math.PI && thetaP > thetaTP && (2 * Math.PI - thetaP) <= (Math.PI - thetaTP)) {
            theta = (Math.PI - thetaTP) - (2 * Math.PI - thetaP);
        } else if ((2 * Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && (Math.PI - (2 * Math.PI - thetaTP)) >= thetaP) {
            theta = (Math.PI - (2 * Math.PI - thetaTP)) - thetaP;
        } else {
            theta = 0;
        }
        return (float)theta;
    }

    /**
     * Refer to NPC Functions 4 but rather now just returns whether the angle is turning right or left
     * @param thetaP
     * @param thetaTP
     * @return right = true, left = false
     */
    private static boolean rightThetaForAngleDiffrence(double thetaP, double thetaTP){
        boolean right;
        if (thetaP <= thetaTP && thetaTP <= Math.PI) {
            right = true;
        } else if ((2 * Math.PI - thetaP) <= (2 * Math.PI - thetaTP) && (2 * Math.PI - thetaTP) <= Math.PI) {
            right = false;
        } else if (thetaTP <= Math.PI && thetaP > thetaTP && (2 * Math.PI - thetaP) >= (Math.PI - thetaTP)) {
            right = false;
        } else if ((2 * Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && thetaP >= (Math.PI - (2 * Math.PI - thetaTP))) {
            right = true;
        } else if (thetaTP <= Math.PI && thetaP > thetaTP && (2 * Math.PI - thetaP) <= (Math.PI - thetaTP)) {
            right = true;
        } else if ((2 * Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && (Math.PI - (2 * Math.PI - thetaTP)) >= thetaP) {
            right = false;
        } else {
            right = true;
        }
        return right;
    }

    /**
     * Refer to NPC Functions 2
     * @param source
     * @param target
     * @param addedSpeed - A value to increase the projectile's speed (usually by the source's speed)
     * @return - Returns the angle to shoot at or move in to hit the target at the right time going at the current speed
     */
    public static float perfectAngleToCollide(Entity source, Entity target, double addedSpeed) {
        double thetaP = normalizeAngle(target.getAngle());
        double thetaTP = source.getAngleTowardsEntity(target);

        boolean right = rightThetaForAngleDiffrence(thetaP, thetaTP);
        double theta = thetaForAngleDiffrence(thetaP, thetaTP);

        double b = timeForPerfectAngleToCollide(source, target, (float)theta, addedSpeed);
        double time = source.distanceFrom(target) / (source.getSpeed() + addedSpeed);

        double SE = (source.getSpeed() + addedSpeed);
        double SP = target.getSpeed();

        double PM = b * SP;
        double EP = time * SE;
        double ME = b * SE;

        double shotAngle = Math.acos(((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP));

        if (right == true) {
            shotAngle = thetaTP - shotAngle;
        } else {
            shotAngle = thetaTP + shotAngle;
        }
        if (((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP) > 1 || ((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP) < -1) {
            shotAngle = thetaTP;
        }

        return normalizeAngle((float) shotAngle);
    }

    /**
     * Refer to NPC Functions 2
     * @param source
     * @param target
     * @param theta
     * @param addedSpeed
     * @return b = the time taken for the source object or shot to connect with the target
     */
    private static float timeForPerfectAngleToCollide(Entity source, Entity target, float theta, double addedSpeed) {
        double time = source.distanceFrom(target) / (source.getSpeed() + addedSpeed);

        double b = time / (2 * Math.cos(theta));
        if (b < 0) {
            b = -b;
        }
        return (float) b;
    }

    //Functions for knowing the force due the distance
    private static float normalDistFromMean(float dist, float standardDeviation, float mean) {
        //Formula for a normal distbution to find the height
        double fx = (1 / (Math.sqrt(2 * Math.PI) * (double)standardDeviation)) * Math.pow(Math.E, -(Math.pow((dist - (double) mean), 2) / (2 * Math.pow((double)standardDeviation, 2))));
        //Just incase it goes over 1 for error stops
        if(fx/(1 / (Math.sqrt(2 * Math.PI) * (double)standardDeviation)) * Math.pow(Math.E, -(1 / (2 * Math.pow((double)standardDeviation, 2)))) > 1){
            return 1f;
        }
        //Is fx/by max fx when the dist = mean
        return (float) (fx/(1 / (Math.sqrt(2 * Math.PI) * (double)standardDeviation)) * Math.pow(Math.E, -(1 / (2 * Math.pow((double)standardDeviation, 2)))));
    }

    private static float straightLineGraphOneIfCloser(float dist, float lowestdist, float startdist) {
        if(dist <= lowestdist){
            return 1f;
        } else if (dist<= startdist){
            return 0.01f*(dist-lowestdist); //possible problem
        } else {
            return 0f;
        }
    }
    //********************

    //Returns the diffrence between 2 angles where angle 1 is the one with the respect (Same as doing a dot product of 2 vectors basically)
    private static float angleDiffrenceBetweenTwoAngles(float angle1, float angle2){
        angle1 = normalizeAngle(angle1);
        angle2 = normalizeAngle(angle2);
        if (normalizeAngle(angle2 - angle1) > Math.PI){
            return (float)(2* Math.PI - normalizeAngle(angle2 - angle1));
        } else {
            return normalizeAngle(angle2 - angle1);
        }
    }

    //Returns true if angle2 is right of angle1 (meaning if I travel along angle1 then turn to angle2 will it be left or right)
    private static boolean rightForAngleDiffrenceBetweenTwoAngles(float angle1, float angle2){
        angle1 = normalizeAngle(angle1);
        angle2 = normalizeAngle(angle2);
        if (angleDiffrenceBetweenTwoAngles(angle1, angle2) >= 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Refer to NPC Functions 5 - Same as adding together a series of vectors
     * @param angles
     * @param forces
     * @return An array acting as a pair (Resultant force magnitude, Resultant force angle) [Basically a vector]
     */
    private static Array<Float> resultantForce(Array<Float> angles, Array<Float> forces){
        Array<Float> force_angle = new Array<Float>();
        float N = 0, E = 0;
        double sigma;
        for (int i = 0; i<angles.size; i++){
            if (normalizeAngle(angles.get(i)) <= Math.PI/2){
                E += forces.get(i)*Math.sin(angles.get(i));
                N -= forces.get(i)*Math.cos(angles.get(i));
            } else if (normalizeAngle(angles.get(i)) <= Math.PI){
                E += forces.get(i)*Math.cos(angles.get(i) - Math.PI/2);
                N += forces.get(i)*Math.sin(angles.get(i) - Math.PI/2);
            } else if (normalizeAngle(angles.get(i)) <= 3*Math.PI/2){
                E -= forces.get(i)*Math.sin(angles.get(i) - Math.PI);
                N += forces.get(i)*Math.cos(angles.get(i) - Math.PI);
            } else {
                E -= forces.get(i)*Math.cos(angles.get(i) - 3*Math.PI/2);
                N -= forces.get(i)*Math.sin(angles.get(i) - 3*Math.PI/2);
            }
        }
        if (N >= 0 && E <= 0) {
            sigma = Math.atan(-E / N);
        } else if (N <= 0 && E <= 0) {
            sigma = (Math.PI / 2 + Math.atan(-N / -E));
        } else if (N <= 0 && E >= 0) {
            sigma = (Math.PI + Math.atan(E / -N));
        } else {
            sigma = ((3 * Math.PI) / 2 + Math.atan(N / E));
        }

        force_angle.add((float)Math.sqrt(N*N + E*E));
        force_angle.add((float)sigma);
        return force_angle;
    }
}
