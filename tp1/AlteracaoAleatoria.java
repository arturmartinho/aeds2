import java.util.Random;

public class AlteracaoAleatoria {
    public static String alterar(String s, char letra1, char letra2) {
        String resp = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == letra1) {
                resp += letra2;
            } else {
                resp += c;
            }
        }
        return resp;
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        Random gerador = new Random();
        gerador.setSeed(4);
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            char letra1 = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));
            char letra2 = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));
            MyIO.println(alterar(linha, letra1, letra2));
            linha = MyIO.readLine();
        }
    }
}
