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

bool isVogalRec(const char *s, int i, int len) {
    bool resp;
    if (i == len) {
        resp = true;
    } else {
        char c = s[i];
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
            c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
            resp = isVogalRec(s, i + 1, len);
        } else {
            resp = false;
        }
    }
    return resp;
}

bool isVogal(const char *s) {
    return isVogalRec(s, 0, strlen(s));
}

bool isConsoanteRec(const char *s, int i, int len) {
    bool resp;
    if (i == len) {
        resp = true;
    } else {
        char c = s[i];
        if ((c >= 'a' && c <= 'z' && c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u') ||
            (c >= 'A' && c <= 'Z' && c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U')) {
            resp = isConsoanteRec(s, i + 1, len);
        } else {
            resp = false;
        }
    }
    return resp;
}

bool isConsoante(const char *s) {
    return isConsoanteRec(s, 0, strlen(s));
}

bool isInteiroRec(const char *s, int i, int len) {
    bool resp;
    if (i == len) {
        resp = true;
    } else {
        char c = s[i];
        if (i == 0 && (c == '-' || c == '+')) {
            resp = isInteiroRec(s, i + 1, len);
        } else if (c >= '0' && c <= '9') {
            resp = isInteiroRec(s, i + 1, len);
        } else {
            resp = false;
        }
    }
    return resp && len > 0;
}

bool isInteiro(const char *s) {
    return isInteiroRec(s, 0, strlen(s));
}

bool isRealRec(const char *s, int i, int len, int pontos) {
    bool resp;
    if (i == len) {
        resp = (pontos == 1);
    } else {
        char c = s[i];
        if (i == 0 && (c == '-' || c == '+')) {
            resp = isRealRec(s, i + 1, len, pontos);
        } else if (c == '.') {
            resp = isRealRec(s, i + 1, len, pontos + 1);
        } else if (c >= '0' && c <= '9') {
            resp = isRealRec(s, i + 1, len, pontos);
        } else {
            resp = false;
        }
    }
    return resp && len > 0 && pontos <= 1;
}

bool isReal(const char *s) {
    return isRealRec(s, 0, strlen(s), 0);
}

int main() {
    char linha[1000];

    while (fgets(linha, sizeof(linha), stdin) != NULL) {
        limparLinha(linha);

        if (strcmp(linha, "FIM") == 0) {
            break;
        }

        const char *vogal = isVogal(linha) ? "SIM" : "NAO";
        const char *consoante = isConsoante(linha) ? "SIM" : "NAO";
        const char *inteiro = isInteiro(linha) ? "SIM" : "NAO";
        const char *real = isReal(linha) ? "SIM" : "NAO";

        printf("%s %s %s %s\n", vogal, consoante, inteiro, real);
    }

    return 0;
}
