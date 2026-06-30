package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    Spinner conversionType, unitFrom, unitTo;
    Button convertBtn;
    TextView resultText;

    String[][] units = {
            {"mm", "cm", "m", "km"},
            {"mg", "g", "kg"},
            {"°C", "°F", "K"},
            {"mL", "L"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        conversionType = findViewById(R.id.conversionType);
        unitFrom = findViewById(R.id.unitFrom);
        unitTo = findViewById(R.id.unitTo);
        convertBtn = findViewById(R.id.convertBtn);
        resultText = findViewById(R.id.resultText);

        String[] types = {"Length", "Weight", "Temperature", "Volume"};

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                types);

        conversionType.setAdapter(typeAdapter);

        conversionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        units[position]);

                unitFrom.setAdapter(unitAdapter);
                unitTo.setAdapter(unitAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        convertBtn.setOnClickListener(v -> convert());
    }

    private void convert() {

        String text = inputValue.getText().toString().trim();

        if (text.isEmpty()) {
            inputValue.setError("Enter a value");
            return;
        }

        double value = Double.parseDouble(text);

        String from = unitFrom.getSelectedItem().toString();
        String to = unitTo.getSelectedItem().toString();
        int type = conversionType.getSelectedItemPosition();

        double result = 0;

        if (type == 0) {
            double mm = toMM(value, from);
            result = fromMM(mm, to);
        } else if (type == 1) {
            double g = toGram(value, from);
            result = fromGram(g, to);
        } else if (type == 2) {
            result = tempConvert(value, from, to);
        } else if (type == 3) {
            double ml = toML(value, from);
            result = fromML(ml, to);
        }

        resultText.setText("Result : " + result + " " + to);
    }

    // -------- LENGTH --------

    private double toMM(double v, String u) {
        switch (u) {
            case "cm":
                return v * 10;
            case "m":
                return v * 1000;
            case "km":
                return v * 1000000;
            default:
                return v;
        }
    }

    private double fromMM(double v, String u) {
        switch (u) {
            case "cm":
                return v / 10;
            case "m":
                return v / 1000;
            case "km":
                return v / 1000000;
            default:
                return v;
        }
    }

    // -------- WEIGHT --------

    private double toGram(double v, String u) {
        switch (u) {
            case "mg":
                return v / 1000;
            case "kg":
                return v * 1000;
            default:
                return v;
        }
    }

    private double fromGram(double v, String u) {
        switch (u) {
            case "mg":
                return v * 1000;
            case "kg":
                return v / 1000;
            default:
                return v;
        }
    }

    // -------- TEMPERATURE --------

    private double tempConvert(double v, String from, String to) {

        double celsius;

        if (from.equals("°F")) {
            celsius = (v - 32) * 5 / 9;
        } else if (from.equals("K")) {
            celsius = v - 273.15;
        } else {
            celsius = v;
        }

        if (to.equals("°F")) {
            return (celsius * 9 / 5) + 32;
        } else if (to.equals("K")) {
            return celsius + 273.15;
        } else {
            return celsius;
        }
    }

    // -------- VOLUME --------

    private double toML(double v, String u) {
        if (u.equals("L")) {
            return v * 1000;
        }
        return v;
    }

    private double fromML(double v, String u) {
        if (u.equals("L")) {
            return v / 1000;
        }
        return v;
    }
}