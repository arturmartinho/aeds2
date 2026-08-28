public class Substring {
    public static int maiorSubstring(String s) {
        int max = 0;
        for (int i = 0; i < s.length(); i++) {
            int[] cont = new int[256];
            int j = i;
            while (j < s.length() && cont[s.charAt(j)] == 0) {
                cont[s.charAt(j)] = 1;
                j++;
            }
            int tamanho = j - i;
            if (tamanho > max) {
                max = tamanho;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        String linha = MyIO.readLine();
        while (!linha.equals("FIM")) {
            MyIO.println(maiorSubstring(linha));
            linha = MyIO.readLine();
        }
    }
}
