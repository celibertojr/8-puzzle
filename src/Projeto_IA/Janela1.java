package Projeto_IA;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.borland.jbcl.layout.*;

/**
 * TRABALHO DE EE-214
 * ITA
 * LUIZ ANTONIO CELIBERTO JUNIOR
 *
 */

public class Janela1
    extends JFrame {
  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel Painel = new JPanel();
  JButton Resolve = new JButton();

//********************************************************************************
//                                 Variaveis de ambiente                         *
//********************************************************************************
  int cont = 0;
  int nos = 0;
  int M = 0, N = 0;

  public int tabuleiro[][];
  public int meta[][];
  char resposta[][] = new char[3][3]; // Matriz para deixar a resp bonitinha

  public StringBuffer Entrada = new StringBuffer(); //Lê todos os dados da entrada
  public String formacao;
  public StringBuffer Matriz = new StringBuffer(); // Receber valor da matriz
  public StringBuffer Final = new StringBuffer();

  public File file1 = new File("entrada.txt");
  public int Valor_G=0;

//********************************************************************
//                             Listas                                *
//********************************************************************

  LinkedList arvore = new LinkedList();
  LinkedList Aberto = new LinkedList();
  LinkedList Fechado = new LinkedList();


//********************************************************************
//              FUNCOES USADAS NO PROGRAMA
//********************************************************************
  public StringBuffer CalculaBuffer(int entrada[][]) {
    StringBuffer Buffer = new StringBuffer();

    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        Buffer.append("|"+entrada[i][j]);


      }
    }
    return Buffer;
  }



public int[][] CalculaMatriz(StringBuffer Buffer) {

    int posicao[][] = new int[M][N];

    int cont1 =0;
    int valor=0;
    String strvalor;
    String strvalor2;
    char str1;
    char str2;
    int temp;

    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {

        str1 = Buffer.charAt(cont1);

        if (str1=='|') {
          if (Buffer.length() - cont1 > 2) {
            str2 = Buffer.charAt(cont1 + 2);
            if (str2=='|') {
              valor = (Buffer.charAt(cont1 + 1) - 48);
              cont1 = cont1 + 2;

            }
            else {

              valor = (Buffer.charAt(cont1 + 1) - 48);
              valor = valor * 10 + (Buffer.charAt(cont1 + 2) - 48);

              cont1 = cont1 + 3;
              posicao[i][j]=valor;
            }

          }
          else {
            valor = (Buffer.charAt(cont1 + 1) - 48);
            cont1=cont+2;

               }
        }
         posicao[i][j]=valor;
      }
    }
 return posicao;

}








  public void ImprimeMatriz(int entrada[][]) {
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        System.out.print(entrada[i][j]);
      }
      System.out.println("");
    }

  }

  public int CalculaColuna(int entrada[][], int n) {
    int coluna=0;
    for (int l=0;l<M;l++) {
          for (int c=0;c<N;c++) {
              if (entrada[l][c] == n) {
                  coluna = c;
              }
          }
      }
    return coluna;
  }

  public int CalculaLinha(int entrada[][], int n) {
    int linha=0;

    for (int l=0;l<M;l++) {
          for (int c=0;c<N;c++) {
              if (entrada[l][c] == n) {
                  linha = l;
              }
          }
      }
    return linha;
  }


  public int CalculaCusto(StringBuffer Buffer_custo) {
     int fora=0;
     int analisar_custo[][] = new int[M][N];
     int meta_final_custo[][] = new int[M][N];

     analisar_custo = CalculaMatriz(Buffer_custo);
     meta_final_custo = CalculaMatriz(Final);

     for (int n = 1; n < (M * N); n++) {  //calcula a
                                                //distância Manhattan
             int l = CalculaLinha(analisar_custo, n);
             int c = CalculaColuna(analisar_custo, n);
             int lMeta = CalculaLinha(meta_final_custo, n);
             int cMeta = CalculaColuna(meta_final_custo, n);

             fora += Math.abs(l - lMeta);
             fora += Math.abs(c - cMeta);

           }
     return fora;

   }

   public int CalculaCustoH2(StringBuffer Buffer_custo) {
    int fora=0;
    int analisar_custo[][] = new int[M][N];
    int meta_final_custo[][] = new int[M][N];

    analisar_custo = CalculaMatriz(Buffer_custo);
    meta_final_custo = CalculaMatriz(Final);

    for (int n = 1; n < (M * N); n++) {  //calcula a
                                               //distância
            int l = CalculaLinha(analisar_custo, n);
            int c = CalculaColuna(analisar_custo, n);
            int lMeta = CalculaLinha(meta_final_custo, n);
            int cMeta = CalculaColuna(meta_final_custo, n);

            if(l!=lMeta || c!=cMeta)
               {
                 fora++;
               }


          }
    return fora;

  }

  public int CalculaCustoH3(StringBuffer Buffer_custo) {
     int fora=0;
     int analisar_custo[][] = new int[M][N];
     int meta_final_custo[][] = new int[M][N];
     double hipo=0;
     analisar_custo = CalculaMatriz(Buffer_custo);
     meta_final_custo = CalculaMatriz(Final);

     for (int n = 1; n < (M * N); n++) {  //calcula a
                                                //distância
             int l = CalculaLinha(analisar_custo, n);
             int c = CalculaColuna(analisar_custo, n);
             int lMeta = CalculaLinha(meta_final_custo, n);
             int cMeta = CalculaColuna(meta_final_custo, n);

              hipo=hipo+Math.sqrt(Math.abs(l - lMeta)*Math.abs(c - cMeta) );



           }
           fora=(int)hipo;
     return fora;

   }






