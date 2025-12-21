#!/usr/bin/env bash
set -euo pipefail

ROOT=$(cd "$(dirname "$0")" && pwd)
cd "$ROOT"

echo "Meng-compile semua sumber ke folder out/"
rm -rf out
mkdir -p out
javac -d out $(find src -name "*.java")

echo "Menjalankan HelloWorld"
java -cp out HelloWorld

echo "Menjalankan com.example.Greet"
java -cp out com.example.Greet

echo "Selesai"

