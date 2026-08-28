#include <stdio.h>
#include <string.h>
#include <stdbool.h>

void limparLinha(char *s) {
    int len = strlen(s);
    while (len > 0 && (s[len - 1] == '\n' || s[len - 1] == '\r')) {
        s[len - 1] = '\0';
        len--;
    }
}

bool isAnagrama(const char *s1, const char *s2) {
    if (strlen(s1) != strlen(s2)) {
        return false;
    }

    int cont[256] = {0};
    int len = strlen(s1);

    for (int i = 0; i < len; i++) {
        cont[(unsigned char) s1[i]]++;
        cont[(unsigned char) s2[i]]--;
    }

    for (int i = 0; i < 256; i++) {
        if (cont[i] != 0) {
            return false;
        }
    }

    return true;
}

int main() {
    char s1[1000], s2[1000];

    while (fgets(s1, sizeof(s1), stdin) != NULL) {
        limparLinha(s1);

        if (strcmp(s1, "FIM") == 0) {
            break;
        }

        fgets(s2, sizeof(s2), stdin);
        limparLinha(s2);

        printf("%s\n", isAnagrama(s1, s2) ? "SIM" : "NAO");
    }

    return 0;
}
