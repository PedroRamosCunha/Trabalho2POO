package com.mycompany.texteditor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.undo.UndoManager;
import javax.swing.text.Document;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.AbstractAction;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.KeyStroke;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.filechooser.FileSystemView;

/**
 * Esta classe tem o objetivo de servir como um dep&oacute;sito de menus internos do programa.
 * @author Pedro Ramos Cunha
 */
public class GfxInterface {


    /**
     *Essa fun&ccedil;&atilde;o tem o intuito de printar na tela um menu gen&eacute;rico para orientar
     * o usu&aacute;rio das op&ccedil;&otilde;es que o compete.
     *
     */
    
    private ServerSimulation server;
    private JFrame mainFrame;
    private JPanel butPanel;//Painel que contêm os botões
    private JPanel textPanel;//Painel que contêm o texto
    private JButton exitButton;//Permite a saída do programa
    private JPopupMenu rClickPopup;//Popup ao clicar botão direito
    private JTextArea textArea;//Área para texto
    private Document doc;//Document utilizado para Undo e Redo
    private UndoManager undoManager;//Utilizado para Undo e Redo
    private String fileName;
    
    public GfxInterface()//O constructor inicializa os componentes.
    {
        mainFrame = new JFrame("Ultra Mega Text Editor Simulator 2019 "
                + "Pro Edition");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        butPanel = new JPanel();
        textPanel = new JPanel();
        
        exitButton = new JButton("Close");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(480, 480));
        server = new ServerSimulation(textArea);
    }
    
    public void startMainWindow()//Inicializa a janela principal do programa
    {
        mainFrame.setLayout(new BorderLayout());
        butPanel.setLayout(new GridLayout(1,2));
        
        butPanel.add(exitButton);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e)
            {
                startNewThread();
            }
            
            public void removeUpdate(DocumentEvent e)
            {
                startNewThread();
            }
            
            public void changedUpdate(DocumentEvent e)
            {
                startNewThread();
            }
        });
        textPanel.add(textArea);
        textPanel.setPreferredSize(new Dimension(480, 480));
        
        startPopupMenu();
        RClickTextAction rightClickAction = new RClickTextAction(rClickPopup);
        textArea.addMouseListener(rightClickAction);
        startMenuBar();
        prepareUndoRedo();
        
        mainFrame.add(butPanel, BorderLayout.PAGE_END);
        mainFrame.add(textPanel, BorderLayout.CENTER);
        
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    
    private void startMenuBar()//Inicializa a barra de menus no topo
    {
        //Inicializa a barra de menus
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuProgram = new JMenu("Program");
        
        JMenuItem fileOpen = new JMenuItem("Open");
        fileOpen.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                setFileName();
            }
        });
        menuFile.add(fileOpen);
        
        JMenuItem programClose = new JMenuItem("Close");
        programClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        menuProgram.add(programClose);
        
        menuBar.add(menuFile);
        menuBar.add(menuProgram);
        mainFrame.add(menuBar, BorderLayout.PAGE_START);
    }
    
    private void startPopupMenu()
    {
        rClickPopup = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                Clipboard clipboard = Toolkit.getDefaultToolkit().
                        getSystemClipboard();
                String copiedTxt = textArea.getSelectedText();
                StringSelection selection = new StringSelection(copiedTxt);
                clipboard.setContents(selection, null);
            }
        });
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                Clipboard clipboard = Toolkit.getDefaultToolkit().
                        getSystemClipboard();
                DataFlavor flavor = DataFlavor.stringFlavor;
                if(clipboard.isDataFlavorAvailable(flavor))
                {
                    try{
                        String pasteText = (String)clipboard.getData(flavor);
                        int start = textArea.getSelectionStart();
                        int end = textArea.getSelectionEnd();
                        textArea.replaceRange("", start, end);
                        textArea.insert(pasteText, textArea.getCaretPosition());
                    }catch(Exception ex)
                    {
                        System.out.println("Erro ao colar.");
                    }
                }
            }
        });
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                Clipboard clipboard = Toolkit.getDefaultToolkit().
                        getSystemClipboard();
                String copiedTxt = textArea.getSelectedText();
                StringSelection selection = new StringSelection(copiedTxt);
                clipboard.setContents(selection, null);
                int start = textArea.getSelectionStart();
                int end = textArea.getSelectionEnd();
                textArea.replaceRange("", start, end);
                
            }
        });
        
        rClickPopup.add(copyItem);
        rClickPopup.add(pasteItem);
        rClickPopup.add(cutItem);
    }
    
    private void prepareUndoRedo()
    {
        //Configura Undo e Redo com as teclas Cntrl+Z e cntrl+Y
        undoManager = new UndoManager();
        
        doc = textArea.getDocument();
        doc.addUndoableEditListener(new UndoableEditListener(){
            public void undoableEditHappened(UndoableEditEvent evt)
            {
                undoManager.addEdit(evt.getEdit());
            }
        });
        
        textArea.getActionMap().put("Undo", new AbstractAction("Undo"){
            public void actionPerformed(ActionEvent evt)
            {
                try{
                    if(undoManager.canUndo())
                    {
                        undoManager.undo();
                    }
                }catch (CannotUndoException exc)
                {
                    System.out.println("Erro ao usar Undo :(");
                }
            }
        });
        textArea.getInputMap().put(KeyStroke.
                getKeyStroke("control Z"), "Undo");
     
        textArea.getActionMap().put("Redo", new AbstractAction("Redo"){
            public void actionPerformed(ActionEvent evt)
            {
                try{
                    if(undoManager.canRedo())
                    {
                        undoManager.redo();
                    }
                }catch (CannotRedoException exc)
                {
                    System.out.println("Erro ao usar Redo :(");
                }
            }
        });
        textArea.getInputMap().put(KeyStroke.
                getKeyStroke("control Y"), "Redo");        
    }
    
    public void setFileName()//Pega nome do arquivo para salar o texto
    {
        this.fileName = JOptionPane.showInputDialog("Qual o nome do arquivo onde"
                + "deseja salvar seu texto?");
        server.setFileName(fileName);
    }
    
    public void startNewThread()//Inicia um nova thread de salvamento
    {
        Thread thread = new Thread(server);
        thread.run();
    }
    
    public JTextArea getText()
    {
        return textArea;
    }
}
