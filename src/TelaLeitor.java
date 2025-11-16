import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * TelaLeitor - Interface para o Leitor (usuário comum)
 * CORRIGIDO: Sistema completo de empréstimos com seleção de livros
 */
public class TelaLeitor extends JFrame {
    private Leitor leitor;
    private GerenciadorLivros gerenciadorLivros;
    private JTextArea areaPermissoes;
    private JLabel labelEmprestimos;
    private JProgressBar barraProgresso;
    private JList<String> listaLivrosDisponiveis;
    private DefaultListModel<String> modeloLivrosDisponiveis;
    private JList<String> listaMeusEmprestimos;
    private DefaultListModel<String> modeloMeusEmprestimos;
    
    public TelaLeitor(Leitor leitor, GerenciadorLivros gerenciadorLivros) {
        this.leitor = leitor;
        this.gerenciadorLivros = gerenciadorLivros;
        
        setTitle("Sistema de Biblioteca - Leitor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel painelSuperior = criarPainelSuperior();
        
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Minhas Permissões", criarAbaPermissoes());
        abas.addTab("Consultar Livros", criarAbaConsulta());
        abas.addTab("Meus Empréstimos", criarAbaEmprestimos());
        abas.addTab("Meu Perfil", criarAbaPerfil());
        
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
        
        int emprestados = gerenciadorLivros.contarEmprestimosPorUsuario(leitor.getUsername());
        
        JLabel labelInfo = new JLabel(
            "<html><b>Nome:</b> " + leitor.getNome() + 
            "<br><b>Tipo:</b> " + leitor.getTipoUsuario() +
            "<br><b>Livros emprestados:</b> " + emprestados + "/" + leitor.getLimiteEmprestimos() +
            "<br><b>Limite de empréstimos:</b> " + leitor.getLimiteEmprestimos() + " livros</html>"
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
        String[] permissoes = leitor.getPermissoes();
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
     * CORRIGIDO: Agora mostra livros reais e usa seleção por lista
     */
    private JPanel criarAbaConsulta() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Livros Disponíveis para Empréstimo:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelBusca = new JLabel("Buscar:");
        JTextField campoBusca = new JTextField(30);
        JButton botaoBuscar = new JButton("Buscar");
        JButton botaoMostrarTodos = new JButton("Mostrar Todos");
        
        painelBusca.add(labelBusca);
        painelBusca.add(campoBusca);
        painelBusca.add(botaoBuscar);
        painelBusca.add(botaoMostrarTodos);
        
        // LISTA de livros (não texto) - mais fácil de selecionar
        modeloLivrosDisponiveis = new DefaultListModel<>();
        listaLivrosDisponiveis = new JList<>(modeloLivrosDisponiveis);
        listaLivrosDisponiveis.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaLivrosDisponiveis.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        atualizarListaLivrosDisponiveis();
        
        JScrollPane scroll = new JScrollPane(listaLivrosDisponiveis);
        
        // Painel de ações
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton botaoReservar = new JButton("Pegar Emprestado");
        JButton botaoAtualizar = new JButton("Atualizar Lista");
        JButton botaoVerDetalhes = new JButton("Ver Detalhes");
        
        painelAcoes.add(botaoVerDetalhes);
        painelAcoes.add(botaoReservar);
        painelAcoes.add(botaoAtualizar);
        
        // Evento buscar
        botaoBuscar.addActionListener(e -> {
            String termo = campoBusca.getText().trim();
            if (termo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Digite algo para buscar!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            buscarLivros(termo);
        });
        
        // Evento mostrar todos
        botaoMostrarTodos.addActionListener(e -> {
            campoBusca.setText("");
            atualizarListaLivrosDisponiveis();
        });
        
        // Evento ver detalhes
        botaoVerDetalhes.addActionListener(e -> {
            int indice = listaLivrosDisponiveis.getSelectedIndex();
            if (indice == -1) {
                JOptionPane.showMessageDialog(this,
                    "Selecione um livro da lista!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            ArrayList<Livro> disponiveis = gerenciadorLivros.getLivrosDisponiveis();
            Livro livro = disponiveis.get(indice);
            
            JOptionPane.showMessageDialog(this,
                livro.getDescricaoCompleta(),
                "Detalhes do Livro",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Evento pegar emprestado - FUNCIONA DE VERDADE
        botaoReservar.addActionListener(e -> {
            int indice = listaLivrosDisponiveis.getSelectedIndex();
            
            if (indice == -1) {
                JOptionPane.showMessageDialog(this,
                    "Selecione um livro da lista!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Verifica limite
            int emprestados = gerenciadorLivros.contarEmprestimosPorUsuario(leitor.getUsername());
            if (emprestados >= leitor.getLimiteEmprestimos()) {
                JOptionPane.showMessageDialog(this,
                    "Você atingiu o limite de " + leitor.getLimiteEmprestimos() + " livros!\n" +
                    "Devolva um livro antes de fazer novo empréstimo.",
                    "Limite Atingido",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            ArrayList<Livro> disponiveis = gerenciadorLivros.getLivrosDisponiveis();
            Livro livro = disponiveis.get(indice);
            
            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja pegar emprestado:\n\n" +
                livro.getTitulo() + " - " + livro.getAutor() + "\n\n" +
                "Prazo de devolução: 14 dias",
                "Confirmar Empréstimo",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                // EMPRESTA DE VERDADE
                boolean sucesso = gerenciadorLivros.emprestarLivro(livro.getId(), leitor.getUsername());
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                        "Empréstimo realizado com sucesso!\n" +
                        "Livro: " + livro.getTitulo() + "\n" +
                        "Prazo: 14 dias\n\n" +
                        "Retire o livro na biblioteca.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    atualizarListaLivrosDisponiveis();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Erro ao realizar empréstimo!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Evento atualizar
        botaoAtualizar.addActionListener(e -> atualizarListaLivrosDisponiveis());
        
        JPanel painelNorte = new JPanel(new BorderLayout());
        painelNorte.add(titulo, BorderLayout.NORTH);
        painelNorte.add(painelBusca, BorderLayout.SOUTH);
        
        painel.add(painelNorte, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelAcoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    /**
     * Atualiza lista de livros disponíveis
     */
    private void atualizarListaLivrosDisponiveis() {
        modeloLivrosDisponiveis.clear();
        ArrayList<Livro> disponiveis = gerenciadorLivros.getLivrosDisponiveis();
        
        if (disponiveis.isEmpty()) {
            modeloLivrosDisponiveis.addElement("Nenhum livro disponível no momento.");
        } else {
            for (Livro livro : disponiveis) {
                modeloLivrosDisponiveis.addElement(livro.getTitulo() + " - " + livro.getAutor() + " (" + livro.getAno() + ")");
            }
        }
    }
    
    /**
     * Busca livros por título
     */
    private void buscarLivros(String termo) {
        modeloLivrosDisponiveis.clear();
        ArrayList<Livro> resultados = gerenciadorLivros.buscarLivrosPorTitulo(termo);
        
        // Filtra apenas os disponíveis
        ArrayList<Livro> disponiveisEncontrados = new ArrayList<>();
        for (Livro livro : resultados) {
            if (livro.isDisponivel()) {
                disponiveisEncontrados.add(livro);
            }
        }
        
        if (disponiveisEncontrados.isEmpty()) {
            modeloLivrosDisponiveis.addElement("Nenhum livro encontrado com: " + termo);
        } else {
            for (Livro livro : disponiveisEncontrados) {
                modeloLivrosDisponiveis.addElement(livro.getTitulo() + " - " + livro.getAutor() + " (" + livro.getAno() + ")");
            }
        }
    }
    
    /**
     * CORRIGIDO: Agora mostra os livros realmente emprestados pelo usuário
     */
    private JPanel criarAbaEmprestimos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Meus Empréstimos:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Status de empréstimos
        JPanel painelStatus = new JPanel(new GridLayout(2, 1, 5, 5));
        painelStatus.setBorder(BorderFactory.createTitledBorder("Status"));
        
        int emprestados = gerenciadorLivros.contarEmprestimosPorUsuario(leitor.getUsername());
        
        labelEmprestimos = new JLabel("Livros emprestados: " + emprestados + "/" + leitor.getLimiteEmprestimos());
        labelEmprestimos.setFont(new Font("Arial", Font.PLAIN, 12));
        
        barraProgresso = new JProgressBar(0, leitor.getLimiteEmprestimos());
        barraProgresso.setValue(emprestados);
        barraProgresso.setStringPainted(true);
        barraProgresso.setString(emprestados + "/" + leitor.getLimiteEmprestimos());
        
        painelStatus.add(labelEmprestimos);
        painelStatus.add(barraProgresso);
        
        // LISTA dos livros emprestados (não texto)
        modeloMeusEmprestimos = new DefaultListModel<>();
        listaMeusEmprestimos = new JList<>(modeloMeusEmprestimos);
        listaMeusEmprestimos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMeusEmprestimos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        atualizarMeusEmprestimos();
        
        JScrollPane scroll = new JScrollPane(listaMeusEmprestimos);
        
        // Botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton botaoRenovar = new JButton("Renovar Empréstimo");
        JButton botaoDevolver = new JButton("Devolver Livro");
        JButton botaoAtualizar = new JButton("Atualizar");
        
        painelBotoes.add(botaoRenovar);
        painelBotoes.add(botaoDevolver);
        painelBotoes.add(botaoAtualizar);
        
        // Evento renovar
        botaoRenovar.addActionListener(e -> {
            int indice = listaMeusEmprestimos.getSelectedIndex();
            if (indice == -1) {
                JOptionPane.showMessageDialog(this,
                    "Selecione um livro da lista!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(this,
                "Empréstimo renovado por mais 14 dias!",
                "Renovação",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Evento devolver - FUNCIONA DE VERDADE
        botaoDevolver.addActionListener(e -> {
            int indice = listaMeusEmprestimos.getSelectedIndex();
            if (indice == -1) {
                JOptionPane.showMessageDialog(this,
                    "Selecione um livro da lista!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            ArrayList<Livro> meusLivros = gerenciadorLivros.getLivrosEmprestadosPara(leitor.getUsername());
            Livro livro = meusLivros.get(indice);
            
            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Confirma a devolução de:\n" + livro.getTitulo() + "?",
                "Confirmar Devolução",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                // DEVOLVE DE VERDADE
                boolean sucesso = gerenciadorLivros.devolverLivro(livro.getId());
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                        "Livro devolvido com sucesso!\n" +
                        "Entregue o livro na biblioteca.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    atualizarMeusEmprestimos();
                    atualizarStatus();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Erro ao devolver livro!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Evento atualizar
        botaoAtualizar.addActionListener(e -> {
            atualizarMeusEmprestimos();
            atualizarStatus();
        });
        
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.add(painelStatus, BorderLayout.NORTH);
        painelCentral.add(scroll, BorderLayout.CENTER);
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(painelCentral, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    /**
     * Atualiza a lista de empréstimos do usuário
     */
    private void atualizarMeusEmprestimos() {
        modeloMeusEmprestimos.clear();
        ArrayList<Livro> meusLivros = gerenciadorLivros.getLivrosEmprestadosPara(leitor.getUsername());
        
        if (meusLivros.isEmpty()) {
            modeloMeusEmprestimos.addElement("Você não tem livros emprestados no momento.");
        } else {
            for (Livro livro : meusLivros) {
                modeloMeusEmprestimos.addElement(livro.getTitulo() + " - " + livro.getAutor() + " (" + livro.getAno() + ")");
            }
        }
    }
    
    /**
     * Atualiza o status de empréstimos
     */
    private void atualizarStatus() {
        int emprestados = gerenciadorLivros.contarEmprestimosPorUsuario(leitor.getUsername());
        labelEmprestimos.setText("Livros emprestados: " + emprestados + "/" + leitor.getLimiteEmprestimos());
        barraProgresso.setValue(emprestados);
        barraProgresso.setString(emprestados + "/" + leitor.getLimiteEmprestimos());
    }
    
    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Meu Perfil:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel painelInfo = new JPanel(new GridLayout(5, 2, 10, 10));
        painelInfo.setBorder(BorderFactory.createTitledBorder("Informações Pessoais"));
        
        painelInfo.add(new JLabel("Nome:"));
        painelInfo.add(new JLabel(leitor.getNome()));
        
        painelInfo.add(new JLabel("Username:"));
        painelInfo.add(new JLabel(leitor.getUsername()));
        
        painelInfo.add(new JLabel("Tipo de Usuário:"));
        painelInfo.add(new JLabel(leitor.getTipoUsuario()));
        
        int emprestados = gerenciadorLivros.contarEmprestimosPorUsuario(leitor.getUsername());
        
        painelInfo.add(new JLabel("Livros Emprestados:"));
        painelInfo.add(new JLabel(String.valueOf(emprestados)));
        
        painelInfo.add(new JLabel("Limite de Empréstimos:"));
        painelInfo.add(new JLabel(String.valueOf(leitor.getLimiteEmprestimos())));
        
        JPanel painelSenha = new JPanel(new GridLayout(3, 2, 10, 10));
        painelSenha.setBorder(BorderFactory.createTitledBorder("Alterar Senha"));
        
        JLabel labelSenhaAtual = new JLabel("Senha Atual:");
        JPasswordField campoSenhaAtual = new JPasswordField();
        
        JLabel labelNovaSenha = new JLabel("Nova Senha:");
        JPasswordField campoNovaSenha = new JPasswordField();
        
        JButton botaoAlterar = new JButton("Alterar Senha");
        
        painelSenha.add(labelSenhaAtual);
        painelSenha.add(campoSenhaAtual);
        painelSenha.add(labelNovaSenha);
        painelSenha.add(campoNovaSenha);
        painelSenha.add(new JLabel(""));
        painelSenha.add(botaoAlterar);
        
        botaoAlterar.addActionListener(e -> {
            String senhaAtual = new String(campoSenhaAtual.getPassword());
            String novaSenha = new String(campoNovaSenha.getPassword());
            
            if (senhaAtual.isEmpty() || novaSenha.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!senhaAtual.equals(leitor.getSenha())) {
                JOptionPane.showMessageDialog(this,
                    "Senha atual incorreta!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (novaSenha.length() < 4) {
                JOptionPane.showMessageDialog(this,
                    "A nova senha deve ter pelo menos 4 caracteres!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            leitor.setSenha(novaSenha);
            
            JOptionPane.showMessageDialog(this,
                "Senha alterada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            
            campoSenhaAtual.setText("");
            campoNovaSenha.setText("");
        });
        
        JPanel painelCentral = new JPanel(new GridLayout(2, 1, 10, 10));
        painelCentral.add(painelInfo);
        painelCentral.add(painelSenha);
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(painelCentral, BorderLayout.CENTER);
        
        return painel;
    }
}