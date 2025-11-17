# Sistema de Gerenciamento de Biblioteca

Sistema de biblioteca em Java com interface gráfica Swing — Trabalho Acadêmico

## Sobre
Projeto Java (Swing) para gerenciamento de usuários, livros e empréstimos. Contém telas diferentes por tipo de usuário e salva dados em arquivos `.txt`.

## Requisitos
- NetBeans IDE 8.2 ou superior  
- JDK 8 ou superior

## Como abrir no NetBeans (passo a passo)
1. Abra o NetBeans.  
2. File → New Project → Categories: Java → Projects: Java Application → Next.  
3. Configure:
    - Project Name: `BibliotecaJava`
    - Project Location: escolha uma pasta
    - Marque: Create Main Class
    - Main Class: `Main`  
    Clique em Finish.
4. Adicionar classes:
    - No painel Projects, clique com o botão direito em Source Packages → `<default package>` → New → Java Class.
    - Crie as classes (nomes sem `.java`):
      - `Usuario`, `Administrador`, `Bibliotecario`, `Leitor`
      - `GerenciadorUsuarios`, `Livro`, `GerenciadorLivros`
      - `TelaLogin`, `TelaCadastro`, `TelaAdmin`, `TelaBibliotecario`, `TelaLeitor`
    - Substitua o código gerado pelo código fornecido no trabalho.
5. Atualizar `Main.java`:
    - Abra `Main.java`, apague o conteúdo padrão e cole o código do Main fornecido.
6. Executar:
    - Clique com o botão direito no projeto `BibliotecaJava` → Run (ou F6). A tela de login deve aparecer.

## Estrutura do projeto
BibliotecaJava
└── Source Packages
     └── <default package>
          ├── Main.java
          ├── Usuario.java
          ├── Administrador.java
          ├── Bibliotecario.java
          ├── Leitor.java
          ├── GerenciadorUsuarios.java
          ├── Livro.java
          ├── GerenciadorLivros.java
          ├── TelaLogin.java
          ├── TelaCadastro.java
          ├── TelaAdmin.java
          ├── TelaBibliotecario.java
          └── TelaLeitor.java  
Total: 13 arquivos `.java`

## Login padrão (para testes)
- Username: `admin`  
- Senha: `admin`

## Arquivos gerados automaticamente
Ao executar, o sistema cria na pasta do projeto:
- `usuarios.txt` — usuários cadastrados  
- `livros.txt` — livros cadastrados  
- `historico_emprestimos.txt` — histórico de empréstimos

## Problemas comuns e soluções
- Erro: "cannot find symbol"  
  Solução: verifique se todas as 13 classes foram criadas e os nomes estão corretos.
- Erro: JDK não encontrado  
  Solução: Tools → Java Platforms → Add Platform → selecione o JDK instalado.
- Interface não aparece  
  Solução: execute novamente (F6) ou reinicie o NetBeans.

## Requisitos do trabalho atendidos
- ✓ Tela inicial de login (usuário e senha)  
- ✓ Telas diferentes para cada tipo de usuário  
- ✓ Cadastro de 3 tipos de usuários  
- ✓ Saída na tela (JOptionPane) e em arquivos (`.txt`)  
- ✓ Uso de Swing (`JFrame`, `JPanel`, `JButton`, etc.)  
- ✓ Uso de `String` (`equals`, `trim`, `length`, `split`)  
- ✓ Classe abstrata (`Usuario`)  
- ✓ Polimorfismo (`Administrador`, `Bibliotecario`, `Leitor`)  
- ✓ Interfaces (`ActionListener`)

## Autores
- Enzo Feltrin  
- Miguel Henrique  
- Gabrielle Pavani  
- Aline Abdalla  
- Pedro Henrique

Data de entrega: 17/11/2025
