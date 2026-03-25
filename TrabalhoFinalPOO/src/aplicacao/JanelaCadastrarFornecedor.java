package aplicacao;

import entidades.Area;
import entidades.Fornecedor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class JanelaCadastrarFornecedor extends JFrame {

    private final ACMETech sistema;

    private final JTextField txtCod   = new JTextField(10);
    private final JTextField txtNome  = new JTextField(28);
    private final JTextField txtData  = new JTextField(10);
    private final JComboBox<Area> cbArea = new JComboBox<>(Area.values());
    private final JTextArea msg = new JTextArea(8, 52);

    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public JanelaCadastrarFornecedor(ACMETech sistema) {
        super("Cadastrar Fornecedor");
        this.sistema = sistema;
        df.setLenient(false);

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

        y++; c.gridx = 0; c.gridy = y; form.add(new JLabel("Fundação (dd/MM/yyyy):"), c);
        c.gridx = 1; form.add(txtData, c);

        y++; c.gridx = 0; c.gridy = y; form.add(new JLabel("Área:"), c);
        c.gridx = 1; form.add(cbArea, c);

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

        // Ações dos botões
        bCadastrar.addActionListener(this::onCadastrar);
        bLimpar.addActionListener(e -> {
            txtCod.setText("");
            txtNome.setText("");
            txtData.setText("");
            msg.setText("");
            txtCod.requestFocus();
        });
        bMostrar.addActionListener(e -> onMostrarTodos());
        bFechar.addActionListener(e -> dispose());
    }

    private void onCadastrar(ActionEvent e) {
        msg.setText("");
        try {
            long cod = Long.parseLong(txtCod.getText().trim());
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) {
                msg.append("Erro: informe o nome.\n");
                return;
            }

            Date fundacao;
            try {
                fundacao = df.parse(txtData.getText().trim());
            } catch (ParseException pe) {
                msg.append("Erro: data inválida (use dd/MM/yyyy).\n");
                return;
            }

            Area area = (Area) cbArea.getSelectedItem();

            String resultado = sistema.cadastrarFornecedor(cod, nome, fundacao, area);
            msg.append(resultado);
            msg.append("\n");

        } catch (NumberFormatException nfe) {
            msg.append("Erro: código deve ser número inteiro (long).\n");
        } catch (Exception ex) {
            msg.append("Erro inesperado: " + ex.getMessage() + "\n");
        }
    }

    private void onMostrarTodos() {
        msg.setText("");
        Collection<Fornecedor> todos = sistema.getFornecedoresOrdenados();
        if (todos.isEmpty()) {
            msg.append("(nenhum fornecedor cadastrado)\n");
            return;
        }
        for (Fornecedor f : todos) {
            msg.append(f.geraDescricao());
            msg.append("\n");
        }
    }
}
