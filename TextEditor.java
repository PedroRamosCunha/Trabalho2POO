

package com.mycompany.texteditor;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
        gfxInterface.startMainWindow();
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }catch(Exception e)
        {
            System.exit(0);
        }
        
        
        Text text = new Text();

        Cursor cursor = new Cursor();//Cursor utilizado pelo usuário.

        String fullOption;//Pega o input completo, se necessário.
        /**
         * Char que define a op&ccedil;&atilde;o a ser utilizada.
         */
        char option;//Char que define a opção a ser utilizada.
        /**
         * Define quando sair.
         */
        boolean quit = false;//Define quando sair.
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Bem-Vindo ao Ultimate TextEditor Experience");

        GfxInterface.printInstructions();
        while(!quit)//Loop principal do programa
        {            
            fullOption = scanner.nextLine();
            option = fullOption.charAt(0);//Pega o primeiro char da String
            
            switch(option)
            {
                case 'p':
                    System.out.println(text.getText());
                    break;
                case 'a':
                    /*Caso o input para adicionar caracter não tenha o caracter
                    a ser adicionado, o programa avisa o usuário e retorna
                    para a escolha de opção*/
                    if(fullOption.length() <= 1)
                    {
                        System.out.println("É necessário colocar o texto"
                                + "a ser adicionado após o 'a'");
                    }else
                    {
                        cursor.setTxt(fullOption.substring(1));
                        addTxt(text, cursor);
                    }
                    break;
                case 'r':
                    removeTxt(text, cursor);
                    break;
                case 'c':
                    System.out.println("Posição do seu cursor: " + 
                            cursor.getPosition());
                    break;
                case 'U':
                    undoChange(text);
                    //Reposiciona cursor se ele ficar após o texto
                    if(cursor.getPosition() > text.length())
                    {
                        cursor.setPosition(text.length());
                    }
                    break;
                case 'R':
                    redoChange(text);
                    break;
                case 'h':
                    GfxInterface.printInstructions();
                    break;
                case 'q':
                    quit = true;
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
        System.out.println("Saindo");
    }

    /**
     * Refazer a ação
     * @param text
     */
    private static void redoChange(Text text)
    {
        if(!text.redo())
            System.out.println("Não há mais mudanças para refazer.");
    }


    /**
     * Remover texto
      * @param text
     * @param cursor
     */
    private static void removeTxt(Text text, Cursor cursor)
    {
        text.remove(cursor);
     
        if(cursor.getPosition() > 0)
            cursor.backward();
    }
    
    //Função adiciona uma string ao texto

    /**
     * Add texto
     * @param text
     * @param cursor
     */
    private static void addTxt(Text text, Cursor cursor)
    {
        String added = cursor.getTxt();
        
        text.add(cursor);
        cursor.forward(added.length());
    }
    
    //Função desfaz última mudança

    /**
     * Desfazer a mudan&ccedil;a feita
     * @param text
     */
    private static void undoChange(Text text)
    {
        if(!text.undo())
        {
            System.out.println("Não há mais versões antigas do texto a serem"
                    + " restauradas.");
        }
    }
}
