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
    private String[] tiposCozinha;
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

                int dig = s.charAt(i) - '0';

                if (depois == false) {
                    num = num * 10 + dig;
                } else {
                    dec += dig * fator;
                    fator /= 10;
                }
            }
        }

        return num + dec;
    }

    public static boolean stringParaBoolean(String s) {
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

    public static Restaurante parseRestaurante(String s) {

        Restaurante r = new Restaurante();

        String[] p = separar(s, ',');

        r.id = stringParaInt(p[0]);
        r.nome = p[1];
        r.cidade = p[2];

        r.capacidade = stringParaInt(p[3]);
        r.avaliacao = stringParaDouble(p[4]);

        r.tiposCozinha = separar(p[5], ';');

        r.faixaPreco = p[6].length();

        String[] h = separar(p[7], '-');

        r.abertura = Hora.parseHora(h[0]);
        r.fechamento = Hora.parseHora(h[1]);

        r.dataAbertura = Data.parseData(p[8]);

        r.aberto = stringParaBoolean(p[9]);

        return r;
    }

    public String formatar() {

        String resp = "[" + id + " ## " + nome + " ## " + cidade +
                " ## " + capacidade + " ## " + avaliacao + " ## [";

        for (int i = 0; i < tiposCozinha.length; i++) {

            resp += tiposCozinha[i];

            if (i < tiposCozinha.length - 1) {
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

class CelulaDupla {

    public Restaurante elemento;
    public CelulaDupla ant;
    public CelulaDupla prox;

    public CelulaDupla() {
        this(null);
    }

    public CelulaDupla(Restaurante x) {
        elemento = x;
        ant = null;
        prox = null;
    }
}

class ListaDupla {

    private CelulaDupla primeiro;
    private CelulaDupla ultimo;

    public ListaDupla() {
        primeiro = new CelulaDupla();
        ultimo = primeiro;
    }

    public void inserirInicio(Restaurante x) {

        CelulaDupla tmp = new CelulaDupla(x);

        tmp.ant = primeiro;
        tmp.prox = primeiro.prox;

        if (primeiro.prox != null) {
            primeiro.prox.ant = tmp;
        }

        primeiro.prox = tmp;

        if (primeiro == ultimo) {
            ultimo = tmp;
        }
    }

    public void inserirFim(Restaurante x) {

        CelulaDupla tmp = new CelulaDupla(x);

        ultimo.prox = tmp;
        tmp.ant = ultimo;

        ultimo = tmp;
    }

    public void inserir(Restaurante x, int pos) {

        int tam = tamanho();

        if (pos == 0) {
            inserirInicio(x);

        } else if (pos == tam) {
            inserirFim(x);

        } else {

            CelulaDupla i = primeiro.prox;

            for (int j = 0; j < pos; j++) {
                i = i.prox;
            }

            CelulaDupla tmp = new CelulaDupla(x);

            tmp.ant = i.ant;
            tmp.prox = i;

            i.ant.prox = tmp;
            i.ant = tmp;
        }
    }

    public Restaurante removerInicio() {

        CelulaDupla tmp = primeiro.prox;

        primeiro.prox = tmp.prox;

        if (tmp.prox != null) {
            tmp.prox.ant = primeiro;
        }

        if (tmp == ultimo) {
            ultimo = primeiro;
        }

        return tmp.elemento;
    }

    public Restaurante removerFim() {

        Restaurante resp = ultimo.elemento;

        ultimo = ultimo.ant;
        ultimo.prox = null;

        return resp;
    }

    public Restaurante remover(int pos) {

        Restaurante resp;

        int tam = tamanho();

        if (pos == 0) {

            resp = removerInicio();

        } else if (pos == tam - 1) {

            resp = removerFim();

        } else {

            CelulaDupla i = primeiro.prox;

            for (int j = 0; j < pos; j++) {
                i = i.prox;
            }

            i.ant.prox = i.prox;
            i.prox.ant = i.ant;

            resp = i.elemento;
        }

        return resp;
    }

    public int tamanho() {

        int tam = 0;

        CelulaDupla i = primeiro.prox;

        while (i != null) {
            tam++;
            i = i.prox;
        }

        return tam;
    }

    public void mostrar() {

        CelulaDupla i = primeiro.prox;

        while (i != null) {
            System.out.println(i.elemento.formatar());
            i = i.prox;
        }
    }
}

class ColecaoRestaurantes {

    private Restaurante[] restaurantes;
    private int tamanho;

    public ColecaoRestaurantes() {

        restaurantes = new Restaurante[1000];
        tamanho = 0;
    }

    public void lerCsv(String path) {

        try {

            Scanner sc = new Scanner(new File(path));

            sc.nextLine();

            while (sc.hasNextLine()) {

                restaurantes[tamanho] = Restaurante.parseRestaurante(sc.nextLine());

                tamanho++;
            }

            sc.close();

        } catch (Exception e) {
        }
    }

    public Restaurante buscar(int id) {

        Restaurante resp = null;

        for (int i = 0; i < tamanho; i++) {

            if (restaurantes[i].getId() == id) {

                resp = restaurantes[i];

                i = tamanho;
            }
        }

        return resp;
    }
}

public class ListaDuplamenteEnc{

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        ColecaoRestaurantes base = new ColecaoRestaurantes();

        base.lerCsv("/tmp/restaurantes.csv");

        ListaDupla lista = new ListaDupla();

        int id = sc.nextInt();

        while (id != -1) {

            lista.inserirFim(base.buscar(id));

            id = sc.nextInt();
        }

        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {

            String linha = sc.nextLine();

            String[] p = Restaurante.separar(linha, ' ');

            if (p[0].charAt(0) == 'I' && p[0].charAt(1) == 'I') {

                lista.inserirInicio(base.buscar(Integer.parseInt(p[1])));

            } else if (p[0].charAt(0) == 'I' && p[0].charAt(1) == 'F') {

                lista.inserirFim(base.buscar(Integer.parseInt(p[1])));

            } else if (p[0].charAt(0) == 'I' && p[0].charAt(1) == '*') {

                lista.inserir(
                        base.buscar(Integer.parseInt(p[2])),
                        Integer.parseInt(p[1]));

            } else if (p[0].charAt(0) == 'R' && p[0].charAt(1) == 'I') {

                Restaurante r = lista.removerInicio();

                System.out.println("(R)" + r.getNome());

            } else if (p[0].charAt(0) == 'R' && p[0].charAt(1) == 'F') {

                Restaurante r = lista.removerFim();

                System.out.println("(R)" + r.getNome());

            } else {

                Restaurante r = lista.remover(Integer.parseInt(p[1]));

                System.out.println("(R)" + r.getNome());
            }
        }

        lista.mostrar();

        sc.close();
    }
}

