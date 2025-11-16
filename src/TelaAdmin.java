import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * TelaAdmin - Interface para o Administrador
 * CORRIGIDO: Agora gerencia livros e mostra estatísticas completas
 */
public class TelaAdmin extends JFrame {
    private Administrador admin;
    private GerenciadorUsuarios gerenciador;
    // adiciona o campo para acessar livros
    private GerenciadorLivros gerenciadorLivros;
    private JTextArea areaPermissoes;
    private JList<String> listaUsuarios;
    private DefaultListModel<String> modeloListaUsuarios;
    private JList<String> listaLivros;
    private DefaultListModel<String> modeloListaLivros;
    
    /**
     * Construtor da tela do administrador
     */
    public TelaAdmin(Administrador admin, GerenciadorUsuarios gerenciador, GerenciadorLivros gerenciadorLivros) {
        this.admin = admin;
        this.gerenciador = gerenciador;
        this.gerenciadorLivros = gerenciadorLivros; // nova linha: guarda a referência
        
        setTitle("Sistema de Biblioteca - Administrador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel painelSuperior = criarPainelSuperior();
        
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Minhas Permissões", criarAbaPermissoes());
        abas.addTab("Gerenciar Usuários", criarAbaUsuarios());
        abas.addTab("Gerenciar Livros", criarAbaLivros());
        abas.addTab("Relatórios", criarAbaRelatorios());
        
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoSair = new JButton("Sair");
        botaoSair.addActionListener(e -> voltarParaLogin());
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
            "<html><b>Nome:</b> " + admin.getNome() + 
            "<br><b>Tipo:</b> " + admin.getTipoUsuario() +
            "<br><b>Descrição:</b> " + admin.getDescricao() + "</html>"
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
        String[] permissoes = admin.getPermissoes();
        for (int i = 0; i < permissoes.length; i++) {
            sb.append((i + 1) + ". " + permissoes[i] + "\n");
        }
        areaPermissoes.setText(sb.toString());
        
        JScrollPane scroll = new JScrollPane(areaPermissoes);
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarAbaUsuarios() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Usuários Cadastrados:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        modeloListaUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloListaUsuarios);
        atualizarListaUsuarios();
        
        JScrollPane scroll = new JScrollPane(listaUsuarios);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton botaoAdicionar = new JButton("Adicionar Usuário");
        JButton botaoRemover = new JButton("Remover Usuário");
        JButton botaoAtualizar = new JButton("Atualizar Lista");
        
        botaoAdicionar.addActionListener(e -> abrirTelaCadastro());
        botaoRemover.addActionListener(e -> removerUsuarioSelecionado());
        botaoAtualizar.addActionListener(e -> atualizarListaUsuarios());
        
        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoRemover);
        painelBotoes.add(botaoAtualizar);
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    /**
     * NOVO: Aba de gerenciamento de livros
     */
    private JPanel criarAbaLivros() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Livros Cadastrados:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Lista de livros
        modeloListaLivros = new DefaultListModel<>();
        listaLivros = new JList<>(modeloListaLivros);
        atualizarListaLivros();
        
        JScrollPane scroll = new JScrollPane(listaLivros);
        
        // Painel de formulário para cadastro
        JPanel painelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo Livro"));
        
        JLabel labelTitulo = new JLabel("Título:");
        JTextField campoTitulo = new JTextField();
        
        JLabel labelAutor = new JLabel("Autor:");
        JTextField campoAutor = new JTextField();
        
        JLabel labelAno = new JLabel("Ano:");
        JTextField campoAno = new JTextField();
        
        JButton botaoCadastrar = new JButton("Cadastrar Livro");
        
        painelFormulario.add(labelTitulo);
        painelFormulario.add(campoTitulo);
        painelFormulario.add(labelAutor);
        painelFormulario.add(campoAutor);
        painelFormulario.add(labelAno);
        painelFormulario.add(campoAno);
        painelFormulario.add(new JLabel(""));
        painelFormulario.add(botaoCadastrar);
        
        // Painel de botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton botaoRemover = new JButton("Remover Livro Selecionado");
        JButton botaoAtualizar = new JButton("Atualizar Lista");
        
        painelBotoes.add(botaoRemover);
        painelBotoes.add(botaoAtualizar);
        
        // Evento cadastrar
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
            
            gerenciadorLivros.cadastrarLivro(titulo_livro, autor, ano);
            
