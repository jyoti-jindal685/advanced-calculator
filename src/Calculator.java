import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Calculator {
    int boardWidth = 520;
    int boardHeight = 760;

    Color customLightGray = new Color(212, 212, 210);
    Color customDarkGray = new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);
    Color memoryBlue = new Color(51, 153, 255);
    Color lightBackground = new Color(245, 245, 245);
    Color lightButton = new Color(230, 230, 230);
    Color lightOperator = new Color(255, 190, 70);

    String[] buttonValues = {
        "AC", "+/-", "%", "√", "x²",
        "M+", "M-", "MR", "÷", "xʸ",
        "7", "8", "9", "×", "0",
        "4", "5", "6", "-", ".",
        "1", "2", "3", "+", "="
    };

    List<String> operatorSymbols = Arrays.asList("÷", "×", "-", "+", "xʸ");
    List<String> functionSymbols = Arrays.asList("AC", "+/-", "%", "√", "x²");
    List<String> memorySymbols = Arrays.asList("M+", "M-", "MR");

    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JPanel historyPanel = new JPanel();
    JTextArea historyArea = new JTextArea();
    JToggleButton themeToggle = new JToggleButton("Light Mode");

    String A = "0";
    String Operator = null;
    String B = null;
    double memoryValue = 0;
    boolean darkMode = true;
    List<JButton> buttons = new ArrayList<>();

    Calculator() {
        frame.setSize(600, 800);
        frame.setMinimumSize(new Dimension(460, 720));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(12, 12));

        Font uiFont = new Font("Segoe UI", Font.PLAIN, 28);
        frame.setFont(uiFont);

        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(uiFont.deriveFont(52f));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayLabel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        displayPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Calculator");
        titleLabel.setFont(uiFont.deriveFont(Font.BOLD, 22f));
        titleLabel.setForeground(Color.white);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        displayPanel.add(titleLabel, BorderLayout.NORTH);
        displayPanel.add(displayLabel, BorderLayout.CENTER);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5, 5, 8, 8));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonsPanel.setOpaque(false);

        for (String buttonValue : buttonValues) {
            addButton(buttonValue, uiFont);
        }

        historyPanel.setLayout(new BorderLayout(8, 8));
        historyPanel.setPreferredSize(new Dimension(220, 0));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel historyLabel = new JLabel("History");
        historyLabel.setFont(uiFont.deriveFont(Font.BOLD, 20f));
        historyPanel.add(historyLabel, BorderLayout.NORTH);

        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyArea.setFont(uiFont.deriveFont(16f));
        historyArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(customLightGray, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(null);
        historyPanel.add(historyScroll, BorderLayout.CENTER);

        themeToggle.setFocusPainted(false);
        themeToggle.setFont(uiFont.deriveFont(18f));
        themeToggle.addActionListener(e -> {
            darkMode = !darkMode;
            updateTheme();
        });
        historyPanel.add(themeToggle, BorderLayout.SOUTH);

        frame.add(buttonsPanel, BorderLayout.CENTER);
        frame.add(historyPanel, BorderLayout.EAST);

        setupKeyBindings();
        updateTheme();

        frame.setVisible(true);
    }

    private void addButton(String buttonValue, Font uiFont) {
        JButton button = new JButton(buttonValue);
        button.setFont(uiFont.deriveFont(18f));
        button.setFocusable(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setBorderPainted(false);
button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setFocusPainted(false);

        Color tempColor = customDarkGray;
        button.setForeground(Color.white);
        if (functionSymbols.contains(buttonValue)) {
            tempColor = customLightGray;
            button.setForeground(customBlack);
        } else if (operatorSymbols.contains(buttonValue) || buttonValue.equals("=")) {
            tempColor = customOrange;
        } else if (memorySymbols.contains(buttonValue)) {
            tempColor = memoryBlue;
        }
        final Color baseColor = tempColor;

        button.setBackground(baseColor);
        button.putClientProperty("baseColor", baseColor);
        button.setToolTipText(getButtonTooltip(buttonValue));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Color currentBase = (Color) button.getClientProperty("baseColor");
                if (currentBase == null) {
                    currentBase = baseColor;
                }
                button.setBackground(adjustBrightness(currentBase, darkMode ? 1.15f : 0.95f));
            }
            public void mouseExited(MouseEvent e) {
                Color currentBase = (Color) button.getClientProperty("baseColor");
                if (currentBase == null) {
                    currentBase = baseColor;
                }
                button.setBackground(currentBase);
            }
        });

        button.addActionListener(e -> handleButton(buttonValue));
        buttons.add(button);
        buttonsPanel.add(button);
    }

    private Color adjustBrightness(Color color, float factor) {
        int red = Math.min(255, Math.max(0, (int) (color.getRed() * factor)));
        int green = Math.min(255, Math.max(0, (int) (color.getGreen() * factor)));
        int blue = Math.min(255, Math.max(0, (int) (color.getBlue() * factor)));
        return new Color(red, green, blue);
    }

    private String getButtonTooltip(String buttonValue) {
        switch (buttonValue) {
            case "AC": return "Clear all";
            case "+/-": return "Toggle sign";
            case "%": return "Percent";
            case "√": return "Square root";
            case "x²": return "Square the current number";
            case "M+": return "Add current value to memory";
            case "M-": return "Subtract current value from memory";
            case "MR": return "Recall memory";
            case "÷": return "Divide";
            case "×": return "Multiply";
            case "-": return "Subtract";
            case "+": return "Add";
            case "xʸ": return "Power: x to the y";
            case "=": return "Calculate result";
            case ".": return "Decimal point";
            default: return "Enter number " + buttonValue;
        }
    }

    private void handleButton(String buttonValue) {
        if (operatorSymbols.contains(buttonValue)) {
            setOperator(buttonValue);
        } else if (buttonValue.equals("=")) {
            completeCalculation();
        } else if (functionSymbols.contains(buttonValue)) {
            handleFunction(buttonValue);
        } else if (memorySymbols.contains(buttonValue)) {
            handleMemory(buttonValue);
        } else if (buttonValue.equals(".")) {
            appendDot();
        } else {
            appendDigit(buttonValue);
        }
    }

    private void setOperator(String op) {
        if (Operator != null && A != null) {
            String intermediate = calculate(A, Operator, displayLabel.getText());
            if (intermediate != null) {
                displayLabel.setText(intermediate);
                A = intermediate;
            }
        } else {
            A = displayLabel.getText();
        }
        Operator = op;
        displayLabel.setText("0");
    }

    private void completeCalculation() {
        if (A == null && Operator == null && displayLabel.getText().startsWith("√")) {
            String result = evaluatePendingSqrt(displayLabel.getText());
            if (result != null) {
                appendHistory(displayLabel.getText() + " = " + result);
                displayLabel.setText(result);
            }
            return;
        }

        if (A == null || Operator == null) {
            return;
        }
        String result = calculate(A, Operator, displayLabel.getText());
        if (result != null) {
            appendHistory(A + " " + Operator + " " + displayLabel.getText() + " = " + result);
            displayLabel.setText(result);
            clearAll();
        }
    }

    private String evaluatePendingSqrt(String text) {
        if (!text.startsWith("√")) {
            return null;
        }
        String numeric = text.substring(1);
        if (numeric.isEmpty()) {
            return null;
        }
        double value = parseDisplayValue(numeric);
        if (Double.isNaN(value) || value < 0) {
            displayLabel.setText("Error");
            return null;
        }
        return removeZeroDecimal(Math.sqrt(value));
    }

    private String calculate(String a, String op, String b) {
        double numA = parseDisplayValue(a);
        double numB = parseDisplayValue(b);
        if (Double.isNaN(numA) || Double.isNaN(numB)) {
            displayLabel.setText("Error");
            return null;
        }

        double result;
        switch (op) {
            case "+":
                result = numA + numB;
                break;
            case "-":
                result = numA - numB;
                break;
            case "×":
                result = numA * numB;
                break;
            case "÷":
                result = numB == 0 ? Double.NaN : numA / numB;
                break;
            case "xʸ":
                result = Math.pow(numA, numB);
                break;
            default:
                return null;
        }

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            displayLabel.setText("Error");
            return null;
        }
        return removeZeroDecimal(result);
    }

    private void handleFunction(String buttonValue) {
        switch (buttonValue) {
            case "AC":
                clearAll();
                displayLabel.setText("0");
                break;
            case "+/-":
                double inverted = parseDisplayValue(displayLabel.getText()) * -1;
                displayLabel.setText(removeZeroDecimal(inverted));
                break;
            case "%":
                double percent = parseDisplayValue(displayLabel.getText()) / 100.0;
                displayLabel.setText(removeZeroDecimal(percent));
                break;
            case "√":
                if (displayLabel.getText().equals("0") || displayLabel.getText().equals("Error")) {
                    displayLabel.setText("√");
                } else if (displayLabel.getText().startsWith("√")) {
                    // do nothing if already waiting for a number
                } else {
                    displayLabel.setText("√" + displayLabel.getText());
                }
                break;
            case "x²":
                double squared = parseDisplayValue(displayLabel.getText());
                String squareValue = removeZeroDecimal(squared * squared);
                appendHistory(displayLabel.getText() + "² = " + squareValue);
                displayLabel.setText(squareValue);
                break;
        }
    }

    private void handleMemory(String buttonValue) {
        switch (buttonValue) {
            case "M+":
                memoryValue += parseDisplayValue(displayLabel.getText());
                appendHistory("M+ " + displayLabel.getText() + " => " + removeZeroDecimal(memoryValue));
                break;
            case "M-":
                memoryValue -= parseDisplayValue(displayLabel.getText());
                appendHistory("M- " + displayLabel.getText() + " => " + removeZeroDecimal(memoryValue));
                break;
            case "MR":
                displayLabel.setText(removeZeroDecimal(memoryValue));
                appendHistory("MR => " + removeZeroDecimal(memoryValue));
                break;
        }
    }

    private void appendDigit(String digit) {
        String text = displayLabel.getText();
        if (text.equals("0") || text.equals("Error")) {
            displayLabel.setText(digit);
        } else if (text.equals("√")) {
            displayLabel.setText("√" + digit);
        } else {
            displayLabel.setText(text + digit);
        }
    }

    private void appendDot() {
        String text = displayLabel.getText();
        if (text.equals("Error")) {
            displayLabel.setText("0.");
        } else if (text.equals("√")) {
            displayLabel.setText("√0.");
        } else if (!text.contains(".")) {
            displayLabel.setText(text + ".");
        } else if (text.startsWith("√") && !text.substring(1).contains(".")) {
            displayLabel.setText(text + ".");
        }
    }

    private void clearAll() {
        A = null;
        Operator = null;
        B = null;
    }

    private double parseDisplayValue(String text) {
        if (text == null || text.isEmpty() || text.equals("Error")) {
            return 0;
        }
        if (text.startsWith("√")) {
            String numeric = text.substring(1);
            if (numeric.isEmpty()) {
                return Double.NaN;
            }
            try {
                return Double.parseDouble(numeric);
            } catch (NumberFormatException ex) {
                return Double.NaN;
            }
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            return Double.NaN;
        }
    }

    private String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }

    private void appendHistory(String entry) {
        historyArea.append(entry + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    private void updateTheme() {
        Color background = darkMode ? customBlack : lightBackground;
        Color panelBackground = darkMode ? customBlack : lightBackground;
        Color displayBackground = darkMode ? customBlack : Color.white;
        Color displayForeground = darkMode ? Color.white : Color.black;
        Color historyBackground = darkMode ? customDarkGray : Color.white;
        Color historyForeground = darkMode ? Color.white : Color.black;

        frame.getContentPane().setBackground(background);
        displayPanel.setBackground(panelBackground);
        buttonsPanel.setBackground(panelBackground);
        historyPanel.setBackground(panelBackground);
        displayLabel.setBackground(displayBackground);
        displayLabel.setForeground(displayForeground);
        historyArea.setBackground(historyBackground);
        historyArea.setForeground(historyForeground);
        themeToggle.setText(darkMode ? "Light Mode" : "Dark Mode");
        themeToggle.setBackground(darkMode ? customLightGray : lightButton);
        themeToggle.setForeground(darkMode ? customBlack : Color.black);

        for (JButton button : buttons) {
            String text = button.getText();
            Color baseColor;
            if (functionSymbols.contains(text)) {
                baseColor = darkMode ? customLightGray : lightButton;
                button.setForeground(darkMode ? customBlack : Color.black);
            } else if (operatorSymbols.contains(text) || text.equals("=")) {
                baseColor = darkMode ? customOrange : lightOperator;
                button.setForeground(Color.white);
            } else if (memorySymbols.contains(text)) {
                baseColor = memoryBlue;
                button.setForeground(Color.white);
            } else {
                baseColor = darkMode ? customDarkGray : Color.white;
                button.setForeground(darkMode ? Color.white : Color.black);
            }
            button.setBackground(baseColor);
            button.putClientProperty("baseColor", baseColor);
        }
    }

    private void setupKeyBindings() {
        JRootPane rootPane = frame.getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        for (char key = '0'; key <= '9'; key++) {
            String value = String.valueOf(key);
            inputMap.put(KeyStroke.getKeyStroke(key), value);
            actionMap.put(value, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    appendDigit(value);
                }
            });
        }

        inputMap.put(KeyStroke.getKeyStroke('.'), "dot");
        actionMap.put("dot", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                appendDot();
            }
        });

        addOperatorKey(inputMap, actionMap, '+', "+");
        addOperatorKey(inputMap, actionMap, '-', "-");
        addOperatorKey(inputMap, actionMap, '*', "×");
        addOperatorKey(inputMap, actionMap, '/', "÷");
        addOperatorKey(inputMap, actionMap, '^', "xʸ");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "equals");
        actionMap.put("equals", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                completeCalculation();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear");
        actionMap.put("clear", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                clearAll();
                displayLabel.setText("0");
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "backspace");
        actionMap.put("backspace", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String text = displayLabel.getText();
                if (text.length() <= 1 || text.equals("Error")) {
                    displayLabel.setText("0");
                } else {
                    displayLabel.setText(text.substring(0, text.length() - 1));
                }
            }
        });
    }

    private void addOperatorKey(InputMap inputMap, ActionMap actionMap, char keyChar, String buttonValue) {
        inputMap.put(KeyStroke.getKeyStroke(keyChar), buttonValue);
        actionMap.put(buttonValue, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                setOperator(buttonValue);
            }
        });
    }
}
