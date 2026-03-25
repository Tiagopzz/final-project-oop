package aplicacao;

import entidades.Comprador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JanelaAlterarComprador extends JFrame {

    private final ACMETech sistema;

    private final JTextField txtCod     = new JTextField(10);
    private final JTextField txtNome    = new JTextField(25);
    private final JTextField txtPais    = new JTextField(20);
    private final JTextField txtEmail   = new JTextField(25);

    private final JButton bBuscar  = new JButton("Buscar");
    private final JButton bSalvar  = new JButton("Salvar alterações");
    private final JButton bFechar  = new JButton("Fechar");

    private final JTextArea msg = new JTextArea(8, 52);

    public JanelaAlterarComprador(ACMETech sistema) {
        super("Alterar Comprador");
        this.sistema = sistema;

        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.WEST;

        int y = 0;

        c.gridx=0; c.gridy=y; form.add(new JLabel("Código do comprador:"), c);
        c.gridx=1; form.add(txtCod, c);
        c.gridx=2; form.add(bBuscar, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Nome:"), c);
        c.gridx=1; form.add(txtNome, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("País:"), c);
        c.gridx=1; form.add(txtPais, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Email:"), c);
        c.gridx=1; form.add(txtEmail, c);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(bSalvar);
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

        bBuscar.addActionListener(this::onBuscar);
        bSalvar.addActionListener(this::onSalvar);
        bFechar.addActionListener(e -> dispose());
    }

    private void onBuscar(ActionEvent e) {
        msg.setText("");

        try {
            long cod = Long.parseLong(txtCod.getText().trim());
            Comprador c = sistema.getCompradoresOrdenados()
                    .stream()
                    .filter(x -> x.getCod() == cod)
                    .findFirst()
                    .orElse(null);

            if (c == null) {
                msg.append("Erro: comprador não encontrado.\n");
                return;
            }

            txtNome.setText(c.getNome());
            txtPais.setText(c.getPais());
            txtEmail.setText(c.getEmail());

            msg.append("Comprador encontrado. Altere os dados abaixo.\n");

        } catch (NumberFormatException ex) {
            msg.append("Erro: código deve ser inteiro.\n");
        }
    }

    private void onSalvar(ActionEvent e) {
        msg.setText("");

        try {
            long cod = Long.parseLong(txtCod.getText().trim());
            String nome = txtNome.getText().trim();
            String pais = txtPais.getText().trim();
            String email = txtEmail.getText().trim();

            String resultado = sistema.alterarComprador(cod, nome, pais, email);
            msg.append(resultado + "\n");

        } catch (NumberFormatException ex) {
            msg.append("Erro: código deve ser inteiro.\n");
        } catch (Exception ex) {
            msg.append("Erro inesperado: " + ex.getMessage() + "\n");
        }
    }
}

