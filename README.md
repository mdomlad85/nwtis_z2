# NWTiS Zadaća 2

Klase i metode trebaju biti komentirane u javadoc formatu. Projekt se isključivo treba predati u formatu NetBeans projekta. Prije predavanja projekta potrebno je napraviti Clean na projektu. Zatim cijeli projekt sažeti u .zip (NE .rar) format s nazivom {LDAP_korisničko_ime}_zadaca_2.zip i predati u Moodle. Uključiti izvorni kod, primjere datoteka konfiguracijskih podataka i popunjeni obrazac za zadaću pod nazivom {LDAP_korisničko_ime}_zadaca_2.doc (u korijenskom direktoriju projekta).

Slušač aplikacije starta pozadinsku dretvu koja provjerava na poslužitelju (adresa i IMAP port određena konfiguracijom) u pravilnom intervalu (određen konfiguracijom u sek) ima li poruka u poštanskom sandučiću korisnika (adresa i lozinka određeni konfiguracijom, npr. servis@nwtis.nastava.foi.hr, 123456). Koristi se IMAP protokol. Poruke koje u predmetu (subject) imaju točno traženi sadržaj (određen konfiguracijom, npr. NWTiS poruka) obrađuju se tako da se ispituje sadržaj poruke. Poruka treba biti u "text/plain" formatu kako bi se provjerio njen sadržaj koji može biti sa sljedećem sintaksom:

ADD IoT d{1-6} ".{1-30}" GPS: -?d{1-3}.d{6},-?d{1-3}.d{6};
npr. ADD IoT 1 "FOI Varaždin"GPS: 46.307756,16.3376668;
TEMP IoT d{1-6} T: yyyy.MM.dd hh:mm:ss C: -?d{1-2}.d;
npr: TEMP IoT 1 T: 2017.04.11 17:20:19 C: 14.2;
EVENT IoT d{1-6} T: yyyy.MM.dd hh:mm:ss F: d{1-2};
npr: EVENT IoT 1 T: 2017.04.11 16:45:36 F: 22;
Za poruku koja ima ispravnu sintaksu slijedi provođenja postupka: ako se radi o komandi ADD tada treba dodati zapis u tablicu UREDAJI u bazi podataka, s time da ako već postoji, onda je pogreška. Ako se radi o komandi TEMP tada se provjerava zapis u tablici UREDAJI za zadani broj IoT i ako postoji, dodaje se zapis u tablicu TEMPERATURE. Ako ne postoji, onda je pogreška. Ako se radi o komandi EVENT tada se provjerava zapis u tablici UREDAJI za zadani broj IoT i ako postoji, dodaje se zapis u tablicu DOGADAJI. Ako ne postoji, onda je pogreška.

Obrađene poruke (određen konfiguracijom, npr. NWTiS poruka) prebacuju se u posebnu mapu (određena konfiguracijom, npr. NWTiS_Poruke). Ostale poruke prebacuju se u svoju mapu (određena konfiguracijom, npr. NWTiS_OstalePoruke). Ako neka mapa ne postoji, dretva ju treba sama kreirati. 

Dretva na kraju svakog ciklusa šalje email poruku u text/plain formatu na adresu (određena konfiguracijom, npr. admin@nwtis.nastava.foi.hr), uz predmet koji započinje statičkim dijelom (određen konfiguracijom, npr. Statistika poruka) iza kojeg dolazi redni broj poruke u formatu #.##0, a u sadržaju ima sljedeće elemente:

Obrada započela u: vrijeme_1 (dd.MM.yyyy hh.mm.ss.zzz)
Obrada završila u: vrijeme_2 (dd.MM.yyyy hh.mm.ss.zzz) 

Trajanje obrade u ms: n
Broj poruka: n - odnosi se na jedan ciklus
Broj dodanih IOT: n 
Broj mjerenih TEMP: n 
Broj izvršenih EVENT: n 
Broj pogrešaka: n 

