package fetch.coding.fetchexercise;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class DisplayData extends AppCompatActivity {
    ListItem[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        /* Override default application behavior to bypass network operation restrictions on the main thread.
           Typically not advisable as application performance may be impacted negatively, i.e. a NetworkOnMainThreadException
           is thrown when an application attempts to perform a networking operation on its main thread.
           This was option chosen to allow for feedback to user regarding connection updates by Toast. */

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            // Locate the web URL and attempt to establish connection to it
            URL jsonDataURL = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
            HttpURLConnection con = (HttpURLConnection) jsonDataURL.openConnection();

            // Retrieve response code from attempted connection, provide feedback to user
            System.out.println("\nSending 'GET' request to URL: " + jsonDataURL);
            int responseCode = con.getResponseCode();
            System.out.println("Response code: " + responseCode);

            // Notify user a connection has been successfully established, notify user connection failed otherwise
            if (responseCode == 200) {
                Log.i("URL Response Code: 200. ", "Connection Successful!");
                Toast.makeText(DisplayData.this, R.string.connection_success, Toast.LENGTH_SHORT).show();

                // Read JSON data from URL
                readJsonFromURL();
            } else {
                Log.i("Connection failed.", "Connection was unsuccessful. Please check your internet connection and try again.");
                Toast.makeText(DisplayData.this, R.string.connection_failure, Toast.LENGTH_SHORT).show();
            }

        } catch (MalformedURLException me) {
            Log.e("MalformedURLException", "The connection to the requested website failed; website not found. Please check the URL is correct and try again.\n" + me);
        } catch (IOException ioe) {
            Toast.makeText(DisplayData.this, R.string.connection_failure, Toast.LENGTH_SHORT).show();
            Log.e("IOException: ", "Connection failed. Please check your internet connection and try again.\n" + ioe);
        }

    }

    // Read JSON data from URL into ListItem model class, sanitize data through filtration and sorting, and populate sanitized data into TableLayout for presentation
    public void readJsonFromURL() throws ClassCastException, JsonSyntaxException {
        try {
            // Populate JSON data into the model ListItem.java class utilizing the Gson library
            Gson gson = new Gson();
            items = gson.fromJson(getJSON("https://fetch-hiring.s3.amazonaws.com/hiring.json"), ListItem[].class);

            // Filter list out of "name" items that have a blank name or null field values, sort list in ascending order
            items = Arrays.stream(items).filter(item -> item.getName() != null && !item.getName().isEmpty()).toArray(ListItem[]::new);
            Arrays.sort(items);

            // Dynamically create all of the necessary table rows for TableLayout and populate them with JSON data
            dynamicallyCreateTableLayoutRowsAndDisplayData();

        } catch (JsonSyntaxException jse) {
            Log.e("JsonSyntaxException", "Attempted to read a malformed JSON element. Ensure all JSON elements are valid.\n" + jse);
        } catch (ClassCastException cce) {
            Log.e("ClassCastException", "Attempted to cast an object to a subclass of which it is not an instance.\n" + cce);
        }
    }

    // Retrieve the JSON data from URL source
    public String getJSON(String dataURL) {
        // Required for future use for URL access and buffer read operations
        HttpURLConnection urlToConnectTo = null;

        // Attempt to establish connection to the URL, internet access required
        try {
            URL jsonDataURL = new URL(dataURL);
            urlToConnectTo = (HttpURLConnection) jsonDataURL.openConnection();
            urlToConnectTo.connect();

            // Buffer input to read input stream
            BufferedReader bReader = new BufferedReader(new InputStreamReader(urlToConnectTo.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            // Append existing string with newly read data
            while ((line = bReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bReader.close();
            return stringBuilder.toString();
        } catch (MalformedURLException mue) {
            Log.e("MalformedURLException", "The connection to the requested website failed, website not found. Please check the URL is correct and try again.\n" + mue);
        } catch (IOException ioe) {
            Log.e("IOException", "The requested operation failed or was interrupted due to an I/O issue. Please check your internet connection and try again.\n" + ioe);
        } finally {
            // Disconnect from the webserver
            if (urlToConnectTo != null) {
                try {
                    urlToConnectTo.disconnect();
                } catch (Exception e) {
                    Log.e("Exception", "Disconnect from webserver failed due to an unknown reason.");
                }
            }
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    // Create all required table layout rows, including header and data rows
    private void dynamicallyCreateTableLayoutRowsAndDisplayData() {
        TableLayout tableLayout = findViewById(R.id.table_layout);

        // Create the table row header, add to the table layout
        TableRow tableRowHeader = new TableRow(this);
        tableRowHeader.setGravity(Gravity.CENTER);
        tableLayout.addView(tableRowHeader);

        // Get the current list item, ensure all items are displayed within same listId group
        ListItem previous = null;
        for (ListItem current : items) {
            if (previous == null || previous.getListId() != current.getListId()) {
                // Create new table header rows to display each new listId section
                insertGroupHeader(current, tableLayout);
            }

            // Insert JSON data into rows for name and id, set previous listId section as current
            insertDataRow(current, tableLayout);
            previous = current;
        }

        // Create and display empty row at bottom of screen once all data has been displayed
        TableRow blankTableRow = new TableRow(this);
        TextView blankTextView = new TextView(this);
        blankTableRow.addView(blankTextView);
        tableLayout.addView(blankTableRow);
    }

    @SuppressLint("SetTextI18n")
    // Create new table header rows to display at the start of each new listId section
    private void insertGroupHeader(ListItem current, TableLayout tableLayout) {

        // Create empty top row, text view field for header
        TableRow topBlankTableRow = new TableRow(this);
        TextView topBlankTextView = new TextView(this);
        topBlankTableRow.addView(topBlankTextView);

        // Create and populate central row with current listId item value
        TableRow dataTableRow = new TableRow(this);
        dataTableRow.setGravity(Gravity.CENTER);
        TextView listIdValue = new TextView(this);
        listIdValue.setText("listId: " + current.getListId());
        listIdValue.setPadding(100,0,0,0);
        listIdValue.setTypeface(null, Typeface.BOLD);
        listIdValue.setGravity(Gravity.CENTER);
        listIdValue.setTextColor(Color.WHITE);
        listIdValue.setTextSize(25);
        dataTableRow.addView(listIdValue);

        // Create empty bottom row, text view field for header
        TableRow bottomBlankTableRow = new TableRow(this);
        TextView bottomBlankTextView = new TextView(this);
        bottomBlankTableRow.addView(bottomBlankTextView);

        // Add the three header rows to the table layout
        tableLayout.addView(topBlankTableRow);
        tableLayout.addView(dataTableRow);
        tableLayout.addView(bottomBlankTableRow);
    }

    @SuppressLint("SetTextI18n")
    // Insert JSON data into rows
    private void insertDataRow(ListItem current, TableLayout tableLayout) {

        // Create new table row for each row of JSON entries
        TableRow dataTableRow = new TableRow(this);

        // Display the "name" values, added to the current table row (1/2)
        TextView nameValue = new TextView(this);
        nameValue.setText(current.getName());
        nameValue.setTextColor(Color.WHITE);
        nameValue.setGravity(Gravity.CENTER);
        nameValue.setPadding(0,0,200,0);
        dataTableRow.addView(nameValue);

        // Display the "id" values, added to the current table row (2/2)
        TextView idValue = new TextView(this);
        idValue.setText(current.getId() + "");
        idValue.setTextColor(Color.WHITE);
        idValue.setGravity(Gravity.CENTER);
        // idValue.setPadding(0,0,0,0);
        dataTableRow.addView(idValue);

        // Add the two populated data columns to the table row
        tableLayout.addView(dataTableRow);
    }

}