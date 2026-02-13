import java.util.Scanner;

//Responsible for all input and output to the console
public class UIDisplay {
    private final MissionController controller;


    public UIDisplay(MissionController controller){
        this.controller = controller;
    }

    public void launch(){
        Scanner input = new Scanner(System.in);
        System.out.print(controller.askToPlayText());
        boolean programRunning = true;
        boolean answer;
        while (programRunning) {
            try {
                answer = controller.checkPlayAnswer(input.nextLine());
                if(answer){
                    startGame(input);
                } else if (!answer) {
                    programRunning = false;
                    System.out.println(controller.shutdownText());
                }
            } catch (IllegalArgumentException iae) {
                System.out.println(iae.getMessage());
                System.out.print(controller.newResponseText());
            }
        }
    }


    private void startGame(Scanner input){
        System.out.println(controller.startText());
        controller.resetEventLists();
        typeNames(input);
        gameLoop(input);
    }

    private void typeNames(Scanner input){
        boolean answerCaptain = false;
        String captainName = "";
        System.out.print(controller.captainNameText());
        while (!answerCaptain){
            try {
                captainName = input.nextLine();
                answerCaptain = controller.captainNameAnswer(captainName);
            } catch (IllegalArgumentException iae){
                System.out.println(iae.getMessage());
                System.out.print(controller.newResponseText());
            }
        }
        boolean answerShip = false;
        String shipName = "";
        System.out.print(controller.shipNameText());
        while (!answerShip){
            try {
                shipName = input.nextLine();
                answerShip = controller.shipNameAnswer(shipName);
            } catch (IllegalArgumentException iae){
                System.out.println(iae.getMessage());
                System.out.print(controller.newResponseText());
            }
        }
        controller.createTheShip(captainName, shipName);

    }

    private void gameLoop(Scanner input){
        final int MAX_ROUNDS = 5;
        showStatus();
        for (int round = 1; round <= MAX_ROUNDS; round++){
            divideEventType(controller.recieveRandomEvent(round), input, round);
            showStatus();
        }
        //Finish stuff
    }


    private void showStatus(){
        System.out.println(controller.formatStatus());
    }

    //Currently there isn't enough different events for each to really warrant splitting them,
    //but considering you might want to add more in the future, I have done so
    private void divideEventType(Event event, Scanner input, int round){
        if (event instanceof DangerEvent){
           manageDangerEvents((DangerEvent) event, input, round);
        } else if (event instanceof OpportunityEvent) {
            manageOpportunityEvents((OpportunityEvent) event, input, round);
        }
    }

    private void manageDangerEvents(DangerEvent event, Scanner input, int round){
        switch (event){
            case SPACE_STORM -> spaceStormEvent(input, round);
            case HOSTILE_SHIP -> hostileShipEvent(input, round);
            case MOTOR_MALFUNCTION -> motorMalfunctionEvent(input, round);
            //Here in case of problems, you shouldn't be able to get this though
            case null, default -> peacefulEvent(round);

        }
    }

    private void manageOpportunityEvents(OpportunityEvent event, Scanner input, int round){
        switch (event){
            case MYSTERIOUS_TRADER -> mysteriousTraderEvent(input, round);
            case SCAVENGE_ABANDONED_FACILITY -> scavengeAbandonedFacilityEvent(input, round);
            //Here in case of problems, you shouldn't be able to get this though
            case null, default -> peacefulEvent(round);
        }
    }

    private void peacefulEvent(int round){
        System.out.println(controller.failsafeEventText(round));
    }



    private void spaceStormEvent(Scanner input, int round){
        final int MAX_INPUT_CHOICE = 4;
        boolean choiceMade = false;
        System.out.println(controller.spaceStormIntroText(round));
        while (!choiceMade) {
            int choice = generalEventInputChecker(input, MAX_INPUT_CHOICE);
            switch (choice) {
                case 1:
                    try {
                        int[] flyChanges = controller.spaceStormCalculations(choice);
                        choiceMade = true;
                        System.out.println(controller.spaceStormFlyText());
                        System.out.println(controller.spaceStormChanges(flyChanges));
                    } catch (IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }
                    break;

                case 2:
                    try {
                        int[] otherChanges = controller.spaceStormCalculations(choice);
                        choiceMade = true;
                        System.out.println(controller.spaceStormOtherText());
                        System.out.println(controller.spaceStormChanges(otherChanges));
                    } catch (IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }
                    break;

                case 3:
                    System.out.println(controller.formatStatus());
                    break;
                case 4:
                    try {
                        controller.tryUseRepairKit();
                        System.out.println(controller.useRepairKitSuccess());
                    } catch (NotUsableException nue){
                        System.out.println(nue.getMessage());
                        //LOGGER
                    }
                    break;
                default:
                    System.out.println(controller.unknownSituationText());
                    //LOGGER
                    break;
            }
        }
    }

    private void hostileShipEvent(Scanner input, int round){
        final int MAX_INPUT_CHOICE = 4;
        boolean choiceMade = false;
        System.out.println(controller.hostileShipIntroText(round));
        while (!choiceMade) {
            int choice = generalEventInputChecker(input, MAX_INPUT_CHOICE);
            switch (choice) {
                case 1:
                    try {
                        int[] flightChanges = controller.hostileShipCalculations(choice);
                        choiceMade = true;
                        System.out.println(controller.hostileShipFlightText());
                        System.out.println(controller.hostileShipChanges(flightChanges));
                    } catch (IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }
                    break;

                case 2:
                    try {
                        int[] scareChanges = controller.hostileShipCalculations(choice);
                        choiceMade = true;
                        System.out.println(controller.hostileShipScareText(scareChanges[1]));
                        System.out.println(controller.hostileShipChanges(scareChanges));
                    } catch (IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }
                    break;
                case 3:
                    System.out.println(controller.formatStatus());
                    break;
                case 4:
                    try {
                        controller.tryUseRepairKit();
                        System.out.println(controller.useRepairKitSuccess());
                    } catch (NotUsableException nue){
                        System.out.println(nue.getMessage());
                        //LOGGER
                    }
                    break;
                default:
                    System.out.println(controller.unknownSituationText());
                    //LOGGER
                    break;
            }
        }
    }