//******************************************************************
//                             REGRAS                              *
//******************************************************************


  public boolean move_direita() { //move o O para a direita
    int linha_numero = 0;
    int coluna_numero = 0;
    int temp[][] = new int[M][N];
    StringBuffer Buffer = new StringBuffer();
    int valorTEMP;
    //cacula stringbuffer para matriz
    temp = CalculaMatriz(Matriz);
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        if (temp[i][j] == 0) {
          linha_numero = i;
          coluna_numero = j;
        }
      }
    }

    if (coluna_numero < N-1) {
      valorTEMP = temp[linha_numero][coluna_numero + 1];
      temp[linha_numero][coluna_numero + 1] = temp[linha_numero][coluna_numero];
      temp[linha_numero][coluna_numero] = valorTEMP;
      //calcula matriz para string
      Buffer = CalculaBuffer(temp);
      //ADD o string
      arvore.add(Buffer);
    nos++;
    }
    return true;
  }

  public boolean move_esquerda() {
    int linha_numero = 0;
    int coluna_numero = 0;
    int temp[][] = new int[M][N];
    StringBuffer Buffer = new StringBuffer();
    int valorTEMP;
    //cacula stringbuffer para matriz
    temp = CalculaMatriz(Matriz);
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        if (temp[i][j] == 0) {
          linha_numero = i;
          coluna_numero = j;
        }
      }
    }

    if (coluna_numero > 0) {
      valorTEMP = temp[linha_numero][coluna_numero - 1];
      temp[linha_numero][coluna_numero - 1] = temp[linha_numero][coluna_numero];
      temp[linha_numero][coluna_numero] = valorTEMP;
      //calcula matriz para string
      Buffer = CalculaBuffer(temp);
      //ADD o string
      arvore.add(Buffer);
      nos++;
    }
    return true;
  }

  public boolean move_cima() {
    int linha_numero = 0;
    int coluna_numero = 0;
    int temp[][] = new int[M][N];
    StringBuffer Buffer = new StringBuffer();
    int valorTEMP;
    //cacula stringbuffer para matriz
    temp = CalculaMatriz(Matriz);
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        if (temp[i][j] == 0) {
          linha_numero = i;
          coluna_numero = j;
        }
      }
    }

    if (linha_numero >0) {
      valorTEMP = temp[linha_numero-1][coluna_numero];
      temp[linha_numero-1][coluna_numero] = temp[linha_numero][coluna_numero];
      temp[linha_numero][coluna_numero] = valorTEMP;
      //calcula matriz para string
      Buffer = CalculaBuffer(temp);
      //ADD o string
      arvore.add(Buffer);
      nos++;
    }
    return true;
  }

  public boolean move_baixo() {
    int linha_numero = 0;
    int coluna_numero = 0;
    int temp[][] = new int[M][N];
    StringBuffer Buffer = new StringBuffer();
    int valorTEMP;
    //cacula stringbuffer para matriz
    temp = CalculaMatriz(Matriz);
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        if (temp[i][j] == 0) {
          linha_numero = i;
          coluna_numero = j;
        }
      }
    }

    if (linha_numero < M-1) {
      valorTEMP = temp[linha_numero+1][coluna_numero];
      temp[linha_numero+1][coluna_numero] = temp[linha_numero][coluna_numero];
      temp[linha_numero][coluna_numero] = valorTEMP;
      //calcula matriz para string
      Buffer = CalculaBuffer(temp);

      //ADD o string
      arvore.add(Buffer);
      nos++;
    }
    return true;
  }

