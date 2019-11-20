

package com.mycompany.texteditor;
import java.util.Scanner;
import javax.swing.JTextArea;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 *
 *
 *Editor de texto. (Atual parte: 1)
 *
 *Observa&ccedil;&otilde;es a serem notadas:
 *A classe Text salva um vetor de Strings de tamanho fixo (padr&atilde;o: 10), que &eacute;
 *utilizado para possibilitar o uso da fun&ccedil;&atilde;o UNDO e REDO, sendo o texto principal
 *salvo na primeira posi&ccedil;&atilde;o desse vetor (Isso est&aacute; mais bem explicado nos
 *coment&aacute;rios da classe).
 *
 *Algumas instru&ccedil;&otilde;es:
 *O comando ADD &eacute; chamado com "a*" sendo '*' a string a ser adicionada.
 *Por exemplo: Tenho atualmente o texto "corrija com cari" e dou como input
 *a string "anho", tendo como produto final o texto "corrija com carinho".
 *@author Thiago Daniel Cagnoni de Pauli 10716629 e Pedro Ramos Cunha 10892248
 *
 *@version 1.0
 */
public class TextEditor {

    public static void main(String args[])
    {
        GfxInterface gfxInterface = new GfxInterface();
        gfxInterface.startClient();
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }catch(Exception e)
        {
            
        }     
    }
}
