Panduan singkat menjalankan Java di proyek ini

1) Struktur sumber yang benar
   - Letakkan file Java di `src/main/java`.
   - Jika menggunakan package `com.example`, gunakan path `src/main/java/com/example/Greet.java` dan tambahkan `package com.example;` di awal file.

2) Compile & Run dari terminal

Compile semua sumber ke folder `out`:

```bash
javac -d out $(find src -name "*.java")
```

Jalankan kelas (tanpa package):

```bash
java -cp out HelloWorld
```

Jalankan kelas dengan package:

```bash
java -cp out com.example.Greet
```

3) Menggunakan IntelliJ IDEA
   - Pastikan `Project SDK` diset (File > Project Structure > Project).
   - Right-click pada folder `src/main/java` dan pilih `Mark Directory as > Sources Root`.
   - Buat class baru lewat `New > Java Class` atau tambahkan file di folder source.
   - Jalankan dengan klik kanan di editor > `Run 'MainClass.main()'`.

4) Troubleshooting singkat
   - Pesan error `class X is public, should be declared in a file named X.java` -> ubah nama file sesuai public class.
   - Jika `NoClassDefFoundError`, pastikan classpath (`-cp`) menunjuk ke direktori hasil kompilasi.

