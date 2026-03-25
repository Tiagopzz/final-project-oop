package aplicacao;

import javax.swing.*;
import java.awt.*;

public class JanelaRelatorioTecnologias extends JFrame {

    private final ACMETech sistema;
    private final JTextArea txtRelatorio = new JTextArea(20, 60);
    private final JButton bAtualizar = new JButton("Atualizar");
    private final JButton bFechar    = new JButton("Fechar");

    public JanelaRelatorioTecnologias(ACMETech sistema) {
        super("Relatório de Tecnologias");
        this.sistema = sistema;

        txtRelatorio.setEditable(false);
        txtRelatorio.setLineWrap(true);
        txtRelatorio.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(txtRelatorio);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(bAtualizar);
        botoes.add(bFechar);

        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        root.add(sp, BorderLayout.CENTER);
        root.add(botoes, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        // ações
        bAtualizar.addActionListener(e -> carregarRelatorio());
        bFechar.addActionListener(e -> dispose());

        // já carrega na abertura
        carregarRelatorio();
    }

    private void carregarRelatorio() {
        String rel = sistema.relatorioTecnologias();
        txtRelatorio.setText(rel);
    }
}
