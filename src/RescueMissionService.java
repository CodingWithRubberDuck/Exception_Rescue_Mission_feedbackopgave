import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

//Responsible for doing logic-checks and throws exceptions if not
public class RescueMissionService {
    private final ExceptionLogger logger;
    private SpaceShip ship = null;
    private List<DangerEvent> dangerEvents;
    private List<OpportunityEvent> opportunityEvents;

    private final Random random = new Random();

    public RescueMissionService(ExceptionLogger logger){
        this.logger = logger;
    }


    public void createShip(String captainName, String shipName){
        final int FUEL_START = 100;
        final int INTEGRITY_START = 100;
        final int SPARE_PARTS_START = 10;
        final int SHIELD_LEVEL_START = 0;
        final boolean REPAIR_KIT_USED_START = false;

        if (ship == null){
            ship = new SpaceShip(FUEL_START, INTEGRITY_START, SPARE_PARTS_START, SHIELD_LEVEL_START, REPAIR_KIT_USED_START, captainName, shipName);
        } else {
            ship.setFuel(FUEL_START);
            ship.setIntegrity(INTEGRITY_START);
            ship.setSpareParts(SPARE_PARTS_START);
            ship.setShieldLevel(SHIELD_LEVEL_START);
            ship.setRepairKitUsed(REPAIR_KIT_USED_START);
            ship.setCaptainName(captainName);
            ship.setShipName(shipName);
        }
    }

    public SpaceShip getShip(){
        return ship;
    }


    public void resetEventLists(){
        dangerEvents = Arrays.asList(DangerEvent.values());
        opportunityEvents = Arrays.asList(OpportunityEvent.values());
    }


    public boolean handlePlayAnswer(String answer){
        final int YES_AND_MIN_OPTION = 1;
        final int NO_AND_MAX_OPTION = 2;
        try {
           int choice = Integer.parseInt(answer);
           if (choice >= YES_AND_MIN_OPTION && choice <= NO_AND_MAX_OPTION){
               if (choice == YES_AND_MIN_OPTION){
                   return true;
               } else if (choice == NO_AND_MAX_OPTION) {
                   return false;
               }
               throw new IllegalArgumentException("Fejl: Logisk fejl som ikke burde kunne ske, prøv igen");
               //LOGGER
           } else {
               throw new IllegalArgumentException("Fejl: Indtast et tal fra " + YES_AND_MIN_OPTION + " til " + NO_AND_MAX_OPTION);
               //LOGGER
           }
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Fejl: Indtast et tal");
            //LOGGER
        }
    }


    public boolean handleCaptainName(String answer){
        final int MAX_LENGTH = 40;
        if (answer.length() > MAX_LENGTH){
            throw new IllegalArgumentException("Fejl: Kaptajnens navn kan maksimalt være " + MAX_LENGTH + " tegn");
            //LOGGER
        }
        if (answer.trim().isBlank()){
            throw new IllegalArgumentException("Fejl: Kaptajnens navn kan ikke være tomt");
            //LOGGER
        }
        return true;
    }

    public boolean handleShipName(String answer){
        final int MAX_LENGTH = 40;
        if (answer.length() > MAX_LENGTH){
            throw new IllegalArgumentException("Fejl: Rumskibets navn kan maksimalt være " + MAX_LENGTH + " tegn");
            //LOGGER
        }
        if (answer.trim().isBlank()){
            throw new IllegalArgumentException("Fejl: Rumskibets navn kan ikke være tomt");
            //LOGGER
        }
        return true;
    }


    public Event getRandomEvent(int round){
        if (round%2!=0){
            int chosenIndex = random.nextInt(0, dangerEvents.size());
            DangerEvent chosenEvent = dangerEvents.get(chosenIndex);
            dangerEvents.remove(chosenIndex);
            return chosenEvent;
        } else {
            int chosenIndex = random.nextInt(0, opportunityEvents.size());
            OpportunityEvent chosenEvent = opportunityEvents.get(chosenIndex);
            opportunityEvents.remove(chosenIndex);
            return chosenEvent;
        }
    }


    public int handleGeneralEventChoice(String answer, int maxOptions){
        final int MIN_OPTION = 1;
        try{
            int choice = Integer.parseInt(answer);
            if (choice <= maxOptions && choice >= MIN_OPTION){
                return choice;
            } else {
                throw new IllegalArgumentException("Fejl: Indtast et tal fra " + MIN_OPTION + " til " + maxOptions);
                //LOGGER
            }
        } catch (NumberFormatException nfe){
            throw new IllegalArgumentException("Fejl: Indtast et tal");
            //LOGGER
        }
    }


