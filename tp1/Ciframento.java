public class Ciframento {
    public static String cifrar(String s) {
        String resp = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            c = (char) (c + 3);
            resp += c;
        }
        return resp;
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        String linha = MyIO.readLine();
        while (linha != null && !linha.equals("FIM") && !linha.equals("")) {
            MyIO.println(cifrar(linha));
            linha = MyIO.readLine();
        }
    }
}
