public class Inversao {
    public static String inverter(String s) {
        String resp = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            resp += s.charAt(i);
        }
        return resp;
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(inverter(linha));
            linha = MyIO.readLine();
        }
    }
}
