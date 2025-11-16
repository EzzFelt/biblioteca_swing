import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * TelaCadastro - Interface para cadastro de novos usuários
 * Permite cadastrar os 3 tipos de usuários (requisito do trabalho)
 */
public class TelaCadastro extends JFrame {
    // Componentes da interface
    private JTextField campoNome;
    private JTextField campoUsername;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmaSenha;
    private JComboBox<String> comboTipoUsuario;
    private JButton botaoCadastrar;
    private JButton botaoCancelar;
    private GerenciadorUsuarios gerenciador;
    
    /**
     * Construtor - cria a interface de cadastro
     */
    public TelaCadastro(GerenciadorUsuarios gerenciador) {
        this.gerenciador = gerenciador;
        
        // Configurações da janela
        setTitle("Cadastro de Usuário");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Apenas fecha esta janela
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Painel principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("Cadastro de Novo Usuário");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Painel para o nome
        JPanel painelNome = criarPainelCampo("Nome Completo:", campoNome = new JTextField(20));
        
        // Painel para o username
        JPanel painelUsername = criarPainelCampo("Nome de Usuário:", campoUsername = new JTextField(20));
        
        // Painel para a senha
        JPanel painelSenha = criarPainelCampo("Senha:", campoSenha = new JPasswordField(20));
        
        // Painel para confirmar senha
        JPanel painelConfirmaSenha = criarPainelCampo("Confirmar Senha:", campoConfirmaSenha = new JPasswordField(20));
        
        // Painel para tipo de usuário (ComboBox - componente Swing)
        JPanel painelTipo = new JPanel();
        painelTipo.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelTipo = new JLabel("Tipo de Usuário:");
        labelTipo.setPreferredSize(new Dimension(120, 25));
        
        // ComboBox com os 3 tipos de usuários
        String[] tipos = {"Leitor", "Bibliotecario", "Administrador"};
        comboTipoUsuario = new JComboBox<>(tipos);
        comboTipoUsuario.setPreferredSize(new Dimension(200, 25));
        
        painelTipo.add(labelTipo);
        painelTipo.add(comboTipoUsuario);
        
        // Painel para os botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setPreferredSize(new Dimension(120, 30));
        
        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setPreferredSize(new Dimension(120, 30));
        
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoCancelar);
        
        // Adiciona todos os componentes
        painelPrincipal.add(titulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        painelPrincipal.add(painelNome);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        painelPrincipal.add(painelUsername);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        painelPrincipal.add(painelSenha);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        painelPrincipal.add(painelConfirmaSenha);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        painelPrincipal.add(painelTipo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        painelPrincipal.add(painelBotoes);
        
        add(painelPrincipal);
        
        // Configura os eventos
        configurarEventos();
    }
    
    /**
     * Método auxiliar para criar painéis de campos de forma consistente
     */
    private JPanel criarPainelCampo(String labelText, JTextField campo) {
        JPanel painel = new JPanel();
        painel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 25));
        painel.add(label);
        painel.add(campo);
        return painel;
    }
    
    /**
     * Configura os eventos dos botões
     */
    private void configurarEventos() {
        // Evento do botão Cadastrar
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarCadastro();
            }
        });
        
        // Evento do botão Cancelar
        botaoCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela
            }
        });
    }
    
    /**
     * Realiza o cadastro do usuário
     * Demonstra validação usando métodos da classe String
     */
    private void realizarCadastro() {
        // Obtém os valores dos campos
        String nome = campoNome.getText().trim();
        String username = campoUsername.getText().trim();
        String senha = new String(campoSenha.getPassword());
        String confirmaSenha = new String(campoConfirmaSenha.getPassword());
        String tipoSelecionado = (String) comboTipoUsuario.getSelectedItem();
        
        // Validações usando métodos da classe String
        
        // 1. Verifica se todos os campos foram preenchidos
        if (nome.isEmpty() || username.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, preencha todos os campos!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 2. Verifica se o nome tem pelo menos 3 caracteres (método length())
        if (nome.length() < 3) {
            JOptionPane.showMessageDialog(this,
                "O nome deve ter pelo menos 3 caracteres!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 3. Verifica se o username tem pelo menos 4 caracteres
        if (username.length() < 4) {
            JOptionPane.showMessageDialog(this,
                "O nome de usuário deve ter pelo menos 4 caracteres!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 4. Verifica se a senha tem pelo menos 4 caracteres
        if (senha.length() < 4) {
            JOptionPane.showMessageDialog(this,
                "A senha deve ter pelo menos 4 caracteres!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 5. Verifica se as senhas coincidem (método equals())
        if (!senha.equals(confirmaSenha)) {
            JOptionPane.showMessageDialog(this,
                "As senhas não coincidem!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            campoSenha.setText("");
            campoConfirmaSenha.setText("");
            return;
        }
        
        // Cria o objeto usuário apropriado baseado no tipo (POLIMORFISMO)
        Usuario novoUsuario = null;
        
        switch (tipoSelecionado) {
            case "Administrador":
                novoUsuario = new Administrador(nome, username, senha);
                break;
            case "Bibliotecario":
                novoUsuario = new Bibliotecario(nome, username, senha);
                break;
            case "Leitor":
                novoUsuario = new Leitor(nome, username, senha);
                break;
        }
        
        // Tenta cadastrar o usuário
        if (gerenciador.cadastrarUsuario(novoUsuario)) {
            // Cadastro bem-sucedido
            JOptionPane.showMessageDialog(this,
                "Usuário cadastrado com sucesso!\n" +
                "Tipo: " + tipoSelecionado + "\n" +
                "Username: " + username,
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Limpa os campos
            limparCampos();
            
        } else {
            // Cadastro falhou (username já existe)
            JOptionPane.showMessageDialog(this,
                "Este nome de usuário já está em uso!\n" +
                "Por favor, escolha outro.",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            campoUsername.requestFocus();
        }
    }
    
    /**
     * Limpa todos os campos do formulário
     */
    private void limparCampos() {
        campoNome.setText("");
        campoUsername.setText("");
        campoSenha.setText("");
        campoConfirmaSenha.setText("");
        comboTipoUsuario.setSelectedIndex(0);
        campoNome.requestFocus();
    }
}