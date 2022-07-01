# ing-sw-2022-Gobbi-Meroni-Orsi

Camilla Gobbi, Alessandro Meroni, Marco Orsi

### Funzionalità mplementate:<br/>
Regole complete, CLI + GUI, Socket, 2 FA: partite multiple, persistenza

### Istruzioni per eseguire il progetto da jar:<br/>
abbiamo deciso di adottare un unico jar, di seguito le istruzioni per eseguire le classi main (Server, Cli, Gui)
per prima cosa occorre posizionarsi nella directory dove si trova il jar ([directory locale del progetto]/deliverables/jar) tramite comando cd<br/>
***Server***: java -cp PSP30.jar it.polimi.ingsw.serverController.Server<br/>
***Cli***: java -cp PSP30.jar it.polimi.ingsw.client.Cli<br/>
***Gui***: javaw -cp PSP30.jar it.polimi.ingsw.client.JavaFXRunner; exit

N.B. il progetto è stato sviluppato interamente su pc Windows e non ci è stato possibile testarlo su altri OS, quindi non garantiamo il corretto funzionamento su di essi
