# Mobile_exam

# Sudoku App – Examen Final

**Nombre:** *Alejandra Arredondo*  
**Matrícula:** *A017114434*  
**Plataforma:** **Android (Kotlin)**

---

## Descripción

Aplicación móvil desarrollada como parte del examen final.  
Permite generar, resolver y continuar puzzles de Sudoku consumiendo la API pública **Sudoku Generator de API Ninjas**.

Incluye:

- Generación de Sudokus 4x4 y 9x9  
- Dificultades: easy, medium, hard  
- Tablero interactivo  
- Verificación de solución  
- Guardado automático de la partida  
- Modo offline  
- Arquitectura MVVM + Clean Architecture  
- Inyección de dependencias (Hilt)  
- Manejo de estados

---

# Funcionalidades

## 1. Generación de Puzzles (API Ninjas)

El usuario puede generar Sudokus seleccionando:

- Tamaño: **4×4** o **9×9**
- Dificultad: **easy**, **medium**, **hard**

Se utiliza el endpoint:
GET https://api.api-ninjas.com/v1/sudokugenerate?width=4&height=4&difficulty=easy

X-Api-Key: YOUR_API_KEY


---

## 2. Tablero Interactivo

- Celdas iniciales bloqueadas (pistas del puzzle)
- Celdas editables con selector de números
- Tablero responsivo 
- Diferenciación visual entre celdas fijas y editables

---

## 3. Verificación de Solución

El usuario puede verificar su progreso:

- Si coincide con la solución entregada por la API → mensaje de éxito
- Si no coincide → mensaje de error   
- Se permite continuar jugando después de la verificación

---

## 4. Control del Puzzle Actual

- **Reiniciar** puzzle (solo borra entradas del usuario)  
- **Nuevo Sudoku** sin cerrar la app  
- Persistencia del progreso  
- Diseño consistente y claro

---

## 5. Guardado y Carga de Partidas

La aplicación guarda automáticamente:

- Puzzle base  
- Progreso actual del usuario  
- Solución del API  
- Tamaño del tablero  
- Dificultad  

Al iniciar:

- Si existe una partida guardada → se carga automáticamente  
- Todo funciona correctamente incluso sin conexión  

---

## 6. Modo Offline

Una vez cargado un puzzle:

- Se puede continuar sin conexión  
- La app no falla si no hay internet  
- Al generar un nuevo puzzle sin conexión:
  - Se muestra un mensaje claro  
  - El usuario puede seguir jugando el puzzle actual  

---

## 7. Manejo de Estados

La UI comunica claramente:

- **Cargando:** mensaje de cargando 
- **Éxito:** tablero mostrado  
- **Error:** diálogo con mensaje claro y botón “Cerrar” o “Reintentar”  

---

# Arquitectura

El proyecto implementa **MVVM + Clean Architecture** con separación estricta de capas:


