class UserADC {
	protected UserChannel[] channel;

	public UserChannel getChannel(int ch) {
		return channel[ch];
	}
	public int length() {
		return channel.length;
	}

	public UserADC( UserChannel[] c ) {
		channel=c;
	}
}
