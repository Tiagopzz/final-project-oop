package aplicacao;

import javax.swing.*;
import java.awt.*;

public class JanelaRelatorioVendas extends JFrame {

    private final ACMETech sistema;
    private final JTextArea txtRelatorio = new JTextArea(20, 70);
    private final JButton bAtualizar = new JButton("Atualizar");
    private final JButton bFechar    = new JButton("Fechar");

    public JanelaRelatorioVendas(ACMETech sistema) {
        super("Relatório de Vendas");
        this.sistema = sistema;

        txtRelatorio.setEditable(false);
        txtRelatorio.setLineWrap(true);
        txtRelatorio.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(txtRelatorio);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(bAtualizar);
        botoes.add(bFechar);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        root.add(sp, BorderLayout.CENTER);
        root.add(botoes, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        bAtualizar.addActionListener(e -> carregar());
        bFechar.addActionListener(e -> dispose());

        carregar();
    }

    private void carregar() {
        txtRelatorio.setText(sistema.relatorioVendas());
    }
}
