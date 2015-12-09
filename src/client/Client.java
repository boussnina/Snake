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
	        	
	            while(!this.login())
	            {
	                ;
	            }
	            
	            // Execute operaions.
	            options:
	            while(true)
	            {
	                try
	                {
	                    int option = this.readOption("1. Create a game 2. Set commands 3. Get high scores 4. Delete a game 5. Start a game 6. Show a game 7. Logout : ", 1, 7);
	                    switch(option)
	                    {
	                    case 1: this.createGame(); break;
	                    case 2: this.setCommands(); break;
	                    case 3: this.getHighScores(); break;
	                    case 4: this.deleteGame(); break;
	                    case 5: this.startGame(); break;
	                    case 6: this.showGame(); break;
	                    case 7: this.logout(); break options;
	                    }
	                }catch(Exception e)
	                {
	                    e.printStackTrace();
	                }
	            }
	        }catch(Exception e)
	        {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    private boolean login()
	    {
	        System.out.print("Username? [Admin]: ");
	        username = scanner.nextLine();
	        if(username.length() == 0)
	        {
	            username = "Admin";
	        }

	        System.out.print("Password? [password]: ");
	        String password = scanner.nextLine();
	        if(password.length() == 0)
	        {
	            password = "password";
	        }
	        
	        LoginRequest request = new LoginRequest();
	        request.setUsername(username);
	        request.setPassword(password);
	        String loginKey = (String) this.sendRequest(request);
	        boolean successful = loginKey != null;
	        System.out.println(successful ? "Logged in successfully." : "Failed to login.");
	        
	        return successful;
	    }
	    private void logout()
	    {
	        LogoutRequest request = new LogoutRequest();
	        request.setUsername(username);
	        this.sendRequest(request);
	    }
	    
	    private void createGame()
	    {
	        System.out.print("Enter game name: ");
	        String name = scanner.nextLine();
	        
	        CreateGameRequest request = new CreateGameRequest();
	        Game game = new Game();
	        request.setGame(game);
	        game.setName(name);
	        
	        Game resultGame = (Game) this.sendRequest(request);
	        System.out.println(resultGame == null ? "Could not create game: \"" + name + "\"." :"Game \"" + name + "\" was succesfully created.");
	    }
	    
	    private void setCommands()
	    {
	        System.out.print("Enter game name: ");
	        String name = scanner.nextLine();
	        
	        System.out.print("Enter commands ");
	        String commands = scanner.nextLine();
	        
	        SetCommandsRequest request = new SetCommandsRequest();
	        request.setUsername(username);
	        request.setGameName(name);
	        request.setCommands(commands);
	        
	        boolean successful = (boolean) this.sendRequest(request);
	        System.out.println(successful ? "Commands set successfully." : "Failed to set commands.");
	    }
	    
	    private void getHighScores()
	    {
	        GetHighScoresRequest request = new GetHighScoresRequest();
	        request.setNumber(10);
	        
	        List<Game> games = (List<Game>) this.sendRequest(request);
	        System.out.println("High scores:");
	        int index = 1;
	        for(Game game : games)
	        {
	            System.out.println(index + "\t\t" + game.getName() + "\t\t" + game.getHighScore());
	            index++;
	        }
	    }
	    
	    private void deleteGame()
	    {
	        System.out.print("Enter game name: ");
	        String name = scanner.nextLine();
	        
	        DeleteGameRequest request = new DeleteGameRequest();
	        request.setName(name);
	        
	        boolean successful = (boolean) this.sendRequest(request);
	        System.out.println(successful ? "Game \"" + name + "\" deleted successfully." : "Failed to delete game \"" + name + "\".");
	    }
	    
	    private void startGame()
	    {
	        System.out.print("Enter game name: ");
	        String name = scanner.nextLine();
	        
	        PlayGameRequest request = new PlayGameRequest();
	        request.setName(name);
	        
	        Game game = (Game) this.sendRequest(request);
	        if(game == null)
	        {
	            System.out.println("The entered game does not exist.");
	            return;
	        }
	        
	        System.out.println(game.getPlayer1() + " V.S. " + game.getPlayer2());
	        System.out.println(game.getLastResult());
	    }
	    
	    private void showGame()
	    {
	        System.out.print("Enter game name: ");
	        String name = scanner.nextLine();
	        
	        GetGameRequest request = new GetGameRequest();
	        request.setName(name);
	        
	        Game game = (Game) this.sendRequest(request);
	        if(game == null)
	        {
	            System.out.println("The entered game does not exist.");
	            return;
	        }
	        
	        System.out.println(game.getName());
	        
	        String player1 = game.getPlayer1();
	        if(player1 != null)
	        {
	            System.out.println(player1 + ": " + game.getCommands1());
	        }
	        
	        String player2 = game.getPlayer2();
	        if(player2 != null)
	        {
	            System.out.println(player2 + ": " + game.getCommands2());
	        }
	        
	        String result = game.getLastResult();
	        if(result != null)
	        {
	            System.out.println(result);
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
