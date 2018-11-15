/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.IOException;
import java.util.List;
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
    public static void main(String[] args) throws IOException
    {
//       Movie screamers = new Movie (16,2001,"Screamers");
//       MovieDAO movieDao = new MovieDAO();
       RatingDAO rating = new RatingDAO();
//       UserDAO user = new UserDAO();
////       movieDao.updateMovie(screamers);
//       user.getAllUsers();
//       User test = user.getUser(2503676);
        List<Rating> allRatings = rating.getAllRatings();
        System.out.println(""+allRatings.size());
//        User user1 = new User(2502098, "Vesna Mellouli");



        Rating rating1 = new Rating(8,1333,1);
        rating.deleteRating(rating1);
        
       
        
      
//        rating.deleteRating(testrating);
//        List<Rating> allRatings2 = rating.getAllRatings();
//        System.out.println(""+allRatings2.size());
      
        
       
       

//       2502098,Vesna Mellouli
        
        
        
        
    }
}
