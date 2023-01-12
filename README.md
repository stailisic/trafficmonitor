# Titel der Applikation

- Hannelore Fayefunmi (ic21b027@technikum-wien.at)
- Anna Nagel (ic21b112@technikum-wien.at)
- Natasa Trajcevska (ic21b124@technikum-wien.at)

group project for ODE

## Stand des Projekts
Bemerkung: der oberste Eintrag ist das jüngste Ereignis. Die noch in Arbeit stehende Implementierung befindet sich im dev Branch und die untenstehenden Ereignisse beziehen sich somit auf die im dev Branch übertragenen commits. 

### 2023-01-12
- aus der CSV-Datei "wienerlienien-ogd-haltestellen.csv" lesen und nach einer bestimmen Adresse suchen, welche dann die dazugehörige Diva ausgibt
- ToDo: in der GUI noch die restlichen Haltestellen erstellen. Danach wenn dann eine Haltestelle in der GUI ausgewählt wird -> wird in der CSV File die Haltestelle gesucht und dann die Diva ausgeben. 
- ToDo: die ausgewählte Diva wird dann in die URL ausgegeben und wir bekommen zu dieser ausgewählten Haltestelle eine JSON Datei mit den Abfahrtszeiten retour. 

### 2023-01-10
- Refactoring von Klassen.
- Klasse JsonParse ist fertig. ToDo: Die Abfahrtszeit in ein lesbares Format anzeigen lassen, derzeit: `2023-01-10T16:43:44.000+0100`
- Unbenutzte Klassen bzw. Files gelöscht
- DebugMode verfügbar

### 2022-12-31
- Klasse JsonParse funktioniert soweit und könnte optimiert werden. Wird verwendet um Instanzen zu erstellen anhand der DIVA (= eindeutige ID der Haltestelle)
  - Methode `getKey` ist die Vorlage zur Extrahierung der Key/Values Attribute. 
  - Methode `getKeyString` hat die ähnliche Struktur wie getKey, aber mit return-Wert 
  - Mit der Methode `getKeyString` war es mir nicht möglich die `"timePlanned"` zu extrahieren. Beim Debugging ist mir aufgefallen, dass 
    - nach `"departures"` (JSONObject)  
    - die darin enthaltener `"departure"` (JSONArray) nicht weiter hinein schaut und daher 
    - mir die darhin enthaltene `"departureTime"` (JSONObject) die gesuchte `"timePlanned"` (innerhalb wiederum eines JSONObject) nicht wiedergibt.
    - Gelöst durch Workaround anhand der Methode `getKeyStringDepartures`
- Klasse Controller und die demo.fxml angepasst, siehe Kommentar mit Screenshots in Issue #7 (GUI erstellen)

