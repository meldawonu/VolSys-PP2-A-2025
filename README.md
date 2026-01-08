**VolSys – Volunteer Management System**

VolSys (Volunteer Management System) bertujuan untuk membantu pengelolaan data volunteer, event, serta pendaftaran volunteer ke event secara 
terstruktur, terintegrasi, dan efisien.

---

📌 Fitur Utama

🔹 User (Volunteer)

- Melihat daftar event
- Mencari event (Search)
- Mendaftar ke event
- Melihat event yang diikuti
- Export data event ke PDF

🔹 Admin

* Manajemen data user (Tambah, Ubah, Hapus, Lihat)
* Manajemen data event (Tambah, Ubah, Hapus, Lihat)
* Manajemen pendaftaran volunteer ke event
* Update status pendaftaran
* Pencarian data (Search)
* Export laporan ke PDF



🏗 Arsitektur Aplikasi (MVC)

Aplikasi VolSys menerapkan konsep MVC (Model–View–Controller) untuk memisahkan logika program, tampilan, dan pengelolaan data agar kode lebih terstruktur, mudah dipelihara, dan mudah dikembangkan.

1️ Model

Berisi representasi data dan logika yang berhubungan langsung dengan database MySQL.

Contoh:

- User
- Event
- Registration

Model bertugas untuk:

Menyimpan struktur data
Mengelola proses CRUD ke database



2️ View

Berisi antarmuka pengguna berbasis Java Swing (JFC).

Contoh:

- Form Login
- Form Data User
- Form Data Event
- Tabel Data (JTable)

View bertugas untuk:

Menampilkan data ke pengguna
Menerima input dari pengguna

---

3️ Controller

Berisi penghubung antara Model dan View.

Contoh:

- UserController
- EventController
- RegistrationController
- AuthController

Controller bertugas untuk:

Memproses input dari View
Memanggil Model untuk mengambil atau mengubah data
Mengirim hasil ke View untuk ditampilkan



## 📂 Struktur Folder (Contoh)

```bash
VolSys-PP2-A-2525
├── .vscode/
├── lib/
│   ├── itextpdf-5.5.13.3.jar
│   └── mysql-connector-j-9.5.0.jar
├── src/
│   ├── config/
│   │   └── DatabaseConnection.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── UserController.java
│   │   ├── EventController.java
│   │   └── RegistrationController.java
│   ├── model/
│   │   ├── User.java
│   │   ├── Event.java
│   │   └── Registration.java
│   ├── utils/
│   │   ├── ValidationUtils.java
│   │   ├── StyleUtils.java
│   │   └── PDFExporter.java
│   ├── view/
│   │   ├── LoginFrame.java
│   │   ├── RegisterFrame.java
│   │   ├── MainFrame.java
│   │   ├── panels/
│   │   └── dialogs/
│   └── Main.java
├── .gitignore
├── database_schema.sql
└── database.properties





🛠 Teknologi yang Digunakan

Java (JFC Swing)
MySQL Database
JTable
JDBC
Git & GitHub



▶ Cara Menjalankan Aplikasi

1. Pastikan JDK dan MySQL sudah terinstal
2. Import database ke MySQL
3. Atur konfigurasi koneksi database
4. Jalankan file `Main.java`



🎯 Tujuan Pengembangan

Menerapkan konsep MVC dalam aplikasi Java Desktop
Meningkatkan efisiensi pengelolaan data volunteer dan event


📽 Demo Aplikasi

📌 Video demo dapat diakses melalui link berikut:
*(https://youtu.be/wloOMWAHdos)*



👨‍💻 Kontributor

1. Trimeldawani – 233040015
2. Azhar Muttaqien - 233040126
3. I Made Surya Kartika - 233040034
4. Dhaffa Galang Fahriza - 233040024
5. ⁠Muhammad andriansyah - 233040010





