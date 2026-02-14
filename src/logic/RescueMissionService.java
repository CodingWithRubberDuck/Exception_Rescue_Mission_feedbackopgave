package logic;

import dal.ExceptionLogger;
import exceptions.CriticalStatusException;
import exceptions.InvalidActionException;
import exceptions.InvalidTradeException;
import exceptions.NotUsableException;
import model.DangerEvent;
import model.Event;
import model.OpportunityEvent;
import model.SpaceShip;

import java.util.*;

//Responsible for doing logic-checks and throws exceptions if not
public class RescueMissionService {
    private final ExceptionLogger logger;
    private SpaceShip ship = null;
    private List<DangerEvent> dangerEvents;
    private List<OpportunityEvent> opportunityEvents;
    private List<String> eventLogs;

    private final Random random = new Random();

    public RescueMissionService(ExceptionLogger logger){
        this.logger = logger;
    }

    private void addEventLog(String message){
        eventLogs.add(message);
    }

    public List<String> getEventLogs(){
        return eventLogs;
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
        //EVENT LOG
        addEventLog("- Start: Kaptajn " + captainName + " på " + shipName);
    }

    public SpaceShip getShip(){
        return ship;
    }


    public void resetEventLists(){
        List<DangerEvent> dangerList = Arrays.asList(DangerEvent.values());
        dangerEvents = new ArrayList<>(dangerList);
        List<OpportunityEvent> opportunityList = Arrays.asList(OpportunityEvent.values());
        opportunityEvents = new ArrayList<>(opportunityList);
        if (eventLogs == null){
            eventLogs = new ArrayList<>();
        } else {
            eventLogs.clear();
        }
        eventLogs.add("Event LOG");
    }





