package tickers;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;

public class TestTicker {
	private static Response response;
	private static String responseBody;
	private static TickerResponse tickerResponse;

	@BeforeClass
	public void getRequest() {
		response = given().get("https://api.coinlore.net/api/tickers");
		responseBody = response.asString();
		Assert.assertEquals(200, response.statusCode());

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			tickerResponse = objectMapper.readValue(responseBody, TickerResponse.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(priority=1, description ="Filter BTC and ETH assets")
	public void test1() {

		List<TickerData> tickerDataList=tickerResponse.getData();

		System.out.println("-----BTC Assets starts------");
		List<TickerData> btcTickerObjects = tickerDataList.stream().filter(object ->object.getSymbol().equals("BTC")).collect(Collectors.toList());
		btcTickerObjects.stream().forEach(btc -> {
			System.out.println("id:"+btc.getId());
			System.out.println("symbol:"+btc.getSymbol());
			System.out.println("name:"+btc.getName());
			System.out.println("nameid:"+btc.getNameid());
			System.out.println("rank:"+btc.getRank());
			System.out.println("price_usd:"+btc.getPrice_usd());
			System.out.println("percent_change_24h:"+btc.getPercent_change_24h());
			System.out.println("percent_change_1h:"+btc.getPercent_change_1h());
			System.out.println("percent_change_7d:"+btc.getPercent_change_7d());
			System.out.println("price_btc:"+btc.getPrice_btc());
			System.out.println("market_cap_usd:"+btc.getMarket_cap_usd());
			System.out.println("volume24:"+btc.getVolume24());
			System.out.println("volume24a:"+btc.getVolume24a());
			System.out.println("csupply:"+btc.getCsupply());
			System.out.println("tsupply:"+btc.getTsupply());
			System.out.println("msupply:"+btc.getMsupply());
		});
		System.out.println("-----BTC Assets ends------");

		System.out.println("-----ETH Assets starts------");
		List<TickerData> ethTickerObjects = tickerDataList.stream().filter(object ->object.getSymbol().equals("ETH")).collect(Collectors.toList());
		ethTickerObjects.stream().forEach(eth -> {
			System.out.println("id:"+eth.getId());
			System.out.println("symbol:"+eth.getSymbol());
			System.out.println("name:"+eth.getName());
			System.out.println("nameid:"+eth.getNameid());
			System.out.println("rank:"+eth.getRank());
			System.out.println("price_usd:"+eth.getPrice_usd());
			System.out.println("percent_change_24h:"+eth.getPercent_change_24h());
			System.out.println("percent_change_1h:"+eth.getPercent_change_1h());
			System.out.println("percent_change_7d:"+eth.getPercent_change_7d());
			System.out.println("price_btc:"+eth.getPrice_btc());
			System.out.println("market_cap_usd:"+eth.getMarket_cap_usd());
			System.out.println("volume24:"+eth.getVolume24());
			System.out.println("volume24a:"+eth.getVolume24a());
			System.out.println("csupply:"+eth.getCsupply());
			System.out.println("tsupply:"+eth.getTsupply());
			System.out.println("msupply:"+eth.getMsupply());
		});
		System.out.println("-----ETH Assets ends------");
	}

	@Test(priority=2, description ="If BTC price falls below $30,000 and ETH price falls below $15,000 tests should fail")
	public void test2() {

		List<TickerData> tickerDataList=tickerResponse.getData();

		List<TickerData> btcTickerObjects = tickerDataList.stream().filter(object ->object.getSymbol().equals("BTC")).collect(Collectors.toList());
		btcTickerObjects.stream().forEach(btc -> {
			Assert.assertTrue(Double.parseDouble(btc.getPrice_usd())>=30000, "BTC price below $30,000");
		});

		List<TickerData> ethTickerObjects = tickerDataList.stream().filter(object ->object.getSymbol().equals("ETH")).collect(Collectors.toList());
		ethTickerObjects.stream().forEach(eth -> {
			Assert.assertTrue(Double.parseDouble(eth.getPrice_usd())>=15000, "ETH price below $15,000");
		});
	}

	@Test(priority=3, description ="Create and save a JSON document with all assets; whose prices have changed +/- 1%\r\n"
			+ "in the last 24 hours.")
	public void test3() {

		List<TickerData> tickerDataList=tickerResponse.getData();

		List<TickerData> priceChangeTickerDataList = tickerDataList.stream().filter(object ->Double.parseDouble(object.getPercent_change_24h())>=1 || Double.parseDouble(object.getPercent_change_24h())<=-1).collect(Collectors.toList());

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String filteredJson = objectMapper.writeValueAsString(priceChangeTickerDataList);

			// Save the JSON string to a file
			File file = new File(System.getProperty("user.dir") + File.separator +"data"+File.separator+ "percentChange_24h.json");
			objectMapper.writeValue(file, filteredJson);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
