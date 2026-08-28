public class CiframentoRecursivo {
    public static String cifrar(String s) {
        return cifrar(s, 0, "");
    }

    private static String cifrar(String s, int i, String resp) {
        if (i == s.length()) {
            return resp;
        }
        char c = s.charAt(i);
        if (c >= 'a' && c <= 'z') {
            c = (char) ((c - 'a' + 3) % 26 + 'a');
        } else if (c >= 'A' && c <= 'Z') {
            c = (char) ((c - 'A' + 3) % 26 + 'A');
        }
        return cifrar(s, i + 1, resp + c);
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(cifrar(linha));
            linha = MyIO.readLine();
        }
    }
}
