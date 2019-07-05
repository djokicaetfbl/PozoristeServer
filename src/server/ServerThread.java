package server;
import model.dao.*;
import model.dto.*;
import util.ProtocolMessages;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;

import model.dao.RezervacijaDAO;

import javax.xml.crypto.Data;

public class ServerThread extends Thread {

    private Socket sock;
    private DataInputStream in;
    private DataOutputStream out;
    private int score = 0;
    public ServerThread(Socket sock)  {
        this.sock = sock;
        try {
            // inicijalizuj ulazni stream
            in = new DataInputStream(sock.getInputStream());
            // inicijalizuj izlazni stream
            out = new DataOutputStream(sock.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        start();
    }

    public void run() throws NullPointerException {
        try {
            int currentQuestion = 0;
            String request="";
            boolean loginOK = false;
            boolean isActive = true;

            while (isActive) {
            	try
                {
            		request = in.readUTF();
                }catch(SocketException | EOFException en) {
                	//System.out.println("");
                    return;
                }
                //if(request!=null) {
                System.out.println("[Klijent " + sock.getInetAddress() + ":" + sock.getPort() + "] " + request);

                if (request.startsWith(ProtocolMessages.UKLONI_IGRANJE.getMessage())) {
                    //Igranje(Date termin, Integer idScene, Integer idPredstave, Integer idGostujucePredstave, Integer idRepertoara)
                    String[] igranje=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(igranje.length==6){
                        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date utermin=formatter.parse(igranje[1]);
                        java.sql.Date termin=new Date(utermin.getTime());
                        Integer idScena=Integer.parseInt(igranje[2]);
                        Integer idPredstave=Integer.parseInt(igranje[3]);
                        Integer idGostujucePredstave=Integer.parseInt(igranje[4]);
                        Integer idRepertoara=Integer.parseInt(igranje[5]);
                        Igranje igranje1=new Igranje(termin, idScena, idPredstave,idGostujucePredstave, idRepertoara);
                        IgranjeDAO.UkloniIgranje(igranje1);
                        out.writeUTF(ProtocolMessages.UKLONI_IGRANJE_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.UKLONI_IGRANJE_NOT_OK.getMessage());
                    }
                }
                else if (request.startsWith(ProtocolMessages.SCENE.getMessage())){
                    String response=ScenaDAO.scene();
                    if(response!=null){
                        out.writeUTF(response);
                    }
                    else{
                        out.writeUTF(ProtocolMessages.SCENE_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.GET_IGRANJA.getMessage())){

                    if(IgranjeDAO.getIgranja(Integer.parseInt(request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage())[1]))!=null){
                        out.writeUTF(IgranjeDAO.getIgranja(Integer.parseInt(request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage())[1])));
                    }
                    else {
                        out.writeUTF(ProtocolMessages.GET_IGRANJA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_IGRANJE.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    java.util.Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(req[1]);
                    Date termin=new Date(date1.getTime());
                    Integer idScene=null;
                    if(!req[2].equals("null"))
                    	idScene=Integer.parseInt(req[2]);
                    Integer idPredstave=null;
                    if(!req[3].equals("null"))
                    	idPredstave=Integer.parseInt(req[3]);
                    Integer idGostujucePredstave=null;
                    if(!req[4].equals("null"))
                    	idGostujucePredstave=Integer.parseInt(req[4]);
                    Integer idRepertoara=null;
                    if(!req[5].equals("null"))
                    	idRepertoara=Integer.parseInt(req[5]);

                    //Igranje(Date termin, Integer idScene, Integer idPredstave, Integer idGostujucePredstave, Integer idRepertoara)
                    Igranje igranje=new Igranje(termin, idScene, idPredstave, idGostujucePredstave, idRepertoara);
                    IgranjeDAO.dodajIgranje(igranje);
                    out.writeUTF(ProtocolMessages.DODAJ_IGRANJE_OK.getMessage());
                }
                else if(request.startsWith(ProtocolMessages.PREDSTAVE.getMessage())){
                    String response=PredstavaDAO.predstave();
                    System.out.println(response);
                    if(response!=null){
                        out.writeUTF(response);
                    }
                    else{
                        out.writeUTF(ProtocolMessages.PREDSTAVE_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.GOSTUJUCE_PREDSTAVE.getMessage())){
                    String response = GostujucaPredstavaDAO.gostujucePredstave();
                    System.out.println(response);
                    if(response!=null){
                        out.writeUTF(response);
                    }
                    else{
                        out.writeUTF(ProtocolMessages.GOSTUJUCE_PREDSTAVE_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.AZURIRAJ_PREDSTAVU.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    // Predstava(String naziv, String opis, String tip)
                    Predstava predstava=new Predstava(req[1], req[2], req[3]);
                    if(req.length==5){
                        predstava.setId(Integer.parseInt(req[4]));
                    }
                    PredstavaDAO.azurirajPredstavu(predstava);
                    out.writeUTF(ProtocolMessages.AZURIRAJ_PREDSTAVU_OK.getMessage());
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_AZURIRANJE.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //Azuriranje(Integer idPredstave, Integer idRepertoara, Integer idGostujucePredstave, Integer idRadnik)
                    Integer idPredstave=null;
                    if(req[1].equals("null")){
                    	Integer rrr=PredstavaDAO.getIdPredstave(req[1]);
                    	if(rrr!=-1)
                    		idPredstave = rrr;
                    }
                    Integer idRepertoara=null;
                    if(!req[2].equals("null")){
                        idRepertoara=Integer.parseInt(req[2]);
                    }
                    Integer idGostujucePredstave=null;
                    if(!req[3].equals("null")){
                    	Integer rrr=GostujucaPredstavaDAO.getIdGostujucePredstave(req[3]);
                    	if(rrr!=-1)
                    		idGostujucePredstave = rrr;
                    }
                    Integer idRadnik=AdministratorDAO.vratiId("admin");
                    Azuriranje azuriranje=new Azuriranje(idPredstave, idRepertoara, idGostujucePredstave, idRadnik);
                    AzuriranjeDAO.dodajAzuriranje(azuriranje);
                    out.writeUTF(ProtocolMessages.DODAJ_AZURIRANJE_OK.getMessage());
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_PREDSTAVU.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //Predstava(String naziv, String opis, String tip)
                    Predstava predstava=new Predstava(req[1], req[2], req[3]);
                    PredstavaDAO.dodajPredstavu(predstava);
                    out.writeUTF(ProtocolMessages.DODAJ_PREDSTAVU_OK.getMessage());
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_KREIRANJE.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //Kreiranje(Integer idPredstave, Integer idRepertoara, Integer idGostujucePredstave, Integer idRadnik)
                    Integer idPredstave=null;
                    if(req[1].equals("NULL")) {
                    	Integer rrr=PredstavaDAO.getIdPredstave(req[5]);
                    	if(rrr!=-1)
                    		idPredstave = rrr;
                    }
                    Integer idRepertoara=null;
                    if(!req[2].equals("NULL")){
                        idRepertoara=Integer.parseInt(req[2]);
                    }
                    Integer idGostujucePredstave=null;
                    if(req[3].equals("NULL")){
                    	Integer rrr=GostujucaPredstavaDAO.getIdGostujucePredstave(req[5]);
                    	if(rrr!=-1)
                    		idGostujucePredstave=rrr;
                    }
                    Integer idRadnik=AdministratorDAO.vratiId("admin");
                    Kreiranje kreiranje=new Kreiranje(idPredstave, idRepertoara, idGostujucePredstave, idRadnik);
                    KreiranjeDAO.dodajKreiranje(kreiranje);
                    Integer idP=-1;
                    if(idPredstave!=null) {
                    	idP=idPredstave;
                    }
                    else {
                    	idP=idGostujucePredstave;
                    }
                    out.writeUTF(ProtocolMessages.DODAJ_KREIRANJE_OK.getMessage()+idP);
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_GOSTUJUCU_PREDSTAVU.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //GostujucaPredstava(String naziv,String opis,String tip,String pisac,String reziser,String glumci)
                    GostujucaPredstava gostujucaPredstava=new GostujucaPredstava(req[1], req[2], req[3], req[4], req[5], req[6]);
                    GostujucaPredstavaDAO.dodajGostujucuPredstavu(gostujucaPredstava);
                    out.writeUTF(ProtocolMessages.DODAJ_GOSTUJUCU_PREDSTAVU_OK.getMessage());
                }
                else if(request.startsWith(ProtocolMessages.AZURIRAJ_GOSTUJUCU_PREDSTAVU.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //GostujucaPredstava(String naziv,String opis,String tip,String pisac,String reziser,String glumci)
                    GostujucaPredstava gostujucaPredstava=new GostujucaPredstava(req[2], req[3], req[4], req[5], req[6], req[7]);
                    gostujucaPredstava.setId(Integer.parseInt(req[1]));
                    GostujucaPredstavaDAO.azurirajGostujucuPredstavu(gostujucaPredstava);
                    out.writeUTF(ProtocolMessages.AZURIRAJ_GOSTUJUCU_PREDSTAVU_OK.getMessage());
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_ADMINISTRATIVNOG_RADNIKA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //AdministrativniRadnik(String ime, String prezime, String jmb, boolean statusRadnika, String kontak
                    //            ,String korisnickoIme, String hashLozinke, String tipKorisnika)
                    boolean statusRadnika=req[4]=="true"?true:false;
                    AdministrativniRadnik administrativniRadnik=new AdministrativniRadnik(req[1], req[2], req[3], statusRadnika,
                            req[5], req[6], req[7], req[8]);
                    AdministratorDAO.dodajAdministrativnogRadnika(administrativniRadnik);
                    out.writeUTF(ProtocolMessages.DODAJ_ADMINISTRATIVNOG_RADNIKA_OK.getMessage());
                }
                else if(request.startsWith(ProtocolMessages.IZMIJENI_ADMINISTRATIVNOG_RADNIKA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //AdministrativniRadnik(String ime, String prezime, String jmb, boolean statusRadnika, String kontak
                    //            ,String korisnickoIme, String hashLozinke, String tipKorisnika)
                    if(req.length==10) {
                        boolean statusRadnika = req[4].equals("true")  ? true : false;
                        AdministrativniRadnik administrativniRadnik = new AdministrativniRadnik(req[1], req[2], req[3], statusRadnika,
                                req[5], req[6], req[7], req[8]);
                        administrativniRadnik.setIdRadnika(Integer.parseInt(req[9]));
                        AdministratorDAO.izmjeniAdministratora(administrativniRadnik);
                        out.writeUTF(ProtocolMessages.IZMIJENI_ADMINISTRATIVNOG_RADNIKA_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.IZMIJENI_ADMINISTRATIVNOG_RADNIKA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_BILETARA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //Biletar(String ime, String prezime, String jmb, boolean statusRadnika, String kontakt,
                    //                   String korisnickoIme, String hashLozinke, String tipKorisnika)
                    if(req.length==9){
                        boolean statusRadnika=req[4].equals("true")?true:false;
                        Biletar biletar=new Biletar(req[1], req[2], req[3], statusRadnika, req[5], req[6], req[7], req[8]);
                        BiletarDAO.dodajBiletara(biletar);
                        out.writeUTF(ProtocolMessages.DODAJ_BILETARA_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.DODAJ_BILETARA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.IZMIJENI_BILETARA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //Biletar(String ime, String prezime, String jmb, boolean statusRadnika, String kontakt,
                    //                   String korisnickoIme, String hashLozinke, String tipKorisnika)
                    if(req.length==10){
                        boolean statusRadnika=req[4].equals("true")?true:false;
                        Biletar biletar=new Biletar(req[1], req[2], req[3], statusRadnika, req[5], req[6], req[7], req[8]);
                        biletar.setIdRadnika(Integer.parseInt(req[9]));
                        BiletarDAO.izmjeniBiletara(biletar);
                        out.writeUTF(ProtocolMessages.IZMIJENI_BILETARA_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.IZMIJENI_BILETARA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_UMJETNIKA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
//                  Umjetnik(String ime, String prezime, String jmb, boolean statusRadnika, String kontak, String biografija)
                    if(req.length==7){
                        boolean statusRadnika=req[4].equals("true")?true:false;
                        Umjetnik umjetnik=new Umjetnik(req[1], req[2], req[3], statusRadnika, req[5], req[6]);
                        UmjetnikDAO.dodajUmjetnika(umjetnik);
                        out.writeUTF(ProtocolMessages.DODAJ_UMJETNIKA_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.DODAJ_UMJETNIKA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.IZMIJENI_UMJETNIKA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
//                  Umjetnik(String ime, String prezime, String jmb, boolean statusRadnika, String kontak, String biografija)
                    if(req.length==8){
                        boolean statusRadnika=req[4].equals("true")?true:false;
                        Umjetnik umjetnik=new Umjetnik(req[1], req[2], req[3], statusRadnika, req[5], req[6]);
                        umjetnik.setIdRadnika(Integer.parseInt(req[7]));
                        UmjetnikDAO.izmjeniUmjetnika(umjetnik);
                        out.writeUTF(ProtocolMessages.IZMIJENI_UMJETNIKA_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.IZMIJENI_UMJETNIKA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_REPERTOAR.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
//                  Repertoar(Integer id,Date mjesecIGodina)
                    if(req.length==3) {
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[2]);
                        Date termin = new Date(date1.getTime());
                        Repertoar repertoar=new Repertoar(Integer.parseInt(req[1]), termin);
                        RepertoarDAO.dodajRepertoar(repertoar);
                        out.writeUTF(ProtocolMessages.DODAJ_REPERTOAR_OK.getMessage());
                    }
                    else {
                        out.writeUTF(ProtocolMessages.DODAJ_REPERTOAR_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.IZMIJENI_REPERTOAR.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
//                  Repertoar(Integer id,Date mjesecIGodina)
                    if(req.length==3) {
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[2]);
                        Date termin = new Date(date1.getTime());
                        Repertoar repertoar=new Repertoar(Integer.parseInt(req[1]), termin);
                        RepertoarDAO.izmjeniRepertoar(repertoar);
                        out.writeUTF(ProtocolMessages.IZMIJENI_REPERTOAR_OK.getMessage());
                    }
                    else {
                        out.writeUTF(ProtocolMessages.IZMIJENI_REPERTOAR_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.REZERVACIJE.getMessage())) {
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==3) {
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[1]);
                        Date termin = new Date(date1.getTime());
                        String response=RezervacijaDAO.rezervacije(termin, Integer.parseInt(req[2]));
                        out.writeUTF(response);
                    }
                    else {
                        out.writeUTF(ProtocolMessages.REZERVACIJE_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.ADD_REZERVACIJA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //Rezervacija(Integer id, String ime, Date termin, Integer idScene)
                    if(req.length==5){
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[3]);
                        Date termin = new Date(date1.getTime());
                        Rezervacija rezervacija=new Rezervacija(Integer.parseInt(req[1]), req[2], termin, Integer.parseInt(req[4]));
                        RezervacijaDAO.addRezervacija(rezervacija);
                        out.writeUTF(ProtocolMessages.ADD_REZERVACIJA_OK.getMessage());
                    }
                    else {
                        out.writeUTF(ProtocolMessages.ADD_REZERVACIJA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.ADD_REZERVISANO_SJEDISTE.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
//                  RezervisanoSjediste(Integer idScene,Integer brojSjedista,Integer idRezervacije,Date termin)
                    if (req.length == 5) {
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[4]);
                        Date termin = new Date(date1.getTime());
                        RezervisanoSjediste rezervisanoSjediste=new RezervisanoSjediste(Integer.parseInt(req[1]), Integer.parseInt(req[2]), Integer.parseInt(req[3]), termin);
                        RezervisanoSjedisteDAO.addRezervisanoSjediste(rezervisanoSjediste);
                        out.writeUTF(ProtocolMessages.ADD_REZERVISANO_SJEDISTE_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.ADD_REZERVISANO_SJEDISTE_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_ANGAZMAN.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
//                  dodajAngazman(Integer idPredstave,Integer idUmjetnika,Integer idVrstaAngazmana,Date datumOd)
                    if(req.length==5){
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[4]);
                        Date terminOd = new Date(date1.getTime());
                        AngazmanDAO.dodajAngazman(req[1]=="null"?null:Integer.parseInt(req[1]), Integer.parseInt(req[2]), Integer.parseInt(req[3]), terminOd);
                        out.writeUTF(ProtocolMessages.DODAJ_ANGAZMAN_OK.getMessage());
                    }
                    else {
                        out.writeUTF(ProtocolMessages.DODAJ_ANGAZMAN_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.AZURIRAJ_ANGAZMAN.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    //azurirajAngazman(Integer idPredstave,Integer idUmjetnika,Integer idVrstaAngazmana,Date datumOd,Date datumDo)
                    if(req.length==6){
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[4]);
                        Date terminOd = new Date(date1.getTime());
                        java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(req[5]);
                        Date terminDo = new Date(date2.getTime());
                        AngazmanDAO.azurirajAngazman(Integer.parseInt(req[1]), Integer.parseInt(req[2]), Integer.parseInt(req[3]), terminOd, terminDo);
                        out.writeUTF(ProtocolMessages.AZURIRAJ_ANGAZMAN_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.AZURIRAJ_ANGAZMAN_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.VRSTE_ANGAZMANA.getMessage())){
                    String response=VrstaAngazmanaDAO.vrsteAngazmana();
                    if(response!=null){
                    	System.out.println("OVO SE POSALJE KLIJENTU"+response);
                        out.writeUTF(response);
                    }
                    else{
                        out.writeUTF(ProtocolMessages.VRSTE_ANGAZMANA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.UMJETNICI.getMessage())){
                    String response=UmjetnikDAO.umjetnici();
                    if(response!=null){
                        DataOutputStream dos=new DataOutputStream(sock.getOutputStream());
                        dos.writeUTF(response);
                    }
                    else {
                        out.writeUTF(ProtocolMessages.UMJETNICI_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.ANGAZMANI.getMessage())){
                    //Predstava(String naziv, String opis, String tip)
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==4) {
                        Predstava predstava=new Predstava(req[1], req[2], req[3]);
                        predstava.setId(PredstavaDAO.getIdPredstave(req[1]));
                        String response = AngazmanDAO.angazmani(predstava);
                        if (response != null) {
                            out.writeUTF(response);
                        }
                        else{
                            out.writeUTF(ProtocolMessages.ANGAZMANI_NOT_OK.getMessage());
                        }
                    }
                    else {
                        out.writeUTF(ProtocolMessages.ANGAZMANI_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_ANGAZMAN_VA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==2){
                        VrstaAngazmanaDAO.dodajAngazman(req[1]);
                        out.writeUTF(ProtocolMessages.DODAJ_ANGAZMAN_VA_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.DODAJ_ANGAZMAN_VA_NOT_OK.getMessage());
                    }
                }
                else  if(request.startsWith(ProtocolMessages.REPERTOARS.getMessage())){
                    String response=RepertoarDAO.repertoars();
                    if(response!=null){
                        out.writeUTF(response);
                    }
                    else {
                        out.writeUTF(ProtocolMessages.REPERTOARS_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.KARTE.getMessage())){
                    String response=KartaDAO.karte();
                    if(response!=null){
                        out.writeUTF(response);
                    }
                    else{
                        out.writeUTF(ProtocolMessages.KARTE_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.SJEDISTA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==2) {
                        String response = SjedisteDAO.sjedista(Integer.parseInt(req[1]));
                        if(response!=null){
                            out.writeUTF(response);
                        }
                        else{
                            out.writeUTF(ProtocolMessages.SJEDISTA_NOT_OK.getMessage());
                        }
                    }
                    else{
                        out.writeUTF(ProtocolMessages.SJEDISTA_NOT_OK.getMessage());
                    }
                }
              else if(request.startsWith(ProtocolMessages.OBRISI_REZERVACIJU.getMessage())){
                    //Rezervacija(Integer id, String ime, Date termin, Integer idScene)
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==5){
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[3]);
                        Date termin = new Date(date1.getTime());
                        Rezervacija rezervacija=new Rezervacija(Integer.parseInt(req[1]), req[2], termin, Integer.parseInt(req[4]));
                        RezervacijaDAO.obrisiRezervaciju(rezervacija);
                        out.writeUTF(ProtocolMessages.OBRISI_REZERVACIJU_OK.getMessage());
                    }
                    else {
                        out.writeUTF(ProtocolMessages.OBRISI_REZERVACIJU_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.DODAJ_KARTU.getMessage())){
                    //Karta(Integer id,Integer brojReda,Integer brojSjedista,Date termin,Integer idScene)
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==6){
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[4]);
                        Date termin = new Date(date1.getTime());
                        Karta karta=new Karta(Integer.parseInt(req[1]), Integer.parseInt(req[2]), Integer.parseInt(req[3]),termin,Integer.parseInt(req[5]));
                        KartaDAO.dodajKartu(karta);
                        out.writeUTF(ProtocolMessages.DODAJ_KARTU_OK.getMessage());
                    }
                    else {
                        out.writeUTF(ProtocolMessages.DODAJ_KARTU_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.OBRISI_KARTU.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==2){
                        KartaDAO.obrisiKartu(Integer.parseInt(req[1]));
                        out.writeUTF(ProtocolMessages.OBRISI_KARTU_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.OBRISI_KARTU_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.OBRISI_REZERVISANO_SJEDISTE.getMessage())){
                    //RezervisanoSjediste(Integer idScene,Integer brojSjedista,Integer idRezervacije,Date termin)
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==5){
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[4]);
                        Date termin = new Date(date1.getTime());
                        RezervisanoSjediste rezervisanoSjediste=new RezervisanoSjediste(Integer.parseInt(req[1]), Integer.parseInt(req[2]), Integer.parseInt(req[3]),termin);
                        RezervisanoSjedisteDAO.obrisiRezervisanoSjediste(rezervisanoSjediste);
                        out.writeUTF(ProtocolMessages.OBRISI_REZERVISANO_SJEDISTE_OK.getMessage());
                    }
                    else{
                        out.writeUTF(ProtocolMessages.OBRISI_REZERVISANO_SJEDISTE_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.SJEDISTA_RS.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==3){
                        //sjedista(Date termin,Integer idScene)
                        java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(req[1]);
                        Date termin = new Date(date1.getTime());
                        String response=RezervisanoSjedisteDAO.sjedista(termin, Integer.parseInt(req[2]));
                        if(response!=null){
                            out.writeUTF(response);
                        }
                        else {
                            out.writeUTF(ProtocolMessages.SJEDISTA_RS_NOT_OK.getMessage());
                        }
                    }
                    else{
                        out.writeUTF(ProtocolMessages.SJEDISTA_RS_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.PROVJERI_MATICNI_BROJ_U_BAZI.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==2){
                        if(AdministratorDAO.provjeriMaticniBrojUBazi(req[1])==false){
                            out.writeUTF(ProtocolMessages.PROVJERI_MATICNI_BROJ_U_BAZI_RESPONSE.getMessage()+"false");
                        }
                        else {
                            out.writeUTF(ProtocolMessages.PROVJERI_MATICNI_BROJ_U_BAZI_RESPONSE.getMessage()+"true");
                        }
                    }
                    else {
                        out.writeUTF(ProtocolMessages.PROVJERI_MATICNI_BROJ_U_BAZI_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.POSOTOJI_U_BAZI_LOZINKA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==2){
                        if(AdministratorDAO.postojiUBaziLozinka(req[1])==true){
                            out.writeUTF(ProtocolMessages.POSOTOJI_U_BAZI_LOZINKA_RESPONSE.getMessage()+"true");
                        }
                        else {
                            out.writeUTF(ProtocolMessages.POSOTOJI_U_BAZI_LOZINKA_RESPONSE.getMessage()+"false");
                        }
                    }
                    else {
                        out.writeUTF(ProtocolMessages.POSOTOJI_U_BAZI_LOZINKA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.POSTOJI_U_BAZI_KORISNICKO_IME.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==2){
                        if(AdministratorDAO.postojiUBaziKorisnickoIme(req[1])==true){
                            out.writeUTF(ProtocolMessages.POSTOJI_U_BAZI_KORISNICKO_IME_RESPONSE.getMessage()+"true");
                        }
                        else {
                            out.writeUTF(ProtocolMessages.POSTOJI_U_BAZI_KORISNICKO_IME_RESPONSE.getMessage()+"false");
                        }
                    }
                    else {
                        out.writeUTF(ProtocolMessages.POSTOJI_U_BAZI_KORISNICKO_IME_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.DODAVANJE_SJEDISTA.getMessage())){
                    String[] req=request.split(ProtocolMessages.MESSAGE_SEPARATOR.getMessage());
                    if(req.length==3){
//                        dodavanjeSjedista(Integer idScene,Integer brojSjedista)
                        if(SjedisteDAO.dodavanjeSjedista(Integer.parseInt(req[1]), Integer.parseInt(req[2]))){
                            out.writeUTF(ProtocolMessages.DODAVANJE_SJEDISTA_OK.getMessage());
                        }
                        else {
                            out.writeUTF(ProtocolMessages.DODAVANJE_SJEDISTA_NOT_OK.getMessage());
                        }
                    }
                    else {
                        out.writeUTF(ProtocolMessages.DODAVANJE_SJEDISTA_NOT_OK.getMessage());
                    }
                }
                else if(request.startsWith(ProtocolMessages.UBACI_U_TABELU_RADNIK_ADMIN.getMessage())) {
                	String response=AdministratorDAO.ubaciUTabeluRadnik();
                	if(response!=null) {
                		out.writeUTF(response);
                	}
                }
                else if(request.startsWith(ProtocolMessages.UBACI_U_TABELU_RADNIK_BILETAR.getMessage())) {
                	String response=BiletarDAO.ubaciUTabeluRadnik();
                	if(response!=null) {
                		out.writeUTF(response);
                	}
                }
                else if(request.startsWith(ProtocolMessages.UBACI_U_TABELU_RADNIK_UMJETNIK.getMessage())) {
                	String response=UmjetnikDAO.ubaciUTabeluRadnik();
                	if(response!=null) {
                        DataOutputStream dos=new DataOutputStream(sock.getOutputStream());
                		dos.writeUTF(response);
                	}
                }
                else if ("END".equals(request)) {
                    isActive = false;
                }
                
            }


            // zatvori konekciju
            in.close();
            out.close();
            sock.close();
        }catch (NullPointerException ex) {
            System.out.println("");
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("[Klijent " + sock.getInetAddress() + ":" + sock.getPort() + "] se odjavljuje.");
    }

}