## Como o plugin funciona

O plugin funciona com base no Spring Boot e Spring Data JPA.
Ele adiciona a dependência do starter para o JPA e do driver JDBC do banco de dados informado durante a aplicação do plugin.
Também adiciona a dependência do módulo de reflection do Kotlin, sem o qual o JPA não funciona com algumas funcionalidades da linguagem.
Caso opte por uma ferramenta de migração, sua dependência também é adicionada.
Além das dependências, o restante da configuração é declarada no arquivo de propriedades do projeto, a partir das informações fornecidas pelo usuário, assim como as definições necessárias para execução com *Docker*.
