package com.mycompany.texteditorserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ArrayList<UpdateDocument> clients = new ArrayList<UpdateDocument>();
        Scanner scanner = new Scanner(System.in);
        ServerSocket serverSocket = null;
        System.out.println("Inicializando configuração do servidor...");
        System.out.println("Insira o port a ser utilizado: ");
        int port = scanner.nextInt();
        System.out.println("Abrindo para conexões no port " + port);
        
        try{
            serverSocket = new ServerSocket(port);
        }catch(IOException e)
        {
            System.out.println("Falha ao utilizar o port " + port);
            System.exit(0);
        }
        
        System.out.println("Conexão estabelecida com sucesso.");
        
        while(true)
        {
            System.out.println("Aguardando conexão...");
            Socket socket = serverSocket.accept();
            
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
            UpdateDocument client = new UpdateDocument(input, output, clients);
            clients.add(client);
            Thread t = new Thread(client);
            t.start();
            System.out.println("Cliente conectado");
        }
    }
}
