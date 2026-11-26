import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.swing.JOptionPane;

import com.sun.jna.Library;
import com.sun.jna.Native;

// =====================================================================
// BIBLIOTECA DO TCA
//   1. TELA      -> limpar a tela, escrever numa posição, cores, cursor
//   2. TEMPO     -> esperar, cronômetro
//   3. TECLADO   -> ler teclas sem precisar apertar ENTER
//   4. MENU      -> menu navegável com W/S e ENTER
//   5. SORTEIO   -> sortear com probabilidades diferentes
//   6. SOM       -> tocar arquivos .wav
//   7. ARQUIVOS  -> salvar e carregar texto (ex.: recorde, save do jogo)
//   8. JANELAS   -> caixas de mensagem e de pergunta
//
// COMO USAR NO SEU JOGO:
//   1. Copie este arquivo (BibliotecaTCA.java) para a pasta do seu jogo.
//   2. Copie também a pasta "lib" com o arquivo jna-5.17.0.jar
//      (é usado pela parte do TECLADO).
//   3. No seu código, chame as funções com "BibliotecaTCA." na frente:
//        BibliotecaTCA.limparTela();
//        BibliotecaTCA.escreverEm(5, 10, "@");
//        BibliotecaTCA.mudarCor(BibliotecaTCA.VERDE);
//        int tecla = BibliotecaTCA.obtemTeclaPressionada();
//      Você NÃO precisa copiar os imports deste arquivo para o seu.
//
// COMO COMPILAR E RODAR PELO TERMINAL (dentro da pasta do projeto):
//   Mac e Linux:
//     javac -cp "lib/*:." MeuJogo.java
//     java -cp "lib/*:." MeuJogo
//   Windows:
//     javac -cp "lib/*;." MeuJogo.java
//     java -cp "lib/*;." MeuJogo
//
//   Compilando o MeuJogo.java, o javac compila a BibliotecaTCA.java
//   junto, sozinho. Atenção ao separador: ":" no Mac/Linux e ";" no Windows.
//
// ATENÇÃO:
//   - Rode no TERMINAL (fica melhor que o do VS Code). 
//   - O terminal começa a contar linhas e colunas em 1, e NÃO em 0
//     como os arrays do Java. O canto de cima à esquerda é (1, 1).
//
// Para ver uma demonstração das funções, rode a própria biblioteca:
//     java -cp "lib/*:." BibliotecaTCA.java
// =====================================================================
public class BibliotecaTCA {

    // =================================================================
    // 1. TELA
    // =================================================================

    // Apaga tudo o que está escrito no terminal e volta o cursor
    // para o canto de cima à esquerda, a posição irPara(1, 1).
    public static void limparTela() {
        System.out.print("\033[2J"); // apaga a tela
        System.out.print("\033[H");  // volta o cursor para o canto (1, 1)
        System.out.flush();          // garante que os comandos acima sejam enviados agora
    }

    // Move o cursor para uma posição do terminal. O próximo
    // System.out.print vai escrever a partir dessa posição.
    //
    // Exemplo:
    //   irPara(5, 10);             // linha 5, coluna 10
    //   System.out.print("Oi!");   // escreve "Oi!" nessa posição
    //
    // ATENÇÃO:
    //   - Primeiro vem a LINHA (para baixo), depois a COLUNA (para o lado).
    //   - O canto de cima à esquerda é irPara(1, 1).
    //   - No fim do programa, mande o cursor para baixo do seu desenho
    //     (ex.: irPara(25, 1)). Senão o terminal escreve por cima dele.
    public static void irPara(int linha, int coluna) {
        char escCode = 0x1B;
        System.out.print(String.format("%c[%d;%df", escCode, linha, coluna));
    }

    // Escreve um texto numa posição do terminal. É o mesmo que chamar
    // irPara e depois System.out.print, só que numa linha só.
    //
    // Exemplo:
    //   escreverEm(1, 1, "Pontos: 10");    // placar no canto de cima
    //   escreverEm(5, 20, "@");            // personagem na linha 5, coluna 20
    public static void escreverEm(int linha, int coluna, String texto) {
        irPara(linha, coluna);
        System.out.print(texto);
    }

