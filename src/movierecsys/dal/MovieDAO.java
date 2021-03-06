/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import movierecsys.be.Movie;

/**
 *
 * @author pgn
 */
public class MovieDAO implements IMovieRepository
{

    private static final String MOVIE_SOURCE = "data/movie_titles.txt";

    /**
     * Gets a list of all movies in the persistence storage.
     *
     * @return List of movies.
     * @throws java.io.IOException 
     */
    @Override
    public List<Movie> getAllMovies() throws IOException
    {
        List<Movie> allMovies = new ArrayList<>();
        String source = "data/movie_titles.txt";
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
                        Movie mov = stringArrayToMovie(line);
                        allMovies.add(mov);
                        
                    } catch (Exception ex)
                    {
                        //Do nothing. Optimally we would log the error.
                    }
                }
            }
        }
        
        return allMovies;
    }

    /**
     * Reads a movie from the comma separated line.
     *
     * @param line the comma separated line.
     * @return The representing Movie object.
     * @throws NumberFormatException
     */
    private Movie stringArrayToMovie(String line)
    {
        String[] arrMovie = line.split(",");

        int id = Integer.parseInt(arrMovie[0]);
        int year = Integer.parseInt(arrMovie[1]);
        String title = arrMovie[2];

        Movie mov = new Movie(id, year, title);
        return mov;
    }

    /**
     * Creates a movie in the persistence storage.
     *
     * @param releaseYear The release year of the movie
     * @param title The title of the movie
     * @return The object representation of the movie added to the persistence
     * storage.
     */
    @Override
    public Movie createMovie(int releaseYear, String title) throws IOException
    {
        Path path = new File(MOVIE_SOURCE).toPath();
        int id = -1;
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.SYNC, StandardOpenOption.APPEND, StandardOpenOption.WRITE))
        {
            id = getNextAvailableMovieID();
            bw.write("\n"+id + "," + releaseYear + "," + title);
        }
        return new Movie(id, releaseYear, title);
    }

    /**
     * Examines all stored movies and returns the next available highest ID.
     *
     * @return
     * @throws IOException
     */


    public int getNextAvailableMovieID() throws IOException
    {
        List<Movie> allMovies = getAllMovies();
        int highId = allMovies.get(allMovies.size() - 1).getId();
        return highId + 1;
    }

    /**
     * Deletes a movie from the persistence storage.
     *
     * @param movie The movie to delete.
     * @throws java.io.FileNotFoundException
     */
    @Override
    public void deleteMovie(Movie movie) throws FileNotFoundException, IOException
    {
  
        PrintWriter writer = new PrintWriter("data/backupmovies.txt", "UTF-8");
        List<Movie> allMovies = getAllMovies();
        File movielist = new File ("data/movie_titles.txt");
   
        
        for (Movie x : allMovies)
        {
            if (x.getId()!=movie.getId()){
            
            writer.write(x.getId()+","+x.getYear()+","+x.getTitle()+"\n");
            }
            
        }
    
        movielist.delete();
        writer.close();
        File backuplist = new File ("data/backupmovies.txt");
        backuplist.renameTo(movielist);
      
    }

    /**
     * Updates the movie in the persistence storage to reflect the values in the
     * given Movie object.
     *
     * @param movie The updated movie.
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    @Override
    public void updateMovie(Movie movie) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        PrintWriter writer = new PrintWriter("data/backupmovies.txt", "UTF-8");
        List<Movie> allMovies = getAllMovies();
        File movielist = new File ("data/movie_titles.txt");
   
        
        for (Movie x : allMovies)
        {
            if (x.getId()!=movie.getId()){
            
            writer.write(x.getId()+","+x.getYear()+","+x.getTitle()+"\n");
            }
            else {
            writer.write(x.getId()+","+movie.getYear()+","+movie.getTitle()+"\n");
            }
            
        }
    
        movielist.delete();
        writer.close();
        File backuplist = new File ("data/backupmovies.txt");
        backuplist.renameTo(movielist);
    }

    /**
     * Gets a the movie with the given ID.
     *
     * @param id ID of the movie.
     * @return A Movie object.
     * @throws java.io.IOException
     */
    @Override
    public Movie getMovie(int id) throws IOException
    {
     List<Movie> allMovies = getAllMovies();
     for (Movie x: allMovies)
     {
         if (x.getId()==id)
         {
             
             return x;
         }
     }
     return null;
    }

}
