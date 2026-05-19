import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

class Data {
    private int ano, mes, dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public static Data parseData(String s) {

        int ano = 0, mes = 0, dia = 0;

        int i = 0;

        while (s.charAt(i) != '-') {
            ano = ano * 10 + (s.charAt(i) - '0');
            i++;
        }

        i++;

        while (s.charAt(i) != '-') {
            mes = mes * 10 + (s.charAt(i) - '0');
            i++;
        }

        i++;

        while (i < s.length()) {
            dia = dia * 10 + (s.charAt(i) - '0');
            i++;
        }

        return new Data(ano, mes, dia);
    }

    public String formatar() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}

class Hora {
    private int hora, minuto;

    public Hora(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
    }

    public static Hora parseHora(String s) {

        int hora = 0, minuto = 0;

        int i = 0;

        while (s.charAt(i) != ':') {
            hora = hora * 10 + (s.charAt(i) - '0');
            i++;
        }

        i++;

        while (i < s.length()) {
            minuto = minuto * 10 + (s.charAt(i) - '0');
            i++;
        }

        return new Hora(hora, minuto);
    }

    public String formatar() {
        return String.format("%02d:%02d", hora, minuto);
    }
}

class Restaurante {

    private int id, capacidade, faixaPreco;
    private double avaliacao;

    private String nome, cidade;

    private String[] tipos;
    private int qtdTipos;

    private Hora abertura, fechamento;

    private Data dataAbertura;

    private boolean aberto;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public static int stringParaInt(String s) {

        int num = 0;

        int i = 0;

        while (i < s.length()) {

            num = num * 10 + (s.charAt(i) - '0');

            i++;
        }

        return num;
    }

    public static double stringParaDouble(String s) {

        double num = 0;
        double dec = 0;

        double fator = 0.1;

        boolean depois = false;

        int i = 0;

        while (i < s.length()) {

            if (s.charAt(i) == '.') {

                depois = true;

            } else {

                int d = s.charAt(i) - '0';

                if (depois == false) {

                    num = num * 10 + d;

                } else {

                    dec += d * fator;

                    fator /= 10;
                }
            }

            i++;
        }

        return num + dec;
    }

    public static boolean stringParaBool(String s) {

        boolean resp = false;

        if (s.charAt(0) == 't' &&
                s.charAt(1) == 'r' &&
                s.charAt(2) == 'u' &&
                s.charAt(3) == 'e') {

            resp = true;
        }

        return resp;
    }

    public static String[] separar(String s, char sep) {

        String[] temp = new String[20];

        int qtd = 0;

        String atual = "";

        int i = 0;

        while (i < s.length()) {

            if (s.charAt(i) == sep) {

                temp[qtd] = atual;

                qtd++;

                atual = "";

            } else {

                atual += s.charAt(i);
            }

            i++;
        }

        temp[qtd] = atual;

        qtd++;

        String[] resp = new String[qtd];

        i = 0;

        while (i < qtd) {

            resp[i] = temp[i];

            i++;
        }

        return resp;
    }

    public static Restaurante parseRestaurante(String linha) {

        Restaurante r = new Restaurante();

        String[] partes = separar(linha, ',');

        r.id = stringParaInt(partes[0]);

        r.nome = partes[1];

        r.cidade = partes[2];

        r.capacidade = stringParaInt(partes[3]);

        r.avaliacao = stringParaDouble(partes[4]);

        r.tipos = separar(partes[5], ';');

        r.qtdTipos = r.tipos.length;

        r.faixaPreco = partes[6].length();

        String[] horas = separar(partes[7], '-');

        r.abertura = Hora.parseHora(horas[0]);

        r.fechamento = Hora.parseHora(horas[1]);

        r.dataAbertura = Data.parseData(partes[8]);

        r.aberto = stringParaBool(partes[9]);

        return r;
    }

    public String formatar() {

        String resp = "[" + id + " ## " + nome + " ## " + cidade +
                " ## " + capacidade + " ## " + avaliacao + " ## [";

        int i = 0;

        while (i < qtdTipos) {

            resp += tipos[i];

            if (i < qtdTipos - 1) {
                resp += ",";
            }

            i++;
        }

        resp += "] ## ";

        i = 0;

        while (i < faixaPreco) {

            resp += "$";

            i++;
        }

        resp += " ## " + abertura.formatar() + "-" + fechamento.formatar();

        resp += " ## " + dataAbertura.formatar();

        resp += " ## " + aberto + "]";

        return resp;
    }
}

class Celula {

    public Restaurante elemento;

    public Celula prox;
    public Celula ant;

