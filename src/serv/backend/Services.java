package serv.backend;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import dbase.DbConfiguration;
import dbase.Game;
import dbase.SessionTool;
import dbase.User;

@SuppressWarnings("unchecked")
public class Services
{
    private static final AnnotationConfigApplicationContext context;
    public static final Services INSTANCE;
    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "password";
    static
    {
        try
        {
            context = new AnnotationConfigApplicationContext(
                    ServiceConfiguration.class, 
                    DbConfiguration.class);
            INSTANCE = context.getBean(Services.class);
        }catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        
        // Create the admin if not exist.
        User admin = INSTANCE.getUserByUsername(ADMIN_USERNAME);
        if(admin == null)
        {
            admin = new User();
            admin.setUsername(ADMIN_USERNAME);
            admin.setPassword(ADMIN_PASSWORD);
            admin.setAdmin(true);
            INSTANCE.add(admin);
        }
    }
    
    private final SessionTool sessionTool;
    
    Services(SessionTool sessionTool)
    {
        this.sessionTool = sessionTool;
    }

    /**
     * Authentications.
     * @param username
     * @param password
     * @return
     */
    @Transactional
    public String login(String username, String password)
    {
        Criteria criteria = sessionTool.getCurrentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("username", username));
        criteria.add(Restrictions.eq("password", password));
        
        List<User> users = criteria.list();
        if(users.isEmpty())
        {
            return null;
        }
        
        User user = users.get(0);
        String loginKey = UUID.randomUUID().toString();
        user.setLoginKey(loginKey);
        sessionTool.update(user);
        return loginKey;
    }
    
    public boolean logout(long id)
    {
        User user = this.getUser(id);
        if(user == null)
        {
            return false;
        }
        
        user.setLoginKey(null);
        sessionTool.update(user);
        return true;
    }

    /**
     * Users.
     * @param user
     * @return
     */
    public User add(User user)
    {
        sessionTool.save(user);
        return user;
    }
    
    public User getUser(long id)
    {
        return sessionTool.get(User.class, id);
    }
    
    @Transactional
    public User getUserByUsername(String firstName)
    {
        Criteria criteria = sessionTool.getCurrentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("username", firstName));
        
        List<User> users = criteria.list();
        return users.isEmpty() ? null : users.get(0);
    }
    
    @Transactional
    public List<User> getUsers()
    {
        Criteria criteria = sessionTool.getCurrentSession().createCriteria(User.class);
        List<User> users = criteria.list();
        return users;
    }
    
    public void deleteUser(long id)
    {
        User user = sessionTool.get(User.class, id);
        sessionTool.delete(user);
    }

    /**
     * Games.
     * @param game
     * @return
     */
    public Game add(Game game)
    {
        sessionTool.save(game);
        return game;
    }
    
    public boolean playGame(long id, String player, String commands)
    {
        Game game = this.getGame(id);
        String player1 = game.getPlayer1();
        String player2 = game.getCommands2();
        if(player.equals(player1))
        {
            game.setCommands1(commands);
        }else if(player.equals(player2))
        {
            game.setCommands2(commands);
        }else if(player1 == null)
        {
            game.setPlayer1(player);
            game.setCommands1(commands);
        }else if(player2 == null)
        {
            game.setPlayer2(player);
            game.setCommands2(commands);
        }else
        {
            return false;
        }
        sessionTool.update(game);
        return true;
    }
    
    public Game getGame(long id)
    {
        return sessionTool.get(Game.class, id);
    }
    
    @Transactional
    public List<Game> getGames()
    {
        Criteria criteria = sessionTool.getCurrentSession().createCriteria(Game.class);
        List<Game> games = criteria.list();
        return games;
    }
    
    @Transactional
    public Game getGameByName(String name)
    {
        Criteria criteria = sessionTool.getCurrentSession().createCriteria(Game.class);
        criteria.add(Restrictions.eq("name", name));
        
        List<Game> games = criteria.list();
        return games.isEmpty() ? null : games.get(0);
    }
    
    public void deleteGame(long id)
    {
        Game game = sessionTool.get(Game.class, id);
        sessionTool.delete(game);
    }
    
    @Transactional
    public List<Game> getHighScores(int number)
    {
        Criteria criteria = sessionTool.getCurrentSession().createCriteria(Game.class);
        criteria.addOrder(Order.desc("highScore"));
        criteria.setFetchSize(number);
        
        List<Game> games = criteria.list();
        return games;
    }
    
    @Transactional
    public Game playGame(long id)
    {
        Game game = this.getGame(id);
        
        Random random = new Random();
        Point size = new Point(10, 10);
        
        String player1 = game.getPlayer1();
        Point position1 = new Point(random.nextInt(10), random.nextInt(10));
        String commands1 = game.getCommands1();
        char[] chars1 = (commands1 == null ? "" : commands1).toCharArray();
        int score1 = 0;
        
        String player2 = game.getPlayer2();
        Point position2 = new Point(random.nextInt(10), random.nextInt(10));
        String commands2 = game.getCommands2();
        char[] chars2 = (commands2 == null ? "" : commands2).toCharArray();
        int score2 = 0;
        
        for(int step = 0;step < chars1.length|| step < chars2.length;step++)
        {
            if(step < chars1.length)
            {
                if(this.move(size, position1, position2, chars1[step], player1))
                {
                    score1++;
                }else
                {
                    score1 = -1;
                    break;
                }
            }
            if(step < chars2.length)
            {
                if(this.move(size, position2, position1, chars2[step], player2))
                {
                    score2++;
                }else
                {
                    score2 = -1;
                    break;
                }
            }
        }
        
        if(score1 > score2)
        {
            System.out.println("Winner is " + player1 + "!");
            game.setHighScore(score1);
            game.setLastResult(player1 + " won the game with " + score1 + ".");
        }else
        {
            System.out.println("Winner is " + player2 + "!");
            game.setHighScore(score2);
            game.setLastResult(player2 + " won the game with " + score2 + ".");
        }
        sessionTool.update(game);
        
        return game;
    }
    
    public boolean move(Point size, Point position, Point opponent, char command, String playerName)
    {
        switch(command)
        {
        case 'n':
        case 'N': position.y--; break;
        case 's':
        case 'S': position.y++; break;
        case 'w':
        case 'W': position.x--; break;
        case 'e':
        case 'E': position.x++; break;
        }
        
        if(position.x < 0|| position.x >= size.x|| position.y < 0|| position.y >= size.y)
        {
            System.out.println(playerName + " ran into a wall.");
            return false;
        }else if(position.x == opponent.x&& position.y == opponent.y)
        {
            System.out.println(playerName + " ran into the opponent.");
            return false;
        }else
        {
            System.out.println(playerName + " moved to (" + position.x + ", " + position.y + ").");
            return true;
        }
    }
}
