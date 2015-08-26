# PedidoVenda
Projeto em Java de um sistema de pedido simples.

Projeto desenvolvido com base nas aulas da AlgaWorks, onde abrange tecnologias como JSF , Primefaces , Spring Security, CDI .

#Instruções:

O banco de dados utilizado é o MySQL.

Para utilizar o projeto crie no banco de dados (MySQL) um schema com o nome "pedidovendadb" ou altere no persistense.xml e na classe "src\main\java\com\pedidovenda\util\jpa\EntityManagerProducer" para o schema que preferir trabalhar.

No arquivo "src\main\webapp\META-INF\persistense.xml" alterar as informações de usuário e senha do banco de dados.

No arquivo "src\main\resources\mail.properties" alterar usuário e senha de e-mail, pois será desta e-mail que o sistema enviará 
as mensagens para os clientes.
