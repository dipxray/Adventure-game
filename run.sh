#!/bin/bash

# Change the current directory to where the script is located
cd "$(dirname "$0")"

echo "Compiling game..."
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin @sources.txt

echo "Copying game assets..."
rsync -a --exclude="*.java" src/ bin/

echo "Starting game..."
java -cp bin main.Main