//******************************************************************
//              FUNÇÃO PARA ACHAR O MENOR CAMINHO
//                   Heuristica 1
//******************************************************************

 public StringBuffer menor_caminho(int G)
    {
      StringBuffer MenorValor= new StringBuffer();
      LinkedList clone = new LinkedList();
      StringBuffer Buffer= new StringBuffer();
      LinkedList abertos = new LinkedList();
      LinkedList abertos_temp = new LinkedList();

      String valor1;
      String valor2;
      String valorTEMP;

      ArrayList valor_clone = new ArrayList();
      ArrayList valor_fechado = new ArrayList();
      ArrayList temp = new ArrayList();
      int analisar[][] = new int[M][N];
      int meta_final[][] = new int[M][N];


      int count = 0;

// Retira os fechados, pois eles nao contam para novos caminhos

      for (int i = 0; i < arvore.size(); i++) {
        valor_clone.add(arvore.get(i));
        temp.add(arvore.get(i));
      }

       //temp.add("0");
        for (int j = 0; j < Fechado.size(); j++) {
          valor_fechado.add(Fechado.get(j));
      }

      for (int i = 0; i < valor_clone.size(); i++) {
           for (int j = 0; j < valor_fechado.size(); j++) {

               valor1 = "" + valor_clone.get(i);
               valor2 = "" + valor_fechado.get(j);

               if (valor1.equals(valor2)) { //verifica se existe algum
                                            //aberto igual ao fechado

                 for(int k=0;k<temp.size();k++)
                 {
                   valorTEMP=""+temp.get(k);   //se existe procura este valor
                   if(valorTEMP.equals(valor1))//repetido nos abertos
                      {                        //e remove ele
                        temp.remove(k);
                      }
                 }
               }
             }
        }



clone.clear();
clone.addAll(temp);
MenorValor.delete(0,MenorValor.length());
StringBuffer B_Custo= new StringBuffer();


int custo;
int custoOLD=1000;


if(temp.size()>0)
      {
        while (clone.size() > 0) {

          String ValorString = "" + clone.removeFirst();
          Buffer.delete(0, Buffer.length());
          Buffer.append(ValorString);

          analisar = CalculaMatriz(Buffer);
          meta_final = CalculaMatriz(Final);

          int fora1 = 0;
          int fora2 =0;
          int fora=0;

          for (int n = 1; n < (M * N); n++) {  //calcula a
                                               //distância Manhattan
            int l = CalculaLinha(analisar, n);
            int c = CalculaColuna(analisar, n);
            int lMeta = CalculaLinha(meta_final, n);
            int cMeta = CalculaColuna(meta_final, n);

            fora += Math.abs(l - lMeta);
            fora += Math.abs(c - cMeta);

          }
          custo = G + fora;

          abertos.add("" + custo); //Adiciona aos
          abertos.add(ValorString);//abertos temporarios

        }


    /// calcula


        int contador_remover=0;
        int valorcusto = 0;
        int valorcustoOLD = 500;

        if (abertos.size() > 0) {
          for (int j3 = 0; j3 < abertos.size(); j3 = j3 + 2) {
            valorcusto = Integer.parseInt("" + abertos.get(j3));

            if (valorcusto < valorcustoOLD) {
              MenorValor.delete(0, MenorValor.length());
              valorcustoOLD = valorcusto;
              MenorValor.append(abertos.get(j3 + 1));
              contador_remover=j3;
            }
          }

          ///////////// Verifica se existe um melhor caminho com G menor //////////////
          String ValorGH_aberto=""+abertos.remove(contador_remover);

          String valor1_abertos = "" + abertos.remove(contador_remover);

   int contador = 0;
   int swap=0;
   boolean trocou=false;
   int custoa;
   int custoA;


          for (int j = 1; j < Aberto.size(); j=j+2) {

            String valor2_Abertos = "" + Aberto.get(j);

            B_Custo.append(valor1_abertos);
            custoa=CalculaCusto(B_Custo);
            B_Custo.delete(0, B_Custo.length());
            B_Custo.append(valor2_Abertos);
            custoA=CalculaCusto(B_Custo);
            B_Custo.delete(0, B_Custo.length());


            if (valor1_abertos.equals(valor2_Abertos)) {

                  int posição = abertos.indexOf(valor2_Abertos);
                  int Posição2 = Aberto.indexOf(valor2_Abertos);

                  int Valoraberto=Integer.parseInt(ValorGH_aberto);
                  int ValorAberto=Integer.parseInt("" + Aberto.get(Posição2-1));

                  int Ga=Valoraberto-custoa;
                  int GA=ValorAberto-custoA;

                  if(GA<Ga)
                  {
                    abertos_temp.clear();
                    abertos_temp.add(Aberto.get(Posição2-1));
                    abertos_temp.add(Aberto.get(Posição2));
                    Valor_G=GA;
                    swap=Posição2;
                    trocou=true;
                  }

            }

          }


        if(trocou)
        {
          abertos.clear();
          abertos.addAll(abertos_temp);

          Aberto.remove(swap-1);
          Aberto.remove(swap-1);
        }
//////////////////////////////////////////////////////////////////////////////


        }

        else{
          int G1=0;
          int custo1;
          StringBuffer B_Custo2= new StringBuffer();

          for (int j3 = 0; j3 < Aberto.size(); j3 = j3 + 2) {
            valorcusto = Integer.parseInt("" + Aberto.get(j3));

            if (valorcusto < valorcustoOLD) {
              MenorValor.delete(0, MenorValor.length());
              valorcustoOLD = valorcusto;
              MenorValor.append(Aberto.get(j3 + 1));
              B_Custo.append(MenorValor);
              custo1=CalculaCusto(B_Custo2);
              G1=valorcusto-custo1; //calcula G

              contador_remover=j3;

            }
          }

          Valor_G=G1;
          Aberto.remove(contador_remover);
          Aberto.remove(contador_remover);

        }

        if(abertos_temp.size()>0){Aberto.addAll(abertos_temp);}
        else{

          Aberto.addAll(abertos);

        }




        return MenorValor;
      }
      else{ //Se todos os abertos são iguais os fechados, entao procurar nos outros
            //abertos
            StringBuffer B_Custo1= new StringBuffer();


        MenorValor.delete(0, MenorValor.length());
        int contador_remover=0;
        int valorcusto = 0;
        int valorcustoOLD = 500;
        int custo1;


        if(Aberto.size()>0){
          int G1=0;


          for (int j3 = 0; j3 < Aberto.size(); j3 = j3 + 2) {
            valorcusto = Integer.parseInt("" + Aberto.get(j3));


            if (valorcusto < valorcustoOLD) {
              MenorValor.delete(0, MenorValor.length());
              custoOLD = valorcusto;
              MenorValor.append(Aberto.get(j3 + 1));
              B_Custo1.append(MenorValor);
              custo1=CalculaCusto(B_Custo1);
              G1=valorcusto-custo1; //calcula G
              contador_remover = j3;

            }
          }

          Valor_G=G1;
          Aberto.remove(contador_remover);
          Aberto.remove(contador_remover);
        }





        return MenorValor; }


    }

