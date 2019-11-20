package com.mycompany.texteditor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class ServerSimulation implements Runnable {

    JTextArea textArea;
    String fileName;
    
    public ServerSimulation(JTextArea textArea)
    {
        this.textArea = textArea;
    }
    
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    //"run" é rodado como thread a cada edição feita no texto, para simular
    //um servidor.
    @Override
    public void run() {
        if(fileName != null)
        {
            String text = textArea.getText();
            try {
                BufferedWriter writer = 
                        new BufferedWriter(new FileWriter(fileName));
                writer.write(text);
                writer.close();
            } catch (IOException ex) {
                System.out.println("Erro ao salvar");
            }
        }else{
            System.out.println("O nome para o arquivo não foi definido");
        }
    }
    
}
