Lego-Bauplan-Recognition
========================

Diese Anwendung wurde im Rahmen des Modul "Programmierparktikum" der TU Clausthal entwickelt. Bei Zeiten entwickel ich die Anwedung zu Übungszwecken nochmal, da sie noch sehr stark verbugged ist und das CNN nicht auf jedem PC läuft. Ein Idee wäre C++ oder Python, da dies einheitlich mit Opencv und Tensorflow funktioniert. Für die GUI wäre Qt angebracht. 

Dieses Anwendung analysiert die Profile von Lego-Baugruppen in Voll-,
Hohl- und offene Profile.

Voraussetzungen
---------------
Damit die Anwendung ausgeführt werden kann, müssen folgende Programme 
installiert sein:

* [OpenCV 4.3.0](https://opencv.org/releases/)
* [Python 3.8](https://www.python.org/downloads/release/python-383/)
* [TensorFlow](https://www.tensorflow.org/install)

Ausführen der Anwendung
-----------------------

Um ein Bild an das Neuronale Netzwerk zu übergeben, muss zunächst der 
lokale Pfad des auszuführenden Netzes in der Mainviewcontroller.java
eingetragen werden ("python + PfadZumNN").
Außerdem muss im Neuronalen Netz (bauplan_recognition.py) der lokale Pfad zum 
übergebenen Bild (recognize-this.jpg) eingetragen werden.

Sobald dies geschehen ist, kann die Main.java ausgeführt werden und alle Funktionen
der GUI stehen zur Verfügung.