    // Constantes das cores básicas, para usar com mudarCor e mudarCorFundo.
    // Assim dá para escrever mudarCor(VERDE) em vez de mudarCor(2).
    // "final" quer dizer que o valor nunca muda.
    public static final int PRETO      = 0;
    public static final int VERMELHO   = 1;
    public static final int VERDE      = 2;
    public static final int AMARELO    = 3;
    public static final int AZUL       = 4;
    public static final int MAGENTA    = 5;
    public static final int CIANO      = 6;
    public static final int CINZA      = 7;
    public static final int BRANCO     = 15;
    public static final int COR_PADRAO = -1; // a cor original do terminal

    // Muda a cor do TEXTO. Tudo o que for escrito depois sai nessa cor.
    //
    // A cor é um número de 0 a 255 (são 256 cores!). Para as cores
    // básicas, use as constantes acima. Para ver todas as 256 cores e
    // escolher o número, chame a função mostrarCores().
    // Use COR_PADRAO (-1) para voltar à cor original do terminal.
    //
    // Exemplo:
    //   mudarCor(VERDE);                  // texto verde
    //   System.out.println("Você venceu!");
    //   mudarCor(208);                    // texto laranja (uma das 256 cores)
    //   System.out.println("Cuidado!");
    //   mudarCor(COR_PADRAO);             // volta ao normal
    //
    // ATENÇÃO:
    //   - Use mudarCor(COR_PADRAO) no fim do programa para o terminal não ficar colorido.
    //   - Cuidado com texto preto em terminal escuro (ou branco em terminal claro):
    //     ele fica invisível!
    public static void mudarCor(int cor) {
        if (cor >= 0 && cor <= 255) {
            System.out.print("\033[38;5;" + cor + "m");
        } else {
            System.out.print("\033[39m"); // cor padrão do terminal
        }
    }

    // Muda a cor do FUNDO (atrás das letras). Usa as mesmas cores
    // do mudarCor: constantes ou números de 0 a 255.
    //
    // Exemplo:
    //   mudarCorFundo(AZUL);              // fundo azul
    //   mudarCor(AMARELO);                // texto amarelo
    //   System.out.print(" PLACAR: 10 ");
    //   mudarCorFundo(COR_PADRAO);        // volta o fundo ao normal
    //   mudarCor(COR_PADRAO);             // volta o texto ao normal
    //
    // Dica: escreva espaços " " com a cor de fundo para desenhar blocos coloridos.
    public static void mudarCorFundo(int cor) {
        if (cor >= 0 && cor <= 255) {
            System.out.print("\033[48;5;" + cor + "m");
        } else {
            System.out.print("\033[49m"); // cor padrão do terminal
        }
    }

    // Mostra na tela as 256 cores, cada número escrito na sua cor.
    // Use para escolher o número da cor que você quer no seu jogo.
    //   0 a 15    : cores básicas
    //   16 a 231  : cores variadas
    //   232 a 255 : tons de cinza, do mais escuro ao mais claro
    public static void mostrarCores() {
        System.out.println("Cores de 0 a 255:");
        for (int i = 0; i < 256; i++) {
            mudarCor(i);
            System.out.printf("Cor %-3d ", i);
            if (i % 8 == 7) {
                System.out.println(); // pula a linha a cada 8 cores
            }
        }
        mudarCor(COR_PADRAO);
    }

    static boolean restauracaoRegistrada = false;

