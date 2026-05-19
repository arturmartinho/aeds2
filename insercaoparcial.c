#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>

#define MAX 1000
#define K 10

long comparacoes = 0;
long movimentacoes = 0;

typedef struct
{
    int ano, mes, dia;
} Data;

Data parseData(char *s)
{
    Data d;
    d.ano = d.mes = d.dia = 0;
    int i = 0;

    while (s[i] != '-')
    {
        d.ano = d.ano * 10 + (s[i] - '0');
        i++;
    }
    i++;

    while (s[i] != '-')
    {
        d.mes = d.mes * 10 + (s[i] - '0');
        i++;
    }
    i++;

    while (s[i] != '\0')
    {
        d.dia = d.dia * 10 + (s[i] - '0');
        i++;
    }

    return d;
}

void formatarData(Data d, char *out)
{
    sprintf(out, "%02d/%02d/%04d", d.dia, d.mes, d.ano);
}

typedef struct
{
    int hora, minuto;
} Hora;

Hora parseHora(char *s)
{
    Hora h;
    h.hora = h.minuto = 0;
    int i = 0;

    while (s[i] != ':')
    {
        h.hora = h.hora * 10 + (s[i] - '0');
        i++;
    }
    i++;

    while (s[i] != '\0')
    {
        h.minuto = h.minuto * 10 + (s[i] - '0');
        i++;
    }

    return h;
}

void formatarHora(Hora h, char *out)
{
    sprintf(out, "%02d:%02d", h.hora, h.minuto);
}

typedef struct
{
    int id, capacidade, faixaPreco;
    double avaliacao;
    char nome[200], cidade[200];
    char tipos[10][200];
    int qtdTipos;
    Hora abertura, fechamento;
    Data dataAbertura;
    bool aberto;
} Restaurante;

int strparaInt(char *s)
{
    int num = 0, i = 0;

    while (s[i] != '\0')
    {
        num = num * 10 + (s[i] - '0');
        i++;
    }

    return num;
}

double strparaDouble(char *s)
{
    double num = 0, dec = 0, fator = 0.1;
    int i = 0, depois = 0;

    while (s[i] != '\0')
    {
        if (s[i] == '.')
        {
            depois = 1;
        }
        else
        {
            int d = s[i] - '0';

            if (!depois)
            {
                num = num * 10 + d;
            }
            else
            {
                dec += d * fator;
                fator /= 10;
            }
        }

        i++;
    }

    return num + dec;
}

bool strparaBool(char *s)
{
    bool resp = false;

    if (s[0] == 't' && s[1] == 'r' && s[2] == 'u' && s[3] == 'e')
    {
        resp = true;
    }

    return resp;
}

int separar(char *s, char sep, char out[][200])
{
    int i = 0, j = 0, k = 0;

    while (s[i] != '\0')
    {
        if (s[i] == sep)
        {
            out[k][j] = '\0';
            k++;
            j = 0;
        }
        else
        {
            out[k][j] = s[i];
            j++;
        }

        i++;
    }

    out[k][j] = '\0';
    k++;

    return k;
}

void limparString(char *s)
{
    int i = 0;

    while (s[i] != '\0')
    {
        if (s[i] == '\n' || s[i] == '\r')
        {
            s[i] = '\0';
        }

        i++;
    }
}

int compararCidade(char *a, char *b)
{
    int i = 0;
    int resp = 0;

    while (a[i] != '\0' && b[i] != '\0' && resp == 0)
    {
        comparacoes++;

        if (a[i] < b[i])
        {
            resp = -1;
        }
        else if (a[i] > b[i])
        {
            resp = 1;
        }

        i++;
    }

    if (resp == 0)
    {
        if (a[i] == '\0' && b[i] != '\0')
        {
            resp = -1;
        }
        else if (a[i] != '\0' && b[i] == '\0')
        {
            resp = 1;
        }
    }

    return resp;
}