    public Celula(Restaurante elemento) {

        this.elemento = elemento;

        this.prox = null;
        this.ant = null;
    }
}

class ListaDupla {

    private Celula primeiro;
    private Celula ultimo;

    public ListaDupla() {

        primeiro = new Celula(null);

        ultimo = primeiro;
    }

    public void inserirFim(Restaurante x) {

        Celula tmp = new Celula(x);
        ultimo.prox = tmp;
        tmp.ant = ultimo;
        ultimo = tmp;
    }

    public void mostrar() {

        Celula i = primeiro.prox;

        while (i != null) {
            System.out.println(i.elemento.formatar());
            i = i.prox;
        }
    }

    public Celula getPrimeiro() {
        return primeiro.prox;
    }

    public Celula getUltimo() {
        return ultimo;
    }
}

public class QuicksortListaDuplaFlex {

    public static long comparacoes = 0;
    public static long movimentacoes = 0;

    public static int compararString(String a, String b) {

        int i = 0;

        int resp = 0;

        while (i < a.length() && i < b.length() && resp == 0) {

            comparacoes++;

            if (a.charAt(i) < b.charAt(i)) {

                resp = -1;

            } else if (a.charAt(i) > b.charAt(i)) {

                resp = 1;
            }

            i++;
        }

        if (resp == 0) {

            if (a.length() < b.length()) {

                resp = -1;

            } else if (a.length() > b.length()) {

                resp = 1;
            }
        }

        return resp;
    }

    public static int compararRestaurante(Restaurante a, Restaurante b) {

        int resp = 0;

        comparacoes++;

        if (a.getAvaliacao() < b.getAvaliacao()) {

            resp = -1;

        } else if (a.getAvaliacao() > b.getAvaliacao()) {

            resp = 1;

        } else {

            resp = compararString(a.getNome(), b.getNome());
        }

        return resp;
    }

    public static void swap(Celula a, Celula b) {

        Restaurante temp = a.elemento;

        a.elemento = b.elemento;

        b.elemento = temp;

        movimentacoes += 3;
    }

    public static Celula meio(Celula esq, Celula dir) {

        Celula i = esq;

        Celula j = esq;

        while (j != dir &&
                j.prox != dir) {

            j = j.prox;

            if (j != dir) {

                j = j.prox;

                i = i.prox;
            }
        }

        return i;
    }

    public static void quicksort(Celula esq, Celula dir) {

        if (esq != null &&dir != null &&esq != dir &&esq != dir.prox) {

            Celula i = esq;

            Celula j = dir;

            Restaurante pivo = meio(esq, dir).elemento;

            while (true) {

                while (compararRestaurante(i.elemento, pivo) < 0) {
                    i = i.prox;
                }

                while (compararRestaurante(j.elemento, pivo) > 0) {
                    j = j.ant;
                }

                boolean parar = false;

                Celula tmp = i;

                while (tmp != null && tmp != j.prox && parar == false) {

                    if (tmp == j) {
                        parar = true;
                    }

                    tmp = tmp.prox;
                }

                if (parar == false) {
                    break;
                }

                swap(i, j);

                i = i.prox;

                j = j.ant;
            }

            quicksort(esq, j);

            quicksort(i, dir);
        }
    }

    public static Restaurante buscar(Restaurante[] lista, int n, int id) {

        Restaurante resp = null;

        int i = 0;

        while (i < n) {

            if (lista[i].getId() == id) {

                resp = lista[i];

                i = n;
            }

            i++;
        }

        return resp;
    }

    public static void main(String[] args) throws Exception {

        Scanner arq = new Scanner(new File("/tmp/restaurantes.csv"));

        Restaurante[] lista = new Restaurante[1000];

        int tamanho = 0;

        arq.nextLine();

        while (arq.hasNextLine()) {

            lista[tamanho] = Restaurante.parseRestaurante(arq.nextLine());

            tamanho++;
        }

        arq.close();

        Scanner sc = new Scanner(System.in);

        ListaDupla ld = new ListaDupla();

        int id = sc.nextInt();

        while (id != -1) {

            ld.inserirFim(buscar(lista, tamanho, id));

            id = sc.nextInt();
        }

        long inicio = System.currentTimeMillis();

        quicksort(ld.getPrimeiro(), ld.getUltimo());

        long fim = System.currentTimeMillis();

        ld.mostrar();

        double tempo = (fim - inicio) / 1000.0;

        PrintWriter log = new PrintWriter(new FileWriter("1593221_quicksort.txt"));

        log.println("1593221\t" + comparacoes + "\t" + movimentacoes + "\t" + tempo);

        log.close();

        sc.close();
    }
}
