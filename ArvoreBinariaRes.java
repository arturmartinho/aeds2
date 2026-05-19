import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

class Data {
    private int ano, mes, dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public static Data parseData(String s) {
        int ano = 0, mes = 0, dia = 0, i = 0;
        while (s.charAt(i) != '-') { ano = ano * 10 + (s.charAt(i++) - '0'); }
        i++;
        while (s.charAt(i) != '-') { mes = mes * 10 + (s.charAt(i++) - '0'); }
        i++;
        while (i < s.length()) { dia = dia * 10 + (s.charAt(i++) - '0'); }
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
        int h = 0, m = 0, i = 0;
        while (s.charAt(i) != ':') { h = h * 10 + (s.charAt(i++) - '0'); }
        i++;
        while (i < s.length()) { m = m * 10 + (s.charAt(i++) - '0'); }
        return new Hora(h, m);
    }

    public String formatar() {
        return String.format("%02d:%02d", hora, minuto);
    }
}

class Restaurante {
    private int id, capacidade, faixaPreco;
    private double avaliacao;
    public String nome, cidade;
    private String[] tiposCozinha;
    private Hora abertura, fechamento;
    private Data dataAbertura;
    private boolean aberto;

    public int getId() { return id; }

    public static int stringParaInt(String s) {
        int num = 0;
        for (int i = 0; i < s.length(); i++) num = num * 10 + (s.charAt(i) - '0');
        return num;
    }

    public static double stringParaDouble(String s) {
        double num = 0, dec = 0, fator = 0.1;
        boolean depois = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.') depois = true;
            else {
                int d = c - '0';
                if (!depois) num = num * 10 + d;
                else { dec += d * fator; fator /= 10; }
            }
        }
        return num + dec;
    }

    public static String[] separar(String s, char sep) {
        String[] temp = new String[20];
        int count = 0;
        String atual = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == sep) { temp[count++] = atual; atual = ""; }
            else atual += s.charAt(i);
        }
        temp[count++] = atual;
        String[] resp = new String[count];
        for (int i = 0; i < count; i++) resp[i] = temp[i];
        return resp;
    }

    public static int compararStrings(String a, String b) {
        int resp = 0;
        int i = 0;
        int lenA = a.length();
        int lenB = b.length();

        while (i < lenA && i < lenB && resp == 0) {
            if (a.charAt(i) < b.charAt(i)) resp = -1;
            else if (a.charAt(i) > b.charAt(i)) resp = 1;
            i++;
        }

        if (resp == 0) {
            if (lenA < lenB) resp = -1;
            else if (lenA > lenB) resp = 1;
        }
        return resp;
    }

    public static Restaurante parseRestaurante(String s) {
        Restaurante r = new Restaurante();

        String[] partes = separar(s, ',');

        r.id = stringParaInt(partes[0]);

        r.nome = partes[1];

        r.cidade = partes[2];

        r.capacidade = stringParaInt(partes[3]);

        r.avaliacao = stringParaDouble(partes[4]);

        r.tiposCozinha = separar(partes[5], ';');

        r.faixaPreco = partes[6].length();

        String[] horas = separar(partes[7], '-');

        r.abertura = Hora.parseHora(horas[0]);

        r.fechamento = Hora.parseHora(horas[1]);

        r.dataAbertura = Data.parseData(partes[8]);

        r.aberto = (partes[9].length() == 4 && partes[9].charAt(0) == 't');

        return r;
    }

    public String formatar() {
        String tipos = "[";
        for (int i = 0; i < tiposCozinha.length; i++) {
            tipos += tiposCozinha[i] + (i < tiposCozinha.length - 1 ? "," : "");
        }
        tipos += "]";
        String cifroes = "";
        for (int i = 0; i < faixaPreco; i++) cifroes += "$";
        
        return String.format("[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %b]",
            id, nome, cidade, capacidade, avaliacao, tipos, cifroes, 
            abertura.formatar(), fechamento.formatar(), dataAbertura.formatar(), aberto);
    }
}

class No {
    public Restaurante elemento;
    public No esq, dir;
    public No(Restaurante elemento) { this.elemento = elemento; this.esq = this.dir = null; }
}

class ArvoreBinaria {
    private No raiz;
    public long comparacoes;

    public ArvoreBinaria() { 
        raiz = null; comparacoes = 0; 
    }

    public void inserir(Restaurante x) { 
        raiz = inserir(x, raiz); 
    }

    private No inserir(Restaurante x, No i) {
        if (i == null) {
            i = new No(x);
        } else {
            int comp = Restaurante.compararStrings(x.nome, i.elemento.nome);
            if (comp < 0) i.esq = inserir(x, i.esq);
            else if (comp > 0) i.dir = inserir(x, i.dir);
        }
        return i;
    }

    public void pesquisar(String nome) {
        System.out.print("raiz ");
        boolean encontrado = pesquisar(nome, raiz);
        if (encontrado) System.out.println("SIM");
        else System.out.println("NAO");
    }

    private boolean pesquisar(String nome, No i) {
        boolean resp = false;
        if (i != null) {
            comparacoes++;
            int comp = Restaurante.compararStrings(nome, i.elemento.nome);
            if (comp == 0) {
                resp = true;
            } else if (comp < 0) {
                System.out.print("esq ");
                resp = pesquisar(nome, i.esq);
            } else {
                System.out.print("dir ");
                resp = pesquisar(nome, i.dir);
            }
        }
        return resp;
    }

    public void mostrarEmOrdem() { mostrarEmOrdem(raiz); }
    private void mostrarEmOrdem(No i) {
        if (i != null) {
            mostrarEmOrdem(i.esq);
            System.out.println(i.elemento.formatar());
            mostrarEmOrdem(i.dir);
        }
    }
}

public class ArvoreBinariaRes {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        long inicio = System.currentTimeMillis();

        Restaurante[] base = new Restaurante[1000];
        int tamBase = 0;
        Scanner scFile = new Scanner(new File("/tmp/restaurantes.csv"));
        if (scFile.hasNextLine()) scFile.nextLine();
        while (scFile.hasNextLine()) base[tamBase++] = Restaurante.parseRestaurante(scFile.nextLine());
        scFile.close();

        ArvoreBinaria arvore = new ArvoreBinaria();

        int id = sc.nextInt();
        while (id != -1) {
            for (int i = 0; i < tamBase; i++) {
                if (base[i].getId() == id) {
                    arvore.inserir(base[i]);
                    i = tamBase;
                }
            }
            id = sc.nextInt();
        }
        sc.nextLine();

        String nome = sc.nextLine();
        while (Restaurante.compararStrings(nome, "FIM") != 0) {
            arvore.pesquisar(nome);
            nome = sc.nextLine();
        }

        arvore.mostrarEmOrdem();

        long fim = System.currentTimeMillis();
        FileWriter log = new FileWriter("856245_arvore_binaria.txt");
        log.write("856245\t" + arvore.comparacoes + "\t" + (fim - inicio));
        log.close();
        sc.close();
    }
}
//Felipe de Faria Rios Coelho
