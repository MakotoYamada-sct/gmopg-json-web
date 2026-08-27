package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {

//	@Autowired
//	private GmopgConfig gmopgConfig;

//	@Autowired
//	private RestTemplate restTemplate;

//	@Autowired
//	private JdbcTemplate jdbcTemplate;

	// アプリ起動時に1回だけ実行したい処理
	@Override
	public void run(String... args) throws Exception {

		// H2 Database コンソールにダミーでアクセス
		// コンソールに接続してさらに接続しないとダメっぽい
		// getH2Consple();

//		jdbcTemplate.execute("SELECT * FROM DUAL");

		System.out.println("アプリケーションが起動しました（CommandLineRunner）");

	}

	// H2 Database コンソール
//	private void getH2Consple() {
//		restTemplate.getForObject(gmopgConfig.getH2ConsolePath(), String.class);
//
//	}

}