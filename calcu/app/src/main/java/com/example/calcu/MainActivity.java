package com.example.calcu;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private TextView input;
    private TextView output;
    private String currentInput = "";
    private String operationSequenceText = "";
    private double result = 0.0;
    private String currentOperator = "";
    private boolean isOperatorPressed = false;
    private static final int MAX_TEXT_SIZE = 50;
    private static final int MIN_TEXT_SIZE = 30;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, set dark theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
            default:
                // Night mode is not active or undefined, set light theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }


        setContentView(R.layout.activity_main);

        output = findViewById(R.id.output);
        input = findViewById(R.id.input);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // Initialize Vibrator

        setButtonListeners();
    }

    private void setButtonListeners() {
        int[] numericButtons = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        int[] operatorButtons = {
                R.id.btnadd, R.id.btnminus, R.id.btnmulti, R.id.btndiv,
                R.id.btnper, R.id.btnequal, R.id.btnAC, R.id.btnplusminus, R.id.btndot
        };

        View.OnClickListener numericButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                vibrate(); // Vibrate on button click
                if (isOperatorPressed) {
                    currentInput = "";
                    isOperatorPressed = false;
                }
                currentInput += button.getText().toString();
                input.setText(currentInput);
                adjustDisplayTextSize();
            }
        };

        View.OnClickListener operatorButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                vibrate(); // Vibrate on button click
                String buttonText = button.getText().toString();

                switch (buttonText) {
                    case "AC":
                        clear();
                        break;
                    case "+/-":
                        toggleSign();
                        break;
                    case "%":
                        calculatePercentage();
                        break;
                    case "=":
                        calculateResult();
                        break;
                    case ".":
                        addDecimalPoint();
                        break;
                    default:
                        setOperator(buttonText);
                        break;
                }
            }
        };

        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(numericButtonListener);
        }

        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(operatorButtonListener);
        }
    }

    private void clear() {
        currentInput = "";
        operationSequenceText = "";
        result = 0.0;
        currentOperator = "";
        isOperatorPressed = false;
        input.setText("0");
        output.setText("");
        adjustDisplayTextSize();
    }

    private void toggleSign() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            value = -value;
            currentInput = String.valueOf(value);
            input.setText(currentInput);
        }
    }

    private void calculatePercentage() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            value = value / 100;
            currentInput = String.valueOf(value);
            input.setText(currentInput);
        }
    }

    private void calculateResult() {
        if (!currentInput.isEmpty()) {
            operationSequenceText += currentInput;
            double currentNumber = Double.parseDouble(currentInput);
            result = performOperation(result, currentNumber, currentOperator);
            input.setText(formatResult(result));
            output.setText(operationSequenceText + " =");
            currentInput = String.valueOf(result);
            currentOperator = "";
            adjustDisplayTextSize();
        }
    }

    private void addDecimalPoint() {
        if (!currentInput.contains(".")) {
            currentInput += ".";
            input.setText(currentInput);
        }
    }

    private void setOperator(String op) {
        if (!currentInput.isEmpty()) {
            double currentNumber = Double.parseDouble(currentInput);
            result = performOperation(result, currentNumber, currentOperator);
            operationSequenceText += currentInput + " " + op + " ";
            output.setText(operationSequenceText);
            currentOperator = op;
            currentInput = "";
            isOperatorPressed = true;
            adjustDisplayTextSize();
        }
    }

    private double performOperation(double first, double second, String operator) {
        switch (operator) {
            case "+":
                return first + second;
            case "-":
                return first - second;
            case "×":
                return first * second;
            case "÷":
                return second != 0 ? first / second : 0;  // Handle division by zero
            default:
                return second;
        }
    }


    private String formatResult(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.format("%.2f", value);
        }
    }

    private void adjustDisplayTextSize() {
        int length = output.getText().toString().length();
        if (length > 10) {
            input.setTextSize(MIN_TEXT_SIZE);
        } else {
            input.setTextSize(MAX_TEXT_SIZE);
        }
    }



    // Method to vibrate on button click
    private void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Use VibrationEffect for API level 26+
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        } else {
            // Fallback to old vibrate method for API level < 26
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(50); // Vibrate for 50 milliseconds
            }
        }
    }
}
