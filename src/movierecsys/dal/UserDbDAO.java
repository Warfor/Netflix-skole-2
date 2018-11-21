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
import movierecsys.be.User;

/**
 *
 * @author Asvør
 */
public class UserDbDAO implements IUserRepository
{
   
    
    public List<User> getAllUsers() throws IOException
   {
        ArrayList<User> allUsers = new ArrayList<>();
        try 
        {
            DatabaseConnection dc = new DatabaseConnection();
            
            
            try (Connection con = dc.getConnection())
            {
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("Select * FROM [user];");
                while (rs.next())
                {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    allUsers.add(new User(id, name));
                    
                    
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
        
   return allUsers;
    }
    

    @Override
    public User getUser(int id) throws IOException
    {
        User foundUser = null;
            try 
            {         
                DatabaseConnection dc = new DatabaseConnection();
         
         
            try (Connection con = dc.getConnection())
            {
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery("Select * FROM user;");
             while (rs.next())
             {
                 if (rs.getInt("id")==id)
                 {
                     foundUser = new User (id, rs.getString("name"));
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
        System.out.println(""+foundUser.getName());
        return foundUser;
    }

    @Override
    public void updateUser(User user) throws FileNotFoundException, IOException
    {
        int id = user.getId();
          
        try
        {
            DatabaseConnection dc = new DatabaseConnection();
            Connection con = dc.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM user;");
             while (rs.next())
             {
                 if (rs.getInt("id")==id)
                 {
                     
                     PreparedStatement pstmt = con.prepareStatement(
                    "UPDATE Movie SET name = (?) WHERE id = (?)");
                     pstmt.setString(1, user.getName());
                     pstmt.setInt(2, id);
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
    
    
}
