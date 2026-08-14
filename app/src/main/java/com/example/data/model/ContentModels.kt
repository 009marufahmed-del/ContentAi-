package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class SocialPlatform(
    val id: String,
    val title: String,
    val titleBn: String,
    val brandColor: Color,
    val secondaryColor: Color,
    val aspectDescription: String,
    val defaultHashtags: List<String>
) {
    FACEBOOK(
        id = "facebook",
        title = "Facebook",
        titleBn = "ফেসবুক",
        brandColor = Color(0xFF1877F2),
        secondaryColor = Color(0xFF4267B2),
        aspectDescription = "Reels & Post",
        defaultHashtags = listOf("#FacebookReels", "#FBViral", "#TrendingNow", "#ViralVideo")
    ),
    TIKTOK(
        id = "tiktok",
        title = "TikTok",
        titleBn = "টিকটক",
        brandColor = Color(0xFFEE1D52),
        secondaryColor = Color(0xFF69C9D0),
        aspectDescription = "9:16 Short Video",
        defaultHashtags = listOf("#TikTokViral", "#FYP", "#ForYou", "#Trending", "#TikTokBangla")
    ),
    YOUTUBE_SHORTS(
        id = "youtube_shorts",
        title = "YouTube Shorts",
        titleBn = "ইউটিউব শর্টস",
        brandColor = Color(0xFFFF0000),
        secondaryColor = Color(0xFFCC0000),
        aspectDescription = "Vertical 60s",
        defaultHashtags = listOf("#Shorts", "#YouTubeShorts", "#ViralShorts", "#YTShortsBangla")
    ),
    INSTAGRAM(
        id = "instagram",
        title = "Instagram",
        titleBn = "ইনস্টাগ্রাম",
        brandColor = Color(0xFFE1306C),
        secondaryColor = Color(0xFF833AB4),
        aspectDescription = "Reels & Feed",
        defaultHashtags = listOf("#InstagramReels", "#InstaDaily", "#ExplorePage", "#ReelsViral")
    )
}

enum class ContentCategory(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val icon: ImageVector,
    val accentColor: Color,
    val subCategories: List<SubCategory>
) {
    FUNNY(
        id = "funny",
        titleEn = "Funny",
        titleBn = "ফানি",
        icon = Icons.Default.Mood,
        accentColor = Color(0xFFFFB703),
        subCategories = listOf(
            SubCategory("memes", "Memes & Trends", "মিমস ও ট্রেন্ডস"),
            SubCategory("relatable", "Relatable Everyday", "দৈনন্দিন মজার ঘটনা"),
            SubCategory("skit", "Comedy Skit / Prank", "কমেডি ড্রামা / প্র্যাংক"),
            SubCategory("roast", "Sarcasm & Roast", "মজার রোস্টিং"),
            SubCategory("standup", "Punchlines & Jokes", "জোকস ও পাঞ্চলাইন"),
            SubCategory("bangla_comedy", "Bangladeshi Satire", "বাংলা কমেডি")
        )
    ),
    EMOTIONAL(
        id = "emotional",
        titleEn = "Emotional",
        titleBn = "ইমোশনাল",
        icon = Icons.Default.Favorite,
        accentColor = Color(0xFFE63946),
        subCategories = listOf(
            SubCategory("heart_touching", "Heart Touching", "হার্ট টাচিং"),
            SubCategory("sad", "Sad & Painful", "স্যাড"),
            SubCategory("family", "Family & Parents", "ফ্যামিলি"),
            SubCategory("friendship", "Friendship", "ফ্রেন্ডশিপ"),
            SubCategory("love", "Love & Heartbreak", "লাভ"),
            SubCategory("life_lesson", "Life Lessons", "লাইফ লেসন")
        )
    ),
    ROMANTIC(
        id = "romantic",
        titleEn = "Romantic",
        titleBn = "রোমান্টিক",
        icon = Icons.Default.Favorite,
        accentColor = Color(0xFFFF4D6D),
        subCategories = listOf(
            SubCategory("cute_couples", "Cute Moments", "মিষ্টি ভালোবাসার মুহূর্ত"),
            SubCategory("deep_love", "Deep Affection", "গভীর ভালোবাসা"),
            SubCategory("long_distance", "Long Distance", "দূরত্বের ভালোবাসা"),
            SubCategory("shayari", "Poetic / Shayari", "রোমান্টিক কবিতা ও শায়েরী"),
            SubCategory("proposal", "Anniversary / Proposal", "প্রপোজাল ও স্মৃতি")
        )
    ),
    MOTIVATIONAL(
        id = "motivational",
        titleEn = "Motivational",
        titleBn = "মোটিভেশনাল",
        icon = Icons.AutoMirrored.Filled.TrendingUp,
        accentColor = Color(0xFF2A9D8F),
        subCategories = listOf(
            SubCategory("hustle", "Hustle & Hard Work", "পরিশ্রম ও লক্ষ্য"),
            SubCategory("never_give_up", "Never Give Up", "কখনও হাল ছেড়ো না"),
            SubCategory("mindset", "Mindset & Focus", "শক্তিশালী মাইন্ডসেট"),
            SubCategory("failure", "Overcoming Failure", "ব্যর্থতা জয় করার গল্প"),
            SubCategory("student", "Student & Career Boost", "ছাত্র-ছাত্রীদের অনুপ্রেরণা")
        )
    ),
    EDUCATIONAL(
        id = "educational",
        titleEn = "Educational",
        titleBn = "এডুকেশনাল",
        icon = Icons.Default.School,
        accentColor = Color(0xFF3A86FF),
        subCategories = listOf(
            SubCategory("tips_hacks", "Quick Tips & Hacks", "দরকারী টিপস ও ট্রিকস"),
            SubCategory("facts", "Did You Know / Facts", "অজানা তথ্য ও রহস্য"),
            SubCategory("tech_ai", "Tech & AI Explainer", "প্রযুক্তি ও এআই"),
            SubCategory("money", "Finance & Money", "অর্থ উপার্জন ও বিজনেস"),
            SubCategory("self_dev", "Skill Development", "দক্ষতা বৃদ্ধি")
        )
    ),
    STORYTELLING(
        id = "storytelling",
        titleEn = "Storytelling",
        titleBn = "স্টোরিটেলিং",
        icon = Icons.AutoMirrored.Filled.MenuBook,
        accentColor = Color(0xFF8338EC),
        subCategories = listOf(
            SubCategory("suspense", "Suspense & Mystery", "সাসপেন্স ও রহস্য"),
            SubCategory("real_life", "Real Life Story", "বাস্তব জীবনের গল্প"),
            SubCategory("horror", "Horror & Spooky", "ভৌতিক গল্প"),
            SubCategory("moral", "Moral Story", "শিক্ষণীয় গল্প"),
            SubCategory("twist", "Unexpected Twist", "টুইস্ট ও চমক")
        )
    ),
    GAMING(
        id = "gaming",
        titleEn = "Gaming",
        titleBn = "গেমিং",
        icon = Icons.Default.Gamepad,
        accentColor = Color(0xFF06D6A0),
        subCategories = listOf(
            SubCategory("clutch", "Clutch Moments", "ক্লাচ ও হাইলাইটস"),
            SubCategory("pro_tips", "Pro Gameplay Tips", "প্রো প্লেয়ার টিপস"),
            SubCategory("funny_moments", "Funny Fails & Bugs", "গেমিং ফানি মোমেন্ট"),
            SubCategory("review", "New Game Review", "গেম রিভিউ ও আপডেট"),
            SubCategory("reaction", "Live Stream Hype", "লাইভ রিঅ্যাকশন")
        )
    ),
    BUSINESS(
        id = "business",
        titleEn = "Business",
        titleBn = "বিজনেস",
        icon = Icons.Default.BusinessCenter,
        accentColor = Color(0xFFFB8500),
        subCategories = listOf(
            SubCategory("product", "Product Showcase", "প্রোডাক্ট শোকেস"),
            SubCategory("trust", "Customer Testimonials", "গ্রাহক সন্তুষ্টি ও বিশ্বাস"),
            SubCategory("sales", "Sales Pitch & Offer", "অফার ও ডিসকাউন্ট ঘোষণা"),
            SubCategory("founder", "Founder Journey", "উদ্যোক্তার পথচলা"),
            SubCategory("marketing", "Marketing Strategy", "মার্কেটিং কৌশল")
        )
    )
}

