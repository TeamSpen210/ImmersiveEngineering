package blusunrize.immersiveengineering.client.manual.export;

import net.minecraft.util.text.TextFormatting;

import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

/**
 * Converts text containing the §-codes into HTML equivalents.
 */
public class HTMLFormatting
{
	private static final char SECTION = '\u00a7';
	private static final Map<TextFormatting, String> TAGS = new EnumMap<>(TextFormatting.class);
	static {
		TAGS.put(TextFormatting.OBFUSCATED, "obfuscate>");
		TAGS.put(TextFormatting.STRIKETHROUGH, "s>");
		TAGS.put(TextFormatting.UNDERLINE, "u>");
		TAGS.put(TextFormatting.ITALIC, "em>");
		TAGS.put(TextFormatting.BOLD, "strong>");
	}

	public static String convert(String text) {
		if (text.indexOf(SECTION) == -1) {
			return text;
		}
		Stack<TextFormatting> openTags = new Stack<>();
		int colorPos = -1;
		StringBuilder builder = new StringBuilder();
		text = text + SECTION + "r"; // Ensure we always reset everything.
		for(int i = 0; i < text.length(); i++)
		{
			char chrSect = text.charAt(i);
			if (chrSect == SECTION) {
				i++;
				TextFormatting format = TextFormatting.fromFormattingCode(text.charAt(i));
				if (format == null)
				{
					continue;
				}
				String tag = TAGS.get(format);
				if (tag != null) // Non-color
				{
					if (!openTags.contains(format))
					{
						builder.append("<").append(tag);
						openTags.add(format);
					}
				} else if (format == TextFormatting.RESET) {
					for(TextFormatting fmt : openTags)
					{
						if (fmt.isColor()) {
							builder.append("</span>");
						} else {
							builder.append("</").append(TAGS.get(fmt));
						}
					}
					colorPos = -1;
					openTags.clear();
				} else { // Colors.
					// First, close everything to get back to the color.
					if (colorPos != -1) {
						for(int j = openTags.size() - 1; j > colorPos; j--)
							builder.append("</").append(openTags.get(j));
						builder.append("<span class=\"text-color-");
						builder.append(format.getFriendlyName());
						builder.append("\">");
						for(int j = colorPos + 1; j < openTags.size() ; j++)
							builder.append("<").append(openTags.get(j));
					} else {
						colorPos = openTags.size();
						openTags.add(format);
						builder.append("<span class=\"text-color-");
						builder.append(format.getFriendlyName());
						builder.append("\">");
					}
				}
			} else {
				builder.append(chrSect);
			}
		}
		return builder.toString();
	}
}
