package entidades;

import java.util.Date;

public class Venda {

	private long num;
    private Date data;
    private double valorFinal;
    private Tecnologia tecnologia;
    private Comprador comprador;

    public Venda(long num, Date data, Tecnologia tecnologia, Comprador comprador) {
        this.num = num;
        this.data = new Date(data.getTime());
        this.tecnologia = tecnologia;
        this.comprador = comprador;
    }

    public long getNum() { return num; }
    public Date getData() { return new Date(data.getTime()); }
    public Tecnologia getTecnologia() { return tecnologia; }
    public Comprador getComprador() { return comprador; }
    public double getValorFinal() { return valorFinal; }

    public double calculaValorFinal() {
        Area area = tecnologia.getFornecedor().getArea();
        double fator;

        switch (area) {
            case TI -> fator = 1.20;
            case ANDROIDES -> fator = 1.15;
            case EMERGENTE -> fator = 1.25;
            case ALIMENTOS -> fator = 1.10;
            default -> fator = 1.0;
        }

        int vendasPrevias;
        if (comprador.getVendas() == null) {
            vendasPrevias = 0;
        } else {
            vendasPrevias = comprador.getVendas().size();
        }
        double desconto = Math.min(vendasPrevias, 10) / 100.0;
        valorFinal = tecnologia.getValorBase() * fator * (1.0 - desconto);
        return valorFinal;
    }

    public void setValorFinalInterno(double valorFinal) {
        this.valorFinal = valorFinal;
    }
}
