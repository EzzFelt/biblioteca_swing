import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * TelaLogin - Interface gráfica de login usando Swing
 * Demonstra uso da CLASSE SWING (requisito do trabalho)
 */
public class TelaLogin extends JFrame {
    // Componentes da interface
    private JTextField campoUsername;
    private JPasswordField campoSenha;
    private JButton botaoLogin;
    private JButton botaoCadastrar;
    private GerenciadorUsuarios gerenciador;
    private GerenciadorLivros gerenciadorLivros;
    
    /**
     * Construtor - cria a interface de login
     */
    public TelaLogin(GerenciadorUsuarios gerenciador, GerenciadorLivros gerenciadorLivros) {
        this.gerenciador = gerenciador;
        this.gerenciadorLivros = gerenciadorLivros;
        
        // Configurações da janela principal
        setTitle("Sistema de Biblioteca - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setResizable(false);
        
        // Cria o painel principal com layout
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("Sistema de Gerenciamento de Biblioteca");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Painel para o username
        JPanel painelUsername = new JPanel();
        painelUsername.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelUsername = new JLabel("Usuário:");
        labelUsername.setPreferredSize(new Dimension(80, 25));
        campoUsername = new JTextField(20);
        painelUsername.add(labelUsername);
        painelUsername.add(campoUsername);
        
        // Painel para a senha
        JPanel painelSenha = new JPanel();
        painelSenha.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setPreferredSize(new Dimension(80, 25));
        campoSenha = new JPasswordField(20);
        painelSenha.add(labelSenha);
        painelSenha.add(campoSenha);
        
        // Painel para os botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        botaoLogin = new JButton("Entrar");
        botaoLogin.setPreferredSize(new Dimension(120, 30));
        
        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setPreferredSize(new Dimension(120, 30));
        
        painelBotoes.add(botaoLogin);
        painelBotoes.add(botaoCadastrar);
        
        // Adiciona todos os componentes ao painel principal
        painelPrincipal.add(titulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        painelPrincipal.add(painelUsername);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        painelPrincipal.add(painelSenha);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        painelPrincipal.add(painelBotoes);
        
        // Adiciona o painel principal à janela
        add(painelPrincipal);
        
        // Adiciona os eventos aos botões
        configurarEventos();
    }
    
    /**
     * Configura os eventos de clique dos botões
     * Demonstra uso de ActionListener (interface)
     */
    private void configurarEventos() {
        // Evento do botão Login
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        // Evento do botão Cadastrar
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaCadastro();
            }
        });
        
        // Permite fazer login pressionando Enter no campo senha
        campoSenha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }
    
    /**
     * Realiza o processo de login
     */
    private void realizarLogin() {
        // Obtém os valores dos campos (uso de métodos da classe String)
        String username = campoUsername.getText().trim();
        String senha = new String(campoSenha.getPassword());
        
        // Validação básica
        if (username.isEmpty() || senha.isEmpty()) {
            // JOptionPane - classe Swing para exibir mensagens
            JOptionPane.showMessageDialog(this,
                "Por favor, preencha todos os campos!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tenta fazer o login
        Usuario usuario = gerenciador.fazerLogin(username, senha);
        
        if (usuario != null) {
            // Login bem-sucedido
            JOptionPane.showMessageDialog(this,
                "Bem-vindo, " + usuario.getNome() + "!",
                "Login Bem-sucedido",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Abre a tela apropriada baseado no tipo de usuário (POLIMORFISMO)
            abrirTelaUsuario(usuario);
            
        } else {
            // Login falhou
            JOptionPane.showMessageDialog(this,
                "Usuário ou senha incorretos!",
                "Erro de Login",
                JOptionPane.ERROR_MESSAGE);
            
            // Limpa os campos
            campoSenha.setText("");
            campoUsername.requestFocus();
        }
    }
    
    /**
     * Abre a tela correspondente ao tipo de usuário
     * Demonstra POLIMORFISMO - trata diferentes tipos de usuários
     */
    private void abrirTelaUsuario(Usuario usuario) {
        // Fecha a tela de login
        this.dispose();
        
        // Abre a tela específica baseado no tipo (instanceof verifica o tipo)
        if (usuario instanceof Administrador) {
            TelaAdmin telaAdmin = new TelaAdmin((Administrador) usuario, gerenciador, gerenciadorLivros);
            telaAdmin.setVisible(true);
            
        } else if (usuario instanceof Bibliotecario) {
            TelaBibliotecario telaBib = new TelaBibliotecario((Bibliotecario) usuario, gerenciadorLivros);
            telaBib.setVisible(true);
            
        } else if (usuario instanceof Leitor) {
            TelaLeitor telaLeitor = new TelaLeitor((Leitor) usuario, gerenciadorLivros);
            telaLeitor.setVisible(true);
        }
    }
    
    /**
     * Abre a tela de cadastro
     */
    private void abrirTelaCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(gerenciador);
        telaCadastro.setVisible(true);
    }
}