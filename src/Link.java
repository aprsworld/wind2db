public abstract class Link {
	public abstract boolean Connect();
	public abstract String getLine();
	public abstract void sendLine(String s);
	public abstract boolean Disconnect();
	public abstract boolean dataReady();
	public abstract void emptyInputBuffer();
}
