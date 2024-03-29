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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
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
    
    private JFrame mainFrame;
    private JPanel butPanel;//Painel que contêm os botões
    private JPanel textPanel;//Painel que contêm o texto
    private JButton exitButton;//Permite a saída do programa
    private JPopupMenu rClickPopup;//Popup ao clicar botão direito
    private JTextArea textArea;//Área para texto
    private Document doc;//Document utilizado para Undo e Redo
    private UndoManager undoManager;//Utilizado para Undo e Redo
    private String fileName;
    private Thread clientThread = null;
    private Socket socket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private boolean needsUpdate = true;//Se falso o arquivo não será enviado para o servidor após mudança
    
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
    }
    
    @SuppressWarnings("empty-statement")
    public void startClient()
    {
        JFrame window = new JFrame("Server Configuration.");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel portPanel = new JPanel();
        JPanel ipPanel = new JPanel();
        JPanel okPanel = new JPanel();
        JLabel portLabel = new JLabel("Port:");
        JLabel ipLabel = new JLabel("IP:");
        JTextField portTField = new JTextField("1234", 20);
        JTextField ipTField = new JTextField("127.0.0.1", 20);
        JButton okButton = new JButton("Connect");
        
        portPanel.setLayout(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.CENTER);
        portPanel.add(portTField, BorderLayout.LINE_END);
        
        ipPanel.setLayout(new BorderLayout());
        ipPanel.add(ipLabel, BorderLayout.CENTER);
        ipPanel.add(ipTField, BorderLayout.LINE_END);
        
        window.setLayout(new GridLayout(3,0));
        window.add(portPanel);
        window.add(ipPanel);
        window.add(okButton);
        
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    String ip = ipTField.getText();
                    int port = Integer.parseInt(portTField.getText());
                    socket = new Socket(ip, port);
                    input = new ObjectInputStream(socket.getInputStream());
                    output = new ObjectOutputStream(socket.getOutputStream());
                    setClientThread();
                    window.dispose();
                    startMainWindow();
                } catch (IOException ex) {
                    System.out.println("Erro conectando ao servidor.");
                }
            }
        });
        
        window.pack();
        window.setVisible(true);
    }
    
    public void startMainWindow()//Inicializa a janela principal do programa
    {
        setFileName();   
        mainFrame.setLayout(new BorderLayout());
        butPanel.setLayout(new GridLayout(1,2));
        
        butPanel.add(exitButton);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        //Define que o texto será enviado ao servidor para cada edição realizada
        textArea.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e)
            {
                if(needsUpdate)
                {
                    try {
                        output.writeInt(2);
                        output.writeUTF(textArea.getText());
                        output.flush();
                    } catch (IOException ex) {
                        System.out.println("Erro ao enviar edição para o servidor.");
                        Logger.getLogger(GfxInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            public void removeUpdate(DocumentEvent e)
            {
                if(needsUpdate)
                {
                    try {
                        output.writeInt(2);
                        output.writeUTF(textArea.getText());
                        output.flush();
                    } catch (IOException ex) {
                        System.out.println("Erro ao enviar edição para o servidor.");
                        Logger.getLogger(GfxInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            public void changedUpdate(DocumentEvent e)
            {
                if(needsUpdate)
                {
                    try {
                        output.writeInt(2);
                        output.writeUTF(textArea.getText());
                        output.flush();
                    } catch (IOException ex) {
                        System.out.println("Erro ao enviar edição para o servidor.");
                        Logger.getLogger(GfxInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
    
    private void setClientThread()//Inicia a thread para receber mensagem do servidor
    {
        clientThread = new Thread(new ClientManager(input, textArea, this));
        clientThread.start();
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
        
        try {
            output.writeInt(1);
            output.writeUTF(fileName);
            output.flush();
        } catch (IOException ex) {
            System.out.println("Erro ao enviar dados ao servidor.");
            Logger.getLogger(GfxInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JTextArea getText()
    {
        return textArea;
    }
    
    public void setNeedsUpdate(boolean bol)
    {
        needsUpdate = bol;
    }
}
