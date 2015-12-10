package server.tui;

import java.util.Scanner;

import server.backend.Services;
import database.Game;
import database.User;

public class Tui
{
    public static void main(String[] strings)
    {
        new Tui().start();
    }
    
    private final Scanner scanner = new Scanner(System.in);
    
    public void start()
    {
        while(true)
        {
            System.out.print("Admin username? [Admin]: ");
            String username = scanner.nextLine();
            if(username.length() == 0)
            {
                username = "Admin";
            }
            
            System.out.print("Admin password? [password]: ");
            String password = scanner.nextLine();
            if(password.length() == 0)
            {
                password = "password";
            }

            User user = Services.INSTANCE.getUserByUsername(username);
            if(user != null&& user.isAdmin()&& password.equals(user.getPassword()))
            {
                break;
            }
            System.out.println("This is not a valid admin.");
        }
        
        questions:
        while(true)
        {
            int option = this.readOption("1. Create a user 2. Delete a user 3. Create a game 4. Quit ", 1, 4);
            switch(option)
            {
            case 1: this.createUser(); break;
            case 2: this.deleteUser(); break;
            case 3: this.createGame(); break;
            default: break questions;
            }
        }
    }
    
    public void createUser()
    {
        User user = new User();
        
        System.out.print("Username? ");
        user.setUsername(scanner.nextLine());
        
        System.out.print("Password? ");
        user.setPassword(scanner.nextLine());
        
        Services.INSTANCE.add(user);
        
        System.out.println("User \"" + user.getUsername() + "\" has been created successfully.");
    }
    
    public void deleteUser()
    {
        System.out.print("Username? ");
        String username = scanner.nextLine();
        
        User user = Services.INSTANCE.getUserByUsername(username);
        if(user == null)
        {
            System.out.println("No such user!");
        }else
        {
            Services.INSTANCE.deleteUser(user.getId());
            System.out.println("User " + user.getUsername() + " has been deleted.");
        }
    }
    
    public void createGame()
    {
        System.out.print("Name? ");
        String name = scanner.nextLine();
        
        Game game = new Game();
        game.setName(name);
        
        Services.INSTANCE.add(game);
        System.out.println("Game " + name + " has been added successfully.");
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