    public void useRepairKit(){
        if (ship.getIsRepairKitUsed()){
            throw new NotUsableException("Fejl: Du har allerede brugt dit repair kit");
        } else {
            final int repairKitIncrease = 20;
            ship.increaseIntegrity(repairKitIncrease);
            ship.setRepairKitUsed(true);
        }
    }



    public int[] calculatingSpaceStorm(int choice) {
        //Through storm
        final int FLY_THROUGH = 1;
        //Other way
        final int OTHER_WAY = 2;
        //Logic
        if (choice == FLY_THROUGH) {
            final int FLY_FUEL_COST = 5;
            final int FLY_MIN_DAMAGE = 15;
            final int FLY_MAX_DAMAGE = 35;
            ship.useFuel(FLY_FUEL_COST);
            int damage = random.nextInt(FLY_MIN_DAMAGE, FLY_MAX_DAMAGE + 1);
            int shieldProctection = ship.getShieldProtectionAmount();

            if (shieldProctection>0){
                ship.loseIntegrity(damage-ship.getShieldProtectionAmount());
                return new int[]{damage, ship.getShieldLevel(), shieldProctection, FLY_FUEL_COST};
            } else {
                ship.loseIntegrity(damage);
                return new int[]{damage, FLY_FUEL_COST};
            }
        }
        if (choice == OTHER_WAY) {
            final int OTHER_FUEL_COST = 15;
            final int OTHER_MIN_DAMAGE = 5;
            final int OTHER_MAX_DAMAGE = 20;
            ship.useFuel(OTHER_FUEL_COST);
            int damage = random.nextInt(OTHER_MIN_DAMAGE, OTHER_MAX_DAMAGE + 1);
            int shieldProctection = ship.getShieldProtectionAmount();

            if (shieldProctection>0){
                ship.loseIntegrity(damage-ship.getShieldProtectionAmount());
                return new int[]{damage, ship.getShieldLevel(), shieldProctection, OTHER_FUEL_COST};
            } else {
                ship.loseIntegrity(damage);
                return new int[]{damage, OTHER_FUEL_COST};
            }
        }
        throw new IllegalArgumentException("Fejl: Der er gået noget ukendt galt i logikken");
        //LOGGER
    }


    public int[] calculatingHostileShip(int choice){
        //Runaway
        final int FLY_AWAY = 1;
        //Scare away
        final int SCARE_AWAY = 2;
        //Logic
        if (choice == FLY_AWAY){
            final int FLY_MIN_FUEL_COST = 15;
            final int FLY_MAX_FUEL_COST = 30;
            final int FLY_MIN_DAMAGE = 5;
            final int FLY_MAX_DAMAGE = 10;

            int fuelUsed = random.nextInt(FLY_MIN_FUEL_COST, FLY_MAX_FUEL_COST+1);
            ship.useFuel(fuelUsed);
            int damage = random.nextInt(FLY_MIN_DAMAGE, FLY_MAX_DAMAGE + 1);
            int shieldProctection = ship.getShieldProtectionAmount();

            if (shieldProctection>0){
                ship.loseIntegrity(damage-ship.getShieldProtectionAmount());
                return new int[]{damage, ship.getShieldLevel(), shieldProctection, fuelUsed};
            } else {
                ship.loseIntegrity(damage);
                return new int[]{damage, fuelUsed};
            }
        }
        if (choice == SCARE_AWAY){
            final int LOWEST_PERCENTAGE = 1;
            final int HIGHEST_PERCENTAGE = 100;
            final int GOOD_OUTCOME_EQUAL_OR_LOWER = 40;
            int outcomeDecider = random.nextInt(LOWEST_PERCENTAGE, HIGHEST_PERCENTAGE + 1);

            if (outcomeDecider >= GOOD_OUTCOME_EQUAL_OR_LOWER){
                final int GOOD_MIN_DAMAGE = 0;
                final int GOOD_MAX_DAMAGE = 10;
                final int FUEL_USED = 5;
                ship.useFuel(FUEL_USED);
                int damage = random.nextInt(GOOD_MIN_DAMAGE, GOOD_MAX_DAMAGE + 1);
                int shieldProctection = ship.getShieldProtectionAmount();

                if (shieldProctection>0){
                    ship.loseIntegrity(damage-ship.getShieldProtectionAmount());
                    return new int[]{damage, ship.getShieldLevel(), shieldProctection, FUEL_USED};
                } else {
                    ship.loseIntegrity(damage);
                    return new int[]{damage, FUEL_USED};
                }
            } else {
                final int BAD_MIN_DAMAGE = 20;
                final int BAD_MAX_DAMAGE = 40;
                final int FUEL_USED = 10;

                ship.useFuel(FUEL_USED);
                int damage = random.nextInt(BAD_MIN_DAMAGE, BAD_MAX_DAMAGE + 1);
                int shieldProctection = ship.getShieldProtectionAmount();

                if (shieldProctection>0){
                    ship.loseIntegrity(damage-ship.getShieldProtectionAmount());
                    return new int[]{damage, ship.getShieldLevel(), shieldProctection, FUEL_USED};
                } else {
                    ship.loseIntegrity(damage);
                    return new int[]{damage, FUEL_USED};
                }
            }
        }
        throw new IllegalArgumentException("Fejl: Der er gået noget ukendt galt i logikken");
        //LOGGER
    }

