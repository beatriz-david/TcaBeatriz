import java.util.Scanner;

public class TcaBeatrizELuiza {

    static Scanner Scanner = new Scanner(System.in);

    public static char lerChar() {
        return Scanner.next().charAt(0);
    }

    public static int lerInt() {
        return Scanner.nextInt();
    }

    // possibilidades de trilha
    public static final int[][] TRILHAS = { //cada chave é uma trilha
            { 0, 1, 2 }, { 2, 14, 23 }, { 21, 22, 23 }, { 0, 9, 21 }, // quadrado maior
            { 3, 4, 5 }, { 5, 13, 20 }, { 18, 19, 20 }, { 3, 10, 18 }, // quadrado medio
            { 6, 7, 8 }, { 8, 12, 17 }, { 15, 16, 17 }, { 6, 11, 15 }, // quadrado pequeno
            { 1, 4, 7 }, { 9, 10, 11 }, { 12, 13, 14 }, { 16, 19, 22 } // linhas interligando os quadrados
    };

    public static final char PECA_AMARELA = '@';
    public static final char PECA_CIANO = '#';

    public static final int[][] COORDENADASTABULEIRO = {//coordenadas de cada casa do tabuleiro
            /* 0 */{ 0, 1 },
            /* 1 */{ 0, 15 },
            /* 2 */{ 0, 29 },
            /* 3 */{ 2, 6 },
            /* 4 */{ 2, 16 },
            /* 5 */{ 2, 26 },
            /* 6 */{ 4, 11 },
            /* 7 */{ 4, 16 },
            /* 8 */{ 4, 21 },
            /* 9 */{ 6, 1 },
            /* 10 */{ 6, 5 },
            /* 11 */{ 6, 10 },
            /* 12 */{ 6, 19 },
            /* 13 */{ 6, 24 },
            /* 14 */{ 6, 29 },
            /* 15 */{ 8, 10 },
            /* 16 */{ 8, 15 },
            /* 17 */{ 8, 20 },
            /* 18 */{ 10, 5 },
            /* 19 */{ 10, 15 },
            /* 20 */{ 10, 25 },
            /* 21 */{ 12, 1 },
            /* 22 */{ 12, 15 },
            /* 23 */{ 12, 29 }
    };
    public static final int[] pecasJog1 = { -1, -1, -1, -1, -1, -1, -1, -1, -1, }; //-1 significa que ainda nao tem peca colocada no tabuleiro
    public static final int[] pecasJog2 = { -1, -1, -1, -1, -1, -1, -1, -1, -1, };

