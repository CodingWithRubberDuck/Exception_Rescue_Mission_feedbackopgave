public class Main {
    public static void main(String[] args) {
        //Dependency Injection
        UIDisplay ui = new UIDisplay(new MissionController(new RescueMissionService(new ExceptionLogger())));
        ui.launch();
    }
}
