public class Senha {
    public static boolean isSenhaValida(String s) {
        if (s.length() < 8) {
            return false;
        }
        boolean temMaiuscula = false;
        boolean temMinuscula = false;
        boolean temNumero = false;
        boolean temEspecial = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                temMaiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                temMinuscula = true;
            } else if (c >= '0' && c <= '9') {
                temNumero = true;
            } else {
                temEspecial = true;
            }
        }
        return temMaiuscula && temMinuscula && temNumero && temEspecial;
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(isSenhaValida(linha) ? "SIM" : "NAO");
            linha = MyIO.readLine();
        }
    }
}
