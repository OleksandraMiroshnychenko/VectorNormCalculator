import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class VectorNormCalculator {
    private JTextField[] vector1Fields = new JTextField[3];
    private JTextField[] vector2Fields = new JTextField[3];
    private JLabel[] resultLabels = new JLabel[9];

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VectorNormCalculator::new);
    }

    public VectorNormCalculator() {
        JFrame frame = new JFrame("Калькулятор норм та метрик Мірошниченко Олександри");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(new GridLayout(0, 1));

        frame.add(new JLabel("Vector 1 (x1, x2, x3):"));
        for (int i = 0; i < 3; i++) {
            vector1Fields[i] = new JTextField(10);
            frame.add(vector1Fields[i]);
        }

        frame.add(new JLabel("Vector 2 (y1, y2, y3):"));
        for (int i = 0; i < 3; i++) {
            vector2Fields[i] = new JTextField(10);
            frame.add(vector2Fields[i]);
        }

        JButton calculateButton = new JButton("Обрахувати");
        calculateButton.setBackground(Color.GREEN);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(this::calculateMetrics);
        frame.add(calculateButton);

        JButton clearButton = new JButton("Очистити");
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> clearEntries());
        frame.add(clearButton);

        String[] headers = {"", "Евклідова", "Міська", "Чебишева"};
        String[] rows = {"Розмір - ||V1||", "Розмір - ||V2||", "Відстань ||V1-V2||"};

        JPanel tablePanel = new JPanel(new GridLayout(4, 4));
        for (String header : headers) {
            tablePanel.add(new JLabel(header, SwingConstants.CENTER));
        }

        int index = 0;
        for (String row : rows) {
            tablePanel.add(new JLabel(row, SwingConstants.CENTER));
            for (int j = 0; j < 3; j++) {
                resultLabels[index] = new JLabel("-");
                tablePanel.add(resultLabels[index]);
                index++;
            }
        }

        frame.add(tablePanel);
        frame.setVisible(true);
    }

    private void calculateMetrics(ActionEvent e) {
        try {
            double[] v1 = Arrays.stream(vector1Fields).mapToDouble(f -> Double.parseDouble(f.getText())).toArray();
            double[] v2 = Arrays.stream(vector2Fields).mapToDouble(f -> Double.parseDouble(f.getText())).toArray();

            double[] v1Norms = {euclideanNorm(v1), manhattanNorm(v1), chebyshevNorm(v1)};
            double[] v2Norms = {euclideanNorm(v2), manhattanNorm(v2), chebyshevNorm(v2)};

            double[] diff = new double[3];
            for (int i = 0; i < 3; i++) diff[i] = v2[i] - v1[i];
            double[] diffNorms = {euclideanNorm(diff), manhattanNorm(diff), chebyshevNorm(diff)};

            double[] values = new double[9];
            System.arraycopy(v1Norms, 0, values, 0, 3);
            System.arraycopy(v2Norms, 0, values, 3, 3);
            System.arraycopy(diffNorms, 0, values, 6, 3);

            for (int i = 0; i < resultLabels.length; i++) {
                resultLabels[i].setText(String.format("%.2f", values[i]));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Будь ласка, введіть коректні числові значення.", "Помилка введення", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearEntries() {
        for (JTextField field : vector1Fields) field.setText("");
        for (JTextField field : vector2Fields) field.setText("");
        for (JLabel label : resultLabels) label.setText("-");
    }

    private double euclideanNorm(double[] v) {
        return Math.round(Math.sqrt(Arrays.stream(v).map(x -> x * x).sum()) * 100.0) / 100.0;
    }

    private double manhattanNorm(double[] v) {
        return Math.round(Arrays.stream(v).map(Math::abs).sum() * 100.0) / 100.0;
    }

    private double chebyshevNorm(double[] v) {
        return Math.round(Arrays.stream(v).map(Math::abs).max().orElse(0) * 100.0) / 100.0;
    }
}
