import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class RunClass64 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("RunClass64");
        JButton selectButton = new JButton("Open");
        JButton startButton = new JButton("Start!");
        JTextArea output = new JTextArea(10, 40);
        output.setEditable(false);

        final File[] selectedFile = {null};

        selectButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = chooser.getSelectedFile();
                output.setText("Selected: " + selectedFile[0].getAbsolutePath());
            }
        });

        startButton.addActionListener(e -> {
            if (selectedFile[0] == null) {
                output.setText("Please select a file that is not null.");
                return;
            }
            String className = selectedFile[0].getName().replace(".class", "");
            String dir = selectedFile[0].getParent();
            try {
                ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    "-cp", dir,
                    className
                );
                pb.redirectErrorStream(true);
                Process proc = pb.start();
                output.setText(new String(proc.getInputStream().readAllBytes()));
            } catch (IOException ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        JPanel panel = new JPanel();
        panel.add(selectButton);
        panel.add(startButton);

        frame.add(panel, "North");
        frame.add(new JScrollPane(output), "Center");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
