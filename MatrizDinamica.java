import java.util.Scanner;

class Celula {
    public int elemento;
    public Celula sup, inf, esq, dir;

    public Celula() {
        this(0);
    }

    public Celula(int elemento) {
        this.elemento = elemento;
        this.sup = null;
        this.inf = null;
        this.esq = null;
        this.dir = null;
    }
}

class Matriz {
    private Celula inicio;
    private int linha, coluna;

    public Matriz(int linha, int coluna) {

        this.linha = linha;
        this.coluna = coluna;

        inicio = new Celula();

        Celula i = inicio;

        int c = 1;

        while (c < coluna) {

            i.dir = new Celula();

            i.dir.esq = i;

            i = i.dir;

            c++;
        }

        Celula linhaAtual = inicio;

        int l = 1;

        while (l < linha) {

            linhaAtual.inf = new Celula();

            linhaAtual.inf.sup = linhaAtual;

            Celula acima = linhaAtual;
            Celula atual = linhaAtual.inf;

            c = 1;

            while (c < coluna) {

                atual.dir = new Celula();

                atual.dir.esq = atual;

                acima = acima.dir;

                acima.inf = atual.dir;

                atual.dir.sup = acima;

                atual = atual.dir;

                c++;
            }

            linhaAtual = linhaAtual.inf;

            l++;
        }
    }

    public void inserir(Scanner sc) {

        Celula linhaAtual = inicio;

        int i = 0;

        while (i < linha) {

            Celula colAtual = linhaAtual;

            int j = 0;

            while (j < coluna) {

                colAtual.elemento = sc.nextInt();

                colAtual = colAtual.dir;

                j++;
            }

            linhaAtual = linhaAtual.inf;

            i++;
        }
    }

    public void mostrarDiagonalPrincipal() {

        Celula i = inicio;

        int cont = 0;

        while (cont < linha) {

            System.out.print(i.elemento);

            if (cont < linha - 1) {
                System.out.print(" ");
            }

            if (i.inf != null) {
                i = i.inf.dir;
            }

            cont++;
        }

        System.out.println();
    }

    public void mostrarDiagonalSecundaria() {

        Celula i = inicio;

        int c = 1;

        while (c < coluna) {
            i = i.dir;
            c++;
        }

        int cont = 0;

        while (cont < linha) {

            System.out.print(i.elemento);

            if (cont < linha - 1) {
                System.out.print(" ");
            }

            if (i.inf != null) {
                i = i.inf.esq;
            }

            cont++;
        }

        System.out.println();
    }

    public Matriz somar(Matriz m) {

        Matriz resp = new Matriz(linha, coluna);

        Celula linhaA = inicio;
        Celula linhaB = m.inicio;
        Celula linhaR = resp.inicio;

        int i = 0;

        while (i < linha) {

            Celula a = linhaA;
            Celula b = linhaB;
            Celula r = linhaR;

            int j = 0;

            while (j < coluna) {

                r.elemento = a.elemento + b.elemento;

                a = a.dir;
                b = b.dir;
                r = r.dir;

                j++;
            }

            linhaA = linhaA.inf;
            linhaB = linhaB.inf;
            linhaR = linhaR.inf;

            i++;
        }

        return resp;
    }

    public Matriz multiplicar(Matriz m) {

        Matriz resp = new Matriz(linha, m.coluna);

        Celula linhaA = inicio;
        Celula linhaR = resp.inicio;

        int i = 0;

        while (i < linha) {

            Celula colunaBInicio = m.inicio;
            Celula r = linhaR;

            int j = 0;

            while (j < m.coluna) {

                Celula a = linhaA;
                Celula b = colunaBInicio;

                int soma = 0;

                int k = 0;

                while (k < coluna) {

                    soma += a.elemento * b.elemento;

                    a = a.dir;
                    b = b.inf;

                    k++;
                }

                r.elemento = soma;

                colunaBInicio = colunaBInicio.dir;
                r = r.dir;

                j++;
            }

            linhaA = linhaA.inf;
            linhaR = linhaR.inf;

            i++;
        }

        return resp;
    }

    public void mostrar() {

        Celula linhaAtual = inicio;

        int i = 0;

        while (i < linha) {

            Celula colAtual = linhaAtual;

            int j = 0;

            while (j < coluna) {

                System.out.print(colAtual.elemento);

                if (j < coluna - 1) {
                    System.out.print(" ");
                }

                colAtual = colAtual.dir;

                j++;
            }

            System.out.println();

            linhaAtual = linhaAtual.inf;

            i++;
        }
    }
}

public class MatrizDinamica {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int casos = sc.nextInt();

        int t = 0;

        while (t < casos) {

            int l1 = sc.nextInt();
            int c1 = sc.nextInt();

            Matriz m1 = new Matriz(l1, c1);

            m1.inserir(sc);

            int l2 = l1;
            int c2 = c1;

            Matriz m2 = new Matriz(l2, c2);

            m2.inserir(sc);

            m1.mostrarDiagonalPrincipal();
            
            m2.mostrarDiagonalSecundaria();

            Matriz soma = m1.somar(m2);

            soma.mostrar();

            Matriz mult = m1.multiplicar(m2);

            mult.mostrar();

            t++;
        }

        sc.close();
    }
}
// Felipe de Faria Rios Coelho
