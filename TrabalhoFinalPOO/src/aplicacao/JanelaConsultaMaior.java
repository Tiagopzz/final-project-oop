package aplicacao;

import javax.swing.*;
import java.awt.*;

public class JanelaConsultaMaior extends JFrame {

    private final ACMETech sistema;

    private final JButton bTec   = new JButton("Tecnologia com maior valor");
    private final JButton bForn  = new JButton("Fornecedor com mais tecnologias");
    private final JButton bComp  = new JButton("Comprador com mais vendas");
    private final JButton bVenda = new JButton("Venda com maior valor");
    private final JButton bFechar = new JButton("Fechar");

    private final JTextArea msg = new JTextArea(12, 60);

    public JanelaConsultaMaior(ACMETech sistema) {
        super("Consultas - Maior");
        this.sistema = sistema;

        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        JPanel botoes = new JPanel(new GridLayout(5, 1, 5, 5));
        botoes.add(bTec);
        botoes.add(bForn);
        botoes.add(bComp);
        botoes.add(bVenda);
        botoes.add(bFechar);

        JScrollPane sp = new JScrollPane(msg);

        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        root.add(botoes, BorderLayout.WEST);
        root.add(sp, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        bTec.addActionListener(e -> msg.setText(sistema.consultarTecnologiaMaiorValor()));
        bForn.addActionListener(e -> msg.setText(sistema.consultarFornecedorMaiorNumTecnologias()));
        bComp.addActionListener(e -> msg.setText(sistema.consultarCompradorMaiorNumVendas()));
        bVenda.addActionListener(e -> msg.setText(sistema.consultarVendaMaiorValor()));
        bFechar.addActionListener(e -> dispose());
    }
}
