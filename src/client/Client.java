package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import request.protocols.CreateGameRequest;
import request.protocols.DeleteGameRequest;
import request.protocols.GetGameRequest;
import request.protocols.GetHighScoresRequest;
import request.protocols.HelloRequest;
import request.protocols.LoginRequest;
import request.protocols.LogoutRequest;
import request.protocols.PlayGameRequest;
import request.protocols.SetCommandsRequest;
import dbase.Game;

@SuppressWarnings("unchecked")
public class Client
{
	    public static void main(String[] strings)
	    {
	        new Client().start();
	    }
	    
	    private final Scanner scanner = new Scanner(System.in);
	    private ObjectInputStream inputStream;
	    private ObjectOutputStream outputStream;
	    private String username;

}
