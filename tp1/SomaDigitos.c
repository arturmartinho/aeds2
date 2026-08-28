#include <stdio.h>

int somar(int n) {
    int soma = 0;
    while (n > 0) {
        soma += n % 10;
        n /= 10;
    }
    return soma;
}

int main() {
    int n;

    while (scanf("%d", &n) == 1 && n != -1) {
        printf("%d\n", somar(n));
    }

    return 0;
}
