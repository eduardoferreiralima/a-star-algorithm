
# 🛡️ IFRN - Tactical Pathfinding Benchmarker

Um simulador tático de busca de caminho desenvolvido em Java para a disciplina de Inteligência Artificial. O projeto compara o desempenho entre buscas ótimas e buscas gulosas em um ambiente de grid dinâmico com obstáculos.

## 🚀 Funcionalidades

* **Benchmarking de Algoritmos**: Compare em tempo real o **A* (Ótimo)** vs **Greedy Search (Guloso)**.
* **Métrica de Performance**: Medição precisa do tempo de processamento em microssegundos () exibida em um relatório tático após cada operação.
* **Visualização Progressiva**: Animação detalhada dos nós em aberto (`OPEN`), nós explorados (`CLOSED`) e o caminho final (`PATH`).
* **Edição Dinâmica**: Ferramentas para desenhar paredes, reposicionar o Herói (Start) e o Alvo (End).
* **Escala Inteligente**: Slider para ajuste de resolução do grid, permitindo testar o estresse dos algoritmos em mapas de grande escala.
* **Geração de Cenários**: Gerador de labirintos aleatórios para testes de complexidade de caminho.

## 🧠 Algoritmos Implementados

* **A* Search**: Garante o caminho mais curto utilizando , equilibrando custo real e distância estimada.
* **Greedy Best-First Search**: Foca puramente na velocidade através da heurística , ideal para caminhos rápidos em mapas abertos.

## 🛠️ Tecnologias

* **Linguagem**: Java 21
* **Interface**: Swing (Java GUI) com renderização de sprites e transparência.
* **Arquitetura**: MVC (Model-View-Controller) com separação estrita de classes de algoritmos.
* **Gerenciador de Dependências**: Maven

## 📁 Estrutura do Projeto

* `ifrn.edu.eduardo.algorithm`: Classes independentes para cada algoritmo de busca.
* `ifrn.edu.eduardo.model`: Classe `Node` que gerencia estados de busca e carregamento de assets.
* `ifrn.edu.eduardo.view`: JFrame principal com painel lateral de controle e grid de renderização.
* `src/main/resources`: Sprites customizados (`heroi.png`, `criminoso.png`, `parede.png`, etc.).

## 🎮 Como Executar

1. Certifique-se de ter o **JDK 21** instalado.
2. Clone o repositório.
3. Compile e execute via terminal ou sua IDE de preferência:
```bash
mvn clean compile exec:java -Dexec.mainClass="ifrn.edu.eduardo.Main"

```
