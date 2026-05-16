#!/usr/bin/env python3
"""
nutrix_firebase_setup.py
Script de configuração automática do Firebase para o Nutrix Hospital OS.

COMO USAR:
1. Instale a Firebase CLI: npm install -g firebase-tools
2. Instale dependências: pip install requests
3. Faça login na CLI do Firebase: firebase login
4. Execute: python nutrix_firebase_setup.py
5. Copie as credenciais exibidas para src/firebase/FirebaseConfig.java
"""

import subprocess
import json
import sys

PROJECT_ID = "nutrix-hospital-os"
ADMIN_EMAIL = "admin@nutrix.com"
ADMIN_PASS = "Nutrix@2025"
MEDICO_EMAIL = "medico@nutrix.com"
MEDICO_PASS = "Nutrix@2025"

def run(cmd, capture=True):
    r = subprocess.run(cmd, shell=True, capture_output=capture, text=True)
    return r.stdout.strip(), r.returncode

def step(msg): print(f"\n{'='*50}\n✅ {msg}\n{'='*50}")
def info(msg): print(f"   ℹ {msg}")
def err(msg): print(f"   ❌ {msg}"); sys.exit(1)

step("1. Verificando Firebase CLI")
out, code = run("firebase --version")
if code != 0:
    err("Firebase CLI não encontrada. Instale com: npm install -g firebase-tools")
info(f"Firebase CLI: {out}")

step("2. Verificando login Firebase")
out, code = run("firebase projects:list --json")
if code != 0:
    err("Não logado. Execute: firebase login")
info("Login OK")

step("3. Criando projeto Firebase")
run(f"firebase projects:create {PROJECT_ID} --display-name='Nutrix Hospital OS'", capture=False)
info(f"Projeto: {PROJECT_ID}")

step("4. Habilitando Authentication (via REST API)")
info("⚠ Auth deve ser habilitado manualmente no console:")
info(f"→ https://console.firebase.google.com/project/{PROJECT_ID}/authentication")
info("→ Clique 'Get started' → Enable Email/Password")

step("5. Obtendo Web API Key")
out, _ = run(f"firebase apps:list --project {PROJECT_ID} --json")
# Fallback: pegar do console
api_key = input("\n   Cole aqui a Web API Key do projeto (Project Settings → Web API Key): ").strip()
if not api_key:
    err("API Key não fornecida.")

step("6. Gerando FirebaseConfig.java atualizado")
config_java = f'''package firebase;

/**
 * Nutrix Hospital OS — FirebaseConfig (GERADO AUTOMATICAMENTE).
 * Projeto: {PROJECT_ID}
 */
public final class FirebaseConfig {{

    private FirebaseConfig() {{}}

    public static final String WEB_API_KEY = "{api_key}";
    public static final String PROJECT_ID  = "{PROJECT_ID}";

    public static final String AUTH_URL =
        "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + WEB_API_KEY;

    public static final String FIRESTORE_URL =
        "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/";

    public static boolean isConfigured() {{
        return true; // Gerado automaticamente
    }}
}}
'''

with open("src/firebase/FirebaseConfig.java", "w", encoding="utf-8") as f:
    f.write(config_java)
info("✅ FirebaseConfig.java atualizado!")

step("7. Próximos passos manuais")
info(f"1. Acesse https://console.firebase.google.com/project/{PROJECT_ID}/authentication/users")
info(f"2. Crie o usuário admin: {ADMIN_EMAIL} | {ADMIN_PASS}")
info(f"3. Crie o usuário medico: {MEDICO_EMAIL} | {MEDICO_PASS}")
info(f"4. Para cada usuário, copie o UID")
info(f"5. No Firestore, crie coleção 'users' com documentos:")
info(f"   {{UID_admin}}  → displayName: 'Administrador', role: 'admin'")
info(f"   {{UID_medico}} → displayName: 'Dr. Nutrix', role: 'medico'")
info(f"6. Recompile: javac -d out/production -sourcepath src src/**/*.java Main.java")
info(f"7. Execute: java -cp out/production Main")

print("\n✅ Configuração concluída! Siga os próximos passos acima.")
