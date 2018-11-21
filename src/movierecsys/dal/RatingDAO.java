 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import static com.oracle.jrockit.jfr.ContentType.Bytes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

/**
 *
 * @author pgn
 */
public class RatingDAO implements IRatingRepository

{

    private static final String RATING_SOURCE = "data/user_ratings";

    private static final int RECORD_SIZE = Integer.BYTES * 3;

    /**
     *
     * Persists the given rating.
     *
     *
     *
     * @param rating the rating to persist.
     *
     */
    @Override
    public void createRating(Rating rating) throws FileNotFoundException, IOException

    {

        int movie = rating.getMovie();
        int user = rating.getUser();
        int score = rating.getRating();
        
        
        RandomAccessFile raf = new RandomAccessFile("data/user_ratings", "rw");
        raf.seek(raf.length());
        raf.writeInt(movie);
        raf.writeInt(user);
        raf.writeInt(score);
        
        raf.close();
        
    }

    /**
     *
     * Updates the rating to reflect the given object. Assumes that the source
     *
     * file is in order by movie ID, then User ID..
     *
     *
     *
     * @param rating The updated rating to persist.
     *
     * @throws java.io.IOException
     *
     */
    @Override
    public void updateRating(Rating rating) throws IOException

    {

        try (RandomAccessFile raf = new RandomAccessFile(RATING_SOURCE, "rw"))

        {

            long totalRatings = raf.length();

            long low = 0;

            long high = ((totalRatings - 1) / RECORD_SIZE) * RECORD_SIZE;

            while (high >= low) //Binary search of movie ID

            {

                long pos = (((high + low) / 2) / RECORD_SIZE) * RECORD_SIZE;

                raf.seek(pos);

                int movId = raf.readInt();

                int userId = raf.readInt();

                if (rating.getMovie() < movId) //We did not find the movie.

                {

                    high = pos - RECORD_SIZE; //We half our problem size to the upper half.

                } else if (rating.getMovie() > movId) //We did not find the movie.

                {

                    low = pos + RECORD_SIZE; //We half our problem size (Just the lower half)

                } else //We found a movie match, not to search for the user:

                {

                    if (rating.getUser() < userId) //Again we half our problem size

                    {

                        high = pos - RECORD_SIZE;

                    } else if (rating.getUser() > userId) //Another half sized problem

                    {

                        low = pos + RECORD_SIZE;

                    } else //Last option, we found the right row:

                    {

                        raf.write(rating.getRating()); //Remember the to reads at line 60,61. They positioned the filepointer just at the ratings part of the current record.

                        return; //We return from the method. We are done here. The try with resources will close the connection to the file.

                    }

                }

            }

        }

        throw new IllegalArgumentException("Rating not found in file, can't update!"); //If we reach this point we have been searching for a non-present rating.

    }

    /**
     *
     * Removes the given rating.
     *
     *
     *
     * @param rating
     *
     */
    @Override
    public void deleteRating(Rating ratingToDelete) throws IOException

    {
       List<Rating> allRatings = getAllRatings();
        
        File binratings = new File("data/binratings");
        RandomAccessFile raf = new RandomAccessFile("data/binratings", "rw");
//        File movielist = new File ("data/movie_titles.txt");
   
        
        for (Rating x : allRatings)
        {
            if (x.getRating()==ratingToDelete.getRating() && x.getMovie()==ratingToDelete.getMovie() && x.getUser()==ratingToDelete.getUser())
                
            {
            // Do nothing
            }
            else {
            int mov = x.getMovie();
           
            int id = x.getUser();
            
            int rat = x.getRating();
            
                        
            String rating = ""+mov+","+id+","+rat+"\n";
            raf.writeInt(mov);
            raf.writeInt(id);
            raf.writeInt(rat);
            }
        }
    
//        movielist.delete();
            raf.close();
            File originalListe = new File("data/user_ratings");
            originalListe.delete();
        
            binratings.renameTo(originalListe);
//        File backuplist = new File ("data/backupmovies.txt");
//        backuplist.renameTo(movielist);

        }
        
 
    


    /**
     *
     * Gets all ratings from all users.
     *
     *
     *
     * @return List of all ratings.
     *
     */
    @Override
    public List<Rating> getAllRatings() throws IOException

    {
        List<Rating> allRatings = new ArrayList<>();

        byte[] all = Files.readAllBytes(new File(RATING_SOURCE).toPath()); //I get all records as binary data!

        for (int i = 0; i < all.length; i += RECORD_SIZE)

        {
            int movieId = ByteBuffer.wrap(all, i, Integer.BYTES).order(ByteOrder.BIG_ENDIAN).getInt();

            int userId = ByteBuffer.wrap(all, i + Integer.BYTES, Integer.BYTES).order(ByteOrder.BIG_ENDIAN).getInt();

            int rating = ByteBuffer.wrap(all, i + Integer.BYTES * 2, Integer.BYTES).order(ByteOrder.BIG_ENDIAN).getInt();

            Rating r = new Rating(movieId, userId, rating);
            
            allRatings.add(r);

        }

        Collections.sort(allRatings, (Rating o1, Rating o2) ->

        {

            int movieCompare = Integer.compare(o1.getMovie(), o2.getMovie());

            return movieCompare == 0 ? Integer.compare(o1.getUser(), o2.getUser()) : movieCompare;

        });

        return allRatings;
    }

    /**
     *
     * Get all ratings from a specific user.
     *
     *
     *
     * @param user The user
     *
     * @return The list of ratings.
     *
     */
    @Override
    public List<Rating> getRatings(User user) throws IOException

    {

       List<Rating> allRatings = getAllRatings();
       ArrayList<Rating> ratingsByUser= new ArrayList<Rating>();
       
       for (Rating x : allRatings){
            if(x.getUser()==user.getId()){
               ratingsByUser.add(x);
               }
               
       }
       return ratingsByUser;
    }

    private Rating getRatingFromLine(String line) throws NumberFormatException

    {

        String[] cols = line.split(",");

        int movId = Integer.parseInt(cols[0]);

        int userId = Integer.parseInt(cols[1]);

        int rating = Integer.parseInt(cols[2]);

        return new Rating(movId, userId, rating);

    }
    
  

}
