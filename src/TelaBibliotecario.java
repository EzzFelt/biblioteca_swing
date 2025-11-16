import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * TelaBibliotecario - Interface para o Bibliotecário
 * CORRIGIDO: Agora salva livros e empréstimos de verdade
 */
public class TelaBibliotecario extends JFrame {
    private Bibliotecario bibliotecario;
    private GerenciadorLivros gerenciadorLivros;
    private JTextArea areaPermissoes;
    private JTextArea areaRegistros;
    private JList<String> listaLivrosEmprestimo;
    private DefaultListModel<String> modeloListaLivros;
    private JTextField campoTitulo, campoAutor, campoAno;
    
    public TelaBibliotecario(Bibliotecario bibliotecario, GerenciadorLivros gerenciadorLivros) {
        this.bibliotecario = bibliotecario;
        this.gerenciadorLivros = gerenciadorLivros;
        
        setTitle("Sistema de Biblioteca - Bibliotecário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel painelSuperior = criarPainelSuperior();
        
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Minhas Permissões", criarAbaPermissoes());
        abas.addTab("Gerenciar Livros", criarAbaLivros());
        abas.addTab("Empréstimos", criarAbaEmprestimos());
        
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoSair = new JButton("Sair");
        botaoSair.addActionListener(e -> System.exit(0));
        painelInferior.add(botaoSair);
        
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(abas, BorderLayout.CENTER);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);
        
        add(painelPrincipal);
    }
    
    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Informações do Usuário"));
        