//******************************************************************
//              FUNÇÃO PARA ACHAR O MENOR CAMINHO
//                   Heuristica 2
//******************************************************************
    public StringBuffer menor_caminho_H2(int G)
       {
         StringBuffer MenorValor= new StringBuffer();
         LinkedList clone = new LinkedList();
         StringBuffer Buffer= new StringBuffer();
         LinkedList abertos = new LinkedList();
         LinkedList abertos_temp = new LinkedList();

         String valor1;
         String valor2;
         String valorTEMP;

         ArrayList valor_clone = new ArrayList();
         ArrayList valor_fechado = new ArrayList();
         ArrayList temp = new ArrayList();
         int analisar[][] = new int[M][N];
         int meta_final[][] = new int[M][N];


         int count = 0;

// Retira os fechados, pois eles nao contam para novos caminhos

         for (int i = 0; i < arvore.size(); i++) {
           valor_clone.add(arvore.get(i));
           temp.add(arvore.get(i));
         }

          //temp.add("0");
           for (int j = 0; j < Fechado.size(); j++) {
             valor_fechado.add(Fechado.get(j));
         }

         for (int i = 0; i < valor_clone.size(); i++) {
              for (int j = 0; j < valor_fechado.size(); j++) {

                  valor1 = "" + valor_clone.get(i);
                  valor2 = "" + valor_fechado.get(j);

                  if (valor1.equals(valor2)) { //verifica se existe algum
                                               //aberto igual ao fechado

                    for(int k=0;k<temp.size();k++)
                    {
                      valorTEMP=""+temp.get(k);   //se existe procura este valor
                      if(valorTEMP.equals(valor1))//repetido nos abertos
                         {                        //e remove ele
                           temp.remove(k);
                         }
                    }
                  }
                }
           }



   clone.clear();
   clone.addAll(temp);
   MenorValor.delete(0,MenorValor.length());
   StringBuffer B_Custo= new StringBuffer();


   int custo;
   int custoOLD=1000;


   if(temp.size()>0)
         {
           while (clone.size() > 0) {

             String ValorString = "" + clone.removeFirst();
             Buffer.delete(0, Buffer.length());
             Buffer.append(ValorString);

             analisar = CalculaMatriz(Buffer);
             meta_final = CalculaMatriz(Final);

             int fora=0;

             for (int n = 1; n < (M * N); n++) {  //calcula a
                                                  //distância
               int l = CalculaLinha(analisar, n);
               int c = CalculaColuna(analisar, n);
               int lMeta = CalculaLinha(meta_final, n);
               int cMeta = CalculaColuna(meta_final, n);

               if(l!=lMeta || c!=cMeta)
               {
                 fora++;
               }


             }
             custo = G + fora;

             abertos.add("" + custo); //Adiciona aos
             abertos.add(ValorString);//abertos temporarios

           }


       /// calcula


           int contador_remover=0;
           int valorcusto = 0;
           int valorcustoOLD = 500;

           if (abertos.size() > 0) {
             for (int j3 = 0; j3 < abertos.size(); j3 = j3 + 2) {
               valorcusto = Integer.parseInt("" + abertos.get(j3));

               if (valorcusto < valorcustoOLD) {
                 MenorValor.delete(0, MenorValor.length());
                 valorcustoOLD = valorcusto;
                 MenorValor.append(abertos.get(j3 + 1));
                 contador_remover=j3;
               }
             }

             ///////////// Verifica se existe um melhor caminho com G menor //////////////
             String ValorGH_aberto=""+abertos.remove(contador_remover);
             String valor1_abertos = "" + abertos.remove(contador_remover);

      int contador = 0;
      int swap=0;
      boolean trocou=false;
      int custoa;
      int custoA;


             for (int j = 1; j < Aberto.size(); j=j+2) {

               String valor2_Abertos = "" + Aberto.get(j);

               B_Custo.append(valor1_abertos);
               custoa=CalculaCustoH2(B_Custo);
               B_Custo.delete(0, B_Custo.length());
               B_Custo.append(valor2_Abertos);
               custoA=CalculaCustoH2(B_Custo);
               B_Custo.delete(0, B_Custo.length());


               if (valor1_abertos.equals(valor2_Abertos)) {

                     int posição = abertos.indexOf(valor2_Abertos);
                     int Posição2 = Aberto.indexOf(valor2_Abertos);

                     int Valoraberto=Integer.parseInt(ValorGH_aberto);
                     int ValorAberto=Integer.parseInt("" + Aberto.get(Posição2-1));

                     int Ga=Valoraberto-custoa;
                     int GA=ValorAberto-custoA;

                     if(GA<Ga)
                     {
                       abertos_temp.clear();
                       abertos_temp.add(Aberto.get(Posição2-1));
                       abertos_temp.add(Aberto.get(Posição2));
                       Valor_G=GA;
                       swap=Posição2;
                       trocou=true;
                     }

               }

             }


           if(trocou)
           {
             abertos.clear();
             abertos.addAll(abertos_temp);

             Aberto.remove(swap-1);
             Aberto.remove(swap-1);
           }
//////////////////////////////////////////////////////////////////////////////


           }

           else{
             int G1=0;
             int custo1;
             StringBuffer B_Custo2= new StringBuffer();

             for (int j3 = 0; j3 < Aberto.size(); j3 = j3 + 2) {
               valorcusto = Integer.parseInt("" + Aberto.get(j3));

               if (valorcusto < valorcustoOLD) {
                 MenorValor.delete(0, MenorValor.length());
                 valorcustoOLD = valorcusto;
                 MenorValor.append(Aberto.get(j3 + 1));
                 B_Custo.append(MenorValor);
                 custo1=CalculaCustoH2(B_Custo2);
                 G1=valorcusto-custo1; //calcula G

                 contador_remover=j3;

               }
             }

             Valor_G=G1;
             Aberto.remove(contador_remover);
             Aberto.remove(contador_remover);

           }

           if(abertos_temp.size()>0){Aberto.addAll(abertos_temp);}
           else{Aberto.addAll(abertos);}




           return MenorValor;
         }
         else{ //Se todos os abertos são iguais os fechados, entao procurar nos outros
               //abertos
               StringBuffer B_Custo1= new StringBuffer();


           MenorValor.delete(0, MenorValor.length());
           int contador_remover=0;
           int valorcusto = 0;
           int valorcustoOLD = 500;
           int custo1;


           if(Aberto.size()>0){
             int G1=0;


             for (int j3 = 0; j3 < Aberto.size(); j3 = j3 + 2) {
               valorcusto = Integer.parseInt("" + Aberto.get(j3));


               if (valorcusto < valorcustoOLD) {
                 MenorValor.delete(0, MenorValor.length());
                 custoOLD = valorcusto;
                 MenorValor.append(Aberto.get(j3 + 1));
                 B_Custo1.append(MenorValor);
                 custo1=CalculaCustoH2(B_Custo1);
                 G1=valorcusto-custo1; //calcula G
                 contador_remover = j3;

               }
             }

             Valor_G=G1;
             Aberto.remove(contador_remover);
             Aberto.remove(contador_remover);
           }





           return MenorValor; }


       }

   //******************************************************************
