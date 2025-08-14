package com.ao.recipes.data.local

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.ao.recipes.data.Recipe
import com.ao.recipes.data.RecipeType
import androidx.core.graphics.createBitmap
import com.ao.recipes.R

/**
 * A static data store of [Recipe]s that loads images as Bitmaps using Coil.
 */
object LocalRecipesDataProvider {

    private suspend fun loadImageAsBitmap(context: Context, @androidx.annotation.DrawableRes drawableResId: Int): Bitmap {
        val request = ImageRequest.Builder(context)
            .data(drawableResId)
            .allowHardware(false) // Disable hardware bitmaps for wider compatibility if needed
            .build()
        val result = context.imageLoader.execute(request).drawable
        return result?.toBitmap() ?: createBitmap(100, 100) // Fallback placeholder
    }

    suspend fun getAllRecipes(context: Context): List<Recipe> = listOf(
        Recipe(
            id = 1,
            name = "Bolo de Cenoura",
            description = "Bolo de cenoura com cobertura de chocolate",
            ingredients = "2 xícaras de farinha de trigo\n" +
                    "2 xícaras de açúcar\n" +
                    "3 cenouras médias\n" +
                    "4 ovos",
            steps = "1. Pré-aqueça o forno a 180°C e unte uma forma com furo central.\n" +
                    "2. No liquidificador, bata as cenouras, o óleo e os ovos até obter uma mistura homogênea.\n" +
                    "3. Em uma tigela, misture a farinha de trigo e o açúcar. Adicione os ingredientes líquidos batidos no liquidificador e misture bem até incorporar todos os ingredientes.\n" +
                    "4. Adicione o fermento em pó e misture delicadamente.\n" +
                    "5. Despeje a massa na forma untada e leve ao forno por aproximadamente 40 minutos, ou até que esteja dourado e firme ao toque.\n",
            type = RecipeType.DESERT,
            picture = loadImageAsBitmap(context, R.drawable.bolo_cenoura),
        ),
        Recipe(
            id = 2,
            name = "Brigadeiro",
            description = "Doce tradicional brasileiro",
            ingredients = "1 lata de leite condensado\n" +
                    "1 colher de sopa de manteiga sem sal\n" +
                    "3 colheres de sopa de chocolate em pó",
            steps = "1. Em uma panela, misture o leite condensado, a manteiga e o chocolate em pó.\n" +
                    "2. Cozinhe em fogo médio, mexendo continuamente, até que a mistura comece a desgrudar do fundo da panela (ponto de brigadeiro).\n" +
                    "3. Transfira o brigadeiro para um prato untado com manteiga e deixe esfriar.\n" +
                    "4. Com as mãos untadas com manteiga, enrole o brigadeiro em pequenas bolinhas.\n" +
                    "5. Passe as bolinhas no granulado de chocolate e coloque em forminhas de papel.\n",
            type = RecipeType.DESERT,
            picture = loadImageAsBitmap(context, R.drawable.brigadeiro),
        ),
        Recipe (
            id = 3,
            name = "Feijoada",
            description = "Prato típico brasileiro",
            ingredients = "500g de feijão preto\n" +
                    "200g de carne seca dessalgada\n" +
                    "150g de paio\n" +
                    "150g de linguiça calabresa\n" +
                    "1 cebola picada\n" +
                    "3 dentes de alho picados\n" +
                    "2 folhas de louro\n" +
                    "Sal a gosto\n" +
                    "Pimenta a gosto\n" +
                    "Cheiro-verde picado a gosto",
            steps = "1. Deixe o feijão preto de molho em água por pelo menos 4 horas.\n" +
                    "2. Em uma panela de pressão, cozinhe o feijão com as folhas de louro por cerca de 20-30 minutos após o início da pressão.\n" +
                    "3. Em outra panela, cozinhe as carnes com água para retirar o excesso de sal e gordura. Escorra e corte em pedaços.\n" +
                    "4. Em uma panela grande, refogue a cebola e o alho. Adicione as carnes e refogue por mais alguns minutos.\n" +
                    "5. Adicione o feijão cozido (com o caldo), sal, pimenta e cheiro-verde. Cozinhe por mais 20-30 minutos para apurar os sabores.\n" +
                    "6. Sirva com arroz branco, couve refogada, farofa e rodelas de laranja.\n",
            type = RecipeType.MAIN_COURSE,
            picture = loadImageAsBitmap(context, R.drawable.feijoada),
        ),
        Recipe (
            id = 4,
            name = "Pão de Queijo",
            description = "Bolinho de queijo tradicional de Minas Gerais",
            ingredients = "500g de polvilho doce\n" +
                    "125ml de água\n" +
                    "125ml de leite\n" +
                    "125ml de óleo vegetal\n" +
                    "10g de sal\n" +
                    "2 ovos grandes\n" +
                    "250g de queijo meia cura ralado\n" +
                    "100g de queijo parmesão ralado",
            steps = "1. Pré-aqueça o forno a 200°C.\n" +
                    "2. Em uma panela, ferva a água, o leite, o óleo e o sal. Despeje essa mistura fervente sobre o polvilho doce em uma tigela grande e misture bem.\n" +
                    "3. Adicione os ovos, um de cada vez, misturando até incorporar completamente.\n" +
                    "4. Acrescente os queijos ralados e misture até obter uma massa homogênea.\n" +
                    "5. Faça bolinhas com a massa e coloque em uma assadeira untada ou forrada com papel manteiga. Asse por cerca de 20-25 minutos, ou até que os pães de queijo estejam dourados e crescidos.\n",
            type = RecipeType.APPETIZER,
            picture = loadImageAsBitmap(context, R.drawable.paoqueijo),
        ),
        Recipe (
            id = 5,
            name = "Moqueca de Peixe",
            description = "Ensopado de peixe com leite de coco e dendê",
            ingredients = "800g de filé de peixe (robalo, badejo ou outro peixe firme)\n" +
                    "3 tomates maduros picados\n" +
                    "1 pimentão vermelho picado\n" +
                    "1 pimentão verde picado\n" +
                    "1 cebola grande picada\n" +
                    "4 dentes de alho amassados\n" +
                    "1 maço de coentro picado\n" +
                    "1 maço de cebolinha picada\n" +
                    "200ml de leite de coco\n" +
                    "2 colheres de sopa de azeite de dendê\n" +
                    "Suco de 1 limão\n" +
                    "Sal e pimenta a gosto",
            steps = "1. Corte o peixe em pedaços médios, tempere com sal, pimenta e suco de limão. Deixe marinar por 20 minutos.\n" +
                    "2. Em uma panela grande, coloque metade dos tomates, pimentões, cebola, alho, coentro e cebolinha.\n" +
                    "3. Disponha os pedaços de peixe sobre essa cama de vegetais.\n" +
                    "4. Cubra com o restante dos vegetais.\n" +
                    "5. Regue com o leite de coco e o azeite de dendê.\n" +
                    "6. Tampe a panela e cozinhe em fogo médio por cerca de 15-20 minutos, até que o peixe esteja cozido.\n" +
                    "7. Sirva com arroz branco e pirão feito com o caldo da moqueca.\n",
            type = RecipeType.MAIN_COURSE,
            picture = loadImageAsBitmap(context, R.drawable.feijoada), // Assuming this was intended, or replace with specific image
        ),
        Recipe (
            id = 6,
            name = "Coxinha",
            description = "Salgadinho frito em formato de gota recheado com frango",
            ingredients = "Para a massa:\n" +
                    "500ml de caldo de galinha\n" +
                    "2 colheres de sopa de manteiga\n" +
                    "2 xícaras de farinha de trigo\n" +
                    "Sal a gosto\n\n" +
                    "Para o recheio:\n" +
                    "300g de peito de frango cozido e desfiado\n" +
                    "1 cebola picada\n" +
                    "2 dentes de alho picados\n" +
                    "1 tomate sem pele e sem sementes, picado\n" +
                    "1 colher de sopa de salsinha picada\n" +
                    "Sal e pimenta a gosto\n\n" +
                    "Para empanar:\n" +
                    "2 ovos batidos\n" +
                    "Farinha de rosca\n" +
                    "Óleo para fritar",
            steps = "1. Para o recheio: Refogue a cebola e o alho em um pouco de óleo. Adicione o frango desfiado, o tomate, sal e pimenta. Cozinhe por alguns minutos. Desligue o fogo e adicione a salsinha. Reserve.\n" +
                    "2. Para a massa: Ferva o caldo de galinha com a manteiga e o sal. Quando ferver, adicione a farinha de trigo de uma vez e mexa vigorosamente até formar uma massa homogênea que desgrude da panela.\n" +
                    "3. Transfira a massa para uma superfície lisa e sove até esfriar o suficiente para manusear.\n" +
                    "4. Pegue pequenas porções da massa, abra na palma da mão, coloque um pouco de recheio no centro e modele no formato de gota (coxinha).\n" +
                    "5. Passe as coxinhas na farinha de trigo, depois nos ovos batidos e por fim na farinha de rosca.\n" +
                    "6. Frite em óleo quente até dourar. Escorra em papel toalha.\n",
            type = RecipeType.APPETIZER,
            picture = loadImageAsBitmap(context, R.drawable.paoqueijo), // Assuming this was intended, or replace with specific image
        ),
        Recipe (
            id = 7,
            name = "Caldo Verde",
            description = "Sopa portuguesa adaptada à culinária brasileira",
            ingredients = "4 batatas médias descascadas e cortadas em cubos\n" +
                    "1 cebola média picada\n" +
                    "2 dentes de alho picados\n" +
                    "1 maço de couve manteiga cortada em tiras finas\n" +
                    "150g de linguiça calabresa defumada cortada em rodelas\n" +
                    "2 colheres de sopa de azeite de oliva\n" +
                    "1,5 litro de água ou caldo de legumes\n" +
                    "Sal e pimenta a gosto",
            steps = "1. Em uma panela grande, refogue a cebola e o alho no azeite até ficarem transparentes.\n" +
                    "2. Adicione as batatas e a água (ou caldo). Tempere com sal e pimenta.\n" +
                    "3. Cozinhe em fogo médio até as batatas ficarem bem macias, cerca de 20 minutos.\n" +
                    "4. Com um mixer ou liquidificador, bata a sopa até ficar cremosa.\n" +
                    "5. Volte a sopa ao fogo, adicione a linguiça calabresa e deixe cozinhar por mais 5 minutos.\n" +
                    "6. Por último, adicione a couve fatiada e cozinhe por apenas 2 minutos para manter a cor verde viva.\n" +
                    "7. Sirva quente, regado com um fio de azeite de oliva.\n",
            type = RecipeType.SOUP,
            picture = loadImageAsBitmap(context, R.drawable.feijoada), // Assuming this was intended, or replace with specific image
        ),
        Recipe (
            id = 8,
            name = "Pudim de Leite Condensado",
            description = "Sobremesa cremosa tradicional brasileira",
            ingredients = "Para o pudim:\n" +
                    "1 lata de leite condensado\n" +
                    "2 latas de leite (use a lata de leite condensado como medida)\n" +
                    "3 ovos\n\n" +
                    "Para a calda:\n" +
                    "1 xícara de açúcar\n" +
                    "1/2 xícara de água",
            steps = "1. Pré-aqueça o forno a 180°C.\n" +
                    "2. Prepare a calda: Em uma panela, derreta o açúcar em fogo médio até ficar dourado. Adicione a água com cuidado (vai borbulhar) e mexa até dissolver todos os cristais de açúcar. Despeje a calda em uma forma de pudim e gire a forma para cobrir o fundo e as laterais.\n" +
                    "3. No liquidificador, bata o leite condensado, o leite e os ovos até ficar homogêneo.\n" +
                    "4. Despeje a mistura na forma caramelizada.\n" +
                    "5. Coloque a forma em banho-maria (dentro de uma assadeira com água quente) e leve ao forno por cerca de 40-45 minutos, ou até que esteja firme.\n" +
                    "6. Deixe esfriar completamente e leve à geladeira por pelo menos 4 horas.\n" +
                    "7. Para desenformar, passe uma faca nas bordas e vire sobre um prato com borda.\n",
            type = RecipeType.DESERT,
            picture = loadImageAsBitmap(context, R.drawable.bolo_cenoura), // Assuming this was intended, or replace with specific image
        ),
        Recipe (
            id = 9,
            name = "Salada de Palmito",
            description = "Salada refrescante com palmito, tomate e cebola",
            ingredients = "2 latas de palmito em conserva, escorrido e cortado em rodelas\n" +
                    "3 tomates maduros cortados em cubos\n" +
                    "1 cebola roxa fatiada finamente\n" +
                    "1/2 pimentão vermelho picado\n" +
                    "1/4 xícara de azeitonas verdes fatiadas\n" +
                    "2 colheres de sopa de salsinha picada\n" +
                    "3 colheres de sopa de azeite de oliva\n" +
                    "2 colheres de sopa de vinagre de vinho tinto\n" +
                    "Sal e pimenta a gosto",
            steps = "1. Em uma tigela grande, combine o palmito, os tomates, a cebola, o pimentão e as azeitonas.\n" +
                    "2. Em uma tigela pequena, misture o azeite, o vinagre, o sal e a pimenta para fazer o molho.\n" +
                    "3. Regue a salada com o molho e misture delicadamente.\n" +
                    "4. Polvilhe com a salsinha picada.\n" +
                    "5. Deixe descansar na geladeira por pelo menos 30 minutos antes de servir para que os sabores se misturem.\n",
            type = RecipeType.SALAD,
            picture = loadImageAsBitmap(context, R.drawable.paoqueijo), // Assuming this was intended, or replace with specific image
        ),
        Recipe (
            id = 10,
            name = "Acarajé",
            description = "Bolinho frito de feijão-fradinho típico da Bahia",
            ingredients = "500g de feijão-fradinho descascado\n" +
                    "1 cebola grande\n" +
                    "2 dentes de alho\n" +
                    "1 colher de chá de sal\n" +
                    "Azeite de dendê para fritar\n\n" +
                    "Para o recheio:\n" +
                    "200g de camarão seco\n" +
                    "1 cebola picada\n" +
                    "2 tomates picados\n" +
                    "Pimenta malagueta a gosto\n" +
                    "Vatapá e caruru a gosto",
            steps = "1. Deixe o feijão de molho em água por 12 horas. Escorra e retire as películas.\n" +
                    "2. No processador, bata o feijão com a cebola e o alho até formar uma massa homogênea.\n" +
                    "3. Transfira para uma tigela e bata vigorosamente com uma colher de pau por cerca de 15 minutos, incorporando ar à massa.\n" +
                    "4. Adicione o sal e continue batendo por mais 5 minutos.\n" +
                    "5. Aqueça o azeite de dendê em uma panela funda.\n" +
                    "6. Com uma colher, pegue porções da massa e frite no azeite quente até dourar dos dois lados.\n" +
                    "7. Escorra em papel toalha.\n" +
                    "8. Abra os acarajés ao meio e recheie com camarão seco refogado, vatapá, caruru e pimenta a gosto.\n",
            type = RecipeType.APPETIZER,
            picture = loadImageAsBitmap(context, R.drawable.paoqueijo), // Assuming this was intended, or replace with specific image
        ),
        Recipe (
            id = 11,
            name = "Canjica",
            description = "Doce cremoso de milho branco",
            ingredients = "500g de milho branco para canjica\n" +
                    "1 litro de leite\n" +
                    "1 lata de leite condensado\n" +
                    "200ml de leite de coco\n" +
                    "100g de coco ralado\n" +
                    "1 canela em pau\n" +
                    "5 cravos-da-índia\n" +
                    "1/2 xícara de açúcar (ajuste conforme o gosto)\n" +
                    "Canela em pó para polvilhar",
            steps = "1. Lave bem o milho e deixe de molho em água por 12 horas.\n" +
                    "2. Escorra o milho e cozinhe em uma panela de pressão com água por cerca de 30 minutos, ou até que esteja macio.\n" +
                    "3. Escorra a água e transfira o milho para uma panela grande.\n" +
                    "4. Adicione o leite, o leite condensado, o leite de coco, o coco ralado, a canela em pau, os cravos e o açúcar.\n" +
                    "5. Cozinhe em fogo baixo, mexendo ocasionalmente, por cerca de 20-30 minutos, ou até engrossar.\n" +
                    "6. Retire a canela em pau e os cravos.\n" +
                    "7. Sirva quente ou fria, polvilhada com canela em pó.\n",
            type = RecipeType.DESERT,
            picture = loadImageAsBitmap(context, R.drawable.brigadeiro), // Assuming this was intended, or replace with specific image
        ),
        Recipe (
            id = 12,
            name = "Maniçoba",
            description = "Prato típico paraense feito com folhas de mandioca",
            ingredients = "2kg de folhas de mandioca moídas (maniva)\n" +
                    "500g de carne seca\n" +
                    "500g de costela de porco\n" +
                    "300g de paio\n" +
                    "300g de linguiça calabresa\n" +
                    "200g de toucinho defumado\n" +
                    "200g de charque\n" +
                    "3 dentes de alho amassados\n" +
                    "1 cebola picada\n" +
                    "Sal a gosto",
            steps = "1. Ferva as folhas de mandioca moídas em água por pelo menos 7 dias, trocando a água diariamente (este processo é necessário para eliminar o ácido cianídrico, que é tóxico).\n" +
                    "2. Dessalgue as carnes salgadas, deixando-as de molho em água por 12 horas e trocando a água algumas vezes.\n" +
                    "3. Corte todas as carnes em pedaços e refogue com alho e cebola.\n" +
                    "4. Adicione as folhas de mandioca já cozidas e continue o cozimento por mais 3 horas em fogo baixo.\n" +
                    "5. Ajuste o sal se necessário.\n" +
                    "6. Sirva com arroz branco e farinha d'água.\n",
            type = RecipeType.MAIN_COURSE,
            picture = loadImageAsBitmap(context, R.drawable.feijoada), // Assuming this was intended, or replace with specific image
        ),
    )

    /**
     * Get a [Recipe] with the given [id].
     */
    suspend fun get(context: Context, id: Long): Recipe? {
        return getAllRecipes(context).firstOrNull { it.id == id }
    }

    /**
     * Create a new, blank [Recipe].
     */
    suspend fun create(context: Context): Recipe {
        val newId = (getAllRecipes(context).maxOfOrNull { it.id } ?: 0) + 1
        // Using a default image for new recipes. Consider adding a specific placeholder drawable.
        val defaultBitmap = loadImageAsBitmap(context, R.drawable.bolo_cenoura) 
        return Recipe(
            id = newId, 
            name = "", 
            description = "", 
            ingredients = "", 
            steps = "", 
            type = RecipeType.MAIN_COURSE, 
            picture = defaultBitmap,
        )
    }
}
