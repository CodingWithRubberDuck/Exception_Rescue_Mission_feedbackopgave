package model;

public class SpaceShip {
    private int fuel;
    private int integrity;
    private int spareParts;
    private int shieldLevel;
    private boolean repairKitUsed;
    private String captainName;
    private String shipName;

    public SpaceShip(int fuel, int integrity, int spareParts, int shieldLevel, boolean repairKitUsed, String captainName, String shipName){
        this.fuel = fuel;
        this.integrity = integrity;
        this.spareParts = spareParts;
        this.shieldLevel = shieldLevel;
        this.repairKitUsed = repairKitUsed;
        this.captainName = captainName;
        this.shipName = shipName;

    }

    //getters
    public int getFuel() {
        return fuel;
    }
    public int getIntegrity() {
        return integrity;
    }
    public int getSpareParts() {
        return spareParts;
    }
    public int getShieldLevel() {
        return shieldLevel;
    }
    public boolean getIsRepairKitUsed() {
        return repairKitUsed;
    }
    public String getCaptainName() {
        return captainName;
    }
    public String getShipName() {
        return shipName;
    }

    //Setters
    public void setFuel(int fuel) {
        this.fuel = fuel;
    }
    public void setIntegrity(int integrity){
        this.integrity = integrity;
    }
    public void setSpareParts(int spareParts){
        this.spareParts = spareParts;
    }
    public void setShieldLevel(int shieldLevel){
        this.shieldLevel = shieldLevel;
    }
    public void setRepairKitUsed(boolean repairKitUsed){
        this.repairKitUsed = repairKitUsed;
    }

    public void setCaptainName(String captainName){
        this.captainName = captainName;
    }
    public void setShipName(String shipName){
        this.shipName = shipName;
    }



    public int getShieldProtectionAmount(){
        final int PROCTECTION_PER_SHIELD_LEVEL = 5;
        return shieldLevel * PROCTECTION_PER_SHIELD_LEVEL;
    }


    public void useFuel(int amount){
        this.fuel -= amount;
    }

    public void increaseFuel(int amount){
        this.fuel += amount;
    }

    public void loseIntegrity(int amount){
        if (amount>0) {
            this.integrity -= amount;
        }
    }

    public void spentSpareParts(int amount){
        if (amount>0) {
            this.spareParts -= amount;
        }
    }

    public void increaseShieldLevelBy(int amount){
        this.shieldLevel += amount;
    }

    public void increaseIntegrity(int amount){
        this.integrity += amount;
    }

}
