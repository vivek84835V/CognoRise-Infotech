package com.example.bmi_calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText ageInput;
    private EditText weightInput;
    private EditText heightInput;
    private Button calculateButton;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ageInput = findViewById(R.id.ageInput);
        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);
        calculateButton = findViewById(R.id.calculateButton);
        result = findViewById(R.id.result);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String ageStr = ageInput.getText().toString();
        String weightStr = weightInput.getText().toString();
        String heightStr = heightInput.getText().toString();

        if (!ageStr.isEmpty() && !weightStr.isEmpty() && !heightStr.isEmpty()) {
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr) / 100;

            float bmi = weight / (height * height);

            String bmiLabel = "";
            if (bmi <= 18.4) {
                bmiLabel = "Underweight";
            } else if (bmi <= 24.9) {
                bmiLabel = "Normal weight";
            } else if (bmi <= 29.9) {
                bmiLabel = "Overweight";
            } else {
                bmiLabel = "Obesity";
            }

            result.setText(String.format("BMI: %.2f :- \n %s", bmi, bmiLabel));
        } else {
            result.setText("Please enter age, weight, and height");
        }
    }
}
