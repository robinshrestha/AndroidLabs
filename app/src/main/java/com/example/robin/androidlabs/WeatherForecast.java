package com.example.robin.androidlabs;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends Activity {


    protected static final String ACTIVITY_NAME = "Weather Forecast";
    private ProgressBar progressBar;
    private ImageView weatherImage;
    private TextView currentTempText;
    private TextView minTempText;
    private TextView maxTempText;
    private TextView windSpeedText;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_forecast);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);


        weatherImage = (ImageView) findViewById(R.id.weatherImage);

        currentTempText = (TextView) findViewById(R.id.currentTemp);

        minTempText = (TextView) findViewById(R.id.minTemp);

        maxTempText = (TextView) findViewById(R.id.maxTemp);

        windSpeedText = (TextView) findViewById(R.id.windSpeed);


        ForecastQuery forecast = new ForecastQuery();

        forecast.execute();


    }


    //inner class

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String minTemp;
        private String maxTemp;
        private String windSpeed;
        private String currentTemp;
        private String iconName;
        private Bitmap bitmap;


        @Override

        protected String doInBackground(String... args) {

            try {

                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000); //in milliseconds

                conn.setConnectTimeout(15000); //in milliseconds

                conn.setRequestMethod("GET");

                conn.setDoInput(true);

                //test

                Log.d(ACTIVITY_NAME, "connecting with url..");

                //Starts the query

                conn.connect();

                //test

               InputStream stream = conn.getInputStream();

                Log.d(ACTIVITY_NAME, "reading stream");

                //test

                Log.d(ACTIVITY_NAME, "stream is: " + stream);


                XmlPullParser parser = Xml.newPullParser();

                parser.setInput(stream, null);


                while (parser.next() != XmlPullParser.END_DOCUMENT) {

                    if (parser.getEventType() != XmlPullParser.START_TAG) {

                        continue;

                    }


                    if (parser.getName().equals("temperature")) {

                        currentTemp = parser.getAttributeValue(null, "value");

                        publishProgress(25,50,75);

                        android.os.SystemClock.sleep(500);//to slow down progress bar loading display

                        minTemp = parser.getAttributeValue(null, "min");

                        publishProgress(25,50,75);

                        android.os.SystemClock.sleep(500);

                        maxTemp = parser.getAttributeValue(null, "max");

                        publishProgress(25,50,75);

                        android.os.SystemClock.sleep(500);

                        windSpeed = parser.getAttributeValue(null, "wind");

                        publishProgress(25,50,75);

                        android.os.SystemClock.sleep(500);


                    }


                    if (parser.getName().equals("speed")) {

                        windSpeed = parser.getAttributeValue(null, "value");
                        publishProgress(25,50,75);
                        android.os.SystemClock.sleep(500);

                    }

                    if (parser.getName().equals("weather")) {

                        iconName = parser.getAttributeValue(null, "icon");

                    }

                }

                conn.disconnect();


                //connecting or searching through file to get weather image

                String iconFile = iconName + ".png";

                if (fileExistence(iconFile)) {

                    FileInputStream fis = null;

                    try {

                        fis = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    }

                    bitmap = BitmapFactory.decodeStream(fis);
                    Log.i(ACTIVITY_NAME, "Weather image exists, read from file");

                } else {

                    URL imageUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");

                    conn = (HttpURLConnection) imageUrl.openConnection();

                    conn.connect();


                    stream = conn.getInputStream();

                    bitmap = BitmapFactory.decodeStream(stream);

                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

                    outputStream.flush();

                    outputStream.close();

                    Log.i(ACTIVITY_NAME, "Weather image does not exist, adding new image from URL");

                }

                Log.i(ACTIVITY_NAME, "file name = " + iconFile);

                publishProgress(100);

            } catch (FileNotFoundException fne) {

                Log.e(ACTIVITY_NAME, fne.getMessage());

            } catch (XmlPullParserException parserException) {

                Log.e(ACTIVITY_NAME, parserException.getMessage());

            } catch (IOException e) {

                Log.e(ACTIVITY_NAME, e.getMessage());

            }

            return null;

        }

//onProgressUpdate


        @Override

        protected void onProgressUpdate(Integer... value) {

            progressBar.setVisibility(View.VISIBLE);

            progressBar.setProgress(value[0]);

            Log.i(ACTIVITY_NAME, "In onProgressUpdate");

        }


        @Override

        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.INVISIBLE);

            minTempText.setText(minTemp+ "\u2103");

            maxTempText.setText(maxTemp+ "\u2103");

            currentTempText.setText(currentTemp+ "\u2103");

            windSpeedText.setText(windSpeed+ " KHP");

            weatherImage.setImageBitmap(bitmap);

        }


        public boolean fileExistence(String fname) {

            File file = getBaseContext().getFileStreamPath(fname);

            return file.exists();

        }


    }
}

            //connecting to url and reading data input stream
