import java.util.Scanner;
import java.io.File;

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

    public static int stringParaInt(String s) {
        int num = 0;

        for (int i = 0; i < s.length(); i++) {
            num = num * 10 + (s.charAt(i) - '0');
        }

        return num;
    }

    public static double stringParaDouble(String s) {
        double num = 0;
        double dec = 0;
        double fator = 0.1;

        boolean depois = false;

        for (int i = 0; i < s.length(); i++) {

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

class Celula {
    public Restaurante elemento;
    public Celula prox;

    public Celula(Restaurante elemento) {
        this.elemento = elemento;
        this.prox = null;
    }
}

class Pilha {
    private Celula topo;

    public Pilha() {
        topo = null;
    }

    public void empilhar(Restaurante x) {

        Celula tmp = new Celula(x);

        tmp.prox = topo;

        topo = tmp;
    }

    public Restaurante desempilhar() {

        Restaurante resp = topo.elemento;

        topo = topo.prox;

        return resp;
    }

    public void mostrar() {

        Celula i = topo;

        while (i != null) {
            System.out.println(i.elemento.formatar());
            i = i.prox;
        }
    }
}

public class PilhaAlocacaoFlexivel {

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

        Pilha pilha = new Pilha();

        int id = sc.nextInt();

        while (id != -1) {

            pilha.empilhar(buscar(lista, tamanho, id));

            id = sc.nextInt();
        }

        int n = sc.nextInt();

        int i = 0;

        while (i < n) {

            String comando = sc.next();

            if (comando.charAt(0) == 'I') {

                int idIns = sc.nextInt();

                pilha.empilhar(buscar(lista, tamanho, idIns));

            } else {

                Restaurante r = pilha.desempilhar();

                System.out.println("(R)" + r.getNome());
            }

            i++;
        }

        pilha.mostrar();

        sc.close();
    }
}
