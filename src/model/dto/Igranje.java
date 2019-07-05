package model.dto;

import java.sql.Date;

public class Igranje {

    private Date termin;

    private Integer idScene;

    private Integer idPredstave;

    private Integer idGostujucePredstave;

    private Integer idRepertoara;

    public Igranje(Date termin, Integer idScene, Integer idPredstave, Integer idGostujucePredstave, Integer idRepertoara) {
        this.termin = termin;
        this.idScene = idScene;
        this.idPredstave = idPredstave;
        this.idGostujucePredstave = idGostujucePredstave;
        this.idRepertoara = idRepertoara;
    }

    public Date getTermin() {
        return termin;
    }

    public void setTermin(Date termin) {
        this.termin = termin;
    }

    public Integer getIdScene() {
        return idScene;
    }

    public void setIdScene(Integer idScene) {
        this.idScene = idScene;
    }

    public Integer getIdPredstave() {
        return idPredstave;
    }

    public void setIdPredstave(Integer idPredstave) {
        this.idPredstave = idPredstave;
    }

    public Integer getIdGostujucePredstave() {
        return idGostujucePredstave;
    }

    public void setIdGostujucePredstave(Integer idGostujucePredstave) {
        this.idGostujucePredstave = idGostujucePredstave;
    }

    public Integer getIdRepertoara() {
        return idRepertoara;
    }

    public void setIdRepertoara(Integer idRepertoara) {
        this.idRepertoara = idRepertoara;
    }

    @Override
    public String toString() {
        return "Igranje{" + "termin=" + termin + ", idScene=" + idScene + ", idPredstave=" + idPredstave + ", idGostujucePredstave=" + idGostujucePredstave + ", idRepertoara=" + idRepertoara + '}';
    }

}