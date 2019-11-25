//Name: Vighnesh
//Surname: Chenthil Kumar
//Student ID: 1103842

import com.sun.tools.javac.comp.Enter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientSide extends JDialog
{
    private JPanel contentPane;
    private JButton SearchWord;
    private JButton QuitGUI;
    private JButton AddWord;
    private JButton DeleteWord;
    private JTextField EnterWord;
    //private JTextField EnterMeaning;
    private JLabel Status;
    private JLabel enterYourWordLabel;
    private JLabel meaningLabel;
    private JTextArea EnterMeaning;
    private static String message_s, message_a, message_d, message_q;

    private static String operate(String m, DataInputStream i, DataOutputStream o) throws IOException
    {
        String messagec;
        //m = m.toLowerCase().trim();
        //if (m.equals("searchword:") || m.equals("deleteword:"))
        //{
        //    messagec = "ERR1";
        //    return messagec;
        //}
        //String[] arr = m.split(":");
        //if (arr[0].equals("searchword"))
        //{
        o.writeUTF(m);
        o.flush();
            //while(true)
            //{
        messagec = i.readUTF();
            //return messagec;
            //break;
            //}
        //}
        //if (arr[0].equals("exitnow"))
        //{
        //    o.writeUTF(m);
        //    o.flush();
        //    messagec = i.readUTF();
        //}
        //return messagec;
        return messagec;
    }

    public ClientSide(DataInputStream in, DataOutputStream ou)
    {

        setContentPane(contentPane);
        setModal(true);
        SearchWord.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String entered_word = EnterWord.getText();
                entered_word = entered_word.toLowerCase().trim();
                if (entered_word.equals("") || entered_word.matches("(.*)[^A-Za-z]+(.*)"))
                {
                    EnterMeaning.setText("");
                    JOptionPane.showMessageDialog(null, "Enter a valid word.");
                    //Status.setText("Enter a valid word.");
                    //EnterWord.setText("");
                    //EnterMeaning.setText("");
                }
                else
                {
                    message_s = "searchword:" + entered_word;
                    try
                    {
                        String messagecr = operate(message_s, in, ou);
                        if (messagecr.equals("ERR"))
                        {
                            EnterMeaning.setText("");
                            JOptionPane.showMessageDialog(null, "This word does not exist in the dictionary.");
                            //Status.setText("This word does not exist.");
                            //EnterMeaning.setText("");
                        }
                        else
                        {
                            //Status.setText("");
                            EnterMeaning.setText(messagecr);
                        }
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex);
                        //ex.printStackTrace();
                    }
                }
            }
        });
        AddWord.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String entered_word = EnterWord.getText();
                entered_word = entered_word.toLowerCase().trim();
                String entered_meaning = EnterMeaning.getText();
                entered_meaning = entered_meaning.trim();
                if (entered_meaning.equals("") || entered_word.equals("") || entered_word.matches("(.*)[^A-Za-z]+(.*)"))
                {
                    JOptionPane.showMessageDialog(null, "Enter a valid word/meaning(s).");
                    //Status.setText("Enter a word/meaning(s).");
                }
                else {
                    message_a = "addword:" + entered_word + ":" + entered_meaning;
                    try
                    {
                        String messagecr = operate(message_a, in, ou);
                        JOptionPane.showMessageDialog(null, messagecr);
                        //Status.setText(messagecr);
                        //EnterMeaning.setText(messagecr);
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex);
                        //ex.printStackTrace();
                    }
                }
            }
        });
        DeleteWord.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String entered_word = EnterWord.getText();
                entered_word = entered_word.toLowerCase().trim();
                if (entered_word.equals("") || entered_word.matches("(.*)[^A-Za-z]+(.*)")) //entered_word.equals("") ||
                {
                    EnterMeaning.setText("");
                    JOptionPane.showMessageDialog(null, "Enter a valid word.");
                    //Status.setText("Enter a word.");
                    //EnterWord.setText("");
                }
                else
                {
                    message_d = "deleteword:" + entered_word;
                    try
                    {
                        String messagecr = operate(message_d, in, ou);
                        //EnterMeaning.setText(messagecr);
                        EnterMeaning.setText("");
                        JOptionPane.showMessageDialog(null, messagecr);
                        //Status.setText(messagecr);
                        //EnterMeaning.setText("");
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex);
                        //ex.printStackTrace();
                    }
                }

            }
        });
        QuitGUI.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                message_q = "exitnow:please";
                try
                {
                    String messagecr = operate(message_q, in, ou);
                    EnterWord.setText("");
                    EnterMeaning.setText("");
                    JOptionPane.showMessageDialog(null, messagecr);
                    //Status.setText(messagecr);
                    //EnterMeaning.setText("");
                    //wait();
                }
                catch (IOException ex)
                {
                    System.out.println(ex);
                    //ex.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Enter only 2 arguments.");
            System.exit(0);
        }
        int port = 0;
        try
        {
            port = Integer.parseInt(args[1]); //3005
        }
        catch (NumberFormatException e)
        {
            System.out.println(e);
            //e.printStackTrace();
            System.out.println("Enter a valid port number.");
            System.exit(0);
        }
        String ip = args[0];
        try(Socket socket = new Socket(ip, port);)
        {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            ClientSide dialog = new ClientSide(input, output);
            dialog.pack();
            dialog.setVisible(true);
            //dialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
        catch (UnknownHostException ex)
        {
            System.out.println(ex);
            //e.printStackTrace();
            System.out.println("Enter a valid IP Address.");
            System.exit(0);
        }
        catch (IOException e)
        {
            System.out.println(e);
            //e.printStackTrace();
        }
    }

    //private void createUIComponents() {
        // TODO: place custom component creation code here
    //}
}
