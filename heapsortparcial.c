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

typedef struct
{
    int hora, minuto;
} Hora;

typedef struct
{
    int id, capacidade, faixaPreco;
    double avaliacao;

    char nome[200];
    char cidade[200];

    char tipos[10][200];
    int qtdTipos;

    Hora abertura, fechamento;
    Data dataAbertura;

    bool aberto;

} Restaurante;

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

int strparaInt(char *s)
{
    int num = 0;
    int i = 0;

    while (s[i] != '\0')
    {
        num = num * 10 + (s[i] - '0');
        i++;
    }

    return num;
}

double strparaDouble(char *s)
{
    double num = 0;
    double dec = 0;
    double fator = 0.1;

    int i = 0;
    int depois = 0;

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

    if (s[0] == 't' &&
        s[1] == 'r' &&
        s[2] == 'u' &&
        s[3] == 'e')
    {
        resp = true;
    }

    return resp;
}

int separar(char *s, char sep, char out[][200])
{
    int i = 0;
    int j = 0;
    int k = 0;

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

int compararString(char *a, char *b)
{
    int i = 0;
    int resp = 0;

    while (a[i] != '\0' &&
           b[i] != '\0' &&
           resp == 0)
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

int compararData(Data d1, Data d2)
{
    int resp = 0;

    if (d1.ano < d2.ano)
    {
        resp = -1;
    }
    else if (d1.ano > d2.ano)
    {
        resp = 1;
    }
    else if (d1.mes < d2.mes)
    {
        resp = -1;
    }
    else if (d1.mes > d2.mes)
    {
        resp = 1;
    }
    else if (d1.dia < d2.dia)
    {
        resp = -1;
    }
    else if (d1.dia > d2.dia)
    {
        resp = 1;
    }

    return resp;
}

bool comparar(Restaurante a, Restaurante b)
{
    bool resp = false;

    int cmpData = compararData(a.dataAbertura, b.dataAbertura);

    if (cmpData > 0)
    {
        resp = true;
    }
    else if (cmpData == 0)
    {
        if (compararString(a.nome, b.nome) > 0)
        {
            resp = true;
        }
    }

    return resp;
}

void heapsortParcial(Restaurante lista[], int n)
{
    Restaurante top[K];

    int tamTop = 0;

    int i = 0;

    while (i < n)
    {
        if (tamTop < K)
        {
            top[tamTop] = lista[i];
            tamTop++;

            int j = tamTop - 1;

            while (j > 0 && comparar(top[j - 1], top[j]))
            {
                Restaurante tmp = top[j];
                top[j] = top[j - 1];
                top[j - 1] = tmp;

                movimentacoes += 3;

                j--;
            }
        }
        else
        {
            if (comparar(top[K - 1], lista[i]))
            {
                top[K - 1] = lista[i];

                movimentacoes++;

                int j = K - 1;

                while (j > 0 && comparar(top[j - 1], top[j]))
                {
                    Restaurante tmp = top[j];
                    top[j] = top[j - 1];
                    top[j - 1] = tmp;

                    movimentacoes += 3;

                    j--;
                }
            }
        }

        i++;
    }

    i = 0;

    while (i < K && i < n)
    {
        lista[i] = top[i];
        i++;
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
    char data[20];
    char h1[10];
    char h2[10];

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

bool existe(Restaurante r, Restaurante top[], int tam)
{
    bool resp = false;
    int i = 0;

    while (i < tam)
    {
        if (r.id == top[i].id)
        {
            resp = true;
        }

        i++;
    }

    return resp;
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
    Restaurante top10[K];

    int i = 0;

    while (i < qtd)
    {
        copia[i] = selecionados[i];
        i++;
    }

    clock_t inicio = clock();

    heapsortParcial(copia, qtd);

    clock_t fim = clock();

    double tempo = (double)(fim - inicio) / CLOCKS_PER_SEC;

    int limite = (qtd < K) ? qtd : K;

    for (i = 0; i < limite; i++)
    {
        selecionados[i] = copia[i];
    }

    for (i = 0; i < qtd; i++)
    {
        formatarRestaurante(selecionados[i]);
    }

    FILE *log = fopen("1593221_heapsort_parcial.txt", "w");

    fprintf(log, "1593221\t%ld\t%ld\t%lf", comparacoes, movimentacoes, tempo);

    fclose(log);

    return 0;
}
