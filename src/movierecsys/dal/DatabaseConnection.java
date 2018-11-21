/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import movierecsys.be.Movie;

/**
 *
 * @author Philip
 */
public class DatabaseConnection
{
private static final String SERVERNAME = "INDSAETIP";
private static final String DATABASENAME = "INDSAETDATABASENAVN";
private static String USER;
private static String PASS;
private static Connection CON;

public DatabaseConnection() throws SQLServerException, IOException
{
   setUserAndPass();
   SQLServerDataSource ds = new SQLServerDataSource();
   ds.setServerName(DatabaseConnection.SERVERNAME);
   ds.setDatabaseName(DatabaseConnection.DATABASENAME);
   ds.setUser(DatabaseConnection.USER);
   ds.setPassword(DatabaseConnection.PASS);
   this.CON = ds.getConnection();
}

public Connection getConnection() throws SQLServerException{
 return CON;  
    
}

private void setUserAndPass() throws FileNotFoundException, IOException{
    String source = "data/login.txt";
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
                        this.USER=line;
                        this.PASS=line;
                    
                        
                    } catch (Exception ex)
                    {
                        //Do nothing. Optimally we would log the error.
                    }
                }
            }
        }

}

}