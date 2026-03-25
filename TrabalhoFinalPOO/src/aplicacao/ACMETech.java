package aplicacao;

import entidades.Area;
import entidades.Comprador;
import entidades.Fornecedor;
import entidades.Tecnologia;
import entidades.Venda;
import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class ACMETech {
    private final TreeMap<Long, Fornecedor> fornecedores = new TreeMap<>();
    private final TreeMap<Long, Comprador> compradores = new TreeMap<>();
    private final TreeMap<Long, Tecnologia> tecnologias = new TreeMap<>();
    private final TreeMap<Long, Venda> vendas = new TreeMap<>(Comparator.reverseOrder());
    private final Queue<Venda> filaVendas = new LinkedList<>();
    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public ACMETech() {
        df.setLenient(false);
    }

    public void inicializar() {
        try {
            fornecedores.clear();
            compradores.clear();
            tecnologias.clear();
            vendas.clear();
            filaVendas.clear();

            try (BufferedReader br = new BufferedReader(new FileReader("PARTICIPANTESENTRADA.CSV"))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    if (linha.isBlank() || linha.startsWith("cod")) continue;

                    String[] parts = linha.split(";");
                    long cod = Long.parseLong(parts[0]);
                    String nome = parts[1];
                    int tipo = Integer.parseInt(parts[2]);

                    if (tipo == 1) {
                        Date fundacao = df.parse(parts[3]);
                        Area area = Area.valueOf(parts[4]);
                        Fornecedor f = new Fornecedor(cod, nome, fundacao, area);
                        f.setTecnologias(new ArrayList<>());
                        fornecedores.put(cod, f);
                    }
                    else if (tipo == 2) {
                        String pais = parts[3];
                        String email = parts[4];
                        Comprador c = new Comprador(cod, nome, pais, email);
                        c.setVendas(new ArrayList<>());
                        compradores.put(cod, c);
                    }
                }
            }

            try (BufferedReader br = new BufferedReader(new FileReader("TECNOLOGIASENTRADA.CSV"))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    if (linha.isBlank() || linha.startsWith("id")) continue;

                    String[] parts = linha.split(";");
                    long id = Long.parseLong(parts[0]);
                    String modelo = parts[1];
                    String descricao = parts[2];
                    double valorBase = Double.parseDouble(parts[3]);
                    double peso = Double.parseDouble(parts[4]);
                    double temperatura = Double.parseDouble(parts[5]);
                    long codFornecedor = Long.parseLong(parts[6]);

                    Fornecedor fornecedor = fornecedores.get(codFornecedor);

                    Tecnologia t = new Tecnologia(id, modelo, descricao,
                            valorBase, peso, temperatura, fornecedor);

                    tecnologias.put(id, t);

                    if (fornecedor != null) {
                        fornecedor.getTecnologias().add(t);
                    }
                }
            }

            try (BufferedReader br = new BufferedReader(new FileReader("VENDASENTRADA.CSV"))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    if (linha.isBlank() || linha.startsWith("num")) continue;

                    String[] parts = linha.split(";");
                    long num = Long.parseLong(parts[0]);
                    Date data = df.parse(parts[1]);
                    long codComprador = Long.parseLong(parts[2]);
                    long idTecnologia = Long.parseLong(parts[3]);

                    Comprador comp = compradores.get(codComprador);
                    Tecnologia tec = tecnologias.get(idTecnologia);

                    Venda v = new Venda(num, data, tec, comp);
                    filaVendas.add(v);
                }
            }

            while (!filaVendas.isEmpty()) {
                Venda v = filaVendas.poll();

                cadastrarVenda(
                        v.getNum(),
                        v.getData(),
                        v.getComprador().getCod(),
                        v.getTecnologia().getId()
                );
            }

        } catch (Exception e) {
            System.err.println("Erro ao inicializar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void executar() {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menu = new MenuPrincipal(this);
            menu.setVisible(true);
        });
    }

    public String cadastrarFornecedor(long cod, String nome, Date fundacao, Area area) {
        if (fornecedores.containsKey(cod)) {
            return "Erro: código de fornecedor já cadastrado.";
        }
        Fornecedor f = new Fornecedor(cod, nome, fundacao, area);
        fornecedores.put(cod, f);
        return "OK: fornecedor cadastrado.";
    }

    public boolean existeFornecedor(long cod) {
        return fornecedores.containsKey(cod);
    }

    public Collection<Fornecedor> getFornecedoresOrdenados() {
        return fornecedores.values();
    }

    public SimpleDateFormat getDateFormat() {
        return df;
    }

    public Collection<Comprador> getCompradoresOrdenados() {
        return compradores.values();
    }

    public Collection<Tecnologia> getTecnologiasOrdenadas() {
        return tecnologias.values();
    }

    public Collection<Venda> getVendasOrdenadas() {
        return vendas.values();
    }

    public String cadastrarTecnologia(long id,
                                      String modelo,
                                      String descricao,
                                      double valorBase,
                                      double peso,
                                      double temperatura,
                                      Long codFornecedor) {

        if (tecnologias.containsKey(id)) {
            return "Erro: identificador de tecnologia já cadastrado.";
        }

        Tecnologia t = new Tecnologia(id, modelo, descricao,
                valorBase, peso, temperatura, null);

        if (codFornecedor != null) {
            Fornecedor f = fornecedores.get(codFornecedor);
            if (f == null) {
                return "Erro: fornecedor informado não existe.";
            }

            t.defineFornecedor(f);

            if (f.getTecnologias() == null) {
                f.setTecnologias(new ArrayList<>());
            }
            f.getTecnologias().add(t);
        }

        tecnologias.put(id, t);
        return "OK: tecnologia cadastrada.";
    }

    public String cadastrarComprador(long cod, String nome, String pais, String email) {
        if (compradores.containsKey(cod)) {
            return "Erro: código de comprador já cadastrado.";
        }

        Comprador c = new Comprador(cod, nome, pais, email);
        c.setVendas(new ArrayList<>());

        compradores.put(cod, c);

        return "OK: comprador cadastrado.";
    }

    public String cadastrarVenda(long num,
                                 Date data,
                                 long codComprador,
                                 long idTecnologia) {

        if (vendas.containsKey(num)) {
            return "Erro: número de venda já cadastrado.";
        }

        Comprador comprador = compradores.get(codComprador);
        if (comprador == null) {
            return "Erro: comprador não encontrado.";
        }

        Tecnologia tecnologia = tecnologias.get(idTecnologia);
        if (tecnologia == null) {
            return "Erro: tecnologia não encontrada.";
        }

        for (Venda v : vendas.values()) {
            if (v.getTecnologia() != null &&
                    v.getTecnologia().getId() == idTecnologia) {
                return "Erro: tecnologia já foi vendida.";
            }
        }

        Venda venda = new Venda(num, data, tecnologia, comprador);
        venda.calculaValorFinal();
        vendas.put(num, venda);

        if (comprador.getVendas() == null) {
            comprador.setVendas(new ArrayList<>());
        }
        comprador.getVendas().add(venda);

        return "OK: venda cadastrada. Valor final = " + venda.getValorFinal();
    }

    public String relatorioTecnologias() {
        if (tecnologias.isEmpty()) {
            return "Erro: não há tecnologia cadastrada.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("id;modelo;descricao;valorBase;peso;temperatura;fornCod;fornNome;fornArea\n");

        for (Tecnologia t : tecnologias.values()) {
            long id = t.getId();
            String modelo = t.getModelo();
            String desc = t.getDescricao();
            double valorBase = t.getValorBase();
            double peso = t.getPeso();
            double temp = t.getTemperatura();

            Fornecedor f = t.getFornecedor();
            if (f != null) {
                sb.append(id).append(";")
                        .append(modelo).append(";")
                        .append(desc).append(";")
                        .append(valorBase).append(";")
                        .append(peso).append(";")
                        .append(temp).append(";")
                        .append(f.getCod()).append(";")
                        .append(f.getNome()).append(";")
                        .append(f.getArea());
            } else {
                sb.append(id).append(";")
                        .append(modelo).append(";")
                        .append(desc).append(";")
                        .append(valorBase).append(";")
                        .append(peso).append(";")
                        .append(temp).append(";")
                        .append("SEM_FORNECEDOR").append(";")
                        .append("SEM_FORNECEDOR").append(";")
                        .append("-");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public String relatorioFornecedores() {
        if (fornecedores.isEmpty()) {
            return "Erro: não há fornecedor cadastrado.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("cod;nome;fundacao;area\n");

        for (Fornecedor f : fornecedores.values()) {
            sb.append(f.getCod()).append(";")
                    .append(f.getNome()).append(";")
                    .append(df.format(f.getFundacao())).append(";")
                    .append(f.getArea())
                    .append("\n");
        }

        return sb.toString();
    }

    public String relatorioCompradores() {
        if (compradores.isEmpty()) {
            return "Erro: não há comprador cadastrado.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("cod;nome;pais;email;qtdVendas\n");

        for (Comprador c : compradores.values()) {
            int qtdVendas = (c.getVendas() == null) ? 0 : c.getVendas().size();

            sb.append(c.getCod()).append(";")
                    .append(c.getNome()).append(";")
                    .append(c.getPais()).append(";")
                    .append(c.getEmail()).append(";")
                    .append(qtdVendas)
                    .append("\n");
        }

        return sb.toString();
    }

    public String relatorioVendas() {
        if (vendas.isEmpty()) {
            return "Erro: não há venda cadastrada.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("num;data;valorFinal;tecId;tecModelo;compCod;compNome\n");

        for (Venda v : vendas.values()) {
            String data = df.format(v.getData());
            double valor = v.getValorFinal();

            Tecnologia t = v.getTecnologia();
            Comprador c = v.getComprador();

            long tecId = (t != null ? t.getId() : -1);
            String tecModelo = (t != null ? t.getModelo() : "N/A");

            long compCod = (c != null ? c.getCod() : -1);
            String compNome = (c != null ? c.getNome() : "N/A");

            sb.append(v.getNum()).append(";")
                    .append(data).append(";")
                    .append(valor).append(";")
                    .append(tecId).append(";")
                    .append(tecModelo).append(";")
                    .append(compCod).append(";")
                    .append(compNome)
                    .append("\n");
        }

        return sb.toString();
    }

    public String removerVenda(long num) {
        Venda v = vendas.remove(num);
        if (v == null) {
            return "Erro: não existe venda com esse número.";
        }

        Comprador c = v.getComprador();
        if (c != null && c.getVendas() != null) {
            c.getVendas().remove(v);
        }

        return "OK: venda removida.";
    }

    public String alterarComprador(long cod,
                                   String novoNome,
                                   String novoPais,
                                   String novoEmail) {

        Comprador c = compradores.get(cod);

        if (c == null) {
            return "Erro: comprador não encontrado.";
        }

        if (novoNome == null || novoNome.isBlank()) {
            return "Erro: nome não pode ficar vazio.";
        }
        if (novoPais == null || novoPais.isBlank()) {
            return "Erro: país não pode ficar vazio.";
        }
        if (novoEmail == null || novoEmail.isBlank()) {
            return "Erro: email não pode ficar vazio.";
        }

        c.setNome(novoNome);
        c.setPais(novoPais);
        c.setEmail(novoEmail);

        return "OK: comprador atualizado.";
    }

    public String consultarTecnologiaMaiorValor() {
        if (tecnologias.isEmpty()) {
            return "Erro: não há tecnologia cadastrada.";
        }

        double max = Double.NEGATIVE_INFINITY;
        List<Tecnologia> lista = new ArrayList<>();

        for (Tecnologia t : tecnologias.values()) {
            if (t.getValorBase() > max) {
                max = t.getValorBase();
                lista.clear();
                lista.add(t);
            } else if (t.getValorBase() == max) {
                lista.add(t);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Tecnologia(s) com maior valor base = ").append(max).append("\n");

        for (Tecnologia t : lista) {
            sb.append(t.getId()).append(" - ").append(t.getModelo()).append("\n");
        }

        return sb.toString();
    }

    public String consultarFornecedorMaiorNumTecnologias() {
        if (fornecedores.isEmpty()) {
            return "Erro: não há fornecedor cadastrado.";
        }

        int max = -1;
        List<Fornecedor> lista = new ArrayList<>();

        for (Fornecedor f : fornecedores.values()) {
            int qtd = (f.getTecnologias() == null ? 0 : f.getTecnologias().size());
            if (qtd > max) {
                max = qtd;
                lista.clear();
                lista.add(f);
            } else if (qtd == max) {
                lista.add(f);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Fornecedor(es) com maior número de tecnologias = ").append(max).append("\n");

        for (Fornecedor f : lista) {
            sb.append(f.getCod()).append(" - ").append(f.getNome()).append("\n");
        }

        return sb.toString();
    }

    public String consultarCompradorMaiorNumVendas() {
        if (compradores.isEmpty()) {
            return "Erro: não há comprador cadastrado.";
        }

        int max = -1;
        List<Comprador> lista = new ArrayList<>();

        for (Comprador c : compradores.values()) {
            int qtd = (c.getVendas() == null ? 0 : c.getVendas().size());
            if (qtd > max) {
                max = qtd;
                lista.clear();
                lista.add(c);
            } else if (qtd == max) {
                lista.add(c);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Comprador(es) com maior número de vendas = ").append(max).append("\n");

        for (Comprador c : lista) {
            sb.append(c.getCod()).append(" - ").append(c.getNome()).append("\n");
        }

        return sb.toString();
    }

    public String consultarVendaMaiorValor() {
        if (vendas.isEmpty()) {
            return "Erro: não há venda cadastrada.";
        }

        double max = Double.NEGATIVE_INFINITY;
        List<Venda> lista = new ArrayList<>();

        for (Venda v : vendas.values()) {
            if (v.getValorFinal() > max) {
                max = v.getValorFinal();
                lista.clear();
                lista.add(v);
            } else if (v.getValorFinal() == max) {
                lista.add(v);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Venda(s) com maior valor final = ").append(max).append("\n");

        for (Venda v : lista) {
            sb.append("num=").append(v.getNum())
                    .append("; comprador=").append(v.getComprador().getNome())
                    .append("; tecnologia=").append(v.getTecnologia().getModelo())
                    .append("; valorFinal=").append(v.getValorFinal())
                    .append("\n");
        }

        return sb.toString();
    }

    public String salvarDados(String nomeBase) {
        String nomeArquivo = nomeBase + ".json";
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");

        sb.append("\"fornecedores\": [\n");
        int count = 0;
        for (Fornecedor f : fornecedores.values()) {
            sb.append("  {\n");
            sb.append("    \"cod\": ").append(f.getCod()).append(",\n");
            sb.append("    \"nome\": \"").append(f.getNome()).append("\",\n");
            sb.append("    \"fundacao\": \"").append(df.format(f.getFundacao())).append("\",\n");
            sb.append("    \"area\": \"").append(f.getArea()).append("\"\n");
            sb.append("  }");
            count++;
            if (count < fornecedores.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("],\n");

        sb.append("\"compradores\": [\n");
        count = 0;
        for (Comprador c : compradores.values()) {
            sb.append("  {\n");
            sb.append("    \"cod\": ").append(c.getCod()).append(",\n");
            sb.append("    \"nome\": \"").append(c.getNome()).append("\",\n");
            sb.append("    \"pais\": \"").append(c.getPais()).append("\",\n");
            sb.append("    \"email\": \"").append(c.getEmail()).append("\"\n");
            sb.append("  }");
            count++;
            if (count < compradores.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("],\n");

        sb.append("\"tecnologias\": [\n");
        count = 0;
        for (Tecnologia t : tecnologias.values()) {
            sb.append("  {\n");
            sb.append("    \"id\": ").append(t.getId()).append(",\n");
            sb.append("    \"modelo\": \"").append(t.getModelo()).append("\",\n");
            sb.append("    \"descricao\": \"").append(t.getDescricao()).append("\",\n");
            sb.append("    \"valorBase\": ").append(t.getValorBase()).append(",\n");
            sb.append("    \"peso\": ").append(t.getPeso()).append(",\n");
            sb.append("    \"temperatura\": ").append(t.getTemperatura()).append(",\n");

            if (t.getFornecedor() != null)
                sb.append("    \"fornecedor\": ").append(t.getFornecedor().getCod()).append("\n");
            else
                sb.append("    \"fornecedor\": null\n");

            sb.append("  }");
            count++;
            if (count < tecnologias.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("],\n");

        sb.append("\"vendas\": [\n");
        count = 0;
        for (Venda v : vendas.values()) {
            sb.append("  {\n");
            sb.append("    \"num\": ").append(v.getNum()).append(",\n");
            sb.append("    \"data\": \"").append(df.format(v.getData())).append("\",\n");
            sb.append("    \"valorFinal\": ").append(v.getValorFinal()).append(",\n");
            sb.append("    \"comprador\": ").append(v.getComprador().getCod()).append(",\n");
            sb.append("    \"tecnologia\": ").append(v.getTecnologia().getId()).append("\n");
            sb.append("  }");
            count++;
            if (count < vendas.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n");

        sb.append("}\n");

        try (FileWriter fw = new FileWriter(nomeArquivo)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            return "Erro ao salvar dados: " + e.getMessage();
        }

        return "OK: dados salvos em " + nomeArquivo;
    }

    public String carregarDados(String nomeBase) {
        String nomeArquivo = nomeBase + ".json";

        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    sb.append(linha).append("\n");
                }
            }

            String json = sb.toString().trim();

            fornecedores.clear();
            compradores.clear();
            tecnologias.clear();
            vendas.clear();

            String blocoFor = extrairBloco(json, "\"fornecedores\"");
            if (blocoFor == null) return "Erro: bloco de fornecedores ausente.";

            for (String obj : separarObjetosJSON(blocoFor)) {
                long cod = Long.parseLong(extrairCampo(obj, "cod"));
                String nome = extrairCampo(obj, "nome");
                String fundacaoStr = extrairCampo(obj, "fundacao");
                String areaStr = extrairCampo(obj, "area");

                Date fundacao = df.parse(fundacaoStr);
                Area area = Area.valueOf(areaStr);

                Fornecedor f = new Fornecedor(cod, nome, fundacao, area);
                f.setTecnologias(new ArrayList<>());

                fornecedores.put(cod, f);
            }
            String blocoComp = extrairBloco(json, "\"compradores\"");
            if (blocoComp == null) return "Erro: bloco de compradores ausente.";
            for (String obj : separarObjetosJSON(blocoComp)) {
                long cod = Long.parseLong(extrairCampo(obj, "cod"));
                String nome = extrairCampo(obj, "nome");
                String pais = extrairCampo(obj, "pais");
                String email = extrairCampo(obj, "email");

                Comprador c = new Comprador(cod, nome, pais, email);
                c.setVendas(new ArrayList<>());

                compradores.put(cod, c);
            }
            String blocoTec = extrairBloco(json, "\"tecnologias\"");
            if (blocoTec == null) return "Erro: bloco de tecnologias ausente.";
            for (String obj : separarObjetosJSON(blocoTec)) {
                long id = Long.parseLong(extrairCampo(obj, "id"));
                String modelo = extrairCampo(obj, "modelo");
                String descricao = extrairCampo(obj, "descricao");
                double valorBase = Double.parseDouble(extrairCampo(obj, "valorBase"));
                double peso = Double.parseDouble(extrairCampo(obj, "peso"));
                double temperatura = Double.parseDouble(extrairCampo(obj, "temperatura"));
                String fornecedorStr = extrairCampo(obj, "fornecedor");
                Fornecedor fornecedor = null;
                if (!fornecedorStr.equals("null")) {
                    long codFor = Long.parseLong(fornecedorStr);
                    fornecedor = fornecedores.get(codFor);
                }
                Tecnologia t = new Tecnologia(id, modelo, descricao, valorBase, peso, temperatura, fornecedor);
                if (fornecedor != null) {
                    fornecedor.getTecnologias().add(t);
                }
                tecnologias.put(id, t);
            }

            String blocoVen = extrairBloco(json, "\"vendas\"");
            if (blocoVen == null) return "Erro: bloco de vendas ausente.";

            for (String obj : separarObjetosJSON(blocoVen)) {
                long num = Long.parseLong(extrairCampo(obj, "num"));
                String dataStr = extrairCampo(obj, "data");
                double valorFinal = Double.parseDouble(extrairCampo(obj, "valorFinal"));
                long codComprador = Long.parseLong(extrairCampo(obj, "comprador"));
                long idTecnologia = Long.parseLong(extrairCampo(obj, "tecnologia"));

                Date data = df.parse(dataStr);
                Comprador c = compradores.get(codComprador);
                Tecnologia t = tecnologias.get(idTecnologia);

                if (c == null || t == null) {
                    return "Erro: venda aponta para comprador/tecnologia inexistente.";
                }
                Venda v = new Venda(num, data, t, c);
                v.setValorFinalInterno(valorFinal);
                vendas.put(num, v);
                c.getVendas().add(v);
            }
            return "OK: dados carregados com sucesso.";
        } catch (Exception e) {
            return "Erro ao carregar dados: " + e.getMessage();
        }
    }

    private String extrairBloco(String json, String chave) {
        int i = json.indexOf(chave);
        if (i == -1) return null;
        i = json.indexOf("[", i);
        if (i == -1) return null;
        int nivel = 0;
        int inicio = i;
        for (int j = i; j < json.length(); j++) {
            char ch = json.charAt(j);
            if (ch == '[') nivel++;
            if (ch == ']') {
                nivel--;
                if (nivel == 0) {
                    return json.substring(inicio, j + 1);
                }
            }
        }
        return null;
    }

    private List<String> separarObjetosJSON(String arrayJSON) {
        List<String> lista = new ArrayList<>();
        int nivel = 0;
        int inicio = -1;

        for (int i = 0; i < arrayJSON.length(); i++) {
            char ch = arrayJSON.charAt(i);
            if (ch == '{') {
                if (nivel == 0) inicio = i;
                nivel++;
            } else if (ch == '}') {
                nivel--;
                if (nivel == 0 && inicio != -1) {
                    lista.add(arrayJSON.substring(inicio, i + 1));
                }
            }
        }
        return lista;
    }

    private String extrairCampo(String obj, String campo) {
        String chave = "\"" + campo + "\"";
        int i = obj.indexOf(chave);
        i = obj.indexOf(":", i);
        i++;
        int j = i;
        while (j < obj.length() && obj.charAt(j) != ',' && obj.charAt(j) != '}') {
            j++;
        }
        String val = obj.substring(i, j).trim();
        if (val.startsWith("\"") && val.endsWith("\"")) {
            val = val.substring(1, val.length() - 1);
        }
        return val;
    }
}
