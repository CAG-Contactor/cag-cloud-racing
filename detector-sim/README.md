Detektor-simulator
==================
Enkel webapp som skall användas för att simulera Raspberry-baserad passage-detektion.

Nu fungerar knapparna Start, Mellan och Mål. Dock måste ni ändra URL-addressen på rad 80 i filen main.ts så den pekar ut rätt raspberry API. 

Ni hittar rätt namn genom att gå till Services och välja API med namnet "cloud-racing-raspberry-api
-XX", där XX är er domän. Väl där så väljer man "Stages" i listan till höger och ert eget domän-namn. Klicka på POST-meddelandet och URLen ni söker visas som "Invoke  URL"

Installera och starta
---------------------

    $ npm install && npm start
    
Öppna browser på http://localhost:9080.
