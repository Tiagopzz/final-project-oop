package aplicacao;

import javax.swing.*;
import java.awt.*;

public class JanelaCarregarDados extends JFrame {

    private final ACMETech sistema;

    private final JTextField txtNome = new JTextField(20);
    private final JTextArea msg = new JTextArea(5, 40);

    public JanelaCarregarDados(ACMETech sistema) {
        super("Carregar Dados (JSON)");
        this.sistema = sistema;

        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        JButton bCarregar = new JButton("Carregar");
        JButton bFechar = new JButton("Fechar");

        JPanel form = new JPanel(new FlowLayout());
        form.add(new JLabel("Nome do arquivo (sem extensão):"));
        form.add(txtNome);
        form.add(bCarregar);

        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        root.add(form, BorderLayout.NORTH);
        root.add(new JScrollPane(msg), BorderLayout.CENTER);
        root.add(bFechar, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        bCarregar.addActionListener(e -> onCarregar());
        bFechar.addActionListener(e -> dispose());
    }

    private void onCarregar() {
        msg.setText("");
        String nome = txtNome.getText().trim();

        if (nome.isBlank()) {
            msg.append("Erro: informe o nome do arquivo.\n");
            return;
        }

        String resultado = sistema.carregarDados(nome);
        msg.append(resultado + "\n");
    }
}

