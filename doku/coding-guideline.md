=Programming Guideline=
Regeln für die Erstellung von Quellcode für die Lego
Bauplanerkennung. Die Guideline orientiert sich an dem Google Java
Style
Guide. https://google.github.io/styleguide/javaguide.html#s6-programming-practices

==File Grundlagen==
Die Top Level Class lautet: JavaDemoApplication.java

===File Encoding===
Der Quellcode wird in UTF-8 codiert. In Anweisungen sollten 
möglichst nur ASCII-Zeichen verwenden. 

===Whitespace und Escape-Sequenzen===
Im Quellcode wir außerhalb von String- und Character-Literale
nur die ASCII-Symbole Leerzeichen, newline \n und carriage
return \r verwendet. Es werden keine Tabs zur Einrückung
verwendet. Tabs sind durch 4 Leerzeichen zu ersetzten.

In String- und Character-Literale können zusätzlich die
Escape-Sequenzen \b, \t, \n, \f, \r, \", \' und \\ verwendet werden.

===Zeilen===
Für eine gute Lesbarkeit ist die Zeilenlänge von max. 100 Zeichen
einzuhalten. Optimale ist eine Zeilenlänge von 80 Zeichen. Es gibt
jedoch explizite Ausnahmen

==Source File Struktur==
Ein Source File enthält folgende Elemente in folgender Reihenfolge:
1. Allgemeine  Informationen 
2. Package Anweisung
3. Import Anweisungen
4. Toplevel Klasse
4.1. Member-Variabln
4.2. Konstruktor
4.3. Methoden

===Allgemeine Informationen===
Die allgemeinen Informationen über Lizenz, Copyright, Author(en),
und dem Datum der letzten Änderung werden in einem Block-Kommentar
eingetragen.

===Package Anweisung===
Nach den Allgemeinen Informationen folgt die Package Anweisung. Die
Package Anweisung ist eine Ausnaheme für die Zeilenlänge und darf mehr
als 100 Zeichen enthalten. Zwischen den allgemeinen Informationen und
der Package Anweisung liegen zwei Leerzeilen um einen optischen Trennung
zu gewährleisten.

===Import Anweisungen===
Nach der Package Anweisung folgen die Import Anweisungen. Importe
werden immer explizit angegeben. Es werden keine Wildcard (*) Imports
verwendet. Anweisungen sind eine Ausnahme für die Zeilenlänge und
dürfen mehr als 100 Zeichen enthalten. Imports werden zusammengehörig
gruppiert. Die einzelnen Gruppen werden werden durch 1 Leerzeile
getrennt. Zwischen Package Anweisung und Import Anweisungen liegen zwei
Leerzeilen um einen optischen Trennung zu gewährleisten.

===Toplevel Klasse===
Nach den Import Anweisungen folgt die Deklaration der Toplevel
Klasse. Jede Toplevel Klasse hat einen eigene Datei. Klassen haben
folgenen Aufbau: 
1. Membervariablen
2. Konstruktoren
3. Methoden

====Membervariablen====
Membervariablen werden sortiert nach private, protected und public
aufgelistet.

====Konstruktoren====
Eine Leerzeile trennt die Membervariablen von den
Konstruktoren. Zwischen den einzelnen Konstruktoren liegt jeweils
eine Leerzeiel 

====Methoden===
Eine Leerzeile trennt die Konstruktoren von den Methoden. Zwischen den
einzelen Methoden liegt jeweils eine Leerzeile. Überladene Methoden
werden zusammengehörend gruppiert.

==Dokumentation und Kommentare==
Dokumentation und Kommentare sind wichtig um den Code nachvollziehen
zu können und sollten nicht vernachlässigt werden.

===Dokumentation===
Alle Klassen, Interfaces, Konstruktoren und Methoden erhalten zur
Dokumentation einen Javadoc-Kommentar. Dieser wird direkt über der
Deklaration verfasst. Klassen und Interfaces erhalten den Tag @author,
Konstruktoren erhalten pro Parameter den Tag @param, Methoden erhalten
die Tags @param, @return, und, wenn nötig, die Tags @exception,
@throws und @see. Dokumentation wird in Englisch verfasst

===Kommentare===
Inline-Kommentare  werden entweder alls Zeilen-Kommentare // oder als
Block-Kommentare /* */ verfasst.

==Bezeichner==
Bezeichner enthalten nur ASCII-Buchstaben, Ziffern und
Unterstrich. Bezeichner werden in Englisch benannt.

===Package Namen===
Packages werden nur mit Kleinbuchstaben und Punkten benannt. Das
Package für die Lego Bauplanerkennung ist: tuc.isse.propra

===Klassen Namen===
Klassen werden in UpperCamelCase benannt. Die zu einer
Klasse zugehörige Testklasse hat den gleichen Bezeichner an den
"Tests" angehängt werden.

===Konstanten Namen===
Knstanten werden nur in Großbuchstaben benannt. Besteht der Bezeichner
aus mehreren Wörtern, so werden diese durch Unterstriche getrennt.

===Variablen Namen===
Variablen werden im loweCamelCase benannt.

===Methoden Namen===
Methoden werden in lowerCamelCase benannt. Methoden sollten mit einem
verb beginnen.

===Tipps===
* Explizit und Konkret Namen vergeben
* Kein generischen Namen verwenden
* Füge wichtige Details hinzu
* Bezeichner nicht zu kurz und nicht zu lang wählen

==Formatierung==
===Blöcke===
Blöcke werden verwendet für Klassen, Interfaces, Konstruktoren,
Methoden, if-, else-, for-, while- und do-Anweisungen. Nach dem
Bezeichner kommt ein Leerzeichen und die öffnende Klammer. Die
schließende Klammer kommt in eine neue Zeile. Anweisungen innerhalb
des Blocks werden mit vier Lerzeichen eingerückt.


