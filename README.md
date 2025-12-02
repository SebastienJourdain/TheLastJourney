# The Last Journey
Un jeu d’action-aventure en Java

The Last Journey est un jeu en 2D isométrique développé en Java 17, basé sur LWJGL 3. Il propose un système complet d’acteurs, d’IA, de combats, d’effets visuels, d’inventaires, de projectiles et de niveaux structurés. Le moteur du jeu est entièrement fait maison : rendu, animation, collisions, interface et logique de gameplay.

---

## 🚀 Fonctionnalités principales

### 🎮 Gameplay
- Déplacements, combat au corps-à-corps et à distance  
- Système de projectiles (flèches, boules de feu, attaques spéciales)  
- Gestion des dégâts, collisions, points de vie et effets temporaires  
- Inventaire, objets, équipements, améliorations  
- Intéractions avec des éléments du décor  
- PNJ variés : Gobelin, Orc, Minotaure, Nécromancien, Boss…

### 🧠 Intelligence Artificielle
- Arbres de comportement (Behaviour Trees)  
  - gestion des cooldowns  
  - attaques multiples  
  - poursuite intelligente  
  - régénération limitée avec effets visuels

### 🗺️ Système de niveaux
- Salles connectées, transitions, portes, fin de niveau  
- Gestion complète via `Level` et `Room`  
- Chargement des assets depuis `assets/`

### 🎨 Interface utilisateur
- Menus complets :  
  - Menu principal  
  - InGame HUD  
  - Inventaire  
  - Choix du personnage  
  - Marchand  
- Police personnalisée via `UIFontLoader`  
- Boutons et widgets maison (`UIButton`, `UIElement`, etc.)

### 📦 Architecture
- Projet Maven Java 17
- Swing (Rendu)
- GLFW (Gestion des inputs manette) 
- Organisation claire par modules :  
  - `Objects/` — acteurs, ennemis, projectiles, effets  
  - `AI/` — arbres de décision, IA personnalisées  
  - `Level/` — cartes, salles, environnement  
  - `UI/` — interface graphique et UX  
  - `Utilitary/` — outils divers (vecteurs, gamepad…)

---

## 🧩 Arborescence du projet

Structure simplifiée :

```
.
├── pom.xml
├── Main.java
├── assets/               # Ressources du jeu (images, sprites…)
├── AI/                   # IA (Behaviour Trees)
├── Level/                # Niveaux, salles, logique de progression
├── Objects/              # Joueurs, ennemis, projectiles, décor
├── UI/                   # Menus, interface, HUD
└── Utilitary/            # Classes utilitaires (vecteur, gamepad…)
```

Plus de 140 classes organisent l’ensemble du moteur.

---

## 🔧 Installation

### Prérequis
- Java **17** ou plus récent  
- Maven installé sur votre système  
- Une machine Windows, Linux ou macOS (le projet embarque toutes les natives LWJGL)

---

## ▶️ Compilation et lancement

### Compiler
```bash
mvn clean package
```

Cette commande génère :
- un JAR classique  (original-TheLastJourney...)
- un **shaded JAR** contenant toutes les dépendances (TheLastJourney...)

### Lancer le jeu
```bash
java -jar bin/TheLastJourney-1.0-SNAPSHOT.jar
```

Grâce au JAR shaded, aucune installation supplémentaire n’est nécessaire.

---

## 🧪 Tests unitaires
Les tests sont réalisés avec **JUnit 4** (déjà inclus dans `pom.xml`).  
Vous pouvez les exécuter via :

```bash
mvn test
```

---

## 🛠️ Outils et technologies
- **Java 17**
- **Swing** (Pour le rendu graphique)
- **LWJGL 3.3.3** (GLFW pour la gestion des manettes)
- **Maven**
- **JUnit 4 & 5**

---

## 📝 Notes complémentaires
Ce projet est conçu pour être extensible : l’ajout d’ennemis, d’architectures IA, de projectiles ou de salles ne nécessite que peu de modifications grâce à la structure modulaire.