### 2022-12-26 + 2022-12-27
- parseJson: 
  - used basis (Überlegung#2 siehe in Issue #4): https://www.wienerlinien.at/ogd_realtime/monitor?stopid=0&diva=60201198
  - retrieving "lines" and saving into List <br>
    ```
    Size of ListKeys: 13  
    {"name":"1","richtungsId":"1","lineId":101,"towards":"Stefan-Fadinger-Platz","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptTram","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-19T23:53:30.000+0100","timeReal":"2022-12-19T23:53:22.000+0100","countdown":1}},{"departureTime":{"timePlanned":"2022-12-20T00:05:30.000+0100","timeReal":"2022-12-20T00:05:30.000+0100","countdown":13}},{"departureTime":{"timePlanned":"2022-12-20T00:17:30.000+0100","timeReal":"2022-12-20T00:17:30.000+0100","countdown":25}},{"departureTime":{"timePlanned":"2022-12-20T00:27:30.000+0100","timeReal":"2022-12-20T00:27:30.000+0100","countdown":35},"vehicle":{"linienId":101,"name":"1","richtungsId":"1","attributes":{},"towards":"Siccardsburggasse, Betriebsbahnhof Favoriten","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptTram","direction":"H"}}]},"platform":"1","direction":"H"} 

    {"name":"1","richtungsId":"2","lineId":101,"towards":"Prater Hauptallee","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptTram","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-20T00:02:00.000+0100","timeReal":"2022-12-20T00:00:22.000+0100","countdown":8}}]},"platform":"2","direction":"R"} 

    {"name":"1","richtungsId":"2","lineId":101,"towards":"Wexstraße, Betriebsbhf. Brigittenau","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptTram","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-20T00:15:00.000+0100","timeReal":"2022-12-20T00:14:55.000+0100","countdown":23}},{"departureTime":{"timePlanned":"2022-12-20T00:30:00.000+0100","timeReal":"2022-12-20T00:30:00.000+0100","countdown":38}}]},"platform":"2","direction":"R"} 

    {"name":"2","richtungsId":"1","lineId":102,"towards":"Friedrich-Engels-Platz","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptTram","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-19T23:39:00.000+0100","timeReal":"2022-12-19T23:53:42.000+0100","countdown":2}},{"departureTime":{"timePlanned":"2022-12-19T23:54:00.000+0100","timeReal":"2022-12-19T23:56:21.000+0100","countdown":4}},{"departureTime":{"timePlanned":"2022-12-20T00:09:00.000+0100","timeReal":"2022-12-20T00:08:21.000+0100","countdown":16}},{"departureTime":{"timePlanned":"2022-12-20T00:28:00.000+0100","timeReal":"2022-12-20T00:28:00.000+0100","countdown":36}}]},"platform":"1","direction":"H"} 

    {"name":"2","richtungsId":"2","lineId":102,"towards":"Dornbach","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptTram","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-20T00:00:00.000+0100","timeReal":"2022-12-20T00:00:07.000+0100","countdown":8}},{"departureTime":{"timePlanned":"2022-12-20T00:13:00.000+0100","timeReal":"2022-12-20T00:24:12.000+0100","countdown":32}}]},"platform":"2","direction":"R"}

    {"name":"N25","richtungsId":"2","lineId":525,"towards":"Kai, Ring","trafficjam":false,"barrierFree":false,"realtimeSupported":true,"type":"ptBusNight","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-20T00:54:00.000+0100","countdown":62}}]},"platform":"2","direction":"R"}

    {"name":"N29","richtungsId":"1","lineId":529,"towards":"Floridsdorf U","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptBusNight","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-20T00:42:00.000+0100","timeReal":"2022-12-20T00:42:00.000+0100","countdown":50}}]},"platform":"1","direction":"H"}
    
    {"name":"N29","richtungsId":"2","lineId":529,"towards":"Wittelsbachstraße","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptBusNight","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-20T00:25:00.000+0100","timeReal":"2022-12-20T00:25:00.000+0100","countdown":33}},{"departureTime":{"timePlanned":"2022-12-20T00:55:00.000+0100","timeReal":"2022-12-20T00:55:00.000+0100","countdown":63}}]},"platform":"2","direction":"R"}

    {"name":"N66","richtungsId":"1","lineId":566,"towards":"Liesing S","trafficjam":false,"barrierFree":false,"realtimeSupported":true,"type":"ptBusNight","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-20T00:54:00.000+0100","countdown":62}}]},"platform":"1","direction":"H"}

    {"name":"U1","richtungsId":"1","lineId":301,"towards":"LEOPOLDAU    ","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-19T23:51:21.000+0100","timeReal":"2022-12-19T23:51:21.000+0100","countdown":0},"vehicle":{"foldingRamp":true,"linienId":301,"name":"U1","richtungsId":"1","attributes":{},"towards":"LEOPOLDAU    ","foldingRampType":"part","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-19T23:59:21.000+0100","timeReal":"2022-12-19T23:59:21.000+0100","countdown":8},"vehicle":{"foldingRamp":true,"linienId":301,"name":"U1","richtungsId":"1","attributes":{},"towards":"LEOPOLDAU    ","foldingRampType":"part","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:06:30.000+0100","countdown":14},"vehicle":{"linienId":301,"name":"U1","richtungsId":"1","attributes":{},"towards":"Leopoldau","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:14:00.000+0100","countdown":22},"vehicle":{"linienId":301,"name":"U1","richtungsId":"1","attributes":{},"towards":"Leopoldau","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:21:30.000+0100","countdown":29},"vehicle":{"linienId":301,"name":"U1","richtungsId":"1","attributes":{},"towards":"Leopoldau","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:33:00.000+0100","countdown":41},"vehicle":{"linienId":301,"name":"U1","richtungsId":"1","attributes":{},"towards":"Leopoldau","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}}]},"platform":"1","direction":"H"}<
    
    {"name":"U1","richtungsId":"2","lineId":301,"towards":"OBERLAA      ","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-19T23:52:21.000+0100","timeReal":"2022-12-19T23:52:21.000+0100","countdown":1},"vehicle":{"foldingRamp":true,"linienId":301,"name":"U1","richtungsId":"2","attributes":{},"towards":"OBERLAA      ","foldingRampType":"part","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}},{"departureTime":{"timePlanned":"2022-12-20T00:00:21.000+0100","timeReal":"2022-12-20T00:00:21.000+0100","countdown":9},"vehicle":{"foldingRamp":true,"linienId":301,"name":"U1","richtungsId":"2","attributes":{},"towards":"OBERLAA      ","foldingRampType":"part","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}},{"departureTime":{"timePlanned":"2022-12-20T00:07:00.000+0100","countdown":15},"vehicle":{"linienId":301,"name":"U1","richtungsId":"2","attributes":{},"towards":"Oberlaa","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}},{"departureTime":{"timePlanned":"2022-12-20T00:14:30.000+0100","countdown":22},"vehicle":{"linienId":301,"name":"U1","richtungsId":"2","attributes":{},"towards":"Oberlaa","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}},{"departureTime":{"timePlanned":"2022-12-20T00:23:00.000+0100","countdown":31},"vehicle":{"linienId":301,"name":"U1","richtungsId":"2","attributes":{},"towards":"Karlsplatz","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}}]},"platform":"2","direction":"R"}
    
    {"name":"U4","richtungsId":"1","lineId":304,"towards":"HEILIGENSTADT","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-19T23:51:21.000+0100","timeReal":"2022-12-19T23:51:21.000+0100","countdown":0}},{"departureTime":{"timePlanned":"2022-12-19T23:55:21.000+0100","timeReal":"2022-12-19T23:55:21.000+0100","countdown":4},"vehicle":{"foldingRamp":true,"linienId":304,"name":"U4","richtungsId":"1","attributes":{},"towards":"HEILIGENSTADT","foldingRampType":"part","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:03:30.000+0100","countdown":11},"vehicle":{"linienId":304,"name":"U4","richtungsId":"1","attributes":{},"towards":"Heiligenstadt","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:11:00.000+0100","countdown":19},"vehicle":{"linienId":304,"name":"U4","richtungsId":"1","attributes":{},"towards":"HS Gl.5   - Heiligenstadt","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:18:30.000+0100","countdown":26},"vehicle":{"linienId":304,"name":"U4","richtungsId":"1","attributes":{},"towards":"HS Gl.5   - Heiligenstadt","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:25:30.000+0100","countdown":33},"vehicle":{"linienId":304,"name":"U4","richtungsId":"1","attributes":{},"towards":"HS Gl.5   - Heiligenstadt","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}},{"departureTime":{"timePlanned":"2022-12-20T00:34:30.000+0100","countdown":42},"vehicle":{"linienId":304,"name":"U4","richtungsId":"1","attributes":{},"towards":"HS Gl.5   - Heiligenstadt","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"H"}}]},"platform":"1","direction":"H"}
    
    {"name":"U4","richtungsId":"2","lineId":304,"towards":"HÜTTELDORF   ","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","departures":{"departure":[{"departureTime":{"timePlanned":"2022-12-19T23:51:21.000+0100","timeReal":"2022-12-19T23:51:21.000+0100","countdown":0},"vehicle":{"foldingRamp":true,"linienId":304,"name":"U4","richtungsId":"2","attributes":{},"towards":"HÜTTELDORF   ","foldingRampType":"part","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}},{"departureTime":{"timePlanned":"2022-12-20T00:02:21.000+0100","timeReal":"2022-12-20T00:02:21.000+0100","countdown":11},"vehicle":{"foldingRamp":true,"linienId":304,"name":"U4","richtungsId":"2","attributes":{},"towards":"HÜTTELDORF   ","foldingRampType":"part","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}},{"departureTime":{"timePlanned":"2022-12-20T00:08:00.000+0100","countdown":16},"vehicle":{"linienId":304,"name":"U4","richtungsId":"2","attributes":{},"towards":"Hütteldorf S U","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}},{"departureTime":{"timePlanned":"2022-12-20T00:15:30.000+0100","countdown":23},"vehicle":{"linienId":304,"name":"U4","richtungsId":"2","attributes":{},"towards":"Hütteldorf S U","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}},{"departureTime":{"timePlanned":"2022-12-20T00:22:30.000+0100","countdown":30},"vehicle":{"linienId":304,"name":"U4","richtungsId":"2","attributes":{},"towards":"Karlsplatz","trafficjam":false,"barrierFree":true,"realtimeSupported":true,"type":"ptMetro","direction":"R"}}]},"platform":"2","direction":"R"}
    ```

- javafx: Demo TableView + TableColumn created. See in Issue #7 (GUI erstellen)


### 2022-12-20
- Überlegung#2 siehe in Issue #4 -> alle Haltepunkte anzeigen lassen und in einer TableView anzeigen lassen.

### 2022-12-18
- JSON Struktur festlegen siehe dazu Issue #4
- Wiki Seite eingerichtet: [JSON Collection](/JSON Collection)
- Branch Struktur erweitert um dev
- JSON Beispiel auf dev gepusht: aus stopID 147 die relevanten key/values extrahiert

### 2022-12-17
- Projektdokumentation erstellt
- Notwendigen Resourcen für Wiener Linien Echtzeitdaten gesammelt:
  - Quelle: https://www.data.gv.at/katalog/dataset/522d3045-0b37-48d0-b868-57c99726b1c4#additional-info
  - !!! Wichtig !!! Die Abfrage der Echtzeitdaten der Wiener Linien unterliegt einem Fair Use-Prinzip nach den folgenden Regeln: Es sind möglichst nur jene Haltepunkte abzufragen, welche für eine persönliche Beauskunftung notwendig sind. **Das Intervall der Abfragen sollte 15 Sekunden nicht unterschreiten.** Die Wiener Linien behalten sich vor, bei Verstoß gegen diese Fair Use-Regeln die IP-Adresse zu blockieren.
  - Metadaten zu finden im Tab Informationen auf der oben genannten Quelle:
    - http://www.wienerlinien.at/ogd_realtime/doku/ogd/wienerlinien-echtzeitdaten-dokumentation.pdf
    - http://www.wienerlinien.at/ogd_realtime/doku/ogd/wienerlinien_ogd_Beschreibung.pdf

  - wienerlinien-ogd-haltepunkte.csv
    - StopID
      ```
      StopID;DIVA;StopText;Municipality;MunicipalityID;Longitude;Latitude
      3;;Messe Prater;Wien;90001;16.4066719;48.2179891
      5;60200195;Börse;Wien;90001;16.3646213;48.2159872
      7;60201848;Praterstern, Lassallestraße;Wien;90001;16.3947175;48.2200786
      8;60201349;Hauptbahnhof S U;Wien;90001;16.3755536;48.1860744
      9;60200619;Julius-Raab-Platz;Wien;90001;16.3825366;48.2103072
      10;60200245;Stubentor U, Dr.-Karl-Lueger-Platz;Wien;90001;16.3799838;48.2067758
      ```
    - Beispiel GET Request einer Haltestelle: https://www.wienerlinien.at/ogd_realtime/monitor?stopId=147
        ```json
        {
            "data": {
                "monitors": [
                    {
                        "locationStop": {
                            "type": "Feature",
                            "geometry": {
                                "type": "Point",
                                "coordinates": [
                                    16.3495202,
                                    48.2246952
                                ]
                            },
                            "properties": {
                                "name": "60201510",
                                "title": "Währinger Straße, Volksoper U",
                                "municipality": "Wien",
                                "municipalityId": 90001,
                                "type": "stop",
                                "coordName": "WGS84",
                                "gate": "C",
                                "attributes": {
                                    "rbl": 147
                                }
                            }
                        },
                        "lines": [
                            {
                                "name": "N41",
                                "towards": "Schottentor U",
                                "direction": "R",
                                "platform": "2",
                                "richtungsId": "2",
                                "barrierFree": true,
                                "realtimeSupported": true,
                                "trafficjam": false,
                                "departures": {
                                    "departure": [
                                        {
                                            "departureTime": {
                                                "timePlanned": "2022-12-17T01:20:00.000+0100",
                                                "timeReal": "2022-12-17T01:20:00.000+0100",
                                                "countdown": 27
                                            }
                                        },
                                        {
                                            "departureTime": {
                                                "timePlanned": "2022-12-17T01:50:00.000+0100",
                                                "timeReal": "2022-12-17T01:50:00.000+0100",
                                                "countdown": 57
                                            }
                                        }
                                    ]
                                },
                                "type": "ptBusNight",
                                "lineId": 541
                            }
                        ],
                        "attributes": {}
                    }
                ]
            },
            "message": {
                "value": "OK",
                "messageCode": 1,
                "serverTime": "2022-12-17T00:53:00.000+0100"
            }
        }
        ```


### 2022-12-06 Projektkonzept
- Konzept festgelegt: Verkehrdatenabfrage - Monitoring
- siehe im Moodle (hochgeladen): Projektbeschreibung_mit_Requirements.pdf


### 2022-11-29 Erstellung des Repositories
- Members zum Repository hinzugefügt
- Members darüber informiert 


