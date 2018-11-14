/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;


/**
 *
 * @author pgn
 */
public class UserDAO
{
    
    /**
     * Gets a list of all known users.
     * @return List of users.
     */
    public List<User> getAllUsers() throws FileNotFoundException, IOException
    {
        List<User> allUsers = new ArrayList<>();
        String source = "data/users.txt";
        File file = new File(source);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) //Using a try with resources!
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (!line.isEmpty())
                {
                    try
                    {
                        User user = stringArrayToUser(line);
                        allUsers.add(user);
                        
                    } catch (Exception ex)
                    {
                        //Do nothing. Optimally we would log the error.
                    }
                }
            }
        }
        return allUsers;
    }
    
    /**
     * Gets a single User by its ID.
     * @param id The ID of the user.
     * @return The User with the ID.
     */
    public User getUser(int id) throws IOException
    {
    
     List<User> allUsers = getAllUsers();
     for (User x: allUsers)
     {
         if (x.getId()==id)
         {
             
             return x;
         }
     }
     return null;
    }
    
    
    /**
     * Updates a user so the persistence storage reflects the given User object.
     * @param user The updated user.
     */
    public void updateUser(User user) throws FileNotFoundException, IOException
    {
          PrintWriter writer = new PrintWriter("data/backupusers.txt", "UTF-8");
        List<User> allUsers = getAllUsers();
        File userlist = new File ("data/users.txt");
   
        
        for (User x : allUsers)
        {
            if (x.getId()!=user.getId()){
            
            writer.write(x.getId()+","+x.getName()+"\n");
            }
            else {
            writer.write(x.getId()+","+user.getName()+"\n");
            }
            
        }
    
        userlist.delete();
        writer.close();
        File backuplist = new File ("data/backupusers.txt");
        backuplist.renameTo(userlist);
    }



   private User stringArrayToUser(String line) throws IOException
    {
        String[] arrUser = line.split(",");
     
        int id = Integer.parseInt(arrUser[0]);
        String name = arrUser[1];

        User user1 = new User(id, name);
        
        return user1;
    }
    
}
