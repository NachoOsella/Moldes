package ar.edu.utn.frc.tup.lciii;

import java.util.*;

public class PokerGame {

    // Lista de jugadores que participan en el juego
    private final List<Player> players;
    // La mesa donde se juega, contiene el mazo, cartas comunitarias, etc.
    private final Table table;
    // Evaluador de manos para determinar la fuerza de las cartas de cada jugador
    private final PokerHand evaluator;
    // Índice del jugador que es dealer en la ronda actual
    private int dealerIndex;
    // Scanner para leer la entrada del usuario (jugador humano)
    private final Scanner scanner;
    // Maneja las apuestas, el pozo, ciegas, etc.
    private final PokerBets bets;

    // Constructor: inicializa las estructuras y asigna los valores iniciales
    public PokerGame() {
        this.players = new ArrayList<>();
        this.table = new Table();
        this.evaluator = new PokerHand();
        this.dealerIndex = 0; // empieza con el primer jugador como dealer
        this.scanner = new Scanner(System.in);
        this.bets = new PokerBets(5, 10); // small blind = 5 fichas, big blind = 10 fichas
    }

    // Método principal que controla el flujo completo de una partida
    public void play() {
        setupGame(); // configura los jugadores y el dealer inicial
        boolean continueGame = true;

        // Ciclo principal del juego: mientras el jugador quiera seguir jugando
        while (continueGame) {
            startNewRound();     // prepara la mesa, el mazo y resetea apuestas
            dealInitialCards();  // reparte las cartas iniciales a cada jugador
            printTableStatus();  // muestra el estado actual de la mesa (cartas, fichas)
            placeBlinds();       // coloca las apuestas obligatorias (ciegas)
            printTableStatus();  // actualiza estado tras las ciegas

            playRounds();        // se juegan las rondas de apuesta y se van descubriendo cartas comunitarias

            determineWinner();   // evalúa quién ganó la mano y reparte el pozo
            continueGame = askToPlayAgain();  // pregunta si se quiere jugar otra mano
            moveDealer();        // mueve el dealer al siguiente jugador para la próxima ronda
        }

        endGame(); // finaliza el juego (puede mostrar estadísticas o cerrar recursos)
    }

    // Configura el juego creando jugadores (1 humano y varios bots) y asignando dealer
    private void setupGame() {
        // Se agregan jugadores, el primero es humano (false) y los demás bots (true)
        players.add(new Player("HERNAN", false, 1000));            // jugador humano
        players.add(new Player("AceBot 😊", true, 1000));          // bots con distintos nombres
        players.add(new Player("BluffMaster😎", true, 1000));
        players.add(new Player("DealerDroid 🤖", true, 1000));
        players.add(new Player("RoyalFlushAI 😈", true, 1000));
        players.add(new Player("ChipCrusher 🤩", true, 1000));

        // Marca el primer jugador (índice dealerIndex=0) como dealer
        players.get(dealerIndex).setDealer(true);
    }

    // Prepara todo para comenzar una nueva ronda de juego
    private void startNewRound() {
        table.resetDeck(); // vuelve a barajar el mazo
        bets.resetBets();  // resetea las apuestas actuales
        bets.resetPot();   // resetea el pozo (dinero acumulado)
        // Resetea el estado de cada jugador para la nueva ronda (ej. no folded, reset apuestas)
        for (Player player : players) {
            player.resetForNewRound();
        }
    }

    // Reparte las dos cartas iniciales a cada jugador
    private void dealInitialCards() {
        table.dealHands(players); // la mesa reparte cartas a cada jugador
        // Muestra las cartas del jugador humano (el primero en la lista)
        System.out.println("\nTus cartas: " + players.get(0).getHand());
    }

    // Coloca las apuestas obligatorias de small blind y big blind
    private void placeBlinds() {
        // Calcula posiciones relativas al dealer para small y big blind
        int smallBlindPos = (dealerIndex + 1) % players.size();
        int bigBlindPos = (dealerIndex + 2) % players.size();
        Player smallBlindPlayer = players.get(smallBlindPos);
        Player bigBlindPlayer = players.get(bigBlindPos);
        // Realiza las apuestas ciegas en PokerBets
        bets.placeBlindBets(smallBlindPlayer, bigBlindPlayer);
    }

    // Controla las diferentes rondas de apuestas y el reparto progresivo de cartas comunitarias
    private void playRounds() {
        // Pre-Flop (antes de que se muestren cartas comunitarias)
        if (bettingRound("Pre-Flop")) {
            table.dealFlop(); // reparte las primeras 3 cartas comunitarias
            printTableStatus();

            // Flop (después de mostrar las 3 cartas comunitarias)
            if (bettingRound("Flop")) {
                table.dealTurn(); // reparte la cuarta carta comunitaria
                printTableStatus();

                // Turn (después de la cuarta carta comunitaria)
                if (bettingRound("Turn")) {
                    table.dealRiver(); // reparte la quinta y última carta comunitaria
                    printTableStatus();

                    // River (última ronda de apuestas)
                    bettingRound("River");
                }
            }
        }
    }

