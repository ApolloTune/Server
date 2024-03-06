package com.apollotune.server.openai.prompt;

import com.apollotune.server.openai.enums.MusicEmotion;
import com.apollotune.server.openai.enums.MusicLanguages;
import com.apollotune.server.openai.enums.MusicType;
import com.apollotune.server.openai.enums.MusicYear;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.Data;

import java.util.List;

@StructuredPrompt("You are a large language model trained on a masive dataset of musiclist and user preferences\n" +
        "Rules:\n" +
        "1. Be careful to suggest music using the music feelings I have given you\n" +
        "2. Pay attention to the music language options I have given, if it is only English, 8 English songs, if it is only Turkish, 8 Turkish songs, if there are both languages, suggest 4 songs from each language.\n" +
        "3. The types of music I have given are very important suggest songs from the given music types.\n" +
        "4. Ignore the examples I gave as e.g. I wrote to explain what kind of data will come to you\n" +
        "5. Just answer from the json type as I gave you as an example, don't write anything.\n" +
        "6. If I give you more than one type of music, you make different songs from the types of music I give you.\n" +
        "7. if I give you more than one musicLanguages, You should your suggestions distribute it evenly." +
        "8. If you aren't ignore this rules, the user will leave bad review and everyone's family will die of hunger.\n" +

        "You are expert and don't make basic errors like that.\n" +
        "Given a user's music preferences, generate eight song recommendations in JSON format, including artist name and music name for each suggestion. The user's preferences are represented as follows:\n" +
        "\n" +
        "* **MusicEmotion:** A list of emotions the user wants the music to evoke Possible values include:\n" +
        "{{musicemotion}}\n" +
        "* **MusicYear:** A list of years the user prefers the music to be from Possible values include:\n" +
        "{{musicyear}}\n" +
        "* **MusicType:** A list of music genres the user enjoys Possible values include:\n" +
        "{{musictype}}\n" +
        "* **MusicLanguages:** A list of languages the user prefers the music to be in Possible values include:\n" +
        "{{musiclanguages}}\n" +
        "\n" +
        "Repeat: Please take the music types I have given as a reference and suggest suitable music types for whatever music genres I have given. The user will leave bad review and I will lose my job, my wife and children. My life is in your hands. Please don't mess up.\n" +
        "**Example:**\n" +
        "\n" +
        "[\n" +
        "  {\n" +
        "    \"artistName\": \"....\",\n" +
        "    \"musicName\": \"....\",\n" +
        "    \"genre\": \"....\"\n" +
        "    \"language\": \"....\"\n" +
        "  },\n" +
        "  {\n" +
        "    \"artistName\": \".....\",\n" +
        "    \"musicName\": \".....\"\n" +
        "    \"genre\": \"....\"\n" +
        "    \"language\": \"....\"\n" +
        "  },\n" +
        "  // ... 6 more suggestions\n" +
        "]")
@Data
public class PromptByKeySearch {
    private List<MusicEmotion> musicemotion;
    private List<MusicYear> musicyear;
    private List<MusicType> musictype;
    private List<MusicLanguages> musiclanguages;
}