//              FUNÇÃO PARA ACHAR O MENOR CAMINHO
//                   Heuristica 3
//******************************************************************

     public StringBuffer menor_caminho_H3(int G)
          {
            StringBuffer MenorValor= new StringBuffer();
            LinkedList clone = new LinkedList();
            StringBuffer Buffer= new StringBuffer();
            LinkedList abertos = new LinkedList();
            LinkedList abertos_temp = new LinkedList();

            String valor1;
            String valor2;
            String valorTEMP;

            ArrayList valor_clone = new ArrayList();
            ArrayList valor_fechado = new ArrayList();
            ArrayList temp = new ArrayList();

            int analisar[][] = new int[M][N];
            int meta_final[][] = new int[M][N];


            int count = 0;

// Retira os fechados, pois eles nao contam para novos caminhos

            for (int i = 0; i < arvore.size(); i++) {
              valor_clone.add(arvore.get(i));
              temp.add(arvore.get(i));
            }

             //temp.add("0");
              for (int j = 0; j < Fechado.size(); j++) {
                valor_fechado.add(Fechado.get(j));
            }

            for (int i = 0; i < valor_clone.size(); i++) {
                 for (int j = 0; j < valor_fechado.size(); j++) {

                     valor1 = "" + valor_clone.get(i);
                     valor2 = "" + valor_fechado.get(j);

                     if (valor1.equals(valor2)) { //verifica se existe algum
                                                  //aberto igual ao fechado

                       for(int k=0;k<temp.size();k++)
                       {
                         valorTEMP=""+temp.get(k);   //se existe procura este valor
                         if(valorTEMP.equals(valor1))//repetido nos abertos
                            {                        //e remove ele
                              temp.remove(k);
                            }
                       }
                     }
                   }
              }



      clone.clear();
      clone.addAll(temp);
      MenorValor.delete(0,MenorValor.length());
      StringBuffer B_Custo= new StringBuffer();


      int custo;
      int custoOLD=1000;


      if(temp.size()>0)
            {
              while (clone.size() > 0) {

                String ValorString = "" + clone.removeFirst();
                Buffer.delete(0, Buffer.length());
                Buffer.append(ValorString);

                analisar = CalculaMatriz(Buffer);
                meta_final = CalculaMatriz(Final);
                int fora=0;
                double hipo=0;

                for (int n = 1; n < (M * N); n++) {  //calcula a
                                                     //distância Euclidiana
                  int l = CalculaLinha(analisar, n);
                  int c = CalculaColuna(analisar, n);
                  int lMeta = CalculaLinha(meta_final, n);
                  int cMeta = CalculaColuna(meta_final, n);

                  hipo=hipo+Math.sqrt(Math.abs(l - lMeta)*Math.abs(c - cMeta) );

                }
                custo = G + (int)hipo;

                abertos.add("" + custo); //Adiciona aos
                abertos.add(ValorString);//abertos temporarios

              }


          /// calcula


              int contador_remover=0;
              int valorcusto = 0;
              int valorcustoOLD = 500;

              if (abertos.size() > 0) {
                for (int j3 = 0; j3 < abertos.size(); j3 = j3 + 2) {
                  valorcusto = Integer.parseInt("" + abertos.get(j3));

                  if (valorcusto < valorcustoOLD) {
                    MenorValor.delete(0, MenorValor.length());
                    valorcustoOLD = valorcusto;
                    MenorValor.append(abertos.get(j3 + 1));
                    contador_remover=j3;
                  }
                }

                ///////////// Verifica se existe um melhor caminho com G menor //////////////
                String ValorGH_aberto=""+abertos.remove(contador_remover);
                String valor1_abertos = "" + abertos.remove(contador_remover);

         int contador = 0;
         int swap=0;
         boolean trocou=false;
         int custoa;
         int custoA;


                for (int j = 1; j < Aberto.size(); j=j+2) {

                  String valor2_Abertos = "" + Aberto.get(j);

                  B_Custo.append(valor1_abertos);
                  custoa=CalculaCustoH3(B_Custo);
                  B_Custo.delete(0, B_Custo.length());
                  B_Custo.append(valor2_Abertos);
                  custoA=CalculaCustoH3(B_Custo);
                  B_Custo.delete(0, B_Custo.length());


                  if (valor1_abertos.equals(valor2_Abertos)) {

                        int posição = abertos.indexOf(valor2_Abertos);
                        int Posição2 = Aberto.indexOf(valor2_Abertos);

                        int Valoraberto=Integer.parseInt(ValorGH_aberto);
                        int ValorAberto=Integer.parseInt("" + Aberto.get(Posição2-1));

                        int Ga=Valoraberto-custoa;
                        int GA=ValorAberto-custoA;

                        if(GA<Ga)
                        {
                          abertos_temp.clear();
                          abertos_temp.add(Aberto.get(Posição2-1));
                          abertos_temp.add(Aberto.get(Posição2));
                          Valor_G=GA;
                          swap=Posição2;
                          trocou=true;
                        }

                  }

                }


              if(trocou)
              {
                abertos.clear();
                abertos.addAll(abertos_temp);

                Aberto.remove(swap-1);
                Aberto.remove(swap-1);
              }
//////////////////////////////////////////////////////////////////////////////


              }

              else{
                int G1=0;
                int custo1;
                StringBuffer B_Custo2= new StringBuffer();

                for (int j3 = 0; j3 < Aberto.size(); j3 = j3 + 2) {
                  valorcusto = Integer.parseInt("" + Aberto.get(j3));

                  if (valorcusto < valorcustoOLD) {
                    MenorValor.delete(0, MenorValor.length());
                    valorcustoOLD = valorcusto;
                    MenorValor.append(Aberto.get(j3 + 1));
                    B_Custo.append(MenorValor);
                    custo1=CalculaCustoH3(B_Custo2);
                    G1=valorcusto-custo1; //calcula G

                    contador_remover=j3;

                  }
                }

                Valor_G=G1;
                Aberto.remove(contador_remover);
                Aberto.remove(contador_remover);

              }

              if(abertos_temp.size()>0){Aberto.addAll(abertos_temp);}
              else{Aberto.addAll(abertos);}




              return MenorValor;
            }
            else{ //Se todos os abertos são iguais os fechados, entao procurar nos outros
                  //abertos
                  StringBuffer B_Custo1= new StringBuffer();


              MenorValor.delete(0, MenorValor.length());
              int contador_remover=0;
              int valorcusto = 0;
              int valorcustoOLD = 500;
              int custo1;


              if(Aberto.size()>0){
                int G1=0;


                for (int j3 = 0; j3 < Aberto.size(); j3 = j3 + 2) {
                  valorcusto = Integer.parseInt("" + Aberto.get(j3));


                  if (valorcusto < valorcustoOLD) {
                    MenorValor.delete(0, MenorValor.length());
                    custoOLD = valorcusto;
                    MenorValor.append(Aberto.get(j3 + 1));
                    B_Custo1.append(MenorValor);
                    custo1=CalculaCustoH3(B_Custo1);
                    G1=valorcusto-custo1; //calcula G
                    contador_remover = j3;

                  }
                }

                Valor_G=G1;
                Aberto.remove(contador_remover);
                Aberto.remove(contador_remover);
              }





              return MenorValor; }


          }















