package edu.pdx.cs410J.mattcole;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path = getFilesDir().getPath();
        execute(new String[]{"-README"});
    }
    protected void execute(String[] arguments)
    {
        TextView textView = findViewById(R.id.outputText);
        textView.setMovementMethod(new ScrollingMovementMethod());
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        if (!Project4.main(arguments,writer))
        {
            Toast.makeText(getApplicationContext(), out.toString(), Toast.LENGTH_LONG).show();
        }
        textView.setText(out.toString());


        //Code copied over from stack overflow to hide keyboard https://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard-programmatically
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
    public void searchButton(View view) {
        String[] args = new String[]{"-textFile", path + "/" + getAirline() + ".txt", "-pretty", "-",
                "-search", getDest(), getSource(),getAirline()};
        execute(args);
    }

    public void addFlight(View view) {
        String[] args = new String[]{"-textFile", path + "/" + getAirline() + ".txt", "-print",
                getAirline(), getNumber(), getSource(),getDepart(), "","",getDest(),getArrive(), "",""};
        execute(args);
    }

    public void helpButton(View view) {
        execute(new String[]{"-README"});
    }

    public void displayAirline(View view) {
        String[] args = new String[]{"-textFile", path + "/" + getAirline() + ".txt", "-pretty", "-", getAirline()};
        execute(args);
    }
    public String get(int id)
    {
        EditText editText = findViewById(id);
        return editText.getText().toString();
    }
    public String getAirline()
    {
        return get(R.id.airlineInput);
    }
    public String getSource()
    {
        return get(R.id.SourceInput);
    }
    public String getDepart()
    {
        return get(R.id.DepartInput);
    }
    public String getArrive()
    {
        return get(R.id.ArriveInput);
    }
    public String getDest()
    {
        return get(R.id.DestinationInput);
    }
    public String getNumber()
    {
        return get(R.id.numberInput);
    }
}