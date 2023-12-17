import spark.Spark;

public class Server {
  public static void main(String[] args) {
//    Spark.port(9000);
    Spark.port(getHerokuAssignedPort());
    Router.configure();
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
  }
}