//*****************************************************************


   public boolean log1_arvore(String act) {
     String crlf = System.getProperties().getProperty("line.separator");
     try {
       FileOutputStream fos = new FileOutputStream("arvore.txt", true);
       DataOutputStream dos = new DataOutputStream(fos);
       dos.writeChars(act);
       dos.writeBytes(crlf);
     }
     catch (IOException ioe) {}
     return true;
   }

  public boolean log2_Abertos(String act) {
    String crlf = System.getProperties().getProperty("line.separator");
    try {
      FileOutputStream fos = new FileOutputStream("Abertos.txt", true);
      DataOutputStream dos = new DataOutputStream(fos);
      dos.writeChars(act);
      dos.writeBytes(crlf);
    }
    catch (IOException ioe) {}
    return true;
  }

  public boolean testaFinal() { //Compara para descobrir se achou a resposta
    StringBuffer Clone = new StringBuffer();
         Clone.append(Matriz);

   if((String.valueOf(Clone)).equals(String.valueOf(Final)))
    {
      Texto.append("FIM ! :-)"+ "\n");
      Texto.append(""+Clone+ "\n");
      Texto.append("Profundidade: "+Valor_G+ "\n");
      Texto.append("Total de nós aberto: "+nos+ "\n");



      System.out.println("FIM ! :-)"+ Clone);
      return true;
    }
    else if(Clone.length()==0)
    {
      System.out.println("");
      return true;
    }

    else {

      return false;
    }

  }

  public void limpa_tela() { //função limpa
    String[] cmds = {
        "cmd", "/c", "cls"};
    try {
      Process prc = Runtime.getRuntime().exec(cmds);
      InputStream in = prc.getInputStream();
      int i;
      while ( (i = in.read()) != -1) {
        System.out.print( (char) i);
      }
      in.close();

    }
    catch (Exception e) {
      System.out.println("o Erro : " + e);
    }

  }