            JOptionPane.showMessageDialog(this,
                "Livro cadastrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Limpa os campos
            campoTitulo.setText("");
            campoAutor.setText("");
            campoAno.setText("");
            
            atualizarListaLivros();
        });
        
        // Evento remover
        botaoRemover.addActionListener(e -> removerLivroSelecionado());
        
        // Evento atualizar
        botaoAtualizar.addActionListener(e -> atualizarListaLivros());
        
        // Painel superior com formulário e lista
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.add(painelFormulario, BorderLayout.NORTH);
        painelCentral.add(scroll, BorderLayout.CENTER);
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(painelCentral, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    /**
     * CORRIGIDO: Relatórios agora incluem estatísticas de livros
     */
    private JPanel criarAbaRelatorios() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Relatórios do Sistema:");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        JTextArea areaRelatorio = new JTextArea();
        areaRelatorio.setEditable(false);
        areaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Gera relatório completo
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("========== RELATÓRIO DO SISTEMA ==========\n\n");
        
        // Estatísticas de usuários
        ArrayList<Usuario> usuarios = gerenciador.getUsuarios();
        
        int totalAdmin = 0, totalBib = 0, totalLeitor = 0;
        for (Usuario u : usuarios) {
            if (u instanceof Administrador) totalAdmin++;
            else if (u instanceof Bibliotecario) totalBib++;
            else if (u instanceof Leitor) totalLeitor++;
        }
        
        relatorio.append("--- USUÁRIOS ---\n");
        relatorio.append("Total de usuários: " + usuarios.size() + "\n");
        relatorio.append("  • Administradores: " + totalAdmin + "\n");
        relatorio.append("  • Bibliotecários: " + totalBib + "\n");
        relatorio.append("  • Leitores: " + totalLeitor + "\n\n");
        
        // NOVO: Estatísticas de livros
        relatorio.append("--- LIVROS ---\n");
        ArrayList<Livro> livros = gerenciadorLivros.getLivros();
        ArrayList<Livro> disponiveis = gerenciadorLivros.getLivrosDisponiveis();
        
        relatorio.append("Total de livros: " + livros.size() + "\n");
        relatorio.append("  • Disponíveis: " + disponiveis.size() + "\n");
        relatorio.append("  • Emprestados: " + (livros.size() - disponiveis.size()) + "\n\n");
        
        relatorio.append("========== LISTA DE USUÁRIOS ==========\n\n");
        for (Usuario u : usuarios) {
            relatorio.append(u.toString() + "\n");
        }
        
        relatorio.append("\n========== LISTA DE LIVROS ==========\n\n");
        for (Livro l : livros) {
            relatorio.append(l.toString() + "\n");
        }
        
        areaRelatorio.setText(relatorio.toString());
        
        JScrollPane scroll = new JScrollPane(areaRelatorio);
        
        JButton botaoExportar = new JButton("Exportar Relatório");
        botaoExportar.addActionListener(e -> exportarRelatorio(relatorio.toString()));
        
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotao.add(botaoExportar);
        
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelBotao, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private void atualizarListaUsuarios() {
        modeloListaUsuarios.clear();
        ArrayList<Usuario> usuarios = gerenciador.getUsuarios();
        
        for (Usuario u : usuarios) {
            String texto = String.format("%s - %s (%s)", 
                u.getUsername(), u.getNome(), u.getTipoUsuario());
            modeloListaUsuarios.addElement(texto);
        }
    }
    
    /**
     * NOVO: Atualiza lista de livros
     */
    private void atualizarListaLivros() {
        modeloListaLivros.clear();
        ArrayList<Livro> livros = gerenciadorLivros.getLivros();
        
        for (Livro l : livros) {
            modeloListaLivros.addElement(l.toString());
        }
    }
    
    /**
     * NOVO: Remove livro selecionado
     */
    private void removerLivroSelecionado() {
        int indice = listaLivros.getSelectedIndex();
        
        if (indice == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, selecione um livro para remover!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ArrayList<Livro> livros = gerenciadorLivros.getLivros();
        Livro livroSelecionado = livros.get(indice);
        
        // Verifica se o livro está emprestado
        if (!livroSelecionado.isDisponivel()) {
            JOptionPane.showMessageDialog(this,
                "Não é possível remover um livro emprestado!\n" +
                "Aguarde a devolução primeiro.",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int resposta = JOptionPane.showConfirmDialog(this,
            "Deseja realmente remover o livro:\n" + livroSelecionado.getTitulo() + "?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION);
        
        if (resposta == JOptionPane.YES_OPTION) {
            gerenciadorLivros.removerLivro(livroSelecionado.getId());
            atualizarListaLivros();
            JOptionPane.showMessageDialog(this,
                "Livro removido com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void removerUsuarioSelecionado() {
        int indice = listaUsuarios.getSelectedIndex();
        
        if (indice == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor, selecione um usuário para remover!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ArrayList<Usuario> usuarios = gerenciador.getUsuarios();
        Usuario usuarioSelecionado = usuarios.get(indice);
        
        if (usuarioSelecionado.getUsername().equals(admin.getUsername())) {
            JOptionPane.showMessageDialog(this,
                "Você não pode remover seu próprio usuário!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int resposta = JOptionPane.showConfirmDialog(this,
            "Deseja realmente remover o usuário " + usuarioSelecionado.getNome() + "?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION);
        
        if (resposta == JOptionPane.YES_OPTION) {
            gerenciador.removerUsuario(usuarioSelecionado.getUsername());
            atualizarListaUsuarios();
            JOptionPane.showMessageDialog(this,
                "Usuário removido com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void abrirTelaCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(gerenciador);
        telaCadastro.setVisible(true);
    }
    
    private void exportarRelatorio(String relatorio) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("relatorio_sistema.txt");
            fw.write(relatorio);
            fw.close();
            
            JOptionPane.showMessageDialog(this,
                "Relatório exportado com sucesso!\n" +
                "Arquivo: relatorio_sistema.txt",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao exportar relatório: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void voltarParaLogin() {
        this.dispose();
        TelaLogin telaLogin = new TelaLogin(gerenciador, gerenciadorLivros);
        telaLogin.setVisible(true);
    }
}