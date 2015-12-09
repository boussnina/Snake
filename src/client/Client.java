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
	    
	    public void start(){
	 
	    	System.out.print("Enter host: ");
	        String host = scanner.nextLine().trim();
	        if(host.length() == 0)
	        {
	            host = "localhost";
	        }
	        
	        System.out.print("Enter server portnumber : ");
	        String portString = scanner.nextLine().trim();
	        if(portString.length() == 0)
	        {
	            portString = "2345";
	        }
	        int port = Integer.parseInt(portString);
	        
	        try(Socket socket = new Socket(host, port))
	        {
	        	outputStream = new ObjectOutputStream(socket.getOutputStream());
	            HelloRequest request = new HelloRequest();
	            outputStream.writeObject(request);
	            inputStream = new ObjectInputStream(socket.getInputStream());
	            inputStream.readObject();
	            System.out.println("Connection established");
	        	
	        }
	        catch (Exception e){
	        	e.printStackTrace();
	        }
	    }
	    private Object sendRequest(Object request)
	    {
	        try
	        {
	            outputStream.writeObject(request);
	            Object response = inputStream.readObject();
	            return response;
	        }catch(Exception e)
	        {
	            ;
	        }
	        return null;
	    }
	    private int readOption(String question, int min, int max)
	    {
	        while(true)
	        {
	            System.out.print(question);
	            try
	            {
	                int option = Integer.parseInt(scanner.nextLine().trim());
	                if(option >= min&& option <= max)
	                {
	                    return option;
	                }
	            }catch(Exception e)
	            {
	                ;
	            }
	            System.out.println("Invalid input!");
	        }
	    }

}
