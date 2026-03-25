package aplicacao;

import entidades.Comprador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

public class JanelaCadastrarComprador extends JFrame {

    private final ACMETech sistema;

    private final JTextField txtCod   = new JTextField(10);
    private final JTextField txtNome  = new JTextField(25);
    private final JTextField txtPais  = new JTextField(20);
    private final JTextField txtEmail = new JTextField(25);

    private final JTextArea msg = new JTextArea(8, 52);

    public JanelaCadastrarComprador(ACMETech sistema) {
        super("Cadastrar Comprador");
        this.sistema = sistema;

        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.WEST;

        int y = 0;
        c.gridx = 0; c.gridy = y; form.add(new JLabel("Código:"), c);
        c.gridx = 1; form.add(txtCod, c);

        y++; c.gridx = 0; c.gridy = y; form.add(new JLabel("Nome:"), c);
        c.gridx = 1; form.add(txtNome, c);

        y++; c.gridx = 0; c.gridy = y; form.add(new JLabel("País:"), c);
        c.gridx = 1; form.add(txtPais, c);

        y++; c.gridx = 0; c.gridy = y; form.add(new JLabel("Email:"), c);
        c.gridx = 1; form.add(txtEmail, c);

        JButton bCadastrar = new JButton("Cadastrar");
        JButton bLimpar    = new JButton("Limpar");
        JButton bMostrar   = new JButton("Mostrar todos");
        JButton bFechar    = new JButton("Fechar");

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(bCadastrar);
        botoes.add(bLimpar);
        botoes.add(bMostrar);
        botoes.add(bFechar);

        JScrollPane sp = new JScrollPane(msg);

        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        root.add(form, BorderLayout.NORTH);
        root.add(botoes, BorderLayout.CENTER);
        root.add(sp, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        bCadastrar.addActionListener(this::onCadastrar);
        bLimpar.addActionListener(e -> limparCampos());
        bMostrar.addActionListener(e -> onMostrarTodos());
        bFechar.addActionListener(e -> dispose());
    }

    private void limparCampos() {
        txtCod.setText("");
        txtNome.setText("");
        txtPais.setText("");
        txtEmail.setText("");
        msg.setText("");
        txtCod.requestFocus();
    }

    private void onCadastrar(ActionEvent e) {
        msg.setText("");

        try {
            long cod = Long.parseLong(txtCod.getText().trim());
            String nome = txtNome.getText().trim();
            String pais = txtPais.getText().trim();
            String email = txtEmail.getText().trim();

            if (nome.isEmpty()) {
                msg.append("Erro: nome não pode estar vazio.\n");
                return;
            }
            if (pais.isEmpty()) {
                msg.append("Erro: informe o país.\n");
                return;
            }
            if (email.isEmpty()) {
                msg.append("Erro: informe o email.\n");
                return;
            }

            String resultado = sistema.cadastrarComprador(cod, nome, pais, email);
            msg.append(resultado + "\n");

        } catch (NumberFormatException ex) {
            msg.append("Erro: código deve ser número inteiro.\n");
        } catch (Exception ex) {
            msg.append("Erro inesperado: " + ex.getMessage() + "\n");
        }
    }

    private void onMostrarTodos() {
        msg.setText("");

        Collection<Comprador> lista = sistema.getCompradoresOrdenados();

        if (lista.isEmpty()) {
            msg.append("(nenhum comprador cadastrado)\n");
            return;
        }

        for (Comprador c : lista) {
            msg.append(c.geraDescricao());
            msg.append("\n");
        }
    }
}

