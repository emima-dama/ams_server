# ams_server
Scalability and Performance

La ultimele modificari:
- dependente pentru prometheus in auth-service
- fisierul prometheus.yml adaugat in auth-service
- clasa de configurare pentru MetricsRegistry
- injectarea bean-ului MetricsRegistry in AuthController si incrementarea unui Counter la login
- configurari pentru metrics in shared/application.yml din config-service
- eroare la zuul din cauza timpul prea mare de asteptare pt login
- eroare la registry service la pornire (Jersey...)
- la celelalte servicii eroare de la influx db 

Ce nu stiu:
- cum sa preiau numarul de requesturi venite la auth-service si sa creez o noua instanta cand se depaseste o limita
