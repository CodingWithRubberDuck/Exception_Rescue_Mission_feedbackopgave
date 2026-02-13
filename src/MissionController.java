//Doesn't display anything but controls what gets displayed,
// and governs simple logic or formatting
public class MissionController {
    private final RescueMissionService service;

    public MissionController(RescueMissionService service){
        this.service = service;
    }

    public String shutdownText(){
        return "\nHåber du har nydt spillet :D";
    }

    public String askToPlayText(){
        return "\nVelkommen til Rumeventyret - Exception Rescue Mission"
                + "\nVil du starte et nyt spil?"
                + "\n1) Ja"
                + "\n2) Nej"
                + "\n> ";
    }

    public boolean checkPlayAnswer(String answer){
        return service.handlePlayAnswer(answer.trim());
    }


    public String startText(){
        return "===============================================" +
                "\n     RUMEVENTYR - EXCEPTION RESCUE MISSION  " +
                "\n===============================================";
    }

    public String newResponseText(){
        return "> ";
    }


    public String captainNameText(){
        return "\nVelkommen kaptajn" +
                "\nIndtast dit navn:" +
                "\n> ";
    }


    public boolean captainNameAnswer(String answer){
        return service.handleCaptainName(answer);
    }


    public String shipNameText(){
        return "\nIndtast navnet på dit rumskib:" +
                "\n> ";
    }

    public boolean shipNameAnswer(String answer){
        return service.handleShipName(answer);
    }

    public void createTheShip(String captainName, String shipName){
        service.createShip(captainName, shipName);
    }

    public void resetEventLists(){
        service.resetEventLists();
    }


    public String formatStatus(){
        SpaceShip ship = service.getShip();

        String repairKitInfo;
        if (ship.getIsRepairKitUsed()){
            repairKitInfo = "brugt";
        } else {
            repairKitInfo = "ikke brugt";
        }

        return  "\nKaptajnen " + ship.getCaptainName() + " tjekker status af " + ship.getShipName() +
                "\n------------------------------------------" +
                "\nSTATUS" +
                "\nBrændstof    : " + ship.getFuel() +
                "\nIntegritet   : " + ship.getIntegrity() +
                "\nReservedele  : " + ship.getSpareParts() +
                "\nSkjold       : " + ship.getShieldLevel() +
                "\nRepair kit   : " + repairKitInfo +
                "\n------------------------------------------";
    }

    public void tryUseRepairKit(){
        service.useRepairKit();
    }

    public String useRepairKitSuccess(){
        return  "\nRepair kit brugt" +
                "\nIntegritet + 20";
    }


    public Event recieveRandomEvent(int round){
        return service.getRandomEvent(round);
    }


    public String failsafeEventText(int roundNumber){
        return  "\nEVENT " + roundNumber + " - FREDFULD DAG" +
                "\nDer er ingen problemer i dag, og altid går fint" +
                "\nDerfor har ingen handlinger brug for at blive taget";
    }

    public int verifyGeneralEventChoice(String answer, int maxInputChoice){
        return service.handleGeneralEventChoice(answer, maxInputChoice);
    }

    public String unknownSituationText(){
        return "Ukendt fejl: Man burde ikke kunne få denne fejl";
    }



    //SpaceStormEvent
    public String spaceStormIntroText(int roundNumber){
        return  "\nEVENT " + roundNumber + " - RUMSTORM" +
                "\nEn voldsom rumstorm nærmer sig" +
                "\n" +
                "\nVælg handling:" +
                "\n1) Flyv igennem stormen (Høj risiko)" +
                "\n2) Tag en omvej (ekstra -10 brændstof, men lavere skade)" +
                "\n3) Se Status" +
                "\n4) Brug repair kit";
    }


    public int[] spaceStormCalculations(int choice){
        return service.calculatingSpaceStorm(choice);
    }

    public String spaceStormFlyText(){
        return  "\nFlyver ind i stormen..." +
                "\nStormskade beregnes...";
    }

    public String spaceStormOtherText(){
        return  "\nBruger mere brændstof på at finde en omvej..." +
                "\nUndgår det værste af stormen..." +
                "\nStormskade beregnes...";
    }

