//Name: Vighnesh
//Surname: Chenthil Kumar
//Student ID: 1103842

//package com.howtodoinjava.demo.jsonsimple;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;

public class Server
{
    private static int counterc = 0;
    private static int counterd = 0;
    private static JSONObject dict;

    public static void main(String[] args)
    {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        if (args.length != 2)
        {
            System.out.println("Enter only 2 arguments.");
            System.exit(0);
        }
        int port = 0;
        try
        {
            port = Integer.parseInt(args[0]); //3005
        }
        catch (NumberFormatException e)
        {
            System.out.println(e);
            //e.printStackTrace();
            System.out.println("Enter a valid port number.");
            System.exit(0);
        }
        String filename = args[1];
        try(ServerSocket server = factory.createServerSocket(port))
        {
            JSONParser jsonParser = new JSONParser();
            System.out.println("Waiting for client connection:");
            try (FileReader reader = new FileReader(filename))
            {
                Object obj = jsonParser.parse(reader);
                dict = (JSONObject) obj;
                while (true)
                {
                    Socket client = server.accept();
                    counterc++;
                    System.out.println("Client " + counterc + " has established a connection.");

                    // Open a thread-per-connection
                    Thread t = new Thread(() -> serveClient(client));
                    t.start();
                }
            }
            catch (ParseException e)
            {
                System.out.println(e);
                //e.printStackTrace();
            }
            catch(FileNotFoundException e)
            {
                //e.printStackTrace();
                System.out.println(e);
                System.out.println("Enter a valid dictionary file.");
                System.exit(0);
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
            //e.printStackTrace();
        }
    }

    private static void serveClient(Socket client)//, JSONObject dict)
    {
        counterd++;
        try (Socket clientSocket = client)
        {
            String messages, key, action, meaning;

            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            //JSONParser jsonParser = new JSONParser();
            //try (FileReader reader = new FileReader("dictionary.json"))
            //{
                //Object obj = jsonParser.parse(reader);
                //JSONObject dict = (JSONObject) obj;
            while (true)
            {
                messages = input.readUTF();
                //messages = messages.toLowerCase().trim();
                String[] arrs = messages.split(":");
                action = arrs[0];
                key = arrs[1];
                key = key.toLowerCase().trim();
                    //try(FileReader reader = new FileReader("dictionary.json"))
                    //{
                    //String reader;
                    //Object obj = jsonParser.parse(reader);
                    //JSONObject dict = (JSONObject) obj;
                synchronized (dict)
                {
                    if (action.equals("searchword"))
                    {
                        if (dict.containsKey(key)) {
                            meaning = (String) dict.get(key);
                            output.writeUTF(meaning);
                            output.flush();
                        } else {
                            meaning = "ERR";
                            output.writeUTF(meaning);
                            output.flush();
                        }

                    }
                    if (action.equals("addword"))
                    {
                        String value = arrs[2];
                        if (!dict.containsKey(key)) {
                            dict.put(key, value);
                            meaning = "The word has been added.";
                            output.writeUTF(meaning);
                            output.flush();
                        }
                        else
                            {
                            meaning = "This word already exists in the dictionary.";
                            output.writeUTF(meaning);
                            output.flush();
                        }

                    }
                    if (action.equals("deleteword"))
                    {
                        if (dict.containsKey(key)) {
                            dict.remove(key);
                            meaning = "The word has been removed.";
                            output.writeUTF(meaning);
                            output.flush();
                        } else {
                            meaning = "This word does not exist in the dictionary.";
                            output.writeUTF(meaning);
                            output.flush();
                        }

                    }
                    if (action.equals("exitnow")) {
                        meaning = "Exiting the application.";
                        output.writeUTF(meaning);
                        output.flush();
                        System.out.println("Client " + counterd + " has closed the connection.");
                        input.close();
                        output.close();
                        break;
                    }
                }
            }
            //input.close();
            //output.close();
            //clientSocket.close();
            //}
            //catch (ParseException | FileNotFoundException e)
            //{
            //    e.printStackTrace();
            //}
        }
        catch (IOException e)
        {
            System.out.println(e);
            //e.printStackTrace();
        }
    }
}
