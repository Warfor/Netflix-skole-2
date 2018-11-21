/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
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
    public static void main(String[] args) throws IOException, SQLServerException
    {
//MovieDBDAO dbdao = new MovieDBDAO();
//Movie django = new Movie(17771, 1967, "Django 4");
//dbdao.updateMovie(django);
        Rating test = new Rating(8,359063,-5);
        RatingDBDAO tester = new RatingDBDAO();
        tester.updateRating(test);
     
        
//        tester.deleteRating(test);
//        tester.createRating(new Rating(9999, 99999999, 5));

        
    }
    
   /**
    * Mitigate metoderne skriver ting ind i databasen.
    * @throws IOException 
    */
    
    public static void mitigateMovies() throws IOException{
           SQLServerDataSource ds = new SQLServerDataSource();
   ds.setServerName("10.176.111.31");
   ds.setDatabaseName("movierecsysPJ");
   ds.setUser("CS2018A_27");
   ds.setPassword("CS2018A_27");
    MovieDAO dao = new MovieDAO();
   List<Movie> movies = dao.getAllMovies();
   
        try (Connection con = ds.getConnection())
        {
            
            String SQL = "INSERT INTO Movie VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            
            for (Movie x : movies){
                
                
                int year = x.getYear();
                int id = x.getId();
                String titel = x.getTitle().replaceAll("'", "");
                pstmt.setInt(1, id);
                pstmt.setInt(2, year);
                pstmt.setString(3, titel);
                pstmt.executeUpdate();
                
                System.out.println(""+id+"   "+year+"     "+titel);
                            
                }
                
            pstmt.close();
               

            

                    
            
                    
                    
     
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public static void mitigateRatings() throws IOException 
    {
   SQLServerDataSource ds = new SQLServerDataSource();
   ds.setServerName("10.176.111.31");
   ds.setDatabaseName("movierecsysPJ");
   ds.setUser("CS2018A_27");
   ds.setPassword("CS2018A_27");
    RatingDAO rdao = new RatingDAO();
   List<Rating> ratings = rdao.getAllRatings();
   
        try (Connection con = ds.getConnection())
        {
            
            String SQL = "INSERT INTO Rating VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            int counter = 0;
            
            for (Rating x : ratings){
                
                int movie = x.getMovie();
                int user = x.getUser();
                int rating = x.getRating();
                
                pstmt.setInt(1, movie);
                pstmt.setInt(2, user);
                pstmt.setInt(3, rating);
                pstmt.addBatch();
                counter++;
                if(counter % 1000 == 0)
                {
                pstmt.executeBatch();
                    System.out.println("Added 1000 ratings");
                }
            }
            pstmt.executeBatch();

            pstmt.close();
               
      
                    
     
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
    }
     public static void mitigateUsers() throws IOException 
    {
   SQLServerDataSource ds = new SQLServerDataSource();
   ds.setServerName("10.176.111.31");
   ds.setDatabaseName("movierecsysPJ");
   ds.setUser("CS2018A_27");
   ds.setPassword("CS2018A_27");
    UserDAO uDAO = new UserDAO();
   List<User> users = uDAO.getAllUsers();
   
        try (Connection con = ds.getConnection())
        {
            
            String SQL = "INSERT INTO [User] VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            int counter = 0;
            
            for (User x : users){
                
                int id = x.getId();
                String name = x.getName();
                
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
               
                pstmt.addBatch();
                counter++;
                if(counter % 1000 == 0)
                {
                pstmt.executeBatch();
                    System.out.println("Added 1000 users");
                }
            }
            pstmt.executeBatch();

            pstmt.close();
               
      
                    
     
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(MovieDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }   
}
}
