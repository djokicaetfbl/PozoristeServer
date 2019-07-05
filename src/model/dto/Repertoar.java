package model.dto;

import java.sql.Date;
import java.util.LinkedList;

public class Repertoar {

    private Date mjesecIGodina;

    private Integer id;

    private LinkedList<Igranje> igranja = new LinkedList<>();

    public Repertoar() {
    }

    public Repertoar(Integer id,Date mjesecIGodina){
        this.mjesecIGodina=mjesecIGodina;
        this.id = id;
    }
    public void setMjesecIGodina(Date mjesecIGodina){
        this.mjesecIGodina=mjesecIGodina;
    }

    public Date getMjesecIGodina(){
        return mjesecIGodina;
    }

    public LinkedList<Igranje> getIgranja() {
        return igranja;
    }

    public void setIgranja(LinkedList<Igranje> igranja) {
        this.igranja = igranja;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Repertoar{" + "mjesecIGodina=" + mjesecIGodina + ", id=" + id + ", igranja=" + igranja + '}';
    }



}