    public String spaceStormChanges(int[] changes){
        final int WITH_SHIELD_LEVEL_LENGTH = 4;

        if (changes.length == WITH_SHIELD_LEVEL_LENGTH) {
            int damage = changes[0];
            int shieldLevel = changes[1];
            int shieldProtection = changes[2];
            int fuelCost = changes[3];

            //Makes sure you can't get weird values for integrity loss
            int integrityLoss = damage-shieldProtection;
            if (integrityLoss < 0){
                integrityLoss = 0;
            }
            return  "\nStormskade: " + damage +
                    "\nShield level " + shieldLevel + " reducerer skade med " + shieldProtection +
                    "\nIntegritet -" + integrityLoss +
                    "\nBrændstof -" + fuelCost;
        } else {
            int damage = changes[0];
            int fuelCost = changes[1];

            return  "\nStormskade: " + damage +
                    "\nShield level 0 reducerer skade med 0" +
                    "\nIntegritet -" + damage +
                    "\nBrændstof -" + fuelCost;
        }
    }


    //HostileShipEvent

    public String hostileShipIntroText(int roundNumber){
        return  "\nEVENT " + roundNumber + " - FJENDTLIGT RUMSKIB" +
                "\nEt ukendt meget stort rumskib med parate våben nærmer sig" +
                "\n" +
                "\nVælg handling:" +
                "\n1) Flyv væk hurtigt for at afværge en konfrontation (Højt brændstof forbrug, men lav skade)" +
                "\n2) Klargør egne våben for at afskrække dem (Stor risiko for meget skade, men også høj sandsynlighed for lav skade)" +
                "\n3) Se Status" +
                "\n4) Brug repair kit";
    }

    public int[] hostileShipCalculations(int choice){
        return service.calculatingHostileShip(choice);
    }

    public String hostileShipFlightText(){
        return  "\nFlyver væk så hurtigt, som du kan..." +
                "\nDu undgår det meste skade" +
                "\nBrændstofforbrug og skade beregnes...";
    }

    public String hostileShipScareText(int damage){
        if (damage < 20){
            return  "\nVåbene på rumskibet gøres klar..." +
                    "\nog det virker til at have lykkedes..." +
                    "\ndu undgik det værst tænkelige scenarie..." +
                    "\nBrændstofforbrug og skade beregnes...";
        } else {
            return  "\nVåbene på rumskibet gøres klar..." +
                    "\n..." +
                    "\nmen det fremmede rumskib virker ikke til at lade sig afskrække" +
                    "\ndette leder til en længere kamp før I begge giver op og flyver hver til sit" +
                    "\ndette var klart ikke scenariet du håbede på..." +
                    "\nBrændstofforbrug og skade beregnes...";
        }
    }

    public String hostileShipChanges(int[] changes){
        final int WITH_SHIELD_LEVEL_LENGTH = 4;

        if (changes.length == WITH_SHIELD_LEVEL_LENGTH) {
            int damage = changes[0];
            int shieldLevel = changes[1];
            int shieldProtection = changes[2];
            int fuelCost = changes[3];

            //Makes sure you can't get weird values for integrity loss
            int integrityLoss = damage-shieldProtection;
            if (integrityLoss < 0){
                integrityLoss = 0;
            }
            return  "\nKampskade: " + damage +
                    "\nShield level " + shieldLevel + " reducerer skade med " + shieldProtection +
                    "\nIntegritet -" + integrityLoss +
                    "\nBrændstof -" + fuelCost;
        } else {
            int damage = changes[0];
            int fuelCost = changes[1];

            return  "\nKampskade: " + damage +
                    "\nShield level 0 reducerer skade med 0" +
                    "\nIntegritet -" + damage +
                    "\nBrændstof -" + fuelCost;
        }
    }

    //MotorMalfunctionEvent

    public String motorMalfunctionIntroText(int roundNumber){
        return  "\nEVENT " + roundNumber + " - MOTOR-PROBLEMER" +
                "\nMotoren fejler pludseligt, du anskuer at du har i alt 2 forsøg til at fikse problemet" +
                "\nDu udleder ydermere at et mislykket forsøg vil gøre skade på rumskibet" +
                "\n" +
                "\nVælg handling:" +
                "\n1) Prøv at fikse uden brug af reservedele (middelmådig chance for success)" +
                "\n2) Brug reservedele til at fikse motoren (høj chance for succes, men bruger 2 reservedele)" +
                "\n3) Se Status" +
                "\n4) Brug repair kit";
    }

