#include <stdio.h>
#include <string.h>
#include <stdbool.h>

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
        s1[strcspn(s1, "\n")] = '\0';

        if (strcmp(s1, "FIM") == 0) {
            break;
        }

        fgets(s2, sizeof(s2), stdin);
        s2[strcspn(s2, "\n")] = '\0';

        printf("%s\n", isAnagrama(s1, s2) ? "SIM" : "NAO");
    }

    return 0;
}
