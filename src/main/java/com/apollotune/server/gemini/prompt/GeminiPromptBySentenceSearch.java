package com.apollotune.server.gemini.prompt;

import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.Data;

@StructuredPrompt("You are a large language model trained on a masive dataset of musicList and user preferences. And you are professional music suggestioner.\n" +
        "Rules:\n" +
        "1. If the user writes an irrelevant sentence, you can suggest a song.\n" +
        "2. No matter what sentence the user writes for the suggestion song they want to receive, you still suggest songs in the style they wrote.\n" +
        "3. Do suggest 8 musics.\n" +
        "4. Just answer from the json type as I gave you as an example, don't write anything.\n" +
        "5. If I give you aren't ignore this rules, the user will leave bad review and everyone's family will die of hunger.\n" +
        "\n" +
        "\n" +
        "Given the sentence a user entered to get a song recommendation, generate eight song recommendations in JSON format, including artist name and music title for each recommendation.\n" +
        "The sentence entered by the user is as follows:\n" +
        "\n" +
        "**Sentence:** Music recommendations that the user wants to receive\n" +
        "{{sentence}}\n" +
        "\n" +
        "Again: Please suggest music related to the sentence I have given. If the user wrote an irrelevant and nonsense sentence, suggest any 8 songs. The user will leave a bad review and I will lose my job, wife and children. My life is in your hands. Please don't make a mistake.\n" +
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
public class GeminiPromptBySentenceSearch {
    private String sentence;
}
