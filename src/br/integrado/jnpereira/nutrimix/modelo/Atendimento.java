
package br.integrado.jnpereira.nutrimix.modelo;

import br.integrado.jnpereira.nutrimix.dao.AutoIncrement;
import br.integrado.jnpereira.nutrimix.dao.Coluna;
import br.integrado.jnpereira.nutrimix.dao.Id;
import br.integrado.jnpereira.nutrimix.dao.Tabela;
import java.util.Date;


@Tabela(nome="atendimento")
public class Atendimento {
    
    @Id
    @AutoIncrement
    @Coluna(nome="cd_atend")
    private Integer cdAtend;
    @Coluna(nome="nr_mesa")
    private Integer nrMesa;
    @Coluna(nome="dt_atend")
    private Date dtAtend;
    @Coluna(nome="st_atend")
    private String stAtend;

    public Integer getCdAtend() {
        return cdAtend;
    }

    public void setCdAtend(Integer cdAtend) {
        this.cdAtend = cdAtend;
    }

    public Integer getNrMesa() {
        return nrMesa;
    }

    public void setNrMesa(Integer nrMesa) {
        this.nrMesa = nrMesa;
    }

    public Date getDtAtend() {
        return dtAtend;
    }

    public void setDtAtend(Date dtAtend) {
        this.dtAtend = dtAtend;
    }

    public String getStAtend() {
        return stAtend;
    }

    public void setStAtend(String stAtend) {
        this.stAtend = stAtend;
    }
    
    
}
