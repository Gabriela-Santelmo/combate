package combate;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

class Campeao {
    private String nome;
    private int vida;
    private int ataque;
    private int armadura;

    public Campeao(String nome, int vida, int ataque, int armadura) {
        this.nome = nome;
        this.vida = vida;
        this.ataque = ataque;
        this.armadura = armadura;
    }

    public void receberDano(int dano) {
        int danoRecebido = Math.max(dano - armadura, 1); 
        vida = Math.max(vida - danoRecebido, 0); 
    }

    public String obterStatus() {
        return String.format("%s: %d %s", nome, vida, (vida > 0 ? "de vida" : "(morreu)"));
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    public int obterAtaque() {
        return ataque;
    }

    public int obterVida() {
        return vida;
    }

    public String obterNome() {
        return nome;
    }
}

public class combate {
    private static final int VIDA_MINIMA = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println(" ===========");
        System.out.println("|  COMBATE  |");
        System.out.println(" ===========");

        Campeao campeao1 = criarCampeao(scanner, "primeiro");
        Campeao campeao2 = criarCampeao(scanner, "segundo");

        boolean primeiroATacar = random.nextBoolean();

        int turnos = obterEntradaInteira(scanner, "\nQuantos turnos você deseja executar? ");

        for (int i = 1; i <= turnos && campeao1.estaVivo() && campeao2.estaVivo(); i++) {
            if (campeao1.obterVida() == VIDA_MINIMA && campeao2.obterVida() == VIDA_MINIMA) {
                realizarCombateFinal(campeao1, campeao2, primeiroATacar);
                break;
            }
            if (primeiroATacar) {
                realizarAtaque(campeao1, campeao2);
            } else {
                realizarAtaque(campeao2, campeao1);
            }

            exibirStatusTurno(i, campeao1, campeao2);
        }

        System.out.println(" ==================");
        System.out.println("|  FIM DO COMBATE  |");
        System.out.println(" ==================");
        scanner.close();
    }

    private static void realizarAtaque(Campeao atacante, Campeao defensor) {
        defensor.receberDano(atacante.obterAtaque());
        if (!defensor.estaVivo()) {
            System.out.printf("%s morreu após o ataque de %s.%n", defensor.obterNome(), atacante.obterNome());
        } else {
            atacante.receberDano(defensor.obterAtaque());
        }
    }

    private static void realizarCombateFinal(Campeao campeao1, Campeao campeao2, boolean primeiroATacar) {
        if (primeiroATacar) {
            campeao2.receberDano(campeao1.obterAtaque());
            System.out.printf("%s morreu, e %s sobreviveu com %d de vida.%n", campeao2.obterNome(), campeao1.obterNome(), campeao1.obterVida());
        } else {
            campeao1.receberDano(campeao2.obterAtaque());
            System.out.printf("%s morreu, e %s sobreviveu com %d de vida.%n", campeao1.obterNome(), campeao2.obterNome(), campeao2.obterVida());
        }
    }

    private static void exibirStatusTurno(int turno, Campeao campeao1, Campeao campeao2) {
        System.out.printf("\nResultado do turno %d:%n", turno);
        System.out.println(campeao1.obterStatus());
        System.out.println(campeao2.obterStatus());
        System.out.println();
    }

    private static Campeao criarCampeao(Scanner scanner, String ordem) {
        System.out.println("\nDigite os dados do " + ordem + " campeão:");
        System.out.print("- Nome: ");
        String nome = scanner.nextLine();

        int vida = obterEntradaInteira(scanner, "- Vida inicial: ");
        int ataque = obterEntradaInteira(scanner, "- Ataque: ");
        int armadura = obterEntradaInteira(scanner, "- Armadura: ");

        return new Campeao(nome, vida, ataque, armadura);
    }

    private static int obterEntradaInteira(Scanner scanner, String mensagem) {
        int valor;
        while (true) {
            System.out.print(mensagem);
            try {
                valor = scanner.nextInt();
                scanner.nextLine(); 

                if (valor <= 0 && !mensagem.contains("Armadura")) {
                    System.out.println("\nO valor deve ser maior que zero.");
                    continue;
                } else if (mensagem.contains("Armadura") && valor < 0) {
                    System.out.println("\nO valor da armadura deve ser maior ou igual a zero.");
                    continue;
                }
                return valor; 
            } catch (InputMismatchException e) {
                System.out.println("\nDigite apenas números inteiros.");
                scanner.next(); 
            }
        }
    }
}
