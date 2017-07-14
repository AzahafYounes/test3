# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table brouillon (
  id                            integer auto_increment not null,
  date_brouillons               datetime(6),
  user_id                       integer,
  offre_id                      integer,
  valide                        integer,
  isdeleted                     integer,
  isupdate                      integer,
  total_remise                  float,
  total_sans_remise             float,
  total_ppv                     float,
  constraint pk_brouillon primary key (id)
);

create table brouillon_propo_brouillon (
  brouillon_id                  integer not null,
  propo_brouillon_id            integer not null,
  constraint pk_brouillon_propo_brouillon primary key (brouillon_id,propo_brouillon_id)
);

create table famille_tarif (
  id_famille_t                  integer auto_increment not null,
  ft_code                       varchar(255),
  nom                           varchar(255),
  tva                           float,
  marge                         float,
  type                          varchar(255),
  constraint pk_famille_tarif primary key (id_famille_t)
);

create table forme (
  id_forme                      integer auto_increment not null,
  fo_desig                      varchar(255),
  focode                        varchar(255),
  constraint pk_forme primary key (id_forme)
);

create table fournisseur (
  id                            integer auto_increment not null,
  nom                           varchar(255),
  email                         varchar(255),
  adress                        varchar(255),
  tel                           varchar(255),
  fix1                          varchar(255),
  fix2                          varchar(255),
  fix3                          varchar(255),
  fax                           varchar(255),
  frcode                        varchar(255),
  constraint pk_fournisseur primary key (id)
);

create table group_officine (
  id                            integer auto_increment not null,
  nbr_officine                  integer,
  nom_group                     varchar(255),
  zone                          varchar(255),
  observation                   varchar(255),
  user_id                       integer,
  constraint pk_group_officine primary key (id)
);

create table group_officine_officine (
  group_officine_id             integer not null,
  officine_id                   integer not null,
  constraint pk_group_officine_officine primary key (group_officine_id,officine_id)
);

create table initialisation_situation (
  id                            integer auto_increment not null,
  date_reguelement              datetime(6),
  montant                       float,
  user_id                       integer,
  user_dist_id                  integer,
  constraint pk_initialisation_situation primary key (id)
);

create table livraison (
  id                            integer auto_increment not null,
  date_livraison                datetime(6),
  user_id                       integer,
  user_dist_id                  integer,
  brouillon_id                  integer,
  valide                        integer,
  total                         float,
  total_ppv                     float,
  total_sans_remise             float,
  constraint pk_livraison primary key (id)
);

create table livraison_propo_livree (
  livraison_id                  integer not null,
  propo_livree_id               integer not null,
  constraint pk_livraison_propo_livree primary key (livraison_id,propo_livree_id)
);

create table notification (
  id                            integer auto_increment not null,
  type                          varchar(255),
  delai                         integer,
  message                       varchar(255),
  is_deleted                    tinyint(1) default 0,
  constraint pk_notification primary key (id)
);

create table observation (
  id                            integer auto_increment not null,
  objet                         varchar(255),
  message                       varchar(255),
  date_observation              datetime(6),
  admin_id                      integer,
  constraint pk_observation primary key (id)
);

create table observation_utilisateur (
  observation_id                integer not null,
  utilisateur_id                integer not null,
  constraint pk_observation_utilisateur primary key (observation_id,utilisateur_id)
);

create table officine (
  id                            integer auto_increment not null,
  nom_officine                  varchar(255),
  adress                        varchar(255),
  inpe                          varchar(255),
  fix                           varchar(255),
  user_id                       integer,
  manager                       integer,
  constraint pk_officine primary key (id)
);

create table offre_officine (
  id                            integer auto_increment not null,
  date_offre                    datetime(6),
  date_limit                    datetime(6),
  user_id                       integer,
  fournisseur_id                integer,
  publie                        integer,
  complete                      integer,
  afficher                      integer,
  groupe_id                     integer,
  expiree                       integer,
  total_non_remise              float,
  total_remise                  float,
  constraint pk_offre_officine primary key (id)
);

create table offre_officine_proposition (
  offre_officine_id             integer not null,
  proposition_id                integer not null,
  constraint pk_offre_officine_proposition primary key (offre_officine_id,proposition_id)
);

create table produit (
  id                            integer auto_increment not null,
  fournisseur_id                integer,
  nom                           varchar(255),
  prix                          float,
  pph                           float,
  code_barre                    varchar(255),
  forme_id_forme                integer,
  famille_tarif_id_famille_t    integer,
  is_new                        integer,
  date_ajout                    datetime(6),
  user_id                       integer,
  constraint pk_produit primary key (id)
);