    public boolean calculatingMotorMalfunction(int choice, int chancesLeft) {
        final int FINAL_CHANCE = 1;
        //Through storm
        final int NO_PARTS = 1;
        //Other way
        final int WITH_PARTS = 2;
        //Logic
        if (choice == NO_PARTS) {
            final int LOWEST_PERCENTAGE = 1;
            final int HIGHEST_PERCENTAGE = 100;
            final int GOOD_OUTCOME_EQUAL_OR_LOWER = 50;

            int outcomeDecider = random.nextInt(LOWEST_PERCENTAGE, HIGHEST_PERCENTAGE+1);
            if (outcomeDecider <= GOOD_OUTCOME_EQUAL_OR_LOWER){
                return true;
            } else {
                if (chancesLeft <= FINAL_CHANCE){
                    throw new CriticalStatusException("FEJL: Din motor er permanent ødelagt og du har tabt spillet");
                }
                return false;
            }

        }
        if (choice == WITH_PARTS) {
            final int LOWEST_PERCENTAGE = 1;
            final int HIGHEST_PERCENTAGE = 100;
            final int GOOD_OUTCOME_EQUAL_OR_LOWER = 80;
            final int SPARE_PARTS_COST = 2;

            if (ship.getSpareParts() - SPARE_PARTS_COST >= 0) {
                ship.spentSpareParts(SPARE_PARTS_COST);
                int outcomeDecider = random.nextInt(LOWEST_PERCENTAGE, HIGHEST_PERCENTAGE + 1);
                if (outcomeDecider <= GOOD_OUTCOME_EQUAL_OR_LOWER) {
                    return true;
                } else {
                    if (chancesLeft <= FINAL_CHANCE){
                        throw new CriticalStatusException("FEJL: Din motor er permanent ødelagt og du har tabt spillet");
                    }
                    return false;
                }
            } else {
                throw new InvalidActionException("Fejl: Du har ikke nok reservedele til denne handling");
            }
        }
        throw new IllegalArgumentException("Fejl: Der er gået noget ukendt galt i logikken");
        //LOGGER
    }

    public void motorMalfunctionTakingDamage(){
        final int motorFailureDamage = 15;
        ship.loseIntegrity(15);
    }


    public void useMysteriousTraderFuelUsage(int fuelCost){
        ship.useFuel(fuelCost);
    }






    public int[] calculatingScavengeFacility(int choice){
        //Look for spareParts
        final int PARTS_LOOK = 1;
        //Look for upgrade
        final int SHIELD_LOOK = 2;
        //Pass by
        final int PASS_BY = 3;

        //Logic
        if (choice == PARTS_LOOK) {
            final int PARTS_FUEL_COST = 10;
            final int PARTS_MIN_GAIN = 2;
            final int PARTS_MAX_GAIN = 8;
            ship.useFuel(PARTS_FUEL_COST);
            int partsGained = random.nextInt(PARTS_MIN_GAIN, PARTS_MAX_GAIN + 1);

            return new int[]{partsGained, PARTS_FUEL_COST};
        }
        if (choice == SHIELD_LOOK) {
            final int SHIELD_FUEL_COST = 10;
            final int SHIELD_LOWEST_PERCENTAGE = 1;
            final int SHIELD_HIGHEST_PERCENTAGE = 100;
            final int GOOD_OUTCOME_EQUAL_OR_LOWER = 50;
            final int INCREASE_SHIELD_BY = 1;
            ship.useFuel(SHIELD_FUEL_COST);

            int outcomeDecider = random.nextInt(SHIELD_LOWEST_PERCENTAGE,SHIELD_HIGHEST_PERCENTAGE+1);
            if (outcomeDecider <= GOOD_OUTCOME_EQUAL_OR_LOWER){
                ship.increaseShieldLevelBy(INCREASE_SHIELD_BY);
                return new int[]{ship.getShieldLevel(), ship.getShieldProtectionAmount(), SHIELD_FUEL_COST};
            } else {
                return new int[]{SHIELD_FUEL_COST};
            }
        }
        if (choice == PASS_BY){
            final int PASS_FUEL_COST = 5;
            ship.useFuel(PASS_FUEL_COST);
            return new int[]{PASS_FUEL_COST};
        }
        throw new IllegalArgumentException("Fejl: Der er gået noget ukendt galt i logikken");
        //LOGGER
    }


}
