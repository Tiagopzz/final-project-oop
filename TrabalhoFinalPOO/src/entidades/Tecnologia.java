package entidades;

public class Tecnologia {

	private long id;
    private String modelo;
    private String descricao;
    private double valorBase;
    private double peso;
    private double temperatura;
    private Fornecedor fornecedor;

	public void defineFornecedor(Fornecedor f) {
        this.fornecedor = f;
	}

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public double getValorBase() {
        return valorBase;
    }

    public Tecnologia(long id, String modelo, String descricao,
                      double valorBase, double peso, double temperatura,
                      Fornecedor fornecedor) {
        this.id = id;
        this.modelo = modelo;
        this.descricao = descricao;
        this.valorBase = valorBase;
        this.peso = peso;
        this.temperatura = temperatura;
        this.fornecedor = fornecedor;
    }

    public long getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPeso() {
        return peso;
    }

    public double getTemperatura() {
        return temperatura;
    }
}
