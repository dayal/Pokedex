import static spark.Spark.*;

public class RequestHandler {
	public static void main(String[] args) {
		get("/hello", (req, res) -> "Hello World");
	}
}
