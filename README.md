# Task 8.1C - LLM ChatBot

An Android application that allows users to log in and interact with a Large Language Model. This project connects to a local instance of the Qwen 2.5 3B model via Ollama and features chat history by user using a Room SQLite Database.

## Features
* **User Authentication:** Simple login screen where users enter a username to access their specific chat history.
* **AI Integration:** Real-time chat interface connected to a local LLM using OkHttp.
* **Persistent Chat History:** Messages are permanently saved using a Room database and automatically load when the user logs back in.
* **User-Specific Data:** The database filters chat history so "Alice" and "Bob" have completely separate, private conversation logs.
* **Modern UI:** Chat bubbles styled with distinct alignments (User right, AI left) and real-time timestamps.

## How to Run the Project Locally

Because this app connects to a local AI server rather than a cloud API, you must configure your local network to allow the Android Emulator to communicate with your computer.

### 1. Prerequisites
* Install **Android Studio**.
* Install **Ollama** on your computer (from [ollama.com](https://ollama.com)).
* Download the Qwen model by opening your terminal and running:
  ollama run qwen2.5:3b
