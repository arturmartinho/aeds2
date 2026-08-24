public class Ciframento {
    public static String cifrar(String s) {
        String resp = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z') {
                c = (char) ((c - 'a' + 3) % 26 + 'a');
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) ((c - 'A' + 3) % 26 + 'A');
            }
            resp += c;
        }
        return resp;
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
