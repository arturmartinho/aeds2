public class InversaoRecursiva {
    public static String inverter(String s) {
        return inverter(s, s.length() - 1, "");
    }

    private static String inverter(String s, int i, String resp) {
        if (i < 0) {
            return resp;
        }
        return inverter(s, i - 1, resp + s.charAt(i));
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
