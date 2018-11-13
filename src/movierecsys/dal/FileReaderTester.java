/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.IOException;
import java.util.List;
import movierecsys.be.Movie;
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
       Movie screamers = new Movie (16,2001,"Screamers");
       MovieDAO movieDao = new MovieDAO();
       UserDAO user = new UserDAO();
//       movieDao.updateMovie(screamers);
       user.getAllUsers();
       User test = user.getUser(79);
        System.out.println(""+test.getName());
      
        
        
        
        
    }
}
