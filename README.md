Personal Finance Manager - Moneeeyyyyyy 💰
Aplikasi manajemen keuangan pribadi berbasis Java dengan GUI Swing yang memungkinkan pengguna untuk melacak pemasukan, pengeluaran, dan mengatur anggaran bulanan.

📋 Daftar Isi
Fitur Utama

Teknologi yang Digunakan

Struktur Projek

Persyaratan Sistem

Cara Menjalankan

Panduan Penggunaan

Format Data

Troubleshooting

Fitur dalam Pengembangan

Lisensi

✨ Fitur Utama
🔐 Manajemen Akun
Registrasi pengguna baru

Login/Logout sistem

Manajemen sesi pengguna

💳 Manajemen Transaksi
Tambah transaksi (pemasukan/pengeluaran)

Edit dan hapus transaksi

Filter transaksi berdasarkan:

Bulan dan tahun

Rentang tanggal

Kategori

Jenis transaksi

Import transaksi dari file eksternal

📊 Dashboard Analitik
Ringkasan keuangan bulan ini:

Saldo terkini

Total pemasukan

Total pengeluaran

Grafik pengeluaran per kategori

Daftar transaksi terbaru

Peringatan anggaran

💰 Manajemen Anggaran (Budget)
Atur limit anggaran per kategori

Tracking pengeluaran vs anggaran

Peringatan saat mendekati/melampaui anggaran

Update dan hapus anggaran

🎨 Antarmuka Pengguna
GUI modern dengan desain rounded corners

Navigasi intuitif

Responsive layout

Color-coded transaction types

🛠️ Teknologi yang Digunakan
Komponen	Teknologi
Bahasa	Java 8+
GUI Framework	Java Swing
Penyimpanan Data	CSV Files
Build Tool	Native Java Compiler
Date Handling	Java Time API
📁 Struktur Projek
text
PersonalFinanceManager/
├── model/                 # Data models
│   ├── Budget.java       # Model untuk anggaran
│   ├── Transaction.java  # Model untuk transaksi
│   └── User.java         # Model untuk pengguna
├── service/              # Business logic layer
│   ├── BudgetService.java
│   ├── DashboardService.java
│   ├── LoginService.java
│   └── TransactionService.java
├── controller/           # Controller layer
│   ├── BudgetController.java
│   ├── DashboardController.java
│   ├── LoginController.java
│   └── TransactionController.java
├── repository/           # Data access layer
│   └── CSVRepository.java
├── util/                 # Utilities
│   ├── DateUtil.java
│   ├── SecurityUtil.java
│   └── SessionManager.java
├── GUI/                  # User interface
│   ├── Button.java       # Custom button component
│   ├── LandingPage.java  # Login/Register page
│   ├── MainGUI.java      # Main application window
│   └── RoundedPanel.java # Custom panel component
├── data/                 # Data storage (auto-generated)
│   ├── transactions.csv
│   ├── users.csv
│   └── budgets.csv
├── Main.java             # Application entry point
└── README.md             # This file
💻 Persyaratan Sistem
Minimum Requirements
Java Development Kit (JDK) 8 atau lebih tinggi

RAM: 512 MB minimum

Storage: 50 MB free space

OS: Windows 7+, macOS 10.9+, atau Linux

Direkomendasikan
Java Development Kit (JDK) 11+

RAM: 1 GB

Storage: 100 MB free space

Screen Resolution: 1366x768 atau lebih tinggi

Verifikasi Instalasi Java
bash
# Cek versi Java
java -version

# Cek instalasi Java Compiler
javac -version
🚀 Cara Menjalankan
Metode 1: Menggunakan IDE (Rekomendasi)
IntelliJ IDEA
Clone atau download proyek

Buka IntelliJ IDEA → File → Open

Pilih folder proyek

Tunggu hingga indexing selesai

Cari file Main.java di root package

Klik kanan → Run 'Main.main()'

Eclipse
Buka Eclipse → File → Import

Pilih "Existing Projects into Workspace"

Browse ke folder proyek

Finish import

Cari file Main.java

Klik kanan → Run As → Java Application

Metode 2: Menggunakan Command Line
Windows
batch
rem Navigate ke direktori proyek
cd path\to\PersonalFinanceManager

