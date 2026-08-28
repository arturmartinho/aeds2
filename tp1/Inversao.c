#include <stdio.h>
#include <string.h>

void inverter(const char *s, char *resp) {
    int len = strlen(s);
    for (int i = 0; i < len; i++) {
        resp[i] = s[len - 1 - i];
    }
    resp[len] = '\0';
}

int main() {
    char linha[1000];
    char resp[1000];

    while (fgets(linha, sizeof(linha), stdin) != NULL) {
        linha[strcspn(linha, "\n")] = '\0';

        if (strcmp(linha, "FIM") == 0) {
            break;
        }

        inverter(linha, resp);
        printf("%s\n", resp);
    }

    return 0;
}