    // Controla la lógica de una ronda de apuestas en una etapa del juego
    private boolean bettingRound(String stage) {
        System.out.println("\n--- Ronda de apuestas: " + stage + " ---");
        List<Player> activePlayers = getActivePlayers(); // jugadores que no se retiraron

        // Si queda 1 o ningún jugador activo, no continúa la ronda
        if (activePlayers.size() <= 1) {
            return false;
        }

        // Encuentra el primer jugador que debe apostar
        int currentPos = findFirstBettingPosition();
        while (true) {
            Player currentPlayer = players.get(currentPos);

            // Si el jugador se retiró (fold), salta al siguiente
            if (currentPlayer.hasFolded()) {
                currentPos = (currentPos + 1) % players.size();
                continue;
            }

            // Si es humano, procesa su turno con interacción
            if (!currentPlayer.isBot()) {
                processHumanTurn(currentPlayer);
            } else {
                // Si es bot, decide su acción automáticamente
                processBotTurn(currentPlayer);
            }

            // Revisa si la ronda de apuestas está completa (ej. todos igualaron o fold)
            if (checkRoundComplete(activePlayers)) {
                break;
            }

            // Mueve al siguiente jugador
            currentPos = (currentPos + 1) % players.size();

            // Si se completó una vuelta y todas las apuestas son iguales, termina ronda
            if (currentPos == findFirstBettingPosition()) {
                if (checkAllBetsEqual(activePlayers)) {
                    break;
                }
            }
        }

        // Resetea apuestas actuales para la siguiente ronda
        bets.resetBets();

        // Retorna true si queda más de un jugador activo para seguir jugando
        return getActivePlayers().size() > 1;
    }

    // Procesa el turno de un jugador humano mostrando opciones e interactuando con la entrada
    private void processHumanTurn(Player player) {
    // Muestra información relevante para decidir
    System.out.println("\nTu turno! Tus cartas: " + player.getHand());
    System.out.println("Apuesta actual: " + bets.getCurrentBet());
    System.out.println("Tu apuesta actual: " + player.getBet());
    System.out.println("Tus fichas: " + player.getChips());
    System.out.println("Pozo total: " + bets.getPot());

    // Calcula cuánto falta para igualar la apuesta actual
    int amountToCall = bets.getCurrentBet() - player.getBet();

    // Muestra opciones disponibles al jugador según la situación
    System.out.println("\nOpciones:");
    System.out.println("1. Fold (Retirarse)");

    if (amountToCall > 0) {
        if (amountToCall >= player.getChips()) {
            System.out.println("2. All-in (" + player.getChips() + " fichas)");
        } else {
            System.out.println("2. Call (Igualar: " + amountToCall + " fichas)");
        }
    } else {
        System.out.println("2. Check (Pasar)");
    }

    System.out.println("3. Raise (Subir apuesta)");

    // Lee la opción elegida por el jugador (entre 1 y 3)
    int choice = getUserInput(1, 3);

    // Ejecuta la acción elegida
    switch (choice) {
        case 1: // Fold: se retira de la mano
            player.fold();
            System.out.println("Te has retirado de esta mano.");
            break;

        case 2: // Call o Check según corresponda
            if (amountToCall > 0) {
                if (amountToCall >= player.getChips()) {
                    int allInAmount = bets.allIn(player);
                    System.out.println("Vas ALL-IN con " + allInAmount + " fichas!");
                } else {
                    int callAmount = bets.call(player);
                    if (callAmount > 0) {
                        System.out.println("Igualas la apuesta con " + callAmount + " fichas.");
                    } else {
                        System.out.println("Error al igualar la apuesta.");
                    }
                }
            } else {
                System.out.println("Pasas.");
            }
            break;

        case 3: // Raise: sube la apuesta con un monto ingresado
            System.out.println("¿Cuánto quieres subir la apuesta?");
            int minRaise = bets.getCurrentBet() + bets.getBigBlind(); // mínimo que puede subir
            int maxRaise = player.getChips() + player.getBet();       // máximo que puede apostar

            System.out.println("Mínimo: " + minRaise + ", Máximo: " + maxRaise);

            int raiseAmount = getUserInput(minRaise, maxRaise);

            boolean success = bets.raise(player, raiseAmount - player.getBet());
            if (success) {
                System.out.println("Subes la apuesta a " + raiseAmount + " fichas.");
            } else {
                System.out.println("No tienes suficientes fichas para subir esa cantidad.");
            }
            break;
    }
}


