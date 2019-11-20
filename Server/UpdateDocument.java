package com.mycompany.texteditorserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateDocument implements Runnable {
    ObjectInputStream input = null;
    ObjectOutputStream output = null;
    String fileName = null;
    ArrayList<UpdateDocument> clients = null;
    
    public UpdateDocument(ObjectInputStream input, ObjectOutputStream output, ArrayList<UpdateDocument> clients)
    {
        this.clients = clients;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        while(true)
        {
            int opt = 0;
            try {
                opt = input.readInt();
            } catch (IOException ex) {
                try {
                    System.out.println("Removendo cliente");
                    input.close();
                    output.close();
                    clients.remove(this);
                } catch (IOException ex1) {
                    Logger.getLogger(UpdateDocument.class.getName()).log(Level.SEVERE, null, ex1);
                }finally{
                    return;
                }
            }
            
            if(opt == 1)//Muda o arquivo sendo editado
            {
                System.out.print("Editando nome de " + fileName + " para ");
                try {
                    fileName = input.readUTF();
                    System.out.println(fileName);
                } catch (IOException ex) {
                    Logger.getLogger(UpdateDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    String contents = new String(Files.readAllBytes(Paths.get(fileName)));
                    output.writeUTF(contents);
                    output.flush();
                } catch (IOException ex) {
                    System.out.println("Arquivo não encontrado. criando...");
                }
            }
            
            if(opt == 2)//Recebe texto editado e salva
            {
                System.out.println("Recebido mensagem para o arquivo " + fileName);
                try {
                    String text = input.readUTF();
                    BufferedWriter writer = null;
                    if(this.fileName != null)
                        writer = new BufferedWriter(new FileWriter(this.fileName));
                    writer.write(text);
                    writer.close();
                    for(UpdateDocument client : clients)
                    {
                        if(client != this && this.fileName.equals(client.getFileName()) && client.getFileName() != null)
                        {
                            client.output.writeUTF(text);
                            client.output.flush();
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(UpdateDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public String getFileName()
    {
        return fileName;
    }
}
