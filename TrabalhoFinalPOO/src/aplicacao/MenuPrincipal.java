package aplicacao;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private final ACMETech sistema;
    private final JList<String> listaOpcoes;
    private final JButton botaoExecutar;
    private final JButton botaoFechar;

    private final String[] OPCOES = {
            "1 - Cadastrar fornecedor",
            "2 - Cadastrar tecnologia",
            "3 - Cadastrar comprador",
            "4 - Cadastrar venda",
            "5 - Relatório de tecnologias",
            "6 - Relatório de fornecedores",
            "7 - Relatório de compradores",
            "8 - Relatório de vendas",
            "9 - Remover venda",
            "10 - Alterar comprador",
            "11 - Consultar maior",
            "12 - Salvar dados",
            "13 - Carregar dados",
            "14 - Finalizar sistema"
    };

    public MenuPrincipal(ACMETech sistema) {
        super("ACME Tech - Menu Principal");
        this.sistema = sistema;

        listaOpcoes = new JList<>(OPCOES);
        listaOpcoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaOpcoes.setSelectedIndex(0);

        botaoExecutar = new JButton("Executar opção");
        botaoFechar   = new JButton("Fechar");

        JScrollPane sp = new JScrollPane(listaOpcoes);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(botaoExecutar);
        botoes.add(botaoFechar);

        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        root.add(new JLabel("Selecione uma opção:"), BorderLayout.NORTH);
        root.add(sp, BorderLayout.CENTER);
        root.add(botoes, BorderLayout.SOUTH);

        setContentPane(root);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        botaoExecutar.addActionListener(e -> executarOpcaoSelecionada());
        botaoFechar.addActionListener(e -> dispose());
    }

    private void executarOpcaoSelecionada() {
        int idx = listaOpcoes.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma opção.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcao = idx + 1;

        switch (opcao) {
            case 1 -> new JanelaCadastrarFornecedor(sistema).setVisible(true);
            case 2 -> new JanelaCadastrarTecnologia(sistema).setVisible(true);
            case 3 -> new JanelaCadastrarComprador(sistema).setVisible(true);
            case 4 -> new JanelaCadastrarVenda(sistema).setVisible(true);
            case 5 -> new JanelaRelatorioTecnologias(sistema).setVisible(true);
            case 6 -> new JanelaRelatorioFornecedores(sistema).setVisible(true);
            case 7 -> new JanelaRelatorioCompradores(sistema).setVisible(true);
            case 8 -> new JanelaRelatorioVendas(sistema).setVisible(true);
            case 9 -> new JanelaRemoverVenda(sistema).setVisible(true);
            case 10 -> new JanelaAlterarComprador(sistema).setVisible(true);
            case 11 -> new JanelaConsultaMaior(sistema).setVisible(true);
            case 12 -> new JanelaSalvarDados(sistema).setVisible(true);
            case 13 -> new JanelaCarregarDados(sistema).setVisible(true);
            case 14 -> finalizarSistema();
            default -> JOptionPane.showMessageDialog(this,
                    "Opção ainda não implementada.",
                    "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void finalizarSistema() {
        int resp = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente finalizar o sistema?",
                "Confirmar saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}

