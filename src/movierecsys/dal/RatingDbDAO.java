/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
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
 * @author Asvør
 */
public class RatingDbDAO implements IRatingRepository
{

    @Override
    public void createRating(Rating rating) throws FileNotFoundException, IOException
    {
       
   
    }

    @Override
    public void deleteRating(Rating ratingToDelete) throws IOException
    {
        try 
        {
            DatabaseConnection dc = new DatabaseConnection();
            
            
            try (Connection con = dc.getConnection())
            {
                
                
            } catch (SQLException ex)
            {
                Logger.getLogger(RatingDbDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLServerException ex)
        {
            Logger.getLogger(RatingDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Rating> getAllRatings() throws IOException
    {
        ArrayList<Rating> allRatings = new ArrayList<>();
        try {
            DatabaseConnection dc = new DatabaseConnection();
            
            
            try (Connection con = dc.getConnection())
            {
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("Select * FROM rating;");
                while (rs.next())
                {
                    int movieId = rs.getInt("movieId");
                    int userId = rs.getInt("userId");
                    int rating = rs.getInt("rating");
                    allRatings.add(new Rating(movieId, userId, rating));
                }
                
                
            } 
            catch (SQLServerException ex)
            {
                Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
   return allRatings;
    }


    @Override
    public List<Rating> getRatings(User user) throws IOException
    {
        return null;
        
    }

    @Override
    public void updateRating(Rating rating) throws IOException
    {
     
    
    }
}