    public boolean motorMalfunctionCalculations(int choice, int chancesLeft){
        return service.calculatingMotorMalfunction(choice, chancesLeft);
    }

    public String motorMalfunctionSuccessText(){
        return  "\nForsøger at genstarte..." +
                "\nGenstart lykkedes" +
                "\nMotoren kører igen";
    }

    public String motorMalfunctiontionFailText(){
        return  "\nForsøger at genstarte..." +
                "\nGenstart mislykkedes" +
                "\nIntegritet -15";
    }

    public void motorMalfunctionFailDamage(){
        service.motorMalfunctionTakingDamage();
    }


    //Mysterious trader

    public String mysteriousTraderIntroText(int roundNumber){
        return  "\nEVENT " + roundNumber + " - MYSTISK KØBMAND" +
                "\nEt rumvæsen tilbyder handel og opgraderinger" +
                "\n" +
                "\nVælg handling:" +
                "\n1) Byt reservedele for brændstof (ratio 1/1)" +
                "\n2) Opgrader shield level med +1 (koster 4 reservedele)" +
                "\n3) Tag videre (tryk denne mulighed, når du er færdig)" +
                "\n4) Se Status" +
                "\n5) Brug repair kit";
    }

    public void mysteriousTraderFuelUsage(int fuelCost){
        service.useMysteriousTraderFuelUsage(fuelCost);
    }


    public String mysteriousTraderMoveOnText(int fuelCost){
        return  "\nDu tager videre..." +
                "\nDit brændstofforbrug beregnes..." +
                "\n" +
                "\nBrændstof -" + fuelCost;
    }




    //ScavangeAbandonedFacility

    public String scavengeFacilityIntroText(int roundNumber){
        return  "\nEVENT " + roundNumber + " - FORLADT FABRIK" +
                "\nI løbet af din rejsen støder du på en forladt fabrik" +
                "\nDu har muligheden for at lede efter resources" +
                "\n" +
                "\nVælg handling:" +
                "\n1) Led efter reservedele (Dette vil kræve mere brændstof end normalt)" +
                "\n2) Led efter en shield opgradering (Middelmådig chance for succes, men kræver mere brændstof)" +
                "\n3) Passer uden yderligere handlinger (Kræver kun det normale antal brændstof)" +
                "\n4) Se Status" +
                "\n5) Brug repair kit";
    }

    public int[] scavengeFacilityCalculations(int choice){
        return service.calculatingScavengeFacility(choice);
    }

    public String scavengeFacilitySparePartsText(){
        return  "\nDu leder efter reservedele..." +
                "\nFundne reservedele og brugt brændstof beregnes...";
    }

    public String scavengeFacilityPartsChangesText(int[] changes){
        int partsGained = changes[0];
        int fuelCost = changes[1];

        return  "\nReservedele +" + partsGained +
                "\nBrændstof -" + fuelCost;
    }

    public String scavengeFacilityShieldText(){
        return  "\nDu leder efter en shield opgradering..." +
                "\nResultatet af din søgen...";
    }


    public String scavengeFacilityShieldChangesText(int[] changes){
        int SUCCESS_LENGTH = 3;
        if (changes.length ==  SUCCESS_LENGTH){
            int shieldLevel = changes[0];
            int shieldProtection = changes[1];
            int fuelCost = changes[2];
            return  "\nEn opgradering blev fundet" +
                    "\n" +
                    "\nShield level " + shieldLevel + " reducerer skade med " + shieldProtection +
                    "\nBrændstof -" + fuelCost;
        } else {
            int fuelCost = changes[0];
            return  "\nIngen opgradering blev fundet" +
                    "\n" +
                    "\nBrændstof -" + fuelCost;
        }
    }

    public String scavengeFacilityPassText(){
        return  "\nDu flyver bare videre..." +
                "\nDit brændstofforbrug beregnes...";
    }

    public String scavengeFacilityPassChangesText(int[] changes){
        int fuelCost = changes[0];

        return  "\nBrændstof -" + fuelCost;
    }









}




