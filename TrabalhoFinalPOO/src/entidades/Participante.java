package entidades;

public abstract class Participante {

	private long cod;
	private String nome;

    protected Participante(long cod, String nome) {
        this.cod = cod;
        this.nome = nome;
    }
    public long getCod()     { return cod; }
    public String getNome()  { return nome; }

    protected void setNome(String nome) {
        this.nome = nome;
    }

	public abstract String geraDescricao();
}
