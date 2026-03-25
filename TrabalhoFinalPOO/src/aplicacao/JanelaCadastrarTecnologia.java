package aplicacao;

import entidades.Area;
import entidades.Fornecedor;
import entidades.Tecnologia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

public class JanelaCadastrarTecnologia extends JFrame {

    private final ACMETech sistema;

    private final JTextField txtId  = new JTextField(10);
    private final JTextField txtModelo = new JTextField(20);
    private final JTextField txtDescricao = new JTextField(25);
    private final JTextField txtValorBase = new JTextField(10);
    private final JTextField txtPeso = new JTextField(10);
    private final JTextField txtTemperatura = new JTextField(10);

    private final JComboBox<String> cbFornecedor = new JComboBox<>();
    private final JTextArea msg = new JTextArea(8, 52);

    public JanelaCadastrarTecnologia(ACMETech sistema) {
        super("Cadastrar Tecnologia");
        this.sistema = sistema;

        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        carregarFornecedoresNoCombo();

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.WEST;

        int y = 0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("ID:"), c);
        c.gridx=1; form.add(txtId, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Modelo:"), c);
        c.gridx=1; form.add(txtModelo, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Descrição:"), c);
        c.gridx=1; form.add(txtDescricao, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Valor base:"), c);
        c.gridx=1; form.add(txtValorBase, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Peso:"), c);
        c.gridx=1; form.add(txtPeso, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Temperatura:"), c);
        c.gridx=1; form.add(txtTemperatura, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Fornecedor:"), c);
        c.gridx=1; form.add(cbFornecedor, c);

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

    private void carregarFornecedoresNoCombo() {
        cbFornecedor.addItem("Nenhum fornecedor");

        Collection<Fornecedor> lista = sistema.getFornecedoresOrdenados();
        for (Fornecedor f : lista) {
            cbFornecedor.addItem(f.getCod() + " - " + f.getNome());
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtModelo.setText("");
        txtDescricao.setText("");
        txtValorBase.setText("");
        txtPeso.setText("");
        txtTemperatura.setText("");
        msg.setText("");
        cbFornecedor.setSelectedIndex(0);
    }

    private void onCadastrar(ActionEvent e) {
        msg.setText("");

        try {
            long id = Long.parseLong(txtId.getText().trim());
            String modelo = txtModelo.getText().trim();
            String descricao = txtDescricao.getText().trim();

            double valorBase = Double.parseDouble(txtValorBase.getText().trim());
            double peso = Double.parseDouble(txtPeso.getText().trim());
            double temperatura = Double.parseDouble(txtTemperatura.getText().trim());

            // verifica fornecedor selecionado
            Long codFornecedor = null;

            if (cbFornecedor.getSelectedIndex() > 0) {
                String item = (String) cbFornecedor.getSelectedItem();
                if (item != null && item.contains("-")) {
                    String codStr = item.split("-")[0].trim();
                    codFornecedor = Long.parseLong(codStr);
                }
            }

            String resultado = sistema.cadastrarTecnologia(
                    id, modelo, descricao, valorBase, peso, temperatura, codFornecedor);

            msg.append(resultado + "\n");

        } catch (NumberFormatException ex) {
            msg.append("Erro: campos numéricos inválidos.\n");
        } catch (Exception ex) {
            msg.append("Erro inesperado: " + ex.getMessage() + "\n");
        }
    }

    private void onMostrarTodos() {
        msg.setText("");
        Collection<Tecnologia> lista = sistema.getTecnologiasOrdenadas();
        if (lista.isEmpty()) {
            msg.append("(nenhuma tecnologia cadastrada)\n");
            return;
        }

        for (Tecnologia t : lista) {
            String str =
                    t.getId() + ";" +
                            t.getModelo() + ";" +
                            t.getDescricao() + ";" +
                            t.getValorBase() + ";" +
                            t.getPeso() + ";" +
                            t.getTemperatura();

            if (t.getFornecedor() != null) {
                str += ";Fornecedor=" + t.getFornecedor().getNome();
            } else {
                str += ";Fornecedor=NENHUM";
            }

            msg.append(str + "\n");
        }
    }
}

