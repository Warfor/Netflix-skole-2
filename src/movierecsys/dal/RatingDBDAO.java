/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author Philip
 */
public class RatingDBDAO implements IRatingRepository
{

    @Override
    public void createRating(Rating rating) throws FileNotFoundException, IOException
    {

   try {
       DatabaseConnection dc = new DatabaseConnection();
       
       
       try (Connection con = dc.getConnection())
       {
           
           String SQL = "INSERT INTO Rating VALUES (?, ?, ?)";
           PreparedStatement pstmt = con.prepareStatement(SQL);
           pstmt.setInt(1, rating.getMovie());
           pstmt.setInt(2,rating.getUser());
           pstmt.setInt(3,rating.getRating());
           pstmt.execute();
           pstmt.close();
           
           System.out.println("Following rating has been added to the database: "+rating.getRating());
           
       } catch (SQLException ex)
       {
           Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
       }
       
   } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void deleteRating(Rating ratingToDelete) throws IOException
    {
       int movie = ratingToDelete.getMovie();
       int user = ratingToDelete.getUser();
        try
        {
            DatabaseConnection dc = new DatabaseConnection();
            Connection con = dc.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Rating;");
             while (rs.next())
             {
                 if (rs.getInt("movie")==movie && rs.getInt("user")==user)
                 {
                     PreparedStatement pstmt = con.prepareStatement("DELETE FROM Rating WHERE movie=(?) AND [user]=(?)");
                     pstmt.setInt(1, movie);
                     pstmt.setInt(2, user);
                     pstmt.execute();
                     pstmt.close();
                     System.out.println("Rating found - and deleted!");
                     
                 }
 
             }
            
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Rating> getAllRatings() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Rating> getRatings(User user) throws IOException
    {
        ArrayList<Rating>ratings = new ArrayList<>();
     try {
         
         DatabaseConnection dc = new DatabaseConnection();
         
         
         try (Connection con = dc.getConnection())
         {
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery("Select * FROM Rating;");
             while (rs.next())
             {
                 if (rs.getInt("user")==user.getId())
                 {
                     System.out.println("Rating af bruger fundet");
                     int movie = rs.getInt("movie");
                     int userid = rs.getInt("user");
                     int rating = rs.getInt("rating");
                     Rating ratingToAdd = new Rating(movie, userid, rating);
                     ratings.add(ratingToAdd);
                 }
 
             }
             
         } catch (SQLException ex)
         {
             Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
         }
     }   catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ratings;
    }

    @Override
    public void updateRating(Rating rating) throws IOException
    {
       int movie = rating.getMovie();
       int user = rating.getUser();
       int newRating = rating.getRating();
        try
        {
            DatabaseConnection dc = new DatabaseConnection();
            Connection con = dc.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Rating;");
             while (rs.next())
             {
                 if (rs.getInt("movie")==movie && rs.getInt("user")==user)
                 {
                     PreparedStatement pstmt = con.prepareStatement("UPDATE Rating SET rating = (?) WHERE movie = (?) AND [user] = (?)");
                     pstmt.setInt(1, newRating);
                     pstmt.setInt(2, movie);
                     pstmt.setInt(3, user);
                     pstmt.execute();
                     pstmt.close();
                     System.out.println("Rating found - and updated!");
                     
                 }
 
             }
            
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
