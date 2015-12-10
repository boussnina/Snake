package protocol;

import java.io.Serializable;

public class SetCommandsRequest implements Serializable
{
    private String username;
    private String gameName;
    private String commands;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public String getCommands()
    {
        return commands;
    }

    public void setCommands(String commands)
    {
        this.commands = commands;
    }

}