        JLabel labelInfo = new JLabel(
            "<html><b>Nome:</b> " + bibliotecario.getNome() + 
            "<br><b>Tipo:</b> " + bibliotecario.getTipoUsuario() +
            "<br><b>Setor:</b> " + bibliotecario.getSetor() +
            "<br><b>Descrição:</b> " + bibliotecario.getDescricao() + "</html>"
        );
        labelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        painel.add(labelInfo, BorderLayout.CENTER);
        return painel;
    }
    
    private JPanel criarAbaPermissoes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Suas permissões no sistema:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        areaPermissoes = new JTextArea();
        areaPermissoes.setEditable(false);
        areaPermissoes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder sb = new StringBuilder();
        String[] permissoes = bibliotecario.getPermissoes();
        for (int i = 0; i < permissoes.length; i++) {
            sb.append((i + 1) + ". " + permissoes[i] + "\n");
        }
        areaPermissoes.setText(sb.toString());
        
        JScrollPane scroll = new JScrollPane(areaPermissoes);
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        
        return painel;
    }
    
    /**
     * CORRIGIDO: Agora os campos mantêm tamanho fixo e livros são salvos
     */
    private JPanel criarAbaLivros() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Gerenciamento de Livros:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Painel com formulário - TAMANHO FIXO
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo Livro"));
        painelFormulario.setPreferredSize(new Dimension(750, 180)); // Tamanho fixo
        painelFormulario.setMinimumSize(new Dimension(750, 180));
        painelFormulario.setMaximumSize(new Dimension(750, 180));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel labelTitulo = new JLabel("Título:");
        painelFormulario.add(labelTitulo, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        campoTitulo = new JTextField();
        campoTitulo.setPreferredSize(new Dimension(300, 25));
        painelFormulario.add(campoTitulo, gbc);
        
        // Autor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel labelAutor = new JLabel("Autor:");
        painelFormulario.add(labelAutor, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        campoAutor = new JTextField();
        campoAutor.setPreferredSize(new Dimension(300, 25));
        painelFormulario.add(campoAutor, gbc);
        
        // Ano
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        JLabel labelAno = new JLabel("Ano:");
        painelFormulario.add(labelAno, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        campoAno = new JTextField();
        campoAno.setPreferredSize(new Dimension(300, 25));
        painelFormulario.add(campoAno, gbc);
        
        // Botão
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton botaoCadastrar = new JButton("Cadastrar Livro");
        botaoCadastrar.setPreferredSize(new Dimension(200, 30));
        painelFormulario.add(botaoCadastrar, gbc);
        
        // Área de mensagens - mostra livros cadastrados
        areaRegistros = new JTextArea();
        areaRegistros.setEditable(false);
        areaRegistros.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaRegistros.setText("===== LIVROS CADASTRADOS =====\n\n");
        
        // Carrega livros existentes
        atualizarListaLivrosCadastrados();
        
        JScrollPane scroll = new JScrollPane(areaRegistros);
        
        // Evento do botão cadastrar - AGORA SALVA DE VERDADE
        botaoCadastrar.addActionListener(e -> {
            String titulo_livro = campoTitulo.getText().trim();
            String autor = campoAutor.getText().trim();
            String ano = campoAno.getText().trim();
            
            if (titulo_livro.isEmpty() || autor.isEmpty() || ano.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // SALVA O LIVRO DE VERDADE
            boolean sucesso = gerenciadorLivros.cadastrarLivro(titulo_livro, autor, ano);
            
            if (sucesso) {
                String mensagem = "Livro cadastrado com sucesso!\n" +
                                 "Título: " + titulo_livro + "\n" +
                                 "Autor: " + autor + "\n" +
                                 "Ano: " + ano + "\n\n";
                areaRegistros.append(mensagem);
                
                // Limpa campos SEM alterar tamanho
                campoTitulo.setText("");
                campoAutor.setText("");
                campoAno.setText("");
                campoTitulo.requestFocus();
                
                JOptionPane.showMessageDialog(this,
                    "Livro cadastrado e salvo com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar livro!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(painelFormulario, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        
        return painel;
    }
    
    /**
     * Atualiza a lista de livros cadastrados na área de texto
     */
    private void atualizarListaLivrosCadastrados() {
        ArrayList<Livro> livros = gerenciadorLivros.getLivros();
        areaRegistros.setText("===== LIVROS CADASTRADOS (" + livros.size() + ") =====\n\n");
        
        for (Livro livro : livros) {
            areaRegistros.append(livro.toString() + "\n");
        }
        
        areaRegistros.append("\n" + "=".repeat(50) + "\n");
    }
    
    /**
     * CORRIGIDO: Agora empréstimos e devoluções são salvos de verdade
     */
    private JPanel criarAbaEmprestimos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Registrar Empréstimos e Devoluções:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Painel para selecionar livro
        JPanel painelSelecao = new JPanel(new BorderLayout(10, 10));
        painelSelecao.setBorder(BorderFactory.createTitledBorder("Selecione um Livro"));
        
        modeloListaLivros = new DefaultListModel<>();
        listaLivrosEmprestimo = new JList<>(modeloListaLivros);
        atualizarListaLivrosEmprestimo();
        
        JScrollPane scrollLista = new JScrollPane(listaLivrosEmprestimo);
        scrollLista.setPreferredSize(new Dimension(700, 150));
        
        JButton botaoAtualizar = new JButton("Atualizar Lista");
        botaoAtualizar.addActionListener(e -> atualizarListaLivrosEmprestimo());
        
        painelSelecao.add(scrollLista, BorderLayout.CENTER);
        painelSelecao.add(botaoAtualizar, BorderLayout.SOUTH);
        
        // Painel de ações
        JPanel painelAcoes = new JPanel(new GridLayout(3, 2, 10, 10));
        painelAcoes.setBorder(BorderFactory.createTitledBorder("Ações"));
        
        JLabel labelLeitor = new JLabel("Username do Leitor:");
        JTextField campoLeitor = new JTextField();
        
        JButton botaoEmprestar = new JButton("Registrar Empréstimo");
        JButton botaoDevolver = new JButton("Registrar Devolução");
        
        painelAcoes.add(labelLeitor);
        painelAcoes.add(campoLeitor);
        painelAcoes.add(new JLabel(""));
        painelAcoes.add(new JLabel(""));
        painelAcoes.add(botaoEmprestar);
        painelAcoes.add(botaoDevolver);
        
        // Área de histórico
        JTextArea areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        areaHistorico.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaHistorico.setText("===== HISTÓRICO DE OPERAÇÕES =====\n\n");
        JScrollPane scrollHistorico = new JScrollPane(areaHistorico);
        
        // Evento de emprestar - AGORA FUNCIONA DE VERDADE
        botaoEmprestar.addActionListener(e -> {
            int indice = listaLivrosEmprestimo.getSelectedIndex();
            String leitor = campoLeitor.getText().trim();
            
            if (indice == -1) {
                JOptionPane.showMessageDialog(this,
                    "Selecione um livro da lista!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (leitor.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Digite o username do leitor!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ArrayList<Livro> livros = gerenciadorLivros.getLivros();
            Livro livroSelecionado = livros.get(indice);
            
            // EMPRESTA DE VERDADE
            boolean sucesso = gerenciadorLivros.emprestarLivro(livroSelecionado.getId(), leitor);
            
            if (sucesso) {
                String registro = java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                    " - EMPRÉSTIMO\n" +
                    "   Livro: " + livroSelecionado.getTitulo() + "\n" +
                    "   Leitor: " + leitor + "\n" +
                    "-".repeat(50) + "\n\n";
                
                areaHistorico.append(registro);
                atualizarListaLivrosEmprestimo();
                campoLeitor.setText("");
                
                JOptionPane.showMessageDialog(this,
                    "Empréstimo registrado e salvo com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro: Este livro já está emprestado!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Evento de devolver - AGORA FUNCIONA DE VERDADE
        botaoDevolver.addActionListener(e -> {
            int indice = listaLivrosEmprestimo.getSelectedIndex();
            
            if (indice == -1) {
                JOptionPane.showMessageDialog(this,
                    "Selecione um livro da lista!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ArrayList<Livro> livros = gerenciadorLivros.getLivros();
            Livro livroSelecionado = livros.get(indice);
            
            if (livroSelecionado.isDisponivel()) {
                JOptionPane.showMessageDialog(this,
                    "Este livro já está disponível!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // DEVOLVE DE VERDADE
            boolean sucesso = gerenciadorLivros.devolverLivro(livroSelecionado.getId());
            
            if (sucesso) {
                String registro = java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                    " - DEVOLUÇÃO\n" +
                    "   Livro: " + livroSelecionado.getTitulo() + "\n" +
                    "-".repeat(50) + "\n\n";
                
                areaHistorico.append(registro);
                atualizarListaLivrosEmprestimo();
                
                JOptionPane.showMessageDialog(this,
                    "Devolução registrada e salva com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao registrar devolução!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        painelSuperior.add(painelSelecao, BorderLayout.CENTER);
        painelSuperior.add(painelAcoes, BorderLayout.SOUTH);
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(painelSuperior, BorderLayout.NORTH);
        painel.add(scrollHistorico, BorderLayout.CENTER);
        
        return painel;
    }
    
    /**
     * Atualiza a lista de livros para empréstimo
     */
    private void atualizarListaLivrosEmprestimo() {
        modeloListaLivros.clear();
        ArrayList<Livro> livros = gerenciadorLivros.getLivros();
        
        for (Livro livro : livros) {
            modeloListaLivros.addElement(livro.toString());
        }
    }
}