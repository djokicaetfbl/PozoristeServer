use pozoriste_is;

call dodavanjeAdministrativnogRadnika("Pero", "Peric", "1111111111111",
"065056999", "admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918","Administrator",@pero);

call dodavanjeBiletara("Simo", "Simic", "2222222222222",
"065333333", "biletar", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918","Biletar",@pero);

call dodavanjeAdministrativnogRadnika('Djordje','Palavestra','1234554321555',
'065198900','djole','8b392d8575e5d482197f24340aba5d1b14eba2cc1007890274ec0f283303cb03','Administrator',@pero);

call dodavanjeBiletara('Milica','Medan','4545456767678',
'065777888','mina','9bb840df7f699d6547f49fd6f9ed17b2d9dd34148f0b01e798d7c51da897ea1a','Biletar',@pero);

call dodavanjeUmjetnika('Sloboda','Micalović','1002003003001','066444676','Слобода Мићаловић Ћетковић (Лесковац, 21. август 1981) српска је филмска и позоришта глумица.
Завршила је средњу музичку школу, а студирала Факултет драмских уметности у Београду у класи Предрага Бајчетића. Публика је памти по улогама у мјузиклима Позоришта на Теразијама, а стални позоришни ангажман добила је 2012. године у Народном позоришту у Београду.
Прву битну позоришну улогу имала је током студирања 2000. године, Борисава Станковића у режији Југа Радивојевића. Публика је памти и по улогама у представама Тартиф као Елмира, Чикаго као Рокси Харт, Френки и Џони као Френки и многим другим',@pero);

call dodavanjeUmjetnika('Dragana','Micalović','5002003003001','066554686','Драгана Мићаловић је рођена у Лесковцу 1991. године. Ћерка је познатог глумца Драгана Мићаловића и Милице Весковић Мићаловић која је по занимању фризерка. Има две старије сестре Мирјану и Слободу, која је такође глумица. Као мала певала је у хору и тада стекла љубав према певању. Завршила је средњу музичку школу "Станислав Бинички" у Лесковцу. Музички је надарена, а од инструмената свира клавир. Као мала са оцем је одлазила у позориште и тада заволела глуму. Са 15 година се преселила у Београд. И поред велике жеље није успела да упише Факултет драмских уметности, али је то није спречило да гради глумачку каријеру. Тренутно живи и ради у Београду, где је уписала Факултет за културу и медије који је завршила 2015. године.',@pero);

call dodavanjeUmjetnika('Nataša','Ninković','5975489654127','066100101','Detinjstvo je provela sa roditeljima — Brankom i Milenom, u Trebinju, gde je završila osnovnu i srednju školu.[1] Posle završene srednje škole odlazi u Beograd, gde je 1994. u klasi profesora Vladimira Jevtovića završila Fakultet dramskih umetnosti[2]. Tokom studija imala je male uloge u filmovima „Najbolji“ i „Tri karte za Holivud“. Nakon toga glumila je u više filmova, TV serija i pozorišnih predstava. Živi u Beogradu sa suprugom Nenadom Šarencem[3], sa kojim ima blizance Luku i Matiju[4]. Radi kao član ansambla drame u Narodnom pozorištu u Beogradu[1]',@pero);
call dodavanjeUmjetnika('Vojin','Ćetković','5971500354128','066100122','Војин Ћетковић (Крушевац, 22. август 1971) српски је филмски и позоришни глумац.
Глумачку каријеру започео је у Крушевачком позоришту, професионалну у Народном позоришту у Београду, а након уписивања Факултета драмских уметности у Београду, 1996. постао је члан Југословенског драмског позоришта, које га је стипендирало. Прву ТВ улогу остварио је у филму Рај, Петра Зеца 1993. као Томин друг, а прву главну улогу у филму Зона Замфирова 2002. године као Мане. Прославио се улогом Горана Бранда Гагића у ТВ серији Породично благо, а након тога је добио улоге у филмовима Танго је тужна мисао која се плеше, Стршљен, Пљачка Трећег рајха, Клопка, Хадерсфилд, Читуља за Ескобара и многим другим',@pero);
call dodavanjeUmjetnika('Miloš','Biković','5975487884178','066999665','Милош Биковић рођен је 13. јануара 1988. у Београду од оца Милана и мајке Марије, има 16 година старијег брата Александра, који се замонашио и добио име Михаило и постао игуман Манастира Јовања.[1] Када је био веома млад родитељи су му се развели, а његов отац се преселио у Немачку.[2] Завршио је четрнаесту београдску гимназију, говори српски, енглески и руски језик.[3] Похађао је школу глуме и говора ДАДОВ. Дипломирао је на Факултету драмских уметности у класи Драгана Петровића заједно са Нином Јанковић, Јелисаветом Орашанин, Тамаром Драгичевић, Ањом Алач.',@pero);
call dodavanjeUmjetnika('Katarina','Radivojević','1457489654123','066742587','Katarina Radivojević (Beograd, 29. ožujka 1979.) je srpska kazališna, televizijska i filmska glumica.',@pero);
call dodavanjeUmjetnika('Lane','Gutović','5974989652525','066245746','Završio je Akademiju za kazalište, film, radio i televiziju u Beogradu i 1967. godine došao u Jugoslovensko dramsko pozorište kao mlad glumac u grupi, koju su tada popularno nazvali "Bojanove bebe", jer ih je doveo Bojan Stupica.
Sljedećih dvadesetak godina igrao je u predstavama "Protekcija", "Kad su cvjetale tikve", "Buba u uhu", "Za kim zvono zvoni", "Pučina", "Kolubarska bitka", "Valjevska bolnica", "Narodni poslanik" itd. Neke od tih uloga donijele su mu Sterijinu nagradu, "Ćurana" na "Danima komedije" u Jagodini, nagradu "Zoran Radmilović" i nekoliko godišnjih nagrada JDP. Glumio je u nekolicini filmskih komedija među kojima su i četiri dijela "Tijesne kože" (1982., 1987., 1988., 1991.), gdje je s Nikolom Simićem igrao jednu od glavnih uloga i koja je doživjela enormnu gledanost.
Godine 1989. u Puli dodijeljena mu je " Zlatnu arenu" za ulogu u filmu "Poltron".
Posljednjih desetljeća glumi u svom "Kabareu" s kojim je gostovao diljem svijeta. Nastupa na Televiziji Pink, TV Fox i RTS, gdje je TV voditelj i autor kratkih humoristično-satiričnih priloga.',@pero);
call dodavanjeUmjetnika('Mima','Karadzić','277512651000','06587965412','Milutin Karadžić je rođen u Bijelom Polju 9. aprila 1955. godine. Dječačke dane, mladost, osnovnu i srednju školu završio je u Nikšiću, u gradu u kojem je živjela njegova porodica. Glumu je diplomirao na Fakultetu dramskih umetnosti u Beogradu, u klasi profesora Milenka Maričića. Bio je stalni član Pozorišta Atelje 212 od 1986. do 2013. godine.',@pero);
insert into predstava
values
(0,"O, kakav divan dan","Са пуно финог хумора говоримо односу Родитељи – Деца – Школа – Друштво. Из дечијег угла, посматрамо проблеме: Рађања, Одрастања, Пубертета, Адолесценције, Породичне сукобе, Кућне тајне … Говоримо о духовном узрастању породице.
О огромној моћи Љубави, Слушања родитеља и Праштања. Највећа драгоценост ове представе је у томе, што се гледаоци кроз пуно здравога смеха, изнова срећу сами са собом. Па, праштајући себи и другима, прихватају живот, опет, са оне могуће, лепе, ведре, оптимистичке, позитивне, РАДОСНЕ стране.","Komedija"),
(0,"Moja ti","омад “ МОЈА ТИ “ се бави проблемом старосне, идеолошке и сексуалне, те родне дискриминације. Протагонисткиње су старице, неке од њих партизанке, комунисткиње, лезбејке, мајке, свакако жене које су у доба либералног капитализма принуђене на борбу са најразличитијим мушким културним родним нормативима и примерима идеолошке деградације вредности за које су се ове жене цео живот бориле. Град бива рушен, насилно претваран у новији, модернији, јефтинији и исплативији грађевински простор, врло могуће правно нелегалних инвеститора. Приватни простори старих жена бивају одузимани. Стара лица постају сметња, спорост и непордуктивни вишак без сврхе.","Drama"),
(0,"Slavna Florens","СЛАВНА ФЛОРЕНС, по тексту британског драмског писца Питера Квилтера, jе истинита прича о Флоренс Фостер Џенкинс, најгорој певачици на свету, која је четрдесетих година у Њујорку била омиљена музичка атракција. Флоренс Џенкинс се прославила оперском каријером, иако није умела да пева. Комад прати њен живот у време када њена каријера достиже врхунац – наступом у Карнеги холу!
","Drama");
insert into gostujuca_predstava
values
(0,"Kafa i cigarete","Већ сам наслов представе КАФА И ЦИГАРЕТЕ – ПОЗДРАВ ИЗ БЕОГРАДА јасно наговаштава да је реч о својеврсном ауторском одјеку култног филма “Coffee and Cigarettes”, Џима Џармуша.
Драмска прича и представа, својим смештањем на позорницу не жели да преименује причу филма, напротив – користећи само изворни драматуршки модел, жели да са публиком подели сасвим личне теме, препознате у савременом тренутку. И све стрепње и све наде које из тих тема произилазе. Сусрети – случајни или прижељкивани; одласци – жељени или изнуђени; неизвесност сваког тренутка ка коме журимо, неминовност сваког тренутка који нас очекује… једном речју сам живот, неизмерно богат, саткан од небројено много слојева и значења,туга и радости, сусрета и растанака – уз кафу и цигарете, можда се може макар мало боље разумети.","Drama","Džim Džaruši","Mia Knežević","Milica Mihajlović,Nebojša Ilić,Tihomir Stanić,Dara Džokić,Milica Trifunović"),
(0,"Strujosek","СТРУЈОСЕК је урнебесна црна комедија сатиричног типа, у духу и сензибилитету блиском Квентину Тарантину, модерни камерни вестерн са местимичним снажним упливима монтипајтоновске естетике. То је прича о специјалном теренском раднику електродистрибуције, човеку који ступа у акцију када треба физички исећи струју посебно лежерним неплатишама и потенцијално криминогеним групама грађана за које постоји основана сумња да “краду” електричну енергију, прикључивши се на туђу станицу.","Crna komedija","Majkl Fuler","Aleksandar Švabić","Ivan Jevtović,Marko Gvero,Milica Gojković"),
(0,"Pet života pretužnog Milutina","Драма ПЕТ ЖИВОТА ПРЕТУЖНОГ МИЛУТИНА Милене Марковић, једне од најзначајнијих ауторки Југоисточне Европе, је још један комад у низу реализације стратегије подршке развоју домаћем драмском стваралаштву кроз механизам наручивања драмског текста писцима од стране Позоришта Атеље 212.","Drama","Milena Marković","Aleksandra Milavić Dejvis","Uroš Jakovljević,Jovana Davidović,Dejan Dedić");
insert into scena
values
(0,"Petar Kočić");
insert into repertoar values (0,'2019-03-01');
insert into repertoar values (0,'2019-04-01');
insert into repertoar values (0,'2019-05-01');
insert into repertoar values (0,'2019-07-01');
insert into igranje
values
('2019-3-1',(select id from scena where scena.nazivScene="Petar Kocic"),(select id from gostujuca_predstava where gostujuca_predstava.naziv="Kafa i cigarete"),null,(select id from repertoar where repertoar.mjesecIGodina='2019-3-1')),
('2019-3-2',(select id from scena where scena.nazivScene="Petar Kocic"),null,(select id from predstava where predstava.naziv="O, kakav divan dan"),(select id from repertoar where repertoar.mjesecIGodina='2019-3-1')),
('2019-7-10',(select id from scena where scena.nazivScene="Petar Kocic"),(select id from gostujuca_predstava where gostujuca_predstava.naziv="Kafa i cigarete"),null,(select id from repertoar where repertoar.mjesecIGodina='2019-7-1')),
('2019-7-12',(select id from scena where scena.nazivScene="Petar Kocic"),(select id from gostujuca_predstava where gostujuca_predstava.naziv="Kafa i cigarete"),null,(select id from repertoar where repertoar.mjesecIGodina='2019-7-1')),
('2019-7-24',(select id from scena where scena.nazivScene="Petar Kocic"),null,(select id from predstava where predstava.naziv="O, kakav divan dan"),(select id from repertoar where repertoar.mjesecIGodina='2019-7-1')),
('2019-3-3',(select id from scena where scena.nazivScene="Petar Kocic"),null,(select id from predstava where predstava.naziv="Slavna Florens"),(select id from repertoar where repertoar.mjesecIGodina='2019-3-1'));

insert into vrsta_angazmana values 
(0,"Reziser"),
(0,"Koreograf"),
(0,"Dirigent"),
(0,"Scenograf"),
(0,"Kostimograf"),
(0,"Tonac"),
(0,"Asistent rezisera"),
(0,"Asistent koreografa"),
(0,"Dizajner osvjetljenja");
#comboBoxVrstaAngazmana.getItems().addAll("Reditelj","Koreograf","Dirigent","Scenograf","Kostimograf","Tonac","Dizajner osvjetljenja","Asistent reditelja","Asistent koreografa"
 #      // ,"Korepetitor");