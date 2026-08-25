public class Is {
    public static boolean isVogal(String s) {
        boolean resp = true;
        int i = 0;
        while (i < s.length() && resp) {
            char c = s.charAt(i);
            if (!(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
                  c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')) {
                resp = false;
            }
            i++;
        }
        return resp;
    }

    public static boolean isConsoante(String s) {
        boolean resp = true;
        int i = 0;
        while (i < s.length() && resp) {
            char c = s.charAt(i);
            if (!((c >= 'a' && c <= 'z' && c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u') ||
                  (c >= 'A' && c <= 'Z' && c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U'))) {
                resp = false;
            }
            i++;
        }
        return resp;
    }

    public static boolean isInteiro(String s) {
        boolean resp = true;
        int i = 0;
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            i = 1;
        }
        while (i < s.length() && resp) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                resp = false;
            }
            i++;
        }
        return resp && s.length() > 0;
    }

    public static boolean isReal(String s) {
        boolean resp = true;
        int i = 0;
        int pontos = 0;
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            i = 1;
        }
        while (i < s.length() && resp) {
            char c = s.charAt(i);
            if (c == '.') {
                pontos++;
                if (pontos > 1) {
                    resp = false;
                }
            } else if (c < '0' || c > '9') {
                resp = false;
            }
            i++;
        }
        return resp && s.length() > 0 && pontos == 1;
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            String vogal = isVogal(linha) ? "SIM" : "NAO";
            String consoante = isConsoante(linha) ? "SIM" : "NAO";
            String inteiro = isInteiro(linha) ? "SIM" : "NAO";
            String real = isReal(linha) ? "SIM" : "NAO";
            MyIO.println(vogal + " " + consoante + " " + inteiro + " " + real);
            linha = MyIO.readLine();
        }
    }
}
