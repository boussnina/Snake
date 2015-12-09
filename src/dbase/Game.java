package dbase;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Game implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String player1;
    @Column
    private String commands1;
    @Column
    private String player2;
    @Column
    private String commands2;
    @Column
    private int highScore;
    @Column
    private String lastResult;

    public String getLastResult()
    {
        return lastResult;
    }

    public void setLastResult(String lastResult)
    {
        this.lastResult = lastResult;
    }

    public int getHighScore()
    {
        return highScore;
    }

    public void setHighScore(int highScore)
    {
        this.highScore = highScore;
    }

    public String getPlayer1()
    {
        return player1;
    }

    public void setPlayer1(String player1)
    {
        this.player1 = player1;
    }

    public String getCommands1()
    {
        return commands1;
    }

    public void setCommands1(String commands1)
    {
        this.commands1 = commands1;
    }

    public String getPlayer2()
    {
        return player2;
    }

    public void setPlayer2(String player2)
    {
        this.player2 = player2;
    }

    public String getCommands2()
    {
        return commands2;
    }

    public void setCommands2(String commands2)
    {
        this.commands2 = commands2;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getId()
    {
        return id;
    }
}
