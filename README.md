# 🧹 Lazy Secret Remover

A simple, GUI-based tool to remove hardcoded secrets and sensitive files from a Git repository.  
No manual BFG or filter-branch fiddling needed. Just point, click, and clean!

---

## 🚀 Features

- Clone your Git repo safely (backup is created automatically)
- Remove text-based secrets across the entire Git history
- Delete sensitive files (like `.env`, `id_rsa`) from all commits
- Clean, minimal interface

---

## 📦 Requirements

- **Java 21 or newer** installed and properly configured  
  👉 You can check your version by running `java -version`  
  👉 [Download Java 21 (OpenJDK)](https://jdk.java.net/21/)

- **[BFG Repo-Cleaner](https://rtyley.github.io/bfg-repo-cleaner/)**  
  👉 Download the `bfg.jar` file and place it in the **same folder** as this application  
  👉 Make sure to **rename it to `bfg.jar`** if it's not already

---

## 🧑‍💻 How to Use

### 0. Run the Application
- Launch the tool using the command:
  ```bash
  java -jar lazySecretRemover.jar

### 1. Clone the Repository
- Paste your Git repository URL into the top input field
- Click **“Clone Repo”**
- ✅ A backup copy of your repository will be created for safety

### 2. Remove Text-Based Secrets
- Paste or type any secret strings (e.g., passwords, API keys) into the **“Remove Text Secrets”** box
- Click **“Remove Text Secrets”**
- 🔐 Secrets will be replaced with `***REMOVED***` in the entire Git history

### 3. Remove Files from History
- Type the names of files you want to permanently delete (e.g., `.env`, `id_rsa`) in the **“Remove Files from history”** box
- ❗ Use only file names — **do not include paths**
- Click **“Remove Files from history”**

### 4. Done!
- Your repository is now cleaned up and safe to push
- 🎉 Go enjoy your life!

---

## 👨‍🎨 Author

Created by **Pedro Camata Andreon**

---

## 📝 License

MIT License (or your preferred license)
