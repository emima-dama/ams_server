# ams_server
Scalability and Performance

La ultimele modificari:
- adăugarea configurărilor pentru monitorizarea metrics-urilor folosing prometheus
- clase de configurare pentru MetricsRegistry in auth-service si attendance-service
- **eroare la zuul din cauza timpul prea mare de asteptare pt login**
- **eroare în registry service (com.sun.jersey.api.client.ClientHandlerException:...)**


Pași de pornire:
1. docker-compose up -d
 (din cauza ca serviciile pornesc inaintea serviciului registry-service, e nevoie de inca o rulare a comenzii)
2. docker-compose scale attendance-service=3 subject-service=3 
 (scalare posibila daca se sterge numele containerelor din docker-compose) 


