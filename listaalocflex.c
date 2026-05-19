#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#define MAX_RESTAURANTES 1000

typedef struct {
    int ano, mes, dia;
} Data;

typedef struct {
    int hora, minuto;
} Hora;

typedef struct {
    int id, capacidade, faixaPreco;
    double avaliacao;
    char nome[200], cidade[200];
    char tipos[10][200];
    int qtdTipos;
    Hora abertura, fechamento;
    Data dataAbertura;
    bool aberto;
} Restaurante;

typedef struct Celula {
    Restaurante elemento;
    struct Celula* prox;
} Celula;

typedef struct {
    Celula *primeiro, *ultimo;
    int tamanho;
} Lista;

bool stringsIguais(char *s1, char *s2) {
    
    int i = 0;
    bool resp = true;

    while (s1[i] != '\0' && s2[i] != '\0' && resp == true) {
        if (s1[i] != s2[i]) {
            resp = false;
        } else {
            i++;
        }
    }
    if (resp == true) {
        resp = (s1[i] == s2[i]);
    }

    return resp;
}

void copiarString(char *destino, char *origem) {
    int i = 0;
    while (origem[i] != '\0') {
        destino[i] = origem[i];
        i++;
    }
    destino[i] = '\0';
}

void limparString(char *s) {
    int i = 0;
    while (s[i] != '\0') {
        if (s[i] == '\n' || s[i] == '\r') {
            s[i] = '\0';
        }
        i++;
    }
}

Data parseData(char *s) {
    Data d = {0, 0, 0};
    int i = 0;
    while (s[i] != '-') {
        d.ano = d.ano * 10 + (s[i++] - '0');
    }
    i++;
    while (s[i] != '-') {
        d.mes = d.mes * 10 + (s[i++] - '0');
    }
    i++;
    while (s[i] != '\0') {
        d.dia = d.dia * 10 + (s[i++] - '0');
    }
    return d;
}

Hora parseHora(char *s) {
    Hora h = {0, 0};
    int i = 0;
    while (s[i] != ':') {
        h.hora = h.hora * 10 + (s[i++] - '0');
    }
    i++;
    while (s[i] != '\0') {
        h.minuto = h.minuto * 10 + (s[i++] - '0');
    }
    return h;
}

int strparaInt(char *s) {
    int num = 0, i = 0;
    while (s[i] >= '0' && s[i] <= '9') {
        num = num * 10 + (s[i] - '0');
        i++;
    }
    return num;
}

double strparaDouble(char *s) {
    double num = 0, dec = 0, fator = 0.1;
    int i = 0, depois = 0;
    while (s[i] != '\0') {
        if (s[i] == '.') {
            depois = 1;
        } else {
            int d = s[i] - '0';
            if (depois == 0) {
                num = num * 10 + d;
            } else {
                dec += d * fator;
                fator /= 10;
            }
        }
        i++;
    }
    return num + dec;
}

int separar(char *s, char sep, char out[][200]) {
    int i = 0, j = 0, k = 0;
    while (s[i] != '\0') {
        if (s[i] == sep) {
            out[k][j] = '\0';
            k++;
            j = 0;
        } else {
            out[k][j] = s[i];
            j++;
        }
        i++;
    }
    out[k][j] = '\0';
    return k + 1;
}

Restaurante parseRestaurante(char *linha) {
    Restaurante r;
    char partes[10][200];
    separar(linha, ',', partes);
    
    for (int i = 0; i < 10; i++) {
        limparString(partes[i]);
    }

    r.id = strparaInt(partes[0]);
    copiarString(r.nome, partes[1]);
    copiarString(r.cidade, partes[2]);
    r.capacidade = strparaInt(partes[3]);
    r.avaliacao = strparaDouble(partes[4]);
    r.qtdTipos = separar(partes[5], ';', r.tipos);
    
    r.faixaPreco = 0;
    while (partes[6][r.faixaPreco] != '\0') {
        r.faixaPreco++;
    }

    char horas[2][200];
    separar(partes[7], '-', horas);
    r.abertura = parseHora(horas[0]);
    r.fechamento = parseHora(horas[1]);
    r.dataAbertura = parseData(partes[8]);
    r.aberto = (partes[9][0] == 't');
    
    return r;
}

Celula* novaCelula(Restaurante x) {
    Celula* nova = (Celula*) malloc(sizeof(Celula));
    nova->elemento = x;
    nova->prox = NULL;
    return nova;
}

void iniciarLista(Lista* l) {
    l->primeiro = novaCelula((Restaurante){0}); 
    l->ultimo = l->primeiro;
    l->tamanho = 0;
}

void inserirInicio(Lista* l, Restaurante x) {
    Celula* tmp = novaCelula(x);
    tmp->prox = l->primeiro->prox;
    l->primeiro->prox = tmp;
    if (l->primeiro == l->ultimo) {
        l->ultimo = tmp;
    }
    l->tamanho++;
}

