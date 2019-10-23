package com.mycompany.texteditor;

/**
 * Essa classe comanda o cursor do usu&aacute;rio. Ela controla a posi&ccedil;&atilde;o
 * e, quando necess&aacute;rio, a string a ser adicionada.
 *
 *Ela &eacute; passada de par&acirc;metro em muitas fun&ccedil;&otilde;es da classe Text, para facilitar o
 *processo.
 */

public class Cursor {
    /**
     * Indica a posição do cursor
     */
    private int position;//Indica a posição do cursor
    private String txt;

    /**
     * Construtor
     */
    public Cursor()
    {
        this.position = 0;
        this.txt = "";
    }

    /**
     * Retornar a posi&ccedil;&atilde;o para o usu&aacute;rio
     * @return
     */
    public int getPosition()
    {
        return this.position;
    }

    /**
     * Settar nova posi&ccedil;&atilde;o
     * @param newPos
     */
    public void setPosition(int newPos)
    {
        this.position = newPos;
    }

    /**
     * Posi&ccedil;&atilde;o para frente
     */
    public void forward()//Avança o cursor
    {
        this.position++;
    }

    /**
     * Posi&ccedil;&atilde;o para frente
     * @param steps
     */
    public void forward(int steps)//Avança o cursor steps passos
    {
        this.position += steps;
    }

    /**
     * Posi&ccedil;&atilde;o para trás
     */
    public void backward()//volta o cursor
    {
        this.position--;
    }

    /**
     * Posi&ccedil;&atilde;o para frente
     * @param steps
     */
    public void backward(int steps)//Volta o cursor steps passos
    {
        this.position -= steps;
    }

    /**
     * Settar Texto
     * @param txt
     */
    public void setTxt(String txt)
    {
        this.txt = txt;
    }

    /**
     * retornar texto
     * @return
     */
    public String getTxt()
    {
        return this.txt;
    }
}
