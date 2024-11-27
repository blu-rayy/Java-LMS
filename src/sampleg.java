import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class sampleg extends JFrame {

    private JPanel panelMain;
    private JLabel sampleLabel;
    private JTextField txtname;
    private JButton confirmButton;

    public sampleg() {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, txtname.getText() + " Hey!");
            }
        });
    }

    public static void main(String[] args) {
        sampleg g = new sampleg();
        g.setContentPane(g.panelMain);
        g.setTitle("ANP LMS - Admin Dashboard");
        g.setSize(1366, 768);
        g.setVisible(true);
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
