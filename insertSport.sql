select * from [smartcitycodefirst].[AspNetUsers];
SET IDENTITY_INSERT [smartcitycodefirst].[Sports] ON;
insert into [smartcitycodefirst].[Sports] (SportId,Libell�)values (1,'Football'),(2,'HandBall');
SET IDENTITY_INSERT [smartcitycodefirst].[Sports] OFF;
select * from [smartcitycodefirst].[Sports];
ALTER TABLE [smartcitycodefirst].[AspNetUsers] ALTER COLUMN [Password] varchar NULL;
ALTER TABLE [smartcitycodefirst].[AspNetUsers] ALTER COLUMN [Sexe] varchar NULL;
ALTER TABLE [smartcitycodefirst].[AspNetUsers] ALTER COLUMN TwoFactorEnabled bit NULL;
ALTER TABLE [smartcitycodefirst].[AspNetUsers] ALTER COLUMN TwoFactorEnabled bit NULL;
ALTER TABLE [smartcitycodefirst].[AspNetUsers] ALTER COLUMN EmailConfirmed bit NULL;
ALTER TABLE [smartcitycodefirst].[Disponibilites] ALTER COLUMN ComplexeSportifId INT NULL
insert into [smartcitycodefirst].[Disponibilites](SportId,UtilisateurId) values();
select * from [smartcitycodefirst].[Disponibilites];
select * from [smartcitycodefirst].[Sports];
select * from [smartcitycodefirst].[AspNetUsers];
insert into [smartcitycodefirst].[Sports](Libellé) values ('hh');
delete from [smartcitycodefirst].[Disponibilites] where DisponibiliteId=37;
SET IDENTITY_INSERT [smartcitycodefirst].[Sports] ON
select * from [smartcitycodefirst].[AspNetUsers] where UserName = 'Frenchoooo';
CREATE UNIQUE INDEX uniqueDisponibilité
ON [smartcitycodefirst].[Disponibilites] (ComplexeSportifId,SportId,UtilisateurId);
SET IDENTITY_INSERT [smartcitycodefirst].[Groupes] ON
ALTER TABLE [smartcitycodefirst].[Groupes] ALTER COLUMN Creation datetime NULL;
insert into [smartcitycodefirst].[Groupes] (GroupeId) values (1);
select * from [smartcitycodefirst].[Groupes];
insert into [smartcitycodefirst].[AttributionGroupes](GroupeId,UtilisateurId) values (1,'a27f7e39-f451-4003-ab87-46cb14be7e6e');
insert into [smartcitycodefirst].[AttributionGroupes](GroupeId,UtilisateurId) values (1,'f6b8ac94-9e60-4fe7-99b0-9e6be80f02a5');
select * from [smartcitycodefirst].[AspNetUsers];
select * from [smartcitycodefirst].[AttributionGroupes];
delete from [smartcitycodefirst].[AttributionGroupes] where AttributionGroupeId between 1 and 19;
delete from [smartcitycodefirst].[Groupes] where GroupeId between 1 and 6;