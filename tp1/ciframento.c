#include <stdio.h>
#include <string.h>

void limparLinha(char *s) {
    int len = strlen(s);
    while (len > 0 && (s[len - 1] == '\n' || s[len - 1] == '\r')) {
        s[len - 1] = '\0';
        len--;
    }
}

void cifrarRec(const char *s, int i, char *resp) {
    if (s[i] == '\0') {
        resp[i] = '\0';
        return;
    }
    resp[i] = (char) (s[i] + 3);
    cifrarRec(s, i + 1, resp);
}

void cifrar(const char *s, char *resp) {
    cifrarRec(s, 0, resp);
}

int main() {
    char linha[1000];
    char resp[1000];

    while (fgets(linha, sizeof(linha), stdin) != NULL) {
        limparLinha(linha);

        if (strcmp(linha, "FIM") == 0) {
            break;
        }

        cifrar(linha, resp);
        printf("%s\n", resp);
    }

    return 0;
}
