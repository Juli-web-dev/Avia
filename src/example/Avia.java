package example;

import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class Avia extends JFrame {
    public JPanel Panell;
    Connection connection = null;
    PreparedStatement pst = null;
    PreparedStatement psst = null;
    int a;
    String CityFrom;
    String CityWhere;
    String DepartDate;
    String Passengers;
    String Surname;
    String Name;
    String Birthday;
    String Valid;
    String Tel;
    String Pass;
    String Email;
    String Gender;
    String Choose_time;
    String Nationality;
    String CountryIssue;
    int TripId;
    String Seat;
    String Stat;
    String PATHH = "C:\\Users\\Juliasha\\IdeaProjects\\Avia\\src\\tickets\\";
    String file;
    String next_date;
    String[] ticket = new String[17];
    String ArrivalDate;
    String ArrivalTime;
    String FlightTime;
    String FlightNumber;
    String PlaneType;
    int PlaceId;
    int TicketId;
    int count;
    int passcount;
    int all_price;
    ArrayList<Integer> ticketsId = new ArrayList<>();
    ArrayList<Integer> placesId = new ArrayList<>();
    String DataNow;
    private JTable table1;
    private JTextField textFrom;
    private JTextField textTo;
    private JFormattedTextField textDeparture;
    private JTextField textPassengers;
    private JButton buttonSearch;
    private JComboBox cmbTimeSelect;
    private JButton buttonConfirm;
    private JLabel tmLabel;
    private JPanel panel1;
    private JButton backbutton;
    private JRadioButton MradioButton;
    private JRadioButton WradioButton;
    private JTextField textSurname;
    private JTextField textName;
    private JComboBox cmbNationality;
    private JComboBox cmbCountryIssue;
    private JFormattedTextField textValid;
    private JFormattedTextField textBirthday;
    private JTextField textTel;
    private JTextField textPass;
    private JTextField textEmail;
    private JButton buttonSave;
    private JLabel lblPassengers;
    private JCheckBox CheckTerms;
    private JComboBox cmbPlace;
    private JButton cancelButton;
    private JButton buyButton;
    private JLabel TotalPrice;
    private JScrollPane scroll;

    public Avia() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(Panell);
        setVisible(true);
        scroll.setVisible(false);
        MradioButton.setFocusPainted(false);
        WradioButton.setFocusPainted(false);
        buttonSearch.setFocusPainted(false);
        backbutton.setFocusPainted(false);
        buttonSave.setFocusPainted(false);
        buttonConfirm.setFocusPainted(false);
        CheckTerms.setFocusPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setVisible(false);
        buyButton.setFocusPainted(false);
        table1.setVisible(false);
        panel1.setVisible(false);
        cmbTimeSelect.setVisible(false);
        buttonConfirm.setVisible(false);
        backbutton.setVisible(false);
        buyButton.setVisible(false);
        TotalPrice.setVisible(false);

        //Zeitformatmaske
        try {
            MaskFormatter maskFormatter = new MaskFormatter("####-##-##");
            maskFormatter.setPlaceholderCharacter('0');
            maskFormatter.install(textDeparture);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            MaskFormatter maskFormatter = new MaskFormatter("####-##-##");
            maskFormatter.setPlaceholderCharacter('0');
            maskFormatter.install(textBirthday);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            MaskFormatter maskFormatter = new MaskFormatter("####-##-##");
            maskFormatter.setPlaceholderCharacter('0');
            maskFormatter.install(textValid);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        ((AbstractDocument) textFrom.getDocument()).setDocumentFilter(new MyDocumentFilter());
        ((AbstractDocument) textTo.getDocument()).setDocumentFilter(new MyDocumentFilter());
        ((AbstractDocument) textPassengers.getDocument()).setDocumentFilter(new DigitFilter());


        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Speicherung von Daten in Variablen aus einem Textfeld
                CityFrom = textFrom.getText();
                CityWhere = textTo.getText();
                DepartDate = textDeparture.getText();
                Passengers = textPassengers.getText();

                boolean t1 = true;
                boolean t2 = true;
                boolean t3 = true;
                boolean t4 = true;

               // Überprüfung der Feldausfüllung

                if (CityFrom.isEmpty() || CityFrom.equals("") || CityFrom == null) {
                    textFrom.setBackground(new Color(140, 0, 15));
                    t1 = false;
                }
                if (CityWhere.isEmpty() || CityWhere.equals("") || CityWhere == null) {
                    textTo.setBackground(new Color(140, 0, 15));
                    t2 = false;
                }
                if (DepartDate.isEmpty() || DepartDate.equals("") || DepartDate == null || DepartDate.equals("0000-00-00")) {
                    textDeparture.setBackground(new Color(140, 0, 15));
                    t3 = false;
                }
                if (Passengers.isEmpty() || Passengers.equals("") || Passengers == null || Passengers.equals("0")) {
                    textPassengers.setBackground(new Color(140, 0, 15));
                    t4 = false;
                }
                if (t1 && t2 && t3 && t4) {

                    textFrom.setBackground(new Color(44, 88, 201));
                    textTo.setBackground(new Color(44, 88, 201));
                    textDeparture.setBackground(new Color(44, 88, 201));
                    textPassengers.setBackground(new Color(44, 88, 201));

                    //konstante Passagierzahl, die sich nicht ändert
                    count = Integer.parseInt(Passengers);

                    // die Zahl der Passagiere nimmt ab
                    a = Integer.parseInt(Passengers);

                    //Ausgabe der Flugtabelle
                    try {
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/luftjet", "root", "root");
                        String sql = "SELECT DISTINCT`departure_date` as `Date departure`,`departure_time` as `Time departure`,`arrival_date` as `Date arrival`," +
                                "`arrival_time` as `Time arrival`,`flight_time` as `Flight duration`,`flight_number` as `Flight number`,`name`as `Plane type`," +
                                "`free_places` as `Free places`,`price` as `Price` FROM trip JOIN plane on trip.trip_id = plane.plane_id " +
                                "JOIN place ON trip.trip_id = place.trip_id JOIN price ON place.price_id = price.price_id" +
                                " WHERE town_from = '" + CityFrom + "' AND town_to = '" + CityWhere + "' AND `departure_date` = '" + DepartDate + "'" +
                                " AND '" + a + "' <= `free_places` ORDER BY `Time departure` ASC ";

                        //Der Codeabschnitt dient dazu, eine vorbereitete Anweisung (PreparedStatement) für eine SQL-Abfrage mit bestimmten Parametern zu erstellen.
                        //Die Methode prepareStatement wird verwendet, um eine SQL-Anweisung vorzubereiten, die in der Variablen sql gespeichert ist.
                        // Das ResultSet kann verwendet werden, um durch die Ergebnisdaten zu navigieren und Aktualisierungen in der Datenbank vorzunehmen.

                        pst = connection.prepareStatement(sql,
                                ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);

                        ResultSet rs = pst.executeQuery();

                        //Die Bedingung if (rs.next()) prüft, ob es mindestens einen Eintrag im ResultSet (rs) gibt.
                        //rs.next() bewegt den Cursor des ResultSets zum nächsten Eintrag und gibt true zurück, wenn ein weiterer Eintrag vorhanden ist, andernfalls false.
                        if (rs.next()) {
                            rs.beforeFirst();
                            do {
                                backbutton.setVisible(true);
                                table1.setVisible(true);
                                cmbTimeSelect.setVisible(true);
                                buttonConfirm.setVisible(true);
                                scroll.setVisible(true);
                                textFrom.setEnabled(false);
                                textTo.setEnabled(false);
                                textDeparture.setEnabled(false);
                                textPassengers.setEnabled(false);
                                buttonSearch.setEnabled(false);


                                table1.getTableHeader().setReorderingAllowed(false);
                                table1.setModel(DbUtils.resultSetToTableModel(rs));
                                table1.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));


                                for (int x = 0; x < 9; x++) {
                                    table1.getColumnModel().getColumn(x).setPreferredWidth(20);//Tabellenspaltenbreite
                                    table1.getColumnModel().getColumn(x).setResizable(false);//Spaltenskalierung
                                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();//Säulenzentrierung
                                    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                                    table1.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);

                                }


                                Fillcombo();


                            } while (rs.next());

                        } else {
                            panel1.setVisible(false);
                            JOptionPane.showMessageDialog(null, "No results were found for this request", "Error!", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please check the entered data is correct", "Error!", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        //ActionListener ist ein Interface in Java, das verwendet wird, um auf Ereignisse von Aktionen zu reagieren, die von Benutzern ausgelöst werden.
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbTimeSelect.getSelectedItem() == null) {
                    cmbTimeSelect.setBackground(new Color(140, 0, 15));
                    JOptionPane.showMessageDialog(null, "Choose time", "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    Choose_time = cmbTimeSelect.getSelectedItem().toString();
                    cmbTimeSelect.setBackground(new Color(44, 88, 201));
                    panel1.setVisible(true);
                    table1.setVisible(false);
                    scroll.setVisible(false);
                    cmbTimeSelect.setVisible(false);
                    buttonConfirm.setVisible(false);
                    backbutton.setVisible(true);
                    backbutton.setVisible(true);
                    tmLabel.setText("Date: " + DepartDate + " Time: " + cmbTimeSelect.getSelectedItem().toString());

                    buttonSearch.setVisible(false);
                    textFrom.setEnabled(false);
                    textTo.setEnabled(false);
                    textPassengers.setEnabled(false);
                    textDeparture.setEnabled(false);


                    // Trip-ID erhaltung
                    String SQL = "SELECT `trip_id` FROM trip  WHERE town_from = '" + CityFrom + "' AND town_to = '" + CityWhere + "'" +
                            " AND `departure_date` >= '" + DepartDate + "' AND `departure_time` = '" + Choose_time + "'";
                    try {
                        ResultSet RS = connection.createStatement().executeQuery(SQL);
                        if (RS.next()) {
                            TripId = RS.getInt("trip_id"); /// für insert und place ausw.

                        }

                    } catch (SQLException ex) {

                        ex.printStackTrace();
                    }


                    Fillplace();

                }

            }
        });
        backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table1.setVisible(false);
                scroll.setVisible(false);
                panel1.setVisible(false);
                backbutton.setVisible(false);
                buttonSearch.setVisible(true);
                buttonSearch.setEnabled(true);
                textFrom.setEnabled(true);
                textTo.setEnabled(true);
                textPassengers.setEnabled(true);
                textDeparture.setEnabled(true);

                MradioButton.setSelected(false);
                WradioButton.setSelected(false);
                textSurname.setText(null);
                textName.setText(null);
                textBirthday.setText(null);
                textBirthday.setText(null);
                textTel.setText(null);
                textEmail.setText(null);
                textPass.setText(null);
                cmbNationality.setSelectedItem("Russia");
                cmbCountryIssue.setSelectedItem("Russia");
                textValid.setText(null);
                CheckTerms.setSelected(false);
                table1.setModel(new DefaultTableModel());
                cmbTimeSelect.removeAllItems();
                cmbTimeSelect.setVisible(false);
                buttonConfirm.setVisible(false);
                cmbPlace.removeAllItems();

                textSurname.setBackground(new Color(44, 88, 201));
                textName.setBackground(new Color(44, 88, 201));
                textBirthday.setBackground(new Color(44, 88, 201));
                textTel.setBackground(new Color(44, 88, 201));
                textValid.setBackground(new Color(44, 88, 201));
                textEmail.setBackground(new Color(44, 88, 201));
                textPass.setBackground(new Color(44, 88, 201));
                cmbNationality.setBackground(new Color(44, 88, 201));
                cmbCountryIssue.setBackground(new Color(44, 88, 201));
                WradioButton.setForeground(new Color(255, 255, 255));
                MradioButton.setForeground(new Color(255, 255, 255));
                CheckTerms.setForeground(new Color(255, 255, 255));


            }
        });
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Surname = textSurname.getText();
                Name = textName.getText();
                Birthday = textBirthday.getText();
                Valid = textValid.getText();
                Tel = textTel.getText();
                Pass = textPass.getText();
                Email = textEmail.getText();
                Seat = cmbPlace.getSelectedItem().toString();
                passcount = 0;


                boolean t1 = true;
                boolean t2 = true;
                boolean t3 = true;
                boolean t4 = true;
                boolean t5 = true;
                boolean t6 = true;
                boolean t7 = true;
                boolean t8 = true;
                boolean t9 = true;


                if (Surname.isEmpty() || Surname.equals("") || Surname == null) {
                    textSurname.setBackground(new Color(140, 0, 15));
                    t1 = false;
                }
                if (Name.isEmpty() || Name.equals("") || Name == null) {
                    textName.setBackground(new Color(140, 0, 15));
                    t2 = false;
                }
                if (Birthday.isEmpty() || Birthday.equals("") || Birthday == null || Birthday.equals("0000-00-00")) {
                    textBirthday.setBackground(new Color(140, 0, 15));
                    t3 = false;
                }
                if (Valid.isEmpty() || Valid.equals("") || Valid == null || Valid.equals("0000-00-00")) {
                    textValid.setBackground(new Color(140, 0, 15));
                    t4 = false;
                }
                if (Tel.isEmpty() || Tel.equals("") || Tel == null) {
                    textTel.setBackground(new Color(140, 0, 15));
                    t5 = false;
                }
                if (Email.isEmpty() || Email.equals("") || Email == null) {
                    textEmail.setBackground(new Color(140, 0, 15));
                    t6 = false;
                }
                if (Pass.isEmpty() || Pass.equals("") || Pass == null) {
                    textPass.setBackground(new Color(140, 0, 15));
                    t7 = false;
                }
                if (!MradioButton.isSelected() && !WradioButton.isSelected()) {
                    MradioButton.setForeground(new Color(140, 0, 15));
                    WradioButton.setForeground(new Color(140, 0, 15));
                    t8 = false;

                }
                if (!CheckTerms.isSelected()) {
                    CheckTerms.setForeground(new Color(140, 0, 15));
                    t9 = false;

                }

                if (CheckTerms.isSelected()) {

                    if (a > 1) {
                        lblPassengers.setText("Enter next Passenger");
                    }
                    if (MradioButton.isSelected()) {
                        Gender = "Men";
                    }
                    if (WradioButton.isSelected()) {
                        Gender = "Women";
                    }

                    if (t1 || t2 || t3 || t4 || t5 || t6 || t7 || t8 || t9) {
                        Nationality = cmbNationality.getSelectedItem().toString();
                        CountryIssue = cmbCountryIssue.getSelectedItem().toString();
                        a = a - 1; //Ein Passagier checkte ein
                        backbutton.setVisible(false);
                        buttonSearch.setVisible(false);
                        CheckTerms.setForeground(new Color(255, 255, 255));
                        MradioButton.setForeground(new Color(255, 255, 255));
                        WradioButton.setForeground(new Color(255, 255, 255));
                        cancelButton.setVisible(true);

                        //Hinzufügung eines Passagiers zur Datenbank
                        try {

                            String sql = "INSERT INTO passenger(`name`,`surname`,`gender`,`birthday`,`passport_nr`,`expire_date`,`telephone`,`email`,`country`,`nationality`)" +
                                    " VALUE (?,?,?,?,?,?,?,?,?,?)";

                            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/luftjet", "root", "root");
                            psst = connection.prepareStatement(sql);
                            Stat = "Not registered";


                            psst.setString(1, Name);
                            psst.setString(2, Surname);
                            psst.setString(3, Gender);
                            psst.setString(4, Birthday);
                            psst.setString(5, Pass);
                            psst.setString(6, Valid);
                            psst.setString(7, Tel);
                            psst.setString(8, Email);
                            psst.setString(9, Objects.requireNonNull(cmbCountryIssue.getSelectedItem()).toString());
                            psst.setString(10, Objects.requireNonNull(cmbNationality.getSelectedItem()).toString());


                            psst.executeUpdate();

                            JOptionPane.showInternalMessageDialog(null, "Passenger added!");

                        } catch (Exception ex) {
                            JOptionPane.showInternalMessageDialog(null, ex);
                        }

                        //Ticketinformationen erhaltung
                        try {

                            String sql1 = "SELECT *  FROM trip JOIN plane ON trip.plane_id = plane.plane_id WHERE town_from = '" + CityFrom + "'" +
                                    " AND town_to = '" + CityWhere + "' AND `departure_date` = '" + DepartDate + "' AND `departure_time` = '" + Choose_time + "' ";

                            pst = connection.prepareStatement(sql1);
                            ResultSet rs1 = pst.executeQuery();

                            while (rs1.next()) {
                                ArrivalDate = rs1.getString("arrival_date");
                                ArrivalTime = rs1.getString("arrival_time");
                                FlightTime = rs1.getString("flight_time");
                                FlightNumber = rs1.getString("flight_number");
                                PlaneType = rs1.getString("plane.name");
                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }

                        // Erstellung den Ticket in der Datenbank
                        try {
                            Date dateNow = new Date();
                            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            DataNow = formatForDateNow.format(dateNow);

                            String sqlll = "INSERT INTO ticket (date, status, trip_id, place_id, price_id, passenger_id , buydate)  " +
                                    "VALUE  ('" + DepartDate + "', '" + Stat + "', '" + TripId + "', (SELECT place_id from place WHERE place = '" + Seat + "' AND trip_id =  '" + TripId + "')," +
                                    " (SELECT price_id FROM place WHERE place = '" + Seat + "' AND trip_id =  '" + TripId + "')," +
                                    " (SELECT passenger_id FROM passenger WHERE passport_nr = '" + Pass + "' AND name = '" + Name + "' AND surname = '" + Surname + "'),'" + DataNow + "' )";


                            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/luftjet", "root", "root");
                            psst = connection.prepareStatement(sqlll);

                            psst.executeUpdate();

                            // Füllung des Ticket-Arrays
                            ticket[0] = ("\nPassenger Name: " + Name + " ");
                            ticket[1] = (Surname + "\n");
                            ticket[2] = ("Date of Birth: " + Birthday + "\n");
                            ticket[3] = ("Passport Nr: " + Pass + "\n");
                            ticket[4] = ("\nE-mail: " + Email + "\n");
                            ticket[5] = ("Telephone: " + Tel + "\n");
                            ticket[6] = ("\nSeat: " + Seat + "\n");
                            ticket[7] = ("Where from: " + CityFrom + "\n");
                            ticket[8] = ("Where to: " + CityWhere + "\n");
                            ticket[9] = ("\nDate Departure: " + DepartDate + "\n");
                            ticket[10] = ("Time Departure: " + Choose_time + "\n");
                            ticket[11] = ("Date Arrival: " + ArrivalDate + "\n");
                            ticket[12] = ("\nTime Arrival: " + ArrivalTime + "\n");
                            ticket[13] = ("Flight Time: " + FlightTime + "\n");
                            ticket[14] = ("\nFlight Number: " + FlightNumber + "\n");
                            ticket[15] = ("Plane Type: " + PlaneType + "\n");
                            ticket[16] = ("Purchase Date " + DataNow + "\n");

                            // Ticket speicherung
                            file = (Surname + "_" + Name + "_" + Seat + ".txt");
                            PATHH = PATHH + file;
                            PrintTicket(ticket, PATHH);
                            file = null;
                            PATHH = "C:\\Users\\Juliasha\\IdeaProjects\\Avia\\src\\tickets\\";


                        } catch (Exception ex) {
                            JOptionPane.showInternalMessageDialog(null, ex);
                        }


                        //Entfernung nicht registrierter Passagiere
                        try {
                            String sqld = "SELECT *  FROM ticket WHERE `status` = ('Not registered')  ";

                            pst = connection.prepareStatement(sqld);
                            ResultSet rs1 = pst.executeQuery();

                            while (rs1.next()) {

                                String sqqqql = "DELETE FROM ticket WHERE `status` = ('Not registered') AND DATE_ADD( buydate, INTERVAL 30 MINUTE) <= '" + DataNow + "' ";

                                try {
                                    pst = connection.prepareStatement(sqqqql);
                                    pst.executeUpdate();

                                } catch (Exception ex) {
                                    JOptionPane.showInternalMessageDialog(null, ex);
                                }

                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }

                        // Sitzplatz-ID erhaltung
                        try {
                            String sql1 = "SELECT *  FROM place WHERE trip_id = '" + TripId + "' AND place = '" + Seat + "' ";

                            pst = connection.prepareStatement(sql1);
                            ResultSet rs1 = pst.executeQuery();

                            while (rs1.next()) {
                                PlaceId = rs1.getInt("place_id"); // für Platzstatus änderung

                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }


                        // Ticket-ID erhaltung

                        try {

                            String sqql = "SELECT * FROM ticket  WHERE status = 'Not registered' AND place_id = '" + PlaceId + "' AND trip_id = '" + TripId + "'";

                            pst = connection.prepareStatement(sqql);
                            ResultSet rs1 = pst.executeQuery();

                            while (rs1.next()) {
                                TicketId = rs1.getInt("ticket_id");

                            }

                        } catch (Exception ex) {
                            JOptionPane.showInternalMessageDialog(null, ex);
                        }

                            //Vergleich der Anzahl der hinzugefügten und deklarierten Passagiere

                        if (passcount <= count) {
                            ticketsId.add(passcount, TicketId);
                                                                // Füllung von Arrays. Zwei Arrays speichern IDs von Sitzplätzen und Tickets, für die Update erforderlich ist.
                            placesId.add(passcount, PlaceId);

                            passcount++;

                        }

                        MradioButton.setSelected(false);
                        WradioButton.setSelected(false);
                        textSurname.setText(null);
                        textName.setText(null);
                        textBirthday.setText(null);
                        textTel.setText(null);
                        textEmail.setText(null);
                        textPass.setText(null);
                        cmbNationality.setSelectedItem("Russia");
                        cmbCountryIssue.setSelectedItem("Russia");
                        textValid.setText(null);
                        CheckTerms.setSelected(false);


                        ////////////////////////////////////////////////////////////////////////
                        textSurname.setBackground(new Color(44, 88, 201));
                        textName.setBackground(new Color(44, 88, 201));
                        textBirthday.setBackground(new Color(44, 88, 201));
                        textTel.setBackground(new Color(44, 88, 201));
                        textValid.setBackground(new Color(44, 88, 201));
                        textEmail.setBackground(new Color(44, 88, 201));
                        textPass.setBackground(new Color(44, 88, 201));
                        cmbNationality.setBackground(new Color(44, 88, 201));
                        cmbCountryIssue.setBackground(new Color(44, 88, 201));
                        WradioButton.setForeground(new Color(255, 255, 255));
                        MradioButton.setForeground(new Color(255, 255, 255));
                        CheckTerms.setForeground(new Color(255, 255, 255));


                        /////////////////////////////////////////////////////////////////////////

                    } else {
                        JOptionPane.showMessageDialog(null, "Please check the entered data is correct", "Error!", JOptionPane.ERROR_MESSAGE);

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Please agree to the therms and conditions", "Error!", JOptionPane.ERROR_MESSAGE);

                    CheckTerms.setForeground(new Color(140, 0, 15));
                }
                //wenn es keine  registrierten Passagiere gibt
                if (a <= 0) {
                    JOptionPane.showInternalMessageDialog(null, "REGISTER SUCCESSFULLY!:)<3");

                    panel1.setVisible(false);
                    table1.setVisible(false);
                    scroll.setVisible(false);
                    buyButton.setVisible(true);
                    TotalPrice.setVisible(true);

                   //Die Gesamtkosten werden angezeigt

                    String sql = "SELECT DISTINCT `price` FROM trip  JOIN place ON trip.trip_id = place.trip_id JOIN price ON place.price_id = price.price_id" +
                            " WHERE town_from = '" + CityFrom + "' AND town_to = '" + CityWhere + "' AND `departure_date` >= '" + DepartDate + "'" +
                            "AND  `departure_date` <= '" + next_date + "' AND '" + a + "' <= `free_places`";

                    try {
                        ResultSet rs = connection.createStatement().executeQuery(sql);

                        if (rs.next()) {
                            all_price = rs.getInt("price");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    all_price = all_price * count;
                    TotalPrice.setText("Book for: " + all_price + " Eur (for all passengers)");

                }

            }
        });
        MradioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MradioButton.isSelected()) {
                    WradioButton.setSelected(false);
                }
            }
        });
        WradioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (WradioButton.isSelected()) {
                    MradioButton.setSelected(false);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                table1.setVisible(false);
                scroll.setVisible(false);
                panel1.setVisible(false);
                backbutton.setVisible(false);
                buttonSearch.setVisible(true);
                buttonSearch.setEnabled(true);
                cancelButton.setVisible(false);
                buyButton.setVisible(false);
                textFrom.setEnabled(true);
                textTo.setEnabled(true);
                TotalPrice.setVisible(false);
                textPassengers.setEnabled(true);
                textDeparture.setEnabled(true);


                MradioButton.setSelected(false);
                WradioButton.setSelected(false);
                textSurname.setText(null);
                textName.setText(null);
                textBirthday.setText(null);
                textBirthday.setText(null);
                textTel.setText(null);
                textEmail.setText(null);
                textPass.setText(null);
                cmbNationality.setSelectedItem("Russia");
                cmbCountryIssue.setSelectedItem("Russia");
                textValid.setText(null);
                CheckTerms.setSelected(false);
                table1.setModel(new DefaultTableModel());
                cmbTimeSelect.removeAllItems();
                cmbTimeSelect.setVisible(false);
                buttonConfirm.setVisible(false);
                cmbPlace.removeAllItems();
            }
        });

        //Kaufbestätigungsschaltfläche
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < ticketsId.size(); i++) {

                    int valueticketId = ticketsId.get(i);
                    int valueplaceId = placesId.get(i);

                    // Ändert den Ticketstatus nach dem Kauf
                    String sqql = "UPDATE ticket SET `status` = ('Registered') WHERE status = 'Not registered' AND ticket_id = '" + valueticketId + "'";

                    try {
                        pst = connection.prepareStatement(sqql);
                        pst.executeUpdate();

                    } catch (Exception ex) {
                        JOptionPane.showInternalMessageDialog(null, ex);
                    }

                   // Ändert den Sitzstatus nach dem Kauf

                    String sqlup = "UPDATE place SET `status` = 1 WHERE status = 0 AND place_id = '" + valueplaceId + "'";

                    try {
                        pst = connection.prepareStatement(sqlup);
                        pst.executeUpdate();

                    } catch (Exception ex) {
                        JOptionPane.showInternalMessageDialog(null, ex);
                    }
                }

                //Reduzierung der freien Plätze nach dem Kauf

                String sqlpl = "UPDATE trip SET `free_places` =(`free_places` - '" + count + "')   WHERE trip_id = '" + TripId + "'  ";

                try {
                    pst = connection.prepareStatement(sqlpl);
                    pst.executeUpdate();

                } catch (Exception ex) {
                    JOptionPane.showInternalMessageDialog(null, ex);
                }

                JOptionPane.showInternalMessageDialog(null, "Successfully!");
                System.exit(0);
            }

        });

    }

    //Füllung der Combobox mit den Abflugszeiten
    private void Fillcombo() {
        try {
            String sql1 = "SELECT * FROM trip  WHERE town_from = '" + CityFrom + "' AND town_to = '" + CityWhere + "' " +
                    "AND `departure_date` = '" + DepartDate + "' AND '" + a + "' <= `free_places`";

            pst = connection.prepareStatement(sql1);
            ResultSet rs1 = pst.executeQuery();

            cmbTimeSelect.removeAllItems();
            while (rs1.next()) {
                String time_departure = rs1.getString("departure_time");
                cmbTimeSelect.addItem(time_departure);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    //Füllung der Combobox mit Sitzplätzen
    private void Fillplace() {

        try {
            String sql1 = "SELECT place,place_id FROM place WHERE `trip_id` = '" + TripId + "' AND status = 0";

            pst = connection.prepareStatement(sql1);
            ResultSet rs1 = pst.executeQuery();

            cmbPlace.removeAllItems();
            while (rs1.next()) {
                String place = rs1.getString("place");
                cmbPlace.addItem(place);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    //Füllung eines Texdokuments(Tickets) aus Arrays (Sting to Bytes)
    private void PrintTicket(String[] ticket, String PT) {
        Path path = Path.of(PT);
        try {

            StringBuilder sb = new StringBuilder();
            for (String ch : ticket) {
                sb.append(ch);
            }
            String string = sb.toString();

            byte[] bs = string.getBytes();
            Path writtenFilePath = Files.write(path, bs);
            new String(Files.readAllBytes(writtenFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Ermöglicht nur Zahlen zu schreiben
    class DigitFilter extends DocumentFilter {
        private static final String DIGITS = "\\d+";

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches(DIGITS)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            if (string.matches(DIGITS)) {
                super.replace(fb, offset, length, string, attrs);
            }
        }
    }

    //verbietet das Schreiben von Zahlen
    class MyDocumentFilter extends example.MyDocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            string = string.replaceAll("[1234567890]", "");
            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            text = text.replaceAll("[1234567890]", "");
            super.replace(fb, offset, length, text, attrs);
        }
    }

}

