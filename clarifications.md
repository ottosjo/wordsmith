## Frågor och förtydliganden kring uppgiften

* Vad jag förstår av lydelsen ska man behålla punkter och kommatecken på sina platser efter orden. Hur är det med andra tecken i texten: siffror? decimaltal? kolon? semikolon? andra tecken? Ska de "flyttas" eller behålla sin position på samma sätt som punkter?
  * Jag tycker vi kan nöja oss med att endast punkt och kommatecken ska behålla sin plats och resten flyttas. Det skulle kunna vara snyggt om det går att konfigurera vilka tecken som ska ha fasta positioner via t.ex. en konstant i koden.
* Vad gäller med tabs, newlines och sånt, de ska behållas rakt av?
  * De kan behållas.
* Vad gäller vid anrop med tom sträng, null eller bara space eller så, specialhantering eller inte?
  * Skulle vara snyggt om både klienten och servern validerar att det åtminstone har matats in ett alfanumeriskt tecken.
* Vad gäller vid felaktigt formaterad text, t.ex. när det saknas space efter en punkt eller komma?
  * Det behöver vi inte hantera i dagsläget.
* Någon särskild teckenuppsättning som behöver stödjas?
  * Vi kan nöja oss med Svenska / UTF-8
* Bör vi ha någon begränsning på antalet tecken som kan matas in?
  * 1024 tecken känns rimligt.
* Hur ser prestanda-kraven ut, behöver den vara performance-optimerad? Ska den klara extremt stora texter också?
  * Nej, I dagsläget vill vi inte lägga för mycket tid på optimering, utan blir det en succé så tar vi det den dagen 😉
* Räcker det med att visa de senaste reverserade meningarna under din browser-session?
  * Ja det räcker, men vi vill att meningerna persisteras så att vi kan analysera dem.
* Behövs inlogg för användarna av någon annan anledning? Utifrån lydelsen ska alla meningar persisteras, det räcker med att persistera den anonymt?
  * Vi börjar med att köra anonymt.
* Bör det finnas någon rate-limiting eller skydd mot DDoS?
  * Jag tycker vi kan hoppa över det just nu, eventuellt kan du lägga en todo med förslag på hur det kan lösas.