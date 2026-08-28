#include <stdio.h>
#include <string.h>

void limparLinha(char *s) {
    int len = strlen(s);
    while (len > 0 && (s[len - 1] == '\n' || s[len - 1] == '\r')) {
        s[len - 1] = '\0';
        len--;
    }
}

int maiorSubstring(const char *s) {
    int max = 0;
    int len = strlen(s);

    for (int i = 0; i < len; i++) {
        int cont[256] = {0};
        int j = i;
        while (j < len && cont[(unsigned char) s[j]] == 0) {
            cont[(unsigned char) s[j]] = 1;
            j++;
        }
        int tamanho = j - i;
        if (tamanho > max) {
            max = tamanho;
        }
    }

    return max;
}

int main() {
    char linha[1000];

    while (fgets(linha, sizeof(linha), stdin) != NULL) {
        limparLinha(linha);

        if (strcmp(linha, "FIM") == 0) {
            break;
        }

        printf("%d\n", maiorSubstring(linha));
    }

    return 0;
}
