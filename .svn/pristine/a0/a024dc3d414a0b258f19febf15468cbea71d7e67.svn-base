
--PROJET DE BDM CREATION DES TYPES--
--set line 500
--set pagesize 500

-- CREATION DE LA PARTIE MOTS-CLE AVEC VIDEO, audio, image --

create type motcle_type as object(
id_mot integer,
nom varchar2(30));
/

create type monument_type;
/
create type media_type;
/
create type description_type;
/

create type theme_type;
/
create type ville_type;
/

-- creation du lien N-N entre motclé et media--
create type media_motcle_type as object(
media_ref ref media_type,
motcle_ref ref motcle_type);
/


--creation de l'héritage--


create type media_type as object(
id_media integer,
nom varchar2(32),
date_ajout date,
legende varchar(200),
monument ref monument_type
) not instantiable not final;
/

show error;

--images--

create type image_type under media_type(
image ORDSYS.ORDImage) final instantiable;
/

--audio--

create type audio_type under media_type(
duree integer,
audio ORDSYS.ORDAUDIO) final instantiable;
/

--video--
create type video_type under media_type(
duree integer,
video ORDSYS.ORDVIDEO) final instantiable;
/


-- creation du lien 1-N entre media et momnument--

create type media_ref_type as object(
media ref media_type);
/


create type medias_type as table of media_ref_type;
/

--creation du type monument--

create type description_type as object(
classement varchar2(200),
constructeur varchar2(50),
adresse varchar2(50),
commentaire varchar(200)
);
/

insert into ville values(21000,'Dijon','Bourgogne','France',monuments_type());
insert into theme values(10,'Public',monuments_type());
insert into monument values(1,'Dijon Gare',(select ref(v) from ville v where id_ville = 21000),'Edifice',TO_DATE('1977/01/15','yyyy/mm/dd'),description_type('Regionale','Homme','Gare simple'),(select ref(t) from theme t where id_theme=10), medias_type());
create type monument_type as object(
id_monument integer,
nom varchar2(20),
ville ref ville_type,
type_monument varchar2(32),--Eglise, hotel de ville ...
date_construction date,
decription description_type,
theme ref theme_type,
medias medias_type
);
/

--cretion lien 1-N avec la ville --

create type monument_ref_type as object(
monument ref monument_type);
/

create type monuments_type as table of monument_ref_type;
/

create type ville_type as object(
id_ville integer,
nom_ville varchar2(50),
region varchar2(50),
pays varchar2(30),
monuments monuments_type);
/

--cretion lien 1-N avec le theme --

create type theme_type as object(
id_theme integer,
theme varchar2(32),
monuments  monuments_type);
/
---- Creations des tables
create table motcle of motcle_type
(CONSTRAINT PK_ID_MOTCLE primary key (id_mot) );

create table theme of theme_type(
CONSTRAINT PK_ID_THEME primary key(id_theme))
nested table monuments store as theme_monument;


create table ville of ville_type(
CONSTRAINT PK_ID_VILLE primary key(id_ville))
nested table monuments store as ville_monument;

create table monument of monument_type
( CONSTRAINT PK_ID_MONUMENT primary key (id_monument)) 
nested table medias store as monument_media;

ALTER TABLE monument ADD (SCOPE FOR (theme) IS theme);
ALTER TABLE monument ADD (SCOPE FOR (ville) IS ville);


create table media_motcle of media_motcle_type;
ALTER TABLE media_motcle ADD (SCOPE FOR (media_ref) IS media); --- Impossible
ALTER TABLE media_motcle ADD (SCOPE FOR (motcle_ref) IS motcle);

create table image of image_type(
CONSTRAINT PK_ID_PHOTO primary key(ID_MEDIA));
ALTER TABLE image ADD (SCOPE FOR (monument) IS monument);

create table audio of audio_type(
CONSTRAINT PK_ID_AUDIO primary key(ID_MEDIA));
ALTER TABLE AUDIO ADD (SCOPE FOR (monument) IS monument);

create table video of video_type(
CONSTRAINT PK_ID_VIDEO primary key(ID_MEDIA));
ALTER TABLE VIDEO ADD (SCOPE FOR (monument) IS monument);
-----------------------------------------------------------------
insert into monument values(2,'Hotel de ville',(select ref(v) from ville v where id_ville = 21000),'Edifice',TO_DATE('25/05/1989','dd/mm/yyyy'),description_type('Mairie','Effel','Muni'),(select ref(t) from theme t where id_theme=10), medias_type());

drop table st_image;
drop type st_image_type;

create type st_image_type as object(
id integer,
nom varchar,
image ORDSYS.SI_STILLIMAGE, 
instantiable final member procedure insert_line (id_ in integer, nom_ in varchar, BLOB bl));