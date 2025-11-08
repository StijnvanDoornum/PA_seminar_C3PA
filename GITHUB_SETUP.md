# GitHub Repository Setup Guide

## Je hebt 2 opties:

### Optie 1: Push naar de originele repository (als je toegang hebt)

Als je contributor bent op de originele repo:

```bash
cd /Users/stijnvandoornum/Documents/tu/Seminar/ConformanceCheckingUsingTries-1
git push origin main
```

### Optie 2: Maak je eigen repository (Aanbevolen)

#### Stap 1: Maak een nieuwe GitHub repository

1. Ga naar https://github.com/new
2. Kies een naam, bijvoorbeeld: `ConformanceCheckingTries-Experiments`
3. Maak het **public** of **private** (jouw keuze)
4. **NIET** initialiseren met README, .gitignore of license (we hebben die al!)
5. Klik op "Create repository"

#### Stap 2: Wijzig de remote origin

Vervang `JOUW-USERNAME` en `JOUW-REPO-NAME` met jouw gegevens:

```bash
cd /Users/stijnvandoornum/Documents/tu/Seminar/ConformanceCheckingUsingTries-1

# Verwijder de oude origin
git remote remove origin

# Voeg jouw nieuwe repository toe
git remote add origin https://github.com/JOUW-USERNAME/JOUW-REPO-NAME.git

# Push alles naar jouw repository
git push -u origin main
```

#### Stap 3: Verifieer dat het werkt

Ga naar jouw GitHub repository in de browser en je zou alle files moeten zien!

## Wat is er gecommit?

Je laatste commit bevat:
- ✅ **SimpleXESReader.java** - Custom XES parser
- ✅ **XESRunner.java** - BPI2015 experiment runner  
- ✅ **SimpleTest.java** - Standalone demo
- ✅ **EXPERIMENT_RESULTS.md** - Resultaten documentatie
- ✅ **SETUP_STATUS.md** - Technische setup details
- ✅ **run-bpi2015.sh** - Convenience script
- ✅ **Updated pom.xml** - Maven configuratie
- ✅ **Updated Runner.java** - Dataset paths

## Wat is NIET gecommit?

- ❌ `target/` directory (build artifacts) - in .gitignore
- ❌ `lib/*.jar` files - te groot voor Git (zie opmerking hieronder)
- ❌ IDE specifieke files

## Belangrijke Opmerking over Dependencies

De `lib/` directory met JAR files is NIET in git opgenomen omdat:
1. Deze files zijn te groot voor GitHub (58 JARs, ~100MB)
2. Ze zijn beschikbaar via de originele repository of ProM

### Als anderen je code willen gebruiken:

Voeg een **DEPENDENCIES.md** file toe met instructies:

```markdown
# Dependencies

Download de volgende JAR files en plaats ze in de `lib/` directory:
- OpenXES-20181205.jar
- ProM-Framework-6.10.110.jar
- ProM-Models-latest.jar
- [... etc]

Of clone de originele repository:
git clone https://github.com/MaxTNielsen/ConformanceCheckingUsingTries.git
```

## Volgende keer wijzigingen pushen

Na nieuwe wijzigingen:

```bash
cd /Users/stijnvandoornum/Documents/tu/Seminar/ConformanceCheckingUsingTries-1

# Bekijk wijzigingen
git status

# Voeg alle wijzigingen toe
git add .

# Commit met beschrijving
git commit -m "Beschrijving van je wijzigingen"

# Push naar GitHub
git push origin main
```

## Troubleshooting

### "Permission denied" error bij pushen?

Je moet je GitHub credentials configureren:

```bash
# Stel je git identiteit in
git config --global user.name "Jouw Naam"
git config --global user.email "jouw.email@example.com"

# Voor HTTPS (makkelijkst):
# GitHub zal om een Personal Access Token vragen
# Maak deze aan via: https://github.com/settings/tokens
```

### Wil je een specifieke branch pushen?

```bash
# Maak een nieuwe branch
git checkout -b experiment-branch

# Push de nieuwe branch
git push -u origin experiment-branch
```

## GitHub Features die je kunt gebruiken

1. **README.md** - Voeg experimentele resultaten toe
2. **Issues** - Track bugs of verbeteringen
3. **Releases** - Tag belangrijke versies
4. **Actions** - Automatisch testen (optioneel)
5. **Wiki** - Uitgebreide documentatie

## Samenvatting van je wijzigingen

Je hebt succesvol toegevoegd:
- 857+ regels nieuwe code
- Complete BPI2015 experiment pipeline
- Werkende conformance checking met 62% perfecte matches
- Uitgebreide documentatie

**Klaar om te delen met de wereld! 🚀**

