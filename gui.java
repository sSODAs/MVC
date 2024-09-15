import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class gui extends JFrame {

    private JTextField info_Field;
    private JTextArea output_Area;
    private JButton cal;
    private Logic logic;

    // View ZONE

    public gui() {

        logic = new Logic();

        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();

        p = new JPanel();
        p.setLayout(null);

        JLabel Text = new JLabel("Input");
        Text.setBounds(20, 40, 80, 25);
        p.add(Text);

        info_Field = new JTextField();
        info_Field.setBounds(70, 40, 260, 25);
        p.add(info_Field);

        output_Area = new JTextArea();
        output_Area.setBounds(20, 90, 260, 25);
        output_Area.setEditable(false);
        p.add(output_Area);

        cal = new JButton("calculate");
        cal.setBounds(350, 40, 100, 25);

        cal.addActionListener(e -> {
            String input = info_Field.getText();

            // Write the input to the CSV file
            writeToCSV(input);

            // Call the logic to process and display the result
            logic.handleCalculate(info_Field, output_Area);
        });

        p.add(cal);

        add(p);

    }

    // Method to write the input to the CSV file
    private void writeToCSV(String input) {
        try (FileWriter writer = new FileWriter("data.csv", true)) { // Append to the file
            String csvLine = input + "\n";
            writer.write(csvLine);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        gui f = new gui();
        f.setVisible(true);
    }

}
