package it.fi.itismeucci;

import java.io.*;
import java.net.*;

public class SocketThread extends Thread{

    protected final Server server;
    protected final Socket client;

    public SocketThread(Socket client, Server server){
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        try{
            for(;;){
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                String recv = in.readLine();
                if(recv.equals("FINE")){
                    System.out.println("\nConnessione terminata");
                    out.writeBytes("\n"); 
                    client.close();
                    server.clients.remove(client);
                    break;
                }
                else if (recv.equals("SPEGNI"))
                {
                    server.serverPort.close();
                    out.writeBytes("\n"); 

                    for (Socket s : server.clients)
                    {
                        try
                        {
                            s.close();
                        } catch (Exception ex) { }
                    }

                    server.clients.clear();
                    break;
                }
                System.out.print("\nStringa ricevuta: " + recv);
                String modifiedRecv = recv.toUpperCase();
                out.writeBytes(modifiedRecv + '\n');        
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
}
