package entidades;

import java.util.Collection;

public class Comprador extends Participante {

	private String pais;
	private String email;
    private Collection<Venda> vendas;

    public Comprador(long cod, String nome, String pais, String email) {
        super(cod, nome);
        this.pais  = pais;
        this.email = email;
    }

    public String getPais() {
        return pais;
    }

    public String getEmail() {
        return email;
    }

    public Collection<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(Collection<Venda> vendas) {
        this.vendas = vendas;
    }

    public void setNome(String nome) {
        super.setNome(nome);
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String geraDescricao() {
        return getCod() + ";" + getNome() + ";" + pais + ";" + email;
    }
}
