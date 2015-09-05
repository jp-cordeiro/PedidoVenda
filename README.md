#LEIA AS INSTRUÇÕES POR FAVOR

# PedidoVenda
Projeto em Java de um sistema simples de pedido de venda.

Projeto desenvolvido com base nas aulas da AlgaWorks, onde abrange tecnologias como JSF , Primefaces , Spring Security, CDI, entre outras .

#Instruções:

O banco de dados utilizado é o MySQL.

Para utilizar o projeto crie no banco de dados (MySQL) um schema com o nome "pedidovendadb" ou altere no persistense.xml e na classe "src\main\java\com\pedidovenda\util\jpa\EntityManagerProducer" para o schema que preferir trabalhar.

No arquivo "src\main\webapp\META-INF\persistense.xml" alterar as informações de usuário e senha do banco de dados.

No arquivo "src\main\resources\mail.properties" alterar usuário e senha de e-mail, pois será deste e-mail que o sistema enviará 
as mensagens para os clientes.

#Requisitos do projeto

Para o projeto PedidoVenda funcionanr normalmente é necessário baixar, importar e instalar via Maven o projeto simple-mail, segue o link: 
https://github.com/codylerum/simple-email.
Para facilitar o projeto simple-mail está diponível para download aqui também. ^^

#Carga de dados para testar o sistema

--use pedidovendadb;

--insert into usuario(login,senha) values ('joa',123);
--insert into usuario(login,senha) values ('jose',123);
--insert into usuario(login,senha) values ('paulo',123);

--insert into grupo (descricao,nome) values ('Administradores','Administradores');
--insert into grupo (descricao,nome) values ('Vendedores','Vendedores');
--insert into grupo (descricao,nome) values ('Auxiliares','Auxiliares');

--insert into usuario_grupo (usuario_id,grupo_id) values (1,1);
--insert into usuario_grupo (usuario_id,grupo_id) values (2,2);
--insert into usuario_grupo (usuario_id,grupo_id) values (3,3);

--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Informática');
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Móveis');
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Eletrodomésticos');
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Computadores',1);
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Periféricos', 1);
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Acessórios', 1);
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Sofá', 2);
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Cama', 2);
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Geladeira', 3);
--INSERT INTO `pedidovendadb`.`categoria` (`descricao`, `categoria_pai_id`) VALUES ('Microondas', 3);
