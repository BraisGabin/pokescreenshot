package com.braisgabin.pokescreenshot.processing;

public class Candy {
  private static String[][] CANDY_TYPES = {
      {"BULBASAUR", "BISAKNOSP", "HERBIZARRE"},
      {"CHARMANDER", "GLUMANDA", "SALAMÈCHE"},
      {"SQUIRTLE", "SCHIGGY", "CARAPUCE"},
      {"CATERPIE", "RAUPY", "CHENIPAN"},
      {"WEEDLE", "HORNLIU", "ASPICOT"},
      {"PIDGEY", "TAUBSI", "ROUCOOL"},
      {"RATTATA", "RATTFRATZ"},
      {"SPEAROW", "HABITAK", "PIAFABEC"},
      {"EKANS", "RETTAN", "ABO"},
      {"PIKACHU"},
      {"SANDSHREW", "SANDAN", "SABELETTE"},
      {"NIDORAN♀"},
      {"NIDORAN♂"},
      {"CLEFAIRY", "PIEPI", "MÉLOFÉE"},
      {"VULPIX", "GOUPIX"},
      {"JIGGLYPUFF", "PUMMELUFF", "RONDOUDOU"},
      {"ZUBAT", "NOSFERAPTI"},
      {"ODDISH", "MYRAPLA", "MYSTHERBE"},
      {"PARAS"},
      {"VENONAT", "BLUZUK", "MIMITOSS"},
      {"DIGLETT", "DIGDA", "TAUPIQUEUR"},
      {"MEOWTH", "MAUZI", "MIAOUSS"},
      {"PSYDUCK", "ENTON", "PSYKOKWAK"},
      {"MANKEY", "MENKI", "FÉROSINGE"},
      {"GROWLITHE", "FUKANO", "CANINOS"},
      {"POLIWAG", "QUAPSEL", "PTITARD"},
      {"ABRA"},
      {"MACHOP", "MACHOLLO", "MACHOC"},
      {"BELLSPROUT", "KNOFENSA", "CHÉTIFLOR"},
      {"TENTACOOL", "TENTACHA"},
      {"GEODUDE", "KLEINSTEIN", "RACAILLOU"},
      {"PONYTA", "PONITA"},
      {"SLOWPOKE", "FLEGMON", "RAMOLOSS"},
      {"MAGNEMITE", "MAGNETILO", "MAGNÉTI"},
      {"FARFETCH'D", "PORENTA", "CANARTICHO"},
      {"DODUO", "DODU"},
      {"SEEL", "JUROB", "OTARIA"},
      {"GRIMER", "SLEIMA", "TADMORV"},
      {"SHELLDER", "MUSCHAS", "KOKIYAS"},
      {"GASTLY", "NEBULAK", "FANTOMINUS"},
      {"ONIX"},
      {"DROWZEE", "TRAUMATO", "SOPORIFIK"},
      {"KRABBY"},
      {"VOLTORB", "VOLTOBAL", "VOLTORBE"},
      {"EXEGGCUTE", "OWEI", "NOEUNOEUF"},
      {"CUBONE", "TRAGOSSO", "OSSELAIT"},
      {"HITMONLEE", "KICKLEE"},
      {"HITMONCHAN", "NOCKCHAN", "TYGNON"},
      {"LICKITUNG", "SCHLURP", "EXCELANGUE"},
      {"KOFFING", "SMOGON", "SMOGO"},
      {"RHYHORN", "RIHORN", "RHINOCORNE"},
      {"CHANSEY", "CHANEIRA", "LEVEINARD"},
      {"TANGELA", "SAQUEDENEU"},
      {"KANGASKHAN", "KANGAMA", "KANGOUREX"},
      {"HORSEA", "SEEPER", "HYPOTREMPE"},
      {"GOLDEEN", "GOLDINI", "POISSIRÈNE"},
      {"STARYU", "STERNDU", "STARI"},
      {"MR. MIME", "PANTIMOS", "M. MIME"},
      {"SCYTHER", "SICHLOR", "INSÉCATEUR"},
      {"JYNX", "ROSSANA", "LIPPOUTOU"},
      {"ELECTABUZZ", "ELEKTEK", "ÉLEKTEK"},
      {"MAGMAR"},
      {"PINSIR", "SCARABRUTE"},
      {"TAUROS"},
      {"MAGIKARP", "KARPADOR", "MAGICARPE"},
      {"LAPRAS", "LOKHLASS"},
      {"DITTO", "MÉTAMORPH"},
      {"EEVEE", "EVOLI", "ÉVOLI"},
      {"PORYGON"},
      {"OMANYTE", "AMONITAS", "AMONITA"},
      {"KABUTO"},
      {"AERODACTYL", "PTÉRA"},
      {"SNORLAX", "RELAXO", "RONFLEX"},
      {"ARTICUNO", "ARKTOS", "ARTIKODIN"},
      {"ZAPDOS", "ÉLECTHOR"},
      {"MOLTRES", "LAVADOS", "SULFURA"},
      {"DRATINI", "MINIDRACO"},
      {"MEWTWO", "MEWTU"},
      {"MEW"},
  };

  static String candyType(String text) {
    for (String[] candies : CANDY_TYPES) {
      for (String candy : candies) {
        if (text.contains(
            candy.replace(" ", "")
                .replace("'", "")
                .replace("É", "E")
                .replace("È", "E"))) {
          return candies[0];
        }
      }
    }
    return null;
  }
}
