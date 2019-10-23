package com.mycompany.texteditor;
/**
 * Esta classe tem o objetivo de servir como um dep&oacute;sito de menus internos do programa.
 * @author Pedro Ramos Cunha
 */
public class Menu {


    /**
     *Essa fun&ccedil;&atilde;o tem o intuito de printar na tela um menu gen&eacute;rico para orientar
     * o usu&aacute;rio das op&ccedil;&otilde;es que o compete.
     *
     */
    static void printInstructions()
    {
        System.out.println("p - imprimir texto atual");
        System.out.println("c - consultar posição do cursor");
        System.out.println("a* - Adicionar caracter (* é o caracter"
                + " a ser adicionado");
        System.out.println("r - remove o caracter na posição do cursor");
        System.out.println("U - Desfaz última mudança");
        System.out.println("R - refaz uma mudança");
        System.out.println("h - Imprimir essa tela de comandos.");
        System.out.println("q - Sair do programa");
    }

}