rem Kompilasi semua file
javac -d . model/*.java service/*.java controller/*.java repository/*.java util/*.java GUI/*.java Main.java

rem Jalankan aplikasi
java Main
Linux/macOS
bash
# Navigate ke direktori proyek
cd path/to/PersonalFinanceManager

# Kompilasi semua file
javac -d . model/*.java service/*.java controller/*.java repository/*.java util/*.java GUI/*.java Main.java

# Jalankan aplikasi
java Main
Metode 3: Menggunakan Script Otomatis
run.sh (Linux/macOS)
bash
#!/bin/bash
echo "Compiling Personal Finance Manager..."
javac -d . model/*.java service/*.java controller/*.java repository/*.java util/*.java GUI/*.java Main.java

if [ $? -eq 0 ]; then
echo "Compilation successful! Starting application..."
java Main
else
echo "Compilation failed. Please check for errors."
fi
run.bat (Windows)
batch
@echo off
echo Compiling Personal Finance Manager...
javac -d . model/*.java service/*.java controller/*.java repository/*.java util/*.java GUI/*.java Main.java

if %errorlevel% equ 0 (
echo Compilation successful! Starting application...
java Main
) else (
echo Compilation failed. Please check for errors.
)
pause
📖 Panduan Penggunaan
1. Registrasi Akun Baru
   Jalankan aplikasi

Klik "Don't have an account? Register"

Isi form registrasi:

Username: Minimal 3 karakter (huruf, angka, underscore)

Password: Minimal 4 karakter

Confirm Password: Harus sama dengan password

Klik "Register"

2. Login ke Aplikasi
   Masukkan username dan password

Klik tombol "Login"

Setelah berhasil, Anda akan diarahkan ke Dashboard

3. Dashboard Utama
   Informasi yang ditampilkan:

👋 Salam pengguna

💰 Sisa saldo bulan ini

📈 Total pemasukan

📉 Total pengeluaran

🎯 Target tabungan (jika ada)

📋 Daftar transaksi terbaru

4. Menambah Transaksi
   Di Dashboard, klik tombol "Add Transaction"

Isi form:

Description: Deskripsi transaksi

Category: Kategori transaksi (makanan, transportasi, dll)

Type: Income (pemasukan) atau Expense (pengeluaran)

Amount: Jumlah uang

Klik OK untuk menyimpan

5. Melihat Riwayat Transaksi
   Klik menu "History Transaction" di header

Gunakan filter untuk mencari transaksi:

This Month: Transaksi bulan ini

By Month: Pilih bulan dan tahun spesifik

By Date Range: Tentukan rentang tanggal

By Category: Filter berdasarkan kategori

6. Mengelola Anggaran (Budget)
   Di Dashboard, cari section "Budget Warnings"

Untuk menambah budget:

Klik tombol "+ Add Target" (fitur dalam pengembangan)

Sistem akan otomatis menampilkan peringatan jika pengeluaran mendekati limit

7. Update/Delete Transaksi
   Di Dashboard, klik tombol "Update" atau "Delete"

Masukkan ID transaksi (bisa dilihat di tabel)

Untuk update: isi data baru

Untuk delete: konfirmasi penghapusan

8. Logout
   Klik menu "Profile"

Klik tombol "Logout"

Anda akan kembali ke halaman login

📊 Format Data
File CSV Structure
1. users.csv
   Format: username,password

text
john_doe,password123
jane_smith,securepass456
2. transactions.csv
   Format: id,username,type,category,amount,date,description

text
001,john_doe,INCOME,Salary,5000000,2024-01-15,Gaji bulan Januari
002,john_doe,EXPENSE,Food,75000,2024-01-16,Makan siang resto
003,jane_smith,INCOME,Freelance,2500000,2024-01-10,Project website
3. budgets.csv
   Format: username,category,month,year,limit

text
john_doe,Food,1,2024,1000000
john_doe,Transportation,1,2024,500000
jane_smith,Entertainment,1,2024,300000
Kategori Transaksi Bawaan
Income: Salary, Freelance, Investment, Bonus, Other

Expense: Food, Transportation, Entertainment, Shopping, Bills, Healthcare, Education, Other

🔧 Troubleshooting
Masalah Umum
1. "Error: Could not find or load main class"
   Penyebab: Classpath tidak sesuai atau kompilasi gagal
   Solusi:

bash
# Pastikan kompilasi berhasil
javac -d . Main.java

# Cek apakah file .class sudah dibuat
ls *.class
2. GUI Tidak Muncul
   Penyebab: Java versi lama atau masalah display
   Solusi:

bash
# Update Java
sudo apt update && sudo apt install default-jre

# Atau coba dengan menonaktifkan hardware acceleration
java -Dsun.java2d.opengl=false Main
3. File CSV Tidak Terbaca
   Penyebab: Permission denied atau file corrupt
   Solusi:

bash
# Berikan permission
chmod 644 data/*.csv

# Atau hapus file untuk dibuat ulang
rm data/*.csv
4. Tombol Tidak Berfungsi
   Penyebab: Event listener error atau null pointer
   Solusi:

Restart aplikasi

Cek log error di console

Pastikan data CSV format benar

Log Error dan Debug
Aktifkan debug mode dengan menambahkan flag:

bash
java -Ddebug=true Main
Log akan ditampilkan di:

Windows: Command Prompt

Linux/macOS: Terminal

🚧 Fitur dalam Pengembangan
Versi Mendatang (Roadmap)
v1.1 - Export/Import
Export data ke Excel/PDF

Import dari bank statement

Backup/restore data

v1.2 - Enhanced Analytics
Chart visualisasi

Perbandingan bulanan

Prediksi pengeluaran

v1.3 - Multi-user Features
Family/shared budgets

Role-based access

Collaboration tools

v1.4 - Mobile Companion
Android app

Sync dengan desktop

Push notifications

Cara Berkontribusi
Fork repository

Buat branch fitur (git checkout -b feature/amazing-feature)

Commit changes (git commit -m 'Add amazing feature')

Push ke branch (git push origin feature/amazing-feature)

Buat Pull Request