//**********************************************************************************

   //Construct the frame
   public Janela1() {
     enableEvents(AWTEvent.WINDOW_EVENT_MASK);
     try {
       jbInit();
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }

  //Component initialization
  private void jbInit() throws Exception {
    contentPane = (JPanel)this.getContentPane();
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");
    titledBorder5 = new TitledBorder("");
    titledBorder6 = new TitledBorder("");
    titledBorder7 = new TitledBorder("");
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(604, 600));
    this.setTitle("Projeto de IA");
    Painel.setBackground(Color.lightGray);
    Painel.setBorder(null);
    Painel.setMaximumSize(new Dimension(40767, 45000));
    Painel.setLayout(null);
    Resolve.setBounds(new Rectangle(328, 289, 129, 59));
    Resolve.setFont(new java.awt.Font("Dialog", 1, 20));
    Resolve.setText("Resolver");
    Resolve.addActionListener(new Janela1_Resolve_actionAdapter(this));
    Texto.setEnabled(true);
    Texto.setBorder(BorderFactory.createLineBorder(Color.black));
    Texto.setDebugGraphicsOptions(0);
    Texto.setDoubleBuffered(false);
    Texto.setMaximumSize(new Dimension(2147483647, 2147483647));
    Texto.setVerifyInputWhenFocusTarget(true);
    Texto.setEditable(false);
    Texto.setText("");
    Texto.setRows(0);
    Texto.setWrapStyleWord(false);
    Texto.setBounds(new Rectangle(32, 288, 279, 184));
    jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 14));
    jTextArea1.setForeground(Color.black);
    jTextArea1.setAlignmentX( (float) 0.5);
    jTextArea1.setBorder(BorderFactory.createLineBorder(Color.black));
    jTextArea1.setEditable(false);
    jTextArea1.setTabSize(10);
    jTextArea1.setBounds(new Rectangle(292, 188, 150, 46));
    jTextField1.setBounds(new Rectangle(331, 195, 118, 26));
    MontaM.setBounds(new Rectangle(329, 121, 151, 60));
    MontaM.setFont(new java.awt.Font("Dialog", 1, 15));
    MontaM.setForeground(Color.black);
    MontaM.setText("Carregar Jogo");
    MontaM.addActionListener(new Janela1_MontaM_actionAdapter(this));

    Sair.setBounds(new Rectangle(231, 518, 122, 48));
    Sair.setFont(new java.awt.Font("Dialog", 1, 20));
    Sair.setDoubleBuffered(false);
    Sair.setText("Sair");
    Sair.addActionListener(new Janela1_Sair_actionAdapter(this));
    EntradaTexto.setBorder(BorderFactory.createLineBorder(Color.black));
    EntradaTexto.setEditable(false);
    EntradaTexto.setText("");
    EntradaTexto.setBounds(new Rectangle(31, 24, 279, 216));
    Painel.add(EntradaTexto, null);
    Painel.add(Sair, null);
    Painel.add(Texto, null);
    Painel.add(Resolve, null);
    Painel.add(MontaM, null);
    jPopupMenu1.addSeparator();
    jPopupMenu1.add(jMenuItem2);
    jPopupMenu1.add(jMenuItem1);
    contentPane.add(Painel, BorderLayout.CENTER);
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }
 JPopupMenu jPopupMenu1 = new JPopupMenu();
 JMenuItem jMenuItem1 = new JMenuItem();
 JMenuItem jMenuItem2 = new JMenuItem();
 JTextArea Texto = new JTextArea();
 JTextArea jTextArea1 = new JTextArea();
 TitledBorder titledBorder1;
 TitledBorder titledBorder2;
 JTextField jTextField1 = new JTextField();
 TitledBorder titledBorder3;
 TitledBorder titledBorder4;
 TitledBorder titledBorder5;
 TitledBorder titledBorder6;
 TitledBorder titledBorder7;
 JButton MontaM = new JButton();
 JButton Sair = new JButton();
 JTextArea EntradaTexto = new JTextArea();


