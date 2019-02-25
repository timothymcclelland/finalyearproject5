package com.example.uuj.finalyearproject;

//java imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class downloadUrl {

    //used https://www.youtube.com/playlist?list=PLxefhmF0pcPlGUW8tyyOJ8-uF7Nk2VpSj tutorial set in the creation of this class

    //this class will retrieve the location data of the nearby places from the url

    public String readURl(String placeUrl) throws IOException
    {
        String Data = "";
        InputStream inputStream= null;
        HttpURLConnection httpURLConnection = null;

        try {
            //created url and opened connection
            URL url = new URL(placeUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //input stream and buffered reader used to read the data
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String line = "";

            //appending line to bufferedReader when reading data
            while((line = bufferedReader.readLine())!=null)
            {
                stringBuffer.append(line);
            }
            //converts stringBuffer to string
            Data = stringBuffer.toString();
            bufferedReader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //closes the inputStream and
            inputStream.close();
            httpURLConnection.disconnect();
        }

        //returns data from url
        return Data;
    }
}