Potrebno je napraviti korisnički dio web aplikacije za izbor jezika za lokalizaciju, slanje email poruka i pregled primljenih email poruka. Prvo se provodi izbor jezika između raspoloživih jezika. Svi statički elementi u pogledima trebaju biti pripremljeni da se prikažu u skladu s izabranih jezikom. U zaglavlju treba biti poveznica na izbor jezika, poveznice na slanje email poruke i pregled primljenih email poruka. 

Kod odabira slanja email poruka slijedi prikaz tablice unutar koje je obrazac za unos podataka poruke (primatelj, pošiljatelj, predmet, sadržaj poruke). U podnožju tablice nalazi se gumb za slanje email poruke. U zaglavlju pogleda treba biti poveznica na izbor jezika i poveznica na pregled primljenih email poruka.

Kod odabira pregleda primljenih email poruka prikazuje se padajući izbornik u kojem su elementi mape (direktoriji) iz email korisničkog računa (radi se o objektima klasa javax.mail.Folder i javax.mail.Store) i gump za promjenu mape. Ispod toga je dio za pretraživanje email poruka koji se sastoji od pripadajuće labele, jedno linijskog unosa  teksa i gumba za pretraživanje. U izborniku kod prikaza pojedine mape treba uz naziv mape dodati i ukupan broj poruka u njojm npr: INBOX - 14. Na početku je odabrana mapa INBOX. Slijedi tablica s prikazom informacija o n (određen konfiguracijom, npr. brojPoruka) najsvježijih poruka (objekti klase javax.mail.Message) iz odabrane mape i na koje je primijenjeno pretraživanje. NE SMIJU se čitati sve poruke iz mape nego samo onoliko koliko je potrebno. Ispod tablice prikazuje se ukupan broj email poruka u izabranoj mapi, gumb za prethodne i sljedeće poruke (ako nema treba pojedini gumb blokirati). Aktiviranjem pojedinog gumba treba prikazati izabrani skup email poruka. U zaglavlju treba biti poveznica na izbor jezika i poveznica na slanje email poruke.

 

JavaMail API nalazi se na adresi: https://javamail.java.net/nonav/docs/api/

JavaMail FAQ nalaze se na adresi http://www.oracle.com/technetwork/java/faq-135477.html

#Instalacija James-a
James 3.0.
preuzeti James verziju 3.0 ovdje
http://james.apache.org/
http://james.apache.org/download.cgi#Apache_James_Server 

instalirati ga na radni direktorij korisnika (npr: D:\NWTiS\grupa_1\)
pokrenuti james (na bin direktoriju skriptu run.bat na Windows ili run.sh na Linux)
postaviti se na bin direktorij u instalacijskom direktoriju james-a (cd /d  D:\NWTiS\grupa_1\apache-james-3.0-beta3\bin)
izvršiti james-cli.bat kako bi se vidjele koje su komande na raspolaganju u james konzoli (slika 1)
izvršiti james-cli.bat -h localhost listusers za ispis postojećih korisnika
izvršiti james-cli.bat -h localhost adddomain nwtis.nastava.foi.hr za kreiranje nove domene nwtis.foi.hr
izvršiti james-cli.bat -h localhost adduser servis@nwtis.nastava.foi.hr 123456 za kreiranje korisnika servis@nwtis.nastava.foi.hr s lozinkom 123456
izvršiti james-cli.bat -h localhost adduser admin@nwtis.nastava.foi.hr 654321 za kreiranje korisnika admin@nwtis.nastava.foi.hr s lozinkom 654321
kreirati nekoliko korisnika ({LDAP_korisnik} i 3 po izboru)
izvršiti james-cli.bat -h localhost listusers za ispis postojećih korisnika
u C:\WINDOWS\system32\drivers\etc\hosts dodati na kraj liniju
127.0.0.1       nwtis.nastava.foi.hr
pokrenuti Tomcat i/ili Glassfish i provjeriti http://nwtis.nastava.foi.hr:80 ili http://nwtis.nastava.foi.hr:8080 ili http://nwtis.nastava.foi.hr:8084 ovisno o postu za Tomcat i Glassfish
