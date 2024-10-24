import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

class Pedido {
    private String id;
    private String cliente;
    private boolean vip;
    private List<String> itens;

    public Pedido(String id, String cliente, boolean vip, List<String> itens) {
        this.id = id;
        this.cliente = cliente;
        this.vip = vip;
        this.itens = itens;
    }

    public String getId() {
        return id;
    }

    public boolean isVip() {
        return vip;
    }

    public List<String> getItens() {
        return itens;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", cliente='" + cliente + '\'' +
                ", vip=" + vip +
                ", itens=" + itens +
                '}';
    }
}

class PedidoManager {
    private Queue<Pedido> pedidosNormais;
    private PriorityQueue<Pedido> pedidosVIP;

    public PedidoManager() {
        pedidosNormais = new LinkedList<>();
        pedidosVIP = new PriorityQueue<>((p1, p2) -> p1.getId().compareTo(p2.getId()));
    }

    // Adicionar um novo pedido
    public void adicionarPedido(Pedido pedido) {
        if (pedido.isVip()) {
            pedidosVIP.add(pedido);
        } else {
            pedidosNormais.add(pedido);
        }
        System.out.println("Pedido adicionado: " + pedido);
    }

    // Processar o próximo pedido
    public Pedido processarProximoPedido() {
        Pedido proximoPedido;
        if (!pedidosVIP.isEmpty()) {
            proximoPedido = pedidosVIP.poll();
        } else {
            proximoPedido = pedidosNormais.poll();
        }
        if (proximoPedido != null) {
            System.out.println("Processando pedido: " + proximoPedido);
        } else {
            System.out.println("Nenhum pedido na fila para processar.");
        }
        return proximoPedido;
    }

    // Visualizar pedidos em espera sem removê-los
    public void visualizarPedidos() {
        System.out.println("Pedidos VIP em espera:");
        for (Pedido pedido : pedidosVIP) {
            System.out.println(pedido);
        }

        System.out.println("Pedidos Normais em espera:");
        for (Pedido pedido : pedidosNormais) {
            System.out.println(pedido);
        }
    }

    // Remover um pedido cancelado
    public boolean removerPedidoCancelado(String pedidoId) {
        boolean removido = pedidosVIP.removeIf(p -> p.getId().equals(pedidoId));
        if (!removido) {
            removido = pedidosNormais.removeIf(p -> p.getId().equals(pedidoId));
        }
        System.out.println(removido ? "Pedido " + pedidoId + " removido com sucesso." : "Pedido " + pedidoId + " não encontrado.");
        return removido;
    }

    // Atualizar prioridade de um pedido
    public void atualizarPrioridadePedido(String pedidoId, boolean vip) {
        Pedido pedidoEncontrado = null;

        // Procurar e remover o pedido da fila VIP ou Normal
        if (vip) {
            for (Pedido pedido : pedidosNormais) {
                if (pedido.getId().equals(pedidoId)) {
                    pedidoEncontrado = pedido;
                    pedidosNormais.remove(pedido);
                    break;
                }
            }
        } else {
            for (Pedido pedido : pedidosVIP) {
                if (pedido.getId().equals(pedidoId)) {
                    pedidoEncontrado = pedido;
                    pedidosVIP.remove(pedido);
                    break;
                }
            }
        }

        // Atualizar a prioridade e adicionar à fila correta
        if (pedidoEncontrado != null) {
            Pedido pedidoAtualizado = new Pedido(pedidoId, pedidoEncontrado.getId(), vip, pedidoEncontrado.getItens());
            adicionarPedido(pedidoAtualizado);
            System.out.println("Pedido " + pedidoId + " atualizado para VIP: " + vip);
        } else {
            System.out.println("Pedido " + pedidoId + " não encontrado para atualização.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        PedidoManager manager = new PedidoManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Sistema de Gerenciamento de Cafeteria ---");
            System.out.println("1. Adicionar um novo pedido");
            System.out.println("2. Processar o próximo pedido");
            System.out.println("3. Visualizar pedidos em espera");
            System.out.println("4. Remover um pedido cancelado");
            System.out.println("5. Atualizar a prioridade de um pedido");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");

            // Verificar se a entrada é um número
            if (!scanner.hasNextInt()) {
                System.out.println("Por favor, digite um número válido.");
                scanner.next(); // Limpar a entrada inválida
                continue;
            }

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    System.out.print("Digite o ID do pedido: ");
                    String id = scanner.nextLine();
                    System.out.print("Digite o nome do cliente: ");
                    String cliente = scanner.nextLine();
                    System.out.print("O cliente é VIP? (s/n): ");
                    boolean vip = scanner.nextLine().equalsIgnoreCase("s");

                    List<String> itens = new ArrayList<>();
                    System.out.println("Digite os itens do pedido (um por linha). Digite 'fim' para encerrar.");
                    while (true) {
                        String item = scanner.nextLine();
                        if (item.equalsIgnoreCase("fim")) {
                            break;
                        }
                        itens.add(item);
                    }

                    manager.adicionarPedido(new Pedido(id, cliente, vip, itens));
                    break;
                case 2:
                    manager.processarProximoPedido();
                    break;
                case 3:
                    manager.visualizarPedidos();
                    break;
                case 4:
                    System.out.print("Digite o ID do pedido a ser removido: ");
                    String idParaRemover = scanner.nextLine();
                    manager.removerPedidoCancelado(idParaRemover);
                    break;
                case 5:
                    System.out.print("Digite o ID do pedido a ser atualizado: ");
                    String idParaAtualizar = scanner.nextLine();
                    System.out.print("Atualizar para VIP? (s/n): ");
                    boolean atualizarParaVip = scanner.nextLine().equalsIgnoreCase("s");
                    manager.atualizarPrioridadePedido(idParaAtualizar, atualizarParaVip);
                    break;
                case 6:
                    running = false;
                    System.out.println("Saindo do sistema. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }
}
