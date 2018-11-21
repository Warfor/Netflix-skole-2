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
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import movierecsys.be.Movie;

/**
 *
 * @author Asvør
 */
public class MovieDbDAO implements IMovieRepository
{

    @Override
    public Movie createMovie(int releaseYear, String title) throws IOException
    {
       int nextId = getNextAvailableMovieID();
      
       Movie createdMovie = null;
   try {
       DatabaseConnection dc = new DatabaseConnection();
       
       
       try (Connection con = dc.getConnection())
       {
           
           String SQL = "INSERT INTO Movie VALUES (?, ?, ?)";
           PreparedStatement pstmt = con.prepareStatement(SQL);
           pstmt.setInt(1, nextId);
           pstmt.setInt(2,releaseYear);
           pstmt.setString(3,title);
           pstmt.execute();
           pstmt.close();
           createdMovie= new Movie(nextId, releaseYear, title);
           System.out.println("Following movie has been added to the database: "+title);
           
       } catch (SQLException ex)
       {
           Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
       }
       
   } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
   return createdMovie;
    }
 
    @Override
    public void deleteMovie(Movie movie) throws FileNotFoundException, IOException
    {
     int id = movie.getId();
        try
        {
            DatabaseConnection dc = new DatabaseConnection();
            Connection con = dc.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Movie;");
             while (rs.next())
             {
                 if (rs.getInt("id")==id)
                 {
                     PreparedStatement pstmt = con.prepareStatement("DELETE FROM Movie WHERE id=(?)");
                     pstmt.setInt(1, id);
                     pstmt.execute();
                     pstmt.close();
                     System.out.println("Movie found. Deleted : " + movie.getTitle());
                 }
 
             }
            
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }

    public List<Movie> getAllMovies() throws IOException
    {
        ArrayList<Movie> allMovies = new ArrayList<>();
        try {
            DatabaseConnection dc = new DatabaseConnection();
            
            
            try (Connection con = dc.getConnection())
            {
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("Select * FROM Movie;");
                while (rs.next())
                {
                    int id = rs.getInt("id");
                    int year = rs.getInt("year");
                    String title = rs.getString("title");
                    allMovies.add(new Movie(id, year, title));
                    
                    
                }
                
                
            } catch (SQLServerException ex)
            {
                Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex)
            {
                Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
   return allMovies;
    }

    @Override
    public Movie getMovie(int id) throws IOException
    {
        Movie foundMovie = null;
            try 
            {         
                DatabaseConnection dc = new DatabaseConnection();
         
         
            try (Connection con = dc.getConnection())
            {
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery("Select * FROM Movie;");
             while (rs.next())
             {
                 if (rs.getInt("id")==id)
                 {
                     foundMovie=new Movie(id, rs.getInt("year"), rs.getString("title"));
                 }
 
             }
             
         } 
            catch (SQLException ex)
         {
             Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
         }
        }   
            catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(""+foundMovie.getTitle());
        return foundMovie;
    }
    
    
    @Override
    public void updateMovie(Movie movie) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
     int id = movie.getId();
          
        try
        {
            DatabaseConnection dc = new DatabaseConnection();
            Connection con = dc.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Movie;");
             while (rs.next())
             {
                 if (rs.getInt("id")==id)
                 {
                     PreparedStatement pstmt = con.prepareStatement(
                    "UPDATE Movie SET year = (?), title= (?) WHERE id = (?)");
                     pstmt.setInt(1, movie.getYear());
                     pstmt.setString(2, movie.getTitle());
                     pstmt.setInt(3, id);
                     pstmt.execute();
                     pstmt.close();
                  }
 
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
        
    
    }

    @Override
    public int getNextAvailableMovieID() throws IOException
    {
        List<Movie> allMovies = getAllMovies();
        allMovies.sort( Comparator.comparing( Movie::getId ) ); 
        int highId = allMovies.get(allMovies.size() - 1).getId();
        System.out.println("" + highId);
        return highId + 1;
        
    }
}