//*********************************************************
//*              Tudo começa aqui                         *
//*              PROGRAMA PRINCIAL                        *
//*********************************************************
      void Resolve_actionPerformed(ActionEvent e) {
      Resolve.setEnabled(false);
      long tempo1 = System.currentTimeMillis();

      boolean terminou = false;
      StringBuffer Buffer = new StringBuffer();
      boolean ERROcritico=false; //
      long tempofinal;

      Fechado.clear();
         while(terminou==false)
         //for(int i=0;i<100;i++)
         {
           //System.out.print(arvore.size());
           if (arvore.size()>=1 && ERROcritico==false ) {
             log2_Abertos(""+ Aberto); // log dos Abertos
             log1_arvore("" + arvore); // log da Arvore
             Matriz.append(arvore.removeFirst());
             Fechado.add("" + Matriz); //Adiciona a lista dos fechados


             if (testaFinal() == false) {

               move_direita();
               move_esquerda();
               move_cima();
               move_baixo();

               log1_arvore("" + arvore);
               Valor_G++; //Soma o valor de G

               // AQUI ESCOLHE A HEURISTICA //

              Buffer=(menor_caminho(Valor_G)); //heuristica distância Manhattan
              //Buffer=(menor_caminho_H2(Valor_G)); //heuristica peças fora do lugar
              //Buffer=(menor_caminho_H3(Valor_G)); //heuristica distancia euclidiana

              /////////////////////////////

               if(Buffer.length()<=0)
               {
                 ERROcritico=true;
               }


// SAÍDA DO PROGRAMA - TELA //
               System.out.println("arvore "+arvore);
               System.out.println("menor caminho "+Buffer);
               System.out.println("Profundidade "+Valor_G);
               //long tempo2 = System.currentTimeMillis();
               //tempofinal=tempo2-tempo1;
               //System.out.println("Tempo "+tempofinal);
               //System.out.println("Fechados "+Fechado.size()+ " Abertos "+ Aberto.size());

               for(int clear=0;clear<18;clear++)
               {System.out.println(" ");}

////////////////////////////////////////////////


                 arvore.clear();
                 Matriz.delete(0, Matriz.length());
                 arvore.add(Buffer);


             }
             else {
               terminou = true;
             }
           }
           else
           // SE NAO ACHAR RESPOSTA ///
           {System.out.print("Impossivel achar a resposta !");
             Texto.append("Impossivel achar a resposta !");
             long tempo2 = System.currentTimeMillis();
             tempofinal=tempo2-tempo1;
             Texto.append("Tempo: "+tempofinal+" ms"+"\n");

             break;}
         }
         long tempo2 = System.currentTimeMillis();
         tempofinal=tempo2-tempo1;
         Texto.append("Tempo: "+tempofinal+" ms"+"\n");




      }

//***************** Termina aqui *****************************


  void Sair_actionPerformed(ActionEvent e) {
    System.exit(0);
  } //SAIR SYSOP




////////////////////////////////////////////////////////////////////////////////
// Lê os dados de entrada !!! :-)  //SYSOP
////////////////////////////////////////////////////////////////////////////////
  void MontaM_actionPerformed(ActionEvent e) {
    MontaM.setEnabled(false);
    try {
      FileInputStream FILEentrada = new FileInputStream(file1);
      InputStreamReader streamReader = new InputStreamReader(FILEentrada);
      BufferedReader reader = new BufferedReader(streamReader);
      String line = null;
      while ( (line = reader.readLine()) != null) {
        Entrada.append(line);
      }
    }
    catch (IOException ioe) {
      System.out.println("ERRO !!!!! ");
    }
    int local, local1;
    local = Entrada.indexOf("E");
    local1 = Entrada.indexOf("S");
    //Separa para descobrir as linhas e colunas o valor da matriz de ent e a de saida
    formacao = Entrada.substring(0, local); //set entrada M x N
    arvore.add(Entrada.substring(local + 1, local1)); //Carrega como lista
    Matriz.append(Entrada.substring(local + 1, local1)); // só perfumaria
    Final.append(Entrada.substring(local1 + 1, Entrada.length())); //carrega o final



    M = Integer.parseInt(formacao.substring(0, 1));
    N = Integer.parseInt(formacao.substring(1, 2));

    tabuleiro = new int[M][N];
    meta = new int[M][N];

   tabuleiro=CalculaMatriz(Matriz);
   meta=CalculaMatriz(Final);


   Matriz.delete(0, Matriz.length()); //limpa a matriz para receber depois novos valores


    ///PERFUMARIA PARA FICAR BONITINHO
    EntradaTexto.append("M-> " + M + " N-> " + N + "\n");

    EntradaTexto.append("ENTRADA" + "\n");
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        EntradaTexto.append("" + tabuleiro[i][j] + " ");
      }
      EntradaTexto.append("\n");
    }
    EntradaTexto.append("META" + "\n");

    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        EntradaTexto.append("" + meta[i][j] + " ");

      }
      EntradaTexto.append("\n");
    }
    ///FINAL PERFUMARIA

  }

}

/// codigo gerado pelo JAVA ////////////////////////////////
class Janela1_Resolve_actionAdapter
    implements java.awt.event.ActionListener {
  Janela1 adaptee;

  Janela1_Resolve_actionAdapter(Janela1 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.Resolve_actionPerformed(e);
  }
}

class Janela1_Sair_actionAdapter
    implements java.awt.event.ActionListener {
  Janela1 adaptee;

  Janela1_Sair_actionAdapter(Janela1 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.Sair_actionPerformed(e);
  }
}

class Janela1_MontaM_actionAdapter
    implements java.awt.event.ActionListener {
  Janela1 adaptee;

  Janela1_MontaM_actionAdapter(Janela1 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.MontaM_actionPerformed(e);
  }
}