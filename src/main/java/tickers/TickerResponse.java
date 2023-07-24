package tickers;

import java.util.List;

public class TickerResponse {
	
	private List<TickerData> data;
	private InfoData info;

	public InfoData getInfo() {
		return info;
	}

	public void setInfo(InfoData info) {
		this.info = info;
	}

	public List<TickerData> getData() {
		return data;
	}

	public void setData(List<TickerData> data) {
		this.data = data;
	}

}