data class SubCategory(
    val id: String,
    val titleEn: String,
    val titleBn: String
)

enum class ContentLanguage(
    val id: String,
    val labelEn: String,
    val labelBn: String
) {
    BENGALI("bn", "Bengali", "বাংলা"),
    ENGLISH("en", "English", "English"),
    BANGLISH("banglish", "Banglish", "বাংলিশ (বাংলা-ইংরেজি)"),
    HINDI("hi", "Hindi", "हिंदी")
}

enum class ContentTone(
    val id: String,
    val labelEn: String,
    val labelBn: String,
    val emoji: String
) {
    VIRAL_ENGAGING("viral", "Viral & Engaging", "ভাইরাল ও আকর্ষক", "🔥"),
    ENERGETIC("energetic", "Energetic & Hyped", "উচ্ছ্বসিত ও এনার্জেটিক", "⚡"),
    DRAMATIC("dramatic", "Dramatic & Intense", "নাটকীয় ও গম্ভীর", "🎭"),
    CASUAL("casual", "Casual & Friendly", "সহজ ও বন্ধুত্বপূর্ণ", "😊"),
    POETIC("poetic", "Poetic & Deep", "কাব্যিক ও গভীর", "✨"),
    PROFESSIONAL("professional", "Professional & Crisp", "প্রফেশনাল ও মার্জিত", "💼"),
    HUMOROUS("humorous", "Humorous & Witty", "রসিকতাপূর্ণ ও মজার", "😂")
}

enum class VideoDuration(
    val id: String,
    val label: String,
    val seconds: Int
) {
    SEC_15("15s", "15s (Short & Punchy)", 15),
    SEC_30("30s", "30s (Recommended)", 30),
    SEC_60("60s", "60s (In-depth)", 60)
}

data class GeneratedContent(
    val id: Long = 0,
    val title: String,
    val hook: String,
    val script: String,
    val caption: String,
    val hashtags: List<String>,
    val platform: SocialPlatform,
    val category: ContentCategory,
    val subCategory: SubCategory,
    val language: ContentLanguage,
    val tone: ContentTone,
    val topic: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
) {
    fun getFullTextForSharing(): String {
        return buildString {
            append("📌 [${platform.title} Content Draft]\n\n")
            append("🎯 TITLE:\n$title\n\n")
            append("🪝 HOOK (First 3s):\n$hook\n\n")
            append("📜 SCRIPT / BODY:\n$script\n\n")
            append("✍️ CAPTION:\n$caption\n\n")
            append("🏷️ HASHTAGS:\n${hashtags.joinToString(" ")}")
        }
    }
}