    public static void main(String[] args) {
        int pecas2 = 9;//pecas iniciais de cada jogador
        int pecas1 = 9;

        char[][] tabuleiro = {
                { '(', '0', ')', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '(', '1', ')', '-', '-', '-',
                        '-', '-', '-', '-', '-', '-', '-', '-', '(', '2', ')' },
                { ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ',
                        ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ' },
                { ' ', '|', ' ', ' ', ' ', '(', '3', ')', '-', '-', '-', '-', '-', '-', '-', '(', '4', ')', '-', '-',
                        '-', '-', '-', '-', '-', '(', '5', ')', ' ', ' ', '|' },
                { ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ',
                        ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ' },
                { ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', '(', '6', ')', '-', '-', '(', '7', ')', '-', '-',
                        '(', '8', ')', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ' },
                { ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                        ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ' },
                { '(', '9', ')', '-', '(', '1', '0', ')', '-', '(', '1', '1', ')', ' ', ' ', ' ', ' ', ' ', '(', '1',
                        '2', ')', '-', '(', '1', '3', ')', '-', '(', '1', '4', ')' },
                { ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                        ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ' },
                { ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', '(', '1', '5', ')', '-', '(', '1', '6', ')', '-', '(',
                        '1', '7', ')', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ' },
                { ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ',
                        ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ' },
                { ' ', '|', ' ', ' ', '(', '1', '8', ')', '-', '-', '-', '-', '-', '-', '(', '1', '9', ')', '-', '-',
                        '-', '-', '-', '-', '(', '2', '0', ')', ' ', ' ', '|' },
                { ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ',
                        ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ' },
                { '(', '2', '1', ')', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '(', '2', '2', ')', '-', '-',
                        '-', '-', '-', '-', '-', '-', '-', '-', '(', '2', '3', ')' }
        };

        BibliotecaTCA.mudarCor(211); //muda a cor da impressao
        System.out.println(
                        "  ███  ███   ███   ███     ████  █████    █████ ████  ███ █     █   █  ███    \n" + //
                         "   █░░█ ░░█ █ ░░░ █ ░░█    █░░░█ █░░░░░    ░█░░░█░░░█  █░░█░    █░  █░█ ░░█   \n" + //
                         "   █░░█░ ░█░█░ ██░█░ ░█░   █░░░█░████░░░    █░░░████░░ █░░█░░   █████░█████░  \n" + //
                         "█  █░░█░░ █░█░░ █░█░░ █░░  █░░ █░█░░░░      █░░ █░░█░ ░█░░█░░   █░░░█░█░░░█░░ \n" + //
                         " ██ ░░ ███ ░░███ ░░███ ░░  ████ ░█████░     █░░ █░░░█░███░█████ █░░░█░█░░░█░░ \n" + //
                         "  ░░ ░  ░░░ ░ ░░░ ░ ░░░ ░   ░░░░ ░░░░░░      ░░  ░░  ░ ░░░ ░░░░░ ░░  ░░░░  ░░ \n" + //
                         "   ░░    ░░░   ░░░   ░░░     ░░░░  ░░░░░      ░   ░   ░ ░░░ ░░░░░ ░   ░ ░   ░  ");
        System.out.println();
        BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);

        System.out.print("APERTE "); //APERTE ENTER PARA INICIAR
        BibliotecaTCA.mudarCor(117);
        System.out.print("ENTER ");
        BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
        System.out.println("PARA INICIAR");
        Scanner.nextLine();


        // inicia o cronometro da rodada
        BibliotecaTCA.iniciarCronometro();

        // vetor que guarda as casas que estao livres(sem nenhuma peca); se esta livre é true
        boolean[] casasLivres = new boolean[24];
        for (int i = 0; i < casasLivres.length; i++) {
            casasLivres[i] = true;  //inicializa todas as casas como true(livres)
        }

        imprimirTabuleiro(tabuleiro);

        // FASE 1 - COLOCAR PECAS NO TABULEIRO

        // loop para colocar as pecas no tabuleiro

        // deixa a frase colorida
        BibliotecaTCA.mudarCor(198);
        System.out.println("FASE 1 - COLOCAR AS PEÇAS NO TABULEIRO");
        BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);

        int i = 0;
        while (i < 9) {// para rodar 9 vezes a fase dos jogadores colocarem as pecas - eles colocam 9 pecas

            printJog1();//imprime "jogador 1"
            System.out.println(", digite onde você quer colocar sua peça");
            int vez;
            vez = 1;
            int pecaDaVez1 = colocarPecas(tabuleiro, casasLivres, COORDENADASTABULEIRO, pecasJog1, i, vez);
            
            if (verificarTrilha(TRILHAS, pecaDaVez1, pecasJog1)) {//se for true significa que o jogador fez uma trilha
                
                BibliotecaTCA.mudarCor(184);
                System.out.println("PARABÉNS! VOCÊ FEZ UMA TRILHA! Jogador 1, qual peça do jogador 2 você quer remover?");
                BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
                
                imprimirTabuleiroComImagem(tabuleiro);//imprime o tabuleiro com o desenho e a mensagem 

                removerPeca(tabuleiro, pecasJog2, casasLivres); //remove a peca
                
                pecas2--;
            }
             
           imprimirTabuleiro(tabuleiro);

            printJog2();//imprime "jogador 2"
            System.out.println(", digite onde você quer colocar sua peça");
            vez = 2;
            int pecaDaVez2 = colocarPecas(tabuleiro, casasLivres, COORDENADASTABULEIRO, pecasJog2, i, vez);

            if (verificarTrilha(TRILHAS, pecaDaVez2, pecasJog2)) {

                BibliotecaTCA.mudarCor(51);//muda a cor pra ciano
                System.out.println("PARABÉNS! VOCÊ FEZ UMA TRILHA! Jogador 2, qual peça do jogador 1 você quer remover?");
                BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);

                   imprimirTabuleiroComImagem(tabuleiro);//imprime com imagem
                removerPeca(tabuleiro, pecasJog1, casasLivres);//remove a peca
                
                pecas1--;
            }
             
            imprimirTabuleiro(tabuleiro);//imprime o tabuleiro fora do if pra nao imprimir duas vezes se fizer trilha

            i++;
        }

        // FASE 2 - MOVER AS PECAS PELO TABULEIRO
        BibliotecaTCA.mudarCor(129);
        System.out.println(
                "FASE 2 - AGORA, OS JOGADORES SÓ PODEM MOVER AS PEÇAS PELO TABULEIRO PARA AS CASAS ADJACENTES");
        BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
        
        while (pecas1 >= 3 && pecas2 >= 3) {
            int vez;

            // jogador 1
            if (pecas1 == 3 && pecas2 > 3) {//se o jogador 1 tiver so 3 pecas e o jogador 2 mais de 3
                vez = 1;
                // se na fase 3 o jogador fizer uma trilha, o metodo FASE_3 retorna true
                if (FASE_3(tabuleiro, casasLivres, pecasJog1, COORDENADASTABULEIRO, vez)) {
                    
                    printJog1();// imprime o jogador 1 amarelo
                    BibliotecaTCA.mudarCor(184);
                    System.out.println("PARABÉNS! VOCÊ FEZ UMA TRILHA!");
                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
                   

                    imprimirTabuleiroComImagem(tabuleiro);//imprime com os desenhos
                
                    printJog1();// imprime o texto jogador 1 amarelo
                    System.out.println(", qual peça do jogador 2 você quer remover?");
                    removerPeca(tabuleiro, pecasJog2, casasLivres);//remove a peca
                    pecas2--;
                }
                   imprimirTabuleiro(tabuleiro);//imprime o tabuleiro normal
            } else if (pecas1 > 3) {//senao, se o jogador 1 tiver mais de 3 pecas
                vez = 1;
                int pecaDaVez1 = moverPecasJog(TabuleiroTca.regras, tabuleiro, COORDENADASTABULEIRO, casasLivres,
                        pecasJog1, vez);
               
                        
                if (verificarTrilha(TRILHAS, pecaDaVez1, pecasJog1)) {

                    BibliotecaTCA.mudarCor(164);
                    System.out.println("PARABÉNS! VOCÊ FEZ UMA TRILHA!");


                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
                    
                    imprimirTabuleiroComImagem(tabuleiro);//imprime com os desenhos
                
                    printJog1();
                    System.out.println(", qual peça do jogador 2 você quer remover?");
                    removerPeca(tabuleiro, pecasJog2, casasLivres);
                    pecas2--;

                }
               
                imprimirTabuleiro(tabuleiro);//imprime normal
            }
            // pra encerrar o codigo antes da jogada do jogador 2, caso ele fique com 2 pecas
            if (pecas2 < 3) {
                break;
            }

            // jogador 2 se tiver 3 pecas
            if (pecas2 == 3 && pecas1 > 3) {
                vez = 2;
                // se na fase 3 o jogador fizer uma trilha, o metodo FASE_3 retorna true
                if (FASE_3(tabuleiro, casasLivres, pecasJog2, COORDENADASTABULEIRO, vez)) {

                    BibliotecaTCA.mudarCor(51);//muda a cor da mensagem da trilha
                    System.out.println(
                            "PARABÉNS! VOCÊ FEZ UMA TRILHA!");


                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
                    imprimirTabuleiroComImagem(tabuleiro);//imprime o tabuleiro com imagem

                   printJog2();//imprime jogador 2
                    System.out.println(", qual peça do jogador 1 você quer remover?");
                    removerPeca(tabuleiro, pecasJog1, casasLivres);//remove a peca do jogador
                    pecas1--;
                    
                }
                  imprimirTabuleiro(tabuleiro);


                // se o jogador tiver mais de 3 pecas
            } else if (pecas2 > 3) {
                vez = 2;
                int pecaDaVez2 = moverPecasJog(TabuleiroTca.regras, tabuleiro, COORDENADASTABULEIRO, casasLivres,pecasJog2, vez);//move a peca pra casa que o jogador quer
               

                if (verificarTrilha(TRILHAS, pecaDaVez2, pecasJog2)) {//verifica se ele fez trilha

                    BibliotecaTCA.mudarCor(164);
                    System.out.println("PARABÉNS! VOCÊ FEZ UMA TRILHA!");


                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
                     imprimirTabuleiroComImagem(tabuleiro);//imprime o tabuleiro com a mensagem ja que ele fez trilha

                    printJog2();
                    System.out.println(", qual peça do jogador 1 você quer remover?");
                    removerPeca(tabuleiro, pecasJog1, casasLivres);//remove a peca
                    pecas1--;

                    
                }

                 imprimirTabuleiro(tabuleiro);//imprime tanto depois de remover a peca em caso de trilha quanto depois de apenas mover a peca
            }

            // se os jogadores estiverem empatados
            if (pecas1 == 3 && pecas2 == 3) {

                BibliotecaTCA.mudarCor(197);//muda a cor da msg
                System.out.println("ATENÇÃO!! VOCÊS ESTÃO EMPATADOS! O primeiro jogador a fazer uma trilha ganha!");
                BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);

                // loop para fazer as ultimas jogadas, se o metodo FASE_3 for true significa que
                // um jogador fez trilha, entao ha um ganhador

                while (pecas1 == pecas2) {//enquanto eles estiverem empatados
                    vez = 1;
                    boolean faseFinalTrilha = FASE_3(tabuleiro, casasLivres, pecasJog1, COORDENADASTABULEIRO, vez);//move a peca
                    imprimirTabuleiro(tabuleiro);
                    if (faseFinalTrilha) {//encerra a partida pois o jogador fez uma trilha
                        pecas2--;
                        break;
                    }
                    vez = 2;
                    faseFinalTrilha = FASE_3(tabuleiro, casasLivres, pecasJog2, COORDENADASTABULEIRO, vez);
                    imprimirTabuleiro(tabuleiro);
                    if (faseFinalTrilha) {
                        pecas1--;
                        break;
                    }

                }
                break;
            }

            // fim do while
        }

        if (pecas1 < 3) {//se o jogador 2 ganhou
            // coloca os segundos passados na variavel tempo
            double tempo = BibliotecaTCA.segundosPassados();
          
            BibliotecaTCA.mudarCor(51);//deixa cor ciano


            System.out.println(
            "  ▓▓▓  ▓▓▓   ▓▓▓   ▓▓▓  ▓▓▓▓   ▓▓▓  ▓▓▓▓      ▓▓▓    \n" +
            "   ▓  ▓   ▓ ▓     ▓   ▓ ▓   ▓ ▓   ▓ ▓   ▓    ▓   ▓   \n" +
            "   ▓  ▓   ▓ ▓  ▓▓ ▓▓▓▓▓ ▓   ▓ ▓   ▓ ▓▓▓▓        ▓    \n" +
            "▓  ▓  ▓   ▓ ▓   ▓ ▓   ▓ ▓   ▓ ▓   ▓ ▓  ▓       ▓    ▓\n" +
            " ▓▓    ▓▓▓   ▓▓▓  ▓   ▓ ▓▓▓▓   ▓▓▓  ▓   ▓    ▓▓▓▓▓ ▓ \n" +
            "\n" +
            " ▓   ▓  ▓▓▓   ▓▓▓  ▓▓▓▓▓     ▓▓▓   ▓▓▓  ▓   ▓ ▓   ▓  ▓▓▓  ▓   ▓     ▓▓▓       ▓▓▓  ▓▓▓   ▓▓▓   ▓▓▓  ▓ \n" +
            " ▓   ▓ ▓   ▓ ▓     ▓        ▓     ▓   ▓ ▓▓  ▓ ▓   ▓ ▓   ▓ ▓   ▓    ▓   ▓       ▓  ▓   ▓ ▓     ▓   ▓ ▓ \n" +
            " ▓   ▓ ▓   ▓ ▓     ▓▓▓▓     ▓  ▓▓ ▓▓▓▓▓ ▓ ▓ ▓ ▓▓▓▓▓ ▓   ▓ ▓   ▓    ▓   ▓       ▓  ▓   ▓ ▓  ▓▓ ▓   ▓ ▓ \n" +
            "  ▓ ▓  ▓   ▓ ▓     ▓        ▓   ▓ ▓   ▓ ▓  ▓▓ ▓   ▓ ▓   ▓ ▓   ▓    ▓   ▓    ▓  ▓  ▓   ▓ ▓   ▓ ▓   ▓   \n" +
            "   ▓    ▓▓▓   ▓▓▓  ▓▓▓▓▓     ▓▓▓  ▓   ▓ ▓   ▓ ▓   ▓  ▓▓▓   ▓▓▓      ▓▓▓      ▓▓    ▓▓▓   ▓▓▓   ▓▓▓  ▓ "
        );
           System.out.println("COM " + pecas2 + " PEÇAS!");
         BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);//volta a cor padrao

         int minutos = (int) (tempo / 60); //como tempo esta em segundos, dividindo por 60 pega os minutos 
         int segundos = (int) (tempo % 60);// 'mod' 60, pega o resto da divisao por 60, ou seja, os segundos

           BibliotecaTCA.mudarCor(128);
         System.out.println("O JOGO DUROU " + minutos + " MINUTO(S) E " + segundos + " SEGUNDOS");
             BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
           

        } else {//senao, foi o jog 1
            double tempo = BibliotecaTCA.segundosPassados(); // ve quanto tempo passou desde o enter do inicio

             BibliotecaTCA.mudarCor(184);//muda pra amarelo
               

            System.out.println(
            "  ▓▓▓  ▓▓▓   ▓▓▓   ▓▓▓  ▓▓▓▓   ▓▓▓  ▓▓▓▓       ▓     \n" +
            "   ▓  ▓   ▓ ▓     ▓   ▓ ▓   ▓ ▓   ▓ ▓   ▓     ▓▓     \n" +
            "   ▓  ▓   ▓ ▓  ▓▓ ▓▓▓▓▓ ▓   ▓ ▓   ▓ ▓▓▓▓       ▓     \n" +
            "▓  ▓  ▓   ▓ ▓   ▓ ▓   ▓ ▓   ▓ ▓   ▓ ▓  ▓       ▓    ▓\n" +
            " ▓▓    ▓▓▓   ▓▓▓  ▓   ▓ ▓▓▓▓   ▓▓▓  ▓   ▓     ▓▓▓  ▓ \n" +
            "\n" +
            " ▓   ▓  ▓▓▓   ▓▓▓  ▓▓▓▓▓     ▓▓▓   ▓▓▓  ▓   ▓ ▓   ▓  ▓▓▓  ▓   ▓     ▓▓▓       ▓▓▓  ▓▓▓   ▓▓▓   ▓▓▓  ▓ \n" +
            " ▓   ▓ ▓   ▓ ▓     ▓        ▓     ▓   ▓ ▓▓  ▓ ▓   ▓ ▓   ▓ ▓   ▓    ▓   ▓       ▓  ▓   ▓ ▓     ▓   ▓ ▓ \n" +
            " ▓   ▓ ▓   ▓ ▓     ▓▓▓▓     ▓  ▓▓ ▓▓▓▓▓ ▓ ▓ ▓ ▓▓▓▓▓ ▓   ▓ ▓   ▓    ▓   ▓       ▓  ▓   ▓ ▓  ▓▓ ▓   ▓ ▓ \n" +
            "  ▓ ▓  ▓   ▓ ▓     ▓        ▓   ▓ ▓   ▓ ▓  ▓▓ ▓   ▓ ▓   ▓ ▓   ▓    ▓   ▓    ▓  ▓  ▓   ▓ ▓   ▓ ▓   ▓   \n" +
            "   ▓    ▓▓▓   ▓▓▓  ▓▓▓▓▓     ▓▓▓  ▓   ▓ ▓   ▓ ▓   ▓  ▓▓▓   ▓▓▓      ▓▓▓      ▓▓    ▓▓▓   ▓▓▓   ▓▓▓  ▓ "
        );

             System.out.println("COM " + pecas1 + " PEÇAS!");
             BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);//volta pro padrao
            

         int minutos = (int) (tempo / 60); //como tempo esta em segundos, dividindo por 60 pega os minutos 
         int segundos = (int) (tempo % 60);// 'mod' 60, pega o resto da divisao por 60, ou seja, os segundos

           BibliotecaTCA.mudarCor(128);
         System.out.println("O JOGO DUROU " + minutos + " MINUTO(S) E " + segundos + " SEGUNDOS");
           BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);

        }

    }

    public static boolean FASE_3(char[][] tabuleiro, boolean[] casasLivres, int[] pecasJog,
            int[][] COORDENADASTABULEIRO, int vez) {

        BibliotecaTCA.mudarCor(135);//muda a cor da msg
        System.out.println("JOGADOR " + vez + ", VOCÊ ESTÁ NA FASE 3 E PODE VOAR PELO TABULEIRO");
        BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);

        System.out.println("Jogador " + vez + ", digite a origem e o destino da peça que você quer mover");//so le a peca dentro do loop caso de erro

        boolean colocou = false;//para finalizar o while
        boolean trilha = false;//é o que o metodo vai retornar
        while (!colocou) { //enquanto ele nao tiver colocado


            int origem = lerInt();
            int destino = lerInt();
            if (origem > 23 || origem < 0 || destino > 23 || destino < 0 || origem == destino
                    || casasLivres[destino] == false) {
                System.out.println("Jogador, digite origem e destino válidos (0-23) e que não estejam ocupados");
                continue;
            }
            // pega a posicao que a peca tava quando tava na origem
            int posicaoPeca = posicaoPecaVetorPecas(pecasJog, origem);
            if (posicaoPeca == -1) {
                System.out.println("Você não tem peça na origem digitada!");
                continue;
            }
            reescreverNumeroCasa(tabuleiro, COORDENADASTABULEIRO, origem);

            // pega a coordenada do destiino
            int linha = COORDENADASTABULEIRO[destino][0];
            int coluna = COORDENADASTABULEIRO[destino][1];

            if (vez == 1) {

                tabuleiro[linha][coluna] = PECA_AMARELA;
            } else {

                tabuleiro[linha][coluna] = PECA_CIANO;
            }
            //alinha a matriz caso o numero ocupe 2 caracteres
            if (destino >= 10) {
                tabuleiro[linha][coluna + 1] = ' ';
            }
            // deixa as casas atualizadas no vetor

            // coloca, na mesma posicao da origem no vetor, o destino(casa atual)
            pecasJog[posicaoPeca] = destino;
            casasLivres[origem] = true;
            casasLivres[destino] = false;
            // destino pois essa peca acabou de ser colocada
            trilha = verificarTrilha(TRILHAS, destino, pecasJog);

            colocou = true;

        }
        return trilha;
    }

    // Colocar pecas do jogador
    public static int colocarPecas(char[][] tabuleiro, boolean[] casasLivres, int[][] COORDENADASTABULEIRO,
            int[] pecasJog, int i, int vez) {

        boolean colocou = false;
        while (colocou == false) {

            int destino = lerInt();

            if (destino < 0 || destino > 23) {
                System.out.println("Casa inválida! Digite uma casa que exista para prosseguir");
                continue;
            }

            if (!casasLivres[destino]) {
                System.out.println("Casa inválida! Digite uma casa que não esteja ocupada para prosseguir");
                continue;
            }

            int linha = COORDENADASTABULEIRO[destino][0];
            int coluna = COORDENADASTABULEIRO[destino][1];

            if (vez == 1) {
                tabuleiro[linha][coluna] = PECA_AMARELA;
            } else {
                tabuleiro[linha][coluna] = PECA_CIANO;
            }
            if (destino >= 10) {
                tabuleiro[linha][coluna + 1] = ' ';
            }
            colocou = true;
            casasLivres[destino] = false;
            pecasJog[i] = destino;
            return destino;
        }
        return -1;
    }

    public static void imprimirTabuleiro(char[][] tabuleiro) {
       
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                char atual = tabuleiro[i][j];

                if (atual == PECA_AMARELA) {
                    BibliotecaTCA.mudarCor(184); // amarelo
                    System.out.print(atual);
                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO); // volta ao normal
                } else if (atual == PECA_CIANO) {
                    BibliotecaTCA.mudarCor(51); // ciano
                    System.out.print(atual);
                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO); // volta a cor normal pra nao pintar o terminal
                                                                      // todo
                } else if (atual == '-' || atual == '|' || atual == '(' || atual == ')') {
                    BibliotecaTCA.mudarCor(205); // deixa o tabuleiro rosa
                    System.out.print(atual);
                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
                } else {
                    System.out.print(atual);
                }
            }
           
            System.out.println();
        }
        System.out.println();

    }

    public static boolean verificarTrilha(int[][] TRILHA, int pecaDaVez, int[] pecasJog) {
        for (int i = 0; i < TRILHA.length; i++) {// percorre as linhas
            // pega as 3 pecas de cada possivel trilha 
            int pecaTrilha1 = TRILHA[i][0];
            int pecaTrilha2 = TRILHA[i][1];
            int pecaTrilha3 = TRILHA[i][2];

            if (pecaTrilha1 == pecaDaVez || pecaTrilha2 == pecaDaVez || pecaTrilha3 == pecaDaVez) {//ve se a ultima peca movida faz parte de uma trilha
                if (posicaoPecaVetorPecas(pecasJog, pecaTrilha1) != -1 &&     //verifica se as pecas da trilha existem no vetor de pecasJog, pq se nao existirem retorna -1
                        posicaoPecaVetorPecas(pecasJog, pecaTrilha2) != -1 &&   // e verifica se a posicao da "pecaTrilha" - ex: peca na casa 1 - existe dentro do vetor de pecas jog, ou seja, a o valor da peca na posicao pecaTrilha nao é igual a -1
                        posicaoPecaVetorPecas(pecasJog, pecaTrilha3) != -1) {

                    return true;

                }
            }
        }

        return false;

    }


    public static void removerPeca(char[][] tabuleiro, int[] pecasJog, boolean[] casasLivres) {

        boolean removeu = false;
        while (!removeu) {
            int pecaRemover = lerInt();

            if (pecaRemover > 23 || pecaRemover < 0) {
                System.out.println("Jogador, digite uma peça válida (0-23)");
                continue;
            }

            int posicaoPeca = posicaoPecaVetorPecas(pecasJog, pecaRemover);//pega a posicao da peca no vetor das pecas do jog
            
            if (posicaoPeca == -1) { //se é =-1 significa que nao tem pecas no vetor das casas
                System.out.println(
                        "Jogador, não há peças para remover nesta casa. Digite uma peça válida para continuar");
                continue;
            }

            if (verificarTrilha(TRILHAS, pecaRemover, pecasJog)&&todasPecasEmTrilha(pecasJog, TRILHAS)==false) {//se a peca escolhida estiver em trilha, e nem todas as pecas do jogador estao em trilha
            //  (o metodo todasPecas... retorna false se nem todas as pecas do jogador estiverem em trilha)

                System.out.println("Jogador, digite uma peça que não está em trilha para remover!");
                continue;
            }

            pecasJog[posicaoPeca] = -1; //marca que nao tem nenhuma peca na casa que foi removida a peca
            reescreverNumeroCasa(tabuleiro, COORDENADASTABULEIRO, pecaRemover);
            casasLivres[pecaRemover] = true; // a peca foi retirada, a casa esta livre
            removeu = true;
        }
    }

    public static int moverPecasJog(int[][] regras, char[][] tabuleiro, int[][] COORDENADASTABULEIRO,
            boolean[] casasLivres, int[] pecasJog, int vez) {

        boolean colocou = false;
        int origem;
        int destino;
        while (!colocou) {

            if (vez == 1) {
                printJog1();
                ;
            } else {
                printJog2();
            }

            System.out.println(", digite a origem e o destino da peça que você quer mover");
            origem = lerInt();
            destino = lerInt();

            if (origem == destino || origem < 0 || origem > 23 || destino < 0 || destino > 23) { //verifica se origem e destinos sao validos
                System.out.println("Jogador " + vez + ", digite uma origem e um destino válidos");
                continue;
            }
            if (casasLivres[destino] == false) {
                System.out.println("O destino desejado está ocupado");
                continue;
            }

            int posicaoPeca = posicaoPecaVetorPecas(pecasJog, origem); //pega a posicao da peca no vetor e verifica se ela existe
            if (posicaoPeca == -1) {
                System.out.println("Você não tem nenhuma peça na casa " + origem + ". Escolha uma peça sua.");
                continue;
            }

            boolean podeMover = TabuleiroTca.possoMover(origem, destino, TabuleiroTca.regras);//verifica se as casas sao adjacentes
            if (podeMover) {

                reescreverNumeroCasa(tabuleiro, COORDENADASTABULEIRO, origem); //reescreve o numero da casa

                pecasJog[posicaoPeca] = destino; //substitui a origem pelo destino

                int linha = COORDENADASTABULEIRO[destino][0];
                int coluna = COORDENADASTABULEIRO[destino][1];
                // coloca a peca no tabuleiro
                if (vez == 1) {

                    tabuleiro[linha][coluna] = PECA_AMARELA;
                } else {                                      //coloca a peca do jogador

                    tabuleiro[linha][coluna] = PECA_CIANO;
                }
                
                if (destino >= 10) {   //o espaco serve para nao desconfigurar a matriz, ja que a peca ocupa apenas 1 caractere
                    tabuleiro[linha][coluna + 1] = ' ';
                }
               
                casasLivres[origem] = true; // para atualiza o estado das casas no vetor
                casasLivres[destino] = false;
                colocou = true;
                return destino;

            } else {
                System.out.println("As casas não são adjacentes!");
            }

        }
        return -1;
    }

    // metodo para colocar o numero na origem, quando a peca for para outro lugar,
    // ou quando a peca for retirada por trilha
    public static void reescreverNumeroCasa(char[][] tabuleiro, int[][] COORDENADASTABULEIRO, int casa) {
        int linha = COORDENADASTABULEIRO[casa][0];  //ex *0 -> [0][1]->coordenadas para chegar a casa 0 do tabueleiro
        int coluna = COORDENADASTABULEIRO[casa][1];

        if (casa < 10) {
            tabuleiro[linha][coluna] = (char) (casa + '0');//+ 0 para transformar em char sem perder o valor correto do numero
        } else {
            tabuleiro[linha][coluna] = (char) ((casa / 10) + '0'); // divide por 10, pega a dezena
            tabuleiro[linha][coluna + 1] = (char) ((casa % 10) + '0'); // mod 10, ou seja, pega o resto da divisao por 10, a unidade
        }                   //+1 pra colocar ao lado da dezena
    }

    public static int posicaoPecaVetorPecas(int[] pecasJog, int origem) { //funcao que pega a posicao que a casa-origem se encontra no vetor
        int posicaoPeca = -1; //para retornar -1 caso a peca nao exista      //ex [13, 22, 19, 21] origem = 13, a funcao retorna 0
        for (int i = 0; i < pecasJog.length; i++) {
            if (pecasJog[i] == origem) { //se encontrar a peca no vetor
                posicaoPeca = i;
            }
        }

        return posicaoPeca;
    }

    //apenas imprime"jogador x" para facilitar
    public static void printJog1() {
         BibliotecaTCA.mudarCor(184);//amarelo
        System.out.print("Jogador 1 ");
        BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
    }

    public static void printJog2() { 
        BibliotecaTCA.mudarCor(51);//ciano
        System.out.print("Jogador 2 ");
        BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
    }

    public static void imprimirTabuleiroComImagem(char[][] tabuleiro) {//mesma funcao do imprimir tabuleiro mas com a imagem de comemoracao

        // pega a funcao do DesenhosTCA e sorteia um numero pra ser o do desenho
        int desenhoAtual = DesenhosTCA.sortearDesenho();

        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                char atual = tabuleiro[i][j];

                if (atual == PECA_AMARELA) {
                    BibliotecaTCA.mudarCor(184); // amarelo
                    System.out.print(atual);
                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO); // volta ao normal
                } else if (atual == PECA_CIANO) {
                    BibliotecaTCA.mudarCor(51); // ciano
                    System.out.print(atual);
                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO); // volta a cor normal pra nao pintar o terminal
                                                                      // todo
                } else if (atual == '-' || atual == '|' || atual == '(' || atual == ')') {
                    BibliotecaTCA.mudarCor(205); // deixa o tabuleiro rosa
                    System.out.print(atual);
                    BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);
                } else {
                    System.out.print(atual);
                }
            }
            // Imprime a linha correspondente da arte sorteada ao lado do tabuleiro
            BibliotecaTCA.mudarCor(DesenhosTCA.CORES[desenhoAtual]);
            System.out.print(DesenhosTCA.DESENHOS[desenhoAtual][i]);
            BibliotecaTCA.mudarCor(BibliotecaTCA.COR_PADRAO);

            System.out.println();
        }
        System.out.println();

    }
        public static boolean todasPecasEmTrilha(int[]pecasJog, int[][]TRILHAS){
          
           for (int i = 0; i < pecasJog.length; i++) {
            if (pecasJog[i]!=-1) {// procede apenas as casas que nao sao -1
                                  //se tiver alguma peca na posicao pecasJog[i] (-1 significa que nao tem)
                
            if(!verificarTrilha(TRILHAS, pecasJog[i], pecasJog)){//se a peca pecasJog[i] nao estiver em trilha (se verificar trilha for false)
                return false; //existe pelo menos uma peca que nao esta em trilha
            }
           }
        }
        
        return true; //retorna true, ou seja, todas as pecas do jogador estao em trilha
        }
   
}