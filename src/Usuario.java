public abstract class Usuario {
    protected String nome;
    protected String username;
    protected String senha;
    protected String tipoUsuario;

    public Usuario(String nome, String username, String senha) {
        this.nome = nome;
        this.username = username;
        this.senha = senha;
    }

        public String getNome() {
        return nome;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public abstract String[] getPermissoes();

    public abstract String getDescricao();

    public String paraArquivo() {
        return tipoUsuario + "|" + username + "|" + senha + "|" + nome;
    }

    @Override
    public String toString() {
        return "Usuario: " + nome + " (" + tipoUsuario + ")";
    }

}
