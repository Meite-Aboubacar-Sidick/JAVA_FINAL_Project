import mysql.connector

# Connexion au serveur MySQL (pas encore à la base)
conn = mysql.connector.connect(
    host="localhost",
    user="root",       # ton utilisateur MySQL
    password="root"        # ton mot de passe MySQL
)

cursor = conn.cursor()

# Création de la base de données
cursor.execute("CREATE DATABASE IF NOT EXISTS reservation_system;")
print("✅ Base de données créée avec succès (reservation_system).")

# Sélection de la base
cursor.execute("USE reservation_system;")
# Création de la table users
cursor.execute("""
CREATE TABLE IF NOT EXISTS book_flight (
    id INT AUTO_INCREMENT PRIMARY KEY,
    flight_schedule VARCHAR(150) NOT NULL,
    flight_no VARCHAR(255) NOT NULL,
    available_seats INT,
    dep VARCHAR(100) NOT NULL,
    dest VARCHAR(100) NOT NULL,
    arrival_time VARCHAR(100) NOT NULL,
    flight_time VARCHAR(100) NOT NULL,
    gate VARCHAR(50),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
""")


# Création de la table resevations
cursor.execute("""
CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    flight_id INT NOT NULL,
    flight_no VARCHAR(255) NOT NULL,
    seats_booked INT NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (flight_id) REFERENCES book_flight(id),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
""")

print("Flight for booking Table created successfuly")

conn.commit()
cursor.close()
conn.close()