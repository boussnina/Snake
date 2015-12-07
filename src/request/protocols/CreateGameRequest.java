package request.protocols;

import java.io.Serializable;

import database.Game;

public class CreateGameRequest implements Serializable
{
    private Game game;

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }
}
