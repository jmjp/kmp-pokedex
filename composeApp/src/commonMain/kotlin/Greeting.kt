import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.random.Random

class Greeting {
    private val platform = getPlatform()
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    
    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
    
    suspend fun greeting(): Pokemon {
        val pokenames = arrayOf("bulbasaur", "ivysaur", "venusaur", "charmander", "charmeleon",
                                "charizard", "squirtle", "wartortle", "blastoise", "caterpie", "metapod",
                                "butterfree", "weedle", "kakuna", "beedrill", "pidgey", "pidgeotto",
                                "pidgeot", "rattata", "raticate", "spearow", "fearow", "ekans", "arbok",
                                "pikachu", "raichu", "sandshrew", "sandslash", "nidoran", "nidorina", "nidoqueen",
                                "nidoran", "nidorino", "nidoking", "clefairy", "clefable", "vulpix", "ninetales", "jigglypuff"
                                , "wigglytuff", "zubat", "golbat", "oddish", "gloom", "vileplume", "paras", "parasect","venonat",
                                "venomoth", "diglett", "dugtrio", "meowth", "persian", "psyduck", "golduck", "mankey", "primeape",
                                "growlithe", "arcanine", "poliwag", "poliwhirl", "poliwrath", "abra", "kadabra", "alakazam", "machop",
                                "machoke", "machamp", "bellsprout", "weepinbell", "victreebel", "tentacool", "tentacruel", "geodude", "graveler",
                                "golem", "ponyta", "rapidash", "slowpoke", "slowbro", "magnemite", "magneton", "farfetch'd", "doduo", "dodrio", "seel",
                                "dewgong", "grimer", "muk", "shellder", "cloyster", "gastly", "haunter", "gengar", "onix", "drowzee", "hypno", "krabby",
                                "kingler", "voltorb", "electrode", "exeggcute", "exeggutor", "cubone", "marowak", "hitmonlee", "hitmonchan", "lickitung", "koffing",
                                "weezing", "rhyhorn", "rhydon", "chansey", "tangela", "kangaskhan", "horsea", "seadra", "goldeen",
                                "seaking", "staryu", "starmie")
        val randomIndex = Random.nextInt(0, pokenames.size - 1)
        val randomPokeName = pokenames[randomIndex]
        val response = client.get("https://pokeapi.co/api/v2/pokemon/${randomPokeName}" )
        return response.body<Pokemon>()
    }
}