    // Esconde o cursor piscando. Bom para jogos que redesenham a tela o
    // tempo todo: sem isso, o cursor fica pulando de um lado para o outro.
    //
    // Exemplo:
    //   esconderCursor();   // no começo do jogo
    //
    // Você NÃO precisa mostrar o cursor de novo no fim: quando o programa
    // termina (até com Ctrl + C), a biblioteca mostra o cursor e volta as
    // cores ao normal sozinha. Sem isso, o terminal ficaria sem cursor.
    public static void esconderCursor() {
        System.out.print("\033[?25l");
        System.out.flush();

        // Registra só uma vez o código que roda quando o programa termina
        // (um "shutdown hook"), mesmo que esconderCursor seja chamada várias vezes.
        if (!restauracaoRegistrada) {
            restauracaoRegistrada = true;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                mudarCor(COR_PADRAO);
                mudarCorFundo(COR_PADRAO);
                mostrarCursor();
            }));
        }
    }

    // Mostra o cursor de novo (desfaz o esconderCursor).
    // Útil se o jogo pedir para o jogador digitar algo no meio do caminho.
    public static void mostrarCursor() {
        System.out.print("\033[?25h");
        System.out.flush();
    }

    // =================================================================
    // 2. TEMPO
    // =================================================================

    // Pausa o programa por um tempo.
    // O tempo é em milissegundos: 1000 milissegundos = 1 segundo.
    //
    // Exemplos:
    //   esperar(1000); // espera 1 segundo
    //   esperar(100);  // espera 0,1 segundo (bom para a velocidade do jogo)
    public static void esperar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            // não faz nada: se a espera for interrompida, o programa só continua
        }
    }

    // Guarda o momento em que o cronômetro começou (em milissegundos)
    static long inicioCronometro = 0;

    // Começa (ou recomeça) a contar o tempo do zero.
    //
    // Exemplo: jogo com tempo limite de 30 segundos
    //   iniciarCronometro();
    //   while (segundosPassados() < 30) {
    //       // ... jogo rodando ...
    //   }
    //   System.out.println("Acabou o tempo!");
    public static void iniciarCronometro() {
        inicioCronometro = System.currentTimeMillis();
    }

    // Retorna quantos segundos passaram desde o iniciarCronometro().
    // O valor tem casas decimais: 1.5 quer dizer um segundo e meio.
    public static double segundosPassados() {
        long agora = System.currentTimeMillis();
        return (agora - inicioCronometro) / 1000.0;
    }

    // =================================================================
    // 3. TECLADO
    //
    // Funções para usar no jogo:
    //   pressionouTecla()       -> true se alguma tecla foi apertada
    //   obtemTeclaPressionada() -> qual tecla foi apertada (código ASCII)
    //   esperarTecla()          -> pausa até o jogador apertar uma tecla
    //
    // Códigos úteis: 'w', 'a', 's', 'd', ' ' (espaço), 13 = ENTER, 27 = ESC.
    //
    // Exemplo: personagem andando sem o jogo parar para esperar tecla
    //   while (true) {
    //       if (pressionouTecla()) {
    //           int tecla = obtemTeclaPressionada();
    //           if (tecla == 'd') coluna++;
    //           if (tecla == 27) break;   // ESC sai do jogo
    //       }
    //       // ... desenha a tela ...
    //       esperar(100);
    //   }
    //
    // Por que tem duas formas dentro das funções?
    //   - No Windows, usamos a biblioteca "msvcrt" (do Windows) pelo JNA.
    //     Por isso precisa da pasta lib com o jna-5.17.0.jar.
    //   - No Mac e no Linux essa biblioteca não existe. Lá usamos o comando
    //     "stty" para o terminal entregar as teclas sem esperar o ENTER.
    //
    // ATENÇÃO:
    //   - As setas do teclado não funcionam: elas mandam vários códigos de
    //     uma vez, começando com 27 (o mesmo do ESC). Use W, A, S, D.
    //   - Depois de usar estas funções, o Scanner deixa de funcionar
    //     direito no Mac/Linux. Num mesmo jogo, use um ou o outro.
    // =================================================================

    // Liga o Java às funções _kbhit e _getch do Windows (só usada no Windows)
    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);
        int _kbhit();
        int _getch();
    }

    static boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
    static boolean tecladoPreparado = false;

    // Retorna true se alguma tecla foi pressionada. NÃO espera: se nenhuma
    // tecla foi apertada, retorna false na hora e o jogo continua.
    public static boolean pressionouTecla() {
        if (windows) {
            return Crt.INSTANCE._kbhit() != 0;
        }
        prepararTecladoMacLinux();
        try {
            return System.in.available() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Retorna o código ASCII da tecla pressionada (ex.: 'a', 'w', 27 = ESC).
    // Se nenhuma tecla foi apertada, ESPERA até o usuário apertar uma.
    public static int obtemTeclaPressionada() {
        if (windows) {
            return Crt.INSTANCE._getch();
        }
        prepararTecladoMacLinux();
        try {
            return System.in.read();
        } catch (Exception e) {
            return -1;
        }
    }

    // Mostra "Pressione uma tecla para continuar..." e PARA o programa
    // até o jogador apertar qualquer tecla.
    //
    // Exemplo:
    //   System.out.println("GAME OVER");
    //   esperarTecla();
    public static void esperarTecla() {
        System.out.println("Pressione uma tecla para continuar...");
        obtemTeclaPressionada(); // fica esperando a tecla
    }

    // Mac/Linux: faz o terminal entregar cada tecla na hora (sem ENTER) e
    // sem mostrar a letra na tela. Quando o programa termina, volta ao normal.
    // Você não precisa chamar: as funções acima chamam sozinhas.
    static void prepararTecladoMacLinux() {
        if (tecladoPreparado) {
            return;
        }
        tecladoPreparado = true;
        executarStty("-icanon -echo -icrnl min 1"); // -icrnl: ENTER vira 13, igual ao Windows
        Runtime.getRuntime().addShutdownHook(new Thread(() -> executarStty("sane")));
    }

    static void executarStty(String opcoes) {
        try {
            new ProcessBuilder("sh", "-c", "stty " + opcoes + " < /dev/tty").start().waitFor();
        } catch (Exception e) {
            // se não conseguir, o teclado só vai funcionar com ENTER
        }
    }

    // =================================================================
    // 4. MENU
    // =================================================================

    // Mostra um menu numa caixa e deixa o jogador escolher uma opção:
    // W e S mudam a opção, ENTER escolhe e ESC sai sem escolher.
    //
    // Retorna a POSIÇÃO da opção escolhida no vetor (a primeira é 0),
    // ou -1 se o jogador apertou ESC.
    //
    // Exemplo:
    //   String[] opcoes = {"Iniciar jogo", "Recordes", "Sair"};
    //   int escolha = escolherNoMenu("Menu Principal", opcoes);
    //   if (escolha == 0) {
    //       // começa o jogo
    //   } else if (escolha == 1) {
    //       // mostra os recordes
    //   } else {
    //       // escolheu "Sair" (2) ou apertou ESC (-1)
    //   }
    //
    // Usa as funções do TECLADO, então valem os mesmos cuidados de lá.
    public static int escolherNoMenu(String titulo, String[] opcoes) {
        int selecionada = 0;

        while (true) {
            desenharMenu(titulo, opcoes, selecionada);
            int tecla = obtemTeclaPressionada(); // espera o jogador apertar uma tecla

            if (tecla == 'w' || tecla == 'W') {
                selecionada--;
                if (selecionada < 0) {
                    selecionada = opcoes.length - 1; // do topo, pula para a última
                }
            } else if (tecla == 's' || tecla == 'S') {
                selecionada++;
                if (selecionada >= opcoes.length) {
                    selecionada = 0; // da última, volta para o topo
                }
            } else if (tecla == 13 || tecla == 10) { // ENTER
                return selecionada;
            } else if (tecla == 27) { // ESC
                return -1;
            }
        }
    }

    // Desenha o menu numa caixa, com a opção selecionada em verde e
    // marcada com ">". A caixa cresce para caber o texto mais comprido.
    // É usada pelo escolherNoMenu, mas também dá para chamar sozinha.
    public static void desenharMenu(String titulo, String[] opcoes, int selecionada) {
        // descobre o tamanho do maior texto, para a caixa caber tudo
        int maior = titulo.length();
        for (int i = 0; i < opcoes.length; i++) {
            if (opcoes[i].length() > maior) {
                maior = opcoes[i].length();
            }
        }
        int largura = maior + 4; // espaço para o "> " e uma margem

        limparTela();
        System.out.println("╔" + "═".repeat(largura) + "╗");
        System.out.println("║  " + completar(titulo, maior) + "  ║");
        System.out.println("╠" + "═".repeat(largura) + "╣");
        for (int i = 0; i < opcoes.length; i++) {
            if (i == selecionada) {
                System.out.print("║> ");
                mudarCor(VERDE);
                System.out.print(completar(opcoes[i], maior));
                mudarCor(COR_PADRAO); // volta para a cor padrão
                System.out.println("  ║");
            } else {
                System.out.println("║  " + completar(opcoes[i], maior) + "  ║");
            }
        }
        System.out.println("╚" + "═".repeat(largura) + "╝");
        System.out.println();
        System.out.println("Use W/S para navegar e ENTER para selecionar. ESC para sair.");
    }

    // Completa o texto com espaços à direita até ter o tamanho pedido.
    // Ex.: completar("oi", 5) -> "oi   "
    static String completar(String texto, int tamanho) {
        return String.format("%-" + tamanho + "s", texto);
    }

    // =================================================================
    // 5. SORTEIO
    // =================================================================

    // Sorteia uma posição de um vetor de chances. Quanto maior o número,
    // maior a chance daquela posição ser sorteada.
    //
    // Retorna a POSIÇÃO sorteada (a primeira é 0).
    //
    // Exemplo: {10, 20, 30, 40}
    //   posição 0 tem 10% de chance, posição 1 tem 20%,
    //   posição 2 tem 30% e posição 3 tem 40%.
    //
    //   String[] itens = {"Espada", "Escudo", "Poção"};
    //   int[] chances  = {   10,       30,      60  };
    //   int sorteado = sortearComProbabilidades(chances);
    //   System.out.println("Você ganhou: " + itens[sorteado]);
    //
    // Os números não precisam somar 100: {1, 1, 2} dá 25%, 25% e 50%.
    public static int sortearComProbabilidades(int[] chances) {
        int soma = 0;
        for (int i = 0; i < chances.length; i++) {
            soma += chances[i];
        }

        double sorteio = Math.random() * soma;
        double acumulado = 0;

        for (int i = 0; i < chances.length; i++) {
            acumulado += chances[i];
            if (sorteio < acumulado) {
                return i;
            }
        }

        return -1; // Caso não encontre, embora não deva acontecer
    }

    // =================================================================
    // 6. SOM
    // =================================================================

    // Toca um arquivo de som .wav.
    //
    // Exemplo:
    //   tocarSom("sons/Cat-Meow.wav", false); // toca e o jogo segue
    //   tocarSom("sons/Cat-Meow.wav", true);  // espera o som acabar
    //
    // ATENÇÃO:
    //   - Coloque seus arquivos de som numa pasta "sons" do seu projeto.
    //   - Use barra normal "/" no caminho (funciona em Windows, Linux e Mac).
    //     Não use "\\".
    //   - O caminho é relativo à pasta de onde o programa é executado
    //     (normalmente a pasta do projeto). Se aparecer "Arquivo de som não
    //     encontrado", confira o nome do arquivo e de onde você está rodando.
    //   - Só funciona com arquivos .wav (mp3 NÃO funciona).
    //   - esperarTerminar = false: o som toca "por trás" e o jogo continua
    //     rodando normalmente. É o que você vai querer na maioria das vezes
    //     (pulo, tiro, batida...). Vários sons podem tocar ao mesmo tempo.
    //   - esperarTerminar = true: o programa PARA até o som acabar. Útil
    //     para momentos em que o jogo deve esperar (ex.: som de game over
    //     antes de fechar o programa).
    //   - Se o main terminar enquanto um som com "false" ainda toca, o
    //     programa fecha e o som é cortado.
    public static void tocarSom(String caminhoArquivoSom, boolean esperarTerminar) {
        try {
            // Abrindo o arquivo de som
            File soundFile = new File(caminhoArquivoSom);
            if (!soundFile.exists()) {
                System.out.println("Arquivo de som não encontrado: " + caminhoArquivoSom);
                return;
            }

            // Criando um AudioInputStream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // Obtendo as informações do formato de áudio
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // Criando o Clip e carregando o áudio
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);

            // O áudio já foi carregado no Clip, então o arquivo pode ser fechado
            audioStream.close();

            // Quando o som terminar, libera os recursos automaticamente
            audioClip.addLineListener(evento -> {
                if (evento.getType() == LineEvent.Type.STOP) {
                    audioClip.close();
                }
            });

            // Tocando o áudio
            audioClip.start();

            // Se pediu para esperar, segura o programa enquanto o som toca
            if (esperarTerminar) {
                Thread.sleep(audioClip.getMicrosecondLength() / 1000);
            }
        } catch (Exception e) {
            // formato não suportado, erro ao ler o arquivo, saída de som indisponível...
            System.err.println("Erro ao tocar o som '" + caminhoArquivoSom + "': " + e.getMessage());
        }
    }

    // =================================================================
    // 7. ARQUIVOS
    // =================================================================

    // Escreve (grava) um texto no arquivo, APAGANDO o que tinha antes.
    // Se o arquivo não existir, ele é criado.
    //
    // Exemplo: salvar o recorde do jogo
    //   escreverNoArquivo("recorde.txt", "" + pontos);
    //
    // Dica: para salvar várias informações, separe com "\n" (nova linha):
    //   escreverNoArquivo("save.txt", nome + "\n" + vidas + "\n" + fase);
    public static void escreverNoArquivo(String nomeArquivo, String conteudo) {
        // tentando abrir o arquivo para escrita
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            // Grava o conteúdo no arquivo
            writer.write(conteudo);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo '" + nomeArquivo + "': " + e.getMessage());
        }
    }

    // Lê (carrega) todo o conteúdo do arquivo e retorna como um texto só.
    // Cada linha do arquivo termina com "\n".
    // Se o arquivo não existir, retorna null.
    //
    // Exemplo: carregar o recorde salvo
    //   String texto = lerDoArquivo("recorde.txt");
    //   int recorde = 0;
    //   if (texto != null) {
    //       recorde = Integer.parseInt(texto.trim()); // trim tira o "\n" do fim
    //   }
    //
    // Dica: para separar as linhas num vetor, use texto.split("\n").
    public static String lerDoArquivo(String nomeArquivo) {
        StringBuilder conteudo = new StringBuilder();

        // tentando abrir o arquivo para leitura
        try (FileReader reader = new FileReader(nomeArquivo);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            String linha;

            // Lê cada linha do arquivo até chegar ao fim (null)
            while (true) {
                // Tenta ler a próxima linha
                linha = bufferedReader.readLine();

                // Se for nulo, chegamos ao final do arquivo, então saímos do loop.
                if (linha == null) {
                    break;
                }

                conteudo.append(linha).append("\n");
            }

            return conteudo.toString();

        } catch (IOException e) {
            // O arquivo não existe ou não pôde ser lido
            return null;
        }
    }

    // =================================================================
    // 8. JANELAS
    //
    // Caixas de diálogo que aparecem numa janela separada do terminal.
    // =================================================================

    // Mostra uma mensagem numa janela e espera o jogador clicar em OK.
    //
    // Exemplo:
    //   mostrarMensagemJanela("Você venceu!");
    public static void mostrarMensagemJanela(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Mensagem", JOptionPane.INFORMATION_MESSAGE);
    }

    // Mostra uma janela com uma pergunta e retorna o texto digitado.
    // Retorna null se o jogador clicar em "Cancelar" ou fechar a janela.
    //
    // Exemplo:
    //   String nome = lerTextoJanela("Qual o seu nome?");
    public static String lerTextoJanela(String pergunta) {
        return JOptionPane.showInputDialog(
            null,                         // Componente pai (null para centralizar na tela)
            pergunta,                     // Mensagem a ser exibida
            "Pergunta",                   // Título da janela
            JOptionPane.QUESTION_MESSAGE  // Tipo de ícone
        );
    }

    // Mostra uma janela pedindo um número inteiro e retorna o número.
    // Se o jogador digitar algo que não é número, avisa e pergunta de novo.
    // Retorna -1 se o jogador clicar em "Cancelar" ou fechar a janela.
    //
    // Exemplo:
    //   int idade = lerInteiroJanela("Quantos anos você tem?");
    public static int lerInteiroJanela(String pergunta) {
        while (true) {
            String texto = lerTextoJanela(pergunta);

            // Verifica se o usuário pressionou "Cancelar" ou fechou a janela
            if (texto == null) {
                return -1;
            }

            try {
                // Converte a String para int
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException e) {
                // Não era um número inteiro válido: avisa e o laço pergunta de novo
                JOptionPane.showMessageDialog(null, "Entrada inválida! Por favor, digite apenas números inteiros.",
                        "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    // =================================================================
    // DEMONSTRAÇÃO
    // Rode este arquivo para ver algumas funções funcionando.
    // =================================================================
    public static void main(String[] args) {
        String[] opcoes = {"Tela e cores", "Cronômetro", "Sorteio", "Som", "Sair"};

        while (true) {
            int escolha = escolherNoMenu("Demonstração da BibliotecaTCA", opcoes);
            limparTela();

            if (escolha == 0) {
                // escreve letras em posições e cores diferentes
                for (int i = 1; i <= 10; i++) {
                    mudarCor(i);
                    escreverEm(i, i * 3, "Linha " + i);
                }
                mudarCor(COR_PADRAO);
                irPara(12, 1); // cursor para baixo do desenho
                System.out.println();
            } else if (escolha == 1) {
                System.out.println("Contando 3 segundos...");
                iniciarCronometro();
                while (segundosPassados() < 3) {
                    escreverEm(2, 1, String.format("%.1f s", segundosPassados()));
                    esperar(100);
                }
                System.out.println();
            } else if (escolha == 2) {
                String[] itens = {"Espada", "Escudo", "Poção"};
                int[] chances  = {   10,       30,      60  };
                for (int i = 0; i < 5; i++) {
                    System.out.println("Você ganhou: " + itens[sortearComProbabilidades(chances)]);
                }
            } else if (escolha == 3) {
                System.out.println("Tocando som...");
                tocarSom("sons/Cat-Meow.wav", true);
            } else {
                return; // "Sair" ou ESC
            }

            esperarTecla();
        }
    }
}