    // Procesa el turno de un bot evaluando su mano y decidiendo la acción
    private void processBotTurn(Player bot) {
        System.out.println("Turno del bot: " + bot.getName());
        System.out.println("Cartas del bot: " + bot.getHand());
        System.out.println("Cartas comunitarias: " + table.getCommunityCards());

        int currentBet = bets.getCurrentBet();
        int callAmount = currentBet - bot.getBet();

        // Combina cartas del bot con cartas comunitarias para evaluar la mano completa
        List<Card> fullHand = new ArrayList<>(bot.getHand());
        fullHand.addAll(table.getCommunityCards());
        PokerResult handResult = evaluator.evaluateHand(fullHand);

        // Considera "fuerte" la mano si es al menos un trío (puede ajustarse)
        boolean strongHand = handResult.hand.ordinal() <= Hands.THREE_OF_A_KIND.ordinal();

        // Decide la acción basada en la fuerza de la mano y las fichas disponibles
        if (strongHand) {
            if (callAmount > 0 && bot.getChips() > callAmount) {
                bets.call(bot);
                System.out.println(bot.getName() + " hace CALL.");
            } else if (bot.getChips() > currentBet * 2) {
                int raiseAmount = currentBet + 10;
                if (bets.raise(bot, raiseAmount - currentBet)) {
                    System.out.println(bot.getName() + " hace RAISE a " + raiseAmount + ".");
                } else {
                    bets.call(bot);
                    System.out.println(bot.getName() + " hace CALL (insuficientes fichas para subir).");
                }
            } else {
                bets.check(bot);
                System.out.println(bot.getName() + " hace CHECK.");
            }
        } else {
            // Si la mano es débil, chequea si no hay apuesta o se retira
            if (callAmount == 0) {
                bets.check(bot);
                System.out.println(bot.getName() + " hace CHECK.");
            } else {
                bot.fold();
                System.out.println(bot.getName() + " hace FOLD.");
            }
        }
    }


