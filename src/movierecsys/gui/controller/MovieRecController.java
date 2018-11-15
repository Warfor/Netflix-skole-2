/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;


import java.io.IOException;
import java.net.URL;
import static java.nio.file.Files.list;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import movierecsys.be.Movie;
import movierecsys.bll.MRSManager;
import movierecsys.bll.exception.MovieRecSysException;
import movierecsys.dal.MovieDAO;
import movierecsys.gui.model.MovieModel;

/**
 *
 * @author pgn
 */
public class MovieRecController implements Initializable
{


    /**
     * The TextField containing the query word.
     */
    @FXML
    private ListView<Movie> lstMovies;

    private MovieModel movieModel;
    @FXML
    private Button søgbtn;
    @FXML
    private TextField txtMovieSearch;
    @FXML
    private Button addMovie;
    @FXML
    private TextField name;
    @FXML
    private TextField year;
    
    MRSManager manager = new MRSManager();
    private ComboBox<String> comboBox;
    
    
    ObservableList<String> list = FXCollections.observableArrayList();
    @FXML
    private Button remove;


    public MovieRecController()
    {
        try
        {
            movieModel = new MovieModel();
        } catch (MovieRecSysException ex)
        {
            displayError(ex);
            System.exit(0);
        } 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lstMovies.setItems(movieModel.getMovies());

       

    }

    /**
     * Displays errormessages to the user.
     * @param ex The Exception
     */
    private void displayError(Exception ex)
    {
        //TODO Display error properly
        System.out.println(ex.getMessage());
        ex.printStackTrace();
    }

    @FXML
    private void FindMovie(ActionEvent event)
         
    {
      String input = txtMovieSearch.getText();
      
      ObservableList<Movie> movieList = FXCollections.observableArrayList(manager.searchMovies(input));
      lstMovies.setItems(movieList);
      
      
      
      
      
    }

    @FXML
    private void addMovie(ActionEvent event) throws IOException
    {
    String navn = name.getText();
    String aar = year.getText();
    int aartal = Integer.parseInt(aar);
    
    movieModel.createMovie(aartal, navn);
    
    name.clear();
    year.clear();
    
//    
    }

    @FXML
    private void removeButton(ActionEvent event)
    {
    Movie toDelete = lstMovies.getSelectionModel().getSelectedItem();
    movieModel.deleteMovie(toDelete);
    
    }


    
    


}
