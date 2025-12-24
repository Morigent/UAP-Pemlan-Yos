# 💰 Personal Finance Manager - Moneeeyyyyyy

![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

**Personal Finance Manager** adalah aplikasi desktop berbasis Java (Swing) yang dirancang untuk membantu pengguna melacak pemasukan, pengeluaran, serta mengelola anggaran bulanan dengan antarmuka yang modern dan intuitif.

---

## 📋 Daftar Isi
- [Fitur Utama](#-fitur-utama)
- [Teknologi](#%EF%B8%8F-teknologi-yang-digunakan)
- [Struktur Proyek](#-struktur-proyek)
- [Persyaratan Sistem](#-persyaratan-sistem)
- [Instalasi & Cara Menjalankan](#-instalasi--cara-menjalankan)
- [Panduan Penggunaan](#-panduan-penggunaan)
- [Format Data](#-format-data)
- [Troubleshooting](#-troubleshooting)
- [Roadmap](#-roadmap-pengembangan)

---

## ✨ Fitur Utama

### 🔐 Manajemen Akun
* Registrasi dan Login pengguna yang aman.
* Manajemen sesi (Multi-user support).

### 💳 Manajemen Transaksi
* **CRUD Transaksi:** Tambah, Edit, Hapus pemasukan dan pengeluaran.
* **Filter Canggih:** Cari transaksi berdasarkan Bulan, Tahun, Rentang Tanggal, Kategori, atau Tipe.
* **Import Data:** Dukungan import transaksi dari file eksternal.

### 📊 Dashboard & Analitik
* Ringkasan Saldo Terkini, Total Pemasukan, dan Pengeluaran.
* Visualisasi grafik pengeluaran per kategori.
* Peringatan otomatis (Alert) jika mendekati batas anggaran.

### 💰 Manajemen Anggaran (Budget)
* Atur limit anggaran spesifik per kategori.
* *Tracking* real-time pengeluaran vs anggaran.

### 🎨 Antarmuka (GUI)
* Desain modern dengan *rounded corners*.
* Warna indikator transaksi (Hijau = Masuk, Merah = Keluar).
* Layout responsif dan navigasi mudah.

---

## 🛠️ Teknologi yang Digunakan

| Komponen | Spesifikasi |
| :--- | :--- |
| **Bahasa Pemrograman** | Java JDK 8+ |
| **GUI Framework** | Java Swing (AWT/Swing) |
| **Database** | CSV Files (Text-based storage) |
| **Build Tool** | Native Java Compiler (`javac`) |
| **Utilities** | Java Time API (`java.time`) |

---

## 📁 Struktur Proyek

```text
PersonalFinanceManager/
├── model/                 # Data Models (POJO)
│   ├── Budget.java
│   ├── Transaction.java
│   └── User.java
├── service/               # Business Logic
│   ├── BudgetService.java
│   ├── DashboardService.java
│   ├── LoginService.java
│   └── TransactionService.java
├── controller/            # Controllers
│   ├── BudgetController.java
│   ├── DashboardController.java
│   ├── LoginController.java
│   └── TransactionController.java
├── repository/            # Data Access (CSV Handling)
│   └── CSVRepository.java
├── util/                  # Utilities
│   ├── DateUtil.java
│   ├── SecurityUtil.java
│   └── SessionManager.java
├── GUI/                   # User Interface
│   ├── MainGUI.java
│   ├── LandingPage.java
│   └── components/        # Custom Components
├── data/                  # Local Storage (Auto-generated)
│   ├── transactions.csv
│   ├── users.csv
│   └── budgets.csv
└── Main.java              # Entry Point