db = db.getSiblingDB('admin');

db.auth("root", "mongowhishlist!2024");

db = db.getSiblingDB('lb-wishlist');

db.createUser(
    {
        user: "<user for database which shall be created>",
        pwd: "<password of user>",
        roles: [
            {
                role: "readWrite",
                db: "<database to create>"
            }
        ]
    }
);