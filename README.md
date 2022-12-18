# Titel der Applikation

- Hannelore Fayefunmi (ic21b027@technikum-wien.at)
- Anna Nagel (ic21b112@technikum-wien.at)
- Natasa Trajcevska (ic21b124@technikum-wien.at)

group project for ODE

## Stand des Projekts
Bemerkung: der oberste Eintrag ist das jüngste Ereignis.


### 2022-12-18
- JSON Struktur festlegen siehe dazu Issue#4
- Wiki Seite eingerichtet: [JSON Collection](/JSON Collection)


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


