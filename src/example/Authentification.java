package example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;


public class Authentification extends JFrame {
    public JPanel Auth;
    private JButton buttonSign;
    private JTextField textUsername;
    private JPasswordField TextPassword;
    Connection connections = null;
    String Username;
    String Password;
    boolean status = false;


    public Authentification() {
        buttonSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Username = textUsername.getText();
                Password = TextPassword.getText();

                boolean t1 = true;
                boolean t2 = true;


                if (Username.isEmpty() || Username.equals("") || Username == null) {
                    textUsername.setBackground(new Color(140, 0, 15));
                    t1 = false;
                }
                if (Password.isEmpty() || Password.equals("") || Password == null) {
                    TextPassword.setBackground(new Color(140, 0, 15));
                    t2 = false;
                }
                if (t1 && t2) {

                    textUsername.setBackground(new Color(44, 88, 201));
                    TextPassword.setBackground(new Color(44, 88, 201));
                }

                //Authorisation
                try {
                    connections = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "root");
                    String SQL = "SELECT * FROM usertable  WHERE username = '" + Username + "' AND password = '" + Password + "'";
                    ResultSet RS = connections.createStatement().executeQuery(SQL);
                    if (RS.next()) {
                        status = true;

                    }

                } catch (SQLException ex) {

                    ex.printStackTrace();
                }

                // öffnung zweites Fenster
                if (status) {

                    JFrame Panell = new Avia();
                    Panell.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    Panell.pack();
                    Panell.setSize(1700, 700);
                    Panell.setLocationRelativeTo(null);
                    Panell.setVisible(true);
                    Panell.setResizable(false);
                    Panell.setIconImage(new ImageIcon("C:\\Users\\Juliasha\\IdeaProjects\\Avia\\src\\example\\air.png").getImage());


                } else {
                    JOptionPane.showMessageDialog(null, "Please check the entered data is correct", "Error!", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        JFrame panel = new JFrame("Authentication");
        panel.setContentPane(new Authentification().Auth);
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.pack();
        panel.setSize(500, 500);
        panel.setLocationRelativeTo(null);
        panel.setVisible(true);
        panel.setResizable(false);
        panel.setIconImage(new ImageIcon("C:\\Users\\Juliasha\\IdeaProjects\\Avia\\src\\example\\air.png").getImage());

    }


}
