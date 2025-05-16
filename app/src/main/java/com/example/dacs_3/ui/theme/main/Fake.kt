package com.example.dacs_3.ui.theme.main
import com.example.dacs_3.model.Instruction
import com.example.dacs_3.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Random

// Khởi tạo Firestore
val firestore = FirebaseFirestore.getInstance()

// Hàm cập nhật timestamp giả cho tất cả Recipe trong Firestore
fun updateRecipeTimestamps() {
    val db = FirebaseFirestore.getInstance()
    val recipesCollection = db.collection("recipes")

    // Thời gian bắt đầu (16/05/2024, 1 năm trước)
    val startTime = 1715839200000L // 16/05/2024 00:00:00 UTC+7
    // Thời gian kết thúc (hiện tại, 16/05/2025)
    val endTime = System.currentTimeMillis()

    // Random để tạo timestamp giả
    val random = Random()

    recipesCollection.get()
        .addOnSuccessListener { snapshot ->
            val documents = snapshot.documents
            for (doc in documents) {
                // Tạo timestamp giả trong khoảng startTime đến endTime
                val fakeTimestamp = startTime + ((endTime - startTime) * random.nextDouble()).toLong()

                // Cập nhật document với trường timestamp
                doc.reference.update("timestamp", fakeTimestamp)
                    .addOnSuccessListener {
                        println("Updated timestamp for recipe ${doc.id}: $fakeTimestamp")
                    }
                    .addOnFailureListener { e ->
                        println("Error updating timestamp for recipe ${doc.id}: $e")
                    }
            }
            println("Completed updating timestamps for ${documents.size} recipes")
        }
        .addOnFailureListener { e ->
            println("Error fetching recipes: $e")
        }
}


