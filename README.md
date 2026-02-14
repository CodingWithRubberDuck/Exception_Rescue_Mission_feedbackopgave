Programmet startes bare fra Main som sætter gang i resten af programmet.




Jeg har forstået opgaven sådan at jeg har en "EventLog" som består af en Arrayliste der bliver vist i enden af hvert spil.
Denne liste består kun af accepterede valg i løbet af spillet.

Derudover har jeg en 'ExceptionLog' af en art som skriver til fil og noterer alle de exceptions der bliver fanget.
Hvis det virker som det skal vil en fil med dato dukke op i "exceptions"-mappen




Hvert event har mindst 4 muligheder, dog er kun 2 eller 3 relevant for det givne event, mens de resterende er "Se Status" og "Brug Repair Kit".
Der bliver tilfældigt valgt mellem et tilfældigt event fra den resterende liste. 
Et "DangerEvent" på ulige antal runder, og et "OppertunityEvent" på lige.





Alle de følgende fejl kastes i "RescueMissionService" (undtagen UnknownSituationException)
og alle fejlene fanges og vises i "UIDisplay"

Jeg har lavet "CriticalStatusException", 
som kastes ved tjek af skibets status i slutningen af hver runde samt når man fejler 2 gange i at fikse motoren i det givne event.

Jeg har lavet "InvalidTradeException",
som kastes når man ikke har de nødvendige resources til en byttehandel.

Jeg har lavet "NotUsableException",
som kastes når man prøver at bruge "Repair Kit" mens det allerede er brugt.

Jeg har lavet "InvalidActionException",
som kastes når man prøver at bruge "Reservedele" til at reparere motoren uden at have de nødvendige "Reservedele"

Jeg har lavet "UnknownSituationException";
som kastes i tilfælde der reelt ikke burde kunne ske, det er nok redundant.






Jeg har testet en del, men jeg har ikke en reel liste, jeg håber ikke der er noget åbenlyst jeg har misset.
