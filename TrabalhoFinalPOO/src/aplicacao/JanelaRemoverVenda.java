package aplicacao;

import entidades.Venda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Collection;

public class JanelaRemoverVenda extends JFrame {

    private final ACMETech sistema;

    private final JTextField txtNum = new JTextField(10);
    private final JTextArea msg = new JTextArea(10, 50);
    private final JButton bRemover = new JButton("Remover");
    private final JButton bListar  = new JButton("Listar vendas");
    private final JButton bFechar  = new JButton("Fechar");

    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public JanelaRemoverVenda(ACMETech sistema) {
        super("Remover Venda");
        this.sistema = sistema;

        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Número da venda:"));
        topo.add(txtNum);
        topo.add(bRemover);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(bListar);
        botoes.add(bFechar);

        JScrollPane sp = new JScrollPane(msg);

        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        root.add(topo, BorderLayout.NORTH);
        root.add(sp, BorderLayout.CENTER);
        root.add(botoes, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        bRemover.addActionListener(this::onRemover);
        bListar.addActionListener(e -> listarVendas());
        bFechar.addActionListener(e -> dispose());
    }

    private void onRemover(ActionEvent e) {
        msg.setText("");
        try {
            long num = Long.parseLong(txtNum.getText().trim());
            String resultado = sistema.removerVenda(num);
            msg.append(resultado + "\n");
        } catch (NumberFormatException ex) {
            msg.append("Erro: número deve ser inteiro (long).\n");
        } catch (Exception ex) {
            msg.append("Erro inesperado: " + ex.getMessage() + "\n");
        }
    }

    private void listarVendas() {
        msg.setText("");
        Collection<Venda> todas = sistema.getVendasOrdenadas();
        if (todas.isEmpty()) {
            msg.append("(nenhuma venda cadastrada)\n");
            return;
        }

        for (Venda v : todas) {
            msg.append("num=" + v.getNum()
                    + "; data=" + df.format(v.getData())
                    + "; valorFinal=" + v.getValorFinal());
            if (v.getComprador() != null) {
                msg.append("; comprador=" + v.getComprador().getCod()
                        + " - " + v.getComprador().getNome());
            }
            if (v.getTecnologia() != null) {
                msg.append("; tecnologia=" + v.getTecnologia().getId()
                        + " - " + v.getTecnologia().getModelo());
            }
            msg.append("\n");
        }
    }
}

