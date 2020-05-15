# POC mit MQTT
## Setup
Dieser erste POC besteht aus 3 Komponenten:

1. Android App
2. MQTT-Broker (Cloud-host)
3. MQTT-Cli (Um Nachrichten zu senden)

### 1. Android App
Um die Android App auszuführen ist es Notwendig, den Android Auto Emulator zu erstmalig einzurichten (siehe Wiki-Eintrag unter Setup).

Anschließend muss Android Studio unter "File -> Settings -> Appearance & Behaviour -> System Settings -> Android SDK" mit dem SDK für Android 9 (API 28) konfiguriert werden.

Diese Schritte sollten ausreichen, um die App ausführen zu können. Wird die App gestartet, so wird automatisch versucht, eine Verbindung zu dem MQTT-Broker herzustellen. War dies erfolgreich, so subscribed der Client sich per default auf das Topic "test". Jede eingehende Nachricht unter diesem Topic wird anschließend als Toast angezeigt.

Sowohl die Server-URL, als auch das Default-Topic sind als Variablen in der Main-Activity gesetzt und können bei Bedarf angepasst werden.

### 2. MQTT HiveMQ Broker (Backend)
Dieser Teil der Anwendung sollte bereits fertig konfiguriert online laufen. Er ist auf einer öffentlich erreichbaren VM (bwCloud) als Docker-Container gehostet.
Alternativ kann der Container auch lokal gestartet werden:
Docker Image: https://hub.docker.com/r/hivemq/hivemq4/

### 3. MQTT-Cli
Das CLI (Command Line Interface) von HiveMQ ([Link zum Windows Client](https://github.com/hivemq/mqtt-cli/releases/download/v1.0.0/mqtt-cli-1.0.0-win.zip)) ist nur eine Möglichkeit, Nachrichten über Den Broker an die Clients zu senden. Weitere Cli Varianten existieren z.B. von Mosquitto, die aber dasselbe Protokoll sprechen und somit kompatibel sind.

Nach dem Entpacken des Zip-Archivs kann die CLI z.B. über Powershell bedient werden. Dafür kann unter Windows einfach die **mqtt-cli-shell.cmd** gestartet werden.

Anschließend muss eine Verbindung zum Broker hergestellt werden. 

`con -h "68fd7a21-267a-45d4-bbd7-d8331d9e2d3f.ul.bw-cloud-instance.org"`

Der Parameter -h enthält den Host-String (URL oder IP) ohne Protokoll oder Port. Wird keine spezifische Client-ID über den Parameter -i angegeben, so wird dem Client eine zufällige ID zugeteilt. Zusätzlich kann der Port, unter welchem der Broker läuft, über den Parameter -p angegeben werden. Wird dies nicht angegeben, wird der Default-Port 1883 verwendet.

Wurde die Verbindung erfolgreich hergestellt, können nun Nachrichten mit folgendem Befehl gesendet werden:

`pub -t "test" -m "Hello World"`

Dabei gibt der Parameter nach **-t** an, zu welchem Topic die Nachricht gehört, und der Parameter nach -m enthält die eigentliche Nachricht selbst.

