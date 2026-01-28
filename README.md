# 🛡️ IFRN - A* Tactical Solver

Um simulador tático de busca de caminho (pathfinding) desenvolvido em Java, utilizando o algoritmo **A*** para encontrar a rota mais eficiente entre um herói e um alvo em um ambiente de grid dinâmico.



## 🚀 Funcionalidades

* **Algoritmo A* Real-time**: Visualização detalhada do processo de busca (nós explorados e fronteiras).
* **Edição Dinâmica**: Altere a posição do herói, do criminoso ou desenhe obstáculos em tempo real.
* **Escala Inteligente**: Ajuste o tamanho do mapa proporcionalmente através de um slider, suportando desde grids simples até mapas complexos.
* **Geração de Labirintos**: Algoritmo aleatório para criação de cenários de teste.
* **Interface Estilizada**: UI em tons escuros (dark mode) com sprites customizados para paredes, grama e personagens.

## 🛠️ Tecnologias

* **Linguagem**: Java 21
* **Interface**: Swing (Java GUI)
* **Gerenciador de Dependências**: Maven

## 📁 Estrutura do Projeto

* `src/main/java/ifrn/edu/eduardo/algorithm`: Lógica matemática e heurística do A*.
* `src/main/java/ifrn/edu/eduardo/model`: Definição dos nós e gerenciamento de sprites.
* `src/main/java/ifrn/edu/eduardo/view`: Interface gráfica e manipulação de eventos.
* `src/main/resources`: Assets visuais (PNGs).

## 🎮 Como Executar

1. Certifique-se de ter o **JDK 21** instalado.
2. Clone o repositório.
3. Execute a classe `Main.java` ou utilize o Maven:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="ifrn.edu.eduardo.Main"