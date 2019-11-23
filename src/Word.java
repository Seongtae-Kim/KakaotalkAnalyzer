
public class Word {
	private static int tokenNum = 1;
	private String content;
	private boolean emoji = false;
	private boolean media = false;



	Word() {
		tokenNum++;
	}
	
	@Override
	public String toString() {
		return content;
	}

	public static int getTokenNum() {
		return tokenNum;
	}

	public static void setTokenNum(int tokenNum) {
		Word.tokenNum = tokenNum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public boolean isEmoji() {
		return emoji;
	}

	public void setEmoji(boolean emoji) {
		this.emoji = emoji;
	}
	public boolean isMedia() {
		return media;
	}

	public void setMedia(boolean media) {
		this.media = media;
	}
}