void inserirFim(Lista* l, Restaurante x) {
    l->ultimo->prox = novaCelula(x);
    l->ultimo = l->ultimo->prox;
    l->tamanho++;
}

void inserir(Lista* l, Restaurante x, int pos) {
    if (pos == 0) {
        inserirInicio(l, x);
    } else if (pos == l->tamanho) {
        inserirFim(l, x);
    } else {
        Celula* i = l->primeiro;
        for (int j = 0; j < pos; j++, i = i->prox) {}
        Celula* tmp = novaCelula(x);
        tmp->prox = i->prox;
        i->prox = tmp;
        l->tamanho++;
    }
}

Restaurante removerInicio(Lista* l) {
    Celula* tmp = l->primeiro->prox;
    Restaurante resp = tmp->elemento;
    l->primeiro->prox = tmp->prox;
    if (tmp == l->ultimo) {
        l->ultimo = l->primeiro;
    }

    l->tamanho--;
    return resp;
}


Restaurante removerFim(Lista* l) {

    Celula* i;
    for (i = l->primeiro; i->prox != l->ultimo; i = i->prox) {}

    Restaurante resp = l->ultimo->elemento;

    l->ultimo = i;
    l->ultimo->prox = NULL;
    l->tamanho--;

    return resp;
}

Restaurante remover(Lista* l, int pos) {
    Restaurante resp;

    if (pos == 0) {
        resp = removerInicio(l);
    } else if (pos == l->tamanho - 1) {
        resp = removerFim(l);
    } else {
        Celula* i = l->primeiro;

        for (int j = 0; j < pos; j++, i = i->prox) {}
        
        Celula* tmp = i->prox;
        resp = tmp->elemento;
        i->prox = tmp->prox;
       
        l->tamanho--;
    }

    return resp;
}

void mostrarLista(Lista* l) {
    for (Celula* i = l->primeiro->prox; i != NULL; i = i->prox) {
        Restaurante r = i->elemento;
        printf("[%d ## %s ## %s ## %d ## %.1lf ## [", r.id, r.nome, r.cidade, r.capacidade, r.avaliacao);
        for (int j = 0; j < r.qtdTipos; j++) {
            printf("%s%s", r.tipos[j], (j < r.qtdTipos - 1 ? "," : ""));
        }
        printf("] ## ");
        for (int j = 0; j < r.faixaPreco; j++) {
            printf("$");
        }
        printf(" ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]\n", r.abertura.hora, r.abertura.minuto, r.fechamento.hora, 
            r.fechamento.minuto,r.dataAbertura.dia, r.dataAbertura.mes, r.dataAbertura.ano, r.aberto ? "true" : "false");
    }
}

Restaurante buscarNaBase(Restaurante base[], int n, int id) {
    Restaurante resp = {0};
    int i = 0;
    bool achou = false;

    while (i < n && !achou) {
        if (base[i].id == id) {
            resp = base[i];
            achou = true;
        }
        i++;
    }

    return resp;
}

int main() {
    Restaurante base[MAX_RESTAURANTES];
    int qtdBase = 0;
    char linha[1000];

    FILE *f = fopen("/tmp/restaurantes.csv", "r");
    if (f) {
        fgets(linha, sizeof(linha), f); 
        while (fgets(linha, sizeof(linha), f)) {
            base[qtdBase++] = parseRestaurante(linha);
        }
        fclose(f);
    }

    Lista lista;
    iniciarLista(&lista);

    int id;
    while (scanf("%d", &id) && id != -1) {
        inserirFim(&lista, buscarNaBase(base, qtdBase, id));
    }

    int num;
    scanf("%d", &num);
    for (int i = 0; i < num; i++) {
        char cmd[5];
        scanf("%s", cmd);
        
        if (stringsIguais(cmd, "II")) {
            scanf("%d", &id);
            inserirInicio(&lista, buscarNaBase(base, qtdBase, id));
        } else if (stringsIguais(cmd, "IF")) {
            scanf("%d", &id);
            inserirFim(&lista, buscarNaBase(base, qtdBase, id));
        } else if (stringsIguais(cmd, "I*")) {
            int pos;
            scanf("%d %d", &pos, &id);
            inserir(&lista, buscarNaBase(base, qtdBase, id), pos);
        } else if (stringsIguais(cmd, "RI")) {
            Restaurante r = removerInicio(&lista);
            printf("(R)%s\n", r.nome);
        } else if (stringsIguais(cmd, "RF")) {
            Restaurante r = removerFim(&lista);
            printf("(R)%s\n", r.nome);
        } else if (stringsIguais(cmd, "R*")) {
            int pos;
            scanf("%d", &pos);
            Restaurante r = remover(&lista, pos);
            printf("(R)%s\n", r.nome);
        }
    }

    mostrarLista(&lista);
    return 0;
}
//Felipe de Faria Rios Coelho 
