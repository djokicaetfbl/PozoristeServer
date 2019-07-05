package model.dto;

public class VrstaAngazmana {
    Integer id;
    String naziv;

    public VrstaAngazmana(Integer id, String naziv){
        this.id=id;
        this.naziv=naziv;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }
}