create table propo_brouillon (
  id                            integer auto_increment not null,
  produit_id                    integer,
  qte                           integer,
  total_remise                  float,
  total_sans_remise             float,
  total_ppv                     float,
  constraint pk_propo_brouillon primary key (id)
);

create table propo_livree (
  id                            integer auto_increment not null,
  produit_id                    integer,
  qte                           integer,
  type_offre                    integer,
  remise                        float,
  unite_gratuit                 integer,
  total                         float,
  total_ppv                     float,
  totalsan_remis                float,
  constraint pk_propo_livree primary key (id)
);

create table proposition (
  id                            integer auto_increment not null,
  date_proposition              datetime(6),
  produit_id                    integer,
  qte                           integer,
  remise                        float,
  unit_gratuit                  integer,
  type_offre                    integer,
  qte_reste                     integer,
  total_remise                  float,
  total_sans_remise             float,
  total_ppv                     float,
  constraint pk_proposition primary key (id)
);

create table user_notif (
  id_notification               bigint auto_increment not null,
  date_notif                    datetime(6),
  message                       varchar(255),
  lu                            tinyint(1) default 0,
  admin_lu                      tinyint(1) default 0,
  notifadminset                 tinyint(1) default 0,
  notifcommercset               tinyint(1) default 0,
  offre                         tinyint(1) default 0,
  offre_user_id                 integer,
  user_id                       integer,
  type                          varchar(255),
  constraint pk_user_notif primary key (id_notification)
);

create table utilisateur (
  id                            integer auto_increment not null,
  nom                           varchar(255),
  prenom                        varchar(255),
  email                         varchar(255),
  identifiant                   varchar(255),
  password                      varchar(255),
  role                          varchar(255),
  tel                           varchar(255),
  status                        varchar(255),
  mac                           varchar(255),
  photo                         varchar(255),
  isdeleted                     tinyint(1) default 0,
  constraint pk_utilisateur primary key (id)
);

