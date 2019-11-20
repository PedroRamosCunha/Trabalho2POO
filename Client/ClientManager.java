package com.mycompany.texteditor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;



public class ClientManager implements Runnable {
    private ObjectInputStream input;
    private JTextArea textArea;
    private GfxInterface gfxInterface;

    ClientManager(ObjectInputStream input, JTextArea textArea, GfxInterface gfxInterface)
    {
        this.input = input;
        this.textArea = textArea;
        this.gfxInterface = gfxInterface;
    }

    @Override
    public void run() {
        while(!Thread.interrupted())
        {
            try {
                this.gfxInterface.setNeedsUpdate(true);
                String text = input.readUTF();
                this.gfxInterface.setNeedsUpdate(false);
                int caretPosition = textArea.getCaretPosition();
                if(caretPosition > textArea.getDocument().getLength())
                    caretPosition = textArea.getDocument().getLength();
                textArea.setText(text);
                textArea.setCaretPosition(caretPosition);
            } catch (IOException ex) {
                System.out.println("Erro ao receber mensagem do servidor... :(");
                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
