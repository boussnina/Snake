package request.protocols;

import java.io.Serializable;

public class GetGameRequest implements Serializable
{
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
