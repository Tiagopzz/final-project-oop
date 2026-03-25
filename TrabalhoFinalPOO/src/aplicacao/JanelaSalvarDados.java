package aplicacao;

import javax.swing.*;
import java.awt.*;

public class JanelaSalvarDados extends JFrame {

    private final ACMETech sistema;

    private final JTextField txtNome = new JTextField(20);
    private final JTextArea msg = new JTextArea(5, 40);

    public JanelaSalvarDados(ACMETech sistema) {
        super("Salvar Dados (JSON)");
        this.sistema = sistema;

        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        JButton bSalvar = new JButton("Salvar");
        JButton bFechar = new JButton("Fechar");

        JPanel form = new JPanel(new FlowLayout());
        form.add(new JLabel("Nome do arquivo (sem extensão):"));
        form.add(txtNome);
        form.add(bSalvar);

        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        root.add(form, BorderLayout.NORTH);
        root.add(new JScrollPane(msg), BorderLayout.CENTER);
        root.add(bFechar, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        bSalvar.addActionListener(e -> onSalvar());
        bFechar.addActionListener(e -> dispose());
    }

    private void onSalvar() {
        msg.setText("");
        String nome = txtNome.getText().trim();

        if (nome.isBlank()) {
            msg.append("Erro: informe o nome do arquivo.\n");
            return;
        }

        String resultado = sistema.salvarDados(nome);
        msg.append(resultado + "\n");
    }
}

