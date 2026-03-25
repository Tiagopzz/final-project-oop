package aplicacao;

import entidades.Comprador;
import entidades.Tecnologia;
import entidades.Venda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class JanelaCadastrarVenda extends JFrame {

    private final ACMETech sistema;

    private final JTextField txtNum = new JTextField(10);
    private final JTextField txtData = new JTextField(10);

    private final JComboBox<String> cbComprador = new JComboBox<>();
    private final JComboBox<String> cbTecnologia = new JComboBox<>();

    private final JTextArea msg = new JTextArea(8, 52);

    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public JanelaCadastrarVenda(ACMETech sistema) {
        super("Cadastrar Venda");
        this.sistema = sistema;
        df.setLenient(false);

        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);

        carregarCompradores();
        carregarTecnologias();

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.WEST;

        int y = 0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("Número:"), c);
        c.gridx=1; form.add(txtNum, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Data (dd/MM/yyyy):"), c);
        c.gridx=1; form.add(txtData, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Comprador:"), c);
        c.gridx=1; form.add(cbComprador, c);

        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Tecnologia:"), c);
        c.gridx=1; form.add(cbTecnologia, c);

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

        // Ações
        bCadastrar.addActionListener(this::onCadastrar);
        bLimpar.addActionListener(e -> limpar());
        bMostrar.addActionListener(e -> mostrarTodos());
        bFechar.addActionListener(e -> dispose());
    }

    private void carregarCompradores() {
        for (Comprador c : sistema.getCompradoresOrdenados()) {
            cbComprador.addItem(c.getCod() + " - " + c.getNome());
        }
    }

    private void carregarTecnologias() {
        for (Tecnologia t : sistema.getTecnologiasOrdenadas()) {
            String f = (t.getFornecedor() == null) ? "SEM FORNECEDOR"
                    : t.getFornecedor().getNome();
            cbTecnologia.addItem(t.getId() + " - " + t.getModelo() + " (" + f + ")");
        }
    }

    private void limpar() {
        txtNum.setText("");
        txtData.setText("");
        msg.setText("");
        cbComprador.setSelectedIndex(0);
        cbTecnologia.setSelectedIndex(0);
    }

    private void onCadastrar(ActionEvent e) {
        msg.setText("");

        try {
            long num = Long.parseLong(txtNum.getText().trim());

            Date data;
            try {
                data = df.parse(txtData.getText().trim());
            } catch (ParseException ex) {
                msg.append("Erro: data inválida.\n");
                return;
            }

            // pegar comprador
            String itemC = (String) cbComprador.getSelectedItem();
            long codComprador = Long.parseLong(itemC.split("-")[0].trim());

            // pegar tecnologia
            String itemT = (String) cbTecnologia.getSelectedItem();
            long idTec = Long.parseLong(itemT.split("-")[0].trim());

            String resultado = sistema.cadastrarVenda(
                    num, data, codComprador, idTec
            );

            msg.append(resultado + "\n");

        } catch (NumberFormatException ex) {
            msg.append("Erro: campos numéricos inválidos.\n");
        }
    }

    private void mostrarTodos() {
        msg.setText("");

        Collection<Venda> todas = sistema.getVendasOrdenadas();

        if (todas.isEmpty()) {
            msg.append("(nenhuma venda cadastrada)\n");
            return;
        }

        for (Venda v : todas) {
            msg.append("num=" + v.getNum()
                    + "; data=" + df.format(v.getData())
                    + "; valorFinal=" + v.getValorFinal()
            );

            if (v.getComprador() != null)
                msg.append("; comprador=" + v.getComprador().getNome());

            if (v.getTecnologia() != null)
                msg.append("; tecnologia=" + v.getTecnologia().getModelo());

            msg.append("\n");
        }
    }
}
