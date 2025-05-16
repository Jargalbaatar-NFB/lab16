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

    public App() throws IOException {
        // front-end path - энэ нь Docker-д "COPY" хийгдсэн байх ёстой
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
                this.game = this.game.play(
                    Integer.parseInt(params.get("x")),
                    Integer.parseInt(params.get("y"))
                );
            }
            GameState gameplay = GameState.forGame(this.game);
            return newFixedLengthResponse(gameplay.toString());
        } else {
            return super.serve(session);
        }
    }
}
