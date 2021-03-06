PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
DROP TABLE pokemon;
CREATE TABLE pokemon (
  id INTEGER PRIMARY KEY NOT NULL,
  name TEXT NOT NULL,
  atk INTEGER NOT NULL,
  def INTEGER NOT NULL,
  stam INTEGER NOT NULL,
  candy TEXT NOT NULL,
  evolveCandy INTEGER NOT NULL
);
INSERT INTO "pokemon" VALUES(1,'Bulbasaur',126,126,90,'BULBASAUR',25);
INSERT INTO "pokemon" VALUES(2,'Ivysaur',156,158,120,'BULBASAUR',100);
INSERT INTO "pokemon" VALUES(3,'Venusaur',198,200,160,'BULBASAUR',0);
INSERT INTO "pokemon" VALUES(4,'Charmander',128,108,78,'CHARMANDER',25);
INSERT INTO "pokemon" VALUES(5,'Charmeleon',160,140,116,'CHARMANDER',100);
INSERT INTO "pokemon" VALUES(6,'Charizard',212,182,156,'CHARMANDER',0);
INSERT INTO "pokemon" VALUES(7,'Squirtle',112,142,88,'SQUIRTLE',25);
INSERT INTO "pokemon" VALUES(8,'Wartortle',144,176,118,'SQUIRTLE',100);
INSERT INTO "pokemon" VALUES(9,'Blastoise',186,222,158,'SQUIRTLE',0);
INSERT INTO "pokemon" VALUES(10,'Caterpie',62,66,90,'CATERPIE',12);
INSERT INTO "pokemon" VALUES(11,'Metapod',56,86,100,'CATERPIE',50);
INSERT INTO "pokemon" VALUES(12,'Butterfree',144,144,120,'CATERPIE',0);
INSERT INTO "pokemon" VALUES(13,'Weedle',68,64,80,'WEEDLE',12);
INSERT INTO "pokemon" VALUES(14,'Kakuna',62,82,90,'WEEDLE',50);
INSERT INTO "pokemon" VALUES(15,'Beedrill',144,130,130,'WEEDLE',0);
INSERT INTO "pokemon" VALUES(16,'Pidgey',94,90,80,'PIDGEY',12);
INSERT INTO "pokemon" VALUES(17,'Pidgeotto',126,122,126,'PIDGEY',50);
INSERT INTO "pokemon" VALUES(18,'Pidgeot',170,166,166,'PIDGEY',0);
INSERT INTO "pokemon" VALUES(19,'Rattata',92,86,60,'RATTATA',25);
INSERT INTO "pokemon" VALUES(20,'Raticate',146,150,110,'RATTATA',0);
INSERT INTO "pokemon" VALUES(21,'Spearow',102,78,80,'SPEAROW',50);
INSERT INTO "pokemon" VALUES(22,'Fearow',168,146,130,'SPEAROW',0);
INSERT INTO "pokemon" VALUES(23,'Ekans',112,112,70,'EKANS',50);
INSERT INTO "pokemon" VALUES(24,'Arbok',166,166,120,'EKANS',0);
INSERT INTO "pokemon" VALUES(25,'Pikachu',124,108,70,'PIKACHU',50);
INSERT INTO "pokemon" VALUES(26,'Raichu',200,154,120,'PIKACHU',0);
INSERT INTO "pokemon" VALUES(27,'Sandshrew',90,114,100,'SANDSHREW',50);
INSERT INTO "pokemon" VALUES(28,'Sandslash',150,172,150,'SANDSHREW',0);
INSERT INTO "pokemon" VALUES(29,'Nidoran♀',100,104,110,'NIDORAN♀',25);
INSERT INTO "pokemon" VALUES(30,'Nidorina',132,136,140,'NIDORAN♀',100);
INSERT INTO "pokemon" VALUES(31,'Nidoqueen',184,190,180,'NIDORAN♀',0);
INSERT INTO "pokemon" VALUES(32,'Nidoran♂',110,94,92,'NIDORAN♂',25);
INSERT INTO "pokemon" VALUES(33,'Nidorino',142,128,122,'NIDORAN♂',100);
INSERT INTO "pokemon" VALUES(34,'Nidoking',204,170,162,'NIDORAN♂',0);
INSERT INTO "pokemon" VALUES(35,'Clefairy',116,124,140,'CLEFAIRY',50);
INSERT INTO "pokemon" VALUES(36,'Clefable',178,178,190,'CLEFAIRY',0);
INSERT INTO "pokemon" VALUES(37,'Vulpix',106,118,76,'VULPIX',50);
INSERT INTO "pokemon" VALUES(38,'Ninetales',176,194,146,'VULPIX',0);
INSERT INTO "pokemon" VALUES(39,'Jigglypuff',98,54,230,'JIGGLYPUFF',50);
INSERT INTO "pokemon" VALUES(40,'Wigglytuff',168,108,280,'JIGGLYPUFF',0);
INSERT INTO "pokemon" VALUES(41,'Zubat',88,90,80,'ZUBAT',50);
INSERT INTO "pokemon" VALUES(42,'Golbat',164,164,150,'ZUBAT',0);
INSERT INTO "pokemon" VALUES(43,'Oddish',134,130,90,'ODDISH',25);
INSERT INTO "pokemon" VALUES(44,'Gloom',162,158,120,'ODDISH',100);
INSERT INTO "pokemon" VALUES(45,'Vileplume',202,190,150,'ODDISH',0);
INSERT INTO "pokemon" VALUES(46,'Paras',122,120,70,'PARAS',50);
INSERT INTO "pokemon" VALUES(47,'Parasect',162,170,120,'PARAS',0);
INSERT INTO "pokemon" VALUES(48,'Venonat',108,118,120,'VENONAT',50);
INSERT INTO "pokemon" VALUES(49,'Venomoth',172,154,140,'VENONAT',0);
INSERT INTO "pokemon" VALUES(50,'Diglett',108,86,20,'DIGLETT',50);
INSERT INTO "pokemon" VALUES(51,'Dugtrio',148,140,70,'DIGLETT',0);
INSERT INTO "pokemon" VALUES(52,'Meowth',104,94,80,'MEOWTH',50);
INSERT INTO "pokemon" VALUES(53,'Persian',156,146,130,'MEOWTH',0);
INSERT INTO "pokemon" VALUES(54,'Psyduck',132,112,100,'PSYDUCK',50);
INSERT INTO "pokemon" VALUES(55,'Golduck',194,176,160,'PSYDUCK',0);
INSERT INTO "pokemon" VALUES(56,'Mankey',122,96,80,'MANKEY',50);
INSERT INTO "pokemon" VALUES(57,'Primeape',178,150,130,'MANKEY',0);
INSERT INTO "pokemon" VALUES(58,'Growlithe',156,110,110,'GROWLITHE',50);
INSERT INTO "pokemon" VALUES(59,'Arcanine',230,180,180,'GROWLITHE',0);
INSERT INTO "pokemon" VALUES(60,'Poliwag',108,98,80,'POLIWAG',25);
INSERT INTO "pokemon" VALUES(61,'Poliwhirl',132,132,130,'POLIWAG',100);
INSERT INTO "pokemon" VALUES(62,'Poliwrath',180,202,180,'POLIWAG',0);
INSERT INTO "pokemon" VALUES(63,'Abra',110,76,50,'ABRA',25);
INSERT INTO "pokemon" VALUES(64,'Kadabra',150,112,80,'ABRA',100);
INSERT INTO "pokemon" VALUES(65,'Alakazam',186,152,110,'ABRA',0);
INSERT INTO "pokemon" VALUES(66,'Machop',118,96,140,'MACHOP',25);
INSERT INTO "pokemon" VALUES(67,'Machoke',154,144,160,'MACHOP',100);
INSERT INTO "pokemon" VALUES(68,'Machamp',198,180,180,'MACHOP',0);
INSERT INTO "pokemon" VALUES(69,'Bellsprout',158,78,100,'BELLSPROUT',25);
INSERT INTO "pokemon" VALUES(70,'Weepinbell',190,110,130,'BELLSPROUT',100);
INSERT INTO "pokemon" VALUES(71,'Victreebel',222,152,160,'BELLSPROUT',0);
INSERT INTO "pokemon" VALUES(72,'Tentacool',106,136,80,'TENTACOOL',50);
INSERT INTO "pokemon" VALUES(73,'Tentacruel',170,196,160,'TENTACOOL',0);
INSERT INTO "pokemon" VALUES(74,'Geodude',106,118,80,'GEODUDE',25);
INSERT INTO "pokemon" VALUES(75,'Graveler',142,156,110,'GEODUDE',100);
INSERT INTO "pokemon" VALUES(76,'Golem',176,198,160,'GEODUDE',0);
INSERT INTO "pokemon" VALUES(77,'Ponyta',168,138,100,'PONYTA',50);
INSERT INTO "pokemon" VALUES(78,'Rapidash',200,170,130,'PONYTA',0);
INSERT INTO "pokemon" VALUES(79,'Slowpoke',110,110,180,'SLOWPOKE',50);
INSERT INTO "pokemon" VALUES(80,'Slowbro',184,198,190,'SLOWPOKE',0);
INSERT INTO "pokemon" VALUES(81,'Magnemite',128,138,50,'MAGNEMITE',50);
INSERT INTO "pokemon" VALUES(82,'Magneton',186,180,100,'MAGNEMITE',0);
INSERT INTO "pokemon" VALUES(83,'Farfetch''d',138,132,104,'FARFETCH''D',0);
INSERT INTO "pokemon" VALUES(84,'Doduo',126,96,70,'DODUO',50);
INSERT INTO "pokemon" VALUES(85,'Dodrio',182,150,120,'DODUO',0);
INSERT INTO "pokemon" VALUES(86,'Seel',104,138,130,'SEEL',50);
INSERT INTO "pokemon" VALUES(87,'Dewgong',156,192,180,'SEEL',0);
INSERT INTO "pokemon" VALUES(88,'Grimer',124,110,160,'GRIMER',50);
INSERT INTO "pokemon" VALUES(89,'Muk',180,188,210,'GRIMER',0);
INSERT INTO "pokemon" VALUES(90,'Shellder',120,112,60,'SHELLDER',50);
INSERT INTO "pokemon" VALUES(91,'Cloyster',196,196,100,'SHELLDER',0);
INSERT INTO "pokemon" VALUES(92,'Gastly',136,82,60,'GASTLY',25);
INSERT INTO "pokemon" VALUES(93,'Haunter',172,118,90,'GASTLY',100);
INSERT INTO "pokemon" VALUES(94,'Gengar',204,156,120,'GASTLY',0);
INSERT INTO "pokemon" VALUES(95,'Onix',90,186,70,'ONIX',0);
INSERT INTO "pokemon" VALUES(96,'Drowzee',104,140,120,'DROWZEE',50);
INSERT INTO "pokemon" VALUES(97,'Hypno',162,196,170,'DROWZEE',0);
INSERT INTO "pokemon" VALUES(98,'Krabby',116,110,60,'KRABBY',50);
INSERT INTO "pokemon" VALUES(99,'Kingler',178,168,110,'KRABBY',0);
INSERT INTO "pokemon" VALUES(100,'Voltorb',102,124,80,'VOLTORB',50);
INSERT INTO "pokemon" VALUES(101,'Electrode',150,174,120,'VOLTORB',0);
INSERT INTO "pokemon" VALUES(102,'Exeggcute',110,132,120,'EXEGGCUTE',50);
INSERT INTO "pokemon" VALUES(103,'Exeggutor',232,164,190,'EXEGGCUTE',0);
INSERT INTO "pokemon" VALUES(104,'Cubone',102,150,100,'CUBONE',50);
INSERT INTO "pokemon" VALUES(105,'Marowak',140,202,120,'CUBONE',0);
INSERT INTO "pokemon" VALUES(106,'Hitmonlee',148,172,100,'HITMONLEE',0);
INSERT INTO "pokemon" VALUES(107,'Hitmonchan',138,204,100,'HITMONCHAN',0);
INSERT INTO "pokemon" VALUES(108,'Lickitung',126,160,180,'LICKITUNG',0);
INSERT INTO "pokemon" VALUES(109,'Koffing',136,142,80,'KOFFING',50);
INSERT INTO "pokemon" VALUES(110,'Weezing',190,198,130,'KOFFING',0);
INSERT INTO "pokemon" VALUES(111,'Rhyhorn',110,116,160,'RHYHORN',50);
INSERT INTO "pokemon" VALUES(112,'Rhydon',166,160,210,'RHYHORN',0);
INSERT INTO "pokemon" VALUES(113,'Chansey',40,60,500,'CHANSEY',0);
INSERT INTO "pokemon" VALUES(114,'Tangela',164,152,130,'TANGELA',0);
INSERT INTO "pokemon" VALUES(115,'Kangaskhan',142,178,210,'KANGASKHAN',0);
INSERT INTO "pokemon" VALUES(116,'Horsea',122,100,60,'HORSEA',50);
INSERT INTO "pokemon" VALUES(117,'Seadra',176,150,110,'HORSEA',0);
INSERT INTO "pokemon" VALUES(118,'Goldeen',112,126,90,'GOLDEEN',50);
INSERT INTO "pokemon" VALUES(119,'Seaking',172,160,160,'GOLDEEN',0);
INSERT INTO "pokemon" VALUES(120,'Staryu',130,128,60,'STARYU',50);
INSERT INTO "pokemon" VALUES(121,'Starmie',194,192,120,'STARYU',0);
INSERT INTO "pokemon" VALUES(122,'Mr. Mime',154,196,80,'MR. MIME',0);
INSERT INTO "pokemon" VALUES(123,'Scyther',176,180,140,'SCYTHER',0);
INSERT INTO "pokemon" VALUES(124,'Jynx',172,134,130,'JYNX',0);
INSERT INTO "pokemon" VALUES(125,'Electabuzz',198,160,130,'ELECTABUZZ',0);
INSERT INTO "pokemon" VALUES(126,'Magmar',214,158,130,'MAGMAR',0);
INSERT INTO "pokemon" VALUES(127,'Pinsir',184,186,130,'PINSIR',0);
INSERT INTO "pokemon" VALUES(128,'Tauros',148,184,150,'TAUROS',0);
INSERT INTO "pokemon" VALUES(129,'Magikarp',42,84,40,'MAGIKARP',400);
INSERT INTO "pokemon" VALUES(130,'Gyarados',192,196,190,'MAGIKARP',0);
INSERT INTO "pokemon" VALUES(131,'Lapras',186,190,260,'LAPRAS',0);
INSERT INTO "pokemon" VALUES(132,'Ditto',110,110,96,'DITTO',0);
INSERT INTO "pokemon" VALUES(133,'Eevee',114,128,110,'EEVEE',25);
INSERT INTO "pokemon" VALUES(134,'Vaporeon',186,168,260,'EEVEE',0);
INSERT INTO "pokemon" VALUES(135,'Jolteon',192,174,130,'EEVEE',0);
INSERT INTO "pokemon" VALUES(136,'Flareon',238,178,130,'EEVEE',0);
INSERT INTO "pokemon" VALUES(137,'Porygon',156,158,130,'PORYGON',0);
INSERT INTO "pokemon" VALUES(138,'Omanyte',132,160,70,'OMANYTE',50);
INSERT INTO "pokemon" VALUES(139,'Omastar',180,202,140,'OMANYTE',0);
INSERT INTO "pokemon" VALUES(140,'Kabuto',148,142,60,'KABUTO',50);
INSERT INTO "pokemon" VALUES(141,'Kabutops',190,190,120,'KABUTO',0);
INSERT INTO "pokemon" VALUES(142,'Aerodactyl',182,162,160,'AERODACTYL',0);
INSERT INTO "pokemon" VALUES(143,'Snorlax',180,180,320,'SNORLAX',0);
INSERT INTO "pokemon" VALUES(144,'Articuno',198,242,180,'ARTICUNO',0);
INSERT INTO "pokemon" VALUES(145,'Zapdos',232,194,180,'ZAPDOS',0);
INSERT INTO "pokemon" VALUES(146,'Moltres',242,194,180,'MOLTRES',0);
INSERT INTO "pokemon" VALUES(147,'Dratini',128,110,82,'DRATINI',25);
INSERT INTO "pokemon" VALUES(148,'Dragonair',170,152,122,'DRATINI',100);
INSERT INTO "pokemon" VALUES(149,'Dragonite',250,212,182,'DRATINI',0);
INSERT INTO "pokemon" VALUES(150,'Mewtwo',284,202,212,'MEWTWO',0);
INSERT INTO "pokemon" VALUES(151,'Mew',220,220,200,'MEW',0);
COMMIT;
