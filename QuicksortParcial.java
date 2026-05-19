import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

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

    public double getAvaliacao() {
        return avaliacao;
    }

    public String getNome() {
        return nome;
    }

    public static int strParaInt(String s) {
        int num = 0;

        for (int i = 0; i < s.length(); i++) {
            num = num * 10 + (s.charAt(i) - '0');
        }

        return num;
    }

    public static double strParaDouble(String s) {
        double num = 0;
        double dec = 0;
        double fator = 0.1;
        boolean depois = false;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                depois = true;
            } else {
                int d = s.charAt(i) - '0';

                if (!depois) {
                    num = num * 10 + d;
                } else {
                    dec += d * fator;
                    fator /= 10;
                }
            }
        }

        return num + dec;
    }

    public static boolean strParaBool(String s) {
        boolean resp = false;

        if (s.length() >= 4 &&
                s.charAt(0) == 't' &&
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

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == sep) {
                temp[qtd] = atual;
                qtd++;
                atual = "";
            } else {
                atual += s.charAt(i);
            }
        }

        temp[qtd] = atual;
        qtd++;

        String[] resp = new String[qtd];

        for (int i = 0; i < qtd; i++) {
            resp[i] = temp[i];
        }

        return resp;
    }

    public static Restaurante parseRestaurante(String linha) {
        Restaurante r = new Restaurante();

        String[] partes = separar(linha, ',');

        r.id = strParaInt(partes[0]);
        r.nome = partes[1];
        r.cidade = partes[2];

        r.capacidade = strParaInt(partes[3]);
        r.avaliacao = strParaDouble(partes[4]);

        r.tipos = separar(partes[5], ';');
        r.qtdTipos = r.tipos.length;

        r.faixaPreco = partes[6].length();

        String[] horas = separar(partes[7], '-');

        r.abertura = Hora.parseHora(horas[0]);
        r.fechamento = Hora.parseHora(horas[1]);

        r.dataAbertura = Data.parseData(partes[8]);

        r.aberto = strParaBool(partes[9]);

        return r;
    }

    public String formatar() {
        String resp = "[" + id + " ## " + nome + " ## " + cidade + " ## " +
                capacidade + " ## " + avaliacao + " ## [";

        for (int i = 0; i < qtdTipos; i++) {
            resp += tipos[i];

            if (i < qtdTipos - 1) {
                resp += ",";
            }
        }

        resp += "] ## ";

        for (int i = 0; i < faixaPreco; i++) {
            resp += "$";
        }

        resp += " ## " + abertura.formatar() + "-" + fechamento.formatar();
        resp += " ## " + dataAbertura.formatar();
        resp += " ## " + aberto + "]";

        return resp;
    }
}

public class QuicksortParcial {

    static long comparacoes = 0;
    static long movimentacoes = 0;

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

    public static void swap(Restaurante[] lista, int i, int j) {
        Restaurante temp = lista[i];
        lista[i] = lista[j];
        lista[j] = temp;

        movimentacoes += 3;
    }

    public static void quicksort(Restaurante[] lista, int esq, int dir) {
        int i = esq;
        int j = dir;

        Restaurante pivo = lista[(esq + dir) / 2];

        while (i <= j) {

            while (compararRestaurante(lista[i], pivo) < 0) {
                i++;
            }

            while (compararRestaurante(lista[j], pivo) > 0) {
                j--;
            }

            if (i <= j) {
                swap(lista, i, j);
                i++;
                j--;
            }
        }

        if (esq < j) {
            quicksort(lista, esq, j);
        }

        if (i < 10 && i < dir) {
            quicksort(lista, i, dir);
        }
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(new File("/tmp/restaurantes.csv"));

        Restaurante[] lista = new Restaurante[1000];
        int tamanho = 0;

        sc.nextLine();

        while (sc.hasNextLine()) {
            lista[tamanho] = Restaurante.parseRestaurante(sc.nextLine());
            tamanho++;
        }

        sc.close();

        Scanner entrada = new Scanner(System.in);

        Restaurante[] selecionados = new Restaurante[1000];
        int qtd = 0;

        int id = entrada.nextInt();

        while (id != -1) {

            int i = 0;

            while (i < tamanho) {

                if (lista[i].getId() == id) {
                    selecionados[qtd] = lista[i];
                    qtd++;
                    i = tamanho;
                }

                i++;
            }

            id = entrada.nextInt();
        }

        long inicio = System.nanoTime();

        quicksort(selecionados, 0, qtd - 1);

        long fim = System.nanoTime();

        double tempo = (fim - inicio) / 1000000000.0;

        int i = 0;

        while (i < qtd) {
            System.out.println(selecionados[i].formatar());
            i++;
        }

        PrintWriter log = new PrintWriter(new FileWriter("1593221_quicksort_parcial.txt"));

        log.println("1593221\t" + comparacoes + "\t" + movimentacoes + "\t" + tempo);

        log.close();

        entrada.close();
    }
}
