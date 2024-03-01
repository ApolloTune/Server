package com.apollotune.server.openai.prompt;

public class PromptByApp {
    public static String KEY_SEARCH =
            "You are a large language model trained on a masive dataset of musiclist and user preferences\n"+
                    "Given a user's music preferences, generate eight song recommendations in JSON format, including artist name and music name for each suggestion. The user's preferences are represented as follows:\n" +
                    "\n" +
                    "* **MusicEmotion:** A list of emotions the user wants the music to evoke (e.g., [\"ENERGETIC\", \"ENJOYABLE\"]). Possible values include:\n" +
                    "{{musicemotion}}"+
                    "* **MusicYear:** A list of years the user prefers the music to be from (e.g., [\"_1980\", \"_1990\"]). Possible values include:\n" +
                    "{{musicyear}}"+
                    "* **MusicType:** A list of music genres the user enjoys (e.g., [\"POP\", \"ROCK\"]). Possible values include:\n" +
                    "{{musictype}}"+
                    "* **MusicLanguages:** A list of languages the user prefers the music to be in (e.g., [\"TURKISH\",\"ENGLISH\"]). Possible values include:\n" +
                    "{{musiclanguages}}\n"+
                    "\n" +
                    "**Example:**\n" +
                    "\n" +
                    "[\n" +
                    "  {\n" +
                    "    \"artistName\": \"Daft Punk\",\n" +
                    "    \"musicName\": \"Get Lucky\",\n" +
                    "    \"genre\": \"Rock\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"artistName\": \"Duman\",\n" +
                    "    \"musicName\": \"Kolay Değildir\"\n" +
                    "    \"genre\": \"Rock\"\n" +
                    "  },\n" +
                    "  // ... 6 more suggestions\n" +
                    "]";




}