alter table brouillon add constraint fk_brouillon_user_id foreign key (user_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_brouillon_user_id on brouillon (user_id);

alter table brouillon add constraint fk_brouillon_offre_id foreign key (offre_id) references offre_officine (id) on delete restrict on update restrict;
create index ix_brouillon_offre_id on brouillon (offre_id);

alter table brouillon_propo_brouillon add constraint fk_brouillon_propo_brouillon_brouillon foreign key (brouillon_id) references brouillon (id) on delete restrict on update restrict;
create index ix_brouillon_propo_brouillon_brouillon on brouillon_propo_brouillon (brouillon_id);

alter table brouillon_propo_brouillon add constraint fk_brouillon_propo_brouillon_propo_brouillon foreign key (propo_brouillon_id) references propo_brouillon (id) on delete restrict on update restrict;
create index ix_brouillon_propo_brouillon_propo_brouillon on brouillon_propo_brouillon (propo_brouillon_id);

alter table group_officine add constraint fk_group_officine_user_id foreign key (user_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_group_officine_user_id on group_officine (user_id);

alter table group_officine_officine add constraint fk_group_officine_officine_group_officine foreign key (group_officine_id) references group_officine (id) on delete restrict on update restrict;
create index ix_group_officine_officine_group_officine on group_officine_officine (group_officine_id);

alter table group_officine_officine add constraint fk_group_officine_officine_officine foreign key (officine_id) references officine (id) on delete restrict on update restrict;
create index ix_group_officine_officine_officine on group_officine_officine (officine_id);

alter table initialisation_situation add constraint fk_initialisation_situation_user_id foreign key (user_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_initialisation_situation_user_id on initialisation_situation (user_id);

alter table initialisation_situation add constraint fk_initialisation_situation_user_dist_id foreign key (user_dist_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_initialisation_situation_user_dist_id on initialisation_situation (user_dist_id);

alter table livraison add constraint fk_livraison_user_id foreign key (user_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_livraison_user_id on livraison (user_id);

alter table livraison add constraint fk_livraison_user_dist_id foreign key (user_dist_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_livraison_user_dist_id on livraison (user_dist_id);

alter table livraison add constraint fk_livraison_brouillon_id foreign key (brouillon_id) references brouillon (id) on delete restrict on update restrict;
create index ix_livraison_brouillon_id on livraison (brouillon_id);

alter table livraison_propo_livree add constraint fk_livraison_propo_livree_livraison foreign key (livraison_id) references livraison (id) on delete restrict on update restrict;
create index ix_livraison_propo_livree_livraison on livraison_propo_livree (livraison_id);

alter table livraison_propo_livree add constraint fk_livraison_propo_livree_propo_livree foreign key (propo_livree_id) references propo_livree (id) on delete restrict on update restrict;
create index ix_livraison_propo_livree_propo_livree on livraison_propo_livree (propo_livree_id);

alter table observation add constraint fk_observation_admin_id foreign key (admin_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_observation_admin_id on observation (admin_id);

alter table observation_utilisateur add constraint fk_observation_utilisateur_observation foreign key (observation_id) references observation (id) on delete restrict on update restrict;
create index ix_observation_utilisateur_observation on observation_utilisateur (observation_id);

alter table observation_utilisateur add constraint fk_observation_utilisateur_utilisateur foreign key (utilisateur_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_observation_utilisateur_utilisateur on observation_utilisateur (utilisateur_id);

alter table officine add constraint fk_officine_user_id foreign key (user_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_officine_user_id on officine (user_id);

alter table offre_officine add constraint fk_offre_officine_user_id foreign key (user_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_offre_officine_user_id on offre_officine (user_id);

alter table offre_officine add constraint fk_offre_officine_fournisseur_id foreign key (fournisseur_id) references fournisseur (id) on delete restrict on update restrict;
create index ix_offre_officine_fournisseur_id on offre_officine (fournisseur_id);

alter table offre_officine add constraint fk_offre_officine_groupe_id foreign key (groupe_id) references group_officine (id) on delete restrict on update restrict;
create index ix_offre_officine_groupe_id on offre_officine (groupe_id);

alter table offre_officine_proposition add constraint fk_offre_officine_proposition_offre_officine foreign key (offre_officine_id) references offre_officine (id) on delete restrict on update restrict;
create index ix_offre_officine_proposition_offre_officine on offre_officine_proposition (offre_officine_id);

alter table offre_officine_proposition add constraint fk_offre_officine_proposition_proposition foreign key (proposition_id) references proposition (id) on delete restrict on update restrict;
create index ix_offre_officine_proposition_proposition on offre_officine_proposition (proposition_id);

alter table produit add constraint fk_produit_fournisseur_id foreign key (fournisseur_id) references fournisseur (id) on delete restrict on update restrict;
create index ix_produit_fournisseur_id on produit (fournisseur_id);

alter table produit add constraint fk_produit_forme_id_forme foreign key (forme_id_forme) references forme (id_forme) on delete restrict on update restrict;
create index ix_produit_forme_id_forme on produit (forme_id_forme);

alter table produit add constraint fk_produit_famille_tarif_id_famille_t foreign key (famille_tarif_id_famille_t) references famille_tarif (id_famille_t) on delete restrict on update restrict;
create index ix_produit_famille_tarif_id_famille_t on produit (famille_tarif_id_famille_t);

alter table produit add constraint fk_produit_user_id foreign key (user_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_produit_user_id on produit (user_id);

alter table propo_brouillon add constraint fk_propo_brouillon_produit_id foreign key (produit_id) references produit (id) on delete restrict on update restrict;
create index ix_propo_brouillon_produit_id on propo_brouillon (produit_id);

alter table propo_livree add constraint fk_propo_livree_produit_id foreign key (produit_id) references produit (id) on delete restrict on update restrict;
create index ix_propo_livree_produit_id on propo_livree (produit_id);

alter table proposition add constraint fk_proposition_produit_id foreign key (produit_id) references produit (id) on delete restrict on update restrict;
create index ix_proposition_produit_id on proposition (produit_id);

alter table user_notif add constraint fk_user_notif_offre_user_id foreign key (offre_user_id) references offre_officine (id) on delete restrict on update restrict;
create index ix_user_notif_offre_user_id on user_notif (offre_user_id);

alter table user_notif add constraint fk_user_notif_user_id foreign key (user_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_user_notif_user_id on user_notif (user_id);


# --- !Downs

alter table brouillon drop foreign key fk_brouillon_user_id;
drop index ix_brouillon_user_id on brouillon;

alter table brouillon drop foreign key fk_brouillon_offre_id;
drop index ix_brouillon_offre_id on brouillon;

alter table brouillon_propo_brouillon drop foreign key fk_brouillon_propo_brouillon_brouillon;
drop index ix_brouillon_propo_brouillon_brouillon on brouillon_propo_brouillon;

alter table brouillon_propo_brouillon drop foreign key fk_brouillon_propo_brouillon_propo_brouillon;
drop index ix_brouillon_propo_brouillon_propo_brouillon on brouillon_propo_brouillon;

alter table group_officine drop foreign key fk_group_officine_user_id;
drop index ix_group_officine_user_id on group_officine;

alter table group_officine_officine drop foreign key fk_group_officine_officine_group_officine;
drop index ix_group_officine_officine_group_officine on group_officine_officine;

alter table group_officine_officine drop foreign key fk_group_officine_officine_officine;
drop index ix_group_officine_officine_officine on group_officine_officine;

alter table initialisation_situation drop foreign key fk_initialisation_situation_user_id;
drop index ix_initialisation_situation_user_id on initialisation_situation;

alter table initialisation_situation drop foreign key fk_initialisation_situation_user_dist_id;
drop index ix_initialisation_situation_user_dist_id on initialisation_situation;

alter table livraison drop foreign key fk_livraison_user_id;
drop index ix_livraison_user_id on livraison;

alter table livraison drop foreign key fk_livraison_user_dist_id;
drop index ix_livraison_user_dist_id on livraison;

alter table livraison drop foreign key fk_livraison_brouillon_id;
drop index ix_livraison_brouillon_id on livraison;

alter table livraison_propo_livree drop foreign key fk_livraison_propo_livree_livraison;
drop index ix_livraison_propo_livree_livraison on livraison_propo_livree;

alter table livraison_propo_livree drop foreign key fk_livraison_propo_livree_propo_livree;
drop index ix_livraison_propo_livree_propo_livree on livraison_propo_livree;

alter table observation drop foreign key fk_observation_admin_id;
drop index ix_observation_admin_id on observation;

alter table observation_utilisateur drop foreign key fk_observation_utilisateur_observation;
drop index ix_observation_utilisateur_observation on observation_utilisateur;

alter table observation_utilisateur drop foreign key fk_observation_utilisateur_utilisateur;
drop index ix_observation_utilisateur_utilisateur on observation_utilisateur;

alter table officine drop foreign key fk_officine_user_id;
drop index ix_officine_user_id on officine;

alter table offre_officine drop foreign key fk_offre_officine_user_id;
drop index ix_offre_officine_user_id on offre_officine;

alter table offre_officine drop foreign key fk_offre_officine_fournisseur_id;
drop index ix_offre_officine_fournisseur_id on offre_officine;

alter table offre_officine drop foreign key fk_offre_officine_groupe_id;
drop index ix_offre_officine_groupe_id on offre_officine;

alter table offre_officine_proposition drop foreign key fk_offre_officine_proposition_offre_officine;
drop index ix_offre_officine_proposition_offre_officine on offre_officine_proposition;

alter table offre_officine_proposition drop foreign key fk_offre_officine_proposition_proposition;
drop index ix_offre_officine_proposition_proposition on offre_officine_proposition;

alter table produit drop foreign key fk_produit_fournisseur_id;
drop index ix_produit_fournisseur_id on produit;

alter table produit drop foreign key fk_produit_forme_id_forme;
drop index ix_produit_forme_id_forme on produit;

alter table produit drop foreign key fk_produit_famille_tarif_id_famille_t;
drop index ix_produit_famille_tarif_id_famille_t on produit;

alter table produit drop foreign key fk_produit_user_id;
drop index ix_produit_user_id on produit;

alter table propo_brouillon drop foreign key fk_propo_brouillon_produit_id;
drop index ix_propo_brouillon_produit_id on propo_brouillon;

alter table propo_livree drop foreign key fk_propo_livree_produit_id;
drop index ix_propo_livree_produit_id on propo_livree;

alter table proposition drop foreign key fk_proposition_produit_id;
drop index ix_proposition_produit_id on proposition;

alter table user_notif drop foreign key fk_user_notif_offre_user_id;
drop index ix_user_notif_offre_user_id on user_notif;

alter table user_notif drop foreign key fk_user_notif_user_id;
drop index ix_user_notif_user_id on user_notif;

drop table if exists brouillon;

drop table if exists brouillon_propo_brouillon;

drop table if exists famille_tarif;

drop table if exists forme;

drop table if exists fournisseur;

drop table if exists group_officine;

drop table if exists group_officine_officine;

drop table if exists initialisation_situation;

drop table if exists livraison;

drop table if exists livraison_propo_livree;

drop table if exists notification;

drop table if exists observation;

drop table if exists observation_utilisateur;

drop table if exists officine;

drop table if exists offre_officine;

drop table if exists offre_officine_proposition;

drop table if exists produit;

drop table if exists propo_brouillon;

drop table if exists propo_livree;

drop table if exists proposition;

drop table if exists user_notif;

drop table if exists utilisateur;