void insercaoParcial(Restaurante lista[], int n)
{
    int i, j;
    Restaurante tmp;

    for (i = 1; i < n; i++)
    {
        tmp = lista[i];
        j = (i < K) ? i - 1 : K - 1;

        while (j >= 0 && compararCidade(lista[j].cidade, tmp.cidade) > 0)
        {
            if (j + 1 < K)
            {
                lista[j + 1] = lista[j];
                movimentacoes++;
            }

            j--;
        }

        if (j + 1 < K)
        {
            lista[j + 1] = tmp;
            movimentacoes++;
        }
    }
}

Restaurante parseRestaurante(char *linha)
{
    Restaurante r;

    char partes[10][200];
    separar(linha, ',', partes);

    int i = 0;

    while (i < 10)
    {
        limparString(partes[i]);
        i++;
    }

    r.id = strparaInt(partes[0]);

    sprintf(r.nome, "%s", partes[1]);
    sprintf(r.cidade, "%s", partes[2]);

    r.capacidade = strparaInt(partes[3]);
    r.avaliacao = strparaDouble(partes[4]);

    r.qtdTipos = separar(partes[5], ';', r.tipos);

    r.faixaPreco = 0;

    while (partes[6][r.faixaPreco] != '\0')
    {
        r.faixaPreco++;
    }

    char horas[2][200];
    separar(partes[7], '-', horas);

    r.abertura = parseHora(horas[0]);
    r.fechamento = parseHora(horas[1]);

    r.dataAbertura = parseData(partes[8]);

    r.aberto = strparaBool(partes[9]);

    return r;
}

void formatarRestaurante(Restaurante r)
{
    char data[20], h1[10], h2[10];

    formatarData(r.dataAbertura, data);
    formatarHora(r.abertura, h1);
    formatarHora(r.fechamento, h2);

    printf("[%d ## %s ## %s ## %d ## %.1lf ## [", r.id, r.nome, r.cidade, r.capacidade, r.avaliacao);

    int i = 0;

    while (i < r.qtdTipos)
    {
        printf("%s", r.tipos[i]);

        if (i < r.qtdTipos - 1)
        {
            printf(",");
        }

        i++;
    }

    printf("] ## ");

    i = 0;

    while (i < r.faixaPreco)
    {
        printf("$");
        i++;
    }

    printf(" ## %s-%s ## %s ## %s]\n", h1, h2, data, r.aberto ? "true" : "false");
}

int main()
{
    Restaurante lista[MAX];
    int tamanho = 0;

    FILE *f = fopen("/tmp/restaurantes.csv", "r");

    char linha[500];

    fscanf(f, " %[^\n]", linha);

    while (fscanf(f, " %[^\n]", linha) == 1)
    {
        lista[tamanho++] = parseRestaurante(linha);
    }

    fclose(f);

    Restaurante selecionados[MAX];
    int qtd = 0;

    int id;

    scanf("%d", &id);

    while (id != -1)
    {
        int i = 0;

        while (i < tamanho)
        {
            if (lista[i].id == id)
            {
                selecionados[qtd++] = lista[i];
                i = tamanho;
            }

            i++;
        }

        scanf("%d", &id);
    }

    Restaurante copia[MAX];

    int i = 0;

    while (i < qtd)
    {
        copia[i] = selecionados[i];
        i++;
    }

    clock_t inicio = clock();

    insercaoParcial(copia, qtd);

    clock_t fim = clock();

    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC;

    int limite = (qtd < K) ? qtd : K;

    i = 0;

    while (i < limite)
    {
        formatarRestaurante(copia[i]);
        i++;
    }

    i = K;

    while (i < qtd)
    {
        formatarRestaurante(selecionados[i]);
        i++;
    }

    FILE *log = fopen("1593221_insercao_parcial.txt", "w");

    fprintf(log,"1593221\t%ld\t%ld\t%lf",comparacoes,movimentacoes,tempo);

    fclose(log);

    return 0;
}
