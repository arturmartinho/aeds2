public class SomaDigitosRecursiva {
    public static int somar(int n) {
        if (n == 0) {
            return 0;
        }
        return n % 10 + somar(n / 10);
    }

    public static void main(String[] args) {
        MyIO.setCharset("UTF-8");
        int n = MyIO.readInt();
        while (n != -1) {
            MyIO.println(somar(n));
            n = MyIO.readInt();
        }
    }
}
