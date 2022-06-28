# Peer-Review 1: UML

Giorgio Miani, Alessia Porta, Andrea Verrone 

Gruppo 31

Valutazione del diagramma UML delle classi del gruppo 30.

## Lati positivi

Come aspetti positivi possiamo evidenziare la scelta di inserire all'interno della classe `Match` un riferimento a tutte le classi principali in modo da averla come unico punto di ingresso al model. Inoltre, se gestita in modo opportuno, ci sembra una buona idea la suddivisione tra gioco con regole base e regole per esperti, in modo da suddividere le due varianti e poterle gestire separatamente. Anche il metodo `importStudents(Cloud)` della classe `Player` ci è sembrata una buona scelta, dato che permette di spostare gli studenti dalla nuvola all'ingresso rendendo questa meccanica più diretta e immediata. Infine, la presenza dell'attributo `powerUp` delle carte personaggio che fornisce una descrizione testuale dell'effetto della carta permette di mostrarlo con facilità ai giocatori.

## Lati negativi

In generale abbiamo notato che le classi sono molto interconnesse tra di loro, il che genera un'eccessiva ripetizione di riferimenti e non permette di avere una chiara distinzione dei ruoli, rendendo il diagramma poco lineare.
Abbiamo poi notato una criticità nella suddivisione delle classi base e per esperti. Infatti dato che il tipo statico della board in `Player` e `Match` è `Board`, il metodo `getCoinsNumber()` non sarebbe utilizzabile in quanto definito solo nella classe `Board_Esperti`.
Infine non siamo riusciti a identificare metodi che permettano il calcolo dell'influenza sulle isole o che permetta di unificarle, se non il metodo `uniteIsland(Island, Island)` dove però non è definito in che modo dovrebbe unirle.


Di seguito abbiamo indicato alcuni dettagli sulle singole classi che secondo noi andrebbero ricontrollati.

Nella classe `Match` secondo noi si dovrebbe mettere o il metodo `addPlayer()`, passandogli in ingresso il `Player` da aggiungere, oppure i 3 costruttori che settano i giocatori di quella partita, ma non entrambi.
Inoltre non ci è sembrata una buona scelta quella di gestire i professori come un array di `Board`, dato che i professori sono già salvati nella `Board` del giocatore che li possiede.

La classe `CharacterCard` non dovrebbe essere un'interfaccia ma una classe astratta in quanto gli attributi non possono essere `static final`.
Considerando invece il metodo `power()`, dato che non riceve nulla in ingresso, restituisce `void` e non ha riferimenti all'esterno, non ci è chiaro come sia possibile fargli applicare l'effetto della carta. 

Nella classe `Island` gli studenti presenti sono salvati come `Collection<Student>`, il che rende particolarmente oneroso contare quanti ce ne sono per ogni tipo in quanto si dovrebbe ogni volta iterare sulla Collection e controllare il tipo. 

La classe `Player` ha un riferimento alle classi `Island` e `Cloud` che secondo noi non servirebbero in quanto mai utilizzate. Inoltre non vi è nessun riferimento al mago scelto dal giocatore che, seppur non usato attivamente, serve per distinguere i diversi mazzi di carte.

Abbiamo infine notato delle incorrettezze a livello sintattico. Infatti le associazioni unidirezionali sono state poste nel verso opposto e sono state messe sia associazioni sia attributi per indicare lo stesso riferimento. Inoltre alcune cardinalità sono errate o mancanti.

## Confronto tra le architetture

Una parte di questo class diagram a cui noi non avevamo pensato è l'attributo `powerUp` della classe `CharacterCard`, che descrive l'effetto della carta personaggio in modo da farlo vedere ai giocatori.

Abbiamo poi notato che sono state fatte delle scelte che avevamo preso in considerazione anche noi inizialmente ma che abbiamo poi deciso di cambiare per una questione di praticità ed efficienza. 
Le classi `Tower` e `Student` ad esempio hanno un solo attributo che ne identifica il tipo e quindi sarebbe possibile utilizzare direttamente l'enumerazione corrispondente nei casi in cui è importante tenere traccia del tipo (ad esempio l'attributo `tower: Tower` in `Island` potrebbe diventare `tower: Color`) oppure utilizzare un intero nel caso in cui sia importante il numero corrispondente (ad esempio in `Board` i vari array di Student che sono presenti potrebbero diventare degli interi che indicano il numero di studenti). In questo modo si eviterebbe di dover avere troppe istanze della stessa classe inutilmente (ad esempio le 130 istanze della classe `Student`).
Anche la classe `MotherNature` potrebbe essere eliminata secondo noi, salvandone la posizione direttamente nella classe `Match`.

Infine crediamo che non sarebbe necessario salvare in una `Collection` tutte le carte assistente usate da un giocatore, ma basterebbe salvare l'ultima ed eliminarle dal mazzo man mano che vengono usate. 