    public void checkShipStatus(int round){
        if (ship.getFuel() < 10){
            //Event Log
            addEventLog("- Event " + round + ": Rumskibet er løbet tør for brændstof, og spillet er tabt");
            throw new CriticalStatusException("FEJL: Du har et kritsk lavt niveau af brændstof og bliver nødt til at stoppe din rejse. " +
                    "\nDu har tabt spillet");
        }
        if  (ship.getIntegrity() < 20){
            //Event Log
            addEventLog("- Event " + round + ": Rumskibet er for smadret til at fortsætte, og spillet er tabt");
            throw new CriticalStatusException("FEJL: Du har et kritisk lavt niveau af integritet og bliver nødt til at stoppe din rejse." +
                    "\nDu har tabt spillet");
        }
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


    public void useRepairKit(int round){
        if (ship.getIsRepairKitUsed()){
            throw new NotUsableException("Fejl: Du har allerede brugt dit repair kit");
        } else {
            final int repairKitIncrease = 20;
            ship.increaseIntegrity(repairKitIncrease);
            ship.setRepairKitUsed(true);
            //EVENT LOG
            addEventLog("- Event " + round + ": Repair kit brugt, integritet +" + repairKitIncrease);
        }
    }



    public int[] calculatingSpaceStorm(int choice, int round) {
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
                int actualDamage = damage-ship.getShieldProtectionAmount();
                ship.loseIntegrity(actualDamage);
                //EVENT LOG
                addEventLog("- Event " + round + ": Flyver igennem storm, skade +" + actualDamage);
                return new int[]{damage, ship.getShieldLevel(), shieldProctection, FLY_FUEL_COST};
            } else {
                ship.loseIntegrity(damage);
                //EVENT LOG
                addEventLog("- Event " + round + ": Flyver igennem storm, skade +" + damage + ", brændstof " + FLY_FUEL_COST);
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
                int actualDamage = damage-ship.getShieldProtectionAmount();
                ship.loseIntegrity(actualDamage);
                //EVENT LOG
                addEventLog("- Event " + round + ": Finder en omvej, skade +" + actualDamage + ", brændstof " + OTHER_FUEL_COST);
                return new int[]{damage, ship.getShieldLevel(), shieldProctection, OTHER_FUEL_COST};
            } else {
                ship.loseIntegrity(damage);
                //EVENT LOG
                addEventLog("- Event " + round + ": Finder en omvej, skade +" + damage + ", brændstof " + OTHER_FUEL_COST);
                return new int[]{damage, OTHER_FUEL_COST};
            }
        }
        throw new IllegalArgumentException("Fejl: Der er gået noget ukendt galt i logikken");
        //LOGGER
    }


    public int[] calculatingHostileShip(int choice, int round){
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
                int actualDamage = damage-ship.getShieldProtectionAmount();
                ship.loseIntegrity(actualDamage);
                //EVENT LOG
                addEventLog("- Event " + round + ": Flygter fra skibet, skade +" + actualDamage + ", brændstof " + fuelUsed);
                return new int[]{damage, ship.getShieldLevel(), shieldProctection, fuelUsed};
            } else {
                ship.loseIntegrity(damage);
                //EVENT LOG
                addEventLog("- Event " + round + ": Flygter fra skibet, skade +" + damage + ", brændstof " + fuelUsed);
                return new int[]{damage, fuelUsed};
            }
        }
        if (choice == SCARE_AWAY){
            final int GOOD_OUTCOME_EQUAL_OR_LOWER = 40;

            if (chanceChecker(GOOD_OUTCOME_EQUAL_OR_LOWER)){
                final int GOOD_MIN_DAMAGE = 0;
                final int GOOD_MAX_DAMAGE = 10;
                final int FUEL_USED = 5;
                ship.useFuel(FUEL_USED);
                int damage = random.nextInt(GOOD_MIN_DAMAGE, GOOD_MAX_DAMAGE + 1);
                int shieldProctection = ship.getShieldProtectionAmount();

                if (shieldProctection>0){
                    int actualDamage = damage-ship.getShieldProtectionAmount();
                    ship.loseIntegrity(actualDamage);
                    //EVENT LOG
                    addEventLog("- Event " + round + ": Succesfuldt skræmt det fjendtlige rumskib væk, skade +" + actualDamage + ", brændstof " + FUEL_USED);
                    return new int[]{damage, ship.getShieldLevel(), shieldProctection, FUEL_USED};
                } else {
                    ship.loseIntegrity(damage);
                    //EVENT LOG
                    addEventLog("- Event " + round + ": Succesfuldt skræmt det fjendtlige rumskib væk, skade +" + damage + ", brændstof " + FUEL_USED);
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
                    int actualDamage = damage-ship.getShieldProtectionAmount();
                    ship.loseIntegrity(actualDamage);
                    //EVENT LOG
                    addEventLog("- Event " + round + ": Fejlet i at skræmme fjendtligt rumskib væk, skade +" + actualDamage + ", brændstof " + FUEL_USED);
                    return new int[]{damage, ship.getShieldLevel(), shieldProctection, FUEL_USED};
                } else {
                    ship.loseIntegrity(damage);
                    //EVENT LOG
                    addEventLog("- Event " + round + ": Fejlet i at skræmme fjendtligt rumskib væk, skade +" + damage + ", brændstof " + FUEL_USED);
                    return new int[]{damage, FUEL_USED};
                }
            }
        }
        throw new IllegalArgumentException("Fejl: Der er gået noget ukendt galt i logikken");
        //LOGGER
    }

    public boolean calculatingMotorMalfunction(int choice, int chancesLeft, int round) {
        final int FINAL_CHANCE = 1;
        final int MOTOR_FAILURE_DAMAGE = 15;
        //Through storm
        final int NO_PARTS = 1;
        //Other way
        final int WITH_PARTS = 2;
        //Logic
        if (choice == NO_PARTS) {
            final int GOOD_OUTCOME_EQUAL_OR_LOWER = 50;

            if (chanceChecker(GOOD_OUTCOME_EQUAL_OR_LOWER)){
                //EVENT LOG
                addEventLog("- Event " + round + ": Genstart af motor lykkedes");
                return true;
            } else {
                if (chancesLeft <= FINAL_CHANCE){
                    //Event Log
                    addEventLog("- Event " + round + ": Rumskibets motor er permanent ødelagt, og spillet er tabt");
                    throw new CriticalStatusException("FEJL: Din motor er permanent ødelagt og du har tabt spillet");
                }
                ship.loseIntegrity(MOTOR_FAILURE_DAMAGE);
                //EVENT LOG
                addEventLog("- Event " + round + ": Genstart af motor fejlede (forsøg tilbage " + (chancesLeft-1) + ")");
                return false;
            }

        }
        if (choice == WITH_PARTS) {
            final int GOOD_OUTCOME_EQUAL_OR_LOWER = 80;
            final int SPARE_PARTS_COST = 2;

            if (ship.getSpareParts() - SPARE_PARTS_COST >= 0) {
                ship.spentSpareParts(SPARE_PARTS_COST);
                if (chanceChecker(GOOD_OUTCOME_EQUAL_OR_LOWER)) {
                    //EVENT LOG
                    addEventLog("- Event " + round + ": Genstart af motor med reservedele lykkedes");
                    return true;
                } else {
                    if (chancesLeft <= FINAL_CHANCE){
                        //Event Log
                        addEventLog("- Event " + round + ": Rumskibets motor er permanent ødelagt, og spillet er tabt");
                        throw new CriticalStatusException("FEJL: Din motor er permanent ødelagt og du har tabt spillet");
                    }
                    ship.loseIntegrity(MOTOR_FAILURE_DAMAGE);
                    //EVENT LOG
                    addEventLog("- Event " + round + ": Genstart af motor fejlede (forsøg tilbage " + (chancesLeft-1) + ")");
                    return false;
                }
            } else {
                throw new InvalidActionException("Fejl: Du har ikke nok reservedele til denne handling");
            }
        }
        throw new IllegalArgumentException("Fejl: Der er gået noget ukendt galt i logikken");
        //LOGGER
    }


    public int mysteriousTraderMoveOn(int round){
        final int PASSIVE_FUEL_COST = 5;
        ship.useFuel(PASSIVE_FUEL_COST);
        return PASSIVE_FUEL_COST;
    }




    public int[] mysteriousTraderTradeForFuel(String answer, int round){
        final int TRADE_RATIO = 5;
        try{
            int choice = Integer.parseInt(answer);
            if (choice > 0){
                if (choice <= ship.getSpareParts()){
                    int fuelGained = choice * TRADE_RATIO;
                    ship.increaseFuel(fuelGained);
                    ship.spentSpareParts(choice);
                    //EVENT LOG
                    addEventLog("- Event " + round + ": Handel " + choice + " reservedele -> +" + fuelGained + " brændstof");
                    return new int[]{choice, TRADE_RATIO*choice};
                } else {
                    throw new InvalidTradeException("Fejl: Du har ikke nok reservedele");
                    //LOGGER
                }
            } else {
                throw new IllegalArgumentException("Fejl: Indtast et positivt tal");
                //LOGGER
            }
        } catch (NumberFormatException nfe){
            throw new IllegalArgumentException("Fejl: Indtast et tal");
            //LOGGER
        }
    }

    public int mysteriousTraderTradeForShield(int round){
        final int UPGRADE_COST = 4;
        final int INCREASE_OF_SHIELD = 1;

        if (ship.getSpareParts() >= UPGRADE_COST){
            ship.spentSpareParts(UPGRADE_COST);
            ship.increaseShieldLevelBy(INCREASE_OF_SHIELD);
            //EVENT LOG
            addEventLog("- Event " + round + ": Handel " + UPGRADE_COST + " reservedele -> opgradet shield med " + INCREASE_OF_SHIELD + " level");
            return ship.getShieldLevel();
        } else {
            throw new InvalidTradeException("Fejl: Du har ikke nok reservedele");
        }
    }






    public int[] calculatingScavengeFacility(int choice, int round){
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

            //EVENT LOG
            addEventLog("- Event " + round + ": Forladt fabrik søg efter reservedele, +" + partsGained + " reservedele");
            return new int[]{partsGained, PARTS_FUEL_COST};
        }
        if (choice == SHIELD_LOOK) {
            final int SHIELD_FUEL_COST = 10;
            final int GOOD_OUTCOME_EQUAL_OR_LOWER = 50;
            final int INCREASE_SHIELD_BY = 1;
            ship.useFuel(SHIELD_FUEL_COST);


            if (chanceChecker(GOOD_OUTCOME_EQUAL_OR_LOWER)){
                ship.increaseShieldLevelBy(INCREASE_SHIELD_BY);
                //EVENT LOG
                addEventLog("- Event " + round + ": Forladt fabrik søg efter shield opgradering, fundet +" + INCREASE_SHIELD_BY + "shield level");
                return new int[]{ship.getShieldLevel(), ship.getShieldProtectionAmount(), SHIELD_FUEL_COST};
            } else {
                //EVENT LOG
                addEventLog("- Event " + round + ": Forladt fabrik søg efter shield opgradering, fundet +" + INCREASE_SHIELD_BY + "shield level");
                return new int[]{SHIELD_FUEL_COST};
            }
        }
        if (choice == PASS_BY){
            final int PASS_FUEL_COST = 5;
            ship.useFuel(PASS_FUEL_COST);
            //EVENT LOG
            addEventLog("- Event " + round + ": Forladt fabrik smutter bare forbi, -" + PASS_FUEL_COST + " brændstof");
            return new int[]{PASS_FUEL_COST};
        }
        throw new IllegalArgumentException("Fejl: Der er gået noget ukendt galt i logikken");
        //LOGGER
    }


    private boolean chanceChecker(int equalAndBelowIsSuccess){
        final int LOWEST_PERCENTAGE = 1;
        final int HIGHEST_PERCENTAGE = 100;
        // +1 in order for 100 to be part of it
        return equalAndBelowIsSuccess <= random.nextInt(LOWEST_PERCENTAGE, HIGHEST_PERCENTAGE+1);
    }




}
