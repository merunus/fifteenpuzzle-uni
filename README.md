# Fifteen Puzzle – Android Game

A simple implementation of the classic **15 Puzzle** game using **Jetpack Compose** in Kotlin.

---

## 🎮 Features

- 4x4 board with tiles from 1 to 15 and one empty space.
- Tiles can be moved horizontally or vertically into the empty cell.
- Automatic detection of winning state.
- Button "New Game" that generates a guaranteed solvable board.
- Move counter with real-time update.
- Timer showing total time in MM:SS format.
- Smooth tile movement animation.
- **"Almost Win"** button for quick win testing.

---

## 🧪 Testing

Manual testing done using Android Emulator.  
The "Almost Win" button allows instant testing of win condition.

---

## 📱 Technologies Used

- **Kotlin**
- **Jetpack Compose**
- `remember`, `mutableStateOf`, `LaunchedEffect`
- `animateDpAsState` for smooth movement
- Coroutines for the timer

---

## 📦 How to Run

1. Clone the repo:
```bash
git clone git@github.com:merunus/fifteenpuzzle-uni.git
