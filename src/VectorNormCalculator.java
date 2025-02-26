import javax.swing.*; // Імпортує бібліотеку для створення графічного інтерфейсу
import java.awt.*; // Імпортує бібліотеку для роботи з елементами графічного інтерфейсу
import java.awt.event.ActionEvent; // Імпортує клас для обробки подій (натискання кнопки)
import java.util.Arrays; // Імпортує утиліти для роботи з масивами

public class VectorNormCalculator {
    private JTextField[] vector1Fields = new JTextField[3]; // Поля введення для першого вектора
    private JTextField[] vector2Fields = new JTextField[3]; // Поля введення для другого вектора
    private JLabel[] resultLabels = new JLabel[9]; // Мітки для відображення результатів обчислень

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VectorNormCalculator::new); // Запускає GUI у потоці диспетчеризації подій
    }

    public VectorNormCalculator() {
        JFrame frame = new JFrame("Калькулятор норм та метрик Мірошниченко Олександри"); // Створює головне вікно
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Закриває програму при виході
        frame.setSize(500, 600); // Встановлює розмір вікна
        frame.setLayout(new GridLayout(0, 1)); // Встановлює менеджер розташування GridLayout

        frame.add(new JLabel("Vector 1 (x1, x2, x3):")); // Додає мітку для першого вектора
        for (int i = 0; i < 3; i++) {
            vector1Fields[i] = new JTextField(10); // Створює поле введення
            frame.add(vector1Fields[i]); // Додає поле до вікна
        }

        frame.add(new JLabel("Vector 2 (y1, y2, y3):")); // Додає мітку для другого вектора
        for (int i = 0; i < 3; i++) {
            vector2Fields[i] = new JTextField(10); // Створює поле введення
            frame.add(vector2Fields[i]); // Додає поле до вікна
        }

        JButton calculateButton = new JButton("Обрахувати"); // Створює кнопку обчислення
        calculateButton.setBackground(Color.GREEN); // Задає зелений фон кнопці
        calculateButton.setForeground(Color.WHITE); // Встановлює білий колір тексту
        calculateButton.addActionListener(this::calculateMetrics); // Додає обробник подій для кнопки
        frame.add(calculateButton); // Додає кнопку до вікна

        JButton clearButton = new JButton("Очистити"); // Створює кнопку очищення
        clearButton.setBackground(Color.RED); // Задає червоний фон кнопці
        clearButton.setForeground(Color.WHITE); // Встановлює білий колір тексту
        clearButton.addActionListener(e -> clearEntries()); // Додає обробник подій для очищення
        frame.add(clearButton); // Додає кнопку до вікна

        String[] headers = {"", "Евклідова", "Міська", "Чебишева"}; // Заголовки таблиці
        String[] rows = {"Розмір - ||V1||", "Розмір - ||V2||", "Відстань ||V1-V2||"}; // Рядки таблиці

        JPanel tablePanel = new JPanel(new GridLayout(4, 4)); // Створює панель для таблиці
        for (String header : headers) {
            tablePanel.add(new JLabel(header, SwingConstants.CENTER)); // Додає заголовки до таблиці
        }

        int index = 0;
        for (String row : rows) {
            tablePanel.add(new JLabel(row, SwingConstants.CENTER)); // Додає назви рядків
            for (int j = 0; j < 3; j++) {
                resultLabels[index] = new JLabel("-"); // Створює мітки результатів
                tablePanel.add(resultLabels[index]); // Додає мітки до таблиці
                index++;
            }
        }

        frame.add(tablePanel); // Додає таблицю до вікна
        frame.setVisible(true); // Робить вікно видимим
    }

    private void calculateMetrics(ActionEvent e) {
        try {
            double[] v1 = Arrays.stream(vector1Fields).mapToDouble(f -> Double.parseDouble(f.getText())).toArray(); // Зчитує вектор 1
            double[] v2 = Arrays.stream(vector2Fields).mapToDouble(f -> Double.parseDouble(f.getText())).toArray(); // Зчитує вектор 2

            double[] v1Norms = {euclideanNorm(v1), manhattanNorm(v1), chebyshevNorm(v1)}; // Обчислює норми для вектора 1
            double[] v2Norms = {euclideanNorm(v2), manhattanNorm(v2), chebyshevNorm(v2)}; // Обчислює норми для вектора 2

            double[] diff = new double[3]; // Масив для різниці векторів
            for (int i = 0; i < 3; i++) diff[i] = v2[i] - v1[i]; // Обчислює різницю
            double[] diffNorms = {euclideanNorm(diff), manhattanNorm(diff), chebyshevNorm(diff)}; // Обчислює норми різниці

            double[] values = new double[9]; // Масив для збереження всіх значень
            System.arraycopy(v1Norms, 0, values, 0, 3);
            System.arraycopy(v2Norms, 0, values, 3, 3);
            System.arraycopy(diffNorms, 0, values, 6, 3);

            for (int i = 0; i < resultLabels.length; i++) {
                resultLabels[i].setText(String.format("%.2f", values[i])); // Відображає результати
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Будь ласка, введіть коректні числові значення.", "Помилка введення", JOptionPane.ERROR_MESSAGE); // Виводить повідомлення про помилку
        }
    }

    private void clearEntries() {
        for (JTextField field : vector1Fields) field.setText(""); // Очищає поля першого вектора
        for (JTextField field : vector2Fields) field.setText(""); // Очищає поля другого вектора
        for (JLabel label : resultLabels) label.setText("-"); // Очищає результати
    }

    private double euclideanNorm(double[] v) {
        return Math.round(Math.sqrt(Arrays.stream(v).map(x -> x * x).sum()) * 100.0) / 100.0; // Обчислює евклідову норму
    }

    private double manhattanNorm(double[] v) {
        return Math.round(Arrays.stream(v).map(Math::abs).sum() * 100.0) / 100.0; // Обчислює мангеттенську норму
    }

    private double chebyshevNorm(double[] v) {
        return Math.round(Arrays.stream(v).map(Math::abs).max().orElse(0) * 100.0) / 100.0; // Обчислює чебишеву норму
    }
}