    // Obtener la lista de jugadores activos: no hayan hecho fold y tengan fichas (>0)
private List<Player> getActivePlayers() {
    List<Player> active = new ArrayList<>();
    for (Player p : players) {
        if (!p.hasFolded() && p.getChips() > 0) {
            active.add(p);
        }
    }
    return active;
}

// Encontrar la posición del primer jugador que comienza a apostar en la ronda
// Es el jugador después del Big Blind (dealerIndex + 3)
private int findFirstBettingPosition() {
    return (dealerIndex + 3) % players.size();
}

// Verificar si la ronda está completa: si queda 1 o menos jugadores activos
private boolean checkRoundComplete(List<Player> activePlayers) {
    return activePlayers.size() <= 1;
}

// Verificar si todas las apuestas de los jugadores activos son iguales
// Si algún jugador activo tiene apuesta menor que la actual, devuelve false
private boolean checkAllBetsEqual(List<Player> activePlayers) {
    int targetBet = bets.getCurrentBet();
    for (Player p : activePlayers) {
        if (p.getBet() < targetBet && p.getChips() > 0 && !p.hasFolded()) {
            return false;
        }
    }
    return true;
}

// Determinar el ganador de la ronda
private void determineWinner() {
    List<Player> activePlayers = getActivePlayers();

    if (activePlayers.isEmpty()) {
        System.out.println("Error: No hay jugadores activos.");
        return;
    }

    // Si sólo queda un jugador activo, es el ganador automáticamente
    if (activePlayers.size() == 1) {
        Player winner = activePlayers.get(0);
        bets.awardPotToWinner(winner); // Se le otorga el pozo
        System.out.println("El ganador es " + winner.getName());
        return;
    }

    // Evaluación final de manos cuando hay más de un jugador activo
    System.out.println("\n--- Evaluación final de manos ---");
    System.out.println("Cartas en la mesa: " + table.getTableCards());

    // Mostrar las cartas de cada jugador activo
    for (Player p : activePlayers) {
        System.out.println(p.getName() + ": " + p.getHand());
    }

    // Preparar lista de manos para el evaluador
    List<List<Card>> playerHands = new ArrayList<>();
    for (Player p : activePlayers) {
        playerHands.add(p.getHand());
    }

    // Evaluar cuál mano es ganadora
    int winnerIndex = evaluator.returnWinner(playerHands, table.getTableCards());
    if (winnerIndex >= 0 && winnerIndex < activePlayers.size()) {
        Player winner = activePlayers.get(winnerIndex);
        bets.awardPotToWinner(winner); // Otorgar pozo al ganador
        System.out.println("El ganador es " + winner.getName());
    } else {
        // En caso de empate o error, se divide el pozo entre los jugadores activos
        System.out.println("Error al determinar ganador. Se divide el pozo.");
        int splitAmount = bets.getPot() / activePlayers.size();
        for (Player p : activePlayers) {
            p.winChips(splitAmount);
            System.out.println(p.getName() + " recibe " + splitAmount + " fichas.");
        }
    }
}

// Imprimir el estado actual de la mesa y jugadores
private void printTableStatus() {
    System.out.println("\n-------------------- DEALER --------------------");
    System.out.println("🃏 Mesa de Poker 🃏\n");

    // Imprimir el dealer y jugadores a la izquierda
    printPlayer(players.get(dealerIndex));
    System.out.println(); // Salto de línea para separar visualmente
    printPlayer(players.get((dealerIndex + 1) % players.size()));

    // Imprimir jugador a la derecha del dealer con espacio delante
    System.out.print("\n                                    ");
    printPlayer(players.get((dealerIndex + 2) % players.size()));

    // Mostrar cartas en la mesa y el pozo
    System.out.println("\n---------------------------------------------------");
    System.out.println("|                                                 |");
    System.out.println("|                Cartas en la Mesa:               |");

    // Mostrar cartas comunitarias si existen, sino indica mesa vacía
    if (!table.getTableCards().isEmpty()) {
        System.out.println("|                " + table.getTableCards() + "             |");
    } else {
        System.out.println("|                    (Mesa Vacía)                  |");
    }

    // Mostrar el pozo actual
    System.out.println("|                      🟡 POZO: " + bets.getPot() + " 🟡                 |");
    System.out.println("|                                                 |");
    System.out.println("---------------------------------------------------\n");

    // Imprimir jugadores restantes (fila inferior)
    printPlayer(players.get((dealerIndex + 3) % players.size()));
    System.out.print("           "); // Espaciado entre jugadores
    printPlayer(players.get((dealerIndex + 4) % players.size()));
    System.out.print("           "); // Espaciado entre jugadores
    printPlayer(players.get((dealerIndex + 5) % players.size()));
    System.out.println(); // Salto de línea final
}

// Imprimir la información de un jugador individual
private void printPlayer(Player player) {
    String name = String.format("%-10s", player.getName()); // Nombre con ancho fijo
    String dealerMark = player.isDealer() ? "🔴 Dealer" : ""; // Marca si es dealer
    String status = player.hasFolded() ? "Fold" : ""; // Estado fold si aplica

    System.out.print("[" + name + "]\n"); // Imprimir nombre
    System.out.print("Fichas: 💰 " + player.getChips() + "  | 🟢 " + player.getBet()); // Fichas y apuesta actual

    // Imprimir marca de dealer si es dealer
    if (!dealerMark.isEmpty()) {
        System.out.print("\n" + dealerMark);
    }

    // Imprimir estado fold si el jugador se retiró
    if (!status.isEmpty()) {
        System.out.print(" " + status);
    }
}

// Cambiar el dealer al siguiente jugador con fichas
void moveDealer() {
    // Quitar la marca de dealer al jugador actual
    players.get(dealerIndex).setDealer(false);

    // Buscar al siguiente jugador con fichas para asignarle el dealer
    int nextDealerIndex = dealerIndex;
    do {
        nextDealerIndex = (nextDealerIndex + 1) % players.size();
        if (players.get(nextDealerIndex).getChips() > 0) {
            break; // Encontró un jugador con fichas
        }
    } while (nextDealerIndex != dealerIndex); // Evita ciclo infinito si nadie tiene fichas

    // Asignar dealer al nuevo jugador encontrado
    dealerIndex = nextDealerIndex;
    players.get(dealerIndex).setDealer(true);
    System.out.println("\nEl nuevo dealer es: " + players.get(dealerIndex).getName());
}

// Preguntar al usuario si quiere jugar otra mano
private boolean askToPlayAgain() {
    System.out.println("\n¿Quieres jugar otra mano? (1-Sí / 2-No)");
    int choice = getUserInput(1, 2);
    return choice == 1;
}

// Obtener una entrada válida del usuario dentro de un rango específico
private int getUserInput(int min, int max) {
    int choice = -1;
    while (choice < min || choice > max) {
        System.out.print("Ingresa tu elección (" + min + "-" + max + "): ");
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            if (choice >= min && choice <= max) {
                scanner.nextLine(); // Consumir el salto de línea pendiente
                break; // Entrada válida, salir del ciclo
            } else {
                System.out.println("Entrada inválida. Por favor, ingresa un número entre " + min + " y " + max + ".");
            }
        } else {
            System.out.println("Entrada inválida. Por favor, ingresa un número.");
            scanner.next(); // Descartar entrada inválida
        }
    }
    return choice;
}

// Terminar el juego cerrando el Scanner y saludando al usuario
private void endGame() {
    System.out.println("¡Gracias por jugar!");
    scanner.close();
}
}