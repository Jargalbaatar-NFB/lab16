package game;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.SimpleWebServer;

public class App extends SimpleWebServer {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private Game game;
    @SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

    /**
     * Start the server at :8080 port.
     * 
     * @throws IOException
     */
    public App() throws IOException {
        super(null, 8080, new File("../front-end/build"), true);

        this.game = new Game();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning on port 8080!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        if (uri.startsWith("/api")) {

            Map<String, String> params = session.getParms();
            if (uri.equals("/api/newgame")) {
                this.game = new Game();
            } else if (uri.equals("/api/play")) {
                // e.g., /play?x=1&y=1
                this.game = this.game.play(Integer.parseInt(params.get("x")), Integer.parseInt(params.get("y")));
            }
            // Extract the view-specific data from the game and apply it to the template.
            GameState gameplay = GameState.forGame(this.game);
            return newFixedLengthResponse(gameplay.toString());
        } else {
            return super.serve(session);
        }
    }
}
