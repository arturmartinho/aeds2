#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>

#define MAX_RESTAURANTES 1000

long comparacoes = 0;
long movimentacoes = 0;


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
    struct Celula *prox;
} Celula;

typedef struct {
    Celula *primeiro, *ultimo;
    int tamanho;
} Lista;


Data parseData(char *s) {
    Data d = {0, 0, 0};
    int i = 0;
    while (s[i] != '-') { d.ano = d.ano * 10 + (s[i] - '0'); i++; }
    i++;
    while (s[i] != '-') { d.mes = d.mes * 10 + (s[i] - '0'); i++; }
    i++;
    while (s[i] != '\0') { d.dia = d.dia * 10 + (s[i] - '0'); i++; }
    return d;
}

Hora parseHora(char *s) {
    Hora h = {0, 0};
    int i = 0;
    while (s[i] != ':') { h.hora = h.hora * 10 + (s[i] - '0'); i++; }
    i++;
    while (s[i] != '\0') { h.minuto = h.minuto * 10 + (s[i] - '0'); i++; }
    return h;
}

int strparaInt(char *s) {
    int num = 0, i = 0;
    while (s[i] != '\0') { num = num * 10 + (s[i] - '0'); i++; }
    return num;
}

double strparaDouble(char *s) {
    double num = 0, dec = 0, fator = 0.1;
    int i = 0, depois = 0;
    while (s[i] != '\0') {
        if (s[i] == '.') depois = 1;
        else {
            int d = s[i] - '0';
            if (depois == 0) num = num * 10 + d;
            else { dec += d * fator; fator /= 10; }
        }
        i++;
    }
    return num + dec;
}

int separar(char *s, char sep, char out[][200]) {
    int i = 0, j = 0, k = 0;
    while (s[i] != '\0') {
        if (s[i] == sep) { out[k][j] = '\0'; k++; j = 0; }
        else { out[k][j] = s[i]; j++; }
        i++;
    }
    out[k][j] = '\0';
    return k + 1;
}

void limparString(char *s) {
    int i = 0;
    while (s[i] != '\0') {
        if (s[i] == '\n' || s[i] == '\r') s[i] = '\0';
        i++;
    }
}

int compararString(char *a, char *b) {
    int i = 0, resp = 0;
    while (a[i] != '\0' && b[i] != '\0' && resp == 0) {
        comparacoes++;
        if (a[i] < b[i]) resp = -1;
        else if (a[i] > b[i]) resp = 1;
        i++;
    }
    if (resp == 0) {
        if (a[i] == '\0' && b[i] != '\0') resp = -1;
        else if (a[i] != '\0' && b[i] == '\0') resp = 1;
    }
    return resp;
}

Celula* novaCelula(Restaurante x) {
    Celula* nova = (Celula*)malloc(sizeof(Celula));
    nova->elemento = x;
    nova->prox = NULL;
    return nova;
}

void iniciarLista(Lista* l) {
    l->primeiro = novaCelula((Restaurante){0});
    l->ultimo = l->primeiro;
    l->tamanho = 0;
}

void inserirFim(Lista* l, Restaurante x) {
    l->ultimo->prox = novaCelula(x);
    l->ultimo = l->ultimo->prox;
    l->tamanho++;
}

void ordenarSelecao(Lista* l) {
    for (Celula* i = l->primeiro->prox; i != NULL && i->prox != NULL; i = i->prox) {
        Celula* menor = i;
        for (Celula* j = i->prox; j != NULL; j = j->prox) {
            if (compararString(j->elemento.nome, menor->elemento.nome) < 0) {
                menor = j;
            }
        }
        if (menor != i) {
            Restaurante temp = i->elemento;
            i->elemento = menor->elemento;
            menor->elemento = temp;
            movimentacoes += 3;
        }
    }
}


Restaurante parseRestaurante(char *linha) {
    
    Restaurante r;
    char partes[10][200];
    separar(linha, ',', partes);

    for (int i = 0; i < 10; i++) limparString(partes[i]);
    r.id = strparaInt(partes[0]);

    sprintf(r.nome, "%s", partes[1]);

    sprintf(r.cidade, "%s", partes[2]);

    r.capacidade = strparaInt(partes[3]);

    r.avaliacao = strparaDouble(partes[4]);

    r.qtdTipos = separar(partes[5], ';', r.tipos);

    r.faixaPreco = 0;

    while (partes[6][r.faixaPreco] != '\0') r.faixaPreco++;
    char horas[2][200];

    separar(partes[7], '-', horas);

    r.abertura = parseHora(horas[0]);

    r.fechamento = parseHora(horas[1]);

    r.dataAbertura = parseData(partes[8]);
    
    r.aberto = (partes[9][0] == 't');

    return r;
}

void formatarRestaurante(Restaurante r) {

    printf("[%d ## %s ## %s ## %d ## %.1lf ## [", r.id, r.nome, r.cidade, r.capacidade, r.avaliacao);

    for (int i = 0; i < r.qtdTipos; i++) {
        printf("%s%s", r.tipos[i], (i < r.qtdTipos - 1 ? "," : ""));
    }

    printf("] ## ");

    for (int i = 0; i < r.faixaPreco; i++) printf("$");
    printf(" ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]\n", 
           r.abertura.hora, r.abertura.minuto, r.fechamento.hora, r.fechamento.minuto,
           r.dataAbertura.dia, r.dataAbertura.mes, r.dataAbertura.ano, r.aberto ? "true" : "false");
}

int main() {
    Restaurante base[MAX_RESTAURANTES];
    int qtdBase = 0;
    char linha[500];

    FILE *f = fopen("/tmp/restaurantes.csv", "r");
    if (f) {
        fgets(linha, sizeof(linha), f);
        while (fgets(linha, sizeof(linha), f)) {
            base[qtdBase++] = parseRestaurante(linha);
        }
        fclose(f);
    }

    Lista selecionados;
    iniciarLista(&selecionados);

    int id;
    while (scanf("%d", &id) == 1 && id != -1) {
        bool achou = false;
        for (int i = 0; i < qtdBase && !achou; i++) {
            if (base[i].id == id) {
                inserirFim(&selecionados, base[i]);
                achou = true;
            }
        }
    }

    clock_t inicio = clock();
    ordenarSelecao(&selecionados);
    clock_t fim = clock();
    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC;

    for (Celula* i = selecionados.primeiro->prox; i != NULL; i = i->prox) {
        formatarRestaurante(i->elemento);
    }

    FILE *log = fopen("1593221_selecao_flexivel.txt", "w");
    fprintf(log, "1593221\t%ld\t%ld\t%lf", comparacoes, movimentacoes, tempo);
    fclose(log);

    return 0;
}
