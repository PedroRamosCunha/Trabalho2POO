package com.mycompany.texteditor;

/**
 *Essa classe contem o texto juntamente com as c&oacute;pias de suas formas passadas para
 *o comando UNDO e REDO.
 *
 *O par&acirc;metro text &eacute; um vetor de Strings, sendo essas posi&ccedil;&otilde;es usadas para guardar
 *os textos anteriores e permitir o uso de UNDO e REDO.
 *
 *Quando a op&ccedil;&atilde;o UNDO &eacute; utilizada, a primeira string do vetor text &eacute; salvo e o
 *vetor &eacute; shiftado para a esquerda. Ap&oacute;s isso, o vetor rText &eacute; shiftado para a
 *esquerda, sendo a string salva colocada na sua nova primeira posi&ccedil;&atilde;o. Com isso,
 *a cada UNDO, &eacute; salvo a c&oacute;pia anterior para ser utilizado o REDO.
 *Quando o REDO &eacute; utilizado, um processo semelhante ocorre, sendo a primeira
 *string de rText sendo passada para a primeira posi&ccedil;&atilde;o de text. e text &eacute; shiftado
 *para a direita e rText para a esquerda.
 *
 *Se h&aacute; apenas strings vazias no em algum dos vetores, dependendo do caso, o
 *comando REDO ou UNDO pode ser negado, com a fun&ccedil;&atilde;o retornando false.
 *
 * @author Thiago Daniel de Pauli Gagnoni
 * @version 1.0
 *
 *
 */

public class Text {
    /**
     *text e rText devem ter o mesmo tamanho e os espa&ccedil;os devem ser
     *inicializados vazios, com excess&atilde;o do primeiro
     */
    private String[] text = {" ", "", "", "", "", "", "", "", "",""};
    private String[] rText = {" ", "", "", "", "", "", "", "", "",""};
    int rAmount;//Vezes possíveis para dar REDO
    
    public Text()
    {
        rAmount = 0;
    }

    /**
     * Retorna o texto principal
     * @return
     */
    public String getText()//Retorna o texto principal
    {
        return text[0];
    }

    /**
     * Retorna uma posi&ccedil;&atilde;o qualquer do array
     * @param position
     * @return
     */
    public String getText(int position)//Retorna uma posição qualquer do array
    {
        if(position > text.length)
            position = text.length;
        return text[position];
    }

    /**
     * Retorna o tamanho do texto principal
     * @return
     */
    public int length()//Retorna o tamanho do texto principal
    {
        return this.text[0].length();
    }

    /**
     * /**
     *      Remove um caracter na posi&ccedil;&atilde;o indicada.
     *      A remo&ccedil;&atilde;o &eacute; feira dividindo a string em duas substrings, uma antes e uma
     *      depois do caracter a ser removido e, ap&oacute;s isso, juntando-se as duas
     * @param cursor
     */
    public void remove(Cursor cursor)
    {
        String ss1 = "";
        String ss2 = "";
        //É deletado o caracter anterior ao cursor.
        int position = cursor.getPosition() - 1;
        shift();
        if(rAmount != 0)
        {
            clearRedo();
        }
        
        if(position - 1 >= 0)
        {
            ss1 = text[0].substring(0, position);
        }
        if(position + 1 <= text[0].length() - 1)
        {
            ss2 = text[0].substring(position + 1, text[0].length());
        }
        
        text[0] = ss1 + ss2;
    }


    /**
     * Adiciona uma String no texto, dividindo o texto principal em duas
     *     Substrings e a colocando no meio
     * @param cursor
     */
    public void add(Cursor cursor)
    {
        String ss1 = "";
        String ss2 = "";
        
        int position = cursor.getPosition();
        String added = cursor.getTxt();
        shift();
        if(rAmount != 0)
        {
            clearRedo();
        }
        
        if(position >= 0)
        {
            ss1 = text[0].substring(0, position);
        }
        if(position + 1 <= text[0].length() - 1)
        {
            ss2 = text[0].substring(position + 1, text[0].length());
        }
        this.text[0] = ss1 + added + ss2;
    }
    
    //Shifta o texto para a direita, para manter cópias anteriores do texto.

    /**
     * Shifta o texto para a direita, para manter c&oacute;pias anteriores do texto.
     */
    private void shift()
    {
        String prev = text[0];
        for(int i = 1; i < text.length; i++)
        {
            String aux = text[i];
            text[i] = prev;
            prev = aux;
        }
    }

    /**
     * Refaz uma modifica&ccedil;&atilde;o
     * @return
     */
    //Refaz uma modificação
    public boolean redo()
    {
        if(rAmount <= 0)
            return false;
                
        rAmount--;
                
        String prev = text[0];
        text[0] = rText[0];
        for(int i = 1; i < text.length; i++)
        {
            String aux = text[i];
            text[i] = prev;
            prev = aux;
        }
        
        prev = rText[rText.length - 1];
        rText[rText.length - 1] = "";
        for(int i = rText.length - 2; i >= 0; i--)
        {
            String aux = rText[i];
            rText[i] = prev;
            prev = aux;
        }
        
        return true;
    }
    
    //Desfaz uma modificação, retorna false se não tiver mais versões antigas.
    public boolean undo()
    {
        //Checa se ainda tem texto antigo salvo para ser feito UNDO
        if("".equals(text[1]))
        {
            return false;
        }
        
        rAmount++;
        
        //Shifta o array text para a esquerda, removendo text[0]
        String prev = text[text.length - 1];
        text[text.length - 1] = "";
        String txt0 = text[0];
        for(int i = text.length - 2; i >= 0; i--)
        {
            String aux = text[i];
            text[i] = prev;
            prev = aux;
        }
        
        //Shifta o array rText para a direita, adicionando o antigo text[0]
        prev = rText[0];
        rText[0] = txt0;
        for(int i = 1; i < text.length; i++)
        {
            String aux = rText[i];
            rText[i] = prev;
            prev = aux;
        }
        
        return true;
    }
    
    /*Limpa o vetor rText quando ocorre alguma modificação além do UNDO,
    Tornando, assim, impossível de utilizar o REDO antes de realizado
    outro UNDO*/
    private void clearRedo()
    {
        this.rAmount = 0;
        for (String txt : this.rText)
        {
            txt = "";
        }
    }
}
