package entidades;

import java.util.Date;
import java.util.Collection;

public class Fornecedor extends Participante {

	private Date fundacao;
    private Collection<Tecnologia> tecnologias;
    private Area area;

    public Fornecedor(long cod, String nome, Date fundacao, Area area) {
        super(cod, nome);
        this.fundacao = new Date(fundacao.getTime());
        this.area = area;
    }

    public Date getFundacao() {
        return new Date(fundacao.getTime());
    }

    public Area getArea() {
        return area;
    }

    public Collection<Tecnologia> getTecnologias() {
        return tecnologias;
    }

    public void setTecnologias(Collection<Tecnologia> tecnologias) {
        this.tecnologias = tecnologias;
    }

    @Override
    public String geraDescricao() {
        String data = new java.text.SimpleDateFormat("dd/MM/yyyy").format(fundacao);
        return getCod() + ";" + getNome() + ";" + data + ";" + area;
    }

}
