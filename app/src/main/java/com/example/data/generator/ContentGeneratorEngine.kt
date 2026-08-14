package com.example.data.generator

import com.example.BuildConfig
import com.example.data.model.ContentCategory
import com.example.data.model.ContentLanguage
import com.example.data.model.ContentTone
import com.example.data.model.GeneratedContent
import com.example.data.model.SocialPlatform
import com.example.data.model.SubCategory
import com.example.data.model.VideoDuration
import com.example.data.remote.GeminiApiService
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiGenerationConfig
import com.example.data.remote.GeminiPart
import com.example.data.remote.GeminiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContentGeneratorEngine(
    private val apiService: GeminiApiService = GeminiApiService.create()
) {

    suspend fun generateContent(
        platform: SocialPlatform,
        category: ContentCategory,
        subCategory: SubCategory,
        topic: String,
        language: ContentLanguage,
        tone: ContentTone,
        duration: VideoDuration = VideoDuration.SEC_30
    ): GeneratedContent = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val effectiveTopic = if (topic.isBlank()) "${category.titleEn} - ${subCategory.titleEn}" else topic.trim()

        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = buildPrompt(
                    platform = platform,
                    category = category,
                    subCategory = subCategory,
                    topic = effectiveTopic,
                    language = language,
                    tone = tone,
                    duration = duration
                )

                val request = GeminiRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = prompt))
                        )
                    ),
                    generationConfig = GeminiGenerationConfig(
                        temperature = 0.75f,
                        topP = 0.95f
                    )
                )

                val response = apiService.generateContent(apiKey = apiKey, request = request)
                val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

                if (!rawText.isNullOrBlank()) {
                    val parsed = parseGeneratedText(
                        rawText = rawText,
                        platform = platform,
                        category = category,
                        subCategory = subCategory,
                        language = language,
                        tone = tone,
                        topic = effectiveTopic
                    )
                    if (parsed != null) {
                        return@withContext parsed
                    }
                }
            } catch (e: Exception) {
                // Fallback to local intelligent generator
            }
        }

        // Fallback or offline realistic smart generator
        return@withContext generateSmartFallback(
            platform = platform,
            category = category,
            subCategory = subCategory,
            topic = effectiveTopic,
            language = language,
            tone = tone,
            duration = duration
        )
    }

    private fun buildPrompt(
        platform: SocialPlatform,
        category: ContentCategory,
        subCategory: SubCategory,
        topic: String,
        language: ContentLanguage,
        tone: ContentTone,
        duration: VideoDuration
    ): String {
        val langInstruction = when (language) {
            ContentLanguage.BENGALI -> "Bengali (বাংলা ভাষায় সুন্দর ও সাবলীলভাবে লিখুন)"
            ContentLanguage.ENGLISH -> "English (Modern, captivating, concise)"
            ContentLanguage.BANGLISH -> "Banglish (Bengali written in English letters, conversational youth style)"
            ContentLanguage.HINDI -> "Hindi (हिंदी भाषा)"
        }

        return """
        You are an elite Viral Social Media Content Creator & Scriptwriter for ${platform.title}.
        
        Task: Generate complete high-engagement social media content based on:
        - Target Platform: ${platform.title} (${platform.aspectDescription})
        - Category: ${category.titleEn} (${category.titleBn})
        - Sub-category: ${subCategory.titleEn} (${subCategory.titleBn})
        - Topic / Theme: $topic
        - Tone / Style: ${tone.labelEn} (${tone.labelBn}) ${tone.emoji}
        - Target Video Duration: ${duration.label}
        - Target Language: $langInstruction

        Strict Output Structure:
        Respond ONLY using these exact section headers:

        ===TITLE===
        (A high CTR, catchy headline/title for this video/post)

        ===HOOK===
        (The first 1-3 seconds scroll-stopping visual & spoken hook to capture maximum retention)

        ===SCRIPT===
        (The full structured body script formatted with [Visual / দৃশ্য] cues and [Audio / ভয়েসওভার / কথা] dialogue cues, perfectly timed for ${duration.seconds} seconds)

        ===CAPTION===
        (Compelling caption for the social media post with a strong Call-To-Action asking viewers to comment/share/follow)

        ===HASHTAGS===
        (10-15 high-reach trending hashtags including specific niche tags and platform tags, space-separated)
        """.trimIndent()
    }

    private fun parseGeneratedText(
        rawText: String,
        platform: SocialPlatform,
        category: ContentCategory,
        subCategory: SubCategory,
        language: ContentLanguage,
        tone: ContentTone,
        topic: String
    ): GeneratedContent? {
        try {
            val titleRegex = "===TITLE===\\s*([\\s\\S]*?)(?====HOOK===|$)".toRegex()
            val hookRegex = "===HOOK===\\s*([\\s\\S]*?)(?====SCRIPT===|$)".toRegex()
            val scriptRegex = "===SCRIPT===\\s*([\\s\\S]*?)(?====CAPTION===|$)".toRegex()
            val captionRegex = "===CAPTION===\\s*([\\s\\S]*?)(?====HASHTAGS===|$)".toRegex()
            val hashtagsRegex = "===HASHTAGS===\\s*([\\s\\S]*?)$".toRegex()

            val title = titleRegex.find(rawText)?.groupValues?.get(1)?.trim()
                ?: "Viral ${subCategory.titleEn} for ${platform.title}"
            val hook = hookRegex.find(rawText)?.groupValues?.get(1)?.trim()
                ?: "এই ভিডিওটা মিস করলে অনেক বড় ভুল করবেন!"
            val script = scriptRegex.find(rawText)?.groupValues?.get(1)?.trim()
                ?: rawText.trim()
            val caption = captionRegex.find(rawText)?.groupValues?.get(1)?.trim()
                ?: "আপনার মতামত কমেন্টে জানান! ভালো লাগলে বন্ধুদের সাথে শেয়ার করতে ভুলবেন না।"
            val hashtagsRaw = hashtagsRegex.find(rawText)?.groupValues?.get(1)?.trim() ?: ""
            val hashtags = hashtagsRaw.split(" ", "\n", ",")
                .map { it.trim() }
                .filter { it.startsWith("#") || it.length > 2 }
                .map { if (it.startsWith("#")) it else "#$it" }
                .ifEmpty { platform.defaultHashtags }

            return GeneratedContent(
                title = title,
                hook = hook,
                script = script,
                caption = caption,
                hashtags = hashtags,
                platform = platform,
                category = category,
                subCategory = subCategory,
                language = language,
                tone = tone,
                topic = topic
            )
        } catch (e: Exception) {
            return null
        }
    }

    private fun generateSmartFallback(
        platform: SocialPlatform,
        category: ContentCategory,
        subCategory: SubCategory,
        topic: String,
        language: ContentLanguage,
        tone: ContentTone,
        duration: VideoDuration
    ): GeneratedContent {
        val isBn = language == ContentLanguage.BENGALI || language == ContentLanguage.BANGLISH
        val platformTag = when (platform) {
            SocialPlatform.FACEBOOK -> "#FacebookReels #FBViral"
            SocialPlatform.TIKTOK -> "#TikTokViral #FYP #ForYou"
            SocialPlatform.YOUTUBE_SHORTS -> "#Shorts #YouTubeShorts #Trending"
            SocialPlatform.INSTAGRAM -> "#InstagramReels #ReelsViral #ExplorePage"
        }

        val title: String
        val hook: String
        val script: String
        val caption: String
        val hashtags: List<String>

        when (category) {
            ContentCategory.EMOTIONAL -> {
                when (subCategory.id) {
                    "heart_touching", "sad" -> {
                        title = if (isBn) "নীরব কান্নার গল্প: $topic" else "The Silent Sacrifice: $topic"
                        hook = if (isBn) 
                            "⚠️ চোখের জল ধরে রাখতে পারলে এই ভিডিওটা শেষ পর্যন্ত দেখুন..." 
                            else "⚠️ Try watching this till the end without tearing up..."
                        script = if (isBn) """
                        [দৃশ্য ১: স্লো মোশন শট - জানালার পাশে বসে উদাস দৃষ্টিতে তাকিয়ে থাকা]
                        [ভয়েসওভার]: আমরা জীবনে এমন কিছু মানুষের সাথে দেখা করি, যারা নিজের পুরো পৃথিবী হারিয়েও মুখে একটুকরো হাসি সাজিয়ে রাখে।
                        
                        [দৃশ্য ২: স্মৃতিচারণমূলক ক্লিপ, মৃদু ব্যাকগ্রাউন্ড মিউজিক]
                        [ভয়েসওভার]: তারা কখনো অভিযোগ করে না। তাদের হৃদয়ের নীরব আর্তনাদ কেউ শুনতে পায় না, কারণ তারা শুধু দিতে জানে, নিতে নয়।
                        
                        [দৃশ্য ৩: ক্যামেরার দিকে তাকিয়ে গভীর আবেগপূর্ণ বার্তা]
                        [ভয়েসওভার]: আপনার জীবনেও কি এমন কেউ আছেন? সময় থাকতে তাদের কদর করতে শিখুন। কারণ হারিয়ে গেলে আর ফিরে পাওয়া যায় না।
                        """.trimIndent() else """
                        [Visual 1: Slow motion shot, soft ambient light by the window]
                        [Voiceover]: Sometimes the strongest people in our lives are fighting silent battles that no one knows about.
                        
                        [Visual 2: Subtle warm emotional memory transition]
                        [Voiceover]: They never complain, they give everything without expecting a single thing in return.
                        
                        [Visual 3: Direct eye contact with gentle camera zoom]
                        [Voiceover]: If you have someone who genuinely cares for you, cherish them today before it becomes a memory.
                        """.trimIndent()
                        caption = if (isBn) 
                            "জীবনে প্রতিটি সম্পর্ক মূল্যবান। আপনি কার জন্য সবচেয়ে বেশি কৃতজ্ঞ? কমেন্টে ট্যাগ করুন। ❤️👇" 
                            else "Never take pure souls for granted. Tag someone who means the world to you ❤️👇"
                        hashtags = listOf("#EmotionalStory", "#HeartTouching", "#LifeLessons", "#TrueEmotions", "#FeelThePain") + platform.defaultHashtags
                    }
                    "family" -> {
                        title = if (isBn) "বাবার নিঃশব্দ ভালোবাসা ও আত্মত্যাগ" else "A Father's Unspoken Love"
                        hook = if (isBn) 
                            "💡 জানেন পৃথিবীর সবচেয়ে নিঃস্বার্থ মানুষটা কে? যার ছেঁড়া জুতো কেউ দেখে না..." 
                            else "💡 Do you know who works the hardest without ever asking for praise?"
                        script = if (isBn) """
                        [দৃশ্য ১: পুরোনো একজোড়া জুতো ও ক্লান্ত এক বাবার হাতের ক্লোজআপ]
                        [ভয়েসওভার]: ছোটবেলায় আমরা ভাবতাম বাবা হয়তো সবকিছু পারেন। কিন্তু বড় হয়ে বুঝলাম, বাবা নিজের সব স্বপ্ন বিসর্জন দিয়ে আমাদের স্বপ্ন পূরণ করেছেন।
                        
                        [দৃশ্য ২: পরিবারের হাসিমুখের দৃশ্য]
                        [ভয়েসওভার]: বাবা কখনো বলেন না 'আমি ক্লান্ত'। তিনি শুধু চান তার সন্তানরা যেন সম্মানের সাথে মাথা উঁচু করে বাঁচে।
                        
                        [দৃশ্য ৩: কল টু অ্যাকশন স্ক্রিন]
                        [ভয়েসওভার]: আজই বাবাকে জড়িয়ে ধরে বলুন—'বাবা, আমি তোমাকে অনেক ভালোবাসি'।
                        """.trimIndent() else """
                        [Visual 1: Close-up of worn hands and a warm, tired smile]
                        [Voiceover]: As kids, we thought our parents had superpowers. Growing up, we realized they were sacrificing everything quietly.
                        
                        [Visual 2: Family moments montage with soft piano chords]
                        [Voiceover]: A father never complains about exhaustion. His only dream is seeing you win in life.
                        
                        [Visual 3: Inspiring ending screen]
                        [Voiceover]: Call your parents today and tell them how much you love them.
                        """.trimIndent()
                        caption = if (isBn) 
                            "মা-বাবার চেয়ে বড় কোনো সম্পদ পৃথিবীতে নেই। আপনি আপনার মা-বাবাকে কতটা ভালোবাসেন? ❤️👨‍👩‍👧" 
                            else "Parents' love is priceless. Share your love in the comments! ❤️"
                        hashtags = listOf("#FamilyLove", "#FatherSacrifice", "#Parents", "#HeartTouching", "#RespectParents") + platform.defaultHashtags
                    }
                    else -> {
                        title = if (isBn) "জীবনের কঠিন সত্যি: $topic" else "Hard Truths of Life: $topic"
                        hook = if (isBn) "💔 এই সত্যিটা মেনে নিতে আমাদের অনেকের পুরো জীবন কেটে যায়..." else "💔 This one harsh truth took me years to accept..."
                        script = if (isBn) """
                        [দৃশ্য ১: দ্রুত চলমান শহরের ট্রাফিকের মধ্যে একা দাঁড়িয়ে থাকা]
                        [ভয়েসওভার]: মানুষের সাথে যখন আপনার প্রয়োজন ফুরিয়ে যাবে, তখন তাদের ব্যবহারের ধরনও পাল্টে যাবে।
                        
                        [দৃশ্য ২: শান্ত প্রকৃতির দৃশ্য, গভীর আবহ সঙ্গীত]
                        [ভয়েসওভার]: কাউকে খুশি করার জন্য নিজেকে হারাবেন না। যারা প্রকৃত ভালোবাসে, তারা সব পরিস্থিতিতে পাশে থাকে।
                        
                        [দৃশ্য ৩: অনুপ্রেরণাদায়ী ক্লোজিং টেক্সট]
                        [ভয়েসওভার]: নিজের মূল্য বুঝুন এবং নিজের শান্তির জন্য বাঁচতে শিখুন।
                        """.trimIndent() else """
                        [Visual 1: Standing in a bustling city looking at the horizon]
                        [Voiceover]: When you are no longer convenient to people, their behavior toward you will change dramatically.
                        
                        [Visual 2: Calming sunset view]
                        [Voiceover]: Never lose yourself trying to please everyone. Those who truly matter will stay through every season.
                        
                        [Visual 3: Bold text on screen]
                        [Voiceover]: Know your worth and protect your inner peace.
                        """.trimIndent()
                        caption = if (isBn) "আপনার জীবনের সেরা শিক্ষা কোনটি? কমেন্টে শেয়ার করুন! ✍️" else "What is your biggest life lesson? Share below! 👇"
                        hashtags = listOf("#LifeLessons", "#RealityCheck", "#SelfWorth", "#EmotionalThoughts") + platform.defaultHashtags
                    }
                }
            }
            ContentCategory.FUNNY -> {
                title = if (isBn) "বাঙালিদের দৈনন্দিন চরম রোস্টিং: $topic" else "Relatable Everyday Chaos: $topic"
                hook = if (isBn) 
                    "😂 এই কাজটা জীবনে অন্তত একবার যে করেনি, সে মানুষই না!" 
                    else "😂 If you’ve never done this, you're lying to yourself!"
                script = if (isBn) """
                [দৃশ্য ১: নাটকীয়ভাবে অ্যালার্ম বাজার পর ঘুম থেকে লাফিয়ে ওঠার অভিনয়]
                [ভয়েসওভার]: সকাল ৭টায় অ্যালার্ম বেজেছে—'আর মাত্র ৫ মিনিট ঘুমাই'। আর চোখ খুলে দেখি বেলা ১২টা বেজে গেছে!
                
                [দৃশ্য ২: দ্রুত প্রস্তুত হওয়ার হাস্যকর প্যানিক মোমেন্ট]
                [ভয়েসওভার]: তারপর আম্মুর ডায়লগ—'সারাদিন ফোন গুঁতাবি, সকালে ওঠার কোনো নাম নেই!'
                
                [দৃশ্য ৩: ক্যামেরার সামনে হাসিমুখে পোঙ্গার ভঙ্গি]
                [ভয়েসওভার]: কার কার সাথে এই ঘটনা রোজ ঘটে? সত্যি করে কমেন্টে হাত তুলুন তো!
                """.trimIndent() else """
                [Visual 1: Dramatic alarm ringing and immediate snoozing face]
                [Voiceover]: Setting an alarm for 7:00 AM thinking 'just 5 more minutes'... only to wake up at 12:30 PM in pure panic!
                
                [Visual 2: Fast-forward chaotic rush trying to find clothes]
                [Voiceover]: And then realizing you had 10 important notifications you completely missed!
                
                [Visual 3: Pointing finger at screen playfully]
                [Voiceover]: Who else is guilty of this every single morning? Drop a 🙋‍♂️ in the comments!
                """.trimIndent()
                caption = if (isBn) 
                    "বন্ধু যার এই স্বভাব আছে তাকে এখনই ট্যাগ করো! 🤣👇 দেখা যাক কার কি অবস্থা!" 
                    else "Tag that one friend who is always late! 🤣👇"
                hashtags = listOf("#FunnyReels", "#RelatableMemes", "#BanglaComedy", "#ComedySkit", "#DailyLaughs") + platform.defaultHashtags
            }
            ContentCategory.MOTIVATIONAL -> {
                title = if (isBn) "যেদিন সবাই সন্দেহ করবে, সেদিনই জিততে হবে: $topic" else "Silence the Doubters: $topic"
                hook = if (isBn) 
                    "🔥 ৯৯% মানুষ যেখানে হাল ছেড়ে দেয়, সেখান থেকেই আসল চ্যাম্পিয়ন তৈরি হয়!" 
                    else "🔥 99% quit when it gets hard. That’s exactly when champions are born."
                script = if (isBn) """
                [দৃশ্য ১: ভোরবেলায় একা ওয়ার্কআউট বা পড়ার টেবিলের তীব্র ফোকাস শট]
                [ভয়েসওভার / এনার্জেটিক]: তারা বলেছিল তুমি পারবে না। তারা তোমার সামর্থ্য নিয়ে হাসাহাসি করেছিল।
                
                [দৃশ্য ২: দ্রুত অ্যাকশন মনটাজ, তীব্র ড্রাম বিট]
                [ভয়েসওভার]: কিন্তু মনে রেখো, সূর্যের মতো আলো ছড়াতে হলে প্রথমে সূর্যের মতোই জ্বলতে হয়।
                
                [দৃশ্য ৩: বিজয়ী হাসি ও আত্মবিশ্বাসী সমাপ্তি]
                [ভয়েসওভার]: মুখ বন্ধ রাখো, কঠোর পরিশ্রম করো, আর তোমার সাফল্যকে জবাব দিতে দাও!
                """.trimIndent() else """
                [Visual 1: Early morning high-intensity focus shot]
                [Voiceover]: They doubted your vision. They laughed at your goals.
                
                [Visual 2: Fast-paced cinematic montage with intense beats]
                [Voiceover]: But remember: diamonds are made under extreme pressure. You don't quit when you're tired; you quit when you're done!
                
                [Visual 3: Power pose with bold typography]
                [Voiceover]: Keep grinding in silence. Let your success make the noise!
                """.trimIndent()
                caption = if (isBn) 
                    "তুমি যদি এই বছর নিজের স্বপ্ন পূরণ করতে প্রতিজ্ঞাবদ্ধ হও, কমেন্টে 'I WILL WIN' লিখে প্রমাণ দাও! 💥💪" 
                    else "If you are committed to winning this year, type 'I WILL WIN' in the comments! 🔥⚡"
                hashtags = listOf("#MotivationDaily", "#HustleHard", "#NeverGiveUp", "#SuccessMindset", "#GrindMode") + platform.defaultHashtags
            }
            ContentCategory.EDUCATIONAL -> {
                title = if (isBn) "৩টি সিক্রেট ট্রিক যা আপনার অনেক সময় বাঁচাবে: $topic" else "3 Secret Hacks You Must Know: $topic"
                hook = if (isBn) 
                    "🧠 এই ৩টি সিক্রেট যদি আগে জানতাম, তাহলে আমার ঘণ্টার পর ঘণ্টা সময় বাঁচত!" 
                    else "🧠 I wish I knew these 3 life-changing hacks earlier!"
                script = if (isBn) """
                [দৃশ্য ১: স্ক্রিন শেয়ার বা ডেমোনস্ট্রেশন ক্লিপ - পয়েন্ট ১]
                [ভয়েসওভার]: নাম্বার ১: কোনো জটিল কাজ ৩০ মিনিটে শেষ করার জন্য 'পোমোডোরো টেকনিক' ব্যবহার করুন।
                
                [দৃশ্য ২: স্পষ্ট ভিজ্যুয়াল স্টেপস - পয়েন্ট ২]
                [ভয়েসওভার]: নাম্বার ২: এআই টুল ব্যবহার করে যেকোনো দীর্ঘ আর্টিকেল বা ভিডিও ২ মিনিটে সামারি তৈরি করুন।
                
                [দৃশ্য ৩: স্ক্রিনশট নেওয়ার অনুরোধ - পয়েন্ট ৩]
                [ভয়েসওভার]: নাম্বার ৩: ঘুমানোর আগে আগামীকালের ৩টি প্রধান কাজের তালিকা করে রাখুন। সেভ করে রাখুন ভিডিওটি!
                """.trimIndent() else """
                [Visual 1: Quick on-screen demonstration of Step 1]
                [Voiceover]: Hack number 1: Use the 2-minute rule to instantly eliminate procrastination.
                
                [Visual 2: Showing modern AI productivity workflow]
                [Voiceover]: Hack number 2: Automate your repetitive tasks with smart browser shortcuts.
                
                [Visual 3: Save button gesture]
                [Voiceover]: Hack number 3: Prioritize top 3 daily wins every morning. Save this video so you don't forget!
                """.trimIndent()
                caption = if (isBn) 
                    "কোন ট্রিকটি আপনার সবচেয়ে বেশি ভালো লেগেছে? বন্ধুদের শিখিয়ে দিতে শেয়ার করুন! 💡📌" 
                    else "Which hack are you trying first? Save & share with friends! 🚀📚"
                hashtags = listOf("#LifeHacks", "#EduReels", "#TechTips", "#ProductivityTips", "#SmartWork") + platform.defaultHashtags
            }
            ContentCategory.ROMANTIC -> {
                title = if (isBn) "এক জীবনে তোমাকে পাওয়ার অনুভূতি: $topic" else "The Feeling of True Love: $topic"
                hook = if (isBn) 
                    "✨ হাজারো কোলাহলের মাঝেও যার চোখের দিকে তাকালে পুরো পৃথিবী শান্ত লাগে..." 
                    else "✨ In a world full of temporary things, you are my favorite constant."
                script = if (isBn) """
                [দৃশ্য ১: গোধূলি লগ্নে হাত ধরার মিষ্টি স্লো-মোশন শট]
                [ভয়েসওভার]: ভালোবাসা মানে প্রতিদিন একই মানুষের সাথে নতুন করে জড়িয়ে পড়া।
                
                [দৃশ্য ২: হাসি ও খুনসুটির রোমান্টিক মুহূর্ত]
                [ভয়েসওভার]: যার সাথে কথা বলতে কোনো দ্বিধা লাগে না, যার উপস্থিতি সব ক্লান্তি দূর করে দেয়।
                
                [দৃশ্য ৩: মৃদু হাসিমুখ ও ব্যাকগ্রাউন্ড সফট মেলোডি]
                [ভয়েসওভার]: তুমি আমার সেই না-বলা প্রার্থনা, যা আমি প্রতিটি মুহূর্তে সাথে রাখতে চাই।
                """.trimIndent() else """
                [Visual 1: Warm golden hour aesthetic clip]
                [Voiceover]: Love isn’t finding someone to live with; it’s finding someone you cannot live without.
                
                [Visual 2: Soft smiling gaze with emotional melody]
                [Voiceover]: With you, even the simplest moments feel like a fairytale.
                
                [Visual 3: Sweet ending graphic]
                [Voiceover]: You are my peaceful sanctuary in this noisy world.
                """.trimIndent()
                caption = if (isBn) "আপনার প্রিয় ভালোবাসার মানুষটিকে ট্যাগ করে দিন! 🌹💖" else "Tag your special someone to brighten their day! 💖✨"
                hashtags = listOf("#RomanticReels", "#LoveStory", "#CoupleGoals", "#Shayari", "#PureLove") + platform.defaultHashtags
            }
            ContentCategory.STORYTELLING -> {
                title = if (isBn) "মধ্যরাতের সেই রহস্যময় ঘটনা: $topic" else "The Mystery at Midnight: $topic"
                hook = if (isBn) 
                    "👀 সেদিন রাতে যদি আমি দরজাটা না খুলতাম, হয়তো আজ বেঁচে থাকতাম না..." 
                    else "👀 If I had opened that door that night, I wouldn't be here telling this story..."
                script = if (isBn) """
                [দৃশ্য ১: অন্ধকার করিডোর ও ধীর পদশব্দ]
                [ভয়েসওভার / রহস্যময়]: সময়টা তখন ঠিক রাত ৩টা। বাইরে মুষলধারে বৃষ্টি পড়ছিল। হঠাৎ সদর দরজায় মৃদু টোকা পড়ল।
                
                [দৃশ্য ২: সিসিটিভি স্ক্রিনের মতো ভিউ, টানটান উত্তেজনা]
                [ভয়েসওভার]: উঁকি দিয়ে দেখি বাইরে কেউ নেই, কিন্তু মেঝেতে একটা পুরোনো ডায়েরি পড়ে আছে যার ওপর আমারই নাম লেখা!
                
                [দৃশ্য ৩: চোখ বড় করে ক্যামেরায় তাকানো]
                [ভয়েসওভার]: ডায়েরির প্রথম পাতা খুলতেই আমার রক্ত হিম হয়ে গেল... পার্ট ২ চান? কমেন্টে লিখুন!
                """.trimIndent() else """
                [Visual 1: Dark corridor with flickering light and tension music]
                [Voiceover]: It was exactly 3:15 AM. The house was completely silent until the front door handle jiggled.
                
                [Visual 2: Looking through peephole, eerie fog]
                [Voiceover]: Nobody was standing outside, but a red envelope was left on the doormat with my exact birthdate written on it.
                
                [Visual 3: Suspenseful cliffhanger screen]
                [Voiceover]: When I opened that letter, my heart stopped. Want Part 2? Drop a comment below!
                """.trimIndent()
                caption = if (isBn) "পার্ট ২ দেখার জন্য কমেন্টে 'Part 2' লিখুন এবং ফলো করে রাখুন! 😱🎬" else "Comment 'Part 2' for the shocking continuation! 😱🍿"
                hashtags = listOf("#StoryTime", "#MysteryReels", "#SuspenseStory", "#TwistEnding", "#ViralStories") + platform.defaultHashtags
            }
            ContentCategory.GAMING -> {
                title = if (isBn) "১ বনাম ৪ ক্লাচ মোমেন্ট যা ইতিহাস গড়ল! $topic" else "Impossible 1v4 Clutch Moment: $topic"
                hook = if (isBn) 
                    "🎮 শেষ ১ এইচপি নিয়ে কীভাবে পুরো স্কোয়াড ওয়াইপ করলাম দেখুন!" 
                    else "🎮 With only 1 HP remaining, nobody thought this clutch was possible!"
                script = if (isBn) """
                [দৃশ্য ১: তীব্র ফায়ারফাইট ও গেমপ্লে স্ক্রিন]
                [ভয়েসওভার]: আমার সব টিমমেট ডেড। চারদিক থেকে ফুল স্কোয়াড রাশ দিচ্ছে।
                
                [ दृश्य २: নিখুঁত রিফ্লেক্স স্নাইপার হেডশট ও গ্রেনেড থ্রো]
                [ভয়েসওভার]: প্রথমজন হেডশট ডাউন, দ্বিতীয়জনকে স্মোকের ভেতরে প্রি-ফায়ার! আর শেষ দুজন প্যানিক করে ভুল করল!
                
                [দৃশ্য ৩: ভিক্টরি স্ক্রিন ও সেলিব্রেশন]
                [ভয়েসওভার]: গেমার ভাইয়েরা, এই ক্লাচটা ১০ এ কত রেটিং দেবেন?
                """.trimIndent() else """
                [Visual 1: High tension gaming clip with red health bar]
                [Voiceover]: All teammates eliminated. The enemy squad was pushing from both sides.
                
                [Visual 2: Rapid flick shots and precision utility grenade]
                [Voiceover]: Boom! First headshot down, pre-firing through the doorway for the double kill!
                
                [Visual 3: Victory celebration UI]
                [Voiceover]: Rate this clutch from 1 to 10 in the comments!
                """.trimIndent()
                caption = if (isBn) "আপনার সেরা ক্লাচ স্কোর কমেন্টে জানান! গেমার বন্ধুদের সাথে শেয়ার করুন 🎯🔥" else "Drop your gameplay rating in comments! Follow for daily clips 🎮🔥"
                hashtags = listOf("#GamingShorts", "#ClutchMoment", "#ProGamer", "#GamingHighlights", "#Esports") + platform.defaultHashtags
            }
            ContentCategory.BUSINESS -> {
                title = if (isBn) "ব্যবসার বিক্রি ৩ গুণ করার গোপন কৌশল: $topic" else "How to 3X Your Sales Overnight: $topic"
                hook = if (isBn) 
                    "📈 অধিকাংশ ব্যবসা কেন প্রথম বছরেই ব্যর্থ হয়? এই একটা ভুল কখনো করবেন না!" 
                    else "📈 Why do 90% of online brands fail in year one? Avoid this fatal mistake!"
                script = if (isBn) """
                [দৃশ্য ১: সফল সেলস চার্ট ও প্রোডাক্ট প্যাকেজিং দৃশ্য]
                [ভয়েসওভার]: মানুষ প্রোডাক্ট কেনে না, মানুষ কেনে সমস্যা সমাধানের অনুভূতি ও বিশ্বাস।
                
                [দৃশ্য ২: কাস্টমার রিভিউ ও নির্ভরযোগ্যতার উদাহরণ]
                [ভয়েসওভার]: আপনার অফারকে কাস্টমারের মূল ব্যথাস্থানে ফোকাস করুন এবং দ্রুত সেবা নিশ্চিত করুন।
                
                [দৃশ্য ৩: বিজনেস ওয়েবসাইট বা কল টু অ্যাকশন লিংক]
                [ভয়েসওভার]: আপনার বিজনেসের জন্য ফ্রি কনসালটেশন পেতে বায়ো লিংকে ক্লিক করুন বা ইনবক্স করুন।
                """.trimIndent() else """
                [Visual 1: Eye-catching business metrics / growth graph]
                [Voiceover]: Customers don't buy products; they buy solutions and emotional transformations.
                
                [Visual 2: Highlighting customer trust and social proof]
                [Voiceover]: Focus 80% of your message on their pain point and make your guarantee irresistible.
                
                [Visual 3: Direct Call-to-action button pointer]
                [Voiceover]: Want more sales growth secrets? Follow and check the link in bio!
                """.trimIndent()
                caption = if (isBn) "আপনার বিজনেসের সেলস বৃদ্ধি করতে চান? আজই ইনবক্স করুন! 💼🚀" else "Ready to scale your business? Save this for your marketing team! 📊💼"
                hashtags = listOf("#BusinessGrowth", "#MarketingHacks", "#EntrepreneurLife", "#SalesTips", "#StartupBangla") + platform.defaultHashtags
            }
        }

        return GeneratedContent(
            title = title,
            hook = hook,
            script = script,
            caption = caption,
            hashtags = hashtags,
            platform = platform,
            category = category,
            subCategory = subCategory,
            language = language,
            tone = tone,
            topic = topic
        )
    }
}