    private void motorMalfunctionEvent(Scanner input, int round){
        final int MAX_INPUT_CHOICE = 4;
        boolean problemFixed = false;
        int chancesLeft = 2;
        System.out.println(controller.motorMalfunctionIntroText(round));
        while (!problemFixed) {
            int choice = generalEventInputChecker(input, MAX_INPUT_CHOICE);
            switch (choice) {
                case 1:
                    try {
                        problemFixed = controller.motorMalfunctionCalculations(choice, chancesLeft);
                        if (!problemFixed){
                            controller.motorMalfunctionFailDamage();
                            System.out.println(controller.motorMalfunctiontionFailText());
                            chancesLeft--;
                        } else {
                            System.out.println(controller.motorMalfunctionSuccessText());
                        }
                    } catch (IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }
                    break;

                case 2:
                    try {
                        problemFixed = controller.motorMalfunctionCalculations(choice, chancesLeft);
                        if (!problemFixed) {
                            controller.motorMalfunctionFailDamage();
                            System.out.println(controller.motorMalfunctiontionFailText());
                            chancesLeft--;
                        } else {
                            System.out.println(controller.motorMalfunctionSuccessText());
                        }
                    } catch (InvalidActionException | IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }
                    break;

                case 3:
                    System.out.println(controller.formatStatus());
                    break;
                case 4:
                    try {
                        controller.tryUseRepairKit();
                        System.out.println(controller.useRepairKitSuccess());
                    } catch (NotUsableException nue){
                        System.out.println(nue.getMessage());
                        //LOGGER
                    }
                    break;
                default:
                    System.out.println(controller.unknownSituationText());
                    //LOGGER
                    break;
            }
        }
    }


    private void mysteriousTraderEvent(Scanner input, int round){
        final int MAX_INPUT_CHOICE = 5;
        boolean doneTrading = false;
        System.out.println(controller.mysteriousTraderIntroText(round));
        while (!doneTrading) {
            int choice = generalEventInputChecker(input, MAX_INPUT_CHOICE);
            switch (choice) {
                case 1:

                    break;

                case 2:

                    break;

                case 3:
                    final int passiveFuelCost = 5;
                    controller.mysteriousTraderFuelUsage(passiveFuelCost);
                    System.out.println(controller.mysteriousTraderMoveOnText(passiveFuelCost));
                    doneTrading = true;
                    break;

                case 4:
                    System.out.println(controller.formatStatus());
                    break;
                case 5:
                    try {
                        controller.tryUseRepairKit();
                        System.out.println(controller.useRepairKitSuccess());
                    } catch (NotUsableException nue){
                        System.out.println(nue.getMessage());
                        //LOGGER
                    }
                    break;

                default:
                    System.out.println(controller.unknownSituationText());
                    //LOGGER
                    break;
            }
        }
    }

    private void scavengeAbandonedFacilityEvent(Scanner input, int round){
        final int MAX_INPUT_CHOICE = 5;
        boolean choiceMade = false;
        System.out.println(controller.scavengeFacilityIntroText(round));
        while (!choiceMade) {
            int choice = generalEventInputChecker(input, MAX_INPUT_CHOICE);
            switch (choice) {
                case 1:
                    try {
                        int[] sparePartsChanges = controller.scavengeFacilityCalculations(choice);
                        choiceMade = true;
                        System.out.println(controller.scavengeFacilitySparePartsText());
                        System.out.println(controller.scavengeFacilityPartsChangesText(sparePartsChanges));
                    } catch (IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }
                    break;

                case 2:
                    try {
                        int[] shieldChanges = controller.scavengeFacilityCalculations(choice);
                        choiceMade = true;
                        System.out.println(controller.scavengeFacilityShieldText());
                        System.out.println(controller.scavengeFacilityShieldChangesText(shieldChanges));
                    } catch (IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }

                    break;

                case 3:
                    try {
                        int[] passChanges = controller.scavengeFacilityCalculations(choice);
                        choiceMade = true;
                        System.out.println(controller.scavengeFacilityPassText());
                        System.out.println(controller.scavengeFacilityPassChangesText(passChanges));
                    } catch (IllegalArgumentException iae){
                        System.out.println(iae.getMessage());
                        //LOGGER
                    }
                    break;

                case 4:
                    System.out.println(controller.formatStatus());
                    break;
                case 5:
                    try {
                        controller.tryUseRepairKit();
                        System.out.println(controller.useRepairKitSuccess());
                    } catch (NotUsableException nue){
                        System.out.println(nue.getMessage());
                    }
                    break;

                default:
                    System.out.println(controller.unknownSituationText());
                    //LOGGER
                    break;
            }
        }
    }


    private int generalEventInputChecker(Scanner input, int maxInputChoice){
        int choice;
        while(true){
            try {
                System.out.println(controller.newResponseText());
                choice = controller.verifyGeneralEventChoice(input.nextLine(), maxInputChoice);
                return choice;
            } catch (IllegalArgumentException iae){
                System.out.println(iae.getMessage());
            }
        }
    }







}