// Hàm thêm món ăn vào Firestore và cập nhật recipeId
fun addRecipeToFirestore(recipe: Recipe, onComplete: (Recipe) -> Unit) {
    // Tạo một reference đến collection "recipes"
    val recipeCollection = firestore.collection("recipes")

    // Đẩy dữ liệu lên Firestore
    recipeCollection.add(recipe)
        .addOnSuccessListener { documentReference ->
            // Cập nhật recipeId với document ID từ Firestore
            val updatedRecipe = recipe.copy(recipeId = documentReference.id)

            // Gọi callback để trả lại recipe đã cập nhật
            onComplete(updatedRecipe)

            println("Món ăn đã được thêm với ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            println("Lỗi khi thêm món ăn: $e")
        }
}

// Danh sách các món ăn
val recipes = listOf(
    Recipe(
        title = "Cajun Bell Pepper Shrimp Stir-Fry",
        story = "Is this Cajun Bell Pepper Shrimp Stir-Fry delicious? For those who enjoy and love the flavors of Italian cuisine, this dish is definitely worth trying. However, for those more accustomed to Japanese or Chinese dishes, I recommend trying recipes with crawfish first.\n" +
                "This dish is rich in vitamins A and C, thanks to the bell peppers – a super source of vitamin C and metabolized vitamin A – plus lemon juice, which is extremely beneficial for the body.",
        servingSize = "2 people",
        cookingTime = "30 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/e61afc3a03f26dbe/680x964cq70/tom-xao-%E1%BB%9Bt-chuong-s%E1%BB%91t-cajun-recipe-main-photo.webp",
        ingredients = listOf(
            "400g shrimp",
            "1 green bell pepper",
            "1 red bell pepper",
            "2 tablespoons Cajun seasoning (about 20g)",
            "5-6 garlic cloves",
            "20g butter (no need to bring to room temperature)",
            "1 tablespoon lemon or lime juice",
            "Salt, sugar (to taste)"
        ),
        instructions = listOf(
            Instruction(stepNumber = 1, description = "Rinse the shrimp, peel, and devein. Marinate the shrimp with a pinch of salt (about 1-2g)."),
            Instruction(stepNumber = 2, description = "Peel and mince the garlic."),
            Instruction(stepNumber = 3, description = "Wash the green and red bell peppers, then dice them. Note: You can replace the red bell pepper with a yellow one for a more vibrant look. Optionally, add corn or onion for extra sweetness and balanced colors."),
            Instruction(stepNumber = 4, description = "Heat a pan over medium heat. Drizzle with a little cooking oil. Note: You can use olive oil for an authentic flavor."),
            Instruction(stepNumber = 5, description = "Add half of the minced garlic and sauté with the diced bell peppers until nearly cooked. This takes about 3-5 minutes."),
            Instruction(stepNumber = 6, description = "Transfer the sautéed mixture to a plate."),
            Instruction(stepNumber = 7, description = "Add the remaining half of the minced garlic and the shrimp to the pan, then quickly add the butter. Lower the heat. Note: Choose animal-based butter for a richer, creamier flavor compared to margarine."),
            Instruction(stepNumber = 8, description = "When the shrimp start turning red (about 1-2 minutes), flip them. Then, add salt, pepper, and Cajun seasoning."),
            Instruction(stepNumber = 9, description = "Add the sautéed vegetables from step 6 back to the pan and stir well. Adjust seasoning to taste.")
        ),
        userId = "Ey6lmMLz77gJxFNLll4OFsrSsRl1"
    ),
    Recipe(
        title = "Salad Ức Gà Áp Chảo Sốt Mù Tạt",
        story = "Món salad low carb nhẹ cho ngày ăn kiêng",
        servingSize = "2 người",
        cookingTime = "20 phút",
        resultImages = "https://img-global.cpcdn.com/recipes/b40b2b3d9289080c/680x964cq70/salad-%E1%BB%A9c-ga-ap-ch%E1%BA%A3o-s%E1%BB%91t-mu-t%E1%BA%A1t-recipe-main-photo.webp",
        ingredients = listOf(
            "2 chicken breasts",
            "8 lettuce leaves",
            "2 tomatoes",
            "4 small beets",
            "1 apple",
            "1 garlic clove",
            "1/2 teaspoon salt",
            "1/2 teaspoon black pepper",
            "1/2 teaspoon paprika",
            "1 shallot",
            "1 tablespoon olive oil",
            "1 tablespoon balsamic vinegar",
            "1/4 teaspoon salt",
            "1/3 teaspoon black pepper",
            "Dijon mustard (quantity to taste)"
        ),
        instructions = listOf(
            Instruction(
                stepNumber = 1,
                description = "Flatten the chicken breasts and marinate with minced garlic, salt, black pepper, and paprika. Pan-sear both sides until cooked.",
                imageUrl = listOf(
                    "https://img-global.cpcdn.com/steps/973ccf3a45eef8ce/160x128cq70/salad-%E1%BB%A9c-ga-ap-ch%E1%BA%A3o-s%E1%BB%91t-mu-t%E1%BA%A1t-recipe-step-1-photo.webp",
                    "https://img-global.cpcdn.com/steps/1cf4bce4ef9d9b61/160x128cq70/salad-%E1%BB%A9c-ga-ap-ch%E1%BA%A3o-s%E1%BB%91t-mu-t%E1%BA%A1t-recipe-step-1-photo.webp",
                    "https://img-global.cpcdn.com/steps/2bbd11b286fb8791/160x128cq70/salad-%E1%BB%A9c-ga-ap-ch%E1%BA%A3o-s%E1%BB%91t-mu-t%E1%BA%A1t-recipe-step-1-photo.webp"
                )
            ),
            Instruction(
                stepNumber = 2,
                description = "Chop lettuce, tomatoes, beets, and apple into small pieces and place in a bowl. Mix with the dressing made from the listed ingredients (olive oil, balsamic vinegar, salt, black pepper). Plate the salad, arrange sliced chicken on top, and sprinkle Dijon mustard seeds over the chicken.",
                imageUrl = listOf(
                    "https://img-global.cpcdn.com/steps/97e2cc60b567bbad/160x128cq70/salad-%E1%BB%A9c-ga-ap-ch%E1%BA%A3o-s%E1%BB%91t-mu-t%E1%BA%A1t-recipe-step-2-photo.webp")
            ),
            Instruction(
                stepNumber = 3,
                description = "Bon appétit!",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/3649200d938091da/160x128cq70/salad-%E1%BB%A9c-ga-ap-ch%E1%BA%A3o-s%E1%BB%91t-mu-t%E1%BA%A1t-recipe-step-3-photo.webp")
            )
        ),
        userId = "Ey6lmMLz77gJxFNLll4OFsrSsRl1",

        ),
    Recipe(
        title = "Creamy Spaghetti",
        story = "This Creamy Spaghetti dish is a comforting delight, perfect for gatherings with friends or family. The rich, velvety sauce made from whipping cream and fresh milk pairs beautifully with the smoky notes of cured ham, creating a luxurious yet simple meal. Ideal for those who love indulgent Italian-inspired flavors, this recipe brings warmth and satisfaction to every bite, making it a go-to for cozy dinners or special occasions.",
        servingSize = "6 people",
        cookingTime = "45 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/a8c14f7644f9815d/680x964f0.5_0.50125_1.0q70/mi-y-s%E1%BB%91t-kem-recipe-main-photo.webp",
        ingredients = listOf(
            "200g spaghetti",
            "200ml whipping cream",
            "200ml fresh milk",
            "1/2 teaspoon pink salt",
            "A small Navier of cured ham",
            "1/4 onion",
            "A small amount of parsley (for garnish)"
        ),
        instructions = listOf(
            Instruction(
                stepNumber = 1,
                description = "Boil water with a pinch of salt and cook the spaghetti for about 12 minutes until al dente.",
                imageUrl = listOf(
                    "https://img-global.cpcdn.com/steps/efb57ac5b0cec48d/160x128cq70/mi-y-s%E1%BB%91t-kem-recipe-step-1-photo.webp",
                    "https://img-global.cpcdn.com/steps/8e217c4fef4be5c4/160x128cq70/mi-y-s%E1%BB%91t-kem-recipe-step-1-photo.webp",
                    "https://img-global.cpcdn.com/steps/d6b399e7396fcce5/160x128cq70/mi-y-s%E1%BB%91t-kem-recipe-step-1-photo.webp")
            ),
            Instruction(
                stepNumber = 2,
                description = "Heat a pan and pan-sear the cured ham. Add the onion and sauté for 1 minute, then pour in the fresh milk and whipping cream, bringing the mixture to a boil.",
                imageUrl = listOf(
                    "https://img-global.cpcdn.com/steps/b5144515cd81b114/160x128cq70/mi-y-s%E1%BB%91t-kem-recipe-step-2-photo.webp",
                    "https://img-global.cpcdn.com/steps/e6cec77136deec48/160x128cq70/mi-y-s%E1%BB%91t-kem-recipe-step-2-photo.webp",
                    "https://img-global.cpcdn.com/steps/342213b9f14703c8/160x128cq70/mi-y-s%E1%BB%91t-kem-recipe-step-2-photo.webp")
            ),
            Instruction(
                stepNumber = 3,
                description = "Add the cooked spaghetti to the pan, stir well, and season to taste.",
                imageUrl = listOf(
                    "https://cookpad.com/vn/step_attachment/images/1731e500042ac9dc?image_region_id=24",
                    "https://cookpad.com/vn/step_attachment/images/4988850fc949cc2a?image_region_id=24")
            ),
            Instruction(
                stepNumber = 4,
                description = "Plate the spaghetti and garnish with parsley.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/8ba3d48d4da2c22c/160x128cq70/mi-y-s%E1%BB%91t-kem-recipe-step-4-photo.webp")
            )
        ),
        userId = "Ey6lmMLz77gJxFNLll4OFsrSsRl1",

        ),
    Recipe(
        title = "Russian Salad",
        story = "Russian Salad is a fresh and vibrant dish, perfect for family gatherings or festive occasions. It combines crunchy vegetables like carrots, onions, and sweet corn, all tossed together with creamy mayonnaise. This easy-to-make salad is a refreshing treat for hot summer days or a great side dish to accompany grilled meals.",
        servingSize = "4 people",
        cookingTime = "20 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/b04d2e9352949437/680x964f0.661976_0.5_1.0q70/salad-nga-recipe-main-photo.webp",
        ingredients = listOf(
            "1/2 carrot",
            "1/2 onion",
            "1/2 corn cob",
            "1 potato",
            "3-5 green beans",
            "2 sausages",
            "1 slice of deli meat",
            "3 pickled baby cucumbers",
            "100g canned peas",
            "2 boiled eggs",
            "2-3 tablespoons mayonnaise",
            "A pinch of salt"
        ),
        instructions = listOf(
            Instruction(
                stepNumber = 1,
                description = "Dice all the vegetables (carrot, onion, potato, green beans), remove corn kernels from the cob, and boil them with a pinch of salt until cooked. Boil the eggs until hard-boiled. Drain the mixture in a colander and let it cool.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/3be7617dae8472dd/160x128cq70/salad-nga-recipe-step-1-photo.webp",
                    "https://img-global.cpcdn.com/steps/408b05c381cf3878/160x128cq70/salad-nga-recipe-step-1-photo.webp")
            ),
            Instruction(
                stepNumber = 2,
                description = "Drain the canned peas and rinse them. Dice the egg whites, sausages, deli meat, and pickled baby cucumbers.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/e9fd49606bb71351/160x128cq70/salad-nga-recipe-step-2-photo.webp")
            ),
            Instruction(
                stepNumber = 3,
                description = "Combine the mixtures from steps 1 and 2 in a large bowl. Add 1/2 teaspoon of salt, crumble the egg yolks, and mix thoroughly with 2-3 tablespoons of mayonnaise. Serve the salad on a plate and enjoy.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/d818271bff78d013/160x128cq70/salad-nga-recipe-step-3-photo.webp")
            )
        ),
        userId = "Ey6lmMLz77gJxFNLll4OFsrSsRl1"

    ),
    Recipe(
        title = "Raclette",
        story = """Raclette is a traditional and famous dish from France, especially popular in the Alps region and commonly enjoyed during winter. The name of the dish comes from the raclette cheese, which originated in Switzerland but is beloved in France.

        The traditional way of enjoying raclette is very unique: the cheese is slowly melted next to a fire or under a grill, and the melted cheese is then poured over accompaniments such as boiled potatoes, pickled cucumbers, pickled onions, and various cold cuts (charcuterie).

        The rich, warm flavor of the cheese blends perfectly with the creamy taste of the potatoes and the savory flavors of the cold cuts, creating an appealing culinary experience that's perfect for family or friend gatherings on cold days.""",
        servingSize = "4 people",
        cookingTime = "30 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/ae194b7972d28973/680x964f0.473427_0.5_1.0q70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-main-photo.webp",
        ingredients = listOf(
            "Comté (Cheese)",
            "Serrano (Ham)",
            "Boiled Potatoes with Salt",
            "Pickled Cucumbers"
        ),
        instructions = listOf(
            Instruction(stepNumber = 1, description = "Boil the potatoes with plenty of salt for 35-45 minutes to create a salted crust on the skin while keeping the inside of the potatoes soft and sticky. You can also boil the potatoes normally without salt if you prefer, depending on the family's taste.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/52442469499320fb/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-1-photo.webp")),
            Instruction(stepNumber = 2, description = "Prepare the cheese and cold cuts.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/4d0e388f3f799822/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-2-photo.webp",
                    "https://img-global.cpcdn.com/steps/337131c7c0101a29/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-2-photo.webp",
                    "https://img-global.cpcdn.com/steps/47ee7db0d130a67e/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-2-photo.webp")),
            Instruction(stepNumber = 3, description = "Heat the raclette machine and place a slice of cheese on the individual tray. Put it in the machine for a few minutes to let the cheese melt.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/dab867b128990116/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-3-photo.webp",
                    "https://img-global.cpcdn.com/steps/2fdc60b64b55b68c/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-3-photo.webp")),
            Instruction(stepNumber = 4, description = "Place the melted cheese on the hot potatoes and enjoy with cold cuts and side dishes. When you remove the hot cheese, add another piece to the grill so that you can continue enjoying hot cheese without waiting. Though, you will still have to wait a bit because the eating speed is always faster than the cooking speed, so patience is key. During this time, side dishes are a great choice.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/2a967891f1e3701c/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-4-photo.webp",
                    "https://img-global.cpcdn.com/steps/6c4343b580ea37b4/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-4-photo.webp",
                    "https://img-global.cpcdn.com/steps/296df35c61a843b9/160x128cq70/raclette-mon-phap-truy%E1%BB%81n-th%E1%BB%91ng-mua-l%E1%BA%A1nh-v%E1%BB%9Bi-pho-mai-nong-ch%E1%BA%A3y-jambon-khoai-tay-recipe-step-4-photo.webp"))
        ),

        userId = "Ey6lmMLz77gJxFNLll4OFsrSsRl1"

    ),
    Recipe(
        title = "Pain d’epices",
        story = """Pain d’épices combined with foie gras is an exquisite dish. The slight sweetness of the cake, blended with spices like cinnamon, star anise, and ginger, helps balance the rich fattiness of the foie gras. The cake is typically baked until crispy (or not), served with thin slices of foie gras and fruit jams such as fig or pear, creating a unique harmony of flavors.

    This dish is a characteristic feature of festive occasions or the luxurious menus of French cuisine, and it’s perfectly suited for potluck parties, classified as an appetizer pairing dish.""",
        servingSize = "6 people",
        cookingTime = "10 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/94db5516cb683054/680x964f0.467644_0.5_1.0q70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-main-photo.webp",
        ingredients = listOf(
            "1 gingerbread loaf (spiced bread)",
            "1 piece of French foie gras"
        ),
        instructions = listOf(
            Instruction(stepNumber = 1, description = "The bread can be homemade or bought. If homemade, prepare it 1-2 days in advance to achieve the perfect texture when enjoyed. Slice the bread into small square pieces, just enough for one bite. You can toast them for a crispier texture or leave them soft as preferred.", imageUrl = listOf("https://img-global.cpcdn.com/steps/7994d2c7194fb58f/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-1-photo.webp",
                "https://img-global.cpcdn.com/steps/b4b731b19e7249e9/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-1-photo.webp")),
            Instruction(stepNumber = 2, description = "Slice the foie gras using a specialized wire cutter.", imageUrl = listOf("https://img-global.cpcdn.com/steps/256a760e5c953511/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-2-photo.webp",
                "https://img-global.cpcdn.com/steps/cf50fc172741de06/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-2-photo.webp")),
            Instruction(stepNumber = 3, description = "Arrange the foie gras slices on top of the bread slices. You can decorate the arrangement however you like, depending on the host’s creativity. At this stage, you may also spread some fig jam or onion confit (a type of thick fruit sauce slowly cooked, not the sweet jam typically spread on bread), though this is optional and based on taste.", imageUrl = listOf("https://img-global.cpcdn.com/steps/bc811c1807a8e714/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-3-photo.webp",
                "https://img-global.cpcdn.com/steps/41ca570599b0cac1/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-3-photo.webp",
                "https://img-global.cpcdn.com/steps/731514f65d8565bb/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-3-photo.webp")),
            Instruction(stepNumber = 4, description = "Serve with champagne 🍾 as an appetizer before moving on to the main courses.", imageUrl = listOf("https://img-global.cpcdn.com/steps/67beb3caf1ff23a6/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-4-photo.webp",
                "https://img-global.cpcdn.com/steps/27c6ec045cb230cd/160x128cq70/pain-depices-banh-mi-g%E1%BB%ABng-gan-ng%E1%BB%97ng-phap-recipe-step-4-photo.webp"))
        ),

        userId = "xZQ4TCwx7phDW1xd4WmIUehCnaG2"

    ),
    Recipe(
        title = "Crispy Bacon Mushroom Salad",
        story = "A salad with the fragrant essence of the Provence region, made with a simple recipe.",
        servingSize = "2 people",
        cookingTime = "15 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/a6cd162ec75dac3d/680x964f0.511304_0.5_1.0q70/salad-n%E1%BA%A5m-m%E1%BB%A1-chay-bacon-recipe-main-photo.webp",
        ingredients = listOf(
            "400g button mushrooms",
            "100g bacon",
            "1 garlic clove",
            "1/2 teaspoon dried Provence herbs",
            "Lettuce of your choice",
            "Favorite salad dressing (I use mustard olive oil dressing)"
        ),
        instructions = listOf(
            Instruction(stepNumber = 1, description = "Chop the bacon and button mushrooms into small pieces.", imageUrl = listOf("https://img-global.cpcdn.com/steps/0b72814863e74220/160x128cq70/salad-n%E1%BA%A5m-m%E1%BB%A1-chay-bacon-recipe-step-1-photo.webp",
                "https://img-global.cpcdn.com/steps/c99b3e444ed7ec64/160x128cq70/salad-n%E1%BA%A5m-m%E1%BB%A1-chay-bacon-recipe-step-1-photo.webp")),
            Instruction(stepNumber = 2, description = "Add the bacon and minced garlic to the pan, stirring until the bacon releases its fat. Then add the mushrooms and 1/2 teaspoon of dried Provence herbs.", imageUrl = listOf("https://img-global.cpcdn.com/steps/9de3518c4af3de47/160x128cq70/salad-n%E1%BA%A5m-m%E1%BB%A1-chay-bacon-recipe-step-2-photo.webp",
                "https://img-global.cpcdn.com/steps/ebee6a90249b3adf/160x128cq70/salad-n%E1%BA%A5m-m%E1%BB%A1-chay-bacon-recipe-step-2-photo.webp",
                "https://img-global.cpcdn.com/steps/40244e6de0b09c8f/160x128cq70/salad-n%E1%BA%A5m-m%E1%BB%A1-chay-bacon-recipe-step-2-photo.webp")),
            Instruction(stepNumber = 3, description = "Stir until the mushrooms and bacon are browned and crispy at the edges. No seasoning is needed as the bacon provides enough salt.", imageUrl = listOf("https://img-global.cpcdn.com/steps/212e139c77b13d55/160x128cq70/salad-n%E1%BA%A5m-m%E1%BB%A1-chay-bacon-recipe-step-3-photo.webp")),
            Instruction(stepNumber = 4, description = "Serve on a plate of prepared lettuce and top with your favorite salad dressing.", imageUrl = listOf("https://img-global.cpcdn.com/steps/9cb66f1e52201307/160x128cq70/salad-n%E1%BA%A5m-m%E1%BB%A1-chay-bacon-recipe-step-4-photo.webp"))
        ),
        userId = "xZQ4TCwx7phDW1xd4WmIUehCnaG2"

    ),
    Recipe(
        title = "Italian Puntarelle Salad",
        story = """Puntarelle Salad is a traditional Italian dish, popular in Lazio and Rome. Puntarelle, a type of chicory, has a crisp texture and a mild bitterness. Available from late fall to early spring, it’s typically soaked in cold water to reduce bitterness and increase crispness. The salad is often paired with an anchovy dressing, garlic, olive oil, and sometimes vinegar. This dish is not only delicious but also packed with fiber, vitamins, and minerals, making it a healthy and refreshing choice.""",
        servingSize = "2 people",
        cookingTime = "20 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/a44182d5a580dad8/680x964cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-main-photo.webp",
        ingredients = listOf(
            "1 bunch of bitter puntarelle kale",
            "1/2 cup olive oil",
            "2 garlic cloves",
            "1 small can of anchovies in oil",
            "1/2 teaspoon ground black pepper",
            "2 boiled eggs"
        ),

        instructions = listOf(
            Instruction(stepNumber = 1, description = "The large leaves of this bitter kale are very bitter, similar to dandelion. For those who can tolerate it, you can sauté, cook, or eat it raw as you like. In this salad, we only use the tender inner buds. Separate each bud, cut off the tough stems, and slice or shred them. Soak the buds in ice-cold water for 10 minutes to make them crispy. After soaking, the kale will curl up a little.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/27173a3606f9fcbb/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-1-photo.webp",
                    "https://img-global.cpcdn.com/steps/dfc7562951d551a5/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-1-photo.webp",
                    "https://img-global.cpcdn.com/steps/94523c9e9e2f11c7/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-1-photo.webp")),
            Instruction(stepNumber = 2, description = "Chop the garlic and cut the anchovies into small pieces. Mix them well with the olive oil to create the dressing for this delicious salad.",
                imageUrl = listOf("https://img-global.cpcdn.com/steps/f195326d3138e845/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-2-photo.webp",
                    "https://img-global.cpcdn.com/steps/59c7c5f65c2429da/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-2-photo.webp",
                    "https://img-global.cpcdn.com/steps/512d4efe8403763e/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-2-photo.webp")),
            Instruction(stepNumber = 3, description = "Drain the kale after soaking, then toss it with the dressing you just made.", imageUrl = listOf("https://img-global.cpcdn.com/steps/34d29e34c923e8e3/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-3-photo.webp",
                "https://img-global.cpcdn.com/steps/34d29e34c923e8e3/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-3-photo.webp",
                "https://img-global.cpcdn.com/steps/2329f02b13c03854/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-3-photo.webp")),
            Instruction(stepNumber = 4, description = "Serve immediately to enjoy the fresh, crispy, and sweet flavor of the tender puntarelle buds, complemented by the savory anchovy dressing.", imageUrl = listOf("https://img-global.cpcdn.com/steps/4b7984db46e7f543/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-4-photo.webp",
                "https://img-global.cpcdn.com/steps/169fba9a60bce49e/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-4-photo.webp",
                "https://img-global.cpcdn.com/steps/7352597c85969885/160x128cq70/salad-puntarelle-ki%E1%BB%83u-y-recipe-step-4-photo.webp"))
        ),

        userId = "xZQ4TCwx7phDW1xd4WmIUehCnaG2"

    ),
    Recipe(
        title = "Pan-Seared Cod",
        story = """I followed the recipe from @cook_14296975 and tried making this dish. I usually make a large batch to keep in the fridge because every time I make it, I cut a lot of different fruits (I'm really into fruits). After cutting, I end up with a big bowl full! The best part is that this dish tastes even better the longer it sits, so no worries, chefs!""",
        servingSize = "1 person",
        cookingTime = "10 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/4ec9e76d68459b79/680x964f0.5_0.533917_1.0q70/ca-tuy%E1%BA%BFt-ap-ch%E1%BA%A3o-salad-trai-cay-recipe-main-photo.webp",
        ingredients = listOf(
            "200g cod fillet",
            "Pepper",
            "Olive oil"
        ),
        instructions = listOf(
            Instruction(stepNumber = 1, description = "Use the leftover fruit salad from the previous day. Click the link to view the recipe.", imageUrl = listOf("https://img-global.cpcdn.com/steps/81f1790deff818b6/160x128cq70/ca-tuy%E1%BA%BFt-ap-ch%E1%BA%A3o-salad-trai-cay-recipe-step-1-photo.webp")),
            Instruction(stepNumber = 2, description = "Pan-sear the fish: Heat a pan, add 1 tablespoon of olive oil, then place the fish in the pan. Use high heat to sear both sides until golden brown. This way, the fish will be crispy on the outside but juicy and tender inside. Finally, sprinkle pepper on top.", imageUrl = listOf("https://img-global.cpcdn.com/steps/f0a4d4a2f247910f/160x128cq70/ca-tuy%E1%BA%BFt-ap-ch%E1%BA%A3o-salad-trai-cay-recipe-step-2-photo.webp")),
            Instruction(stepNumber = 3, description = "Plate the dish: Start with the salad to avoid water from the salad affecting the presentation. Then, place the seared fish on top. Tilt the plate for a better photo angle 😂😂😂. Place a sprig of rosemary next to the fish for a graceful touch, and sprinkle a bit of pepper on top for the finishing touch.", imageUrl = listOf("https://img-global.cpcdn.com/steps/be3fd247745a8973/160x128cq70/ca-tuy%E1%BA%BFt-ap-ch%E1%BA%A3o-salad-trai-cay-recipe-step-3-photo.webp"))
        ),
        userId = "xZQ4TCwx7phDW1xd4WmIUehCnaG2"

    ),
    Recipe(
        title = "Pan-Seared Salmon",
        story = "",
        servingSize = "1 person",
        cookingTime = "30 minutes",
        resultImages = "https://img-global.cpcdn.com/recipes/bf74df25527f1cc3/680x964f0.5_0.47681_1.0q70/finger-food-10-ca-h%E1%BB%93i-ap-ch%E1%BA%A3o-s%E1%BB%91t-cay-th%E1%BA%ADt-cay-recipe-main-photo.webp",
        ingredients = listOf(
            "300g salmon fillet",
            "1 tsp seasoning powder",
            "1 tbsp olive oil",
            "1 tbsp chili sauce",
            "1 tbsp Sriracha chili sauce",
            "1 tsp HQ chili powder",
            "1 tbsp apple cider vinegar",
            "1 tbsp soy sauce",
            "1 tsp brown sugar",
            "Toasted sesame seeds",
            "Seasonings: garlic powder, pepper"
        ),
        instructions = listOf(
            Instruction(stepNumber = 1, description = "Prepare the sauce: Mix together all ingredients including oil, chili sauce, chili powder, soy sauce, sugar, garlic powder, and pepper.", imageUrl = listOf("https://img-global.cpcdn.com/steps/bf7b601ebba4d639/160x128cq70/finger-food-10-ca-h%E1%BB%93i-ap-ch%E1%BA%A3o-s%E1%BB%91t-cay-th%E1%BA%ADt-cay-recipe-step-1-photo.webp")),
            Instruction(stepNumber = 2, description = "Cut the salmon fillet into chunks (remove the skin if you don’t like it). Marinate with seasoning powder and 1 tbsp olive oil for at least 15 minutes."),
            Instruction(stepNumber = 3, description = "Heat oil in a pan, sear the skin side of the salmon until golden and crispy, then cook the other sides.", imageUrl = listOf("https://img-global.cpcdn.com/steps/0ae5940c9fe117e8/160x128cq70/finger-food-10-ca-h%E1%BB%93i-ap-ch%E1%BA%A3o-s%E1%BB%91t-cay-th%E1%BA%ADt-cay-recipe-step-3-photo.webp")),
            Instruction(stepNumber = 4, description = "Remove excess oil, leave the salmon in the pan, increase the heat, pour the sauce in, and turn the salmon quickly. Once the sauce thickens, turn off the heat. Sprinkle toasted sesame seeds on top of the salmon."),
            Instruction(stepNumber = 5, description = "Serve the salmon on a plate. Skewer with skewers.", imageUrl = listOf("https://img-global.cpcdn.com/steps/48cefbffa2fd3b59/160x128cq70/finger-food-10-ca-h%E1%BB%93i-ap-ch%E1%BA%A3o-s%E1%BB%91t-cay-th%E1%BA%ADt-cay-recipe-step-5-photo.webp"))
        ),
        userId = "xZQ4TCwx7phDW1xd4WmIUehCnaG2"

    )
)

// Sử dụng vòng lặp để thêm tất cả món ăn vào Firestore và cập nhật recipeId

