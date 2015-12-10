package protocol;

import java.io.Serializable;

public class GetHighScoresRequest implements Serializable
{
    private int number;

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }
}
