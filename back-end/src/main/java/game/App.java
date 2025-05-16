package game;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.SimpleWebServer;

public class App extends SimpleWebServer {

    private Game game;

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    /**
     * Start the server at port 8080.
     */
    public App() throws IOException {
       super(null, 8080, new File("front-end/build"), true);


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
                int x = Integer.parseInt(params.get("x"));
                int y = Integer.parseInt(params.get("y"));
                this.game = this.game.play(x, y);
            }

            GameState gameplay = GameState.forGame(this.game);
            return newFixedLengthResponse(gameplay.toString());
        } else {
            return super.serve(session);
        }
    }
}
