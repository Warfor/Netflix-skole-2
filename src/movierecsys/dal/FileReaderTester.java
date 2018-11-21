/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author pgn
 */
public class FileReaderTester
{

    /**
     * Example method. This is the code I used to create the users.txt files.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
  
      //mitigateMovies();
      //mitigateUsers();
      //mitigateRatings();
        RatingDbDAO rating = new RatingDbDAO();
        
        System.out.println("" + rating.getAllRatings().size());
      
             
        
    }
    
   /*public static void mitigateMovies() throws IOException 
   {
       SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("10.176.111.31");
        ds.setDatabaseName("mrs4");
        ds.setUser("CS2018A_4");
        ds.setPassword("CS2018A_4");
        
        MovieDAO mvDao = new MovieDAO();
        List<Movie> movies = mvDao.getAllMovies();
        
        try (Connection con = ds.getConnection())
        {
            Statement statement = con.createStatement();
            
            for (Movie movie: movies)
            {
                String sql = "INSERT INTO movie (id, year, title) VALUES("
                        + movie.getId() + ","
                        + movie.getYear() + ",'"
                        + movie.getTitle().replace("'", "") + "');";
                    
                    System.out.println(sql);
                    int i = statement.executeUpdate(sql);
                    System.out.println("" + i);
                
            }
      
            
        } 
         catch (SQLException ex)
        {
            ex.printStackTrace();
        }*/
        
    
    public static void mitigateUsers() throws IOException
    {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("10.176.111.31");
        ds.setDatabaseName("mrs4");
        ds.setUser("CS2018A_4");
        ds.setPassword("CS2018A_4");

        List<User> users = new UserDbDAO().getAllUsers();

        try (Connection con = ds.getConnection())
        {
            Statement statement = con.createStatement();
            int counter = 0;
            for (User user : users)
            {
                String sql = "INSERT INTO [user] (id,name) VALUES("
                        + user.getId() + ",'"
                        + user.getName() + "');";
                statement.addBatch(sql);
                counter++;
                if (counter % 1000 == 0)
                {
                    statement.executeBatch();
                    System.out.println("Added 1000 users.");
                }
            }
            if (counter % 1000 != 0)
            {
                statement.executeBatch();
                System.out.println("Added final batch of users.");
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Untested batch insert. We'll see how it goes tomorrow.
     *
     * @throws IOException
     */
    public static void mitigateRatings() throws IOException
    {
        List<Rating> allRatings = new RatingDbDAO().getAllRatings();
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("10.176.111.31");
        ds.setDatabaseName("mrs4");
        ds.setUser("CS2018A_4");
        ds.setPassword("CS2018A_4");
        try (Connection con = ds.getConnection())
        {
            Statement st = con.createStatement();
            int counter = 0;
            for (Rating rating : allRatings)
            {
                String sql = "INSERT INTO Rating (movieId, userId, rating) VALUES ("
                        + rating.getMovie() + ","
                        + rating.getUser() + ","
                        + rating.getRating() + ");";
                        
                st.addBatch(sql);
                counter++;
                if (counter % 1000 == 0)
                {
                    st.executeBatch();
                    System.out.println("Added   " + counter + " ratings.");
                }
            }
            if (counter % 1000 != 0)
            {
                st.executeBatch();
                System.out.println("Added final batch of ratings.");
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

}

